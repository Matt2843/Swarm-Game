package com.swarmer.gui.widgets;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.swarmer.gui.StyleSheet;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.network.GameClient;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.TreeMap;

public class FriendList extends ChatWidget {

	private Map<String, FriendListEntry> onlineFriends = new TreeMap<>();
	private Map<String, FriendListEntry> ingameFriends = new TreeMap<>();
	private Map<String, FriendListEntry> offlineFriends = new TreeMap<>();

	private Map<String, PrivateChat> privateChatArrayList = new TreeMap<>();

	private Table theList = new Table();

	private static FriendList friendList;

	public static FriendList getInstance() {
		if(friendList == null) {
			friendList = new FriendList("Friends", ChatWidget.chatWidgetCount);
		}
		return friendList;
	}

	public void openFriendTabs() {
		for(PrivateChat privateChat : privateChatArrayList.values()) {
			getParent().addActor(privateChat);
		}
	}

	public FriendList(String title, int widgetNo) {
		super(title, widgetNo);
		scrollableObject.add(theList).expand().fill();

		configureAddFriendButton();
		//addFriendToFriendList("Matt", FriendListEntry.ONLINE);
		/*addFriendToFriendList("Albert", FriendListEntry.OFFLINE);
		addFriendToFriendList("Georg", FriendListEntry.INGAME);
		addFriendToFriendList("Aa", FriendListEntry.OFFLINE);
		addFriendToFriendList("Bb", FriendListEntry.ONLINE);
		addFriendToFriendList("Cc", FriendListEntry.INGAME);
		addFriendToFriendList("Hans", FriendListEntry.OFFLINE);
		addFriendToFriendList("Aa", FriendListEntry.ONLINE);*/
	}

	private void configureAddFriendButton() {
		interaction.setText("ADD");
		interaction.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				try {
					GameClient.getInstance().udp.addBroadcastAddress(new InetSocketAddress("127.0.0.1", 43132));
					GameClient.getInstance().udp.sendMessage(new Message(128342982, "Hello World"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				/*try {
					GameClient.getInstance().tcp.sendMessage(new Message(34789, new String[] {GameClient.getInstance().getCurrentPlayer().getUsername(), input.getText()}));
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		});
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

		for(FriendListEntry onlineFriend : onlineFriends.values()) {
			theList.add(onlineFriend);
			theList.row();
		}

		for(FriendListEntry ingameFriend : ingameFriends.values()) {
			theList.add(ingameFriend);
			theList.row();
		}

		for(FriendListEntry offlineFriend : offlineFriends.values()) {
			theList.add(offlineFriend);
			theList.row();
		}
	}

	public class FriendListEntry extends Table {

		public static final int OFFLINE = 0;
		public static final int ONLINE = 1;
		public static final int INGAME = 2;

		private Label friendNameLabel;
		private Label onlineStatusLabel;

		private FriendListEntry(final int width, final int height, String friendName, int onlineStatus) {
			friendNameLabel = new Label(friendName, StyleSheet.defaultSkin);
			onlineStatusLabel = new Label("", StyleSheet.defaultSkin);
			onlineStatusLabel.setAlignment(Align.center);
			setOnlineStatus(onlineStatus);
			add(friendNameLabel).width((float) (width * 0.78)).height(height);
			add(onlineStatusLabel).width((float) (width * 0.15)).height(height);

			friendNameLabel.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					if(!privateChatArrayList.containsKey(friendNameLabel.getText().toString())) {
						privateChatArrayList.put(friendNameLabel.getText().toString(), new PrivateChat(friendNameLabel.getText().toString(), ++ChatWidget.chatWidgetCount));
						openFriendTabs();
					}
				}
			});

			friendNameLabel.addListener(new ClickListener(Input.Buttons.RIGHT) {
				@Override public void clicked(InputEvent event, float x, float y) {

					final Table menuTable = new Table();


					final TextButton inviteToLobby = new TextButton("Invite To Lobby", StyleSheet.defaultSkin);
					inviteToLobby.addListener(new ClickListener() {
						@Override public void clicked(InputEvent event, float x, float y) {
							// Invite to lobby.

							if(!LobbyScreen.getInstance().getLobbyId().equals(null)) {
								Object[] objects = {GameClient.getInstance().getCurrentPlayer(), friendNameLabel.getText().toString(), LobbyScreen.getInstance().getLobbyId(), GameClient.getInstance().tcp.getConnection().getRemoteSocketAddress()};
								try {
									GameClient.getInstance().tcp.sendMessage(new Message(890, objects));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}


							menuTable.remove();
						}
					});

					menuTable.add(inviteToLobby);
					menuTable.setPosition(x, y-menuTable.getHeight());
					addActor(menuTable);
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
}
