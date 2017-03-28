package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;

public class MainMenuScreen extends Stage implements Screen {

	private Texture backgroundTexture;
	private Sprite backgroundSprite;
	private SpriteBatch spriteBatch;

	private final SwarmerMain game;

	private Group buttonGroup;

	public MainMenuScreen(final SwarmerMain game, int width, int height) {
		this.game = game;
		setViewport(new StretchViewport(width, height));
		create();
	}

	private void loadBackground() {
		spriteBatch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("swarmer-v1.png"));
		backgroundSprite = new Sprite(backgroundTexture);
	}

	private void handleInput() {
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
		}
	}

	private void create() {
		loadBackground();
		buttonGroup = new Group();
		TextButton loginButton = new TextButton("Login", new MainMenuButtonSkin());
		loginButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				addActor(new MainMenuLoginBox());
			}
		});

		buttonGroup.addActor(loginButton);

		buttonGroup.setOrigin(loginButton.getWidth() / 2, loginButton.getHeight() / 2);
		buttonGroup.setPosition(Gdx.graphics.getWidth() / 2 - (loginButton.getWidth() / 2), Gdx.graphics.getHeight() / 5 - (loginButton.getHeight() / 2));

		addActor(buttonGroup);
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
		dispose();
	}
}
