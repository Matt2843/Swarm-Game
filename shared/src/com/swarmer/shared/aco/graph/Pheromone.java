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

    public void decreasePheromone() {
        if(quantity > 0) {
            quantity -= 1;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
