package com.swarmer.server.protocols;

import com.swarmer.server.nodes.AuthenticationNode;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Protocol;

import java.io.IOException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AuthenticationProtocol extends Protocol {

	private Connection caller;

	@Override protected void react(Message message, Connection caller) throws IOException {
		this.caller = caller;
		switch (message.getOpcode()) {
			case 109:
				loginUser((String[]) message.getObject());
				break;
			case 201:
				createUser((String[]) message.getObject());
				break;
			default:
				break;
		}

	}

	private void createUser(String[] credentials) throws IOException {
		String username = credentials[0];
		char[] password = credentials[1].toCharArray();
		boolean evaluate = AuthenticationNode.createUser(username, password);
		if(evaluate) {
			caller.sendMessage(new Message(202));
		} else {
			caller.sendMessage(new Message(203));
		}
	}

	private void loginUser(String[] credentials) throws IOException {
		String username = credentials[0];
		char[] password = credentials[1].toCharArray();
		boolean evaluate = AuthenticationNode.authenticateUser(username, password);
		if(evaluate) {
			caller.getPlayer().setAlias(username);
			caller.sendMessage(new Message(110));
		} else {
			caller.sendMessage(new Message(111));
		}
	}
}
