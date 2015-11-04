package de.htwg.ticketqualitymonitor;

import java.util.HashSet;
import java.util.Set;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
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
	// Defines a custom Intent action
	public static final String BROADCAST_ACTION = "de.htwg.ticketqualitymonitor.CRITICAL_ISSUES_BROADCAST";
	public static final String EXTRA_ISSUES_KEY = "de.htwg.ticketqualitymonitor.ISSUES";

	private JiraApi api;
	private String projectKey;

	private final Listener<JiraIssue[]> issuesListener;
	private final ErrorListener errorListener;
	private double thresholdCritical;

	public CriticalIssuesFetchService() {
		super("Jira Issues Fetch Service");

		errorListener = new NotificationIssuesErrorListener();
		issuesListener = new NotificationIssuesListener();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(CriticalIssuesFetchService.class.getSimpleName(),
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

	private void sendBroadcastWithCriticalIssues(Set<String> criticalIssueKeys) {
		final Intent localIntent = new Intent(BROADCAST_ACTION);
		localIntent.putExtra(EXTRA_ISSUES_KEY,
				criticalIssueKeys.toArray(new String[] {}));
		// Broadcasts the Intent to receivers in this app.
		LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

		Log.i(CriticalIssuesFetchService.class.getSimpleName(),
				"Broadcast with critical issues sent.");
	}

	private class NotificationIssuesListener implements Listener<JiraIssue[]> {

		@Override
		public void onResponse(JiraIssue[] issues) {
			final Set<String> criticalIssueKeys = new HashSet<>();

			for (final JiraIssue issue : issues) {
				if (issue.getSpentTimeHoursPerUpdate() > thresholdCritical) {
					criticalIssueKeys.add(issue.getKey());
				}
			}

			sendBroadcastWithCriticalIssues(criticalIssueKeys);
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
