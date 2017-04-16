package com.swarmer.gui.screens.prelobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.swarmer.gui.StyleSheet;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.gui.widgets.SwarmerScreen;
import com.swarmer.network.GameClient;

import java.text.Format;

/**
 * Created by Matt on 04/16/2017.
 */
public class PreLobbyScreen extends SwarmerScreen {

	private TextButton startLobbyButton, startCompetitiveGameButton, startCasualGameButton;
	private Label gameModeLabel, playWithFriendsLabel;

	public PreLobbyScreen(int width, int height) {
		super(width, height);
	}

	@Override protected void create() {
		createButtons();

		addActor(contentPane);
	}

	private void createButtons() {
		addLogoutButton();

		startLobbyButton = new TextButton("Start Lobby", StyleSheet.defaultSkin);
		startCompetitiveGameButton = new TextButton("Competitive", StyleSheet.defaultSkin);
		startCasualGameButton = new TextButton("Casual", StyleSheet.defaultSkin);

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
				ScreenManager.getInstance().show(ScreenLib.MAIN_MENU_SCREEN);
				GameClient.getInstance().tcp.stopConnection();
				GameClient.getInstance().tcp = null;
				GameClient.getInstance().establishTCPConnection("127.0.0.1", 1111);
			}
		});
		logout.setPosition(0, Gdx.graphics.getHeight() - logout.getHeight());
		addActor(logout);
	}

	@Override protected void handleInput() {

		// KEY INTERACTION MANAGEMENT


		// BUTTON INTERACTION MANAGEMENT

		startLobbyButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {

				// TODO: Send created lobby flag to server.

				ScreenManager.getInstance().show(ScreenLib.LOBBY_SCREEN);
			}
		});

		startCompetitiveGameButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {

			}
		});

		startCasualGameButton.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {

			}
		});


	}
}
