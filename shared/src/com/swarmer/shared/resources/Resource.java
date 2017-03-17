package com.swarmer.shared.resources;

public abstract class Resource {
	
	private int quantity;

	public Resource() {
		this.quantity = 0;
	}
	
	public Resource(int quantity) {
		this.quantity = quantity;
	}

	public abstract String getType();

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void removeQuantity(int quantity) {
		if (this.quantity > 0 + quantity) {
			this.quantity -= quantity;
		}
	}

	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	@Override public boolean equals(Object o) {
		Resource that = (Resource) o;
		if(this.getType() == that.getType())
			return true;
		return false;
	}

	@Override public int hashCode() {
		return quantity;
	}
}
