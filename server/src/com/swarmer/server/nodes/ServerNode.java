package com.swarmer.server.nodes;

import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nodeUUID = UUID.randomUUID().toString();
    protected int usersConnected = 0;

    protected ServerSocket serverSocket;
    protected Socket connection;

    protected static HashMap<Player, Connection> activeConnections = new HashMap<>();

    protected ServerNode(int port) {
        try {
            initializeServerSocket(port);
            notifyMotherShip(port);
            awaitConnection(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initializeServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private void notifyMotherShip(int port) {
        try {
            TCPConnection motherShipConnection = new TCPConnection(new Socket("127.0.0.1", 1110), null);
            motherShipConnection.start();
            String[] object = new String[] {String.valueOf(serverSocket.getLocalPort()), getDescription()};
            motherShipConnection.sendMessage(new Message(2, object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void awaitConnection(Socket connection) throws IOException {
        while(true) {
            connection = serverSocket.accept();
            handleConnection(connection);
        }
    }

    protected Connection getActiveConnection(Player player) {
        if(activeConnections.containsKey(player))
            return activeConnections.get(player);
        else return null;
    }

    protected boolean addActiveConnection(Player player, Connection connection) {
        if(!activeConnections.containsKey(player)) {
            activeConnections.put(player, connection);
            return true;
        } else return false;
    }

    protected boolean removeActiveConnection(Player player) {
        if(activeConnections.containsKey(player)) {
            activeConnections.remove(player);
            return true;
        } else return false;
    }

    protected abstract void handleConnection(Socket connection) throws IOException;
    public abstract String getDescription();

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
