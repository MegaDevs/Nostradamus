package com.megadevs.nostradamus.nostratoothhelper.msg;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Vector;

import com.megadevs.nostradamus.nostratoothhelper.user.User;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8862850012959593013L;
	
	public int signature;
	
	public long timestamp;
	public String destination;
	public String source;
	public String text;
	
	public Vector<String> hops = new Vector<String>();
	
	public Knowledge knowledge = new Knowledge();
	
	public boolean acked = false;
	public boolean sent = false;
	public boolean read = false;
	
	public Message() {}
	
	protected Message(Message src) {
		this.signature = src.signature;
		this.timestamp = src.timestamp;
		this.destination = new String(src.destination);
		this.source = new String(src.source);
		this.text = new String(src.text);
		this.hops = new Vector<String>(src.hops);
		this.knowledge = new Knowledge(src.knowledge);
		this.acked = src.acked;
		this.sent = src.sent;
		this.read = src.read;
	}
	
	public void updateKnowledge(Knowledge k) {
		knowledge = new Knowledge(k);
	}
	
	public int sign() {
		timestamp = System.currentTimeMillis();
		signature = (timestamp+destination+source+text).hashCode();
		return signature;
	}
	
	public boolean isTheSame(Message msg) {
		return signature == msg.signature;
	}
	
	public void addHop(String device) {
		hops.add(device);
	}
	
	public boolean comeFrom(String device) {
		for (String d : hops) {
			if (d.equals(device)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isAcked() {
		return acked;
	}
	
	public boolean isSent() {
		return sent;
	}
	
	public boolean isRead() {
		return read;
	}
	
	public static Message encrypt(Message msg) {
		Message newMsg = new Message(msg);
		int hash = (newMsg.source + newMsg.timestamp + newMsg.destination).hashCode();
		try {
			newMsg.text = SimpleCrypto.encrypt(String.valueOf(hash), newMsg.text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newMsg;
	}
	
	public static Message decrypt(Message msg) {
		Message newMsg = new Message(msg);
		int hash = (newMsg.source + newMsg.timestamp + newMsg.destination).hashCode();
		try {
			newMsg.text = SimpleCrypto.decrypt(String.valueOf(hash), newMsg.text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newMsg;
	}
	
	@Override
	public String toString() {
		String separator = "\n";
		StringBuilder sb = new StringBuilder();
		sb.append("MESSAGE").append(separator);
		sb.append("signature: ").append(signature).append(separator);
		sb.append("from: ").append(source).append(separator);
		sb.append("to: ").append(destination).append(separator);
		sb.append("hops: ");
		for (String d : hops) {
			sb.append(d).append(", ");
		}
		sb.append(separator);
		sb.append("text: ").append(text).append(separator);
		return sb.toString();
	}
}
