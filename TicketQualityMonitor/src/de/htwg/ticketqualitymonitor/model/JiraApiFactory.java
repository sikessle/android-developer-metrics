package de.htwg.ticketqualitymonitor.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.htwg.ticketqualitymonitor.R;

public class JiraApiFactory {

	public static JiraApi createInstance(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String host = prefs.getString(
				context.getString(R.string.key_jira_host), "http://localhost/");
		String user = prefs.getString(
				context.getString(R.string.key_jira_username), "admin");
		String pass = prefs.getString(
				context.getString(R.string.key_jira_password), "admin");

		return new JiraApi(host, user, pass,
				RequestQueueSingleton.init(context));
	}

}
