package com.swarmer.gui.screens.mainmenu;

import com.swarmer.gui.widgets.SwarmerScreen;

public class MainMenuScreen extends SwarmerScreen {

	private static MainMenuScreen mainMenuScreenInstance;

	private MainMenuScreen(int width, int height) {
		super(width, height);
	}

	public static MainMenuScreen getInstance() {
		if(mainMenuScreenInstance == null) {
			mainMenuScreenInstance = new MainMenuScreen(1280, 800);
		}
		return mainMenuScreenInstance;
	}

	@Override protected void create() {
		contentPane.add(new MainMenuLoginBox());
	}

	@Override protected void handleInput() {
		/*if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			SwarmerMain.getInstance().show(GameScreen.getInstance());
			//ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
		}*/
	}
}