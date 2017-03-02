package com.swarmer.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.swarmer.gui.screens.GameScreen;
import com.swarmer.gui.screens.ScreenLib;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.utility.Temp;

public class SwamerInputProcessor implements InputProcessor {

	private GameScreen Parent;

	private int PreX = 0;
	private int PreY = 0;

	public SwamerInputProcessor(GameScreen Parent){
		this.Parent = Parent;
	}


	@Override public boolean mouseMoved(int screenX, int screenY) {
		// we can also handle mouse movement without anything pressed
		//Parent.camera.unproject(tp.set(screenX, screenY, 0));
		return false;
	}

	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// ignore if its not left mouse button or first touch pointer
		if(button == Input.Buttons.RIGHT) {
			//Temp.spawn((int)(screenX + Parent.camera.position.x), (int)(screenY + Parent.camera.viewportHeight - Parent.camera.position.y));
			Temp.spawn(50, 50);
		}
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		PreX = screenX;
		PreY = screenY;
		Parent.dragging = true;
		return false;
	}

	@Override public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!Parent.dragging) return false;
		Parent.camera.translate(Parent.getInBounds((int) (-(screenX - PreX) * Parent.camera.zoom), (int) ((screenY - PreY) * Parent.camera.zoom)));
		PreX = screenX;
		PreY = screenY;
		return false;
	}

	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		//Parent.camera.unproject(tp.set(screenX, screenY, 0));
		Parent.dragging = false;
		return false;
	}

	@Override public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ESCAPE)
			ScreenManager.getInstance().show(ScreenLib.MAIN_MENU_SCREEN);
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
