package com.swarmer.screens;

import com.badlogic.gdx.Screen;
import com.swarmer.screens.mainmenu.MainMenuScreen;

/**
 * Created by Matt on 02/28/2017.
 */
public enum ScreenLib {

    MAIN_MENU_SCREEN {
        @Override protected Screen getScreenInstance() {
            return new MainMenuScreen(ScreenManager.getInstance().getGame(), 1280, 800);
        }
    },

    GAME_SCREEN {
        @Override protected Screen getScreenInstance() {
            return new GameScreen(ScreenManager.getInstance().getGame());
        }
    };

    protected abstract Screen getScreenInstance();

}
