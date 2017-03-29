package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swarmer.network.GameClient;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.exceptions.GameClientNotInstantiatedException;

import java.io.IOException;

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

        login.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                try {
                    GameClient.getInstance().sendMessage(new Message("Hello World"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GameClientNotInstantiatedException e) {
                    e.printStackTrace();
                }
            }
        });

        defaults().width(150);
        add(accountNameLabel);
        add(accountName);
        row();
        add(passwordLabel);
        add(password);
        row();
        add(login).colspan(2).width(300);

        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 5);
    }
}
