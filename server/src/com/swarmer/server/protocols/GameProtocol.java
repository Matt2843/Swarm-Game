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
                spawnAnt(caller);
                break;
			default:
				super.react(message, caller);
				break;
		}
	}

    private void spawnAnt(Connection caller) {
        Player antOwner = caller.getPlayer();
        
    }

    private void startGame(Message message) throws IOException, InterruptedException {
		System.out.println("Starting new game");
		HashMap<Player, LocationInformation> players = (HashMap<Player, LocationInformation>) message.getObject();

		((GameUnit) serverUnit).startNewGame(players);
	}
}
