package com.swarmer.utility;

public abstract class Resource {
	
	private int quantity;
	private String type = null;
	
	public Resource(int quantity) {
		this.quantity = quantity;
	}
	
	public abstract void setType();

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
