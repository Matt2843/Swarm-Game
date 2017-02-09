package com.swarmer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class SwarmerMain extends ApplicationAdapter {
	
	private IsometricTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private TiledMap map;
	
	private void input(int keyCode) {
		if(keyCode == Input.Keys.LEFT) {
			camera.position.x -= 200;
		}
		if(keyCode == Input.Keys.RIGHT) {
			camera.position.x += 200;
		}
		if(keyCode == Input.Keys.UP) {
			camera.position.y += 200;
		}
		if(keyCode == Input.Keys.UP) {
			camera.position.y -= 200;
		}
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
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-10, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(10, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 10, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -10, 0);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			camera.rotate(20);
		}
		
		camera.update();
		
		renderer.setView(camera);
		renderer.render();
	}
	
	@Override
	public void dispose () {
		map.dispose();
		renderer.dispose();
	}
}
