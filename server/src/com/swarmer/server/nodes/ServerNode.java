package com.swarmer.server.nodes;

import com.swarmer.server.Connection;
import com.swarmer.server.MotherShip;
import com.swarmer.shared.exceptions.UnkownServerNodeException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode extends Thread implements Serializable {

    private static int usersConnected = 0;
    private static List<Connection> activeConnections;

    private static final long serialVersionUID = -234873247238948932L;
    private String nodeId;

    public ServerNode() {
        nodeId = UUID.randomUUID().toString();
        activeConnections = new ArrayList<>();
        addNodeToDatabase();
    }

    private void addNodeToDatabase() {
        try {
            MotherShip.addNode(this);
        } catch (UnkownServerNodeException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeConnection(Connection connection) {
        activeConnections.remove(connection);
        usersConnected -= 1;
    }

    public static void addConnection(Connection connection) {
        activeConnections.add(connection);
        usersConnected += 1;
    }

    public abstract String generateInsertQuery();
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
