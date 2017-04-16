package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class SecureTCPConnection extends Connection {
	protected ObjectInputStream input;
	protected ObjectOutputStream output = null;

	protected Cipher inCipher;
	protected Cipher outCipher;

	private KeyPair KEY = null;
	private PublicKey exPublicKey;

	private Socket connection = null;
	private boolean stop = false;

	private Callable NonSecureTCP;

	public SecureTCPConnection(Socket connection, Protocol protocol) throws IOException {
		super(protocol);
		this.connection = connection;
		correspondentsIp = connection.getRemoteSocketAddress().toString();
		setupInputStreams();

		NonSecureTCP = new Callable(connection, new Message(1111, KEY.getPublic()));
		exPublicKey = (PublicKey) NonSecureTCP.getFutureResult().getObject();
	}

	@Override protected void setupStreams() throws IOException {}

	public PublicKey getPublicKey() {
		return KEY.getPublic();
	}

	public void setExternalPublicKey(PublicKey publicKey) {
		exPublicKey = publicKey;
	}

	private void setupInputStreams() throws IOException {
		try {
			KEY = KeyPairGenerator.getInstance("RSA").generateKeyPair();

			inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

			IvParameterSpec iv = new IvParameterSpec("aaaaaaaaaaaaaaaa".getBytes("UTF-8"));

			inCipher.init(Cipher.ENCRYPT_MODE, KEY.getPrivate(), iv);
			input = new ObjectInputStream(new CipherInputStream(connection.getInputStream(), inCipher));
		} catch(InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void setupOutputStreams() throws IOException {
		try {
			IvParameterSpec iv = new IvParameterSpec("aaaaaaaaaaaaaaaa".getBytes("UTF-8"));
			outCipher.init(Cipher.DECRYPT_MODE, exPublicKey, iv);
			output = new ObjectOutputStream(new CipherOutputStream(connection.getOutputStream(), outCipher));
			output.flush();
		} catch(InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

	@Override public void run() {
		Message message = null;
		do {
			try {
				message = (Message) input.readObject();
				react(message);
			} catch (IOException e) {
				System.out.println("WHY GOD WHY");
			} catch (ClassNotFoundException | OperationInWrongServerNodeException | SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} while(message.getOpcode() != 0 && !stop); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	public void stopConnection() {
		try {
			sendMessage(new Message(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop = true;
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		if(exPublicKey != null) {
			if(output == null){
				setupOutputStreams();
			}
			output.writeObject(m);
			output.flush();
		} else {
			System.out.println("Cannot send message without an external public key!");
		}
	}

	@Override public void cleanUp() {
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getConnection() {
		return connection;
	}
}
