package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyUserList2 extends Table {
	private final int listSize = 4;

	private Skin defaultSkin;

	private ArrayList<Label> userLabels;

	public LobbyUserList2(float width, float height) {
		defaultSkin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
		userLabels = new ArrayList<>();
		setSize(width, height);

		Pixmap tableColor = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888);
		tableColor.setColor(Color.DARK_GRAY);
		tableColor.fill();

		defaultSkin.add("background", new Texture(tableColor));
		setBackground(defaultSkin.getDrawable("background"));
	}

	public void addUserToList(String user) {
		if(userLabels.size() < listSize) {
			Label newUser = new Label(user, defaultSkin);
			newUser.setColor(Color.ORANGE);
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
		for(int i = 0; i < userLabels.size(); i++) {
			add(userLabels.get(i)).expandX().height(getHeight() / listSize);
			if(i < userLabels.size() - 1) {
				row();
			}
		}

	}
}
