package com.swarmer.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Node {
	
	private final Vector2 position;
	private Array<Node> nodesInVicinity;
	
	private Resource resource = null;
	
	public Node nextNode(Node previousNode) {
		/*
		 * Implement:
		 * set coefficient of previousNode low
		 * roll through nodesInVicinity
		 * return the node
		 */
		return null;
	}

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
