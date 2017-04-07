package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.swarmer.gui.animations.AnimationLibrary;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.network.GameClient;

import com.swarmer.shared.communication.Message;
import com.swarmer.shared.exceptions.GameClientNotInstantiatedException;

import java.io.IOException;

public class SwarmerMain extends Game {

	public void create() {
		establishNetworkConnection();

		ScreenManager.getInstance().initialize(this);
		AnimationLibrary.getInstance().initializeAntAnimations();

		//ScreenManager.getInstance().show(ScreenLib.LOBBY_SCREEN);
		ScreenManager.getInstance().show(ScreenLib.MAIN_MENU_SCREEN);
	}

	private void establishNetworkConnection() {
		try {
			GameClient.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		try {
			GameClient.getInstance().tcp.sendMessage(new Message(0));
			GameClient.getInstance().cleanUp();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}