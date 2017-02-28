package com.swarmer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.swarmer.game.SwarmerMain;

public class MainMenuScreen extends Stage implements Screen {

	private final SwarmerMain game;

	private TextButton buttonTest;
	private TextButton.TextButtonStyle buttonStyle;
	private BitmapFont font;
	private Skin skin;
	private TextureAtlas buttonAtlas;

	public MainMenuScreen(final SwarmerMain game, int width, int height) {
		this.game = game;
		setViewport(new ExtendViewport(width, height));
		Gdx.input.setInputProcessor(this);
		create();
	}

	private void create() {
		font = new BitmapFont();
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("button.atlas"));
		skin.addRegions(buttonAtlas);
		buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.up = skin.getDrawable("button-up");
		buttonStyle.down = skin.getDrawable("button-down");
		buttonTest = new TextButton("PRESS ME", buttonStyle);
		addActor(buttonTest);
	}

	@Override public void show() {

	}

	@Override public void render(float delta) {
		Gdx.gl.glClearColor( 0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		act(delta);
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
