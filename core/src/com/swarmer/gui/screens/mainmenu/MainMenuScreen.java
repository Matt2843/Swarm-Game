package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.ScreenManager;

public class MainMenuScreen extends Stage implements Screen {

	private final SwarmerMain game;

	private Group buttonGroup;

	private TextButton[] buttons = new TextButton[5];

	public MainMenuScreen(final SwarmerMain game, int width, int height) {
		this.game = game;
		setViewport(new StretchViewport(width, height));
		create();
	}

	private void create() {
		buttonGroup = new Group();

		for(int i = 0; i < buttons.length; i++) {
			buttons[i] = new TextButton("Button " + (i + 1), new MainMenuButtonSkin());
			final int finalI = i;
			buttons[i].addCaptureListener(new ChangeListener() {
				@Override public void changed(ChangeEvent event, Actor actor) {
					System.out.println("Button " + (finalI + 1) + " pressed.");
				}
			});
		}
		for(int i = 0; i < buttons.length; i++) {
			if(i > 0) {
				buttons[i].setPosition(0, buttons[i - 1].getY() - buttons[i].getHeight());
			} else {
				buttons[i].setPosition(0, 0);
			}
			buttonGroup.addActor(buttons[i]);
		}

		buttons[0].setText("Play");
		buttons[0].addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				ScreenManager.getInstance().show(com.swarmer.gui.screens.ScreenLib.GAME_SCREEN);
			}
		});

		buttons[4].setText("Exit");
		buttons[4].addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});


		buttonGroup.setPosition(getWidth() / 2 - buttons[0].getWidth() / 2, getHeight() / 2 + 5 * buttons[0].getHeight() / 2 - buttons[0].getHeight());
		addActor(buttonGroup);
	}

	@Override public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		act(delta);
		draw();
	}

	@Override public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}

	@Override public void pause() {

	}

	@Override public void resume() {

	}

	@Override public void hide() {

	}

	@Override public void dispose() {
		dispose();
	}
}
