package com.swarmer.server.units;

import com.swarmer.server.Unit;
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
			String[] object = new String[]{String.valueOf(getPort()), getDescription()};
			databaseControllerConnection.sendMessage(new Message(2, object));
		} catch(IOException e) {
			e.printStackTrace();
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
		if(!activeConnections.containsKey(player)) {
			activeConnections.put(player, connection);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeActiveConnection(Player player) {
		if(activeConnections.containsKey(player)) {
			activeConnections.remove(player);
			return true;
		} else {
			return false;
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
