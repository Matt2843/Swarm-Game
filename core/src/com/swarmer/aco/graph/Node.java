package com.swarmer.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Node {
	
	private final Vector2 position;
	private Array<Edge> connectedEdges;
	
	private Resource resource = null;
	
	public Node(Vector2 position) {
		this.position = position;
		connectedEdges = new Array<Edge>();
	}
	
	public Node(Vector2 position, Array<Edge> connectedEdges) {
		this.position = position;
		this.connectedEdges = connectedEdges;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void addEdge(Edge e) {
		connectedEdges.add(e);
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Array<Edge> getConnectedEdges() {
		return connectedEdges;
	}

	public void setConnectedEdges(Array<Edge> connectedEdges) {
		this.connectedEdges = connectedEdges;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}

	@Override public boolean equals(Object node){
		return position.equals(((Node) node).getPosition());
	}
	
}
