package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.swarmer.gui.StyleSheet;

public class LobbyScreen extends Stage implements Screen {

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private SpriteBatch spriteBatch;

    private Table contentPane;

    public static LobbyChat lobbyChat;
    private LobbyUserList2 lobbyUserList2;

    public LobbyScreen(int width, int height) {
        setViewport(new StretchViewport(width, height));
        create();
    }

    private void loadBackground() {
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("swarmer-v1.png"));
        backgroundSprite = new Sprite(backgroundTexture);
    }

    private void handleInput() {
        // TODO: Handle user input in the game lobby screen
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            lobbyChat.pressSendInput();
        }
    }

    private void create() {
        loadBackground();
        contentPane = new Table();
        contentPane.setSize(getWidth(), getHeight());

        final Table middleSection = new Table();

        lobbyUserList2 = new LobbyUserList2((float) (getWidth() * 0.8 * 0.3), getHeight() / 2);
        lobbyChat = new LobbyChat((float) (getWidth() * 0.8 * 0.7), getHeight() / 2);

        middleSection.add(lobbyUserList2);
        middleSection.add(lobbyChat);

        contentPane.add(middleSection);
        contentPane.row();

        // TODO: Delete these test buttons
        TextButton test1 = new TextButton("Add Georg", StyleSheet.defaultSkin);
        TextButton test2 = new TextButton("Remove Georg", StyleSheet.defaultSkin);

        test1.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                lobbyUserList2.addUserToList("Georg");
            }
        });

        test2.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                lobbyUserList2.removeUserFromList("Georg");
            }
        });

        contentPane.add(test1);
        contentPane.row();
        contentPane.add(test2);

        addActor(contentPane);
        // TODO: Add actors here.
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override public void render(float delta) {
        handleInput();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        draw();
        act(delta);
    }

    @Override public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public void hide() {

    }

    @Override public void dispose() {
        super.dispose();
        spriteBatch.dispose();
        backgroundTexture.dispose();
    }
}
