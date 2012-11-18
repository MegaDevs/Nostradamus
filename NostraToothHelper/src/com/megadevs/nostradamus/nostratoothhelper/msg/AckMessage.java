package com.megadevs.nostradamus.nostratoothhelper.msg;

import java.util.Vector;

public class AckMessage extends Message {

	public Vector<String> forwardPath;
	
	public AckMessage(Message msg) {
		destination = msg.source;
		source = msg.destination;
		signature = msg.signature;
		forwardPath = new Vector<String>(hops);
	}
	
	public String getLastPathHop() {
		if (forwardPath.size() == 0) return null;
		return forwardPath.remove(forwardPath.size()-1);
	}
	
	@Override
	public String toString() {
		String separator = "\n";
		StringBuilder sb = new StringBuilder();
		sb.append("ACK MESSAGE").append(separator);
		sb.append("signature: ").append(signature).append(separator);
		sb.append("hops: ");
		for (String d : hops) {
			sb.append(d).append(", ");
		}
		sb.append(separator);
		return sb.toString();
	}

}
