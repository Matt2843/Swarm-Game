package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.StyleSheet;
import com.swarmer.gui.screens.prelobby.PreLobbyScreen;
import com.swarmer.gui.widgets.SwarmerScreen;

public class LobbyScreen extends SwarmerScreen {
    private String lobbyId = "";

    public static LobbyChat lobbyChat;
    private LobbyUserList2 lobbyUserList2;

    private static LobbyScreen lobbyScreenInstance;

    private LobbyScreen(int width, int height, String description) {
        super(width, height, description);
    }

    public static LobbyScreen getInstance() {
        if(lobbyScreenInstance == null) {
            lobbyScreenInstance = new LobbyScreen(1280, 800, "lobby_screen");
        }
        return lobbyScreenInstance;
    }

    @Override protected void create() {
        addReturnButton();
        
        final Table middleSection = new Table();

        lobbyUserList2 = new LobbyUserList2((float) (getWidth() * 0.8 * 0.3), getHeight() / 2);
        lobbyChat = new LobbyChat((float) (getWidth() * 0.8 * 0.7), getHeight() / 2);

        middleSection.add(lobbyUserList2);
        middleSection.add(lobbyChat);

        contentPane.add(middleSection);
        contentPane.row();

        System.out.println("LOBBY SCREEN CREATED :)");
    }

    private void addReturnButton() {
        TextButton returnToPreLobbyScreen = new TextButton("Exit Lobby", StyleSheet.defaultSkin);
        returnToPreLobbyScreen.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                SwarmerMain.getInstance().show(PreLobbyScreen.getInstance());
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

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
        lobbyChat.updateLobbyStatus();
    }
}