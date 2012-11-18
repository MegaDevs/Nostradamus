package com.megadevs.nostradamus.backend;

import java.util.ArrayList;


public class DisasterEntity {

	public static final String name = "disaster";	
	
	public static final String id = "id";
	public static final String enabled = "enabled";

	public static final String earthquake = "earthquake";
	public static final String flooding = "flooding";
	public static final String fire = "fire";
	public static final String terrorism = "terrorism";
	public static final String tornado = "tornado";
	
	public static ArrayList<String> getKeys() {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add(earthquake);
		list.add(flooding);
		list.add(fire);
		list.add(terrorism);
		list.add(tornado);
		
		return list;
	}
	
}
