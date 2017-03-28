package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * Created by Matt on 03/28/2017.
 */
public class MainMenuLoginBox extends Table {

    private TextField accountName;
    private TextField password;

    private Label accountNameLabel;
    private Label passwordLabel;

    private TextButton login;

    private Skin defaultSkin;

    private FileHandle uiskin = Gdx.files.internal("default/skin/uiskin.json");

    public MainMenuLoginBox() {
        createFields();
    }

    private void createFields() {
        defaultSkin = new Skin(uiskin);

        accountNameLabel = new Label("Account Name: ", defaultSkin);
        passwordLabel = new Label("Password: ", defaultSkin);

        accountName = new TextField("", defaultSkin);
        password = new TextField("", defaultSkin);

        login = new TextButton("Login", defaultSkin);

        add(accountNameLabel).width(150);
        add(accountName);
        row();
        add(passwordLabel).width(150);
        add(password);
        row();
        add(login).colspan(2);

        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 5);
    }
}
