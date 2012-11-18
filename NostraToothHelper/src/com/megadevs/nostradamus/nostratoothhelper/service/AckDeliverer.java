package com.megadevs.nostradamus.nostratoothhelper.service;

import java.util.Vector;

import com.megadevs.nostradamus.nostratoothhelper.msg.AckMessage;
import com.megadevs.nostradamus.nostratoothhelper.msg.Message;

import android.bluetooth.BluetoothDevice;

public class AckDeliverer extends Deliverer {

	public AckDeliverer(Message m, Service s) {
		super(m, s);
	}
	
	public AckDeliverer(AckDeliverer d, boolean initialSleep) {
		super(d, initialSleep);
	}
	
	@Override
	public void run() {
//		if (msg.isHopLimitReached()) return;
		if (initialSleep) {
			try {
				Thread.sleep(REDELIVER_NO_NEIGHBOURS_WAIT_TIME);
			} catch (InterruptedException e1) {}
		}
		
		msg.addHop(service.getMyAddress());
		
		service.getNeighbours(new NeighboursAvailableListener() {
			
			@Override
			public void onNeighboursAvailable(Vector<BluetoothDevice> neighbours) {
				AckMessage ack = (AckMessage)msg;
				String pathToFind = ack.getLastPathHop();
				for (int i = 0; i < neighbours.size(); i++) {
					final BluetoothDevice d = neighbours.elementAt(i);
					if (d.getAddress().equals(pathToFind) || d.getAddress().equals(ack.destination)) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								sendToDevice(d, msg);
							}
						}).start();
						return;
					}
				}
				for (int i = 0; i < neighbours.size(); i++) {
					final BluetoothDevice d = neighbours.elementAt(i);
					new Thread(new Runnable() {
						@Override
						public void run() {
							sendToDevice(d, msg);
						}
					}).start();
				}
			}
			
			@Override
			public void onNoNeighboursAvailable() {
				System.out.println("No neighbours available");
				new AckDeliverer(AckDeliverer.this, true).start();
			}
			
		});
	}

}
