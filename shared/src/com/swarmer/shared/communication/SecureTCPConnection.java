package com.swarmer.shared.communication;

import com.swarmer.shared.exceptions.OperationInWrongServerNodeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;

public class SecureTCPConnection extends Connection {
	protected ObjectInputStream input;
	protected ObjectOutputStream output = null;

	protected Cipher inCipher;
	protected Cipher outCipher;

	private CipherInputStream cis;
	private CipherOutputStream cos;

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

			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());

		} catch(InvalidKeyException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override public void run() {
		Message message = null;
		do {
			try {
				message = (Message) recreateMessage((ArrayList<SealedObject>) input.readObject());
				react(message);
			} catch (IOException e) {
				System.out.println("WHY GOD WHY");
				stop = true;
			} catch (ClassNotFoundException | OperationInWrongServerNodeException | NoSuchAlgorithmException | SQLException e) {
				e.printStackTrace();
			}
		} while(!stop && message.getOpcode() != 0); // TODO: CHANGE STOP CONDITION.
		cleanUp();
	}

	@Override public void sendMessage(Message m) throws IOException {
		if(!stop) {
			if(exPublicKey != null) {
				System.out.println("Send msg: " + m.toString());
				output.writeObject(generateList(toByte(m)));
				output.flush();
			} else {
				System.out.println("Cannot send message without an external public key!");
			}
		} else {
			System.out.println("Connection Closed");
		}
	}

	@Override public Message getNextMsg() {
		try {
			return (Message) recreateMessage((ArrayList<SealedObject>) input.readObject());
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Message recreateMessage(ArrayList<SealedObject> lst) {
		Message message = null;
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			for(SealedObject o: lst) {
				bout.write((byte[]) o.getObject(inCipher));
			}
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
			message = (Message) in.readObject();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(BadPaddingException e) {
			e.printStackTrace();
		} catch(IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return message;
	}

	private ArrayList<SealedObject> generateList(byte[] data) {
		ArrayList<SealedObject> lst = new ArrayList<SealedObject>();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			byte[] bytes = new byte[200];
			int len = 0;
			while((len = bis.read(bytes)) != -1) {
				lst.add(new SealedObject(Arrays.copyOfRange(bytes, 0, len), outCipher));
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return lst;
	}

	private byte[] toByte(Message m) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(bos);

			out.writeObject(m);
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	@Override public void stopConnection(Object... o) {
		if(!stop) {
			try {
				if(o.length > 0) {
					sendMessage(new Message(0, o[0]));
				} else {
					sendMessage(new Message(0));
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			stop = true;
			cleanUp();
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
