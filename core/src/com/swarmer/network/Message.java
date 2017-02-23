package com.swarmer.network;

import java.io.Serializable;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String message;
	private final Object object;
	
	public Message(String message) {
		this.message = message;
		this.object = null;
	}
	
	public Message(Object object) {
		this.message = null;
		this.object = object;
	}
	
	public Message(String message, Object object) {
		this.message = message;
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public Object getObject() {
		return object;
	}

}
