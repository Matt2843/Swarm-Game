package com.swarmer.utility;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.gui.screens.GameScreen;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.game.units.Ant;

public class Temp {

	public static Boolean spawn(int x, int y) {
		if(between(x, 0, gameScreen().graph.nodes.length) && between(y, 0, gameScreen().graph.nodes[x].length)) {
			if(gameScreen().graph.nodes[x][y] != null && gameScreen().graph.nodes[x][y].getConnectedEdges().size > 0) {
				gameScreen().getAnts().add(new Ant((TiledMapTileLayer) gameScreen().getMap().getLayers().get(1), gameScreen().graph.nodes[x][y]));
				return true;
			}
		}
		return false;
	}

	public static Boolean spawn(Vector2 vec) {
		if(gameScreen().graph.nodes[(int) vec.x][(int) vec.y] != null && gameScreen().graph.nodes[(int) vec.x][(int) vec.y].getConnectedEdges().size > 0) {
			gameScreen().getAnts().add(new Ant((TiledMapTileLayer) gameScreen().getMap().getLayers().get(1), gameScreen().graph.nodes[(int) vec.x][(int) vec.y]));
			return true;
		}
		return false;
	}

	private static GameScreen gameScreen() {
		return (GameScreen) ScreenManager.getInstance().getScreen(com.swarmer.gui.screens.ScreenLib.GAME_SCREEN);
	}

	public static Boolean between(int val, int min, int max) {
		return (min <= val && val < max);
	}

}
