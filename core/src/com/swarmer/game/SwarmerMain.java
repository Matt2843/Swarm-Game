package com.swarmer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class SwarmerMain extends ApplicationAdapter implements InputProcessor {
	
	private IsometricTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private TiledMap map;
	
	private float mapWidth, mapHeight;
	private int xOffset, yOffset;
	
	private Vector2 getInBounds(int x, int y) {
		Vector2 vec = new Vector2();
		
		if(camera.position.x + x > mapWidth - camera.viewportWidth / 2){
			vec[0] = (mapWidth - (camera.viewportWidth / 2)) - camera.position.x;
		} else if(camera.position.x + x < camera.viewportWidth / 2){
			vec[0] = (camera.viewportWidth / 2) - camera.position.x;
		}
		
		if(camera.position.y + y > mapHeight - camera.viewportmapHeight / 2) {
			vec[1] = (mapHeight - (camera.viewportmapHeight / 2)) - camera.position.y;
		} else if(camera.position.x + x < camera.viewportWidth / 2){
			vec[1] = (camera.viewportmapHeight / 2) - camera.position.y;
		}
		return vec;
	}
	
	private void handleInput() {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (camera.position.x > Gdx.graphics.getWidth() / 2) {
				camera.translate(-10, 0, 0);
			};
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (camera.position.x < Gdx.graphics.getWidth() * 2) {
				camera.translate(10, 0, 0);
			};
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if (camera.position.y < Gdx.graphics.getHeight() / 2 + 20) {
				camera.translate(0, 10, 0);
			};
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (camera.position.y > (Gdx.graphics.getHeight() / 2 - 15) * -1) {
				camera.translate(0, -10, 0);
			};
		}
	}
	
	private void centerCamera() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		mapWidth = layer.getWidth() * layer.getTileWidth();
		mapHeight = layer.getHeight() * layer.getTileHeight();

		camera.translate(mapWidth / 2, 0);
	}
	
	
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}
	
	@Override
	public void create () {
		map = new TmxMapLoader().load("map.tmx");
		renderer = new IsometricTiledMapRenderer(map);
		camera = new OrthographicCamera();

		Gdx.input.setInputProcessor(this);
		
		centerCamera();
	}

	@Override
	public void render() {
		handleInput();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		
		renderer.setView(camera);
		renderer.render();
	}
	
	@Override
	public void dispose () {
		map.dispose();
		renderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	boolean dragging;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
        dragging = true;
        xOffset = screenX;
        yOffset = screenY;
        return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
        dragging = false;
        return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!dragging) return false;
		camera.translate(getInBounds(-(screenX - xOffset), screenY - yOffset));
		xOffset = screenX;
		yOffset = screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}