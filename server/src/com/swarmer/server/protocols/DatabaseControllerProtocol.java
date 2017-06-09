package com.swarmer.server.protocols;

import com.swarmer.server.DatabaseController;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Semaphore;

public class DatabaseControllerProtocol extends ServerProtocol {

	private Connection caller;

	public DatabaseControllerProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	public DatabaseControllerProtocol() {
		super(null);
	}

	private Semaphore semaphore = new Semaphore(1, true);

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException, InterruptedException {
		this.caller = caller;
		System.out.println("Message in mothership protocol: " + message.toString());
		switch (message.getOpcode()) {
			case 0: // Ignore Closing Connections.
				break;
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
				getLobbyUnitFromDb();
				break;
            case 16000:
                getUsersFriendlist(message);
                break;
			case 34788:
				addFriendShipToDatabase(message);
				break;
            case 15000:
                //updateUserCount(message);
				updateUserCounts(message);
                break;
			default:
				super.react(message, caller);
				break;
		}
	}

    private void getUsersFriendlist(Message message) throws SQLException, IOException {
        Player target = (Player) message.getObject();

        ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery("SELECT * FROM friendships WHERE user_id_1 = ? OR user_id_2 = ?", target.getId(), target.getId());

        String[] relations = {};
        caller.sendMessage(new Message(16001, relations));
	}

    private void updateUserCounts(Message message) throws IOException, SQLException, InterruptedException {
		HashMap<LocationInformation, Integer> userCounts = (HashMap<LocationInformation, Integer>) message.getObject();
		String[] allUnits = {"access_units", "authentication_units", "coordination_units", "game_units", "lobby_units"};
		semaphore.acquire();
        for(String string : allUnits) {
            DatabaseController.mySQLConnection.sqlExecute("UPDATE " + string + " SET user_count=0;");
        }
		for(Map.Entry<LocationInformation, Integer> entry : userCounts.entrySet()) {
			DatabaseController.mySQLConnection.sqlExecute("UPDATE " + entry.getKey().getServerUnitDescription() +
																" SET user_count=" + entry.getValue() +
																" WHERE id='" + entry.getKey().getServerUUID() + "';");
		}
		semaphore.release();
		caller.sendMessage(new Message(15001, true));
	}

    private void addFriendShipToDatabase(Message message) throws SQLException, IOException {
		// TODO: Add friendship to database message contains String[] {User1, User2}
		//DatabaseController.mySQLConnection.sqlExecute("INSERT INTO friendships ()");
		Player player1 = ((Player[]) message.getObject())[0];
		Player player2 = ((Player[]) message.getObject())[1];
		DatabaseController.mySQLConnection.sqlExecute("INSERT INTO friendships (id, user_id_1, user_id_2) VALUES (?, ?, ?)",  UUID.randomUUID().toString(), player1.getId(), player2.getId());
		caller.sendMessage(new Message(true));
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

	private void getLobbyUnitFromDb() throws SQLException, IOException {
		String sqlQuery = "SELECT ip_address,port FROM lobby_units ORDER BY user_count ASC LIMIT 1";
		ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery(sqlQuery);
		resultSet.next();
		String queryResult = resultSet.getString("ip_address") + ":" + resultSet.getString("port");
		caller.sendMessage(new Message(998, queryResult));
	}

	private void getUnitFromDb(Message message) throws IOException, SQLException, InterruptedException {
	    semaphore.acquire();
		String sqlQuery = "SELECT ip_address,port FROM " + message.getObject() + " ORDER BY user_count ASC LIMIT 1";
		ResultSet resultSet = DatabaseController.mySQLConnection.sqlExecuteQuery(sqlQuery);
		resultSet.next();
		String queryResult = resultSet.getString("ip_address") + ":" + resultSet.getString("port");
		semaphore.release();
		caller.sendMessage(new Message(999, queryResult));
	}

	private void addNodeToDb(Message message) throws SQLException {
		String[] queryDetails = (String[]) message.getObject();
		String sqlQuery = "INSERT INTO " + queryDetails[1] + " (id, ip_address, port, user_count) VALUES (?, ?, ?, ?)";
		DatabaseController.mySQLConnection.sqlExecute(sqlQuery, queryDetails[2], ((TCPConnection) caller).getConnection().getInetAddress().toString(), queryDetails[0], "0");
		try {
			caller.sendMessage(new Message(((TCPConnection) caller).getConnection().getInetAddress().toString()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private boolean userExistsInDatabase(String username) throws SQLException, IOException {
		return DatabaseController.mySQLConnection.sqlExecuteQuery("SELECT 1 FROM users WHERE username = ?", username).last();
	}

}
