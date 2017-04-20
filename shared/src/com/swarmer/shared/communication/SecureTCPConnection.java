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
	private PublicKey exPublicKey = null;

	private Socket connection = null;
	private boolean stop = false;

	private Callable NonSecureTCP;

	public SecureTCPConnection(Socket connection, Protocol protocol, KeyPair KEY, PublicKey exPublicKey) throws IOException {
		super(protocol);
		this.KEY = KEY;
		this.exPublicKey = exPublicKey;

		this.connection = connection;
		correspondentsIp = connection.getRemoteSocketAddress().toString();
		setupCiphers();
		setupIOStreams();
	}

	@Override protected void setupStreams() throws IOException {}

	public PublicKey getPublicKey() {
		return KEY.getPublic();
	}

	public void setExternalPublicKey(PublicKey publicKey) {
		exPublicKey = publicKey;
	}

	private void setupCiphers() throws IOException {
		try {
			inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch(NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	private void setupIOStreams() {
		try {
			inCipher.init(Cipher.DECRYPT_MODE, KEY.getPrivate());
			outCipher.init(Cipher.ENCRYPT_MODE, exPublicKey);
			CipherInputStream cis = new CipherInputStream(connection.getInputStream(), inCipher);
			CipherOutputStream cos = new CipherOutputStream(connection.getOutputStream(), outCipher);

			System.out.println("1");
			//output = new ObjectOutputStream(connection.getOutputStream());
			
			output = new ObjectOutputStream(cos);
			sendMessage(new Message(9876, new byte[1000]));
			
			cos.flush();
			output.flush();
			
			System.out.println("2");

			//input = new ObjectInputStream(connection.getInputStream());
			input = new ObjectInputStream(cis);
			System.out.println("3");
		} catch(InvalidKeyException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override public void run() {
		Message message = null;
		do {
			try {
				message = (Message) input.readObject();
				System.out.println(message.toString());
				react(message);
			} catch (IOException e) {
				System.out.println("WHY GOD WHY");
				stop = true;
			} catch (ClassNotFoundException | OperationInWrongServerNodeException | SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} while(message.getOpcode() != 0 && !stop); // TODO: CHANGE STOP CONDITION.
		stop = true;
		cleanUp();
	}

	public void stopConnection() {
		try {
			sendMessage(new Message(0));
		} catch(IOException e) {
			e.printStackTrace();
		}
		stop = true;
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		if(!stop) {
			if(exPublicKey != null) {
				output.writeObject(m);
				output.flush();
			} else {
				System.out.println("Cannot send message without an external public key!");
			}
		} else {
			System.out.println("Connection Closed");
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
