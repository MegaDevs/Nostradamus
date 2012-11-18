package com.megadevs.nostradamus.backend;

import java.util.ArrayList;

public class UserEntity {

	public static final String name = "user";	
	
	public static final String username = "username";
	public static final String latitude = "latitude";
	public static final String longitude = "longitude";
	public static final String email = "email";
	public static final String mesh_components = "mesh_components";
	public static final String google_id = "google_id";
	
	public static ArrayList<String> getKeys() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(username);
		result.add(latitude);
		result.add(longitude);
		result.add(email);
		result.add(mesh_components);
		result.add(google_id);
		
		return result;
	}

}
