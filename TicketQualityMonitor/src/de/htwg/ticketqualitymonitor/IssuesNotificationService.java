package de.htwg.ticketqualitymonitor;

import java.util.HashSet;
import java.util.Set;

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
 * Notifies the user about new critical issues.
 */
public class IssuesNotificationService extends IntentService {

	public static final String INTENT_KEY_PROJECT = "projectKey";

	private JiraApi api;
	private String projectKey;

	private final Listener<JiraIssue[]> issuesListener;
	private final ErrorListener errorListener;
	private double thresholdCritical;

	public IssuesNotificationService() {
		super("Jira Issues Notification Service");

		errorListener = new NotificationIssuesErrorListener();
		issuesListener = new NotificationIssuesListener();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(IssuesNotificationService.class.getSimpleName(),
				"onHandleIntent called.");

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		thresholdCritical = Double.parseDouble(prefs.getString(
				getString(R.string.key_color_threshold_yellow), "2.0"));

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
		final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// hide the notification after it is selected
		notifi.flags |= Notification.FLAG_AUTO_CANCEL;

		manager.notify(0, notifi);
		Log.i(IssuesNotificationService.class.getSimpleName(),
				"Notification sent");
	}

	private class NotificationIssuesListener implements Listener<JiraIssue[]> {

		private final Set<String> previousCriticalIssues = new HashSet<>();

		@Override
		public void onResponse(JiraIssue[] issues) {
			final Set<String> currentCriticalIssues = new HashSet<>();

			for (final JiraIssue issue : issues) {
				if (issue.getSpentTimeHoursPerUpdate() > thresholdCritical) {
					currentCriticalIssues.add(issue.getKey());
				}
			}

			if (!previousCriticalIssues.containsAll(currentCriticalIssues)) {
				previousCriticalIssues.addAll(currentCriticalIssues);
				sendNotification();
			}
		}
	}

	private class NotificationIssuesErrorListener implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(NotificationIssuesErrorListener.class.getSimpleName(),
					error.toString());
		}

	}

}
