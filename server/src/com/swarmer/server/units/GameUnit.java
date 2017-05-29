package com.swarmer.server.units;

import com.swarmer.server.protocols.GameProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.shared.communication.Player;

/**
 * Created by Matt on 03/16/2017.
 */
public class GameUnit extends ServerUnit {

	private final GameProtocol gameProtocol = new GameProtocol(this);

	protected GameUnit() {
		super();
	}

	@Override public int getPort() {
		return ServerUnit.GAME_UNIT_TCP_PORT;
	}

	@Override protected ServerProtocol getProtocol() {
		return gameProtocol;
	}

	@Override
	public String getDescription() {
		return "game_units";
	}

	public static void main(String[] args) {
		new GameUnit();
	}

	public void startNewGame(Player[] players) {

	}
}
