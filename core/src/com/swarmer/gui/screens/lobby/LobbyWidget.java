package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyWidget extends Table {

    public static LobbyUserList2 lobbyUserList;

    public LobbyWidget(float width, float height) {
        lobbyUserList = new LobbyUserList2(width / 2, height);
        lobbyUserList.addUserToList("Georg");
        lobbyUserList.addUserToList("Georg");

        setSize(width, height);
        add(lobbyUserList);
    }
}
