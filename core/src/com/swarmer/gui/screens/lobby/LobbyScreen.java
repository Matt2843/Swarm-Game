package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class LobbyScreen extends Stage implements Screen {

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private SpriteBatch spriteBatch;

    private Table contentPane;

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
    }

    private void create() {
        loadBackground();
        contentPane = new Table();
        contentPane.setSize(getWidth(), getHeight());
        contentPane.add(new LobbyWidget(getWidth() / 2, getHeight() / 3));
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
