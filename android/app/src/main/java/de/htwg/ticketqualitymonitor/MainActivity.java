package de.htwg.ticketqualitymonitor;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;
import de.htwg.ticketqualitymonitor.model.JiraIssue;

/**
 * Starting activity which display a list of assigned issues and their remaining
 * costs update rate.
 */
public class MainActivity extends Activity implements OnRefreshListener {

	private ArrayAdapter<JiraIssue> listAdapter;
	private SwipeRefreshLayout swipeRefresh;
	private ListView issuesList;
	private Handler refreshHandler;
	private Runnable issuesLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeRefresh.setOnRefreshListener(this);
		listAdapter = new IssuesListArrayAdapter(this, new JiraIssue[] {});
		issuesList = ((ListView) findViewById(R.id.issuesList));
		issuesList.setOnScrollListener(new DisableRefreshOnNormalScrolling());
		issuesList.setAdapter(listAdapter);
	}

	@Override
	protected void onPause() {
		Log.i(MainActivity.class.getSimpleName(), "Pausing.");
		super.onPause();
		stopAutoRefresh();
	}

	@Override
	protected void onResume() {
		Log.i(MainActivity.class.getSimpleName(), "Resuming.");
		super.onRestart();
		startAutoRefresh();
	}

	@Override
	public void onRefresh() {
		loadIssues();
	}

	private void startAutoRefresh() {
		final int interval = getAutoRefreshInterval();
		refreshHandler = new Handler();
		issuesLoader = new Runnable() {
			@Override
			public void run() {
				loadIssues();
				refreshHandler.postDelayed(issuesLoader,
						TimeUnit.MINUTES.toMillis(interval));
			}
		};
		refreshHandler.post(issuesLoader);
	}

	private void stopAutoRefresh() {
		refreshHandler.removeCallbacks(issuesLoader);
	}

	private int getAutoRefreshInterval() {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return Integer.parseInt(prefs.getString(
				getString(R.string.key_issues_refresh_rate), "1"));
	}

	private void loadIssues() {
		final JiraApi api = JiraApiFactory.getInstance(this);
		final TextView errorView = (TextView) findViewById(R.id.issuesError);
		final IssuesListener listener = new IssuesListener(listAdapter,
				swipeRefresh);
		final IssuesErrorListener errorListener = new IssuesErrorListener(
				swipeRefresh, errorView);

		showLoadingIndicator();

		errorView.setVisibility(View.GONE);
		api.getAssignedIssuess(getProjectKey(), listener,
				errorListener);
	}

	private void showLoadingIndicator() {
		swipeRefresh.post(new Runnable() {
			@Override
			public void run() {
				swipeRefresh.setRefreshing(true);
			}
		});
	}

	private String getProjectKey() {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getString(getString(R.string.key_project), "none");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here.
		final int id = item.getItemId();
		if (id == R.id.settingsAction) {
			final Intent settingsIntent = new Intent(this,
					MainPreferenceActivity.class);
			startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Disables refreshing if the user scrolls up normally (and not pulling down
	 * to refresh).
	 */
	private class DisableRefreshOnNormalScrolling implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			final int topRowVerticalPosition = (issuesList == null || issuesList
					.getChildCount() == 0) ? 0 : issuesList.getChildAt(0)
					.getTop();

			swipeRefresh.setEnabled(firstVisibleItem == 0
					&& topRowVerticalPosition >= 0);
		}
	}

}