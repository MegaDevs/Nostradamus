package com.megadevs.nostradamus.cameramonitor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PostimageClient {

	
	private static final int RELEVANT_SIZE = 300;


	public static String uploadImage(File f){
		HTTPPostParameters p = new HTTPPostParameters();
		p.addParam("upload", f);
		p.addParam("mode", "local");
		p.addParam("formurl", "http://www.postimage.org/");
		p.addParam("hash","215");
		p.addParam("tpl", ".");
		p.addParam("um", "image");
		p.addParam("MAX_FILE_SIZE", "10485760");
		//p.addParam("antibot", "true");
		p.addParam("submit", "Upload it!");
		
		
		p.addParam("areaid", "");
		p.addParam("addform", "");
		p.addParam("mform", "");
		p.addParam("ver", "");
		p.addParam("ui", "24__1280__800__true__?__?__Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.19 (KHTML, like Gecko) Ubuntu/11.10 Chromium/18.0.1025.168 Chrome/18.0.1025.168 Safari/535.19__");
		
		
		HashMap<String,String> cook = new HashMap<String, String>();
		cook.put("um","image");
		cook.put("adult", "no");
		cook.put("optsize", "0");
		
		try {
			HTTPResult r  = Utils.executeHTTPUrlPost("http://www.postimage.org/", p, cook);
			return processOutput(r.getData());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Extract link from output
	 * @param data
	 * @return
	 */
	private static String processOutput(String data){
		if(data==null || data.length()<=RELEVANT_SIZE)return null;
		
		int j = data.indexOf("Link");
		int i = data.indexOf("Direct Link");
		
		if(i>0 && j>0 && i>j){
			String cut = data.substring(j, i);
			int s = cut.indexOf("http://");
			int e = cut.indexOf(".jpg");
			if(s>0 && e >0 && e>s){
				return cut.substring(s, e+4);
			}
		}
		
		
		
		return null;
	}
	
	
	public static void main(String[] args) {
		String s = uploadImage(new File("/home/ziby/Desktop/cams/51.jpg"));
		System.out.println(s);
	}
	
	
}
