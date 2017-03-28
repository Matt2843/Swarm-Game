package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by Matt on 03/28/2017.
 */
public class MainMenuLoginBox extends Table {

    private TextField accountName;
    private TextField password;

    private Label accountNameLabel;
    private Label passwordLabel;

    private Skin textFieldSkin;
    private TextureAtlas defaultAtlas;
    private TextField.TextFieldStyle textFieldStyle;

    public MainMenuLoginBox() {
        createFields();
    }

    private void createFields() {
        textFieldSkin = new Skin();

        defaultAtlas = new TextureAtlas(Gdx.files.internal("default/skin/uiskin.atlas"));

        textFieldSkin.addRegions(defaultAtlas);
        textFieldSkin.add("default", new BitmapFont());

        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = textFieldSkin.getDrawable("textfield");
        textFieldStyle.font = textFieldSkin.getFont("default");

        accountName = new TextField("123", textFieldStyle);
        password = new TextField("321", textFieldStyle);

        //accountNameLabel = new Label("Account Name:", textFieldSkin);
        //passwordLabel = new Label("Password:", textFieldSkin);

        //add(accountNameLabel);
        add(accountName);
        row();
        //add(passwordLabel);
        add(password);
    }
}
