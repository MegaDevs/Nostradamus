package com.megadevs.nostradamus.nostratoothhelper;

import java.util.Date;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.megadevs.nostradamus.nostratoothhelper.msg.Message;
import com.megadevs.nostradamus.nostratoothhelper.service.Service;
import com.megadevs.nostradamus.nostratoothhelper.storage.MessageStorage;
import com.megadevs.nostradamus.nostratoothhelper.storage.UserStorage;
import com.megadevs.nostradamus.nostratoothhelper.user.User;

public class ConversationFragment extends Fragment {
	
	public class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (Service.SERVICE_RECEIVE_MESSAGE.equals(action)) {
				Message msg = (Message)intent.getSerializableExtra(Service.EXTRA_MESSAGE);
				if (msg.source.equals(user.address)) {
					adapter.notifyDataSetChanged();
					abortBroadcast();
				}
			} else if (Service.SERVICE_MESSAGE_SENT_FROM_SOURCE.equals(action)) {
				Message msg = (Message)intent.getSerializableExtra(Service.EXTRA_MESSAGE);
				if (msg.destination.equals(user.address)) {
					adapter.notifyDataSetChanged();
					abortBroadcast();
				}
			} else if (Service.SERVICE_DISCOVERY_FINISHED.equals(action)) {
				updateSubtitle();
			}
		}
		
	}
	
	public class ConversationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return MessageStorage.getInstance().getMessagesCount(user);
		}

		@Override
		public Message getItem(int position) {
			return MessageStorage.getInstance().getMessages(user).elementAt(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Message msg = getItem(position);
			if (msg.source.equals(mainActivity.getMyAddress())) {
				MessageStorage.getInstance().setRead(msg, MessageStorage.OUT);
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.message_row_mine, null);
				int resTxt = 0;
				if (getItem(position).isAcked()) {
					resTxt = R.string.message_acked;
				} else if (getItem(position).isSent()) {
					resTxt = R.string.waiting_message_ack;
				} else {
					resTxt = R.string.sending_message;
				}
				((TextView)convertView.findViewById(R.id.txt_ack)).setText(resTxt);
				((FrameLayout)convertView.findViewById(R.id.badge)).addView(UserStorage.getInstance().getUserFromAddress(mainActivity.getMyAddress()).getBadge(mainActivity));
			} else {
				MessageStorage.getInstance().setRead(msg, MessageStorage.IN);
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.message_row_other, null);
				((FrameLayout)convertView.findViewById(R.id.badge)).addView(user.getBadge(mainActivity));
			}
			Date d = new Date(getItem(position).timestamp);
			String date = DateFormat.getTimeFormat(mainActivity).format(d)+" "+DateFormat.getDateFormat(mainActivity).format(d);
			((TextView)convertView.findViewById(R.id.txt_msg)).setText(getItem(position).text);
			((TextView)convertView.findViewById(R.id.txt_date)).setText(date);
			return convertView;
		}
		
	}
	
	public static final String EXTRA_USER = "com.megadevs.bluetoothtest.EXTRA_USER";
	
	private BluetoothTestActivity mainActivity;
	
	private Receiver mReceiver;

	private EditText eMessage;
	private ImageButton btnSend;
	private ListView messageList;
	
	private User user;
	
	private ConversationAdapter adapter;
	
	public ConversationFragment(User u) {
		user = u;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (BluetoothTestActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.conversation, null);
		
		adapter = new ConversationAdapter();
		messageList = (ListView)v.findViewById(R.id.conversation_list);
		messageList.setAdapter(adapter);
		
		btnSend = (ImageButton)v.findViewById(R.id.btn_send);
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				send(v);
			}
		});
		
		eMessage = (EditText)v.findViewById(R.id.edit_message);
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(Service.SERVICE_RECEIVE_MESSAGE);
		filter.addAction(Service.SERVICE_MESSAGE_SENT_FROM_SOURCE);
		filter.addAction(Service.SERVICE_DISCOVERY_FINISHED);
		mReceiver = new Receiver();
		getActivity().registerReceiver(mReceiver, filter);
		
		getActivity().getActionBar().setTitle(user.name);
		updateSubtitle();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
		
		getActivity().getActionBar().setTitle(R.string.app_name);
		getActivity().getActionBar().setSubtitle(null);
	}
	
	private void updateSubtitle() {
		if (mainActivity.getService() != null && user.inRange(mainActivity.getService().getNeighbours())) {
			getActivity().getActionBar().setSubtitle(R.string.user_in_range);
		}
	}
	
	public void send(View v) {
		if (mainActivity.getService() == null) {
			Toast.makeText(mainActivity, R.string.service_not_started, Toast.LENGTH_LONG).show();
		} else {
			Message msg = new Message();
			msg.source = mainActivity.getMyAddress();
			msg.destination = user.address;
			msg.text = eMessage.getText().toString();
			msg.sign();
			MessageStorage.getInstance().storeMessage(msg, MessageStorage.OUT);
			mainActivity.getService().sendMessage(Message.encrypt(msg));
			
			adapter.notifyDataSetChanged();
			eMessage.setText("");
		}
	}
	
}
