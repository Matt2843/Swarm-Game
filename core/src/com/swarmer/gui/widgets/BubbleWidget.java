package com.swarmer.gui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Matt on 08-03-2017.
 */
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
