package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swarmer.screens.ScreenLib;
import com.swarmer.screens.ScreenManager;

public class SwarmerMain extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(ScreenLib.MAIN_MENU_SCREEN);
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
