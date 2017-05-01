package com.swarmer.shared.communication;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -3726114245181941134L;
	
	private final int opcode;
	private final Object object;
	
	public Message(int opcode) {
		this(opcode, null);
	}
	
	public Message(Object object) {
		this(0, object);
	}
	
	public Message(int opcode, Object object) {
		if(object != null && !(object instanceof Serializable)){
			System.out.println(object.getClass().getSimpleName() + ": is not Serializable.");
		}
		this.opcode = opcode;
		this.object = object;
	}

	public int getOpcode() {
		return opcode;
	}

	public Object getObject() {
		return object;
	}

	@Override public String toString() {
		return "Message{" +
				"opcode=" + opcode +
				", object=" + object +
				'}';
	}
}
