package com.megadevs.nostradamus.nostratooth.storage;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.megadevs.nostradamus.nostratooth.msg.AckMessage;
import com.megadevs.nostradamus.nostratooth.msg.Message;
import com.megadevs.nostradamus.nostratooth.user.User;

public class MessageStorage extends CommonStorage {

	public static final int IN = 1;
	public static final int OUT = 2;
	
	private static MessageStorage instance;
	
	public Hashtable<String, Vector<Message>> storage = null;
	
	public static MessageStorage getInstance() {
		if (instance == null) {
			instance = new MessageStorage();
		}
		return instance;
	}
	
	private MessageStorage() {}
	
	public void load() {
		if (storage == null) storage = new Hashtable<String, Vector<Message>>();
		storage = (Hashtable<String, Vector<Message>>)super.load(storage);
	}
	
	public int getConversationsCount() {
		return storage.size();
	}
	
	public Vector<String> getConversationsAddresses() {
		return new Vector<String>(storage.keySet());
	}
	
	public int getMessagesCount(User u) {
		if (!storage.containsKey(u.getAddress())) return 0;
		return storage.get(u.getAddress()).size();
	}
	
	public int getMessagesUnreadCount(User u) {
		if (!storage.containsKey(u.getAddress())) return 0;
		int tot = 0;
		Vector<Message> msgs = storage.get(u.getAddress());
		for (int i = 0; i < msgs.size(); i++) {
			if (!msgs.elementAt(i).isRead()) tot++;
		}
		return tot;
	}
	
	public Vector<Message> getMessages(User u) {
		if (!storage.containsKey(u.address)) return new Vector<Message>();
		return storage.get(u.address);
	}
	
	public Message getLastMessageFromConversation(User u) {
		if (!storage.containsKey(u.getAddress())) return null;
		return storage.get(u.getAddress()).lastElement();
	}
	
	public Vector<Message> getUnreadMessages() {
		Vector<Message> out = new Vector<Message>();
		Enumeration<Vector<Message>> list = storage.elements();
		while (list.hasMoreElements()) {
			Vector<Message> msgs = list.nextElement();
			for (int i = 0; i < msgs.size(); i++) {
				if (!msgs.elementAt(i).isRead()) {
					out.add(msgs.elementAt(i));
				}
			}
		}
		return out;
	}
	
	public boolean setAck(AckMessage ack) {
		Vector<Message> msgs = storage.get(ack.source);
		for (int i = 0; i < msgs.size(); i++) {
			Message m = msgs.elementAt(i);
			if (m.signature == ack.signature) {
				m.acked = true;
				super.save(storage);
				return true;
			}
		}
		return false;
	}
	
	public boolean setSent(Message msg) {
		Vector<Message> msgs = storage.get(msg.destination);
		for (int i = 0; i < msgs.size(); i++) {
			Message m = msgs.elementAt(i);
			if (m.signature == msg.signature) {
				m.sent = true;
				super.save(storage);
				return true;
			}
		}
		return false;
	}
	
	public boolean setRead(Message msg, int direction) {
		String key = null;
		switch (direction) {
		case IN:
			key = msg.source;
			break;
		case OUT:
			key = msg.destination;
			break;
		}
		Vector<Message> msgs = storage.get(key);
		for (int i = 0; i < msgs.size(); i++) {
			Message m = msgs.elementAt(i);
			if (m.signature == msg.signature) {
				m.read = true;
				super.save(storage);
				return true;
			}
		}
		return false;
	}
	
	public boolean storeMessage(Message msg, int direction) {
		String key = null;
		switch (direction) {
		case IN:
			key = msg.source;
			break;
		case OUT:
			key = msg.destination;
			break;
		}
		Vector<Message> msgs = null;
		if (!storage.containsKey(key)) {
			msgs = new Vector<Message>();
		} else {
			msgs = storage.get(key);
		}
		
		for (int i = 0; i < msgs.size(); i++) {
			if (msgs.elementAt(i).signature == msg.signature) {
				return false;
			}
		}
		
		msgs.add(msg);
		storage.put(key, msgs);
		super.save(storage);
		return true;
	}
	
}
