package de.htwg.ticketqualitymonitor;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

/**
 * Handles error while retrieving projects from the Jira API.
 */
public class IssuesErrorListener implements ErrorListener {
	private final String errorMessage;

	/**
	 * @param
	 */
	public IssuesErrorListener(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public void onErrorResponse(VolleyError error) {

		Log.e(IssuesErrorListener.class.getSimpleName(), error.toString());
	}

}