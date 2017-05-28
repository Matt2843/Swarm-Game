package com.swarmer.server.units;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.DatabaseControllerCallable;
import com.swarmer.server.Unit;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
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

	public void addFriendShip(Message message) throws IOException {
		Message response = new DatabaseControllerCallable(message).getFutureResult();
		// Friend Succesfully Added, send message to both.
		if((boolean) response.getObject()) {
			String user1 = ((Player[])message.getObject())[0].getUsername();
			String user2 = ((Player[])message.getObject())[1].getUsername();
			sendToPlayer(user1, new Message(34790, user2));
			sendToPlayer(user2, new Message(34790, user1));
		}
	}

	public void sendToPlayer(String username, Message message) throws IOException {
		for(Player player : activeConnections.keySet()) { // Check if the suspect is in local activeConnections.
			if(player.getUsername().equals(username)) {
				activeConnections.get(player).sendMessage(message);
				return;
			}
		}
		Message coordinationUnitResponse = new CoordinationUnitCallable(new Message(1153, username)).getFutureResult();
		sendTo((LocationInformation) coordinationUnitResponse.getObject(), null, message);
	}

	private static void sendTo(LocationInformation local, Protocol prt, Message msg) {
		try {
			TCPConnection con = new TCPConnection(new Socket(local.getServerUnitIp(), local.getServerUnitPort()), prt);
			con.start();
			con.sendMessage(msg);
			//con.stopConnection();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private Player getPlayerFromUsername(String username) throws IOException {
		for(Player player : activeConnections.keySet()) {
			if(player.getUsername().equals(username)) {
				return player;
			}
		}
		// Was not in local, looking up player...
		Message coordinationUnitResponse = new CoordinationUnitCallable(new Message(1154, username)).getFutureResult();
		return (Player) coordinationUnitResponse.getObject();
	}

	// Object[0] == from, Object[1] == to;
	public void sendFriendRequest(Message message) throws IOException {
		String from = ((String[])message.getObject())[0];
		String to = ((String[])message.getObject())[1];

		sendToPlayer(to, new Message(888, new Object[] {new Message(34789, getPlayerFromUsername(from)), getPlayerFromUsername(to)}));
	}

	public void forwardMessage(Message message) throws IOException {
		Message messageToBeForwarded = (Message) ((Object[])message.getObject())[0];
		Player to = (Player) ((Object[])message.getObject())[1];

		sendToPlayer(to.getUsername(), messageToBeForwarded);
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
