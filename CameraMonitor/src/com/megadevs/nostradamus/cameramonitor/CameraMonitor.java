package com.megadevs.nostradamus.cameramonitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.mashape.client.http.MashapeResponse;
import com.megadevs.nostradamus.cameramonitor.json.Faces;
import com.megadevs.nostradamus.cameramonitor.json.db.IPCams;
import com.megadevs.nostradamus.cameramonitor.reko.FaceAndSceneRecognitionProvidedByReKognitioncom;

public class CameraMonitor {
	public static boolean STOP = true;
	private ArrayList<IPCamera> cameras;
	private int interval=10000;
	private int timeoutImages=15000;
	private static String server="nostradamus-whymca.appspot.com/add_camera_entry?";
	private static String allcams="nostradamus-whymca.appspot.com/get_all_cameras";
	public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String probresume = "probresume";
	public CameraMonitor(){
		cameras = new ArrayList<IPCamera>();
	}


	public void addCamera(IPCamera cam){
		cameras.add(cam);
	}


	public void stopMonitor(){
		STOP=true;
	}


	public void recoverCams(){
		//TODO chiamare backend
		System.out.println("Getting cams...");
		String query=allcams;
		try {
			HTTPResult executeHTTPUrl = Utils.executeHTTPUrl(query, null, null);
			Gson g = new Gson();
			IPCams[] ips = g.fromJson(executeHTTPUrl.getData(),IPCams[] .class);
			
			for(int i=0;i<ips.length;i++){
				IPCamera cam = new IPCamera(ips[i].getId(), ips[i].getHoster());
				cam.setLatitude(ips[i].getLatitude());
				cam.setLongitude(ips[i].getLongitude());
				cam.setReportUri(ips[i].getProbresume());
				addCamera(cam);
			}
			System.out.println("ok, total cam recovered: "+cameras.size());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void push(IPCamera cam, String snap, int faces){

		//id=whymca-padiglione2&hoster=10.1.90.245:8080&snapshot=http://s15.postimage.org/rm0ua4fy3/HFarm_1353199856177.jpg&faces=1

		String query=server+"";
		query += "id=" +cam.getId()+ "&";
		query += "hoster=" +cam.getHost()+ "&";
		query += "snapshot=" +snap+ "&";
		query += latitude+"=" +cam.getLatitude()+ "&";
		query += longitude+"=" +cam.getLongitude()+ "&";
		query += probresume+"=" +cam.getReportUri()+ "&";
		query += "faces=" +faces;
		System.out.println(query);
		try {
			HTTPResult executeHTTPUrl = Utils.executeHTTPUrl(query, null,null);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}  

	}

	public void startMonitor(){
		if(!STOP)return;

		STOP = false;
		Thread t=new Thread(){
			public void run(){
				while(!STOP){
					for(IPCamera cam : cameras){
						if(STOP)return;
						//Check if cam is reachable
						System.out.println("Checking cam: "+cam.getId());
						boolean checkRemoteImageTimed = Utils.checkRemoteImageTimed(cam.getHost(), timeoutImages);
						if(checkRemoteImageTimed){
							if(STOP)return;
							//save snap
							System.out.println("--Saving snapshot...");
							//File snap = new File(getCacheDir()+cam.getId()+"_"+System.currentTimeMillis()+".jpg");
							FakeFile snap = new FakeFile(cam.getId()+"_"+System.currentTimeMillis()+".jpg", cam.getFrame());
							//FileOutputStream fo;
							try {
								//fo = new FileOutputStream(snap);
								//fo.write(cam.getFrame());
								//fo.close();
								if(STOP)return;

								//push to postImage
								System.out.println("--Pushing...");
								String uploadImage = PostimageClient.uploadImage(snap);
								System.out.println(uploadImage);
								if(STOP)return;

								//Call Face detection API
								FaceAndSceneRecognitionProvidedByReKognitioncom client = new FaceAndSceneRecognitionProvidedByReKognitioncom("PUBmYQQbHcF7g4ZUK3h@yqbvABGScWMr", "PRIeQvOOX3MMDPux@sDd#yzq%P-khu4Z");
								// A sample method call. These parameters are not properly filled in.
								// See FaceAndSceneRecognitionProvidedByReKognitioncom.java to find all method names and parameters.

								System.out.println("--FaceDetection...");
								String[] rekoApi = APITable.getRekoApi();
								MashapeResponse<JSONArray> response = client.face(rekoApi[0],rekoApi[1],"face","CameraMonitor",uploadImage,null,null,null);
								//public MashapeResponse<JSONArray> face(String api_key, String api_secret, String jobs, String name_space, String urls, String user_id, String access_token, String fb_id)
								//System.out.println(response.getBody());
								if(STOP)return;

								if(response.getCode()==200){
									Gson g = new Gson();
									Faces f = g.fromJson(Utils.readInputStreamAsString(response.getRawBody()),Faces.class);
									int howMany = f.getFace_detection().size();
									System.out.println("Numero di persone: "+howMany);
									System.out.println("--Update db...");
									if(STOP)return;
									push(cam, uploadImage, howMany);
									//PUSH SU BASCKEND
									System.out.println("Complete.");
								}else{
									System.out.println("Errore API");
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						else{
							//TODO notificare camera non raggiungibile
							System.out.println("Camera not available");
						}
					}

					try {
						System.out.println("Sleeping for "+interval/1000+"s");
						sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		};
		t.start();

	}

	public int getInterval() {
		return interval;
	}


	public void setInterval(int interval) {
		this.interval = interval;
	}

}
