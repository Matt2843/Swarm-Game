package com.swarmer.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.swarmer.aco.ai.AntBrain;
import com.swarmer.gui.animations.AnimationLibrary;
import com.swarmer.shared.communication.Player;
import com.swarmer.utility.CoordsTranslator;
import com.swarmer.aco.graph.Node;

public class Ant {

	private float stateTime;
	private Vector2 velocity = new Vector2();

	private float x;
	private float y;
	private float food;

	private AntBrain brain;

	private Vector2 desiredPosition;

	public Ant(Player owner, TiledMapTileLayer layer, Node startNode) {

		brain = new AntBrain(owner, startNode);

		food = 200;
		desiredPosition = CoordsTranslator.getInstance().getScreenCoordinates(startNode.getPosition());

		setX(desiredPosition.x);
		setY(desiredPosition.y);

		stateTime = 0;
	}

	public void draw(Batch batch) {
		float delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		update(delta);

		String animation = "stance_down";

		if (food <= 0) {
			animation = "die_down";
		}

		if(velocity.x > 0 && velocity.y > 0) {
			animation = "running_up_right";
		} else if(velocity.x > 0 && velocity.y == 0) {
			animation = "running_right";
		} else if(velocity.x > 0 && velocity.y < 0) {
			animation = "running_down_right";
		} else if(velocity.x == 0 && velocity.y < 0) {
			animation = "running_down";
		} else if(velocity.x < 0 && velocity.y < 0) {
			animation = "running_down_left";
		} else if(velocity.x < 0 && velocity.y == 0) {
			animation = "running_left";
		} else if(velocity.x < 0 && velocity.y > 0) {
			animation = "running_up_left";
		} else if(velocity.x == 0 && velocity.y > 0) {
			animation = "running_up";
		}

		AnimationLibrary.antAnimation.get(animation).draw(batch, stateTime, getX(), getY());
	}

	private void update(float delta) {

		int speed = 80;

		velocity.x = 0;
		velocity.y = 0;

		if(brain.getPreviousNode().getResource() != null) { // Ophiocordyceps unilateralis
		//	return;
		}

		if (food <= 0) {
			return;
		}

		if(Math.round(getX()) < Math.round(desiredPosition.x)) {
			velocity.x = speed;
		} else if(Math.round(getX()) > Math.round(desiredPosition.x)) {
			velocity.x = -speed;
		}

		if(Math.round(getY()) < Math.round(desiredPosition.y)) {
			velocity.y = speed;
		} else if(Math.round(getY()) > Math.round(desiredPosition.y)) {
			velocity.y = -speed;
		}

		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);

		if(Math.abs(getX() - desiredPosition.x) < 10 && Math.abs(getY() - desiredPosition.y) < 10) {
			desiredPosition = CoordsTranslator.getInstance().getScreenCoordinates(brain.determineNextPath().getNode().getPosition());
			food -= 1;
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
