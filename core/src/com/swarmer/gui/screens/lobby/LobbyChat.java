package com.swarmer.gui.screens.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Matt on 03/30/2017.
 */
public class LobbyChat extends Table {

	private Skin defaultSkin;

	public LobbyChat(float width, float height) {
		setSize(width, height);
		defaultSkin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));

		Pixmap tableColor = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888);
		tableColor.setColor(Color.PINK);
		tableColor.fill();

		defaultSkin.add("background", new Texture(tableColor));
		setBackground(defaultSkin.getDrawable("background"));
	}
}
