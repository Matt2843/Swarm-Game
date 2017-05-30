package com.swarmer.shared.communication;

import java.io.Serializable;
import java.util.ArrayList;

public class SerialisedAnts implements Serializable {

	ArrayList<SerialisedAnt> ants;

	public SerialisedAnts() {
		ants = new ArrayList<SerialisedAnt>();
	}

	public void addAnt(int id, int x, int y) {
		ants.add(new SerialisedAnt(id, x, y));
	}

	private class SerialisedAnt implements Serializable {
		public int id;
		public int x;
		public int y;

		public SerialisedAnt(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
}
