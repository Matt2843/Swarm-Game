package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Matt on 02/28/2017.
 */
public class MainMenuButtonSkin extends TextButton.TextButtonStyle {

	private Skin skin;
	private TextureAtlas buttonAtlas;

	public MainMenuButtonSkin() {
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("MainMenu/MainMenuButton.atlas"));
		skin.addRegions(buttonAtlas);
		this.font = new BitmapFont();
		this.up = skin.getDrawable("button-up");
		this.down = skin.getDrawable("button-down");
	}
}
