package com.swarmer.server.units.utility;

import com.swarmer.shared.communication.Player;

import java.util.ArrayList;

public class Lobby {

	private ArrayList<Player> connectedUsers = new ArrayList<>();

	public Lobby() {
	}

	public void connectUser(Player player) {
		if(!connectedUsers.contains(player)) {
			connectedUsers.add(player);
		}
	}

	public ArrayList<Player> getConnectedUsers() {
		return connectedUsers;
	}
}
