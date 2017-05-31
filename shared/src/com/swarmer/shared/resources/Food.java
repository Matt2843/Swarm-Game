package com.swarmer.shared.resources;

public class Food extends Resource {

	public Food(int quantity) {
		super(quantity);
	}

	@Override public String getType() {
		return "Food";
	}

}
