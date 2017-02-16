package com.swarmer.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Node {
	
	private final Vector2 position;
	private Array<Path> connectedPaths;
	
	private Resource resource = null;
	

	public Node(Vector2 position, Array<Path> connectedPaths) {
		this.position = position;
		this.connectedPaths = connectedPaths;
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

	public Array<Path> getConnectedPaths() {
		return connectedPaths;
	}

	public void setConnectedPaths(Array<Path> connectedPaths) {
		this.connectedPaths = connectedPaths;
	}
	
}
