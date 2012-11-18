package com.megadevs.nostradamus.nostratooth.user;

import java.io.Serializable;

public class SimpleUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6575260364844768509L;

	public String name;
	
	public SimpleUser(String name) {
		this.name = name;
	}

}
