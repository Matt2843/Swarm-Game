package com.swarmer.server.game.logic;

import com.swarmer.shared.aco.graph.Graph;
import com.swarmer.shared.aco.graph.Vector2;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.resources.Food;
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

	private Graph graph;
	private int mapWidth, mapHeight;
	private ArrayList<Ant> ants;
	private ArrayList<Hive> hives;
	private HashMap<Player, Connection> players;
	private UDPConnection udpConnection;

	public Game(HashMap<Player, Connection> players, int width, int height) {
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
			int x = ThreadLocalRandom.current().nextInt(1, mapWidth);
			int y = ThreadLocalRandom.current().nextInt(1, mapHeight);

			if(graph.nodes[x][y] != null && graph.nodes[x][y].getConnectedEdges().size() > 0) {
				graph.nodes[x][y].setResource(new Food(100));
			}
		}

		int i = 0;
		for(Map.Entry<Player, Connection> player : players.entrySet()) {
			if (i == 0) {
				hives.add(new Hive(player.getKey(), graph.nodes[0][0]));
			}
			if (i == 1) {
				hives.add(new Hive(player.getKey(), graph.nodes[mapWidth-1][mapHeight-1]));
			}
			if (i == 2) {
				hives.add(new Hive(player.getKey(), graph.nodes[mapWidth-1][0]));
			}
			if (i == 3) {
				hives.add(new Hive(player.getKey(), graph.nodes[0][mapHeight-1]));
			}
			i++;
		}
	}

	public boolean render() {
		//int size = 5;
		SerialisedAnts serialisedAnts = new SerialisedAnts(ants.size());

		for(int i = 0; i < graph.nodes.length; i++) {
			for(int j = 0; j < graph.nodes[0].length; j++) {
				graph.nodes[i][j].evaporate();
			}
		}

		int j = 0;
		for(int i = 0; i < ants.size(); i++) {
			ants.get(i).update();
			/*if(j > size){
				try {
					udpConnection.sendMessage(new Message(23323, serialisedAnts));
				} catch(IOException e) {
					e.printStackTrace();
				}
				serialisedAnts = new SerialisedAnts(size);
				j = 0;
			}*/
			if(ants.get(i).food > 0) {
				serialisedAnts.addAnt(i, ants.get(i).desiredPosition.x, ants.get(i).desiredPosition.y);
			}
			//j++;
		}

		for(Map.Entry<Player, Connection> player : players.entrySet()) {
			try {
				player.getValue().sendMessage(new Message(23323, serialisedAnts));
			} catch(IOException e) {
				return false;
			}
		}

		/*if (serialisedAnts.size > 0) {
			try {
				udpConnection.sendMessage(new Message(23323, serialisedAnts));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}*/
		return true;
	}

	public void spawnAnt(Player owner) {
		for(Hive hive : hives) {
			if(hive.owner.equals(owner)) {
				ants.add(new Ant(owner, hive.node, graph));
			}
		}
	}

	public Graph getGraph() {
		return graph;
	}

	public void setUdpConnection(UDPConnection udpConnection) {
		this.udpConnection = udpConnection;
	}
}
