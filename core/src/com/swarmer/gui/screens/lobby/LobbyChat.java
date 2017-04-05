package com.swarmer.gui.screens.lobby;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.swarmer.gui.StyleSheet;
import com.swarmer.network.GameClient;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.exceptions.GameClientNotInstantiatedException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyChat extends Table {

	private static TextField userInput;
	public static TextButton sendInput;
	private static Label lobbyStatus;
	private static ScrollPane scrollChat;
	private static Label chatWindow;

	public LobbyChat(float width, float height) {
		setSize(width, height);
		configureWidgets();
		addWidgets();
	}

	private void configureWidgets() {
		lobbyStatus = new Label("Connected to Lobby: Not Connected", StyleSheet.defaultSkin);

		chatWindow = new Label("", StyleSheet.defaultSkin);
		chatWindow.clear();
		chatWindow.setAlignment(Align.topLeft);
		chatWindow.setWrap(true);

		scrollChat = new ScrollPane(chatWindow);
		scrollChat.setForceScroll(false, true);

		userInput = new TextField("", StyleSheet.defaultSkin);

		sendInput = new TextButton("Send", StyleSheet.defaultSkin);
		sendInput.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				if(!userInput.getText().toString().equals("")) {
					try {
						GameClient.getInstance().sendMessage(new Message(301, ""));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (GameClientNotInstantiatedException e) {
						e.printStackTrace();
					}
					//TODO: Delete this shit
					appendToChatWindow("Georg", userInput.getText());
					userInput.setText("");
				}
			}
		});
	}

	public static void appendToChatWindow(String username, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		chatWindow.getText().append("[" + sdf.format(new Timestamp(System.currentTimeMillis())) + "] " + username + ": " + message + "\n");
		chatWindow.invalidateHierarchy();
		scrollChat.layout();
		scrollChat.scrollTo(0, 0, 0, 0);
	}

	private void addWidgets() {
		add(lobbyStatus).width(getWidth()).height((float) (getHeight() * 0.08)).colspan(2);
		row();
		add(scrollChat).width(getWidth()).height((float) (getHeight() * 0.84)).expandX().colspan(2);
		row();
		add(userInput).height((float) (getHeight() * 0.08)).width((float) (getWidth() * 0.8));
		add(sendInput).height((float) (getHeight() * 0.08)).width((float) (getWidth() * 0.2));
	}
}
