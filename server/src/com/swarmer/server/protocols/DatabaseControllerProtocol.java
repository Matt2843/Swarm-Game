package com.swarmer.server.protocols;

import com.swarmer.server.DatabaseController;
import com.swarmer.server.units.AuthenticationUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.security.PublicKey;
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
			case 1: // Get best quality authentication unit from db.
				getUnitFromDb(message);
				break;
			case 2: // New unit added to architecture, add it to db.
				addNodeToDb(message);
				break;
			case 109: // Authenticate user from forwarded message
				authenticateUserInDatabase(message);
				break;
			case 201: // Create user forwarded message from authentication unit.
				addUserToDatabase(message);
				break;
			case 301: // Ambigious, either get best quality lobby unit randomly or get specific lobby unit. i.e. either "random" or "unit_id" in message.getObject()
				getLobbyUnit(message);
				break;
			case 11111:
				if(exPublicKey != (PublicKey) message.getObject()) {
					exPublicKey = (PublicKey) message.getObject();
					caller.sendMessage(new Message(11111, AuthenticationUnit.KEY.getPublic()));
				}
				break;
			default:
				break;
		}
	}

	private void getLobbyUnit(Message message) throws IOException, SQLException {
		if(message.getObject().equals("random")) {
			getRandomLobbyUnitFromDb("lobby_units");
		} else {
			getLobbyUnitFromDb("lobby_units", (String) message.getObject());
		}
	}

	private void authenticateUserInDatabase(Message message) throws IOException, SQLException {
		String receivedUsername = (String) message.getObject();
		if(userExistsInDatabase(receivedUsername)) {
			ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery("SELECT * FROM users WHERE username=? LIMIT 1", receivedUsername);
			resultSet.next();
			Player player = new Player(resultSet.getString("id"), resultSet.getString("username"), resultSet.getInt("rating"));
			String[] password = new String[] {resultSet.getString("password"), resultSet.getString("password_salt")};
			caller.sendMessage(new Message(997, new Object[]{player, password}));
		} else {
			caller.sendMessage(new Message(997, null));
		}
	}

	private void addUserToDatabase(Message message) throws IOException, SQLException {
		String[] receivedObjects = (String[]) message.getObject();
		if (!userExistsInDatabase(receivedObjects[0])) {
			String id = UUID.randomUUID().toString();
			DatabaseController.mySQLConnection.sqlExecute("INSERT INTO users (id, username, password, password_salt, rating) VALUES (?, ?, ?, ?, ?)", id, receivedObjects[0], receivedObjects[1], receivedObjects[2], "0");
			caller.sendMessage(new Message(998, new Player(id, receivedObjects[0], 0)));
		} else {
			caller.sendMessage(new Message(998, null));
		}
	}

	private void getRandomLobbyUnitFromDb(String unitType) throws SQLException, IOException {
		String sqlQuery = "SELECT ip_address,port FROM " + unitType + " ORDER BY user_count ASC LIMIT 1";
		ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery(sqlQuery);
		resultSet.next();
		String queryResult = resultSet.getString("ip_address") + ":" + resultSet.getString("port");
		caller.sendMessage(new Message(998, queryResult));
	}

	private void getLobbyUnitFromDb(String unitType, String unitID) throws SQLException, IOException {
		String sqlQuery = "SELECT ip_address,port FROM " + unitType + " WHERE id = ?";
		ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery(sqlQuery, unitID);
		resultSet.next();
		String queryResult = resultSet.getString("ip_address") + ":" + resultSet.getString("port");
		caller.sendMessage(new Message(998, queryResult));
	}

	private void getUnitFromDb(Message message) throws IOException, SQLException {
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
		return DatabaseController.mySQLConnection.sqlExecuteQuery("SELECT 1 FROM users WHERE username = ?", username).last();
	}

}
