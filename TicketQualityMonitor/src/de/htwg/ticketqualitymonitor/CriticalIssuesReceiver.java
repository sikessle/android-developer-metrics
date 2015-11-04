package de.htwg.ticketqualitymonitor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class CriticalIssuesReceiver extends BroadcastReceiver {

	private Set<String> previousKeys = new HashSet<>();

	public Set<String> getPreviousKeys() {
		return previousKeys;
	}

	public void setPreviousKeys(Set<String> previousKeys) {
		this.previousKeys = previousKeys;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String[] keys = intent
				.getStringArrayExtra(CriticalIssuesFetchService.EXTRA_ISSUES_KEY);
		final Set<String> currentKeys = new HashSet<>(Arrays.asList(keys));

		Log.i(CriticalIssuesReceiver.class.getSimpleName(),
				"Broadcast received. Checking for new critical issues.");

		if (!previousKeys.containsAll(currentKeys)) {
			previousKeys.addAll(currentKeys);
			sendNotification(context);
		}
	}

	private void sendNotification(Context context) {
		final PendingIntent startAppIntent = PendingIntent.getActivity(context,
				0, new Intent(context, MainActivity.class), 0);

		final Notification notifi = new NotificationCompat.Builder(context)
				.setContentTitle(context.getString(R.string.notification_title))
				.setContentText(
						context.getString(R.string.notification_description))
				.setContentIntent(startAppIntent)
				.setSmallIcon(R.drawable.ic_launcher).build();
		final NotificationManager manager = (NotificationManager) context
				.getSystemService(IntentService.NOTIFICATION_SERVICE);

		// hide the notification after it is selected
		notifi.flags |= Notification.FLAG_AUTO_CANCEL;

		manager.notify(0, notifi);
		Log.i(CriticalIssuesReceiver.class.getSimpleName(), "Notification sent");
	}

}
