package com.megadevs.nostradamus.nostranfc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.megadevs.tagutilslib.TULReader;

@EActivity(R.layout.activity_read)
public class ReadActivity extends Activity {

	@ViewById
	public TextView header;
	
	@ViewById
	public TextView txt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setLogo(R.drawable.logo1);
		getActionBar().setDisplayShowTitleEnabled(false);
		manageIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
//		manageIntent(intent);
		setIntent(intent);
	}
	
	@AfterViews
	public void afterView() {
		manageIntent(getIntent());
	}
	
	private void manageIntent(Intent intent) {
		try {
			Tag tag = intent.getExtras().getParcelable(NfcAdapter.EXTRA_TAG);
			if (tag != null) {
				String[] msgs = TULReader.readStrings(tag);
				int i = 0;
				while (i < msgs.length -1) {
					String ente = msgs[i++];
					String messaggio = msgs[i++];
					SpannableStringBuilder ssb = new SpannableStringBuilder();
					ssb.append(ente).append(": ").append(messaggio).append("\n");
					ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, ente.length(), 0);
					txt.append(ssb);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
