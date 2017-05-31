package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.StyleSheet;
import com.swarmer.gui.screens.prelobby.PreLobbyScreen;
import com.swarmer.gui.widgets.SwarmerScreen;
import com.swarmer.network.GameClient;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;

import java.io.IOException;
import java.util.ArrayList;

public class LobbyScreen extends SwarmerScreen {
    private String lobbyId = "";

    public LobbyChat lobbyChat;
    public TextButton findGame;

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
        
        final Table middleSection = new Table();

        lobbyChat = new LobbyChat((float) (getWidth() * 0.8 * 0.7), getHeight() / 2);

        middleSection.add(LobbyUserList.getInstance());
        middleSection.add(lobbyChat);
        middleSection.row();

        final Table buttonContainer = new Table();

        findGame = new TextButton("Find Game", StyleSheet.defaultSkin);
        findGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    GameClient.getInstance().tcp.sendMessage(new Message(13371, LobbyScreen.getInstance().getLobbyId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonContainer.add(findGame).width(LobbyUserList.getInstance().getWidth() / 2).height(LobbyUserList.getInstance().getHeight() / 4);


        TextButton returnToPreLobbyScreen = new TextButton("Exit Lobby", StyleSheet.defaultSkin);
        buttonContainer.add(returnToPreLobbyScreen).width(LobbyUserList.getInstance().getWidth() / 2).height(LobbyUserList.getInstance().getHeight() / 4);
        returnToPreLobbyScreen.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                SwarmerMain.getInstance().show(PreLobbyScreen.getInstance());
                // TODO: Notify Server that the lobby was cancelled by the owner.
            }
        });


        middleSection.add(buttonContainer).colspan(2).pad(20,0,0,0);
        contentPane.add(middleSection);
        contentPane.row();
    }

    @Override protected void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            lobbyChat.pressSendInput();
        }
    }

    public TextButton getFindGame() {
        return findGame;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
        lobbyChat.updateLobbyStatus();
    }
}