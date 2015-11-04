package de.htwg.ticketqualitymonitor;

import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class IssuesService extends IntentService {

	public static final String INTENT_KEY_PROJECT = "projectKey";

	private final JiraApi api;
	private final String projectKey;

	public IssuesService() {
		super("Jira Issues Service");
		api = JiraApiFactory.createInstance(this);
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		projectKey = prefs.getString(getString(R.string.key_project),
				"none");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(IssuesService.class.getSimpleName(),
				"onHandleIntent called.");
	}

}
