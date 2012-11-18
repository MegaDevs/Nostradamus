package com.megadevs.nostradamus.cameramonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.mashape.client.http.MashapeResponse;
import com.megadevs.nostradamus.cameramonitor.json.Faces;
import com.megadevs.nostradamus.cameramonitor.reko.FaceAndSceneRecognitionProvidedByReKognitioncom;

public class CameraMonitor {

    private ArrayList<IPCamera> cameras;
    private String cacheDir;
    private int interval=10000;
    private int timeoutImages=15000;
    
    public CameraMonitor(){
    	cameras = new ArrayList<IPCamera>();
    }
    
    
    
    public void addCamera(IPCamera cam){
    	cameras.add(cam);
    }


	public String getCacheDir() {
		return cacheDir;
	}


	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;
		File f = new File(cacheDir);
		if(!f.exists())
			f.mkdirs();
	}
	

	public void startMonitor(){
		for(IPCamera cam : cameras){
			//Check if cam is reachable
			System.out.println("Checking cam: "+cam.getId());
			boolean checkRemoteImageTimed = Utils.checkRemoteImageTimed(cam.getHost(), timeoutImages);
			if(checkRemoteImageTimed){
				
				//save snap
				System.out.println("--Saving snapshot...");
				File snap = new File(getCacheDir()+cam.getId()+"_"+System.currentTimeMillis()+".jpg");
				FileOutputStream fo;
				try {
					fo = new FileOutputStream(snap);
					fo.write(cam.getFrame());
					fo.close();

				
				//push to postImage
				System.out.println("--Pushing...");
				String uploadImage = PostimageClient.uploadImage(snap);
				System.out.println(uploadImage);
				
				//Call Face detection API
				FaceAndSceneRecognitionProvidedByReKognitioncom client = new FaceAndSceneRecognitionProvidedByReKognitioncom("PUBmYQQbHcF7g4ZUK3h@yqbvABGScWMr", "PRIeQvOOX3MMDPux@sDd#yzq%P-khu4Z");
				// A sample method call. These parameters are not properly filled in.
				// See FaceAndSceneRecognitionProvidedByReKognitioncom.java to find all method names and parameters.
				
				System.out.println("--FaceDetection...");
				String[] rekoApi = APITable.getRekoApi();
				MashapeResponse<JSONArray> response = client.face(rekoApi[0],rekoApi[1],"face","CameraMonitor",uploadImage,null,null,null);
				//public MashapeResponse<JSONArray> face(String api_key, String api_secret, String jobs, String name_space, String urls, String user_id, String access_token, String fb_id)
				Gson g = new Gson();
				Faces f = g.fromJson(Utils.readInputStreamAsString(response.getRawBody()),Faces.class);
				System.out.println("Complete.");
				//now you can do something with the response.
				System.out.println("API Call returned a response code of " + response.getCode());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			else{
				//TODO notificare camera non raggiungibile
			}
		}
	}

	public int getInterval() {
		return interval;
	}


	public void setInterval(int interval) {
		this.interval = interval;
	}

}
