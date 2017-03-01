package com.swarmer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.ai.AntBrain;
import com.swarmer.utility.Node;

import java.util.HashMap;

public class Ant extends Sprite {

	private float stateTime;
	/** the movement */
	private Vector2 velocity = new Vector2();

	private TiledMapTileLayer layer;
	private TextureAtlas textureAtlas;
	private HashMap<String, Animation<TextureRegion>> animations;

	private float tileWidth;
	private float tileHeight;

	private AntBrain Brain;

	private Vector2 desiredPosition;

	public Ant(TiledMapTileLayer layer, Node startNode) {
//		super(new Sprite(new Texture("Ant/iceant/1.png")));

		animations = new HashMap<>();

		textureAtlas = new TextureAtlas(Gdx.files.internal("Ant/atlas/iceant.atlas"));

		String[] animationlist = {
				"running_left",
				"running_up_left",
				"running_up",
				"running_up_right",
				"running_right",
				"running_down_right",
				"running_down",
				"running_down_left",
				"stance_down",
		};

		for (String animation : animationlist) {
			animations.put(animation, new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions(animation), Animation.PlayMode.LOOP));
		}

		Brain = new AntBrain("Matt", startNode);

		this.layer = layer;
		this.tileWidth = layer.getTileWidth();
		this.tileHeight = layer.getTileHeight();

		desiredPosition = getScreenCoordinates(startNode.getPosition());

		setX(desiredPosition.x);
		setY(desiredPosition.y);

		stateTime = 0;
	}

	@Override
	public void draw(Batch batch) {
		float delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		update(delta);

		String direction = "stance_down";
		if (velocity.x > 0 && velocity.y > 0) {
			direction = "running_up_right";
		} else if (velocity.x > 0 && velocity.y == 0) {
			direction = "running_right";
		} else if (velocity.x > 0 && velocity.y < 0) {
			direction = "running_down_right";
		} else if (velocity.x == 0 && velocity.y < 0) {
			direction = "running_down";
		} else if (velocity.x < 0 && velocity.y < 0) {
			direction = "running_down_left";
		} else if (velocity.x < 0 && velocity.y == 0) {
			direction = "running_left";
		} else if (velocity.x < 0 && velocity.y > 0) {
			direction = "running_up_left";
		} else if (velocity.x == 0 && velocity.y > 0) {
			direction = "running_up";
		}

		batch.draw(animations.get(direction).getKeyFrame(stateTime, true), getX(), getY());
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
