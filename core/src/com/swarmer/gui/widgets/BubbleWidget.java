package com.swarmer.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BubbleWidget extends ImageButton {

    private Skin skin;
    private TextureAtlas bubbleAtlas;

    public BubbleWidget(int childCount, String[] childContent, Vector2 position) {
        super(new Skin());
        configureSkin();
    }

    private void configureSkin() {
        skin = new Skin();
        bubbleAtlas = new TextureAtlas(Gdx.files.internal("roundButton.atlas"));
        this.setSkin(skin);
    }
}
