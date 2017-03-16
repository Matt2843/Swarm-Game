package com.swarmer.shared.communication;

import com.swarmer.shared.resources.Resource;

import java.util.HashMap;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class Product {

    private HashMap<Resource, Integer> cost = null;

    public Product(HashMap<Resource, Integer> cost) {
        this.cost = cost;
    }

    public HashMap<Resource, Integer> getCost() {
        return cost;
    }
}
