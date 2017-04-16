package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.widgets.SwarmerScreen;

public class MainMenuScreen extends SwarmerScreen {

	public MainMenuScreen(int width, int height) {
		super(width, height);
	}

	@Override protected void create() {
		contentPane.add(new MainMenuLoginBox());
	}

	@Override protected void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
		}
	}
}