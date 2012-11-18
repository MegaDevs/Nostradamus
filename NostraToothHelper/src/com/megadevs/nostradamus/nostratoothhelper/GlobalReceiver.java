package com.megadevs.nostradamus.nostratoothhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.megadevs.nostradamus.nostrapushreceiver.PushService;
import com.megadevs.nostradamus.nostratoothhelper.service.Service;
import com.megadevs.nostradamus.nostratoothhelper.service.Service_;

public class GlobalReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (Service.SERVICE_RECEIVE_MESSAGE.equals(action)) {
//			Message msg = (Message)intent.getSerializableExtra(Service.EXTRA_MESSAGE);
//			if (msg.getClass().equals(Message.class)) {
//				showNotification(context, msg);
//			}
		}
		
		if (PushService.ACTION_ENABLE_EMERGENCY.equals(action)) {
			System.out.println("Emergency enabled");
			context.startService(new Intent(context, Service_.class));
		} else if (PushService.ACTION_DISABLE_EMERGENCY.equals(action)) {
			
		}
	}
	
//	private void showNotification(Context context, Message msg) {
//		User u = UserStorage.getInstance().getUserFromAddress(msg.source);
//		
//		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		int icon = R.drawable.ic_launcher;
//		CharSequence tickerText = String.format(context.getResources().getString(R.string.new_message_from), u.name);
//		CharSequence contentTitle = u.name;
//		CharSequence contentText = msg.text;
//		
//		Intent notificationIntent = new Intent(BluetoothTestActivity.ACTION_SHOW_CONVERSATION);
//		notificationIntent.setClass(context, BluetoothTestActivity_.class);
//		notificationIntent.putExtra(ConversationFragment.EXTRA_USER, u);
//		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//		
//		Notification.Builder b = new Notification.Builder(context);
//		b.setContentTitle(contentTitle)
//			.setContentText(contentText)
//			.setTicker(tickerText)
//			.setSmallIcon(icon)
//			.setLargeIcon(u.loadContactPhoto(context.getContentResolver()))
//			.setWhen(msg.timestamp)
//			.setAutoCancel(true)
//			.setContentIntent(contentIntent);
//		
//		mNotificationManager.notify(msg.signature, b.getNotification());
//	}

}
