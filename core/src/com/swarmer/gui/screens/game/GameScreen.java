package com.swarmer.gui.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.swarmer.aco.graph.Graph;
import com.swarmer.game.SwarmerMain;
import com.swarmer.game.input.SwamerInputProcessor;
import com.swarmer.game.input.SwarmerGestureDetector;
import com.swarmer.game.structures.Hive;
import com.swarmer.game.units.Ant;
import com.swarmer.gui.widgets.SwarmerScreen;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.resources.Food;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen extends SwarmerScreen {

	private final static float SCALE = 1f;
	private final static float INV_SCALE = 1.f / SCALE;
	private final static float VP_WIDTH = 1280 * INV_SCALE;
	private final static float VP_HEIGHT = 720 * INV_SCALE;
	public static TiledMap map;
	public Graph graph;
	public boolean dragging;
	private IsometricTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private ExtendViewport viewport;
	private float mapWidth, mapHeight;
	private InputMultiplexer IM;
	private Vector2 vec = new Vector2();
	private ArrayList<Ant> ants;
	private ArrayList<Hive> hives;
	private int[] backgroundLayers;
	private int[] foregroundLayers;

	private static GameScreen gameScreenInstance;

	private GameScreen(int width, int height, String description) {
		super(width, height, description);
	}

	public static GameScreen getInstance() {
		if(gameScreenInstance == null) {
			gameScreenInstance = new GameScreen(1280, 800, "game_screen");
		}
		return gameScreenInstance;
	}

	@Override protected void create() {
		hives = new ArrayList<>();
		ants = new ArrayList<>();
		camera = SwarmerMain.getInstance().camera;
		map = new TmxMapLoader().load("newmap.tmx");
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		mapWidth = layer.getWidth() * layer.getTileWidth();
		mapHeight = layer.getHeight() * layer.getTileHeight();

		backgroundLayers = new int[]{0, 1, 2};
		foregroundLayers = new int[]{3};

		graph = new Graph(map);

		viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);

		viewport.apply(false);

		IM = new InputMultiplexer();
		IM.addProcessor(new SwamerInputProcessor(this));
		IM.addProcessor(new GestureDetector(new SwarmerGestureDetector(this)));

		centerCamera();

		TiledMapTileLayer lay0 = map.getLayers().getByType(TiledMapTileLayer.class).get(2);

		TiledMapTile tile = map.getLayers().getByType(TiledMapTileLayer.class).get(3).getCell(0, 0).getTile();
		for(int i = 0; i < 200; i++) {
			int x = ThreadLocalRandom.current().nextInt(1, 99);
			int y = ThreadLocalRandom.current().nextInt(1, 99);

			if(graph.nodes[x][y] != null && graph.nodes[x][y].getConnectedEdges().size > 0) {
				lay0.setCell(x, y, new TiledMapTileLayer.Cell());
				lay0.getCell(x,y).setTile(tile);
				graph.nodes[x][y].setResource(new Food(100));
			}
		}

		renderer = new IsometricTiledMapRenderer(map);

		int playerCount = 3;
		for(int i = 0; i < playerCount; i++) {
			int x = ThreadLocalRandom.current().nextInt(1, 99);
			int y = ThreadLocalRandom.current().nextInt(1, 99);
			Player player = new Player("Alias", "" + i, 0);
			if(graph.nodes[x][y] != null && graph.nodes[x][y].getConnectedEdges().size > 0) {
				hives.add(new Hive(player, graph.nodes[x][y]));
			}
		}
	}

	@Override public void show() {
		Gdx.input.setInputProcessor(IM);
	}

	@Override public void render(float delta) {
		handleInput();
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		renderer.setView(camera);
		renderer.render(backgroundLayers);

		renderer.getBatch().begin();
		for(Ant ant : ants) {
			ant.draw(renderer.getBatch());
		}

		for(Hive hive: hives) {
			hive.draw(renderer.getBatch());
		}
		renderer.getBatch().end();

		renderer.render(foregroundLayers);
	}

	@Override public void resize(int width, int height) {
		viewport.update(width, height, false);
	}

	@Override public void pause() {

	}

	@Override public void resume() {

	}

	@Override public void hide() {

	}

	@Override public void dispose() {

	}

	public void centerCamera() {
		camera.position.x = mapWidth / 2;
		camera.position.y = 0;
	}

	public Vector2 getInBounds(int x, int y) {
		float vecX = x, vecY = y;

		if(camera.position.x + x > mapWidth - camera.viewportWidth / 2 * camera.zoom) {
			vecX = mapWidth - camera.viewportWidth / 2 * camera.zoom - camera.position.x;
		} else if(camera.position.x + x < camera.viewportWidth / 2 * camera.zoom) {
			vecX = camera.viewportWidth / 2 * camera.zoom - camera.position.x;
		}

		if(camera.position.y + y > mapHeight / 2 - camera.viewportHeight / 2 * camera.zoom) {
			vecY = mapHeight / 2 - camera.viewportHeight / 2 * camera.zoom - camera.position.y;
		} else if(camera.position.y + y < -(mapHeight / 2) + camera.viewportHeight / 2 * camera.zoom) {
			vecY = -mapHeight / 2 + camera.viewportHeight / 2 * camera.zoom - camera.position.y;
		}
		return new Vector2(vecX, vecY);
	}

	@Override protected void handleInput() {
		vec.set(0, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			vec.x += -10 * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			vec.x += 10 * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			vec.y += 10 * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			vec.y += -10 * camera.zoom;
		}
		if(!vec.isZero()) {
			camera.translate(getInBounds((int) vec.x, (int) vec.y));
		}
	}

	public TiledMap getMap() {
		return map;
	}

	public ArrayList<Ant> getAnts() {
		return ants;
	}
}
