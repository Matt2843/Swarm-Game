package com.swarmer.server.protocols;

import com.swarmer.server.DatabaseController;
import com.swarmer.server.units.AuthenticationUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.TCPConnection;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.PublicKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseControllerProtocol extends ServerProtocol {

	private Connection caller;

	public DatabaseControllerProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	public DatabaseControllerProtocol() {
		super(null);
	}

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
				getLobbyUnit(message);
				break;
			case 34788:
				addFriendShipToDatabase(message);
				break;
            case 15000:
                updateUserCount(message);
                break;
			default:
				super.react(message, caller);
				break;
		}
	}

    private void updateUserCount(Message message) throws IOException, SQLException {
	    Object[] objects = (Object[]) message.getObject();
	    String UUID = (String) objects[0];
	    String description = (String) objects[1];
	    int usersConnected = (int) objects[2];
        DatabaseController.mySQLConnection.sqlExecute("UPDATE " + description + " SET user_count=" + usersConnected + ";");
        //DatabaseController.mySQLConnection.sqlExecute("UPDATE " + description + " SET user_count=" + usersConnected + " WHERE id=" + "'" + UUID + "'" + ";");
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
