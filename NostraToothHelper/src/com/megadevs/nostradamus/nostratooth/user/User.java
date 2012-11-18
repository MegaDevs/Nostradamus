package com.megadevs.nostradamus.nostratooth.user;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.PhoneLookup;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.QuickContactBadge;

import com.megadevs.nostradamus.nostratoothhelper.storage.UserStorage;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3562102265517567631L;
	
	public static final int BADGE_SIZE = 60;
	
	public static final int ORDER_DATE_DESC = 0;
	public static final int ORDER_ALPHANUMERIC_ASC = 1;

	public String address;
	public String name;
	public String phoneNumber;
	public String email;
	
	private boolean unknown = false;

	private long contactId = -1;

	public User(Context context, String name, String address, String phoneNumber, String email) {
		this(context, name, address, phoneNumber, email, false);
	}
	
	public User(Context context, String name, String address, String phoneNumber, String email, boolean unknown) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.unknown = unknown;

		String data[] = fetchContactData(context);
		if (data != null && data[0] != null) {
			contactId = Long.parseLong(data[0]);
			this.name = data[1];
		}
	}

	public long getContactId() {
		return contactId;
	}

	public Uri getContactUri() {
		if (contactId == -1) return null;
		return ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
	}

	public String getAddress() {
		return address;
	}

	public QuickContactBadge getBadge(Context context) {
		QuickContactBadge badge = new QuickContactBadge(context);
		badge.setLayoutParams(new LayoutParams(
				(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BADGE_SIZE, context.getResources().getDisplayMetrics()),
				(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BADGE_SIZE, context.getResources().getDisplayMetrics())));
		if (contactId != -1) {
			badge.assignContactUri(getContactUri());
			badge.setImageURI(getPhotoUri(context));
		} else {
			badge.setImageToDefault();
		}
		return badge;
	}
	
	public boolean inRange(Vector<BluetoothDevice> neighbours) {
		for (BluetoothDevice d : neighbours) {
			if (d.getAddress().equals(address)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isUnknown() {
		return unknown;
	}
	
	public static Vector<User> convertAddressesToUsers(Context context, Vector<String> addresses) {
		UserStorage us = UserStorage.getInstance();
		Vector<User> users = new Vector<User>();
		for (int i = 0; i < addresses.size(); i++) {
			String address = addresses.elementAt(i);
			if (us.containsAddress(address)) {
//				users.add(us.getUserFromAddress(address));
			} else {
				BluetoothDevice d = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
				users.add(new User(context, d.getName(), address, null, null, true));
			}
		}
		return users;
	}
	
	public static Vector<User> convertNeighboursToUsers(Context context, Vector<BluetoothDevice> neighbours) {
		Vector<String> addresses = new Vector<String>();
		for (int i = 0; i < neighbours.size(); i++) {
			addresses.add(neighbours.elementAt(i).getAddress());
		}
		return convertAddressesToUsers(context, addresses);
	}
	
	public static Vector<User> removeDuplicates(Vector<User> source, Vector<User> another) {
		Vector<User> out = new Vector<User>();
		for (int i = 0; i < source.size(); i++) {
			if (!another.contains(source.elementAt(i))) {
				out.add(source.elementAt(i));
			}
		}
		return out;
	}
	
	public static Vector<User> orderUsers(Vector<User> list, final int orderType) {
		User[] arr = new User[list.size()];
		list.toArray(arr);
		Arrays.sort(arr, new Comparator<User>() {
			@Override
			public int compare(User lhs, User rhs) {
				switch (orderType) {
				case ORDER_ALPHANUMERIC_ASC:
					return lhs.name.compareToIgnoreCase(rhs.name);
				case ORDER_DATE_DESC:
//					Message lmsg = MessageStorage.getInstance().getLastMessageFromConversation(lhs);
//					Message rmsg = MessageStorage.getInstance().getLastMessageFromConversation(rhs);
//					if (lmsg.timestamp < rmsg.timestamp) return 1;
//					else if (lmsg.timestamp > rmsg.timestamp) return -1;
//					else return 0;
				}
				return 0;
			}
		});
		return new Vector<User>(Arrays.asList(arr));
	}

	private String[] fetchContactData(Context context) {
		String data[] = null;
		if (phoneNumber != null) {
			data = fetchContactIdFromPhoneNumber(context, phoneNumber);
		}
		if (data == null && email != null) {
			data = fetchContactIdFromEmail(context, email);
		}
		return data;
	}

	private String[] fetchContactIdFromPhoneNumber(Context context, String phoneNumber) {
		String columnIndex[] = {PhoneLookup._ID, PhoneLookup.DISPLAY_NAME};
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		return doFetchContactData(context, uri, columnIndex);
	}
	
	private String[] fetchContactIdFromEmail(Context context, String email) {
		String columnIndex[] = {Email.CONTACT_ID, Email.DISPLAY_NAME_PRIMARY};
		Uri uri = Uri.withAppendedPath(Email.CONTENT_FILTER_URI, Uri.encode(email));
		return doFetchContactData(context, uri, columnIndex);
	}
	
	private String[] doFetchContactData(Context context, Uri uri, String[] columnIndex) {
		String data[] = new String[columnIndex.length];
		Cursor cursor = context.getContentResolver().query(uri, columnIndex, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				for (int i = 0; i < data.length; i++) {
					data[i] = cursor.getString(cursor.getColumnIndex(columnIndex[i]));
				}
			} while (cursor.moveToNext());
		}
		cursor.close();

		return data;
	}

	public Bitmap loadContactPhoto(ContentResolver cr) {
		if (contactId == -1) return null;
		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, getContactUri());
		if (input == null) {
			return null;
		}
		return BitmapFactory.decodeStream(input);
	}

	public Uri getPhotoUri(Context context) {
		ContentResolver contentResolver = context.getContentResolver();

		try {
			Cursor cursor = contentResolver
					.query(ContactsContract.Data.CONTENT_URI,
							null,
							ContactsContract.Data.CONTACT_ID
							+ "="
									+ contactId
									+ " AND "

	                                + ContactsContract.Data.MIMETYPE
	                                + "='"
	                                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
	                                + "'", null, null);

			if (cursor != null) {
				if (!cursor.moveToFirst()) {
					System.out.println("No photo");
					return null; // no photo
				}
				cursor.close();
			} else {
				System.out.println("error in cursor photo");
				return null; // error in cursor process
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
		return Uri.withAppendedPath(person,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
	}

}
