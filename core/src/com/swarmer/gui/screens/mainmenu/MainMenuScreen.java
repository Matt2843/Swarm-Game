package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.game.GameScreen;
import com.swarmer.gui.widgets.SwarmerScreen;

public class MainMenuScreen extends SwarmerScreen {

	private static MainMenuScreen mainMenuScreenInstance;

	private MainMenuScreen(int width, int height, String description) {
		super(width, height, description);
	}

	public static MainMenuScreen getInstance() {
		if(mainMenuScreenInstance == null) {
			mainMenuScreenInstance = new MainMenuScreen(1280, 800, "main_menu_screen");
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