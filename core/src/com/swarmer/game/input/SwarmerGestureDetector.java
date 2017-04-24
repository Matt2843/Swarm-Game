package com.swarmer.game.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.game.GameScreen;
import com.swarmer.utility.CoordsTranslator;

public class SwarmerGestureDetector implements GestureDetector.GestureListener {

	private GameScreen parent;
	private OrthographicCamera camera;
	private float zoom = 0;

	public SwarmerGestureDetector(GameScreen parent) {
		this.parent = parent;
		camera = SwarmerMain.getInstance().camera;
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		zoom = camera.zoom;
		return false;
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override public boolean longPress(float x, float y) {
		Vector2 tileCoords = CoordsTranslator.getInstance().getTileCoordinatesFromScreen((int) x, (int) y);
//		Temp.spawn((int) tileCoords.x, (int) tileCoords.y);
		return false;
	}

	@Override public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override public boolean zoom(float initialDistance, float distance) {
		parent.dragging = false;
		float delta = 1 - (distance - initialDistance) / initialDistance;
		if((delta < 1 && zoom * delta > 1) || (delta > 1 && zoom * delta < 3)) {
			camera.zoom = zoom * delta;
		}
		return false;
	}

	@Override public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		parent.dragging = false;
		return false;
	}

	@Override public void pinchStop() {

	}
}
