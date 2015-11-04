package de.htwg.ticketqualitymonitor;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Response.Listener;

import de.htwg.ticketqualitymonitor.model.JiraIssue;

/**
 * Handles the successful request of issues from the Jira API.
 */
public class IssuesListener implements Listener<JiraIssue[]> {
	private final ArrayAdapter<? super JiraIssue> adapter;

	/**
	 * @param adapter
	 *            An adapter to fill with the projects.
	 */
	public IssuesListener(ArrayAdapter<? super JiraIssue> adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onResponse(JiraIssue[] projects) {
		adapter.clear();
		adapter.addAll(projects);

		Log.i(IssuesListener.class.getSimpleName(), "Issues loaded");
	}

}