package com.swarmer.gui.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swarmer.gui.StyleSheet;
import com.swarmer.network.GameClient;
import com.swarmer.shared.communication.Message;

import java.io.IOException;

public class MainMenuLoginBox extends Table {

	private TextField userName;
	private TextField password;
	private TextField verifyPassword;

	private Label verifyPasswordLabel;

	private TextButton login;
	private TextButton createUser;

	public MainMenuLoginBox() {
		createFields();
	}

	private void createFields() {

		userName = new TextField("", StyleSheet.defaultSkin);
		Label userNameLabel = new Label("Account Name: ", StyleSheet.defaultSkin);

		password = new TextField("",  StyleSheet.defaultSkin);
		Label passwordLabel = new Label("Password: ", StyleSheet.defaultSkin);
		password.setPasswordCharacter('*');
		password.setPasswordMode(true);

		verifyPassword = new TextField("",  StyleSheet.defaultSkin);
		verifyPasswordLabel = new Label("Verify Password: ", StyleSheet.defaultSkin);
		verifyPassword.setPasswordCharacter('*');
		verifyPassword.setPasswordMode(true);

		verifyPassword.setVisible(false);
		verifyPasswordLabel.setVisible(false);

		login = new TextButton("Login", StyleSheet.defaultSkin);
		createUser = new TextButton("Create User", StyleSheet.defaultSkin);

		login.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				Object[] textFieldData = null;
				try {
					if(userName.getText().matches("^[a-zA-Z0-9][a-zA-Z0-9_\\-']{1,28}[a-zA-Z0-9]$")) {
						textFieldData = new Object[]{userName.getText(), password.getText().toCharArray()};
					} else {
						// TODO: Inform user that he's slightly retarded
						System.out.println("Username is in wrong format!");
					}
					if(login.getText().toString().equals("Create")) {
						if(password.getText().equals(verifyPassword.getText())) {
							GameClient.getInstance().stcp.sendMessage(new Message(201, textFieldData));
						}
					} else {
						GameClient.getInstance().stcp.sendMessage(new Message(109, textFieldData));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				loginState();
				clearFields();
			}
		});

		createUser.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				if(createUser.getText().toString().equals("Create User")) {
					createUserState();
				} else {
					loginState();
				}
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

	private void createUserState() {
		verifyPassword.setVisible(true);
		verifyPasswordLabel.setVisible(true);
		login.setText("Create");
		createUser.setText("Cancel");
	}

	private void loginState() {
		verifyPassword.setVisible(false);
		verifyPasswordLabel.setVisible(false);
		createUser.setText("Create User");
		login.setText("Login");
	}

	private void clearFields() {
		userName.setText("");
		verifyPassword.setText("");
		password.setText("");
	}
}
