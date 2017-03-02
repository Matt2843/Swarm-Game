package com.swarmer.gui.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.IntMap;
import com.swarmer.game.SwarmerMain;

/**
 * Created by Matt on 02/28/2017.
 */
public final class ScreenManager {

    // The only static instance to be used.
    private static ScreenManager instance;

    private SwarmerMain game;

    private Screen currentScreen;
    private IntMap<Screen> screens;

    // Prevent instantiation of this static class, use getInstance() method instead.
    private ScreenManager() {
        screens = new IntMap<>();
    }

    public static ScreenManager getInstance() {
        if(instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(SwarmerMain game) {
        this.game = game;
    }

    public void show(ScreenLib screenLib) {
        if(game == null) return;
        if(!screens.containsKey(screenLib.ordinal())) {
            screens.put(screenLib.ordinal(), screenLib.getScreenInstance());
        }
        currentScreen = screens.get(screenLib.ordinal());
        game.setScreen(currentScreen);
    }

    public void dispose(ScreenLib screenLib) {
        if (!screens.containsKey(screenLib.ordinal())) return;
        screens.remove(screenLib.ordinal()).dispose();
    }

    public void dispose() {
        for (com.badlogic.gdx.Screen screen : screens.values()) {
            screen.dispose();
        }
        screens.clear();
        instance = null;
    }

    public Screen getScreen(ScreenLib screenLib) {
        if(screens.containsKey(screenLib.ordinal())) return screens.get(screenLib.ordinal());
        return null;
    }

    public SwarmerMain getGame() {
        return game;
    }

    public com.badlogic.gdx.Screen getScreen() {
        return currentScreen;
    }
}
