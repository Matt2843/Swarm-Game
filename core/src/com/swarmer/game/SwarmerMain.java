package com.swarmer.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.swarmer.gui.animations.AnimationLibrary;
import com.swarmer.gui.screens.game.GameScreen;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.gui.screens.mainmenu.MainMenuScreen;
import com.swarmer.gui.screens.prelobby.PreLobbyScreen;
import com.swarmer.gui.widgets.FriendList;
import com.swarmer.gui.widgets.SwarmerNotification;
import com.swarmer.gui.widgets.SwarmerScreen;
import com.swarmer.network.GameClient;

public class SwarmerMain extends Game {

	private SwarmerScreen currentScreen;
	public OrthographicCamera camera;
    private static SwarmerMain instance;

	private SwarmerMain() {
		// DO NOT INSTANTIATE
        camera = new OrthographicCamera();
	}

    public static SwarmerMain getInstance() {
        if (instance == null) {
            instance = new SwarmerMain();
        }
        return instance;
    }

	public void create() {
		establishNetworkConnection();
		initializeScreens();

		AnimationLibrary.getInstance().initializeAntAnimations();

		show(MainMenuScreen.getInstance());
		//show(LobbyScreen.getInstance());
	}

	private void initializeScreens() {
		MainMenuScreen.getInstance();
		GameScreen.getInstance();
		PreLobbyScreen.getInstance();
		LobbyScreen.getInstance();
	}

	public void show(final SwarmerScreen screen) {
		Gdx.app.postRunnable(new Runnable() {
			@Override public void run() {
				currentScreen = screen;
				setScreen(screen);
				screen.addActor(FriendList.getInstance());
				FriendList.getInstance().openFriendTabs();
			}
		});
	}

	public SwarmerScreen getCurrentScreen() {
		return currentScreen;
	}

	private void establishNetworkConnection() {
		GameClient.getInstance();
	}

	public void dispose() {
		GameClient.getInstance().tcp.stopConnection(GameClient.getInstance().getCurrentPlayer());
	}

	public void showNotification(SwarmerNotification notification) {
		currentScreen.addActor(notification);
	}
}