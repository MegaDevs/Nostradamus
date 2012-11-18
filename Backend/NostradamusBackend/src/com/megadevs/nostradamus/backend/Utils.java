package com.megadevs.nostradamus.backend;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

	public static final String CALLBACK = "callback";
	
	public static String prepareResponse(HashMap<String, String> rsp, String callback) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(rsp);

		if (callback != null)
			return callback + "(" + json + ");";
		else return json;
	}
	
	public void handleError(String message, Exception e) {
		
	}
	
}
