package com.swarmer.shared;

public class Food extends Resource {

	private int quantity;

	public Food(int quantity) {
		super(quantity);
		setType("food");
	}

}
