package com.megadevs.nostradamus.nostratooth.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.Vector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.Parcelable;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EService;
import com.megadevs.nostradamus.nostratooth.BluetoothTestActivity;
import com.megadevs.nostradamus.nostratooth.BluetoothTestActivity_;
import com.megadevs.nostradamus.nostratooth.R;
import com.megadevs.nostradamus.nostratooth.SetScanModeActivity;
import com.megadevs.nostradamus.nostratooth.SetScanModeActivity_;
import com.megadevs.nostradamus.nostratooth.msg.Knowledge;
import com.megadevs.nostradamus.nostratooth.msg.Message;
import com.megadevs.nostradamus.nostratooth.storage.MessageStorage;
import com.megadevs.nostradamus.nostratooth.user.SimpleUser;

@EService
public class Service extends android.app.Service {
	
	public class Receiver extends BroadcastReceiver {
		
		private int discoveryDeviceCount = 0;
		private int discoveryDeviceAsyncCount = 0;
		private int discoveryDeviceAsyncReturnedCount = 0;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				System.out.println("starting discovery");
				neighbours.clear();
				discoveryDeviceCount = 0;
				discoveryDeviceAsyncCount = 0;
				discoveryDeviceAsyncReturnedCount = 0;
				
				Intent i = new Intent(Service.SERVICE_DISCOVERY_STARTED);
				sendOrderedBroadcast(i, null);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				if (discoveryDeviceAsyncCount == 0) {
					finishDiscovery();
				}
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				discoveryDeviceCount++;
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				System.out.println("Found: "+device.getName());

