package com.swarmer.gui.animations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation<T> {
		
		public com.badlogic.gdx.graphics.g2d.Animation<? extends T> animation;
		
		public TextureRegion region;
		
		public float frameDuration;
		public Array<? extends T> keyFrames;
		public com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode;
		
		public float originX, originY;
		public float scaleX, scaleY;
		public float rotation;
		
	public Animation(float frameDuration, Array<? extends T> keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode, float originX, float originY, float scaleX, float scaleY, float rotation) {
		init(frameDuration, keyFrames, playMode, originX, originY, scaleX, scaleY, rotation);
	}
	
	public Animation(float frameDuration, Array<? extends T> keyFrames, float scaleX, float scaleY) {
		init(frameDuration, keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, 0f, 0f, scaleX, scaleY, 0f);
	}
	
	public Animation(float frameDuration, Array<? extends T> keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode, float scaleX, float scaleY) {
		init(frameDuration, keyFrames, playMode, 0f, 0f, scaleX, scaleY, 0f);
	}
	
	public Animation(float frameDuration, Array<? extends T> keyFrames) {
		init(frameDuration, keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, 0f, 0f, 1f, 1f, 0f);
	}
	
	private void init(float frameDuration, Array<? extends T> keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode, float originX, float originY, float scaleX, float scaleY, float rotation){
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
		this.playMode = playMode;
		this.originX = originX;
		this.originY = originY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.rotation = rotation;
		initializeAnimation();
	}
	
	private void initializeAnimation(){
		animation = new com.badlogic.gdx.graphics.g2d.Animation<>(frameDuration, keyFrames, playMode);
	}
	
	public void draw(Batch batch, float stateTime, float x, float y){
		region = (TextureRegion) animation.getKeyFrame(stateTime);
		batch.draw(region, x, y, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 2, 2, rotation);
	}
}



