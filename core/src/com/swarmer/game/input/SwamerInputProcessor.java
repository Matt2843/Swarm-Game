package com.swarmer.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.game.GameScreen;
import com.swarmer.gui.screens.mainmenu.MainMenuScreen;
import com.swarmer.utility.CoordsTranslator;

public class SwamerInputProcessor implements InputProcessor {

	private GameScreen parent;
	private OrthographicCamera camera;

	private int PreX = 0;
	private int PreY = 0;

	public SwamerInputProcessor(GameScreen parent) {
		this.parent = parent;
		camera = SwarmerMain.getInstance().camera;
	}


	@Override public boolean mouseMoved(int screenX, int screenY) {
		// we can also handle mouse movement without anything pressed
		//parent.camera.unproject(tp.set(screenX, screenY, 0));
		return false;
	}

	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// ignore if its not left mouse button or first touch pointer
		if(button == Input.Buttons.RIGHT) {
			Vector2 tileCoords = CoordsTranslator.getInstance().getTileCoordinatesFromScreen(screenX, screenY);
			//Temp.spawn((int) tileCoords.x, (int) tileCoords.y);
		} else if(button == Input.Buttons.LEFT || pointer == 0) {
			PreX = screenX;
			PreY = screenY;
			parent.dragging = true;
		}
		return false;
	}

	@Override public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(!parent.dragging) {
			return false;
		}
		camera.translate(parent.getInBounds((int) (-(screenX - PreX) * camera.zoom), (int) ((screenY - PreY) * camera.zoom)));
		PreX = screenX;
		PreY = screenY;
		return false;
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button != Input.Buttons.LEFT || pointer > 0) {
			return false;
		}
		//parent.camera.unproject(tp.set(screenX, screenY, 0));
		parent.dragging = false;
		return false;
	}

	@Override public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ESCAPE) {
			SwarmerMain.getInstance().show(MainMenuScreen.getInstance());
			//ScreenManager.getInstance().show(MainMenuScreen.getInstance());
		}
		return false;
	}

	@Override public boolean keyUp(int keycode) {
		return false;
	}

	@Override public boolean keyTyped(char character) {
		if(character == 'c') {
			parent.centerCamera();
		}
		if(character == 'p'){
			Gdx.graphics.setContinuousRendering(!Gdx.graphics.isContinuousRendering());
		}
		return false;
	}

	@Override public boolean scrolled(int amount) {
		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && ((amount < 0 && camera.zoom > amount * -1) || (amount > 0 && camera.zoom < 3))) {
			camera.zoom += amount * 0.2f;
		}
		return false;
	}
}
