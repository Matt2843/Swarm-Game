package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swarmer.screens.MainMenuScreen;

public class SwarmerMain extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {

		batch = new SpriteBatch();
		font = new BitmapFont();

		this.setScreen(new MainMenuScreen(this, 1920, 1080));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
