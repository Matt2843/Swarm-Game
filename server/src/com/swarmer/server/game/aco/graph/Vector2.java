package com.swarmer.server.game.aco.graph;

public class Vector2 {

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public void setLength(float size) {
        x = x / getLength() * size;
        y = y / getLength() * size;
    }

    public float getLength() {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
