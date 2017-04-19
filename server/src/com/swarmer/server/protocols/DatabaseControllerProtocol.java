package com.swarmer.server.protocols;

import com.swarmer.server.DatabaseController;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Matt on 04/06/2017.
 */
public class DatabaseControllerProtocol extends ServerProtocol {

	private Connection caller;

	public DatabaseControllerProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	public DatabaseControllerProtocol() {
		super(null);
	}

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
		this.caller = caller;
		System.out.println("Message in mothership protocol: " + message.toString());
		switch (message.getOpcode()) {
			case 1: // Get best quality authentication node from db.
				getNodeFromDb(message);
				break;
			case 2: // New node added to architecture, add it to db.
				addNodeToDb(message);
				break;
			case 109: // Authenticate user from forwarded message
				authenticateUserInDatabase(message);
				break;
			case 201: // Create user forwarded message from authentication node.
				addUserToDatabase(message);
				break;
			default:
				break;
		}
	}

	private void authenticateUserInDatabase(Message message) throws IOException, SQLException {
		String receivedUsername = (String) message.getObject();
		if(userExistsInDatabase(receivedUsername)) {
			ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery("SELECT password,password_salt FROM users WHERE username=? LIMIT 1", receivedUsername);
			resultSet.next();
			String[] result = new String[] {resultSet.getString("password"), resultSet.getString("password_salt")};
			caller.sendMessage(new Message(997, result));
		} else {
			caller.sendMessage(new Message(997, null));
		}
	}


	private void addUserToDatabase(Message message) throws IOException, SQLException {
		String[] receivedObjects = (String[]) message.getObject();
		if (!userExistsInDatabase(receivedObjects[0])) {
			DatabaseController.mySQLConnection.sqlExecute("INSERT INTO users (id, username, password, password_salt, ranking) VALUES (?, ?, ?, ?, ?)", UUID.randomUUID().toString(), receivedObjects[0], receivedObjects[1], receivedObjects[2], "0");
			caller.sendMessage(new Message(998, true));
		} else {
			caller.sendMessage(new Message(998, false));
		}
	}

	private void getNodeFromDb(Message message) throws IOException, SQLException {
		String sqlQuery = "SELECT ip_address,port FROM " + message.getObject() + " ORDER BY user_count ASC LIMIT 1";
		ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery(sqlQuery);
		resultSet.next();
		String queryResult = resultSet.getString("ip_address") + ":" + resultSet.getString("port");
		caller.sendMessage(new Message(999, queryResult));
	}

	private void addNodeToDb(Message message) throws SQLException {
		String[] queryDetails = (String[]) message.getObject();
		String sqlQuery = "INSERT INTO " + queryDetails[1] + " (id, ip_address, port, user_count) VALUES (?, ?, ?, ?)";
		DatabaseController.mySQLConnection.sqlExecute(sqlQuery, UUID.randomUUID().toString(), ((TCPConnection) caller).getConnection().getInetAddress().toString(), queryDetails[0], "0");
	}

	private boolean userExistsInDatabase(String username) throws SQLException, IOException {
		boolean userConnected = DatabaseController.mySQLConnection.sqlExecuteQuery("SELECT 1 FROM users WHERE username = ?", username).last();
		return userConnected;
	}

}
