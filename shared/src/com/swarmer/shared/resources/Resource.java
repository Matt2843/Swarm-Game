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

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Resource resource = (Resource) o;

		if (quantity != resource.quantity) return false;
		return type != null ? type.equals(resource.type) : resource.type == null;
	}

	@Override public int hashCode() {
		int result = quantity;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}
}
