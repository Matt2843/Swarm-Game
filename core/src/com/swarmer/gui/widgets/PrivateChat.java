package com.swarmer.gui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.swarmer.gui.StyleSheet;

/**
 * Created by Matt on 04/26/2017.
 */
public class PrivateChat extends ChatWidget {

	private Label chatArea = new Label("", StyleSheet.defaultSkin);

	public PrivateChat(String title, int widgetNo) {
		super(title, widgetNo);
		scrollableObject.add(chatArea).expand().fill();
	}
}
