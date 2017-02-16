package com.swarmer.utility;

import java.util.HashMap;

public class Path {
	
	private HashMap<String, Pheromone> pheromones = new HashMap<String, Pheromone>();
	private final String PATH_ID;
	
	public Path(String PATH_ID) {
		this.PATH_ID = PATH_ID;
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
	

}
