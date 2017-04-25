package com.swarmer.gui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.swarmer.gui.StyleSheet;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Matt on 04/25/2017.
 */
public class FriendList extends Table {

	private float parentWidth, parentHeight, width, height;
	private float animationSpeed = 0.5f;

	private TextButton description;
	private TextField searchList;

	private ScrollPane scrollList;
	private Table theList;

	public Map<String, FriendListEntry> onlineFriends = new TreeMap<>();
	public Map<String, FriendListEntry> ingameFriends = new TreeMap<>();
	public Map<String, FriendListEntry> offlineFriends = new TreeMap<>();

	private boolean collapsed = true;
	
	public FriendList(float width, float height) {
		this.parentWidth = width; this.parentHeight = height;
		this.width = width / 4; this.height = (float) (height * 0.4);
		setSize(this.width, this.height);
		configureWidget();
		addWidgets();

		addFriendToFriendList("Matt", FriendListEntry.ONLINE);
		addFriendToFriendList("Albert", FriendListEntry.OFFLINE);
		addFriendToFriendList("Georg", FriendListEntry.INGAME);
		addFriendToFriendList("Aa", FriendListEntry.OFFLINE);
		addFriendToFriendList("Bb", FriendListEntry.ONLINE);
		addFriendToFriendList("Cc", FriendListEntry.INGAME);
		addFriendToFriendList("Hans", FriendListEntry.OFFLINE);
		addFriendToFriendList("Aa", FriendListEntry.ONLINE);
	}

	public void addFriendToFriendList(String name, int onlineStatus) {
		FriendListEntry friendListEntry = new FriendListEntry((int)width, 25, name, onlineStatus);

		switch (onlineStatus) {
			case FriendListEntry.ONLINE:
				onlineFriends.put(name, friendListEntry);
				break;
			case FriendListEntry.OFFLINE:
				offlineFriends.put(name, friendListEntry);
				break;
			case FriendListEntry.INGAME:
				ingameFriends.put(name, friendListEntry);
				break;
			default:
				break;
		}

		theList.clear();
		theList.top();
		theList.left();

		for(FriendListEntry onlineFriends : onlineFriends.values()) {
			theList.add(onlineFriends);
			theList.row();
		}

		for(FriendListEntry ingameFriends : ingameFriends.values()) {
			theList.add(ingameFriends);
			theList.row();
		}

		for(FriendListEntry offlineFriends : offlineFriends.values()) {
			theList.add(offlineFriends);
			theList.row();
		}

	}

	private void configureWidget() {
		Pixmap labelColor = new Pixmap((int)width, (int)height * 10/12, Format.RGB888);
		labelColor.setColor(0.12f, 0.12f, 0.12f, 1);
		labelColor.fill();

		description = new TextButton("Friends", StyleSheet.defaultSkin);
		searchList = new TextField("", StyleSheet.defaultSkin);
		theList = new Table();

		scrollList = new ScrollPane(theList, StyleSheet.defaultSkin);
		scrollList.setScrollingDisabled(true, false);
		scrollList.setFadeScrollBars(false);

		description.setSize(width, height / 12);
		theList.setSize(width, height * 10/12);
		searchList.setSize(width, height / 12);

		theList.setBackground(new Image(new Texture(labelColor)).getDrawable());

		final MoveToAction collapse = new MoveToAction();
		collapse.setPosition(parentWidth - width, -(height - description.getHeight()));
		collapse.setDuration(animationSpeed);

		final MoveToAction expand = new MoveToAction();
		expand.setPosition(parentWidth - width, 0);
		expand.setDuration(animationSpeed);

		description.addCaptureListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				if(collapsed) {
					addAction(expand);
					collapse.reset();
				} else {
					addAction(collapse);
					expand.reset();
				}
				collapsed = !collapsed;
			}
		});
		setPosition(parentWidth - width, -(height - description.getHeight()));
	}

	private class FriendListEntry extends Table {

		public static final int OFFLINE = 0;
		public static final int ONLINE = 1;
		public static final int INGAME = 2;

		private Label friendNameLabel;
		private Label onlineStatusLabel;

		private FriendListEntry(int width, int height, String friendName, int onlineStatus) {
			friendNameLabel = new Label(friendName, StyleSheet.defaultSkin);
			onlineStatusLabel = new Label("", StyleSheet.defaultSkin);
			onlineStatusLabel.setAlignment(Align.center);
			setOnlineStatus(onlineStatus);
			add(friendNameLabel).width((float) (width * 0.78)).height(height);
			add(onlineStatusLabel).width((float) (width * 0.15)).height(height);

			friendNameLabel.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					System.out.println(friendNameLabel.getText() + " clicked.");
				}
			});
		}

		public void setOnlineStatus(int onlineStatus) {
			switch(onlineStatus) {
				case ONLINE:
					onlineStatusLabel.setText("ON");
					onlineStatusLabel.setColor(Color.valueOf("99B898"));
					break;
				case OFFLINE:
					onlineStatusLabel.setText("OFF");
					onlineStatusLabel.setColor(Color.valueOf("2A363B"));
					break;
				case INGAME:
					onlineStatusLabel.setText("IG");
					onlineStatusLabel.setColor(Color.valueOf("FECEA8"));
					break;
				default:
					break;
			}
		}
	}

	private void addWidgets() {
		defaults().width(theList.getWidth());
		add(description);
		row();
		add(scrollList);
		row();
		add(searchList);
	}

}
