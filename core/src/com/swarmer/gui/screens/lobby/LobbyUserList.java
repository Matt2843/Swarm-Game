package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.awt.*;
import java.util.ArrayList;

/**
 * LobbyUserList made to contain the GUI representation of players present in the lobby.
 */
public class LobbyUserList extends Table {

    private Skin defaultSkin;
    private ArrayList<Label> users;

    public LobbyUserList(float width, float height) {
        defaultSkin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));

        Pixmap tableColor = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888);
        tableColor.setColor(Color.BLACK);
        tableColor.fill();



        users = new ArrayList<>();
        setSize(width, height);
        
    }

    private void updateGui() {
        this.clear();
        for(Label user : users) {
            defaults().width(getWidth());
            add(user);
            row();
        }
    }

    public void addUser(String username) {
        users.add(new Label(username, defaultSkin));
        updateGui();
    }

    public void removeUser(String username) {
        for(Label user : users) {
            if(user.getText().equals(username)) {
                users.remove(user);
            }
        }
    }
}
