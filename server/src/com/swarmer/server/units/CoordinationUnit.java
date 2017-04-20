package com.swarmer.server.units;

import com.swarmer.server.protocols.CoordinationProtocol;
import com.swarmer.server.protocols.ServerProtocol;
import com.swarmer.server.units.utility.LocationInformation;
import com.swarmer.shared.communication.Player;

import java.util.HashMap;

/**
 * Created by Matt on 04/17/2017.
 */
public class CoordinationUnit extends ServerUnit {

	private final CoordinationProtocol coordinationProtocol = new CoordinationProtocol(this);
	private static HashMap<Player, LocationInformation> allConnectedUsers = new HashMap<>();

	protected CoordinationUnit() {
		super();
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
	}

	public static void removeConnection(Player player) {
		if(allConnectedUsers.containsKey(player)) {
			allConnectedUsers.remove(player);
		}
	}

	@Override public String getDescription() {
		return "coordination_units";
	}

	public static void main(String[] args) {
		new CoordinationUnit();
	}
}
