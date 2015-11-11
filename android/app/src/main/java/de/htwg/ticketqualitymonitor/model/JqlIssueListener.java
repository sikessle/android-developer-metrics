package de.htwg.ticketqualitymonitor.model;

import com.android.volley.Response.Listener;

/**
 * Listens for JQL responses.
 */
public class JqlIssueListener implements Listener<JqlObject> {

	private final Listener<JiraIssue[]> issueListener;

	/**
	 * @param issueListener
	 *            Listener which handles the issues.
	 */
	public JqlIssueListener(Listener<JiraIssue[]> issueListener) {
		this.issueListener = issueListener;
	}

	@Override
	public void onResponse(JqlObject jql) {
		issueListener.onResponse(jql.getIssues());
	}

}