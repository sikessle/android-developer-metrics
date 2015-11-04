package de.htwg.ticketqualitymonitor;

import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class IssuesService extends IntentService {

	public static final String INTENT_KEY_PROJECT = "projectKey";

	private final JiraApi api;

	public IssuesService() {
		super("Jira Issues Service");
		api = JiraApiFactory.createInstance(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final String projectKey = intent.getExtras().getString(
				INTENT_KEY_PROJECT);

		Log.i(IssuesService.class.getSimpleName(),
				"onHandleIntent called with project key: " + projectKey);
	}

}
