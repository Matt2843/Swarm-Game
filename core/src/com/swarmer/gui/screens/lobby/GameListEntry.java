package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameListEntry extends Table {

    private Label gameName;
    private Label playerCount;
    private Skin defaultSkin;

    public GameListEntry(String name, String count, float width, float height) {
        defaultSkin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));

        gameName = new Label(name, defaultSkin);
        playerCount = new Label(count, defaultSkin);

        add(gameName).width((float) (width*0.8));
        add(playerCount).width((float) (width*0.2));
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount.setText(playerCount);
    }
}

