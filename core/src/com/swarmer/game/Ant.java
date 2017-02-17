package com.swarmer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Ant extends Sprite {

	/** the movement */
	private Vector2 velocity = new Vector2();

	private float speed = 60 * 2;
	private TiledMapTileLayer layer;;

	public Ant(Sprite sprite, TiledMapTileLayer layer) {
		super(sprite);

		this.layer = layer;

		setX(0);
		setY(0);
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
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

		float w = layer.getTileWidth(); float h = layer.getTileHeight();
		float sx = getX(); float sy = getY();
		float y = ( w*sy + h*sx )/( w*h );
		float x = (( w*sy - h*sx )/( w*h ))*-1;

		if (layer.getCell((int) (x + velocity.x * delta), (int) (y + velocity.y * delta)) == null) {
			setX(getX() + velocity.x * delta);
			setY(getY() + velocity.y * delta);
		}
	}
}
