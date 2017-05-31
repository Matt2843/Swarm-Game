package com.swarmer.server.game.logic;

import com.swarmer.server.game.aco.graph.Graph;
import com.swarmer.server.game.aco.graph.Vector2;
import com.swarmer.server.game.logic.resources.Food;
import com.swarmer.server.game.logic.structures.Hive;
import com.swarmer.server.game.logic.units.Ant;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.SerialisedAnts;
import com.swarmer.shared.communication.UDPConnection;

import java.io.IOException;
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
	private UDPConnection udpConnection;

	public Game(HashMap<Player, LocationInformation> players, int width, int height, UDPConnection udpConnection) {
		this.players = players;
		mapWidth = width;
		mapHeight = height;
		this.udpConnection = udpConnection;
		init();
	}

	private void init() {
		hives = new ArrayList<>();
		ants = new ArrayList<>();

		graph = new Graph(mapWidth, mapHeight);

		for(int i = 0; i < 200; i++) {
			int x = ThreadLocalRandom.current().nextInt(1, mapWidth);
			int y = ThreadLocalRandom.current().nextInt(1, mapHeight);

			if(graph.nodes[x][y] != null && graph.nodes[x][y].getConnectedEdges().size() > 0) {
				graph.nodes[x][y].setResource(new Food(100));
			}
		}

		int i = 0;
		for(Map.Entry<Player, LocationInformation> player : players.entrySet()) {
			if (i == 0) {
				hives.add(new Hive(player.getKey(), graph.nodes[0][0]));
			}
			if (i == 1) {
				hives.add(new Hive(player.getKey(), graph.nodes[mapWidth][mapHeight]));
			}
			if (i == 2) {
				hives.add(new Hive(player.getKey(), graph.nodes[mapWidth][0]));
			}
			if (i == 3) {
				hives.add(new Hive(player.getKey(), graph.nodes[0][mapHeight]));
			}
			i++;
		}
	}

	public void render() {
		SerialisedAnts serialisedAnts = new SerialisedAnts();
		int j = 1;
		for(int i = 0; i < ants.size(); i++) {
			ants.get(i).update();
			if(j >= 10){
				try {
					udpConnection.sendMessage(new Message(23323, serialisedAnts));
				} catch(IOException e) {
					e.printStackTrace();
				}
				serialisedAnts = new SerialisedAnts();
				j = 0;
			}
			serialisedAnts.addAnt(i, ants.get(i).desiredPosition.x, ants.get(i).desiredPosition.y);
			j++;
		}
		if (ants.size() % 10 != 0) {
			try {
				udpConnection.sendMessage(new Message(23323, serialisedAnts));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
