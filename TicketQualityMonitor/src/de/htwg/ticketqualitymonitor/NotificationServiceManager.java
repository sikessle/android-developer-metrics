package de.htwg.ticketqualitymonitor;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class NotificationServiceManager {

	private final long intervalMillis;
	private final Class<? extends Service> serviceClass;
	private final CriticalIssuesReceiver issuesReceiver;
	private boolean receiverRegistered;

	/**
	 * @param intervalMinutes
	 *            Inexact interval when the intent will be called.
	 * @param serviceClass
	 *            A class which emits a notification and will be periodically
	 *            called.
	 */
	public NotificationServiceManager(long intervalMinutes,
			Class<? extends Service> serviceClass) {
		this.serviceClass = serviceClass;
		intervalMillis = TimeUnit.MINUTES.toMillis(intervalMinutes);
		issuesReceiver = new CriticalIssuesReceiver();
	}

	/**
	 * Starts the service.
	 */
	public void start(Context context) {
		if (!receiverRegistered) {
			final IntentFilter filter = new IntentFilter(
					CriticalIssuesFetchService.BROADCAST_ACTION);
			LocalBroadcastManager.getInstance(context).registerReceiver(
					issuesReceiver, filter);
		}
		final PendingIntent pendingIntent = PendingIntent.getService(context,
				0, new Intent(context, serviceClass),
				PendingIntent.FLAG_CANCEL_CURRENT);
		getAlarmManager(context).setInexactRepeating(AlarmManager.RTC,
				System.currentTimeMillis() + intervalMillis, intervalMillis,
				pendingIntent);
		Log.i(NotificationServiceManager.class.getSimpleName(),
				"Notification service started.");
	}

	/**
	 * Stops the service.
	 */
	public void stop(Context context) {
		final PendingIntent pendingIntent = PendingIntent.getService(context,
				0, new Intent(context, serviceClass),
				PendingIntent.FLAG_CANCEL_CURRENT);

		getAlarmManager(context).cancel(pendingIntent);
		Log.i(NotificationServiceManager.class.getSimpleName(),
				"Notification service cancelled.");
	}

	private AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	public Set<String> getCriticalIssueKeys() {
		return issuesReceiver.getPreviousKeys();
	}

	public void setCriticalIssueKeys(Set<String> notifiedCriticalIssueKeys) {
		issuesReceiver.setPreviousKeys(notifiedCriticalIssueKeys);
	}

}
