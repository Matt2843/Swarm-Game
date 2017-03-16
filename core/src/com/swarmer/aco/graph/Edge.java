package com.swarmer.aco.graph;

import java.util.HashMap;

public class Edge {
	
	private HashMap<String, Pheromone> pheromones;
	private String PATH_ID;

	public Edge reverse;
	
	private final Node dest;
	
	public Edge(Node start, Node end, Boolean bool) {
		dest = end;
		if(start != null && end != null) {
			start.addEdge(this);
			if(bool) {
				pheromones = new HashMap<>();
				reverse = new Edge(end, start, false);
				reverse.setPheromones(pheromones);
				reverse.reverse = this;
			}
		}
	}

	public String getPATH_ID() {
		return PATH_ID;
	}
	
	public Pheromone getPheromones(String key) {
		if(!pheromones.containsKey(key)) {
			pheromones.put(key, new Pheromone(0));
		}
		return pheromones.get(key);
	}
	
	public HashMap<String, Pheromone> getPheromones() {
		return pheromones;
	}
	
	public void setPheromones(HashMap<String, Pheromone> pheromones) {
		this.pheromones = pheromones;
	}

	@Override public boolean equals(Object edge) {
		return edge != null && dest.equals(edge);
	}

	public Node getNode() {
		return dest;
	}
}
