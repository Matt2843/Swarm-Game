package com.swarmer.server.units;

import com.swarmer.server.game.Swarmer;
import com.swarmer.server.protocols.GameProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.aco.graph.Graph;
import com.swarmer.shared.aco.graph.SerialisedGraph;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameUnit extends ServerUnit {

	private final GameProtocol gameProtocol = new GameProtocol(this);
	private HashMap<String, Swarmer> currentRunningGames = new HashMap<>();

	protected GameUnit() {
		super();
	}

	@Override public int getPort() {
		return ServerUnit.GAME_UNIT_TCP_PORT;
	}

	@Override protected ServerProtocol getProtocol() {
		return gameProtocol;
	}

	@Override public String getDescription() {
		return "game_units";
	}

	public static void main(String[] args) {
		new GameUnit();
	}

	public void startNewGame(HashMap<Player, LocationInformation> players) throws IOException, InterruptedException {
		int port = getPort() + currentRunningGames.size() + 4;

		Swarmer game = new Swarmer(players, this, port);
		new Thread(game).start();

		SerialisedGraph map = new SerialisedGraph(game.getMap());
		String ID = game.getGameUUID();

		currentRunningGames.put(ID, game);

		System.out.println("her 1");

		for (Map.Entry<Player, LocationInformation> player : players.entrySet()) {
			sendToPlayer(player.getKey().getUsername(), new Message(13371, new Object[]{ID, port, map}));
		}

		System.out.println("her 2");

		new Thread(game).start();

		System.out.println("her 3");
	}
}
