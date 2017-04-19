package com.swarmer.server.protocols;

import com.swarmer.server.units.AuthenticationUnit;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AuthenticationProtocol extends Protocol {

	private Connection caller;

	@Override protected void react(Message message, Connection caller) throws IOException, NoSuchAlgorithmException {
		this.caller = caller;
		System.out.println("Authentication Unit: " + message.toString());
		switch (message.getOpcode()) {
			case 1:
				break;
			case 109:
				authenticateUser(message);
				break;
			case 201:
				createUser(message);
				break;
			case 1111:
				establishSecureConnection(message, caller);
				break;
			default:
				break;
		}
	}

	private void establishSecureConnection(Message message, Connection caller) throws IOException {
		SecureTCPConnection secureTCPConnection = new SecureTCPConnection(((TCPConnection)caller).getConnection(), this);
		secureTCPConnection.setExternalPublicKey((PublicKey) message.getObject());
		secureTCPConnection.start();
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
