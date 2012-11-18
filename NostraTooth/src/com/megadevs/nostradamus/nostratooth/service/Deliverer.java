package com.megadevs.nostradamus.nostratooth.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import com.megadevs.nostradamus.nostratooth.BluetoothTestActivity;
import com.megadevs.nostradamus.nostratooth.msg.Message;
import com.megadevs.nostradamus.nostratooth.storage.MessageStorage;

public class Deliverer extends Thread {
	
	public static Object LOCK = new Object();
	
	public static final long REDELIVER_NO_NEIGHBOURS_WAIT_TIME = 1 * 60 * 1000;
	public static final long REDELIVER_SHORT_WAIT_TIME = 1 * 1000;
	public static final long REDELIVER_LONG_WAIT_TIME = 20 * 1000;
	
	public static final int REDELIVER_SHORT_COUNTER = 1;//3;
	public static final int REDELIVER_LONG_COUNTER = 1;//3;

	protected Message msg;
	protected Service service;
	
	protected boolean initialSleep = false;
	protected boolean cloned = false;
	
	protected int neighboursSize = 0;
	protected int sentCount = 0;
	protected int notSentCount = 0;
	
	public Deliverer(Message m, Service s) {
		msg = m;
		service = s;
	}
	
	public Deliverer(Deliverer d, boolean initialSleep) {
		msg = d.msg;
		service = d.service;
		this.initialSleep = initialSleep;
		cloned = true;
	}
	
	@Override
	public void run() {
		synchronized (LOCK) {
	//		if (msg.isHopLimitReached()) return;
			if (initialSleep) {
				try {
					Thread.sleep(REDELIVER_NO_NEIGHBOURS_WAIT_TIME);
				} catch (InterruptedException e1) {}
			}
			
			if (!cloned) {
				msg.addHop(service.getMyAddress());
			}		
			
			service.getNeighbours(new NeighboursAvailableListener() {
				
				@Override
				public void onNeighboursAvailable(Vector<BluetoothDevice> neighbours) {
					for (int i = 0; i < neighbours.size(); i++) {
						final BluetoothDevice d = neighbours.elementAt(i);
						if (d.getAddress().equals(msg.destination)) {
							neighboursSize = 1;
							new Thread(new Runnable() {
								@Override
								public void run() {
									sendToDevice(d, msg);
								}
							}).start();
							return;
						}
					}
					neighboursSize = neighbours.size();
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
					new Deliverer(Deliverer.this, true).start();
				}
				
			});
		}
	}
	
	protected void sendToDevice(BluetoothDevice device, Message msg) {
		sendToDevice(device, msg, REDELIVER_LONG_COUNTER);
	}
	
	protected void sendToDevice(BluetoothDevice device, Message msg, int bigCounter) {
		if (msg.comeFrom(device.getAddress())) return;
		if (bigCounter == 0) {
			notifyNotSent();
			return;
		}
		
		boolean send = false;
		int counter = REDELIVER_SHORT_COUNTER;
		while (!send && counter-- > 0) {
			try {
				service.cancelDiscovery();
				
				BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(BluetoothTestActivity.MY_UUID);
				System.out.println("connecting to "+device.getName());
				socket.connect();
				System.out.println("connected");
				
				OutputStream os = socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(msg);
				oos.close();
				os.close();
				System.out.println("writed");
				
				socket.close();
				System.out.println("closed");
				
				send = true;
			} catch (IOException e) {
				System.out.println("error while sending message to device "+device.getName());
				e.printStackTrace();
				try {
					Thread.sleep(REDELIVER_SHORT_WAIT_TIME);
				} catch (InterruptedException e1) {}
			}
		}
		
		if (!send) {
			try {
				Thread.sleep(REDELIVER_LONG_WAIT_TIME);
				sendToDevice(device, msg, --bigCounter);
			} catch (InterruptedException e) {}
		} else {
//			notifySent();
		}
	}
	
	private void notifyNotSent() {
		notSentCount++;
		if (notSentCount == neighboursSize) {
			System.out.println("Failed all neighbours");
//			new Deliverer(Deliverer.this, true).start();
		}
	}
	
	private void notifySent() {
		sentCount++;
		if (service.getMyAddress().equals(msg.source)) {
			MessageStorage.getInstance().setSent(msg);
			Intent i = new Intent(Service.SERVICE_MESSAGE_SENT_FROM_SOURCE);
			i.putExtra(Service.EXTRA_MESSAGE, msg);
			service.sendOrderedBroadcast(i, null);
		}
	}
	
}
