package com.swarmer.shared.communication;

import java.io.Serializable;
import java.util.ArrayList;

public class SerialisedAnts implements Serializable {

	public SerialisedAnt[] ants;
	public int size = 0;

	public SerialisedAnts(int size) {
		ants = new SerialisedAnt[size];
	}

	public SerialisedAnt getAnt(int index) {
		return ants[index];
	}

	public void addAnt(int index, int id, int x, int y) {
		if(index < ants.length) {
			ants[index] = new SerialisedAnt(id, x, y);
			size++;
		}
	}
}
