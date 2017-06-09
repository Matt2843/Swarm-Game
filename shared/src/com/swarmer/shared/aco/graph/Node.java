package com.swarmer.shared.aco.graph;

import com.swarmer.shared.communication.Player;
import com.swarmer.shared.resources.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {

    private boolean isHome = false;
    public Player owner;

    private final Vector2 position;
    private ArrayList<Edge> connectedEdges;

    public Resource resource = null;

    public Node(Vector2 position) {
        this.position = position;
        connectedEdges = new ArrayList<>();
    }

    public Node(Vector2 position, ArrayList<Edge> connectedEdges) {
        this.position = position;
        this.connectedEdges = connectedEdges;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addEdge(Edge e) {
        connectedEdges.add(e);
    }

    public Boolean isHome(Player player) {
        return isHome && owner.equals(player);
    }

    public void setHome(Player owner) {
        this.isHome = true;
        this.owner = owner;
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

    public ArrayList<Edge> getConnectedEdges() {
        return connectedEdges;
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
