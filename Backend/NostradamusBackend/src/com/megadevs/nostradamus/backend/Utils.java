package com.megadevs.nostradamus.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

	public static final String CALLBACK = "callback";
	
	public static String prepareResponse(Object list, String callback) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(list);

		if (callback != null)
			return callback + "(" + json + ");";
		else return json;
	}
	
	public void handleError(String message, Exception e) {
		
	}
}
