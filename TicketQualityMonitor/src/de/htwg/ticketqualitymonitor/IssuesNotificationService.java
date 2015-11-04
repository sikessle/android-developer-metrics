package de.htwg.ticketqualitymonitor;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;
import de.htwg.ticketqualitymonitor.model.JiraIssue;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
		Log.i(IssuesNotificationService.class.getSimpleName(), "onHandleIntent called.");

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
		// TODO notify..
		// api.getAssignedInProgressIssuess(projectKey, issuesListener, errorListener);
	}

}
