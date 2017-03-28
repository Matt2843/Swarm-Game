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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by Matt on 03/28/2017.
 */
public class MainMenuLoginBox extends Table {

    private TextField accountName;
    private TextField password;

    private Skin textFieldSkin;

    private FileHandle uiskin = Gdx.files.internal("default/skin/uiskin.json");

    public MainMenuLoginBox() {
        createFields();
    }

    private void createFields() {
        textFieldSkin = new Skin(uiskin);

        accountName = new TextField("", textFieldSkin);
        password = new TextField("", textFieldSkin);

        //accountNameLabel = new Label("Account Name:", textFieldSkin);
        //passwordLabel = new Label("Password:", textFieldSkin);

        //add(accountNameLabel);
        add(accountName);
        row();
        //add(passwordLabel);
        add(password);
    }
}
