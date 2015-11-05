package de.htwg.ticketqualitymonitor;

import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Response.Listener;

import de.htwg.ticketqualitymonitor.model.JiraIssue;

/**
 * Handles the successful request of issues from the Jira API.
 */
public class IssuesListener implements Listener<JiraIssue[]> {
	private final ArrayAdapter<? super JiraIssue> adapter;
	private final SwipeRefreshLayout swipeRefresh;
	private final double thresholdGreen;
	private final double thresholdYellow;

	/**
	 * @param adapter
	 *            An adapter to fill with the projects.
	 */
	public IssuesListener(ArrayAdapter<? super JiraIssue> adapter,
			SwipeRefreshLayout swipeRefresh) {
		this.adapter = adapter;
		this.swipeRefresh = swipeRefresh;

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(adapter.getContext());
		thresholdGreen = Double.parseDouble(prefs.getString(adapter
				.getContext().getString(R.string.key_color_threshold_green),
				"2.0"));
		thresholdYellow = Double.parseDouble(prefs.getString(adapter
				.getContext().getString(R.string.key_color_threshold_yellow),
				"2.0"));
	}

	@Override
	public void onResponse(JiraIssue[] issues) {
		adapter.clear();
		adapter.addAll(issues);
		swipeRefresh.setRefreshing(false);

		final Editor store = adapter
				.getContext()
				.getSharedPreferences(
						NotificationServiceManager.VIEWED_ISSUE_KEYS, 0).edit();

		final Set<String> relevantIssues = ViewedIssuesHandler
				.getRelevantUniqueIssueIdents(issues, thresholdGreen, thresholdYellow);

		for (final String issueIdent : relevantIssues) {
			store.putString(issueIdent, ".");
		}
		store.apply();

		Log.i(IssuesListener.class.getSimpleName(), "Issues loaded");
	}

}