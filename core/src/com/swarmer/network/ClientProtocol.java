package com.swarmer.network;

import com.badlogic.gdx.Gdx;
import com.swarmer.game.SwarmerMain;
import com.swarmer.gui.screens.lobby.LobbyScreen;
import com.swarmer.gui.screens.lobby.LobbyUserList;
import com.swarmer.gui.screens.prelobby.PreLobbyScreen;
import com.swarmer.gui.widgets.FriendList;
import com.swarmer.gui.widgets.SwarmerNotification;
import com.swarmer.shared.communication.Connection;
import com.swarmer.shared.communication.Message;
import com.swarmer.shared.communication.Player;
import com.swarmer.shared.communication.Protocol;
import com.swarmer.shared.communication.SerialisedAnts;
import com.swarmer.shared.communication.SerialisedAnt;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.PublicKey;

public class ClientProtocol extends Protocol {

	private String ip;
	private int port;

	@Override public void react(final Message message, Connection caller) throws IOException {
		System.out.println(message.toString());
		switch (message.getOpcode()) {
			case 1: // TEST
				break;
			case 110: // Login succeeded
				loginSucceeded(message);
				break;
			case 202: // User creation state
				userCreatingState(message);
				break;
			case 301: // Received message in lobby chat
                receivedMessageInLobby(message);
				break;
			case 302: // User joined lobby.
				userJoinedLobby(message);
				break;
			case 666: // Update udp socket address info
				updateUdpSocketAddressInfo(message);
				break;
			case 890:
				handleLobbyRequest(message);
				break;
			case 997:
				connectedToLobby(message);
				break;
			case 998:
				connectToLobbyUnitAndStartLobby(message);
				break;
			case 999:
				connectServerUnit(message);
				break;
			case 13372:
				handleFoundGame(message);
			case 11111:
				secureConnectToAuthNode(message);
				break;
			case 23323:
				printAnts(message);
				break;
			case 34789: // Received friend request.
				handleFriendRequest(message);
				break;
			case 34790: // Friend added, object = string with friends name.
				friendAdded(message);
				break;
			default:
				break;
		}
	}

    private void receivedMessageInLobby(Message message) {
        String[] receivedMessageArray = (String[]) message.getObject();
        LobbyScreen.getInstance().lobbyChat.appendToChatWindow(receivedMessageArray[1], receivedMessageArray[0]);
    }

    private void printAnts(Message message) {
		String res = "";
		SerialisedAnts ants = (SerialisedAnts) message.getObject();
		for(SerialisedAnt ant : ants.ants) {
			res += ant.toString() + "\n";
		}
		System.out.println(res);
	}

	private void updateUdpSocketAddressInfo(Message message) {
		GameClient.getInstance().udp.addBroadcastAddress((InetSocketAddress) message.getObject());
	}

	private void userJoinedLobby(Message message) {
		Player joinedPlayer = (Player) message.getObject();
//		LobbyScreen.getInstance().getFindGame().remove();
		LobbyUserList.getInstance().addUserToList(joinedPlayer.getUsername());
		SwarmerMain.getInstance().show(LobbyScreen.getInstance());
	}

	private void handleLobbyRequest(final Message message) {
		Object[] receivedObjects = (Object[]) message.getObject();
		final Player requestFrom = (Player) receivedObjects[0];
		final String lobbyID = (String) receivedObjects[1];
		final InetSocketAddress connectionDetails = (InetSocketAddress) receivedObjects[2];

		Gdx.app.postRunnable(new Runnable() {
			@Override public void run() {
				SwarmerMain.getCurrentScreen().addActor(new SwarmerNotification("Lobby Request", requestFrom.getUsername() + " invited you to a lobby.") {
					@Override public void accept() throws IOException {
						GameClient.getInstance().establishTCPConnection(connectionDetails.getAddress().toString().replaceAll("/", ""), connectionDetails.getPort());
						GameClient.getInstance().tcp.sendMessage(new Message(303, new Object[] {lobbyID, GameClient.getInstance().getCurrentPlayer()}));
						LobbyScreen.getInstance().setLobbyId(lobbyID);
					}

					@Override public void reject() {
						// Do nothing :)
					}
				});
			}
		});
	}

	private void handleFoundGame(final Message message) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				SwarmerMain.getCurrentScreen().addActor(new SwarmerNotification("Game found", "A game was found!") {
					@Override
					public void accept() throws IOException {
						GameClient.getInstance().tcp.sendMessage(new Message(76767));
					}

					@Override
					public void reject() throws IOException {
						GameClient.getInstance().tcp.sendMessage(new Message(78787));
					}
				});
			}
		});
	}

	private void friendAdded(Message message) {
		FriendList.getInstance().addFriendToFriendList((String) message.getObject(), FriendList.FriendListEntry.ONLINE);
	}

	private void handleFriendRequest(final Message message) {
		Gdx.app.postRunnable(new Runnable() {
			@Override public void run() {
				SwarmerMain.getCurrentScreen().addActor(new SwarmerNotification("Friend Request", ((Player)message.getObject()).getUsername() + " wants to add you as a friend.") {
					@Override public void accept() throws IOException {
						GameClient.getInstance().tcp.sendMessage(new Message(34788, new Player[] {GameClient.getInstance().getCurrentPlayer(), (Player) message.getObject()}));
					}

					@Override public void reject() {
						// Nothing happens
					}
				});
			}
		});
	}

	private void userCreatingState(Message message) {
		if(message.getObject() != null) {
			SwarmerMain.getInstance().show(PreLobbyScreen.getInstance());

			GameClient.getInstance().setCurrentPlayer((Player) message.getObject());
		} else {
			// TODO: Notify user that user creation failed.
			System.out.println();
		}
	}

	private void loginSucceeded(Message message) {
		if (message.getObject() != null) {
			SwarmerMain.getInstance().show(PreLobbyScreen.getInstance());
			//ScreenManager.getInstance().show(ScreenLib.PRE_LOBBY_SCREEN);
			GameClient.getInstance().setCurrentPlayer((Player) message.getObject());
		} else {
			// TODO: Notify user that login failed.
			System.out.println("Login failed");
		}
	}

	private void connectedToLobby(Message message) {
		LobbyScreen.getInstance().setLobbyId((String) message.getObject());
		SwarmerMain.getInstance().show(LobbyScreen.getInstance());
	}

	private void connectToLobbyUnitAndStartLobby(Message message) throws IOException {
		connectServerUnit(message);
		GameClient.getInstance().tcp.sendMessage(new Message(302));
	}

	private void connectServerUnit(Message message) {
		String[] receivedMessageArray = ((String) message.getObject()).split(":");
		ip = receivedMessageArray[0].replace("/", "");
		port = Integer.parseInt(receivedMessageArray[1]);
		if(GameClient.getInstance().stcp != null) {
			GameClient.getInstance().stcp.stopConnection(GameClient.getInstance().getCurrentPlayer());
		}
		//if(GameClient.udp != null) {
		//	GameClient.udp.stopConnection(getCurrentPlayer());
		//}
		try {
			GameClient.getInstance().establishTCPConnection(ip, port);
			GameClient.getInstance().tcp.sendMessage(new Message(11111, GameClient.KEY.getPublic()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void secureConnectToAuthNode(Message message) {
		GameClient.getInstance().establishSecureTCPConnection(ip, port, (PublicKey) message.getObject());
	}
}
