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

	protected Cipher  inCipher;
	protected Cipher  outCipher;

	private KeyPair KEY = null;
	public PublicKey publicKey;

	protected Socket connection = null;
	private boolean stop = false;

	public SecureTCPConnection(Socket connection, Protocol protocol) throws IOException {
		super(protocol);
		this.connection = connection;
		correspondentsIp = connection.getRemoteSocketAddress().toString();
		try {
			inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch(NoSuchPaddingException e) {
			e.printStackTrace();
		}
		setupInputStreams();
	}

	@Override protected void setupStreams() throws IOException {}

	public PublicKey generateKeys() {
		try {
			KEY = KeyPairGenerator.getInstance("RSA").generateKeyPair();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return KEY.getPublic();
	}

	private void setupInputStreams() throws IOException {
		try {
			if(KEY == null){
				generateKeys();
			}
			IvParameterSpec iv = new IvParameterSpec("aaaaaaaaaaaaaaaa".getBytes("UTF-8"));

			inCipher.init(Cipher.ENCRYPT_MODE, KEY.getPrivate(), iv);
			input = new ObjectInputStream(new CipherInputStream(connection.getInputStream(), inCipher));
		} catch(InvalidKeyException e) {
			e.printStackTrace();
		} catch(InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

	private void setupOutputStreams() throws IOException {
		try {
			IvParameterSpec iv = new IvParameterSpec("aaaaaaaaaaaaaaaa".getBytes("UTF-8"));
			outCipher.init(Cipher.DECRYPT_MODE, publicKey, iv);
			output = new ObjectOutputStream(new CipherOutputStream(connection.getOutputStream(), outCipher));
			output.flush();
		} catch(InvalidKeyException e) {
			e.printStackTrace();
		} catch(InvalidAlgorithmParameterException e) {
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
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (OperationInWrongServerNodeException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (SQLException e) {
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
		if(output == null){
			setupOutputStreams();
		}
		output.writeObject(m);
		output.flush();
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
