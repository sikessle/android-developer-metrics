package de.htwg.ticketqualitymonitor;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;
import de.htwg.ticketqualitymonitor.model.JiraIssue;

/**
 * Retrieves critical issues.
 */
public class CriticalIssuesFetchService extends IntentService {

	public static final String INTENT_KEY_PROJECT = "projectKey";

	private JiraApi api;
	private String projectKey;

	private final Listener<JiraIssue[]> issuesListener;
	private final ErrorListener errorListener;

	public CriticalIssuesFetchService() {
		super("Jira Issues Fetch Service");

		errorListener = new NotificationIssuesErrorListener();
		issuesListener = new NotificationIssuesListener();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(CriticalIssuesFetchService.class.getSimpleName(),
				"onHandleIntent called.");

		setupInstanceVariablesFromPrefs();
		loadIssues();
	}

	private void setupInstanceVariablesFromPrefs() {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		api = JiraApiFactory.getInstance(this);
		projectKey = prefs.getString(getString(R.string.key_project), "none");
	}

	private void loadIssues() {
		api.getAssignedInProgressIssuess(projectKey, issuesListener,
				errorListener);
	}

	private void sendNotification() {
		final PendingIntent startAppIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		final Notification notifi = new NotificationCompat.Builder(this)
				.setContentTitle(getString(R.string.notification_title))
				.setContentText(getString(R.string.notification_description))
				.setContentIntent(startAppIntent)
				.setSmallIcon(R.drawable.ic_launcher).build();
		final NotificationManager manager = (NotificationManager) getSystemService(IntentService.NOTIFICATION_SERVICE);

		// hide the notification after it is selected
		notifi.flags |= Notification.FLAG_AUTO_CANCEL;

		manager.notify(0, notifi);
		Log.i(CriticalIssuesFetchService.class.getSimpleName(),
				"Notification sent.");
	}

	/**
	 * Checks if any new relevant issues are available. If yes, then send a
	 * notification.
	 */
	private class NotificationIssuesListener implements Listener<JiraIssue[]> {

		@Override
		public void onResponse(JiraIssue[] issues) {
			final boolean allRelevantIssuesSeen = ViewedIssuesHandler
					.allRelevantIssuesSeen(CriticalIssuesFetchService.this,
							issues);

			if (!allRelevantIssuesSeen) {
				sendNotification();
			} else {
				Log.i(CriticalIssuesFetchService.class.getSimpleName(),
						"No new critical issues found.");
			}
		}
	}

	/**
	 * Handles error, does mainly logging.
	 */
	private class NotificationIssuesErrorListener implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(NotificationIssuesErrorListener.class.getSimpleName(),
					error.toString());
		}

	}

}
