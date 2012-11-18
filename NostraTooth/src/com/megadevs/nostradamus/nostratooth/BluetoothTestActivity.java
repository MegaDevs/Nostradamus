package com.megadevs.nostradamus.nostratooth;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.googlecode.androidannotations.annotations.EActivity;
import com.megadevs.androiduserlibrary.AndroidUserLibrary;
import com.megadevs.androiduserlibrary.AndroidUserLibrary.OnSelectedAccountListener;
import com.megadevs.nostradamus.nostrapushreceiver.PushService;
import com.megadevs.nostradamus.nostratooth.service.IService;
import com.megadevs.nostradamus.nostratooth.service.Service;
import com.megadevs.nostradamus.nostratooth.service.Service_;
import com.megadevs.nostradamus.nostratooth.storage.MessageStorage;
import com.megadevs.nostradamus.nostratooth.storage.UserStorage;
import com.megadevs.nostradamus.nostratooth.user.SimpleUser;
import com.megadevs.nostradamus.nostratooth.user.User;

@EActivity
public class BluetoothTestActivity extends Activity {
	
	public class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (Service.SERVICE_DISCOVERY_STARTED.equals(action)) {
//				setProgressBarIndeterminateVisibility(true);
			} else if (Service.SERVICE_DISCOVERY_FINISHED.equals(action)) {
//				setProgressBarIndeterminateVisibility(false);
			}
		}
		
	}
	
	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//UUID.fromString("0fdba92f-d218-46d4-8150-97fb6925358f");//UUID.fromString("fd300e40-ad66-11e1-afa6-0800200c9a66");
//	public static final UUID MY_UUID = UUID.fromString("00001133-0000-1000-8000-00805f9b34fb");


	private Intent serviceIntent;
	private ServiceConnection serviceConn;
	private IService service;
	
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private MessageStorage storage;
	private UserStorage userDB;
	
	private Receiver mReceiver;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		
		initStorage();
		
		startService(new Intent(this, PushService.class));
		
		serviceIntent = new Intent(this, Service_.class);
		
		serviceConn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				BluetoothTestActivity.this.service = null;
				invalidateOptionsMenu();
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				BluetoothTestActivity.this.service = (IService)service;
				invalidateOptionsMenu();
			}
		};
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.replace(R.id.fragment_container, new UserListFragment());
		ft.replace(R.id.fragment_container, new DebugFragment_());
//		ft.replace(R.id.fragment_container, new EmergencyFragment_());
		ft.commit();
		
		setProgressBarVisibility(false);
		setProgressBarIndeterminateVisibility(false);
		
		processIntent(getIntent());
		
		if (Service.myUser == null) {
			final AndroidUserLibrary userLib = AndroidUserLibrary.getInstance(this);
			userLib.init(new OnSelectedAccountListener() {
				@Override
				public void onComplete() {
					Service.myUser = new SimpleUser(userLib.getOwnerName());
					Service.myUser.email = userLib.getOwnerEmail();
					Service.myUser.gid = userLib.getOwnerID();
					System.out.println("User data fetched: "+Service.myUser);
					finish();
				}
			});
		} else {
			finish();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		processIntent(intent);
	}
	
	private void processIntent(Intent intent) {
		String action = intent.getAction();
	}

	private void initStorage() {
		storage = MessageStorage.getInstance();
		if (storage.dataFile == null) {
			storage.dataFile = new File(getCacheDir(), "storage.dat");
			if (!storage.dataFile.exists()) {
				try {
					storage.dataFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (storage.storage == null) {
			storage.load();
		}
		
		userDB = UserStorage.getInstance();
		if (userDB.dataFile == null) {
			userDB.dataFile = new File(getCacheDir(), "userdb.dat");
			if (!userDB.dataFile.exists()) {
				try {
					userDB.dataFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (userDB.storage == null) {
			userDB.load();
			//TODO delete
			if (userDB.getUsersCount() == 0) {
				System.out.println("recreating users");
				userDB.storeUser(new User(this, "Dario", "38:16:D1:88:20:71", "3283840931", "dm0388@gmail.com"));
				userDB.storeUser(new User(this, "Seba", "6C:83:36:48:4A:7B", "3491696919", "dextorer@gmail.com"));
				userDB.storeUser(new User(this, "Ziby", "E0:D7:BA:B0:0D:D7", null, "bonnyfone@gmail.com"));
				userDB.storeUser(new User(this, "EVO", "38:E7:D8:2A:22:91", null, null));
				userDB.storeUser(new User(this, "Zeb", "18:46:17:96:BA:36", null, "zebganzo@gmail.com"));
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bindService(serviceIntent, serviceConn, 0);
		
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(Service.SERVICE_DISCOVERY_STARTED);
		filter.addAction(Service.SERVICE_DISCOVERY_FINISHED);
		mReceiver = new Receiver();
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unbindService(serviceConn);
		
		unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onBackPressed() {
		getActionBar().setDisplayHomeAsUpEnabled(false);
		super.onBackPressed();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu_refresh_neighbours, menu);
		mi.inflate(R.menu.menu_activate_toggle, menu);
        return true;
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem m = menu.findItem(R.id.menu_activate);
		if (service == null) {
			m.setIcon(android.R.drawable.button_onoff_indicator_off);
			m.setTitle(R.string.menu_activate_off);
		} else {
			m.setIcon(android.R.drawable.button_onoff_indicator_on);
			m.setTitle(R.string.menu_activate_on);
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_activate:
			toggleService();
			break;
		case R.id.menu_refresh_neighbours:
			service.refreshNeighbours();
			break;
		}
		return true;
	}
	
	private void toggleService() {
		if (service == null) {
			startService(serviceIntent);
			if (bindService(serviceIntent, serviceConn, 0)) {
				invalidateOptionsMenu();
			}
		} else {
			service.turnOff();
			service = null;
			invalidateOptionsMenu();
		}
	}
	
	public IService getService() {
		return service;
	}
	
	public String getMyAddress() {
		return mBluetoothAdapter.getAddress();
	}
	
	public void openConversation(User user) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, new ConversationFragment(user));
		ft.addToBackStack(null);
		ft.commit();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}