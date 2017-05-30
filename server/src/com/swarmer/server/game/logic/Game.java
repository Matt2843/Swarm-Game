package com.swarmer.server.game.logic;

import com.swarmer.server.game.aco.graph.Graph;
import com.swarmer.server.game.aco.graph.Vector2;
import com.swarmer.server.game.logic.resources.Food;
import com.swarmer.server.game.logic.structures.Hive;
import com.swarmer.server.game.logic.units.Ant;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

	public static Graph graph;
	private int mapWidth, mapHeight;
	private Vector2 vec = new Vector2();
	private ArrayList<Ant> ants;
	private ArrayList<Hive> hives;
	private HashMap<Player, LocationInformation> players;

	public Game(HashMap<Player, LocationInformation> players, int width, int height) {
		this.players = players;
		mapWidth = width;
		mapHeight = height;
		init();
	}

	private void init() {
		hives = new ArrayList<>();
		ants = new ArrayList<>();

		graph = new Graph(mapWidth, mapHeight);

		for(int i = 0; i < 200; i++) {
			int x = ThreadLocalRandom.current().nextInt(1, 99);
			int y = ThreadLocalRandom.current().nextInt(1, 99);

			if(graph.nodes[x][y] != null && graph.nodes[x][y].getConnectedEdges().size() > 0) {
				graph.nodes[x][y].setResource(new Food(100));
			}
		}

		for(Map.Entry<Player, LocationInformation> player : players.entrySet()) {
			int x = ThreadLocalRandom.current().nextInt(1, 99);
			int y = ThreadLocalRandom.current().nextInt(1, 99);

			if(graph.nodes[x][y] != null && graph.nodes[x][y].getConnectedEdges().size() > 0) {
				hives.add(new Hive(player.getKey(), graph.nodes[x][y]));
				ants.add(new Ant(player.getKey(), graph.nodes[x][y]));
			}
		}
	}

	public void render() {
		for(Ant ant : ants) {
			ant.update();
		}
	}
}
