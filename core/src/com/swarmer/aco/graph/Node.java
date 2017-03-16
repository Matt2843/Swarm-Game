package com.swarmer.aco.graph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.swarmer.shared.resources.Resource;

public class Node {

	private boolean isHome = false;
	private String owner;

	private final Vector2 position;
	private Array<com.swarmer.aco.graph.Edge> connectedEdges;
	
	private Resource resource = null;
	
	public Node(Vector2 position) {
		this.position = position;
		connectedEdges = new Array<>();
	}
	
	public Node(Vector2 position, Array<Edge> connectedEdges) {
		this.position = position;
		this.connectedEdges = connectedEdges;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void addEdge(com.swarmer.aco.graph.Edge e) {
		connectedEdges.add(e);
	}

	public Boolean isHome(String playerID) {
		return isHome && owner.equals(playerID);
	}

	public void setHome(String playerID) {
		this.isHome = true;
		this.owner = playerID;
	}
	
	public Boolean hasResource() {
		return resource != null;
	}
	
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Array<com.swarmer.aco.graph.Edge> getConnectedEdges() {
		return connectedEdges;
	}

	public void setConnectedEdges(Array<com.swarmer.aco.graph.Edge> connectedEdges) {
		this.connectedEdges = connectedEdges;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}

	@Override public boolean equals(Object node) {
		return position.equals(((Node) node).getPosition());
	}
	
}
