package com.swarmer.server.units;

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
public abstract class ServerUnit {

    public static final int DATABASE_CONTROLLER_TCP_PORT = 43100;
    public static final int DATABASE_CONTROLLER_STCP_PORT = 43101;
    public static final int DATABASE_CONTROLLER_UDP_PORT = 43102;

    public static final int COORDINATE_UNIT_TCP_PORT = 43110;
    public static final int COORDINATE_UNIT_STCP_PORT = 43111;
    public static final int COORDINATE_UNIT_UDP_PORT = 43112;

    public static final int ACCESS_UNIT_TCP_PORT = 43120;
    public static final int ACCESS_UNIT_STCP_PORT = 43121;
    public static final int ACCESS_UNIT_UDP_PORT = 43122;

    public static final int AUTHENTICATION_UNIT_TCP_PORT = 43130;
    public static final int AUTHENTICATION_UNIT_STCP_PORT = 43131;
    public static final int AUTHENTICATION_UNIT_UDP_PORT = 43132;

    public static final int LOBBY_UNIT_TCP_PORT = 43140;
    public static final int LOBBY_UNIT_STCP_PORT = 43141;
    public static final int LOBBY_UNIT_UDP_PORT = 43142;

    public static final int GAME_UNIT_TCP_PORT = 43150;
    public static final int GAME_UNIT_STCP_PORT = 43151;
    public static final int GAME_UNIT_UDP_PORT = 43152;

    private final String nodeUUID = UUID.randomUUID().toString();

    protected ServerSocket serverSocket;
    protected Socket connection;

    protected static HashMap<Player, Connection> activeConnections = new HashMap<>();
    protected int usersConnected = 0;

    protected ServerUnit(int port) {
        try {
            serverSocket = new ServerSocket(port); // TODO: implement abstract initialize server unit i.e. 3 serverSocket fields TCP, STCP and UDP.
            notifyMotherShip();
            awaitConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyMotherShip() {
        try {
            TCPConnection motherShipConnection = new TCPConnection(new Socket("127.0.0.1", 1110), null);
            motherShipConnection.start();
            String[] object = new String[] {String.valueOf(serverSocket.getLocalPort()), getDescription()};
            motherShipConnection.sendMessage(new Message(2, object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void awaitConnection() throws IOException {
        while(true) {
            connection = serverSocket.accept();
            handleConnection(connection);
        }
    }

    public Connection getActiveConnection(Player player) {
        if(activeConnections.containsKey(player))
            return activeConnections.get(player);
        else return null;
    }

    public boolean addActiveConnection(Player player, Connection connection) {
        if(!activeConnections.containsKey(player)) {
            activeConnections.put(player, connection);
            return true;
        } else return false;
    }

    public boolean removeActiveConnection(Player player) {
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
        return super.toString() + " -- ServerUnit{" +
                "nodeId='" + nodeUUID + '\'' + " " + getDescription() +
                '}';
    }

    private static class ServerUnitNotInitializedException extends Throwable {
    }
}
