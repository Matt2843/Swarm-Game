package com.swarmer.gui.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationLibrary {

	private static AnimationLibrary instance;


	private static TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Ant/atlas/iceant.atlas"));

	public static HashMap<String, Animation> antAnimation;

	// Prevent instantiation
	private AnimationLibrary() {

	}

	public void initializeAntAnimations() {
		antAnimation = new HashMap<>();

		String[] animationlist = {"running_left", "running_up_left", "running_up", "running_up_right", "running_right", "running_down_right", "running_down", "running_down_left"};
		
		for (String str : animationlist) {
			antAnimation.put(str, new Animation<TextureRegion>(1f/20f, textureAtlas.findRegions(str), 3f, 3f));
		}
		
		antAnimation.put("stance_down", new Animation<TextureRegion>(1f/4f, textureAtlas.findRegions("stance_down"), com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG, .35f, .35f));
	}

	public static AnimationLibrary getInstance() {
		if(instance == null) {
			instance = new AnimationLibrary();
		}
		return instance;
	}

}
