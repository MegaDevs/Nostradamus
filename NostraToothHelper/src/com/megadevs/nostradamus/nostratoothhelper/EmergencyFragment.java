package com.megadevs.nostradamus.nostratoothhelper;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googlecode.androidannotations.annotations.EFragment;
import com.megadevs.nostradamus.nostratoothhelper.msg.Message;
import com.megadevs.nostradamus.nostratoothhelper.service.Service;
import com.megadevs.nostradamus.nostratoothhelper.user.SimpleUser;

@EFragment(R.layout.emergency)
public class EmergencyFragment extends Fragment {
	
	public class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (Service.SERVICE_DISCOVERY_FINISHED.equals(action)) {
//				calculateAdapterList();
//				adapter.notifyDataSetChanged();
			} else if (Service.SERVICE_RECEIVE_MESSAGE.equals(action)) {
				showMessage((Message)intent.getSerializableExtra(Service.EXTRA_MESSAGE));
			}
		}
		
	}
	
	private Receiver mReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter();
		filter.setPriority(100);
		filter.addAction(Service.SERVICE_RECEIVE_MESSAGE);
		filter.addAction(Service.SERVICE_DISCOVERY_FINISHED);
		mReceiver = new Receiver();
		getActivity().registerReceiver(mReceiver, filter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
	}
	
	private void showMessage(Message msg) {
		System.out.println("Received message: "+msg.toString());
		StringBuilder sb = new StringBuilder();
		int num = msg.knowledge.knowledge.size();
		sb.append(num).append("\n");
		for (SimpleUser u : msg.knowledge.knowledge.values()) {
			sb.append(u.name).append("\n");
		}
//		txt.setText(sb.toString());
	}
	
}
