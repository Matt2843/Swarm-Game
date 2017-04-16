package com.swarmer.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Matt on 04/16/2017.
 */
public abstract class SwarmerScreen extends Stage implements Screen {

	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final Texture backgroundTexture = new Texture(Gdx.files.internal("swarmer-v1.png"));

	protected Table contentPane = new Table();


	protected SwarmerScreen(int width, int height) {
		setViewport(new StretchViewport(width, height));
		contentPane.setSize(width, height);
		create();
	}

	protected abstract void create();
	protected abstract void handleInput();

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
