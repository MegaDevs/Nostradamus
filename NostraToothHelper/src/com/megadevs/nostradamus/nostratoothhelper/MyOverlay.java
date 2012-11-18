package com.megadevs.nostradamus.nostratoothhelper;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyOverlay extends ItemizedOverlay {
	private Context context;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public MyOverlay(Drawable icon, Context cont) {
		super(boundCenterBottom(icon));
		context = cont;
	}
	
	public void clear() {
		mOverlays.clear();
	}

	@Override
	protected OverlayItem createItem(int item) {
		return mOverlays.get(item);
	}

	@Override
	public int size() {
		 return mOverlays.size();
	}
	
	
	@Override
	protected boolean onTap(int itemIndex) {
		  OverlayItem item = mOverlays.get(itemIndex);
		  AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.show();
		  return true;
	}
	
	
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
	}
	
	public void refresh() {
		populate();
	}

}