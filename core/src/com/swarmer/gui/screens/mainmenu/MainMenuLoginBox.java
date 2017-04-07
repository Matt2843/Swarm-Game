package com.swarmer.gui.screens.mainmenu;

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

    private TextField userName;
    private TextField password;
    private TextField verifyPassword;

    private Label userNameLabel;
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

        userName = new TextField("", defaultSkin);
        userNameLabel = new Label("Account Name: ", defaultSkin);

        password = new TextField("", defaultSkin);
        passwordLabel = new Label("Password: ", defaultSkin);
        password.setPasswordCharacter('*');
        password.setPasswordMode(true);

        verifyPassword = new TextField("", defaultSkin);
        verifyPasswordLabel = new Label("Verify Password: ", defaultSkin);
        verifyPassword.setPasswordCharacter('*');
        verifyPassword.setPasswordMode(true);

        verifyPassword.setVisible(false);
        verifyPasswordLabel.setVisible(false);

        login = new TextButton("Login", defaultSkin);
        createUser = new TextButton("Create User", defaultSkin);

        login.addCaptureListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String[] textFieldData = null;
                try {
                    if(userName.getText().matches("^[a-zA-Z0-9][a-zA-Z0-9_\\-']{1,28}[a-zA-Z0-9]$")) {
                        textFieldData = new String[]{userName.getText(), verifyPassword.getText()};
                    } else {
                        // TODO: Inform user that he's slightly retarded
                    }
                    if(login.getText().toString().equals("Create")) {
                        if(password.getText().equals(verifyPassword.getText())) {
                            GameClient.getInstance().tcp.sendMessage(new Message(201, textFieldData));
                        }
                    } else {
                        GameClient.getInstance().tcp.sendMessage(new Message(109, textFieldData));
                    }
                } catch (IOException e) {
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
        add(userNameLabel);
        add(userName).width(225);
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
