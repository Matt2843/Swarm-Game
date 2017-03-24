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

    public static int usersConnected = 0;
    private static List<Connection> activeConnections;

    private static final long serialVersionUID = 1L;
    private String nodeId;

    public ServerNode() {
        nodeId = UUID.randomUUID().toString();
        activeConnections = new ArrayList<>();

        try {
            MotherShip.addNode(this);
        } catch (UnkownServerNodeException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public void run() {
        while(true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeConnection(Connection connection) {
        activeConnections.remove(connection);
        if(usersConnected > 0) usersConnected -= 1;
    }

    public static void addConnection(Connection connection) {
        activeConnections.add(connection);
        usersConnected += 1;
    }

    public abstract String generateInsertQuery();
    public abstract String getDescription();
    public abstract String nextInPrimitiveChain();

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public int hashCode() {
        return nodeId != null ? nodeId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return super.toString() + " -- ServerNode{" +
                "nodeId='" + nodeId + '\'' + " " + getDescription() +
                '}';
    }
}
