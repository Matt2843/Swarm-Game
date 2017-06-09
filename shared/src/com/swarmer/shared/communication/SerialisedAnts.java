package com.swarmer.shared.communication;

import java.io.Serializable;
import java.util.ArrayList;

public class SerialisedAnts implements Serializable {
    private static final long serialVersionUID = -1337L;
	public ArrayList<SerialisedAnt> ants;
	public int size = 0;

	public SerialisedAnts() {
		ants = new ArrayList<>();
	}

	public void addAnt(int id, int x, int y) {
		ants.add(new SerialisedAnt(id, x, y));
	}
}
