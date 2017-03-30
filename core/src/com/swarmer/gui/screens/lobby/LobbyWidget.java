package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyWidget extends Table {

    public static LobbyUserList2 lobbyUserList;
    public static LobbyChat lobbyChat;

    public LobbyWidget(float width, float height) {
        setSize(width, height);
        lobbyUserList = new LobbyUserList2((float) (width * 0.3), height);
        lobbyChat = new LobbyChat((float) (width * 0.7), height);

        add(lobbyUserList);
        add(lobbyChat);
    }
}
