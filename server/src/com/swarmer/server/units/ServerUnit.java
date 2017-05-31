package com.swarmer.server.units;

import com.swarmer.server.CoordinationUnitCallable;
import com.swarmer.server.DatabaseControllerCallable;
import com.swarmer.server.Unit;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
			Message msg = new DatabaseControllerCallable(new Message(2, new String[] {String.valueOf(getPort()), getDescription()})).getFutureResult();
			IP = (String) msg.getObject();
		} catch(IOException e) {
			e.printStackTrace();
		}
    }

	public void print() {
		String str = "\n";
		for(Map.Entry<Player, Connection> entry : activeConnections.entrySet()) {
			str += entry.getKey().getUsername() + ", " + entry.getValue().toString() + "\n";
		}
		System.out.println(str);
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

    public boolean addActiveConnection(Player player, Connection connection) throws IOException {
	    if(!activeConnections.containsKey(player)) {
			System.out.println("Add: " + player.getUsername());
            activeConnections.put(player, connection);
			new CoordinationUnitCallable(new Message(1150, new Object[]{player, getDescription(), getPort()})).getFutureResult().getObject();
			//print();
            return true;
        } else {
        	return false;
		}
    }

    public boolean removeActiveConnection(Player player) throws IOException {
		if(activeConnections.containsKey(player)) {
			System.out.println("Remove: " + player.getUsername());
            activeConnections.remove(player);
			new CoordinationUnitCallable(new Message(1152, new Object[] {player, getId()})).getFutureResult().getObject();
			//print();
            return true;
        } else {
			return false;
		}
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
		//print();
		for(Player player : activeConnections.keySet()) { // Check if the suspect is in local activeConnections.
			if(player.getUsername().equals(username)) {
				activeConnections.get(player).sendMessage(message);
				return;
			}
		}
		Message coordinationUnitResponse = new CoordinationUnitCallable(new Message(1153, username)).getFutureResult();
		sendTo(username, (LocationInformation) coordinationUnitResponse.getObject(), null, message);
	}

	public static void sendTo(String username, LocationInformation local, Protocol prt, Message msg) {
		try {
			TCPConnection con = new TCPConnection(new Socket(local.getServerUnitIp(), local.getServerUnitPort()), prt);
			con.start();
			con.sendMessage(new Message(888, new Object[] {msg, username}));
			//con.stopConnection();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void forwardMessage(Message message) throws IOException {
		Message messageToBeForwarded = (Message) ((Object[])message.getObject())[0];
		String to = (String) ((Object[])message.getObject())[1];

		sendToPlayer(to, messageToBeForwarded);
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

		sendToPlayer(to, new Message(34789, getPlayerFromUsername(from)));
	}

	public void addFriendToLobby(Message message) throws IOException {
		Player from = (Player) ((Object[])message.getObject())[0];
		String to = (String) ((Object[])message.getObject())[1];
		String lobbyID = (String) ((Object[])message.getObject())[2];
		InetSocketAddress nodeLocation = (InetSocketAddress) ((Object[])message.getObject())[3];
		sendToPlayer(to, new Message(890, new Object[] {from, lobbyID, nodeLocation}));
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
