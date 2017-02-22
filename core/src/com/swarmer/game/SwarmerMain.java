package com.swarmer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.utility.Graph;
import com.swarmer.utility.Node;

public class SwarmerMain extends ApplicationAdapter implements InputProcessor {
	
	private IsometricTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private TiledMap map;
	
	private float mapWidth, mapHeight;
	private int xOffset, yOffset;

	private Ant ant;
	
	private Vector2 getInBounds(int x, int y) {
		float vecX = x, vecY = y;
		
		if(camera.position.x + x > mapWidth - camera.viewportWidth / 2){
			vecX = (mapWidth - (camera.viewportWidth / 2)) - camera.position.x;
		} else if(camera.position.x + x < camera.viewportWidth / 2){
			vecX = (camera.viewportWidth / 2) - camera.position.x;
		}
		
		if(camera.position.y + y > mapHeight / 2 - camera.viewportHeight / 2) {
			vecY = (mapHeight / 2 - (camera.viewportHeight / 2)) - camera.position.y;
		} else if(camera.position.y + y < -(mapHeight / 2) + camera.viewportHeight / 2){
			vecY = -(mapHeight / 2) + (camera.viewportHeight / 2) - camera.position.y;
		}
		return new Vector2(vecX, vecY);
	}
	
	private void handleInput() {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(getInBounds(-10, 0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(getInBounds(10, 0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(getInBounds(0, 10));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(getInBounds(0, -10));
		}

		if(Gdx.input.isKeyPressed(Input.Keys.X)) {
			centerCamera();
		}
	}
	
	private void centerCamera() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		mapWidth = layer.getWidth() * layer.getTileWidth();
		mapHeight = layer.getHeight() * layer.getTileHeight();

		camera.position.x = mapWidth / 2;
		camera.position.y = 0;
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}
	
	@Override
	public void create () {
		map = new TmxMapLoader().load("small_map.tmx");
		new Graph(map);
		renderer = new IsometricTiledMapRenderer(map);
		camera = new OrthographicCamera();
		Gdx.input.setInputProcessor(this);

		centerCamera();

		
		ant = new Ant(
				new Sprite(new Texture("player.png")),
				(TiledMapTileLayer) map.getLayers().get(1)
		);
	}

	@Override
	public void render() {
		handleInput();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		
		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
		ant.draw(renderer.getBatch());
		renderer.getBatch().end();
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