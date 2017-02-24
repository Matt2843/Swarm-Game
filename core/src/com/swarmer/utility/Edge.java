package com.swarmer.utility;

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
				pheromones = new HashMap<String, Pheromone>();
				reverse = new Edge(end, start, false);
				reverse.setPheromones(pheromones);
				reverse.reverse = this;
				end.addEdge(reverse);
			}
		}
	}

	public String getPATH_ID() {
		return PATH_ID;
	}
	
	public Pheromone getPheromones(String key) {
		if(!pheromones.containsKey(key)){
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

	public Boolean equals(Edge edge){
		if(edge == null) {
			return false;
		} else {
			return dest.equals(edge);
		}
	}

	public Node getNode() {
		return dest;
	}
}
