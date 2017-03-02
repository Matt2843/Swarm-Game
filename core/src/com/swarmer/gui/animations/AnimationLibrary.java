package com.swarmer.gui.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

/**
 * Created by Matt on 02-03-2017.
 */
public class AnimationLibrary {

    private static AnimationLibrary instance;


    private static TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Ant/atlas/iceant.atlas"));

    public static HashMap<String, Animation<TextureRegion>> antAnimation;

    // Prevent instantiation
    private AnimationLibrary() {
        initializeAntAnimations();

        System.out.println("Hello World");

    }

    private void initializeAntAnimations() {
        antAnimation = new HashMap<>();
        String[] animationlist = {
                "running_left",
                "running_up_left",
                "running_up",
                "running_up_right",
                "running_right",
                "running_down_right",
                "running_down",
                "running_down_left",
                "stance_down",
        };

        for (String animation : animationlist) {
            antAnimation.put(animation, new Animation<TextureRegion>(1f/30f, textureAtlas.findRegions(animation), Animation.PlayMode.LOOP));
        }
    }

    public static AnimationLibrary getInstance() {
        if(instance == null) {
            instance = new AnimationLibrary();
        }
        return instance;
    }

}
