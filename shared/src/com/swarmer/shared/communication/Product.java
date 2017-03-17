package com.swarmer.shared.communication;

import com.swarmer.shared.resources.Resource;

import java.util.HashMap;

/**
 * Created by Matt on 03/16/2017.
 */
public abstract class Product {

    private HashMap<Resource, Integer> cost = null;
    private String poductDescription = "null";

    public Product(HashMap<Resource, Integer> cost, String poductDescription) {
        this.cost = cost;
        this.poductDescription = poductDescription;
    }

    public HashMap<Resource, Integer> getCost() {
        return cost;
    }
}
