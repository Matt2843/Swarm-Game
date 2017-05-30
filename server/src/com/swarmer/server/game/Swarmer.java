package com.swarmer.server.game;

import com.swarmer.server.game.logic.Game;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;

import java.util.HashMap;

public class Swarmer implements Runnable {

	private Game game;

	public Swarmer(HashMap<Player, LocationInformation> players) {
		game = new Game(players, 500, 500);
	}

	@Override public void run() {
		while(true) {
			long time = System.currentTimeMillis();
			game.render();
			long sleep = 1000 - (System.currentTimeMillis() - time);
			try {
				Thread.currentThread().sleep(sleep);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
