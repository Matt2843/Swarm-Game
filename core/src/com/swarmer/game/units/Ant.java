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

	private float speed = 100f;
	private float zero  = speed / 5;
	private String animation;

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

		if (food <= 0) {
			animation = "die_down";
		} else {
			String[][] directions = {{"running_up_left", "running_left", "running_down_left"}, {"running_up", "stance_down", "running_down"}, {"running_up_right", "running_right", "running_down_right"}};

			int i = velocity.x > zero ? 2 : (velocity.x < -zero ? 0 : 1);
			int j = velocity.y > zero ? 0 : (velocity.y < -zero ? 2 : 1);
			animation = directions[i][j];
		}
		AnimationLibrary.antAnimation.get(animation).draw(batch, stateTime, getX(), getY());
	}

	private void update(float delta) {
		if(brain.getPreviousNode().getResource() != null) { // Ophiocordyceps unilateralis
		//	return;
		}

		if (food <= 0) {
			return;
		}

		velocity.x = desiredPosition.x - getX();
		velocity.y = desiredPosition.y - getY();

		velocity.setLength(speed);

		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);

		if(Math.abs(Math.round(getX() - desiredPosition.x)) < 10 && Math.abs(Math.round(getY() - desiredPosition.y)) < 10) {
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
