package com.swarmer.server.protocols;

import com.swarmer.server.units.GameUnit;
import com.swarmer.server.units.ServerUnit;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GameProtocol extends ServerProtocol {

	public GameProtocol(ServerUnit serverUnit) {
		super(serverUnit);
	}

	@Override protected void react(Message message, Connection caller) throws IOException, SQLException, InterruptedException {
		System.out.println("Lobby unit: " + message.toString());
		switch (message.getOpcode()) {
			case 101:
				startGame(message);
				break;
            case 23324:
                spawnAnt(message, caller);
                break;
			default:
				super.react(message, caller);
				break;
		}
	}

    private void spawnAnt(Message message, Connection caller) {
		String gameID = (String) message.getObject();
		Player antOwner = caller.getPlayer();
		((GameUnit) serverUnit).spawnAnt(gameID, antOwner);
    }

    private void startGame(Message message) throws IOException, InterruptedException {
		HashMap<Player, LocationInformation> players = (HashMap<Player, LocationInformation>) message.getObject();

		for(Map.Entry<Player, LocationInformation> player : players.entrySet()) {
			if(!player.getValue().getInetAddress().equals(serverUnit.getId())) {
				ServerUnit.sendTo(player.getKey().getUsername(), player.getValue(), null, new Message(1000, serverUnit.getId()));
			}
		}
		((GameUnit) serverUnit).startNewGame(players);
	}
}
