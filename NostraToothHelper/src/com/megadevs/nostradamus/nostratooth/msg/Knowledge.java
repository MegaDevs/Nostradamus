package com.megadevs.nostradamus.nostratooth.msg;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map.Entry;

import com.megadevs.nostradamus.nostratooth.user.SimpleUser;

public class Knowledge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5659777929183220419L;

	public static final long USER_TIMEOUT = 10 * 60 * 1000;		//10min

	public Hashtable<String, SimpleUser> knowledge = new Hashtable<String, SimpleUser>();
	public Hashtable<String, Long> knowledgeTimes = new Hashtable<String, Long>();
	
	public Knowledge() {}
	
	public Knowledge(Knowledge k) {
		knowledge = new Hashtable<String, SimpleUser>(k.knowledge);
		knowledgeTimes = new Hashtable<String, Long>(k.knowledgeTimes);
	}
	
	public void update(String device, SimpleUser data) {
		knowledgeTimes.put(device, System.currentTimeMillis());
		knowledge.put(device, data);
		for (Entry<String, Long> e : knowledgeTimes.entrySet()) {
			if (e.getValue() < System.currentTimeMillis() - USER_TIMEOUT) {
				knowledge.remove(e.getKey());
			}
		}
	}
	
	public void merge(Knowledge k) {
		for (Entry<String, Long> e : k.knowledgeTimes.entrySet()) {
			if (!knowledge.containsKey(e.getKey())) {
				knowledgeTimes.put(e.getKey(), e.getValue());
				knowledge.put(e.getKey(), k.knowledge.get(e.getKey()));
			} else if (e.getValue() > knowledgeTimes.get(e.getKey())) {
				knowledgeTimes.put(e.getKey(), e.getValue());
				knowledge.put(e.getKey(), k.knowledge.get(e.getKey()));
			}
			if (e.getValue() < System.currentTimeMillis() - USER_TIMEOUT) {
				knowledge.remove(e.getKey());
			}
		}
	}

}
