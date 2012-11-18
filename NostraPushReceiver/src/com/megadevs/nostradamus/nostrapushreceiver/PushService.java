package com.megadevs.nostradamus.nostrapushreceiver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.megadevs.nostradamus.automatic.Catastrophe;
import com.megadevs.nostradamus.automatic.HTTPResult;
import com.megadevs.nostradamus.automatic.Utils;
import com.megadevs.nostradamus.automatic.WebcamEvent;

public class PushService extends Service {

	public static final String ACTION_ENABLE_EMERGENCY = "com.megadevs.nostradamus.ENABLE_EMERGENCY";
	public static final String ACTION_DISABLE_EMERGENCY = "com.megadevs.nostradamus.DISABLE_EMERGENCY";
	public static String query_evidence = "nostradamus-whymca.appspot.com/get_disaster_status";

	public class CheckThread extends Thread {
		@Override
		public void run() {
			//TODO manage
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			Intent intent = new Intent(ACTION_ENABLE_EMERGENCY);
			sendBroadcast(intent);
		}
	}

	private boolean isEmergency(){
		try {

			WebcamEvent we = new WebcamEvent();
			//we.setNetFile("/home/ziby/Desktop/Hackaton/webcam.xml");
			System.out.println("Getting schema...");
			HTTPResult executeHTTPUrl;
			executeHTTPUrl = Utils.executeHTTPUrl("megadevs.com/nostradamus/generic.xml", null,null);

			we.setNetData(executeHTTPUrl.getData());

			System.out.println("Getting evidence...");
			//TODO get evidence
			HTTPResult ev = Utils.executeHTTPUrl(query_evidence, null,null);
			String datajson = ev.getData();


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
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return true;//assumo Emergenza se non va 
		}
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
