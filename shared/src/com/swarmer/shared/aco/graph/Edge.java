package com.swarmer.shared.aco.graph;

import com.swarmer.shared.communication.Player;

import java.io.Serializable;
import java.util.HashMap;

public class Edge implements Serializable {

	private final Vector2 dest;
	public Edge reverse;
	private HashMap<Player, Pheromone> pheromones;

	public Edge(Node start, Node end, Boolean bool) {
		dest = new Vector2((int) end.getX(), (int) end.getY());
		if(start != null) {
			start.addEdge(this);
			if(bool) {
				pheromones = new HashMap<>();
				reverse = new Edge(end, start, false);
				reverse.setPheromones(pheromones);
				reverse.reverse = this;
			}
		}
	}

	public Pheromone getPheromones(Player player) {
		if(!pheromones.containsKey(player)) {
			pheromones.put(player, new Pheromone(0));
		}
		return pheromones.get(player);
	}

	public void setPheromones(HashMap<Player, Pheromone> pheromones) {
		this.pheromones = pheromones;
	}

	@Override public boolean equals(Object edge) {
		return edge != null && dest.equals(edge);
	}

	public Vector2 getNode() {
		return dest;
	}
}
