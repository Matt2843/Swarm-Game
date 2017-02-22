package com.swarmer.server;

public class Message {
	
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
