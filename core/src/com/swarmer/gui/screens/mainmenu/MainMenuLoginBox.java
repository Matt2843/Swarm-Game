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
    private TextField verifyPassword;

    private Label accountNameLabel;
    private Label passwordLabel;
    private Label verifyPasswordLabel;

    private TextButton login;
    private TextButton createUser;

    private Skin defaultSkin;

    private FileHandle uiskin = Gdx.files.internal("default/skin/uiskin.json");

    public MainMenuLoginBox() {
        createFields();
    }

    private void createFields() {
        defaultSkin = new Skin(uiskin);

        accountName = new TextField("", defaultSkin);
        accountNameLabel = new Label("Account Name: ", defaultSkin);

        password = new TextField("", defaultSkin);
        passwordLabel = new Label("Password: ", defaultSkin);

        verifyPassword = new TextField("", defaultSkin);
        verifyPasswordLabel = new Label("Verify Password: ", defaultSkin);

        verifyPassword.setVisible(false);
        verifyPasswordLabel.setVisible(false);

        login = new TextButton("Login", defaultSkin);
        createUser = new TextButton("Create User", defaultSkin);

        login.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                try {
                    if(login.getText().equals("Create")) {

                    } else {
                        GameClient.getInstance().sendMessage(new Message(100));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GameClientNotInstantiatedException e) {
                    e.printStackTrace();
                }
            }
        });

        createUser.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                verifyPassword.setVisible(true);
                verifyPasswordLabel.setVisible(true);
                login.setText("Create");
                createUser.setVisible(false);
            }
        });

        defaults().width(125);
        add(accountNameLabel);
        add(accountName).width(225);
        row();
        add(passwordLabel);
        add(password).width(225);
        row();
        add(verifyPasswordLabel);
        add(verifyPassword).width(225);
        row();
        add(login).colspan(2).width(350);
        row();
        add(createUser).colspan(2).width(350);

        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 5);
    }
}
