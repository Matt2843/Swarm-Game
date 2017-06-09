package com.swarmer.shared.aco.graph;

import java.io.Serializable;

public class Pheromone implements Serializable {
    private int quantity = 0;

    public Pheromone(int i) {
        quantity = i;
    }

    public void addPheromone(int amount) {
        quantity += amount;
    }

    public int getQuantity() {
        return quantity;
    }
}