				ParcelUuid uuids[] = device.getUuids();
				boolean compatible = false;
				if (uuids != null) {
					for (int i = 0; i < uuids.length; i++) {
						if (uuids[i].getUuid().equals(BluetoothTestActivity.MY_UUID)) {
							compatible = true;
							System.out.println("Compatible!");
							if (!neighbours.contains(device)) {
								neighbours.add(device);
							}
							break;
						}
					}
				}
				if (!compatible) {
					System.out.println("NOT compatible!");
					discoveryDeviceAsyncCount++;
					device.fetchUuidsWithSdp();
				}
			} else if (BluetoothDevice.ACTION_UUID.equals(action)) {
				BluetoothDevice d = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				System.out.println("After fetching UUID with SDP for device " + d.getName());
				Parcelable[] uuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
				if (uuids != null) {
					for (int i = 0; i < uuids.length; i++) {
						System.out.println(((ParcelUuid)uuids[i]).getUuid());
						if (((ParcelUuid)uuids[i]).getUuid().equals(BluetoothTestActivity.MY_UUID)) {
							if (!neighbours.contains(d)) {
								neighbours.add(d);
							}
							break;
						}
					}
				}
				if (++discoveryDeviceAsyncReturnedCount == discoveryDeviceAsyncCount) {
					finishDiscovery();
				}
			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)) {
				case BluetoothAdapter.STATE_ON:
					bluetoothEnabled = true;
					init();
					break;
				default:
					deInit();
					bluetoothEnabled = false;
					break;
				}
			} else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
				switch (intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE)) {
				case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
					break;
				default:
					askScanMode();
					break;
				}
			}
		}
		
		private void finishDiscovery() {
			System.out.println("finished discovery");
			lastDiscoverTime = System.currentTimeMillis();
			
			for (int i = 0; i < finishDiscoveryListeners.size(); i++) {
				NeighboursAvailableListener l = finishDiscoveryListeners.elementAt(i);
				if (neighbours.size() == 0) l.onNoNeighboursAvailable();
				else l.onNeighboursAvailable(neighbours);
			}
			finishDiscoveryListeners.clear();
			
			Intent i = new Intent(Service.SERVICE_DISCOVERY_FINISHED);
			sendOrderedBroadcast(i, null);
		}

	}
	
	public class ServiceBinder extends Binder implements IService {

		@Override
		public void sendMessage(Message msg) {
			send(msg);
		}
		
		@Override
		public void refreshNeighbours() {
			if (!mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.startDiscovery();
			}
		}
		
		@Override
		public void turnOff() {
			stopSelf();
		}
		
		@Override
		public String getMyAddress() {
			return Service.this.getMyAddress();
		}
		
		@Override
		public Vector<BluetoothDevice> getNeighbours() {
			return neighbours;
		}

		@Override
		public void init() {
			Service.this.init();
		}

		@Override
		public void deInit() {
			Service.this.deInit();
		}
		
	}
	
	public static final String SERVICE_RECEIVE_MESSAGE = "com.megadevs.bluetoothtest.SERVICE_RECEIVE_MESSAGE";
	public static final String SERVICE_DISCOVERY_STARTED = "com.megadevs.bluetoothtest.SERVICE_DISCOVERY_STARTED";
	public static final String SERVICE_DISCOVERY_FINISHED = "com.megadevs.bluetoothtest.SERVICE_DISCOVERY_FINISHED";
	public static final String SERVICE_MESSAGE_SENT_FROM_SOURCE = "com.megadevs.bluetoothtest.SERVICE_MESSAGE_SENT_FROM_SOURCE";
	public static final String EXTRA_MESSAGE = "com.megadevs.bluetoothtest.EXTRA_MESSAGE";
	
	public static final int NOTIFICATION_ID = 1;
	
	private ServiceBinder mBinder;
	private BluetoothAdapter mBluetoothAdapter;
	private Receiver mReceiver;
	private BluetoothServerSocket srvSocket;
	private BluetoothSocket socket;
	
	private Vector<BluetoothDevice> neighbours = new Vector<BluetoothDevice>();
	private Vector<NeighboursAvailableListener> finishDiscoveryListeners = new Vector<NeighboursAvailableListener>();
	
	private boolean bluetoothEnabled = false;
	private boolean listening = false;
	
	private boolean autoDiscoverEnabled = true;
	private long AUTO_DISCOVER_SLEEP_TIME = 3 * 60 * 1000;
	private long MAX_DISCOVER_TIME_DIFF = AUTO_DISCOVER_SLEEP_TIME;
	private long lastDiscoverTime;
	
	private MessageStorage storage;
	
	private Handler mHandler = new Handler();
	
	private Knowledge myKnowledge = new Knowledge();

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("service created");
		showNotification();
		
		storage = MessageStorage.getInstance();
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		mBinder = new ServiceBinder();
		
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_UUID);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		mReceiver = new Receiver();
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}
	
	private void activateDiscoverable() {
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		} else if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			askScanMode();
		}
	}
	
	private void askScanMode() {
		System.out.println("asking scan mode...");
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Hello";
		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		
		Intent notificationIntent = new Intent(Intent.ACTION_MAIN);
		notificationIntent.setClass(getApplicationContext(), SetScanModeActivity_.class);
		PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		Notification.Builder b = new Notification.Builder(getApplicationContext());
		b.setContentTitle(contentTitle).setContentText(contentText).setTicker(tickerText).setSmallIcon(icon);
		b.setFullScreenIntent(contentIntent, false);
		
		mNotificationManager.notify(SetScanModeActivity.NOTIFICATION_ID, b.getNotification());
	}
	
	public void init() {
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			askScanMode();
		}
		listen();
		autoDiscover();
		myKnowledge.update(getMyAddress(), getMyUser());
		Random rand = new Random(System.currentTimeMillis());
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				autoSend();
			}
		}, (20 + rand.nextInt(20)) * 1000);
	}
	
	public void deInit() {
		stopListen();
		stopAutoDiscover();
	}

	@Override
	public IService onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("service started");
		if (!mBluetoothAdapter.isEnabled()) {
			activateDiscoverable();
		} else {
			init();
		}
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideNotification();
		stopListen();
		unregisterReceiver(mReceiver);
		mBluetoothAdapter.disable();
		System.out.println("service destroyed");
	}
	
	@Background
	public void autoDiscover() {
		autoDiscoverEnabled = true;
		try {
			while (autoDiscoverEnabled) {
				if (!mBluetoothAdapter.isDiscovering()) {
					mBluetoothAdapter.startDiscovery();
				}
				Thread.sleep(AUTO_DISCOVER_SLEEP_TIME);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void stopAutoDiscover() {
		autoDiscoverEnabled = false;
	}
	
	public void cancelDiscovery() {
		mBluetoothAdapter.cancelDiscovery();
	}
	
	public String getMyAddress() {
		return mBluetoothAdapter.getAddress();
	}
	
	public SimpleUser getMyUser() {
		return new SimpleUser(mBluetoothAdapter.getName());
	}
	
	public void getNeighbours(NeighboursAvailableListener listener) {
		if (neighbours.size() == 0 || (System.currentTimeMillis() - lastDiscoverTime > MAX_DISCOVER_TIME_DIFF) || mBluetoothAdapter.isDiscovering()) {
			finishDiscoveryListeners.add(listener);
			if (!mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.startDiscovery();
			}
		} else {
			listener.onNeighboursAvailable(neighbours);
		}
	}
	
	@Background
	public void listen() {
		if (listening) return;
		try {
			System.out.println("listening");
			listening = true;
			srvSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Test", BluetoothTestActivity.MY_UUID);
			socket = srvSocket.accept();
			System.out.println("accepted");
			
			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			Message msg = (Message)(ois.readObject());
			myKnowledge.merge(msg.knowledge);
			myKnowledge.update(getMyAddress(), getMyUser());
			msg.updateKnowledge(myKnowledge);
			send(msg);
			
			Intent i = new Intent(SERVICE_RECEIVE_MESSAGE);
			i.putExtra(EXTRA_MESSAGE, msg);
			sendOrderedBroadcast(i, null);
			
			ois.close();
			is.close();
			
			socket.close();
			srvSocket.close();
			System.out.println("closed");
			
			listening = false;
			
			listen();
		} catch (IOException e) {
			listening = false;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			listening = false;
//			e.printStackTrace();
		}
	}
	
	private void stopListen() {
		if (srvSocket != null) {
			try {
				srvSocket.close();
				listening = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}
	
	@Background
	public void send(Message msg) {
		new Deliverer(msg, this).start();
	}
	
	public void autoSend() {
		Random rand = new Random(System.currentTimeMillis());
		Message msg = new Message();
		msg.source = getMyAddress();
		msg.destination = String.valueOf(rand.nextInt());
		msg.text = String.valueOf(rand.nextInt());
		msg.updateKnowledge(myKnowledge);
		msg.sign();
//		MessageStorage.getInstance().storeMessage(msg, MessageStorage.OUT);
		send(Message.encrypt(msg));
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				autoSend();
			}
		}, (50 + rand.nextInt(20)) * 1000);
	}
	
	private void showNotification() {
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = getApplicationContext().getResources().getString(R.string.app_running);
		CharSequence contentTitle = tickerText;
		CharSequence contentText = getApplicationContext().getResources().getString(R.string.click_to_show_app);
		
		Intent notificationIntent = new Intent(Intent.ACTION_MAIN);
		notificationIntent.setClass(getApplicationContext(), BluetoothTestActivity_.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
		
		Notification.Builder b = new Notification.Builder(getApplicationContext());
		b.setContentTitle(contentTitle)
			.setContentText(contentText)
			.setTicker(tickerText)
			.setSmallIcon(icon)
			.setOngoing(true)
			.setContentIntent(contentIntent);
		
		startForeground(NOTIFICATION_ID, b.getNotification());
	}
	
	private void hideNotification() {
		stopForeground(true);
	}

}
