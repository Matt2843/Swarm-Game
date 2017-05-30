package com.swarmer.shared.communication;

import java.io.Serializable;
import java.util.ArrayList;

public class SerialisedAnts implements Serializable {

	public ArrayList<SerialisedAnt> ants;

	public SerialisedAnts() {
		ants = new ArrayList<SerialisedAnt>();
	}

	public void addAnt(int id, int x, int y) {
		ants.add(new SerialisedAnt(id, x, y));
	}

	public void clear() {
		ants.clear();
	}
}
