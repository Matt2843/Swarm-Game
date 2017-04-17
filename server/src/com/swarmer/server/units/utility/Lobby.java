package com.swarmer.server.units.utility;

import com.swarmer.shared.communication.Player;

import java.util.ArrayList;

/**
 * Created by Matt on 04/17/2017.
 */
public class Lobby {

	private Player lobbyOwner;
	private String lobbyID;

	private ArrayList<Player> connectedUsers = new ArrayList<>();

	public Lobby(String lobbyID, Player lobbyOwner) {
		this.lobbyID = lobbyID;
		this.lobbyOwner = lobbyOwner;
	}

	public void connectUser(Player player) {
		if(!connectedUsers.contains(player)) {
			connectedUsers.add(player);
		}
	}

	public void removeUser(Player player) {
		if(connectedUsers.contains(player)) {
			connectedUsers.remove(player);
		}
	}

}
