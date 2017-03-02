package com.swarmer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import com.swarmer.ai.AntBrain;
import com.swarmer.utility.CoordsTranslator;
import com.swarmer.utility.Node;

import java.util.HashMap;
import java.util.Map;

public class Ant {

	private float stateTime;
	private Vector2 velocity = new Vector2();

	private HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();

	private CoordsTranslator coordsTranslator;

	private float x;
	private float y;

	private AntBrain brain;

	private Vector2 desiredPosition;

	public Ant(TiledMapTileLayer layer, Node startNode) {

		coordsTranslator = new CoordsTranslator(layer);

		TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Ant/atlas/iceant.atlas"));

		HashMap<String, Float> animationlist = new HashMap<>();
		animationlist.put("running_left",		1f/20f);
		animationlist.put("running_up_left",	1f/20f);
		animationlist.put("running_up",			1f/20f);
		animationlist.put("running_up_right",	1f/20f);
		animationlist.put("running_right",		1f/20f);
		animationlist.put("running_down_right",	1f/20f);
		animationlist.put("running_down",		1f/20f);
		animationlist.put("running_down_left",	1f/20f);
		animationlist.put("stance_down",		1f/8f);

		for (Map.Entry<String, Float> animation : animationlist.entrySet()) {
			animations.put(animation.getKey(), new Animation<TextureRegion>(animation.getValue(), textureAtlas.findRegions(animation.getKey()), Animation.PlayMode.LOOP));
		}

		brain = new AntBrain("Matt", startNode);

		desiredPosition = coordsTranslator.getScreenCoordinates(startNode.getPosition());

		setX(desiredPosition.x);
		setY(desiredPosition.y);

		stateTime = 0;
	}

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

		TextureRegion frame = animations.get(direction).getKeyFrame(stateTime, true);
		batch.draw(frame, getX(), getY(), 0, 0, frame.getRegionWidth(), frame.getRegionHeight(), .35f, .35f, 0);
	}

	private void update(float delta) {

		int speed = 100;

		velocity.x = 0; velocity.y = 0;

		if (brain.getCurrentNode().getResource() != null) {
			return;
		}

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
			desiredPosition = coordsTranslator.getScreenCoordinates(brain.determineNextPath().getNode().getPosition());
		}
	}

	private void setX(float x) {
		this.x = x;
	}

	private void setY(float y) {
		this.y = y;
	}

	private float getX() {
		return x;
	}

	private float getY() {
		return y;
	}
}
