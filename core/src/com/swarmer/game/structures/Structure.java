package com.swarmer.game.structures;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Structure {
	private Sprite sprite;

    /*
	 * TODO: Implement abstract class structure for structures ingame containing spawn methods etc.
     */

    public abstract void draw(Batch batch);

	protected void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	protected Sprite getSprite() {
		return this.sprite;
	}
}
