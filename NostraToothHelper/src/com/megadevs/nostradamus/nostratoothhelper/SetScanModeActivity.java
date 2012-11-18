package com.megadevs.nostradamus.nostratoothhelper;

import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.SystemService;

@EActivity
public class SetScanModeActivity extends Activity {

	public static final int NOTIFICATION_ID = 3;
	
	@SystemService
	public NotificationManager mNotificationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new View(this));
		
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
	    discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivityForResult(discoverableIntent, 1);
	    
	    System.out.println("started setscanmodeactivity");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("setscanmodeactivity onactivityresult");
		if (resultCode == RESULT_CANCELED) {
			System.out.println("scanmode canceled");
		} else {
			System.out.println("scan mode ok "+resultCode);
		}
		
		mNotificationManager.cancel(NOTIFICATION_ID);
		finish();
	}
	
}
