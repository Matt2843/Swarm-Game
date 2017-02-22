package com.swarmer.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Node {
	
	private final Vector2 position;
	private Array<Edge> connectedEdges;
	
	private Resource resource = null;
	
	public Node(Vector2 position) {
		this.position = position;
	}
	

	public Node(Vector2 position, Array<Edge> connectedPaths) {
		this.position = position;
		this.connectedEdges = connectedPaths;
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

	public Array<Edge> getConnectedPaths() {
		return connectedEdges;
	}

	public void setConnectedPaths(Array<Edge> connectedPaths) {
		this.connectedEdges = connectedPaths;
	}
	
}
