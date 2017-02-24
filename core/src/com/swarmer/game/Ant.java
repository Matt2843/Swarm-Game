package com.swarmer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.ai.AntBrain;
import com.swarmer.utility.Node;

public class Ant extends Sprite {

	/** the movement */
	private Vector2 velocity = new Vector2();

	private float speed = 60 * 2;
	private TiledMapTileLayer layer;
	private float tileWidth;
	private float tileHeight;

	private AntBrain Brain;


	public Ant(Sprite sprite, TiledMapTileLayer layer, Node startNode) {
		super(sprite);

		Brain = new AntBrain("Matt", startNode);

		this.layer = layer;
		this.tileWidth = layer.getTileWidth();
		this.tileHeight = layer.getTileHeight();

		Vector2 start = getScreenCoordinates(startNode.getPosition());

		setX(start.x);
		setY(start.y);
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float delta) {
		//velocity.y = 0;
		//velocity.x = 0;

		/*if(Gdx.input.isKeyPressed(Input.Keys.A)) {
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
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			Vector2 coords = getScreenCoordinates(8, 8);
			setX(coords.x);
			setY(coords.y);
		}*/

		Vector2 dest = getScreenCoordinates(Brain.determineNextPath().getNode().getPosition());

		//Vector2 tileCoords = getTileCoordinates(getX(), getY());

		//Vector2 nextTileCoords = getTileCoordinates(getX() + velocity.x * delta, getY() + velocity.y * delta);

//		System.out.println(tileCoords.x + " " + tileCoords.y);
		//System.out.println("(" + dest.x + ", " + dest.y + ")");

		setX(dest.x);
		setY(dest.y);
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
