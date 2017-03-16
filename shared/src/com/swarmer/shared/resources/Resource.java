package com.swarmer.shared.resources;

public abstract class Resource {
	
	private int quantity;
	private String type = null;
	
	public Resource(int quantity) {
		this.quantity = quantity;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	;

	public String getType() {
		return this.type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void removeQuantity(int quantity) {
		if(this.quantity > 0 + quantity) {
			this.quantity -= quantity;
		}
	}

	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}
}
