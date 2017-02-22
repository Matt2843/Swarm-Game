package com.swarmer.utility;

import java.util.HashMap;

public class Edge {
	
	private HashMap<String, Pheromone> pheromones = new HashMap<String, Pheromone>();
	private String PATH_ID;
	
	private final Node node1;
	private final Node node2;
	
	public Edge(Node n1, Node n2) {
		this.node1 = n1;
		this.node2 = n2;
		if(!n1.isCollisionNode && !n2.isCollisionNode) {
			n1.addEdge(this);
			n2.addEdge(this);
		}
	}
	
	public Edge(String PATH_ID) {
		this.PATH_ID = PATH_ID;
		node1 = null;
		node2 = null;
	}

	public String getPATH_ID() {
		return PATH_ID;
	}

	public HashMap<String, Pheromone> getPheromones() {
		return pheromones;
	}

	public void setPheromones(HashMap<String, Pheromone> pheromones) {
		this.pheromones = pheromones;
	}

	public Node getNode1() {
		return node1;
	}

	public Node getNode2() {
		return node2;
	}
	

}
