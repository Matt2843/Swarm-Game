package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;

public class SwarmerMain extends Game {
	
	public void create() {
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
	}
}