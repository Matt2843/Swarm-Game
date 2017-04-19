package com.swarmer.server.protocols;

import com.swarmer.server.units.AuthenticationUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.concurrent.ExecutionException;

public class AuthenticationProtocol extends ServerProtocol {

	private Connection caller;

	public AuthenticationProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	@Override protected void react(Message message, Connection caller) throws IOException {
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
			case 11111:
				exPublicKey = (PublicKey) message.getObject();
				caller.sendMessage(new Message(11111, AuthenticationUnit.KEY.getPublic()));
				break;
			case 1111:
				establishSecureConnection(message, caller);
				break;
			default:
				break;
		}
	}

	private void establishSecureConnection(Message message, Connection caller) throws IOException {
		exPublicKey = (PublicKey) message.getObject();
	}

	private void createUser(Message message) {
		try {
			boolean queryState = AuthenticationUnit.createUser(message);
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
		boolean queryState = AuthenticationUnit.authenticateUser(message);
		caller.sendMessage(new Message(110, queryState));
	}
}
