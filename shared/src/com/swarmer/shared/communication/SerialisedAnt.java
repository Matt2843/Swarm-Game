package com.swarmer.shared.communication;

		import java.io.Serializable;

public class SerialisedAnt implements Serializable {
	public int id;
	public int x;
	public int y;

	public SerialisedAnt(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	public String toString() {
		return "ID: " + id + ", (" + x + ", " + y + ")";
	}
}
