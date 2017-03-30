package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyWidget extends Table {

    private LobbyUserList lobbyUserList;

    public LobbyWidget(float width, float height) {
        lobbyUserList = new LobbyUserList(getWidth() / 2, getHeight());
        lobbyUserList.addUser("Georg");
        lobbyUserList.addUser("Albert");
        lobbyUserList.addUser("Matt");
        add(lobbyUserList);
        setSize(width, height);
    }
}
