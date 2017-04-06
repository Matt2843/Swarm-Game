package com.swarmer.shared.communication;

import java.io.Serializable;

/**
 * Created by Matt on 03/16/2017.
 */
public final class Player implements Serializable {

	private String alias = null;
	private int id = 0;

	public Player() {
	}

	public Player(String alias, int id) {
		this.alias = alias;
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override public boolean equals(Object player) {
		if (this == player) return true;
		if (player == null || getClass() != player.getClass()) return false;

		Player that = (Player) player;

		return id == that.id && (alias != null ? player.equals(that.getAlias()) : that.getAlias() == null);
	}

	@Override public int hashCode() {
		int result = alias != null ? alias.hashCode() : 0;
		result = 31 * result + id;
		return result;
	}

	@Override public String toString() {
		return "p" + alias + "i" + id;
	}
}
