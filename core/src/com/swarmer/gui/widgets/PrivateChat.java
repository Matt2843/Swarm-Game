package com.swarmer.gui.widgets;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swarmer.gui.StyleSheet;

/**
 * Created by Matt on 04/26/2017.
 */
public class PrivateChat extends ChatWidget {

	private Label chatArea = new Label("", StyleSheet.defaultSkin);

	public PrivateChat(String title, int widgetNo) {
		super(title, widgetNo);
		description.addListener(new ClickListener(Input.Buttons.RIGHT) {
			@Override public void clicked(InputEvent event, float x, float y) {
				remove();
				//TODO: Handle this correctly
			}
		});
		scrollableObject.add(chatArea).expand().fill();
	}
}
