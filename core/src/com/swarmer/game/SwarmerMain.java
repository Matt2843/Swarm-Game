package com.swarmer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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

public class SwarmerMain extends ApplicationAdapter {
	
	private IsometricTiledMapRenderer renderer;
	public OrthographicCamera camera;
	private TiledMap map;

	private ExtendViewport viewport;
	
	private float mapWidth, mapHeight;

	public final static float SCALE = 1f;
	public final static float INV_SCALE = 1.f/SCALE;

	public final static float VP_WIDTH = 1280 * INV_SCALE;
	public final static float VP_HEIGHT = 720 * INV_SCALE;

	public boolean dragging;

	private ArrayList<Ant> ants = new ArrayList<>();

	@Override public void create () {
		map = new TmxMapLoader().load("alb.tmx");
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		mapWidth = layer.getWidth() * layer.getTileWidth();
		mapHeight = layer.getHeight() * layer.getTileHeight();

		Graph graph = new Graph(map);
		renderer = new IsometricTiledMapRenderer(map);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);

		viewport.apply(false);

		InputMultiplexer IM = new InputMultiplexer();
		IM.addProcessor(new SwamerInputProcessor(this));
		IM.addProcessor(new GestureDetector(new SwarmerGestureDetector(this)));

		Gdx.input.setInputProcessor(IM);

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
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
		for (Ant ant : ants) {ant.draw(renderer.getBatch());}
		renderer.getBatch().end();
	}

	Vector2 vec = new Vector2();

	private void handleInput() {
		vec.set(0, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))	{vec.x += -10;}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){vec.x += 10;}
		if(Gdx.input.isKeyPressed(Input.Keys.UP))	{vec.y += 10;}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))	{vec.y += -10;}
		if(!vec.isZero()){
			camera.translate(getInBounds((int) vec.x, (int) vec.y));
		}
	}

	@Override public void resize(int width, int height) {
		// viewport must be updated for it to work properly
		viewport.update(width, height, true);
	}

	@Override public void dispose () {
		// disposable stuff must be disposed
	}

	public void centerCamera() {
		camera.position.x = mapWidth / 2;
		camera.position.y = 0;
	}

	public Vector2 getInBounds(int x, int y) {
		float vecX = x, vecY = y;

		if(camera.position.x + x > mapWidth - camera.viewportWidth / 2 * camera.zoom){
			vecX = mapWidth - camera.viewportWidth / 2 * camera.zoom - camera.position.x;
		} else if(camera.position.x + x < camera.viewportWidth / 2 * camera.zoom){
			vecX = camera.viewportWidth / 2 * camera.zoom - camera.position.x;
		}

		if(camera.position.y + y > mapHeight / 2 - camera.viewportHeight / 2 * camera.zoom) {
			vecY =  mapHeight / 2 - camera.viewportHeight / 2 * camera.zoom - camera.position.y + 15;
		} else if(camera.position.y + y < -(mapHeight / 2) + camera.viewportHeight / 2 * camera.zoom){
			vecY = -mapHeight / 2 + camera.viewportHeight / 2 * camera.zoom - camera.position.y + 15;
		}
		return new Vector2(vecX, vecY);
	}
}