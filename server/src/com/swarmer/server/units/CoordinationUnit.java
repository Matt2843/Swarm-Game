package com.swarmer.server.units;

import com.swarmer.server.protocols.CoordinationProtocol;
import com.swarmer.server.units.utility.GameQueueEntry;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		if(player != null) {
			if(!allConnectedUsers.containsKey(player)) {
				allConnectedUsers.put(player, locationInformation);
			}
			for(Map.Entry<Player, LocationInformation> entry : allConnectedUsers.entrySet()) {
				System.out.println(entry.getKey().getUsername() + ", " + entry.getValue().toString());
			}
		}
	}

	public static void removeConnection(Player player) {
		if(player != null) {
			if(allConnectedUsers.containsKey(player) && !allConnectedUsers.get(player).getServerUnitDescription().equals("lobby_units")) { // TODO: Find out why new entries are deleted after being added
				allConnectedUsers.remove(player);
			}
			for(Map.Entry<Player, LocationInformation> entry : allConnectedUsers.entrySet()) {
				System.out.println(entry.getKey().getUsername() + ", " + entry.getValue().toString());
			}
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
			System.out.println(player.getUsername() + " is looking for a game");
		}

		int averageRanking = totalRating / players.size();

		String skillGroup = "HIGH";
		if (averageRanking < 3000) {
			skillGroup = "MID";
		}
		if (averageRanking < 1000) {
			skillGroup = "LOW";
		}

		GameQueueEntry gameQueueEntry = new GameQueueEntry(players);
		queue.get(skillGroup).add(gameQueueEntry);

		boolean foundGame = false;

		do {
			for (GameQueueEntry queueEntry : queue.get(skillGroup)) {
				if (queueEntry.equals(gameQueueEntry)) {
					continue;
				}

				if (queueEntry.hasFreeSpots( players.size() )) {
					queueEntry.addPlayers(players);

					queue.get(skillGroup).remove(gameQueueEntry);

					gameQueueEntry = queueEntry;

					if (gameQueueEntry.isFull()) {
						System.out.println("Found a game");
						foundGame = true;
						for (Player player : gameQueueEntry.getPlayers()) {
							LocationInformation locationInformation = findPlayerLocationInformation(player.getUsername());

							System.out.println("Sending request to " + player.getUsername());
							sendTo(locationInformation, null, new Message(13372));
						}
					}
				}
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!foundGame);
	}
}
