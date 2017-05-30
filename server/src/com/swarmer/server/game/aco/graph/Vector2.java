package com.swarmer.server.game.aco.graph;

public class Vector2 {

    public int x;
    public int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public void setLength(int size) {
        x = x / getLength() * size;
        y = y / getLength() * size;
    }

    public int getLength() {
        return (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
