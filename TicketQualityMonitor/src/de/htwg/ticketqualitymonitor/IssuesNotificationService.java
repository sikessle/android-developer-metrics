package de.htwg.ticketqualitymonitor;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;
import de.htwg.ticketqualitymonitor.model.JiraIssue;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Notifies the user about new critical issues.
 */
public class IssuesNotificationService extends IntentService {

	public static final String INTENT_KEY_PROJECT = "projectKey";

	private JiraApi api;
	private String projectKey;

	private Listener<JiraIssue[]> issuesListener;
	private ErrorListener errorListener;

	public IssuesNotificationService() {
		super("Jira Issues Notification Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(IssuesNotificationService.class.getSimpleName(),
				"onHandleIntent called.");

		setUpApiAndProjectKey();
		loadIssues();
	}

	private void setUpApiAndProjectKey() {
		if (api == null) {
			api = JiraApiFactory.createInstance(this);
		}
		if (projectKey == null) {
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			projectKey = prefs.getString(getString(R.string.key_project),
					"none");
		}
	}

	private void loadIssues() {
		sendNotification();
		// TODO notify..
		// api.getAssignedInProgressIssuess(projectKey, issuesListener,
		// errorListener);
	}

	private void sendNotification() {
		final PendingIntent startAppIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		final Notification notifi = new NotificationCompat.Builder(this)
				.setContentTitle(getString(R.string.notification_title))
				.setContentText(getString(R.string.notification_description))
				.setContentIntent(startAppIntent)
				.setSmallIcon(R.drawable.ic_launcher).build();
		final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// hide the notification after its selected
		notifi.flags |= Notification.FLAG_AUTO_CANCEL;

		manager.notify(0, notifi);
	}

}
