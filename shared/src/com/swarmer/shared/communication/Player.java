package com.swarmer.shared.communication;

import java.io.Serializable;

public final class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username = null;
	private String id = null;
	private int rating;

	public Player(String id, String username, int rating) {
		this.id = id;
		this.username = username;
		this.rating = rating;
	}

	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	public int getRating() {
		return rating;
	}

	@Override public boolean equals(Object player) {
		Player that = (Player) player;
		return this.id.equals(that.getId()) && this.username.equals(that.getUsername());
	}

	@Override public int hashCode() {
		if(id != null) {
			return id.hashCode();
		}
		return -1;
	}

	@Override public String toString() {
		return "Player{" +
				"username='" + username + '\'' +
				", id='" + id + '\'' +
				", rating=" + rating +
				'}';
	}
}
