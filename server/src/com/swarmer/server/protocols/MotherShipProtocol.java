package com.swarmer.server.protocols;

import com.swarmer.server.MotherShip;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Matt on 04/06/2017.
 */
public class MotherShipProtocol extends Protocol {

	private Connection caller;

	@Override protected void react(Message message, Connection caller) throws SQLException, IOException {
		this.caller = caller;
		switch (message.getOpcode()) {
			case 3: // Create "user" in database, .getObject() = String[] {username, hashedPassword, salt}
				addUserCredentialsToDatabase(((String[]) message.getObject())[0], ((String[]) message.getObject())[1], ((String[]) message.getObject())[2]);
				break;
			case 4: // Authenticate user in database, .getObject() = String[] {username, password}
				sendCredentialsAndDatabaseCredentialsToAuthenticationNode((String[]) message.getObject());
				break;
			default:
				break;
		}
	}

	private void sendCredentialsAndDatabaseCredentialsToAuthenticationNode(String[] object) throws SQLException, IOException {
		String saltFromDatabase = MotherShip.mySQLConnection.sqlExecuteQueryToString("SELECT salt FROM users WHERE username = ?", object[0]);
		String hasedPasswordFromDatabase = MotherShip.mySQLConnection.sqlExecuteQueryToString("SELECT password FROM users WHERE username = ?", object[0]);
		caller.sendMessage(new Message(14, new String[] {object[0], object[1], saltFromDatabase, hasedPasswordFromDatabase}));
	}

	private boolean userExistsInDatabase(String username) throws SQLException, IOException {
		boolean userConnected = MotherShip.mySQLConnection.sqlExecuteQuery("SELECT 1 FROM users WHERE username = ?", username).last();
		return userConnected;
	}

	private void addUserCredentialsToDatabase(String username, String hashedPassword, String salt) throws SQLException, IOException {
		if(userExistsInDatabase(username)) {
			MotherShip.mySQLConnection.sqlExecute("INSERT INTO users (id, username, password, password_salt) VALUES ('" + UUID.randomUUID().toString() + "',?,'" + hashedPassword + "','" + salt + "')", username);
			caller.sendMessage(new Message(13, true));
		} else {
			caller.sendMessage(new Message(13, false));
		}
	}

	private void authenticateUserInDatabase(String username) throws SQLException, IOException {
		if(userExistsInDatabase(username)) {
			String hashedPasswordFromDatabase = MotherShip.mySQLConnection.sqlExecuteQueryToString("SELECT password FROM users WHERE username = ?", username);
			caller.sendMessage(new Message(14, hashedPasswordFromDatabase));
		} else {
			caller.sendMessage(new Message(14, false));
		}

	}

}
