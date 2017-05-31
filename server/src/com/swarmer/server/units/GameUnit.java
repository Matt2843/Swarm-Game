package com.swarmer.server.units;

import com.swarmer.server.game.Swarmer;
import com.swarmer.server.game.aco.graph.Graph;
import com.swarmer.server.protocols.GameProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameUnit extends ServerUnit {

	private final GameProtocol gameProtocol = new GameProtocol(this);
	private HashMap<String, Swarmer> currentRunningGames = new HashMap<>();

	protected GameUnit() {
		super();
		HashMap<Player, LocationInformation> players = new HashMap<Player, LocationInformation>();
		players.put(new Player("1", "Matt", -5), null);
//		startNewGame(players);
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

		Graph map = game.getMap();
		String ID = game.getGameUUID();

		currentRunningGames.put(ID, game);

		for (Map.Entry<Player, LocationInformation> player : players.entrySet()) {
			sendTo(player.getKey().getUsername(), player.getValue(), null, new Message(13371, new Object[]{ID, port, map}));
		}

		game.run();
	}
}
