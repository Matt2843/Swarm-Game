package com.swarmer.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class BubbleWidget extends Stage {

    private Table table;
    private TextButton[] bubbles;
    private TextButton.TextButtonStyle bubbleStyle;
    private TextureAtlas bubbleAtlas;
    private Skin bubbleSkin;
    private int childCount;

    public BubbleWidget(int childCount) {
        this.childCount = childCount;
        Gdx.input.setInputProcessor(this);
        configureSkin();
    }

    private void configureSkin() {
        table = new Table();

        bubbleAtlas = new TextureAtlas(Gdx.files.internal("roundButton.atlas"));
        bubbleSkin = new Skin();
        bubbleSkin.addRegions(bubbleAtlas);

        bubbleStyle = new TextButton.TextButtonStyle();
        bubbleStyle.up = bubbleSkin.getDrawable("button-up");
        bubbleStyle.down = bubbleSkin.getDrawable("button-down");
        bubbleStyle.font = new BitmapFont();

        bubbles = new TextButton[childCount];

        for(TextButton i : bubbles) {
            i = new TextButton("TEST", bubbleStyle);

            i.addCaptureListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    System.out.println("Hello World");
                }
            });

            table.add(i).width(100);
        }

        addActor(table);
    }
}
