package com.megadevs.nostradamus.nostratoothhelper;

import java.util.Vector;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.foound.widget.AmazingAdapter;
import com.foound.widget.AmazingListView;
import com.megadevs.nostradamus.nostratooth.user.User;
import com.megadevs.nostradamus.nostratoothhelper.service.IService;
import com.megadevs.nostradamus.nostratoothhelper.service.Service;
import com.megadevs.nostradamus.nostratoothhelper.storage.MessageStorage;
import com.megadevs.nostradamus.nostratoothhelper.storage.UserStorage;

public class UserListFragment extends Fragment {
	
	public class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (Service.SERVICE_DISCOVERY_FINISHED.equals(action)) {
				calculateAdapterList();
				adapter.notifyDataSetChanged();
			} else if (Service.SERVICE_RECEIVE_MESSAGE.equals(action)) {
				calculateAdapterList();
				adapter.notifyDataSetChanged();
			}
		}
		
	}
	
	public class UserListAdapter extends AmazingAdapter {
		
		public static final int SECTION_RECENT = 0;
		public static final int SECTION_NEIGHBOURS = 1;
		public static final int SECTION_FRIENDS = 2;

		@Override
		public int getCount() {
			return recentUsers.size() + neighbourUsers.size() + friendUsers.size();
		}

		@Override
		public User getItem(int position) {
			if (position >= friendsStartPos) {
				return friendUsers.elementAt(position - friendsStartPos);
			} else if (position >= neighboursStartPos) {
				return neighbourUsers.elementAt(position - neighboursStartPos);
			} else {
				return recentUsers.elementAt(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		protected void onNextPageRequested(int page) {}

		@Override
		protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
			if (!displaySectionHeader) {
				view.findViewById(R.id.header).setVisibility(View.GONE);
			} else {
				view.findViewById(R.id.header).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.txt_section_name)).setText(getSections()[getSectionForPosition(position)]);
			}
		}

		@Override
		public View getAmazingView(int position, View convertView, ViewGroup parent) {
			User u = (User)getItem(position);
			convertView = LayoutInflater.from(getActivity()).inflate(R.layout.user_row, null);
			((FrameLayout)convertView.findViewById(R.id.badge)).addView(u.getBadge(getActivity()));
			((TextView)convertView.findViewById(R.id.txt_name)).setText(u.name);
			IService service = ((BluetoothTestActivity)getActivity()).getService();
			if (service != null && u.inRange(service.getNeighbours())) {
				((ImageView)convertView.findViewById(R.id.img_available)).setImageResource(android.R.drawable.presence_online);
			}
			int unread = MessageStorage.getInstance().getMessagesUnreadCount(u);
			if (unread > 0) {
				((TextView)convertView.findViewById(R.id.txt_unread_count)).setText(String.valueOf(unread));
			}
			if (getSectionForPosition(position) == SECTION_RECENT) {
				String last = MessageStorage.getInstance().getLastMessageFromConversation(u).text;
				convertView.findViewById(R.id.txt_last_message).setVisibility(View.VISIBLE);
				((TextView)convertView.findViewById(R.id.txt_last_message)).setText(last);
			}
			return convertView;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			TextView txt = (TextView)header.findViewById(R.id.txt_section_name);
			txt.setText(getSections()[getSectionForPosition(position)]);
			txt.setTextColor(alpha << 24 | (0xffffffff));
			header.setBackgroundColor(alpha << 24 | (0x000000));
		}

		@Override
		public int getPositionForSection(int section) {
			switch (section) {
			case SECTION_RECENT:
				return recentStartPos;
			case SECTION_NEIGHBOURS:
				return neighboursStartPos;
			case SECTION_FRIENDS:
				return friendsStartPos;
			default:
				return 0;
			}
		}

		@Override
		public int getSectionForPosition(int position) {
			if (position >= friendsStartPos) return SECTION_FRIENDS;
			else if (position >= neighboursStartPos) return SECTION_NEIGHBOURS;
			else return SECTION_RECENT;
		}

		@Override
		public String[] getSections() {
			String[] res = new String[3];
			res[SECTION_RECENT] = getString(R.string.recent_conversations);
			res[SECTION_NEIGHBOURS] = getString(R.string.in_range_users);
			res[SECTION_FRIENDS] = getString(R.string.my_friends);
			return res;
		}
		
	}
	
	private Receiver mReceiver;
	private UserListAdapter adapter;
	
	private Vector<User> recentUsers = new Vector<User>();
	private Vector<User> neighbourUsers = new Vector<User>();
	private Vector<User> friendUsers = new Vector<User>();
	
	private int recentStartPos = 0;
	private int neighboursStartPos = 0;
	private int friendsStartPos = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.user_list, null);
		calculateAdapterList();
		adapter = new UserListAdapter();
		AmazingListView list = (AmazingListView)v.findViewById(R.id.user_list);
		list.setPinnedHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.list_section, list, false));
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//				((BluetoothTestActivity)getActivity()).openConversation((User)adapter.getItemAtPosition(position));
			}
		});
		
		return v;
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
	
	private void calculateAdapterList() {
		Vector<String> recents = MessageStorage.getInstance().getConversationsAddresses();
		recentStartPos = (recents.size() == 0) ? -1 : 0;
		recentUsers = User.convertAddressesToUsers(getActivity(), recents);
		recentUsers = User.orderUsers(recentUsers, User.ORDER_DATE_DESC);
		neighboursStartPos = recentUsers.size();
		IService service = ((BluetoothTestActivity)getActivity()).getService();
		if (service != null) {
			neighbourUsers = User.convertNeighboursToUsers(getActivity(), service.getNeighbours());
			neighbourUsers = User.removeDuplicates(neighbourUsers, recentUsers);
			neighbourUsers = User.orderUsers(neighbourUsers, User.ORDER_ALPHANUMERIC_ASC);
		} else {
			neighbourUsers = new Vector<User>();
		}
		friendsStartPos = neighboursStartPos + neighbourUsers.size();
		friendUsers = UserStorage.getInstance().getUsers();
		friendUsers = User.removeDuplicates(friendUsers, neighbourUsers);
		friendUsers = User.removeDuplicates(friendUsers, recentUsers);
		friendUsers = User.orderUsers(friendUsers, User.ORDER_ALPHANUMERIC_ASC);
	}
	
}
