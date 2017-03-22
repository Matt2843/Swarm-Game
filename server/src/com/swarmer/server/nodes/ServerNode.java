package com.swarmer.server.nodes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode extends Thread implements Serializable {

    private static final long serialVersionUID = -234873247238948932L;
    private String nodeId;

    private ArrayList<ServerNode> parents;
    private ArrayList<ServerNode> children;

    protected ServerNode() {
        nodeId = UUID.randomUUID().toString();
    }

    protected ServerNode(ArrayList<ServerNode> parents) {
        nodeId = UUID.randomUUID().toString();
        this.parents = parents != null ? parents : new ArrayList<ServerNode>();
    }

    public abstract String getDescription();

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerNode that = (ServerNode) o;

        return nodeId != null ? nodeId.equals(that.nodeId) : that.nodeId == null;
    }

    @Override
    public int hashCode() {
        return nodeId != null ? nodeId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ServerNode{" +
                "nodeId='" + nodeId + '\'' +
                '}';
    }

    public void setParents(ArrayList<ServerNode> parents) {
        this.parents = parents;
    }

    public void setChildren(ArrayList<ServerNode> children) {
        this.children = children;
    }

    public ArrayList<ServerNode> getParents() {
        return parents;
    }

    public ArrayList<ServerNode> getChildren() {
        return children;
    }
}
