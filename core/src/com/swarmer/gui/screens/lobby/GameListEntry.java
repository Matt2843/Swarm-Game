package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.swarmer.gui.StyleSheet;

public class GameListEntry extends Table {

    private Label gameName;
    private Label playerCount;

    public GameListEntry(String name, String count, float width, float height) {


        gameName = new Label(name, StyleSheet.defaultSkin);
        playerCount = new Label(count, StyleSheet.defaultSkin);

        add(gameName).width((float) (width*0.8));
        add(playerCount).width((float) (width*0.2));
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount.setText(playerCount);
    }
}

