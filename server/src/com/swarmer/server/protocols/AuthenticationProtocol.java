package com.swarmer.server.protocols;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.units.AuthenticationUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matt on 04/06/2017.
 */
public class AuthenticationProtocol extends ServerProtocol {

	private Connection caller;

	public AuthenticationProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
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
				super.react(message, caller);
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
			Player createdPlayer = AuthenticationUnit.createUser(message);
			new CoordinationUnitCallable(new Message(1150, new Object[]{createdPlayer, serverUnit.getDescription(), serverUnit.getPort()})).getFutureResult();
			caller.sendMessage(new Message(202, createdPlayer));
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void authenticateUser(Message message) throws IOException {
		Player authenticatedPlayer = AuthenticationUnit.authenticateUser(message);
		new CoordinationUnitCallable(new Message(1150, new Object[]{authenticatedPlayer, serverUnit.getDescription(), serverUnit.getPort()}));
		caller.sendMessage(new Message(110, authenticatedPlayer));
	}
}
