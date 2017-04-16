package com.swarmer.gui.screens;

import com.badlogic.gdx.Screen;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.gui.screens.mainmenu.MainMenuScreen;
import com.swarmer.gui.screens.prelobby.PreLobbyScreen;

/**
 * Created by Matt on 02/28/2017.
 */
public enum ScreenLib {

	MAIN_MENU_SCREEN {
		@Override protected Screen getScreenInstance() {
			return new MainMenuScreen(1280, 800);
		}
	},

	PRE_LOBBY_SCREEN {
		@Override protected Screen getScreenInstance() {
			return new PreLobbyScreen(1280, 800);
		}
	},

	LOBBY_SCREEN {
		@Override protected Screen getScreenInstance () {
			return new LobbyScreen(1280, 800);
		}
	},

	GAME_SCREEN {
		@Override protected Screen getScreenInstance() {
			return new com.swarmer.gui.screens.game.GameScreen();
		}
	};

	protected abstract Screen getScreenInstance();

}
