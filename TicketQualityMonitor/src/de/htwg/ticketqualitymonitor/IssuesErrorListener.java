package de.htwg.ticketqualitymonitor;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

/**
 * Handles error while retrieving projects from the Jira API.
 */
public class IssuesErrorListener implements ErrorListener {
	private final SwipeRefreshLayout swipeRefresh;
	private final TextView errorView;

	/**
	 * @param swipeRefresh
	 *            The swipe refresh layout to disable loading on error.
	 * @param listView
	 *            The view where the error will be displayed.
	 */
	public IssuesErrorListener(SwipeRefreshLayout swipeRefresh,
			TextView errorView) {
		this.swipeRefresh = swipeRefresh;
		this.errorView = errorView;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		swipeRefresh.setRefreshing(false);
		errorView.setVisibility(View.VISIBLE);
		Log.e(IssuesErrorListener.class.getSimpleName(), error.toString());
	}

}