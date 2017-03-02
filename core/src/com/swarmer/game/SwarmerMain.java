package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.swarmer.screens.ScreenLib;
import com.swarmer.screens.ScreenManager;

public class SwarmerMain extends Game {
	
	public void create() {
		ScreenManager.getInstance().initialize(this);

		// TODO: Start main menu at launch
		ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
	}
}