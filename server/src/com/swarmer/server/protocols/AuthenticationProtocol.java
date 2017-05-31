package com.swarmer.server.protocols;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.DatabaseControllerCallable;
import com.swarmer.server.units.AuthenticationUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

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
			case 301:
				getLobbyUnit(message); // message = {lobby unit ID} or message = {random}
				break;
			case 13371:
				findGame(message); // message = ArrayList<Player>
			default:
				super.react(message, caller);
				break;
		}
	}

	private void findGame(Message message) {
		try {
			Message response = new CoordinationUnitCallable(message).getFutureResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getLobbyUnit(Message message) throws IOException {
		Message lobbyUnitConnectionInformation = new DatabaseControllerCallable(message).getFutureResult();
		caller.sendMessage(lobbyUnitConnectionInformation);
	}

	private void createUser(Message message) {
		try {
			Player createdPlayer = AuthenticationUnit.createUser(message);
			if(createdPlayer != null) {
				new CoordinationUnitCallable(new Message(1150, new Object[]{createdPlayer, serverUnit.getDescription(), serverUnit.getPort()})).getFutureResult();
			}
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
		if(authenticatedPlayer != null) {
			new CoordinationUnitCallable(new Message(1150, new Object[]{authenticatedPlayer, serverUnit.getDescription(), serverUnit.getPort()})).getFutureResult();
			caller.sendMessage(new Message(110, authenticatedPlayer));
			serverUnit.addActiveConnection(authenticatedPlayer, caller);
		}
	}
}
