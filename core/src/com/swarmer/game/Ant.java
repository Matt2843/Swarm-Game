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
	private TiledMapTileLayer layer;
	private float tileWidth;
	private float tileHeight;


	public Ant(Sprite sprite, TiledMapTileLayer layer) {
		super(sprite);

		this.layer = layer;
		this.tileWidth = layer.getTileWidth();
		this.tileHeight = layer.getTileHeight();

		setX(0);
		setY(0);
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
		velocity.y = 0;
		velocity.x = 0;

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

		Vector2 tileCoords = getTileCoordinates(getX(), getY());

		Vector2 nextTileCoords = getTileCoordinates(getX() + velocity.x * delta, getY() + velocity.y * delta);

		if (getCell(layer, nextTileCoords) == null)
		{
			setX(getX() + velocity.x * delta);
			setY(getY() + velocity.y * delta);
		}
	}

	public Vector2 getTileCoordinates(float sx, float sy) {
		float x = Math.round(( ( tileWidth * sy - tileHeight * sx ) / ( tileWidth*tileHeight) ) * -1);
		float y = Math.round(( tileWidth * sy + tileHeight * sx ) / ( tileWidth*tileHeight));

		return new Vector2(x,y);
	}

	public Vector2 getScreenCoordinates(float tx, float ty) {
		float x = 0.5f * tx * tileWidth + 0.5f * ty * tileWidth;
		float y = -0.5f * tileHeight * (tx-ty);

		return new Vector2(x,y);
	}

	public TiledMapTileLayer.Cell getCell(TiledMapTileLayer layer, Vector2 coords) {
		return layer.getCell((int) coords.x, (int) coords.y);
	}
}
