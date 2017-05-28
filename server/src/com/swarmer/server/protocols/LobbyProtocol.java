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
				((LobbyUnit)serverUnit).broadcastMessageToLobby(message);
				break;
			case 302: // User wants to create a lobby.
				System.out.println(caller.getPlayer());
				createLobby(caller.getPlayer());
				break;
			default:
				super.react(message, caller);
				break;
		}
	}

	private void createLobby(Player player) throws IOException {
		String createdLobbyId = LobbyUnit.createLobby(player);
		caller.sendMessage(new Message(997, createdLobbyId));
	}
}
