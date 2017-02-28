package com.swarmer.game;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.screens.GameScreen;

public class SwarmerGestureDetector implements GestureDetector.GestureListener {

	GameScreen Parent;

	public SwarmerGestureDetector(GameScreen Parent){
		this.Parent = Parent;
	}

	@Override public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override public boolean longPress(float x, float y) {
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
		Parent.dragging = false;
		float delta = (distance - initialDistance) / initialDistance;
		if((delta < 0 && Parent.camera.zoom > delta * -1) || (delta > 0 && Parent.camera.zoom < 3)) {
			Parent.camera.zoom = delta;
		}
		return false;
	}

	@Override public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		Parent.dragging = false;
		return false;
	}

	@Override public void pinchStop() {

	}
}
