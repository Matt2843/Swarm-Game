package com.swarmer.game.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.swarmer.aco.graph.Node;
import com.swarmer.gui.screens.ScreenManager;
import com.swarmer.utility.CoordsTranslator;
import com.swarmer.utility.Temp;

public class Hive extends Structure {

	private final OrthographicCamera camera;
	private Vector3 touchPoint = new Vector3();


	public Hive(Node placement) {
		Vector2 position = CoordsTranslator.getInstance().getScreenCoordinates(placement.getPosition());

		Sprite sprite = new Sprite(new Texture("structures/hive.png"));
		sprite.setX(position.x);
		sprite.setY(position.y);

		setSprite(sprite);

		this.camera = ScreenManager.getInstance().camera;
	}

	@Override
	public void draw(Batch batch) {
		batch.draw(getSprite(), getSprite().getX(), getSprite().getY());
		update();
	}

	private void update() {
		if(Gdx.input.justTouched())
		{
			Vector3 worldCoords = camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if(getSprite().getBoundingRectangle().contains(worldCoords.x,worldCoords.y))
			{

				Temp.spawn(CoordsTranslator.getInstance().getTileCoordinates(worldCoords.x, worldCoords.y));
			}
		}
	}
}
