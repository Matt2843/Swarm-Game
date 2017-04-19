package com.swarmer.shared.communication;

import java.io.Serializable;

/**
 * Created by Matt on 03/16/2017.
 */
public final class Player implements Serializable {

	private String username = null;
	private String id = null;
	private int ranking;

	public Player(String username, String id, int ranking) {
		this.username = username;
		this.id = id;
		this.ranking = ranking;
	}

	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	public int getRanking() {
		return ranking;
	}

	@Override public boolean equals(Object player) {
		Player that = (Player) player;
		return this.id.equals(that.getId()) && this.username.equals(that.getUsername());
	}
}
