package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.swarmer.gui.animations.AnimationLibrary;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;

public class SwarmerMain extends Game {
	
	public void create() {
		ScreenManager.getInstance().initialize(this);
		AnimationLibrary.getInstance().initializeAntAnimations();

		// TODO: Start main menu at launch
		ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
	}
}