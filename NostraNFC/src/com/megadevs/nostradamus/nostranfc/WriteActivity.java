package com.megadevs.nostradamus.nostranfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.megadevs.tagutilslib.TULReader;
import com.megadevs.tagutilslib.TULWriter;

@EActivity
public class WriteActivity extends Activity {

	private NfcAdapter nfcAdapter;
	
	@ViewById
	public EditText edit;
	
	@ViewById
	public TextView header;
	
	@Pref
	public Prefs_ prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		if (!prefs.role().exists()) {
			startActivity(new Intent(this, RoleChooser_.class));
		} else {
			header.setText(String.format("Scrittura informazioni emergenza da parte di %1$s", prefs.role().get()));
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = new Intent(this, WriteActivity_.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcAdapter.enableForegroundDispatch(this, pi, new IntentFilter[] {filter}, null);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		try {
			Tag tag = intent.getExtras().getParcelable(NfcAdapter.EXTRA_TAG);
			if (tag != null) {
				NdefRecord[] old = TULReader.readRecords(tag);
				NdefRecord appRecord = TULWriter.createApplicationRecord(getPackageName());
				NdefRecord roleRecord = TULWriter.createMimeRecord("application/"+getPackageName(), prefs.role().get());
				NdefRecord dataRecord = TULWriter.createMimeRecord("application/"+getPackageName(), edit.getText().toString());
				NdefRecord[] toWrite = new NdefRecord[old.length+2];
				System.arraycopy(old, 0, toWrite, 0, old.length-1);
				System.arraycopy(new NdefRecord[] {roleRecord, dataRecord, appRecord}, 0, toWrite, old.length-1, 3);
				TULWriter.write(tag, toWrite);
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
