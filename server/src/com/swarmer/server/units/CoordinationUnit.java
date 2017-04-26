package com.swarmer.server.units;

import com.swarmer.server.protocols.CoordinationProtocol;
import com.swarmer.server.units.utility.GameQueueEntry;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 04/17/2017.
 */
public class CoordinationUnit extends ServerUnit {

	private final CoordinationProtocol coordinationProtocol = new CoordinationProtocol(this);
	private static HashMap<Player, LocationInformation> allConnectedUsers = new HashMap<>();
	private static HashMap<String, ArrayList<GameQueueEntry>> queue = new HashMap<>();

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
		if(!allConnectedUsers.containsKey(player)) {
			allConnectedUsers.put(player, locationInformation);
		}

		for (Map.Entry<Player, LocationInformation> entry : allConnectedUsers.entrySet()) {
			System.out.println(entry.getKey().getUsername() + ", " + entry.getValue().toString());
		}
	}

	public static void removeConnection(Player player) {
		if(allConnectedUsers.containsKey(player)) {
			allConnectedUsers.remove(player);
		}
		for (Map.Entry<Player, LocationInformation> entry : allConnectedUsers.entrySet()) {
			System.out.println(entry.getKey().getUsername() + ", " + entry.getValue().toString());
		}
	}

	@Override public String getDescription() {
		return "coordination_units";
	}

	public static void main(String[] args) {
		new CoordinationUnit();
	}

	private void initializeQueue() {
		queue.put("LOW", new ArrayList<GameQueueEntry>());
		queue.put("MID", new ArrayList<GameQueueEntry>());
		queue.put("HIGH", new ArrayList<GameQueueEntry>());
	}

	public static void findMatch(ArrayList<Player> players) {
		int totalRating = 0;

		for (Player player : players) {
			totalRating += player.getRating();
		}

		int averageRanking = totalRating / players.size();

		String skillGroup = "HIGH";
		if (averageRanking < 3000) {
			skillGroup = "MID";
		}
		if (averageRanking < 1000) {
			skillGroup = "LOW";
		}

		boolean foundMatch = false;
		for (GameQueueEntry queueEntry : queue.get(skillGroup)) {
			if (queueEntry.hasFreeSpots(players.size())) {
				queueEntry.addPlayers(players);
				foundMatch = true;
			}
		}

		if (!foundMatch) {
			new GameQueueEntry(players);
		}

	}
}
