package com.swarmer.server.protocols;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.security.HashingTools;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AuthenticationProtocol extends Protocol {

	private Connection caller;

	@Override protected void react(Message message, Connection caller) throws IOException, NoSuchAlgorithmException {
		this.caller = caller;
		switch (message.getOpcode()) {
			case 13: // State of user creation, if object is true the user was succesfully created if object is false, the user was not created.

				break;
			case 14: { // .getObject() = {username, password, password salt, hashed password}
				String[] object = (String[]) message.getObject();
				authenticateCredentials(object[0], object[1], object[2], object[3]);
				break;
			}
			case 109:
				createUser((String[]) message.getObject());
				break;
			case 201:
				authenticateUser((String[]) message.getObject());
				break;
			default:
				break;
		}

	}

	private void authenticateCredentials(String username, String password, String salt, String hashedPassword) {
		password = HashingTools.hashPassword(password.toCharArray(), HashingTools.hexToBytes(salt));
		if(password.equals(hashedPassword)) {
			// SUCCESS
		} else {
			// FAIL
		}
	}

	private void createUser(String[] credentials) throws IOException, NoSuchAlgorithmException {
		String username = credentials[0];
		String password = credentials[1];
		AuthenticationNode.createUser(username, password);
	}

	private void authenticateUser(String[] credentials) throws IOException {
		String username = credentials[0];
		String password = credentials[1];
		AuthenticationNode.authenticateUser(username, password);
	}
}
