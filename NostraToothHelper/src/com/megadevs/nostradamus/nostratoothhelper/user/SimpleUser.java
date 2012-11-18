package com.megadevs.nostradamus.nostratoothhelper.user;

import java.io.Serializable;

public class SimpleUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6575260364844768509L;

	public String name;
	public String email;
	public String gid;
	
	public double latitude;
	public double longitude;
	
	public SimpleUser(String name) {
		this.name = name;
	}

}
