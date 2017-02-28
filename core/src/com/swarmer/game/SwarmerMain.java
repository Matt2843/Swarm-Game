package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.swarmer.screens.ScreenLib;
import com.swarmer.screens.ScreenManager;

public class SwarmerMain extends Game {
	
	public void create() {
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(ScreenLib.MAIN_MENU_SCREEN);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
	}
}
