package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.swarmer.gui.StyleSheet;

import java.util.ArrayList;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyUserList2 extends Table {
	private final int listSize = 4;

	private Label tooltip;
	private ArrayList<Label> userLabels;

	public LobbyUserList2(float width, float height) {
		setSize(width, height);
		userLabels = new ArrayList<>();

		tooltip = new Label("Users in Lobby:", StyleSheet.defaultSkin);
		top();
		add(tooltip).width(getWidth()).height((float) (getHeight() * 0.08));


		Pixmap darkGrayBackground = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
		darkGrayBackground.setColor(0.2f, 0.2f, 0.2f, 0.9f);
		darkGrayBackground.fill();

		StyleSheet.defaultSkin.add("gray-background", new Texture(darkGrayBackground));
		setBackground(StyleSheet.defaultSkin.getDrawable("gray-background"));
	}

	public void addUserToList(String user) {
		if(userLabels.size() < listSize) {
			Label newUser = new Label(user, StyleSheet.defaultSkin);
			newUser.setAlignment(Align.center);
			userLabels.add(newUser);
		}
		updateGui();
	}

	public void removeUserFromList(String user) {
		for(Label lab : userLabels) {
			if(lab.getText().toString().equals(user)) {
				userLabels.remove(lab);
				break;
			}
		}
		updateGui();
	}

	private void updateGui() {
		this.clear();
		top();
		add(tooltip).width(getWidth()).height((float) (getHeight() * 0.08));
		row();

		for(int i = 0; i < userLabels.size(); i++) {
			add(userLabels.get(i)).width(getWidth()).height((float) ((getHeight() * 0.92) / listSize));
			if(i < userLabels.size() - 1) {
				row();
			}
		}

	}
}
