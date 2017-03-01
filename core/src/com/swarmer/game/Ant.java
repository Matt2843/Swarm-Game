package com.swarmer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.ai.AntBrain;
import com.swarmer.utility.Node;

public class Ant extends Sprite {

	/** the movement */
	private Vector2 velocity = new Vector2();

	private TiledMapTileLayer layer;
	private float tileWidth;
	private float tileHeight;

	private AntBrain Brain;

	private Vector2 desiredPosition;

	public Ant(TiledMapTileLayer layer, Node startNode) {
		super(new Sprite(new Texture("player.png")));

		Brain = new AntBrain("Matt", startNode);

		this.layer = layer;
		this.tileWidth = layer.getTileWidth();
		this.tileHeight = layer.getTileHeight();

		desiredPosition = getScreenCoordinates(startNode.getPosition());

		setX(desiredPosition.x);
		setY(desiredPosition.y);
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {

		int speed = 100;

		velocity.x = 0; velocity.y = 0;

		if (Math.round(getX()) < Math.round(desiredPosition.x)) {
			velocity.x = speed;
		} else if (Math.round(getX()) > Math.round(desiredPosition.x)) {
			velocity.x = -speed;
		}

		if (Math.round(getY()) < Math.round(desiredPosition.y)) {
			velocity.y = speed;
		} else if (Math.round(getY()) > Math.round(desiredPosition.y)) {
			velocity.y = -speed;
		}

		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);

		if (Math.abs(getX() - desiredPosition.x) < 10 && Math.abs(getY() - desiredPosition.y) < 10){
			desiredPosition = getScreenCoordinates(Brain.determineNextPath().getNode().getPosition());
		}
	}

	public Vector2 getTileCoordinates(Vector2 pos) {
		float x = Math.round(( ( tileWidth * pos.y - tileHeight * pos.x ) / ( tileWidth*tileHeight) ) * -1);
		float y = Math.round(( tileWidth * pos.y + tileHeight * pos.x ) / ( tileWidth*tileHeight));
		return new Vector2(x,y);
	}

	public Vector2 getScreenCoordinates(Vector2 pos) {
		float x = 0.5f * pos.x * tileWidth + 0.5f * pos.y * tileWidth;
		float y = -0.5f * tileHeight * (pos.x-pos.y);
		return new Vector2(x,y);
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
