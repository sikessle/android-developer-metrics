package de.htwg.ticketqualitymonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.RequestQueueSingleton;

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
