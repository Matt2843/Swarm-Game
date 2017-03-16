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

		String[] runningAnimations = {"running_left", "running_up_left", "running_up", "running_up_right", "running_right", "running_down_right", "running_down", "running_down_left"};
		
		for (String str : runningAnimations) {
			antAnimation.put(str, new Animation<TextureRegion>(1f/20f, textureAtlas.findRegions(str), 1, 1));
		}

		String[] dieAnimations = {"die_left", "die_up_left", "die_up", "die_up_right", "die_right", "die_down_right", "die_down", "die_down_left"};

		for (String str : dieAnimations) {
			antAnimation.put(str, new Animation<TextureRegion>(1f/2f, textureAtlas.findRegions(str), com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL, 1, 1));
		}
		
		antAnimation.put("stance_down", new Animation<TextureRegion>(1f/4f, textureAtlas.findRegions("stance_down"), com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG, 1, 1));
	}

	public static AnimationLibrary getInstance() {
		if(instance == null) {
			instance = new AnimationLibrary();
		}
		return instance;
	}

}
