package com.swarmer.server.units;

import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class ServerUnit {

    public static final int TCP = 0;
    public static final int STCP = 1;
    public static final int UDP = 2;

    public static final int DATABASE_CONTROLLER_TCP_PORT = 43100;
    //public static final int DATABASE_CONTROLLER_STCP_PORT = 43101;
    //public static final int DATABASE_CONTROLLER_UDP_PORT = 43102;

    public static final int COORDINATE_UNIT_TCP_PORT = 43110;
    //public static final int COORDINATE_UNIT_STCP_PORT = 43111;
    //public static final int COORDINATE_UNIT_UDP_PORT = 43112;

    public static final int ACCESS_UNIT_TCP_PORT = 43120;
    //public static final int ACCESS_UNIT_STCP_PORT = 43121;
    //public static final int ACCESS_UNIT_UDP_PORT = 43122;

    public static final int AUTHENTICATION_UNIT_TCP_PORT = 43130;
    //public static final int AUTHENTICATION_UNIT_STCP_PORT = 43131;
    //public static final int AUTHENTICATION_UNIT_UDP_PORT = 43132;

    public static final int LOBBY_UNIT_TCP_PORT = 43140;
    //public static final int LOBBY_UNIT_STCP_PORT = 43141;
    //public static final int LOBBY_UNIT_UDP_PORT = 43142;

    public static final int GAME_UNIT_TCP_PORT = 43150;
    //public static final int GAME_UNIT_STCP_PORT = 43151;
    //public static final int GAME_UNIT_UDP_PORT = 43152;

    private final String nodeUUID = UUID.randomUUID().toString();
	public static KeyPair KEY = null;
    protected static HashMap<Player, Connection> activeConnections = new HashMap<>();
    protected int usersConnected = 0;

	protected ServerUnit() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KEY = kpg.generateKeyPair();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		startConnectionThreads();
		notifyMotherShip();
	}

	protected void startConnectionThreads() {
		new ServerSocketThread(TCP).start();
		new ServerSocketThread(STCP).start();
		//new ServerSocketThread(UDP).start();
	}

    private void notifyMotherShip() {
        try {
            String IP = AccessPoint.getInstance().getDatabaseControllerIP();
            TCPConnection databaseControllerConnection = new TCPConnection(new Socket(IP, DATABASE_CONTROLLER_TCP_PORT), null);
            databaseControllerConnection.start();
            String[] object = new String[] {String.valueOf(getPort()), getDescription()};
            databaseControllerConnection.sendMessage(new Message(2, object));
        } catch (IOException e) {
            e.printStackTrace();
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

    protected class ServerSocketThread extends Thread {

        private int serverSocketType;

        private ServerSocket serverSocket;
        private DatagramSocket datagramSocket;

        private Socket connection;

        public ServerSocketThread(int serverSocketType) {
            this.serverSocketType = serverSocketType;
        }

		private void awaitConnection() throws IOException {
			while(true) {
				if(serverSocketType == TCP) {
					connection = serverSocket.accept();
					new TCPConnection(connection, getProtocol()).start();
				} else if(serverSocketType == STCP) {
					connection = serverSocket.accept();
					new SecureTCPConnection(connection, getProtocol(), KEY, getProtocol().exPublicKey).start();
					// TODO: IMPLEMENT THIS VITAL CODE :)
				} else if(serverSocketType == UDP) {
					// TODO: IMPLEMENT THIS VITAL CODE :)
				}
			}
		}

        @Override public void run() {
            try {
                if (serverSocketType == TCP || serverSocketType == STCP) {
                    serverSocket = new ServerSocket(getPort() + serverSocketType);
                } else if (serverSocketType == UDP) {
                    datagramSocket = new DatagramSocket(getPort() + serverSocketType);
                }
                awaitConnection();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    public abstract int getPort();
    protected abstract ServerProtocol getProtocol();

    public abstract String getDescription();
    public String getNodeUUID() {
        return nodeUUID;
    }

    @Override public String toString() {
        return super.toString() + " -- ServerUnit{" +
                "nodeId='" + nodeUUID + '\'' + " " + getDescription() +
                '}';
    }
}
