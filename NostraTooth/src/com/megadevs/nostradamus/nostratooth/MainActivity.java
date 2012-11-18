package com.megadevs.nostradamus.nostratooth;

import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.megadevs.nostradamus.nostratooth.BluetoothTestActivity.Receiver;
import com.megadevs.nostradamus.nostratooth.service.IService;
import com.megadevs.nostradamus.nostratooth.storage.MessageStorage;
import com.megadevs.nostradamus.nostratooth.storage.UserStorage;

@EActivity(R.layout.main)
public class MainActivity extends Activity {

	public static final UUID MY_UUID = UUID.fromString("fd300e40-ad66-11e1-afa6-0800200c9a66");

	private Intent serviceIntent;
	private ServiceConnection serviceConn;
	private IService service;
	
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private MessageStorage storage;
	private UserStorage userDB;
	
	private Receiver mReceiver;
	
	@Pref
	public Prefs_ prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
