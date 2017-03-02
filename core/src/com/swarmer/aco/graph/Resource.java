package com.swarmer.aco.graph;

public abstract class Resource {
	
	private int quantity;
	private String type = null;
	
	public Resource(int quantity) {
		this.quantity = quantity;
	}
	
	public void setType(String type) {
		this.type = type;
	};

	public String getType() {
		return this.type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
