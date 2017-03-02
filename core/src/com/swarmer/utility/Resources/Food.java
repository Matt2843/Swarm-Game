package com.swarmer.utility.Resources;

import com.swarmer.utility.Resource;

public class Food extends Resource {

	private int quantity;

	public Food(int quantity) {
		super(quantity);
		setType("food");
	}

}
