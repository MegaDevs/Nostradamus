package com.megadevs.nostradamus.nostratoothhelper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.SystemService;
import com.megadevs.nostradamus.nostrapushreceiver.PushService;
import com.megadevs.nostradamus.nostrapushreceiver.Utils;
import com.megadevs.nostradamus.nostratooth.msg.Message;
import com.megadevs.nostradamus.nostratooth.user.SimpleUser;
import com.megadevs.nostradamus.nostratooth.user.User;
import com.megadevs.nostradamus.nostratoothhelper.json.Users;
import com.megadevs.nostradamus.nostratoothhelper.service.IService;
import com.megadevs.nostradamus.nostratoothhelper.service.Service;
import com.megadevs.nostradamus.nostratoothhelper.service.Service_;
import com.megadevs.nostradamus.nostratoothhelper.storage.MessageStorage;
import com.megadevs.nostradamus.nostratoothhelper.storage.UserStorage;

@EActivity
public class BluetoothTestActivity extends MapActivity implements LocationListener {
	
	public class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (Service.SERVICE_DISCOVERY_STARTED.equals(action)) {
//				setProgressBarIndeterminateVisibility(true);
			} else if (Service.SERVICE_DISCOVERY_FINISHED.equals(action)) {
//				setProgressBarIndeterminateVisibility(false);
			} else if (Service.SERVICE_RECEIVE_MESSAGE.equals(action)) {
				lastMessage = (Message)intent.getSerializableExtra(Service.EXTRA_MESSAGE);
				int tot = lastMessage.knowledge.knowledge.size();
				if (tot > 0) {
					header.setText(String.format("Persone rilevate: %1$d", tot));
				} else {
					header.setText("Nessuna persona rilevata");
				}
			}
		}
		
	}
	
//	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//UUID.fromString("0fdba92f-d218-46d4-8150-97fb6925358f");//UUID.fromString("fd300e40-ad66-11e1-afa6-0800200c9a66");
	public static final UUID MY_UUID = UUID.fromString("00001133-0000-1000-8000-00805f9b34fb");


	private Intent serviceIntent;
	private ServiceConnection serviceConn;
	private IService service;
	
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private MessageStorage storage;
	private UserStorage userDB;
	
	private Receiver mReceiver;
	
	private TextView header;
	private MapView map;
	private MyLocationOverlay myLoc;
	private MyOverlay peopleOverlay;
	private Location lastLocation;
	private String provider;
	
	@SystemService
	public LocationManager locationManager;
	
	private Message lastMessage;
	
	private Thread fetchMapData = new Thread() {
		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(3000);
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet req = new HttpGet("http://nostradamus-whymca.appspot.com/get_nearby_users");
					HttpResponse resp = client.execute(req);
					String data = Utils.readInputStreamAsString(resp.getEntity().getContent());
					Gson g = new Gson();
					Users[] fromJson = g.fromJson(data, Users[].class);
					updateMapData(fromJson);
				}
			} catch (Exception e) {}
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getActionBar().setLogo(R.drawable.logo1);
		getActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.main);
		
		header = (TextView) findViewById(R.id.header);
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (lastMessage != null) {
					int tot = lastMessage.knowledge.knowledge.size();
					if (tot == 0) {
						Toast.makeText(BluetoothTestActivity.this, "Nessuna persona nelle vicinanze", Toast.LENGTH_LONG).show();
						return;
					}
					String[] ele = new String[tot];
					int i = 0;
					for (SimpleUser u : lastMessage.knowledge.knowledge.values()) {
						ele[i++] = u.name;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothTestActivity.this);
					builder.setTitle("Persone rilevate")
						.setItems(ele, null)
						.show();
				} else {
					Toast.makeText(BluetoothTestActivity.this, "Nessuna persona nelle vicinanze", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		map = (MapView) findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		myLoc = new MyLocationOverlay(this, map);
		peopleOverlay = new MyOverlay(getResources().getDrawable(R.drawable.marker_red), this);
		
		fetchMapData.start();
		
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
		
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.replace(R.id.fragment_container, new UserListFragment());
//		ft.replace(R.id.fragment_container, new DebugFragment_());
//		ft.replace(R.id.fragment_container, new EmergencyFragment_());
//		ft.commit();
		
		setProgressBarVisibility(false);
		setProgressBarIndeterminateVisibility(false);
		
		processIntent(getIntent());
		
//		if (Service.myUser == null) {
//			final AndroidUserLibrary userLib = AndroidUserLibrary.getInstance(this);
//			userLib.init(new OnSelectedAccountListener() {
//				@Override
//				public void onComplete() {
//					Service.myUser = new SimpleUser(userLib.getOwnerName());
//					Service.myUser.email = userLib.getOwnerEmail();
//					Service.myUser.gid = userLib.getOwnerID();
//					finish();
//				}
//			});
//		} else {
//			finish();
//		}
	}

	private void updateMapData(final Users[] users) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (users.length > 0) {
					peopleOverlay.clear();
					for (Users u : users) {
						GeoPoint geo = new GeoPoint((int)(Double.parseDouble(u.getLatitude())*1E6), (int)(Double.parseDouble(u.getLongitude())*1E6));
						peopleOverlay.addOverlay(new OverlayItem(geo, u.getUsername(), u.getEmail()));
					}
					peopleOverlay.refresh();
				}
			}
		});
	}
	
	@Override
	public void onNewIntent(Intent intent) {
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
		filter.addAction(Service.SERVICE_RECEIVE_MESSAGE);
		mReceiver = new Receiver();
		registerReceiver(mReceiver, filter);
		
		checkLocationProvider();
		myLoc.enableMyLocation();
		map.getOverlays().add(myLoc);
		map.getOverlays().add(peopleOverlay);
		if (myLoc != null && myLoc.getMyLocation() != null) {
			map.getController().animateTo(myLoc.getMyLocation());
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unbindService(serviceConn);
		
		unregisterReceiver(mReceiver);
		
		locationManager.removeUpdates(this);
		myLoc.disableMyLocation();
	}
	
	@Override
	public void onBackPressed() {
		getActionBar().setDisplayHomeAsUpEnabled(false);
		super.onBackPressed();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
//		mi.inflate(R.menu.menu_refresh_neighbours, menu);
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
//			toggleService();
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

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		lastLocation = location;
		moveToLastLocation();
	}

	@Override
	public void onProviderDisabled(String provider) {
		//invocato quando il provider viene disattivato
		checkLocationProvider();
	}

	@Override
	public void onProviderEnabled(String provider) {
		//invocato quando il provider viene abilitato
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		//invocato quando il provider subisce un cambio di stato
	}
	
	private void checkLocationProvider(){
		locationManager.removeUpdates(this);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locationManager.getBestProvider(criteria, false);
//		Toast.makeText(this, "Best provider: "+provider, Toast.LENGTH_SHORT).show();
		lastLocation = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 1000, 1, this);
	}

	private void moveToLastLocation(){
		if(lastLocation!=null){
			GeoPoint myPos = new GeoPoint((int)(1E6*lastLocation.getLatitude()),(int)(1E6*lastLocation.getLongitude()));
			map.getController().animateTo(myPos);
		}
	}
}