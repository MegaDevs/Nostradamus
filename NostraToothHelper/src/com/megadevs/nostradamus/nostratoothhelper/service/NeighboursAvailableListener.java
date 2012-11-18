package com.megadevs.nostradamus.nostratoothhelper.service;

import java.util.Vector;

import android.bluetooth.BluetoothDevice;

public abstract class NeighboursAvailableListener {

	public abstract void onNeighboursAvailable(Vector<BluetoothDevice> neighbours);
	public abstract void onNoNeighboursAvailable();
	
}
