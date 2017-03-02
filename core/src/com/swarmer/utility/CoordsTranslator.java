package com.swarmer.utility;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CoordsTranslator {

	private OrthographicCamera camera;
	private float tileWidth;
	private float tileHeight;

	public CoordsTranslator(TiledMapTileLayer layer) {
		this.tileWidth = layer.getTileWidth();
		this.tileHeight = layer.getTileHeight();
	}

	public CoordsTranslator(TiledMapTileLayer layer, OrthographicCamera camera) {
		this.tileWidth = layer.getTileWidth();
		this.tileHeight = layer.getTileHeight();

		this.camera = camera;
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

	public Vector2 getTileCoordinatesFromScreen(float screenX, float screenY) {
		Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

		return getTileCoordinates(worldCoords.x, worldCoords.y);
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
