package com.swarmer.server.nodes;

import java.io.Serializable;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode extends Thread implements Serializable {

    private static final long serialVersionUID = -234873247238948932L;

    protected String nodeDescription;
    protected int nodeId;

    public ServerNode(String nodeDescription, int nodeId) {
        this.nodeDescription = nodeDescription;
        this.nodeId = nodeId;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerNode that = (ServerNode) o;

        return nodeId == that.nodeId;
    }

    @Override public int hashCode() {
        return nodeId;
    }

    @Override public String toString() {
        return "ServerNode{" +
                "nodeDescription='" + nodeDescription + '\'' +
                ", nodeId=" + nodeId +
                '}';
    }
}
