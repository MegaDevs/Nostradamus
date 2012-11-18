package com.megadevs.nostradamus.nostrapushreceiver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;

public class PushService extends Service {

	public static final String ACTION_ENABLE_EMERGENCY = "com.megadevs.nostradamus.ENABLE_EMERGENCY";
	public static final String ACTION_DISABLE_EMERGENCY = "com.megadevs.nostradamus.DISABLE_EMERGENCY";
	public static String query_evidence = "nostradamus-whymca.appspot.com/get_disaster_status";

	private boolean isEmergency = false;
	
	public class CheckThread extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(5000);
					System.out.println("CheckThread");
					if (isEmergency()) {
						System.out.println("isEmergency");
						if (isEmergency) continue;
						isEmergency = true;
						Intent intent = new Intent(ACTION_ENABLE_EMERGENCY);
						sendBroadcast(intent);
					} else {
						System.out.println("NOTisEmergency");
						if (!isEmergency) continue;
						isEmergency = false;
						Intent intent = new Intent(ACTION_DISABLE_EMERGENCY);
						sendBroadcast(intent);
					}
				}
			} catch (InterruptedException e) {}
		}
	}

	private boolean isEmergency(){
		try {

			//TODO get evidence
//			HTTPResult ev = Utils.executeHTTPUrl(query_evidence, null,null);
//			String datajson = ev.getData();
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet req = new HttpGet("http://"+query_evidence);
			HttpResponse resp = client.execute(req);
			String datajson = Utils.readInputStreamAsString(resp.getEntity().getContent());


			Gson g=new Gson();
			ArrayList<HashMap<String,String>> a =(ArrayList<HashMap<String,String>> ) g.fromJson(datajson, ArrayList.class);
			System.out.println(a.size());
			for(int i=0;i<a.size();i++){

				HashMap<String, String> cat = a.get(i);
				String k = ""+cat.keySet().toArray()[0];
				String v = cat.get(k);
				if(v.equals("true"))return true;
				

			}
		} catch (IOException e) {
			e.printStackTrace();
			return true; //assumo Emergenza se non va 
		} /*catch (URISyntaxException e) {
			e.printStackTrace();
			return true;//assumo Emergenza se non va 
		}*/
		return false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("Start command push service");
		new CheckThread().start();
		return START_REDELIVER_INTENT;
	}

}
