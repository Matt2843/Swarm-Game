package com.swarmer.utility;

public class Pheromone {
	private int quantity = 0;
	
	public Pheromone(int i) {
		quantity = i;
	}
	
	public void addPheromone() {
		quantity += 1;
	}
	
	public void decreasePheromone() {
		if(quantity > 0) {
			quantity -= 1;
		}
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
