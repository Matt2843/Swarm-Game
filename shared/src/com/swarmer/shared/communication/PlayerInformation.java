package com.swarmer.shared.communication;

import java.io.Serializable;

/**
 * Created by Matt on 03/16/2017.
 */
public final class PlayerInformation implements Serializable {

    private String playerAlias;
    private int playerId;

    public PlayerInformation(String playerAlias, int playerId) {
        this.playerAlias = playerAlias;
        this.playerId = playerId;
    }

    public String getPlayerAlias() {
        return playerAlias;
    }

    public void setPlayerAlias(String playerAlias) {
        this.playerAlias = playerAlias;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInformation that = (PlayerInformation) o;

        if (playerId != that.playerId) return false;
        return playerAlias != null ? playerAlias.equals(that.playerAlias) : that.playerAlias == null;
    }

    @Override public int hashCode() {
        int result = playerAlias != null ? playerAlias.hashCode() : 0;
        result = 31 * result + playerId;
        return result;
    }

    @Override public String toString() {
        return "p" + playerAlias + "i" + playerId;
    }

}
