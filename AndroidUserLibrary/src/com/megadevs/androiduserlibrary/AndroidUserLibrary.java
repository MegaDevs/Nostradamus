package com.megadevs.androiduserlibrary;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Profile;

import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;

@SuppressLint("NewApi")
public class AndroidUserLibrary {

	private Account[] accounts;
	private Context mContext;

	private int chosenAccount = -1;
	
	private static AndroidUserLibrary instance;
	private OnSelectedAccountListener listener;
	
	private AndroidUserLibrary(Activity activity) {
		mContext = activity;
	}
	
	public static AndroidUserLibrary getInstance(Activity activity) {
		if (instance == null)
			instance = new AndroidUserLibrary(activity);

		return instance;
	}
	
	public void init(OnSelectedAccountListener listener) {
		this.listener = listener;
		showAccountChooserDialog().show();
	}

	protected Dialog showAccountChooserDialog() {
		GoogleAccountManager googleAccountManager = new GoogleAccountManager(mContext);
		accounts = googleAccountManager.getAccounts();

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Select a Google account");
		
		final int size = accounts.length;
		String[] names = new String[size];
		for (int i = 0; i < size; i++) {
			names[i] = accounts[i].name;
		}
		
		builder.setItems(names, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				chosenAccount = which;
				listener.onComplete();
			}
		});
		return builder.create();
	}

	public static class OnSelectedAccountListener {
		public void onComplete() {
			;
		}
	}
	
	public String getOwnerName() {
		if (chosenAccount <0)
			return null;
		else return getName();
	}
	
	public String getOwnerID() {
		if (chosenAccount <0)
			return null;
		else return getID();
	}
	
	public String getOwnerEmail() {
		if (chosenAccount <0)
			return null;
		else return getEmail();
	} 
	
	private String getName() {
		final String[] SELF_PROJECTION = new String[] {Phone.DISPLAY_NAME,};
		Cursor cursor = mContext.getContentResolver().query(Profile.CONTENT_URI, SELF_PROJECTION, null, null, null);
		cursor.moveToFirst();

		return cursor.getString(0);
	}

	private String getID() {
		final String[] SELF_PROJECTION = new String[] {Phone._ID};
		Cursor cursor = mContext.getContentResolver().query(Profile.CONTENT_URI, SELF_PROJECTION, null, null, null);
		cursor.moveToFirst();

		return cursor.getString(0);
	}
	
	private String getEmail() {
		Account account = accounts[chosenAccount];
		return account.name;
	}

}
