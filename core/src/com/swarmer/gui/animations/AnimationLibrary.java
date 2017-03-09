package com.swarmer.gui.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class AnimationLibrary {

	private static AnimationLibrary instance;


	private static TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Ant/atlas/iceant.atlas"));

	public static HashMap<String, Animation> antAnimation;

	// Prevent instantiation
	private AnimationLibrary() {

	}

	public void initializeAntAnimations() {
		antAnimation = new HashMap<>();

		HashMap<String, Float> animationlist = new HashMap<>();
		animationlist.put("running_left",		1f/20f);
		animationlist.put("running_up_left",	1f/20f);
		animationlist.put("running_up",			1f/20f);
		animationlist.put("running_up_right",	1f/20f);
		animationlist.put("running_right",		1f/20f);
		animationlist.put("running_down_right",	1f/20f);
		animationlist.put("running_down",		1f/20f);
		animationlist.put("running_down_left",	1f/20f);
		animationlist.put("stance_down",		1f/4f);

		for (Map.Entry<String, Float> animation : animationlist.entrySet()) {
			antAnimation.put(animation.getKey(), new Animation(animation.getValue(), textureAtlas.findRegions(animation.getKey()), .35f, .35f));
		}
	}

	public static AnimationLibrary getInstance() {
		if(instance == null) {
			instance = new AnimationLibrary();
		}
		return instance;
	}

}
