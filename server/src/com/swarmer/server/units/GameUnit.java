package com.swarmer.server.units;

import com.swarmer.server.game.Swarmer;
import com.swarmer.server.protocols.GameProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;

import java.util.HashMap;

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

	public void startNewGame(HashMap<Player, LocationInformation> players) {
		Swarmer game = new Swarmer(players, UDPsocket.udpConnection);
		currentRunningGames.put(game.getGameUUID(), game);
		game.run();
	}
}
