package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.game.GameScreen;
import com.swarmer.gui.widgets.SwarmerScreen;

public class MainMenuScreen extends SwarmerScreen {

	private MainMenuLoginBox mainMenuLoginBox;

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
		contentPane.add(MainMenuLoginBox.getInstance());
		setKeyboardFocus(MainMenuLoginBox.getInstance().getUserName());
	}

	@Override protected void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if(MainMenuLoginBox.getInstance().getCreateUser().getText().equals("Create User"))
				MainMenuLoginBox.getInstance().getCreateUser().toggle();
			else MainMenuLoginBox.getInstance().getLogin().toggle();
		}

		/*if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			SwarmerMain.getInstance().show(GameScreen.getInstance());
			//ScreenManager.getInstance().show(ScreenLib.GAME_SCREEN);
		}*/
	}
}