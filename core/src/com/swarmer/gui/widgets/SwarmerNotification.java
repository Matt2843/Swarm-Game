package com.swarmer.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.swarmer.gui.StyleSheet;

import java.io.IOException;

public abstract class SwarmerNotification extends Dialog {


	public SwarmerNotification(String title, String description) {
		super(title, StyleSheet.defaultSkin);
		TextButton accept = new TextButton("Accept", StyleSheet.defaultSkin);
		TextButton reject = new TextButton("Reject", StyleSheet.defaultSkin);

		Label descriptionLabel = new Label(description, StyleSheet.defaultSkin);
		descriptionLabel.setWrap(true);

		getContentTable().add(descriptionLabel).prefWidth(getWidth());
		button(accept);
		button(reject);

		accept.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				try {
					accept();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});

		reject.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				reject();
			}
		});

		setPosition(Gdx.graphics.getWidth() - getWidth(), Gdx.graphics.getHeight() - getHeight());
	}

	public abstract void accept() throws IOException;
	public abstract void reject();
}
