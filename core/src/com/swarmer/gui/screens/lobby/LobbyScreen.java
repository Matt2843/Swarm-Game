package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swarmer.gui.StyleSheet;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.widgets.SwarmerScreen;

public class LobbyScreen extends SwarmerScreen {

    public LobbyScreen(int width, int height) {
        super(width, height);
    }

    public static LobbyChat lobbyChat;
    private LobbyUserList2 lobbyUserList2;

    @Override protected void create() {
        addReturnButton();
        
        final Table middleSection = new Table();

        lobbyUserList2 = new LobbyUserList2((float) (getWidth() * 0.8 * 0.3), getHeight() / 2);
        lobbyChat = new LobbyChat((float) (getWidth() * 0.8 * 0.7), getHeight() / 2);

        middleSection.add(lobbyUserList2);
        middleSection.add(lobbyChat);

        contentPane.add(middleSection);
        contentPane.row();
    }

    private void addReturnButton() {
        TextButton returnToPreLobbyScreen = new TextButton("Exit Lobby", StyleSheet.defaultSkin);
        returnToPreLobbyScreen.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().show(ScreenLib.PRE_LOBBY_SCREEN);
                // TODO: Notify Server that the lobby was cancelled by the owner.
            }
        });
        returnToPreLobbyScreen.setPosition(0, Gdx.graphics.getHeight() - returnToPreLobbyScreen.getHeight());
        addActor(returnToPreLobbyScreen);
    }

    @Override protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            lobbyChat.pressSendInput();
        }
    }
}