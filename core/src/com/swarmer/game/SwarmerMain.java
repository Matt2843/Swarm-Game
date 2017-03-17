package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.swarmer.gui.animations.AnimationLibrary;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.network.GameClient;

public class SwarmerMain extends Game {

	public static GameClient gameClient;

	public void create() {
		establishNetworkConnection();

		ScreenManager.getInstance().initialize(this);
		AnimationLibrary.getInstance().initializeAntAnimations();

		// TODO: Start main menu at launch
		ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
	}

	private void establishNetworkConnection() {
		gameClient = new GameClient();
	}

	public void render() {
		super.render();
	}

	public void dispose() {
	}
}