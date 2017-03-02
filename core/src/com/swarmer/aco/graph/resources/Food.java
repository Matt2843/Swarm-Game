package com.swarmer.aco.graph.resources;

import com.swarmer.aco.graph.Resource;

public class Food extends Resource {

	private int quantity;

	public Food(int quantity) {
		super(quantity);
		setType("food");
	}

}
