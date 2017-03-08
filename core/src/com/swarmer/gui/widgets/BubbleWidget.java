package com.swarmer.gui.widgets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BubbleWidget extends ImageButton {

    private Skin skin;
    private TextureAtlas bubbleAtlas;

    public BubbleWidget(int childCount, String[] childContent) {
        super(new Skin());
        configureSkin();
    }

    private void configureSkin() {
        skin = new Skin();
        
        this.setSkin(skin);
    }

}
