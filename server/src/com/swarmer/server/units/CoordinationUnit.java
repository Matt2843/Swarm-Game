package com.swarmer.server.units;

import com.swarmer.server.protocols.CoordinationProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.server.units.utility.Queue;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoordinationUnit extends ServerUnit {

	private final CoordinationProtocol coordinationProtocol = new CoordinationProtocol(this);
	private static HashMap<Player, LocationInformation> allConnectedUsers = new HashMap<>();
	private static Queue queue;

	private CoordinationUnit() {
		super();
		initializeQueue();
	}

	@Override public int getPort() {
		return ServerUnit.COORDINATE_UNIT_TCP_PORT;
	}

	@Override protected ServerProtocol getProtocol() {
		return coordinationProtocol;
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
		if(player != null) {
			System.out.println("Add:" + player.getUsername());
			if(!allConnectedUsers.containsKey(player)) {
				allConnectedUsers.put(player, locationInformation);
			} else {
				changeLocationInformation(player, locationInformation);
			}
			printlocations();
		}
	}

	public static void removeConnection(Player player, String serverId) {
		if(player != null) {
			System.out.println("Remove:" + player.getUsername());
			if(allConnectedUsers.containsKey(player) && !allConnectedUsers.get(player).getInetAddress().equals(serverId)) {
				allConnectedUsers.remove(player);
			}
			printlocations();
		}
	}

	public static void printlocations() {
		String str = "";
		for(Map.Entry<Player, LocationInformation> entry : allConnectedUsers.entrySet()) {
			str += entry.getKey().getUsername() + ", " + entry.getValue().toString() + "\n";
		}
		System.out.print(str);
	}

	@Override public String getDescription() {
		return "coordination_units";
	}

	public static void main(String[] args) {
		new CoordinationUnit();
	}

	private void initializeQueue() {
		queue = new Queue();
		queue.run();
	}

	public static Player findPlayerReturnPlayer(Message message) throws IOException {
		for(Player player : allConnectedUsers.keySet()) {
			if(player.getUsername().equals(message.getObject())) {
				return player;
			}
		}

		return null;
	}

	public static void findMatch(ArrayList<Player> players) {
		queue.findMatch(players);
	}
}
