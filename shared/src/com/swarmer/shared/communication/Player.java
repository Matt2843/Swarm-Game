package com.swarmer.shared.communication;

import java.io.Serializable;

/**
 * Created by Matt on 03/16/2017.
 */
public final class Player implements Serializable {

	private String username = null;
	private String id = null;

	public Player(String username, String id) {
		this.username = username;
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	@Override public boolean equals(Object player) {
		if (this == player) return true;
		if (player == null || getClass() != player.getClass()) return false;

		Player that = (Player) player;

		return id.equals(that.getId()) && username.equals(that.getUsername());
	}
}
