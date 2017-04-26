package com.swarmer.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.swarmer.gui.StyleSheet;

public class SwarmerNotification extends Table {

	private Label header, text;
	private TextButton close;

	public SwarmerNotification(String headerText, String textText) {
		setSkin(StyleSheet.defaultSkin);
		int width = Gdx.graphics.getWidth() / 10;
		int height = Gdx.graphics.getHeight() / 10;

		Pixmap backgroundColor = new Pixmap(width, height, Pixmap.Format.RGB888);
		backgroundColor.setColor(0.12f,0.12f,0.12f,1);
		backgroundColor.fill();

		setSize(width, height);
		setBackground(new Image(new Texture(backgroundColor)).getDrawable());

		this.header = new Label(headerText, StyleSheet.defaultSkin);
		this.text = new Label(textText, StyleSheet.defaultSkin);
		close = new TextButton("x", StyleSheet.defaultSkin);

		add(header).width(width * 5 / 6).height(height / 6);
		add(close).width(width / 6).height(height / 6);
		row();
		add(text).width(width).height(height * 5 / 6).colspan(2);

		setPosition(Gdx.graphics.getWidth() - width, Gdx.graphics.getHeight() - height);
		autoClose();
	}

	private void autoClose() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		addAction(Actions.fadeOut(0.15f));
	}
}
