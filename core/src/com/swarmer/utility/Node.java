package com.swarmer.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Node {
	
	private final Vector2 position;
	private Array<Node> connectedNodes;
	
	private Resource resource = null;
	

	public Node(Vector2 position, Array<Node> connectedNodes) {
		this.position = position;
		this.connectedNodes = connectedNodes;
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
