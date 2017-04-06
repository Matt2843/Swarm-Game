package com.swarmer.server.nodes;

import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int usersConnected = 0;

    protected ServerSocket serverSocket;
    protected Socket connection;

    protected TCPConnection motherShipConnection;

    private final String nodeUUID = UUID.randomUUID().toString();

    protected ServerNode(int port) throws IOException {
        initializeServerSocket(port);
        notifyMySQLServer();
        setupMotherShipConnection();
        awaitConnection(connection);
    }

    protected void initializeServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    protected void notifyMySQLServer() {
        // TODO: notify through mothership
    }

    private void awaitConnection(Socket connection) throws IOException {
        while(true) {
            connection = serverSocket.accept();
            handleConnection(connection);
        }
    }

    protected abstract void setupMotherShipConnection() throws IOException;

    public abstract String getDescription();

    protected abstract void handleConnection(Socket connection) throws IOException;

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
