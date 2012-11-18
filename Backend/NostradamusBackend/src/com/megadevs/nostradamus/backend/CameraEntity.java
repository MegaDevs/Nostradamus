package com.megadevs.nostradamus.backend;

import java.util.ArrayList;

public class CameraEntity {

	public static final String name = "camera";	
	
	public static final String id = "id";
	public static final String hoster = "hoster";
	public static final String snapshot = "snapshot";
	public static final String faces = "faces";
	public static final String latitude = "latitude";
	public static final String longitude = "longitude";
	public static final String probresume = "probresume";
	public static final String timestamp = "timestamp";
	
	public static ArrayList<String> getKeys() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(id);
		result.add(hoster);
		result.add(snapshot);
		result.add(faces);
		result.add(latitude);
		result.add(longitude);
		result.add(probresume);
		result.add(timestamp);
		
		return result;
	}

}
