package com.swarmer.server.game.logic.resources;

public class Food extends Resource {

    public Food(int quantity) {
        super(quantity);
    }

    @Override public String getType() {
        return "Food";
    }

}
