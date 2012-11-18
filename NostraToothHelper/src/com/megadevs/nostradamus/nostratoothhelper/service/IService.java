package com.megadevs.nostradamus.nostratoothhelper.service;

import java.util.Vector;

import android.bluetooth.BluetoothDevice;
import android.os.IBinder;

import com.megadevs.nostradamus.nostratooth.msg.Knowledge;
import com.megadevs.nostradamus.nostratooth.msg.Message;

public interface IService extends IBinder {

	public void sendMessage(Message msg);
	
	public void refreshNeighbours();
	
	public void turnOff();
	
	public String getMyAddress();
	
	public Vector<BluetoothDevice> getNeighbours();
	
	public void init();
	
	public void deInit();
	
	public Knowledge getMyKnowledge();
	
}
