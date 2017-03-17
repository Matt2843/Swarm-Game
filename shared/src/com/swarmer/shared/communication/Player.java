package com.swarmer.shared.communication;

import java.io.Serializable;

/**
 * Created by Matt on 03/16/2017.
 */
public final class Player implements Serializable {

    private String alias;
    private int ID;

    public Player(String alias, int ID) {
        this.alias = alias;
        this.ID = ID;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override public boolean equals(Object player) {
        if (this == player) return true;
        if (player == null || getClass() != player.getClass()) return false;

        Player that = (Player) player;

        return ID == that.ID && (alias != null ? player.equals(that.getAlias()) : that.getAlias() == null);
    }

    @Override public int hashCode() {
        int result = alias != null ? alias.hashCode() : 0;
        result = 31 * result + ID;
        return result;
    }

    @Override public String toString() {
        return "p" + alias + "i" + ID;
    }
}
