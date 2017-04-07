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
			case 109:
				createUser(message);
				break;
			case 201:
				authenticateUser(message);
				break;
			default:
				break;
		}

	}

	private void createUser(Message message) throws IOException, NoSuchAlgorithmException {
		AuthenticationNode.createUser(message);
	}

	private void authenticateUser(Message message) throws IOException {
		AuthenticationNode.authenticateUser(message);
	}
}
