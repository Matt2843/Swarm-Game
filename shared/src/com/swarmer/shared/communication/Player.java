package com.swarmer.shared.communication;

import java.io.Serializable;

/**
 * Created by Matt on 03/16/2017.
 */
public final class Player implements Serializable {

	private String username = null;
	private String id = null;

	public Player(String id, String username) {
		this.id = id;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	@Override public boolean equals(Object player) {
		Player that = (Player) player;
		return this.id.equals(that.getId()) && this.username.equals(that.getUsername());
	}
}
