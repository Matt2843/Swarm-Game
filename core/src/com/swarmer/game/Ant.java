package com.swarmer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Ant extends Sprite {

	/** the movement */
	private Vector2 velocity = new Vector2();

	private float speed = 60 * 2;
	private TiledMapTileLayer layer;
	private OrthographicCamera camera;

	public Ant(Sprite sprite, TiledMapTileLayer layer, OrthographicCamera camera) {
		super(sprite);

		this.layer = layer;
		this.camera = camera;

		setX(100);
		setY(0);
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	public void update(float delta) {
		velocity.y = 0; velocity.x = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			velocity.x = -speed;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			velocity.x = speed;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			velocity.y = speed;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			velocity.y = -speed;
		}
		

		if (layer.getCell((int) ((getX() + velocity.x * delta) / layer.getTileWidth()), (int) ((getY() + velocity.y * delta) / layer.getTileHeight())) == null) {
			setX(getX() + velocity.x * delta);
			setY(getY() + velocity.y * delta);
		}
	}
}
