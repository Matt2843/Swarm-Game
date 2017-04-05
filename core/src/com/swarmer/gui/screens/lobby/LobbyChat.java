package com.swarmer.gui.screens.lobby;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.swarmer.gui.StyleSheet;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyChat extends Table {

	private TextField userInput;
	private TextButton sendInput;

	private Label lobbyStatus;

	private ScrollPane scrollChat;

	private Label chatWindow;

	public LobbyChat(float width, float height) {
		setSize(width, height);
		configureWidgets();
		addWidgets();
	}

	private void configureWidgets() {
		lobbyStatus = new Label("Connected to Lobby: Not Connected", StyleSheet.defaultSkin);

		chatWindow = new Label("", StyleSheet.defaultSkin);
		chatWindow.setAlignment(Align.topLeft);
		chatWindow.setWrap(true);

		scrollChat = new ScrollPane(chatWindow);
		scrollChat.setScrollingDisabled(true, false);
		scrollChat.setupOverscroll(0, 0, 0);

		userInput = new TextField("", StyleSheet.defaultSkin);

		sendInput = new TextButton("Send", StyleSheet.defaultSkin);
		sendInput.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				if(chatWindow.getText().toString().equals("")) {
					chatWindow.clear();
					chatWindow.setText(userInput.getText());
				} else {
					chatWindow.setText(chatWindow.getText() + "\n" + userInput.getText());
					layout();
					scrollChat.scrollTo(0,0,0,0);
				}
			}
		});
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
