package com.swarmer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SwamerInputProcessor implements InputProcessor {

	SwarmerMain Parent;

	public SwamerInputProcessor(SwarmerMain Parent){
		this.Parent = Parent;
	}

	Vector3 tp = new Vector3();
	Vector3 tp2 = new Vector3();
	@Override public boolean mouseMoved(int screenX, int screenY) {
		// we can also handle mouse movement without anything pressed
		Parent.camera.unproject(tp.set(screenX, screenY, 0));
		return false;
	}

	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// ignore if its not left mouse button or first touch pointer
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		Parent.camera.unproject(tp.set(screenX, screenY, 0));
		Parent.dragging = true;
		return false;
	}

	@Override public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!Parent.dragging) return false;
		Parent.camera.unproject(tp2.set(screenX, screenY, 0));
		Vector2 tt1 = new Vector2((int) (tp.x - tp2.x), (int) (tp.y - tp2.y));
		Vector2 tt2 = Parent.getInBounds((int) (tp.x - tp2.x), (int) (tp.y - tp2.y));
		if(tt1.equals(tt2)) {
			Parent.camera.translate(tt2);
		} else {
			System.out.println(tp.x + ", " + tp.y + "\n" + tp2.x + ", " + tp2.y + "\n" + tt1.x + ", " + tt1.y + "\n" + tt2.x + ", " + tt2.y + "\n");
		}
		return false;
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		Parent.camera.unproject(tp.set(screenX, screenY, 0));
		Parent.dragging = false;
		return false;
	}

	@Override public boolean keyDown(int keycode) {
		return false;
	}

	@Override public boolean keyUp(int keycode) {
		return false;
	}

	@Override public boolean keyTyped(char character) {
		if(character == 'c'){Parent.centerCamera();}
		return false;
	}

	@Override public boolean scrolled(int amount) {
		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && ((amount < 0 && Parent.camera.zoom > amount * -1) || (amount > 0 && Parent.camera.zoom < 3))) {
			Parent.camera.zoom += amount * 0.2f;
		}
		return false;
	}
}
