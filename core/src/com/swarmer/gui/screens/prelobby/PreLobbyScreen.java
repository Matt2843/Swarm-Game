package com.swarmer.gui.screens.prelobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.StyleSheet;
import com.swarmer.gui.screens.mainmenu.MainMenuScreen;
import com.swarmer.gui.widgets.SwarmerScreen;
import com.swarmer.network.GameClient;
import com.swarmer.shared.communication.IPGetter;
import com.swarmer.shared.communication.Message;

import java.io.IOException;

/**
 * Created by Matt on 04/16/2017.
 */
public class PreLobbyScreen extends SwarmerScreen {

	private TextButton startLobbyButton, startCompetitiveGameButton, startCasualGameButton, cancelSearchButton;
	private Label gameModeLabel, playWithFriendsLabel;

	private static PreLobbyScreen lobbyScreenInstance;

	private PreLobbyScreen(int width, int height, String description) {
		super(width, height, description);
	}

	public static PreLobbyScreen getInstance() {
		if(lobbyScreenInstance == null) {
			lobbyScreenInstance = new PreLobbyScreen(1280, 800, "pre_lobby_screen");
		}
		return lobbyScreenInstance;
	}

	@Override protected void create() {
		createButtons();
		handleButtonInput();
		addActor(contentPane);
	}

	private void handleButtonInput() {
		startLobbyButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				try {
					GameClient.getInstance().tcp.sendMessage(new Message(301, "random"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});


		startCompetitiveGameButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				findGame("competitive");
			}
		});

		startCasualGameButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				findGame("casual");
			}
		});

		cancelSearchButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				cancelSearch();
			}
		});
	}

	private void findGame(String mode) {
		gameModeLabel.setText("Looking for " + mode.toLowerCase() + " game...");

		int size = contentPane.getCells().size;

		contentPane.removeActor(startCompetitiveGameButton);
		contentPane.removeActor(startCasualGameButton);

		contentPane.getCells().removeIndex(size-1);
		contentPane.getCells().removeIndex(size-2);

		contentPane.row();
		contentPane.add(cancelSearchButton).colspan(2).width(350);
	}

	private void cancelSearch() {
		gameModeLabel.setText("Select Game Mode: ");

		int size = contentPane.getCells().size;

		contentPane.removeActor(cancelSearchButton);

		contentPane.getCells().removeIndex(size-1);

		contentPane.row();
		contentPane.add(startCompetitiveGameButton);
		contentPane.add(startCasualGameButton);
	}

	private void createButtons() {
		addLogoutButton();

		startLobbyButton = new TextButton("Start Lobby", StyleSheet.defaultSkin);
		startCompetitiveGameButton = new TextButton("Competitive", StyleSheet.defaultSkin);
		startCasualGameButton = new TextButton("Casual", StyleSheet.defaultSkin);

		cancelSearchButton = new TextButton("Cancel", StyleSheet.defaultSkin);

		gameModeLabel = new Label("Select Game Mode: ", StyleSheet.defaultSkin);
		playWithFriendsLabel = new Label("Play With Friends?", StyleSheet.defaultSkin);

		contentPane.defaults().width(175);
		contentPane.defaults().height(100);
		contentPane.add(playWithFriendsLabel).colspan(2).width(350).height(35);
		contentPane.row();
		contentPane.add(startLobbyButton).colspan(2).width(350).pad(0, 0, 25, 0);
		contentPane.row();
		contentPane.add(gameModeLabel).colspan(2).width(350).height(35);
		contentPane.row();
		contentPane.add(startCompetitiveGameButton);
		contentPane.add(startCasualGameButton);
	}

	private void addLogoutButton() {
		TextButton logout = new TextButton("Logout ", StyleSheet.defaultSkin);
		logout.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				SwarmerMain.getInstance().show(MainMenuScreen.getInstance());
				//ScreenManager.getInstance().show(ScreenLib.MAIN_MENU_SCREEN);
				GameClient.getInstance().tcp.stopConnection(GameClient.getCurrentPlayer());
				GameClient.getInstance().tcp = null;
				GameClient.getInstance().establishTCPConnection(IPGetter.getInstance().getAccessUnitIP(), 43120);
			}
		});
		logout.setPosition(0, Gdx.graphics.getHeight() - logout.getHeight());
		addActor(logout);
	}

	@Override protected void handleInput() {
		// KEY INTERACTION MANAGEMENT

	}
}
