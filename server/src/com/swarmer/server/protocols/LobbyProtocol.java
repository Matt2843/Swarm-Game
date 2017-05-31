package com.swarmer.server.protocols;

import com.swarmer.server.units.LobbyUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Matt on 04/06/2017.
 */
public class LobbyProtocol extends ServerProtocol {

	private Connection caller;

	public LobbyProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException {
		this.caller = caller;
		System.out.println("Lobby unit: " + message.toString());
		switch (message.getOpcode()) {
			case 301:
				((LobbyUnit)serverUnit).broadcastMessageToLobby(message, caller.getPlayer());
				break;
			case 302: // User wants to create a lobby.
				createLobby(caller.getPlayer());
				break;
			case 303:
				userWantsToJoinLobby(message);
				break;
			case 13371:
				startGame(message);
			default:
				super.react(message, caller);
				break;
		}
	}

	private void startGame(Message message) throws IOException {
		((LobbyUnit)serverUnit).startGame((String) message.getObject());
	}

	private void userWantsToJoinLobby(Message message) throws IOException {
		String lobbyId = (String) ((Object[])message.getObject())[0];
		Player target = (Player) ((Object[])message.getObject())[1];
		((LobbyUnit)serverUnit).joinLobby(lobbyId, target);
	}

	private void createLobby(Player player) throws IOException {
		String createdLobbyId = ((LobbyUnit)serverUnit).createLobby(player);
		caller.sendMessage(new Message(997, createdLobbyId));
	}
}
