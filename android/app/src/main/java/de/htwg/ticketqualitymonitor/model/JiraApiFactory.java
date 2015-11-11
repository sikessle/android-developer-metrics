package de.htwg.ticketqualitymonitor.model;

import net.jcip.annotations.Immutable;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.htwg.ticketqualitymonitor.R;

@Immutable
public class JiraApiFactory {

	/**
	 * Creates a new instance of the {@link JiraApi} with access data from the
	 * default shared preferences.
	 *
	 * @param context
	 *            The context from which the default shared preferences will be
	 *            retrieved.
	 * @return A new {@link JiraApi} object.
	 */
	public static JiraApi getInstance(Context context) {
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
