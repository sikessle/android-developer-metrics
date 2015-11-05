package de.htwg.ticketqualitymonitor;

import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationServiceManager {

	/**
	 * Toggles the service.
	 */
	public static void setState(Context context,
			Class<? extends Service> serviceClass, boolean start) {
		if (start) {
			start(context, serviceClass);
		} else {
			stop(context, serviceClass);
		}
	}

	/**
	 * Starts the service.
	 */
	public static void start(Context context,
			Class<? extends Service> serviceClass) {
		final Intent serviceIntent = new Intent(context, serviceClass);
		final PendingIntent pendingIntent = PendingIntent.getService(context,
				0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		final long intervalMillis = getIntervalMillis(context);

		final long firstStart = System.currentTimeMillis() + intervalMillis;

		getAM(context).setInexactRepeating(AlarmManager.RTC, firstStart,
				intervalMillis, pendingIntent);

		Log.i(NotificationServiceManager.class.getSimpleName(),
				"Notification service started.");
	}

	private static long getIntervalMillis(Context context) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		final int intervalMinutes = Integer.parseInt(prefs.getString(
				context.getString(R.string.key_notifications_interval), "1"));

		return TimeUnit.MINUTES.toMillis(intervalMinutes);
	}

	/**
	 * Stops the service.
	 */
	public static void stop(Context context,
			Class<? extends Service> serviceClass) {
		final Intent serviceIntent = new Intent(context, serviceClass);
		final PendingIntent pendingIntent = PendingIntent.getService(context,
				0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		getAM(context).cancel(pendingIntent);

		Log.i(NotificationServiceManager.class.getSimpleName(),
				"Notification service cancelled.");
	}

	private static AlarmManager getAM(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

}
