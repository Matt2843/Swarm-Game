
public class Animation {
		
		public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;
		
		public TextureRegion region;
		
		public float frameDuration;
		public Array<? extends T> keyFrames;
		public com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode;
		
		public float originX, originY;
		public float scaleX, scaleY;
		public float rotation;
		
	public Animation(float frameDuration, Array<? extends T> keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode, float originX, float originY, float scaleX, float scaleY, float rotation) {
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
	
	public Animation(float frameDuration, Array<? extends T> keyFrames, float scaleX, float scaleY) {
		MyAnimation(frameDuration, keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, 0f, 0f, scaleX, scaleY, 0f);
	}
	
	public Animation(float frameDuration, Array<? extends T> keyFrames) {
		MyAnimation(frameDuration, keyFrames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, 0f, 0f, 1f, 1f, 0f);
	}
	
	public void initializeAnimation(){
		animation = new com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>(frameDuration, keyFrames, playMode);
	}
	
	public void draw(Batch batch, float stateTime, float x, float y){
		region = animation.getKeyFrame(stateTime);
		batch.draw(region, x, y, originX, originY, region.getRegionWidth(), region.getRegionWidth(), scaleX, scaleY, rotation);
	}
}



