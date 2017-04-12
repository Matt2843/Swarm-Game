package com.swarmer.server.protocols;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.server.security.HashingTools;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AuthenticationProtocol extends Protocol {

	private Connection caller;

	@Override protected void react(Message message, Connection caller) throws IOException, NoSuchAlgorithmException {
		this.caller = caller;
		System.out.println("Authentication Node: " + message.toString());
		switch (message.getOpcode()) {
			case 1:

				break;
			case 109:
				authenticateUser(message);
				break;
			case 201:
				createUser(message);
				break;
			default:
				break;
		}
	}

	private void createUser(Message message) {
		try {
			boolean queryState = AuthenticationNode.createUser(message);
			caller.sendMessage(new Message(202, queryState));
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void authenticateUser(Message message) throws IOException {
		boolean queryState = AuthenticationNode.authenticateUser(message);
		caller.sendMessage(new Message(110, queryState));
	}
}
