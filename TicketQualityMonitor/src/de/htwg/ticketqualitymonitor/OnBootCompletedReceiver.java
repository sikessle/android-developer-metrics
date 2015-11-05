package de.htwg.ticketqualitymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(OnBootCompletedReceiver.class.getSimpleName(),
				"Device booted. Setting notification service state.");
		NotificationServiceManager.startOrStopBasedOnPreference(context);
	}

}
