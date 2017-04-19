package com.swarmer.server.units;

import com.swarmer.server.protocols.CoordinationProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Matt on 04/17/2017.
 */
public class CoordinationUnit extends ServerUnit {

	private static CoordinationProtocol coordinationProtocol;

	private static HashMap<Player, LocationInformation> allConnectedUsers = new HashMap<>();

	protected CoordinationUnit(int port) {
		super(port);
		coordinationProtocol = new CoordinationProtocol(this);
	}

	public static LocationInformation findPlayerLocationInformation(String username) {
		for(Player player : allConnectedUsers.keySet()) {
			if(player.getUsername().equals(username)) {
				return allConnectedUsers.get(player);
			}
		}
		return null;
	}

	public static void changeLocationInformation(Player player, LocationInformation locationInformation) {
		if(allConnectedUsers.containsKey(player)) {
			allConnectedUsers.remove(player);
			allConnectedUsers.put(player, locationInformation);
		}
	}

	public static void addConnection(Player player, LocationInformation locationInformation) {
		if(!allConnectedUsers.containsKey(player)) {
			allConnectedUsers.put(player, locationInformation);
		}
	}

	public static void removeConnection(Player player) {
		if(allConnectedUsers.containsKey(player)) {
			allConnectedUsers.remove(player);
		}
	}

	@Override protected void handleConnection(Socket connection) throws IOException {
		TCPConnection tcpConnection = new TCPConnection(connection, coordinationProtocol);
		tcpConnection.start();
	}

	@Override public String getDescription() {
		return "coordination_units";
	}

	public static void main(String[] args) {
		new CoordinationUnit(ServerUnit.COORDINATE_UNIT_TCP_PORT);
	}
}
