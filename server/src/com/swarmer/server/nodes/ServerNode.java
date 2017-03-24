package com.swarmer.server.nodes;

import com.swarmer.server.MotherShip;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode extends Thread implements Serializable {

    private static final long serialVersionUID = -234873247238948932L;
    private String nodeId;

    public ServerNode() {
        nodeId = UUID.randomUUID().toString();
        addNodeToDatabase();
    }

    public abstract String generateInsertQuery();

    private void addNodeToDatabase() {
        try {
            MotherShip.addNode(this);
        } catch (UnkownServerNodeException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
