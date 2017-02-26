package com.swarmer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.swarmer.utility.Graph;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SwarmerMain extends ApplicationAdapter implements InputProcessor {
	
	private IsometricTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private TiledMap map;

	private ExtendViewport viewport;
	
	private float mapWidth, mapHeight;
	private int xOffset, yOffset;

	public final static float SCALE = 32f;
	public final static float INV_SCALE = 20.f/SCALE;

	public final static float VP_WIDTH = 1280 * INV_SCALE;
	public final static float VP_HEIGHT = 720 * INV_SCALE;

	private ArrayList<Ant> ants = new ArrayList<>();
	
	private Vector2 getInBounds(int x, int y) {
		float vecX = x, vecY = y;
		
		if(camera.position.x + x > mapWidth - camera.viewportWidth / 2 * camera.zoom){
			vecX = mapWidth - camera.viewportWidth / 2 * camera.zoom - camera.position.x;
		} else if(camera.position.x + x < (camera.viewportWidth / 2) * camera.zoom){
			vecX = camera.viewportWidth / 2 * camera.zoom - camera.position.x;
		}

		if(camera.position.y + y > mapHeight / 2 - (camera.viewportHeight / 2) * camera.zoom) {
			vecY =  mapHeight / 2 - camera.viewportHeight / 2 * camera.zoom - camera.position.y;
		} else if(camera.position.y + y < -(mapHeight / 2) + (camera.viewportHeight / 2) * camera.zoom){
			vecY = -mapHeight / 2 + camera.viewportHeight / 2 * camera.zoom - camera.position.y;
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
	
	@Override public void create () {
		map = new TmxMapLoader().load("alb.tmx");
		Graph graph = new Graph(map);
		renderer = new IsometricTiledMapRenderer(map);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);
		Gdx.input.setInputProcessor(this);

		centerCamera();

		for (int i = 0; i < 100; i++) {
			int x = 50; // ThreadLocalRandom.current().nextInt(1, 99);
			int y = 50; // ThreadLocalRandom.current().nextInt(1, 99);

			if (graph.nodes[x][y] != null && graph.nodes[x][y].getConnectedEdges().size > 0) {
				ants.add(new Ant(new Sprite(new Texture("player.png")), (TiledMapTileLayer) map.getLayers().get(1), graph.nodes[x][y]));
			}
		}
	}

	@Override public void render() {
		handleInput();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		
		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
		for (Ant ant : ants) {
			ant.draw(renderer.getBatch());
		}

		renderer.getBatch().end();
	}
	
	Vector3 tp = new Vector3();
	Vector3 tp2 = new Vector3();
	boolean dragging;
	@Override public boolean mouseMoved (int screenX, int screenY) {
		// we can also handle mouse movement without anything pressed
		camera.unproject(tp.set(screenX, screenY, 0));
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		// ignore if its not left mouse button or first touch pointer
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		camera.unproject(tp.set(screenX, screenY, 0));
		dragging = true;
		return true;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		if (!dragging) return false;
		camera.unproject(tp2.set(screenX, screenY, 0));
		camera.translate(getInBounds((int) (tp.x - tp2.x), (int) (tp.y - tp2.y)));
		return true;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		camera.unproject(tp.set(screenX, screenY, 0));
		dragging = false;
		return true;
	}

	@Override public void resize (int width, int height) {
		// viewport must be updated for it to work properly
		viewport.update(width, height, true);
	}

	@Override public void dispose () {
		// disposable stuff must be disposed
	}

	@Override public boolean keyDown (int keycode) {
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean scrolled(int amount) {
		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && ((amount < 0 && camera.zoom > amount * -1) || (amount > 0 && camera.zoom < 3))) {
			camera.zoom += amount * 0.2f;
		}
		return false;
	}
}