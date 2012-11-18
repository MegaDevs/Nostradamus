package com.megadevs.nostradamus.nostratoothhelper.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.megadevs.nostradamus.nostratoothhelper.msg.Message;
import com.megadevs.nostradamus.nostratoothhelper.user.User;

public class UserStorage extends CommonStorage {

	private static UserStorage instance;
	
	public Hashtable<String, User> storage = null;
	
	public static UserStorage getInstance() {
		if (instance == null) {
			instance = new UserStorage();
		}
		return instance;
	}
	
	private UserStorage() {}
	
	public void load() {
		if (storage == null) storage = new Hashtable<String, User>();
		storage = (Hashtable<String, User>)super.load(storage);
	}
	
	public boolean storeUser(User u) {
		if (storage.containsKey(u.getAddress())) return false;
		storage.put(u.getAddress(), u);
		super.save(storage);
		return true;
	}
	
	public int getUsersCount() {
		return storage.size();
	}
	
	public Vector<User> getUsers() { 
		return new Vector<User>(storage.values());
	}
	
	public User getUserFromAddress(String address) {
		return storage.get(address);
	}
	
	public User getUserFromId(long id) {
		for (User u : storage.values()) {
			if (u.getContactId() == id) {
				return u;
			}
		}
		return null;
	}
	
	public boolean containsAddress(String address) {
		return storage.containsKey(address);
	}
	
}
