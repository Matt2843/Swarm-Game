package com.swarmer.utility;

import com.badlogic.gdx.math.Vector2;

public class Node {
	
	private final Vector2 position;
	
	private Resource resource = null;

	public Node(Vector2 position) {
		this.position = position;
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
}
