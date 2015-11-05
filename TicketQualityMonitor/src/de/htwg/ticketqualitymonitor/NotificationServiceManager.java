package de.htwg.ticketqualitymonitor;

import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationServiceManager {

	public static final String VIEWED_ISSUE_KEYS = "de.htwg.ticketqualitymonitor.viewed_issues";
	private final long intervalMillis;
	private final Class<? extends Service> serviceClass;

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
	}

	/**
	 * Starts the service.
	 */
	public void start(Context context) {
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

}
