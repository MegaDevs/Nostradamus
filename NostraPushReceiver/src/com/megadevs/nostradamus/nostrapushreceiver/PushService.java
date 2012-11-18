package com.megadevs.nostradamus.nostrapushreceiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PushService extends Service {
	
	public static final String ACTION_ENABLE_EMERGENCY = "com.megadevs.nostradamus.ENABLE_EMERGENCY";
	public static final String ACTION_DISABLE_EMERGENCY = "com.megadevs.nostradamus.DISABLE_EMERGENCY";
	
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
