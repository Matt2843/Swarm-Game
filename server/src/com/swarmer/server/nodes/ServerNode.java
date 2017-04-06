package com.swarmer.server.nodes;

import com.swarmer.server.Connection;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private ServerSocket serverSocket;
    private Socket connection;

    public static int usersConnected = 0;
    protected static ArrayList<Connection> activeConnections = new ArrayList<>();

    private final String nodeUUID = UUID.randomUUID().toString();

    protected ServerNode(int port, int sqlPort, String sqlIp) throws IOException {
        initializeServerSocket(port);
        awaitConnection();
    }

    protected void initializeServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void broadcast(Message message) throws IOException {
        for(Connection con : activeConnections) {
            con.sendMessage(message);
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

    public ArrayList<Connection> getActiveConnections() {
        return activeConnections;
    }

    public abstract String generateInsertQuery();
    public abstract String nextInPrimitiveChain();
    public abstract String getDescription();

    protected abstract void awaitConnection();

    public String getNodeUUID() {
        return nodeUUID;
    }

    @Override
    public String toString() {
        return super.toString() + " -- ServerNode{" +
                "nodeId='" + nodeUUID + '\'' + " " + getDescription() +
                '}';
    }
}
