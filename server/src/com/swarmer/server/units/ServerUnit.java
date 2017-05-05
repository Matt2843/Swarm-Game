package com.swarmer.server.units;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.Unit;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ServerUnit extends Unit {

	protected static HashMap<Player, Connection> activeConnections = new HashMap<>();
	private final String nodeUUID = UUID.randomUUID().toString();
	protected int usersConnected = 0;

	protected ServerUnit() {
		super();
		notifyMotherShip();
	}

    private void notifyMotherShip() {
        try {
            String IP = IPGetter.getInstance().getDatabaseControllerIP();
            TCPConnection databaseControllerConnection = new TCPConnection(new Socket(IP, DATABASE_CONTROLLER_TCP_PORT), null);
            databaseControllerConnection.start();
            String[] object = new String[] {String.valueOf(getPort()), getDescription()};
            databaseControllerConnection.sendMessage(new Message(2, object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public boolean hasConnection(Player player) {
		return activeConnections.containsKey(player);
	}

    protected static void sendToRemotePlayer(Player player, Message message) throws IOException {
		if(activeConnections.containsKey(player)) {
			activeConnections.get(player).sendMessage(message);
			System.out.println("Player was in local and message has been sent.");
		} else {
			System.out.println("Player not in local");
		}
    }

    public Connection getActiveConnection(Player player) {
        if(activeConnections.containsKey(player)) {
            return activeConnections.get(player);
		} else {
        	return null;
		}
    }

    public boolean addActiveConnection(Player player, Connection connection) {
		System.out.println("Add: " + player.getUsername());
	    if(!activeConnections.containsKey(player)) {
            activeConnections.put(player, connection);
            return true;
        } else {
        	return false;
		}
    }

    public boolean removeActiveConnection(Player player) {
		System.out.println("Remove: " + player.getUsername());
		if(activeConnections.containsKey(player)) {
            activeConnections.remove(player);
            return true;
        } else return false;
    }

	public void addFriendShip(String user1, String user2) {
		
	}

	public void sendFriendRequest(String from, String to) throws IOException {
		for(Player player : activeConnections.keySet()) { // Check if the suspect is in local activeConnections.
			if(player.getUsername().equals(to)) {
				activeConnections.get(player).sendMessage(new Message(34789, from));
				return;
			}
		}
		Message coordinationUnitResponse = new CoordinationUnitCallable(new Message(1153, to)).getFutureResult();
		sendTo((LocationInformation) coordinationUnitResponse.getObject(), null, new Message(34789, new String[] {from, to}));
		System.out.println(coordinationUnitResponse.toString());
		// TODO: Suspect was not in local activeConnections, get his locationinformation from coordination unit :)
	}

	public static void sendTo(LocationInformation local, Protocol prt, Message msg) {
		try {
			TCPConnection con = new TCPConnection(new Socket(local.getServerUnitIp(), local.getServerUnitPort()), prt);
			con.start();
			con.sendMessage(msg);
			con.stopConnection();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	protected class ServerSocketThread extends Thread {

		private int serverSocketType;

		private ServerSocket serverSocket;
		private DatagramSocket datagramSocket;

		private Socket connection;

		public ServerSocketThread(int serverSocketType) {
			this.serverSocketType = serverSocketType;
		}

		public boolean removeActiveConnection(Player player) {
			if (activeConnections.containsKey(player)) {
				activeConnections.remove(player);
				return true;
			} else {
				return false;
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
