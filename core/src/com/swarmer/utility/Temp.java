package com.swarmer.utility;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.game.units.Ant;
import com.swarmer.gui.screens.game.GameScreen;
import com.swarmer.shared.communication.Player;

public class Temp {

	public static Boolean spawn(Player owner, int x, int y) {

		if(between(x, 0, GameScreen.getInstance().graph.nodes.length) && between(y, 0, GameScreen.getInstance().graph.nodes[x].length)) {
			if(GameScreen.getInstance().graph.nodes[x][y] != null && GameScreen.getInstance().graph.nodes[x][y].getConnectedEdges().size() > 0) {
				GameScreen.getInstance().getAnts().add(new Ant(owner, GameScreen.getInstance().graph.nodes[x][y]));
				return true;
			}
		}
		return false;
	}

	public static Boolean spawn(Player owner, Vector2 vec) {
		return spawn(owner, (int) vec.x, (int) vec.y);
	}

	public static Boolean between(int val, int min, int max) {
		return (min <= val && val < max);
	}

}
