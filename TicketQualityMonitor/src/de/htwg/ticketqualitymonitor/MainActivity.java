package de.htwg.ticketqualitymonitor;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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

	private ArrayAdapter<JiraIssue> adapter;
	private SwipeRefreshLayout swipeRefresh;
	private JiraApi api;
	private NotificationServiceManager notificationManager;
	private ListView issuesList;
	private Handler refreshHandler;
	private Runnable loadIssuesRunnable;
	private boolean enableNotifications;
	private boolean autoRefreshCancelled;
	private Set<String> notifiedCriticalIssueKeys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		issuesList = ((ListView) findViewById(R.id.issuesList));

		setupPullToRefresh();
	}

	@Override
	protected void onPause() {
		super.onPause();

		notifiedCriticalIssueKeys = notificationManager.getCriticalIssueKeys();
		stopAutoRefresh();
		// Start notification service if app is paused
		if (enableNotifications) {
			notificationManager.start(this);
		}
	}

	@Override
	protected void onResume() {
		super.onRestart();

		api = JiraApiFactory.createInstance(this);
		connectListAdapter();
		setUpNotificationManager();
		startAutoRefresh();
		// Cancel notification service if app is active
		notificationManager.stop(this);
	}

	private void startAutoRefresh() {
		autoRefreshCancelled = false;
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final int interval = Integer.parseInt(prefs.getString(
				getString(R.string.key_issues_refresh_rate), "1"));
		refreshHandler = new Handler();
		loadIssuesRunnable = new Runnable() {
			@Override
			public void run() {
				loadIssues();
				if (!autoRefreshCancelled) {
					refreshHandler.postDelayed(loadIssuesRunnable,
							TimeUnit.MINUTES.toMillis(interval));
				}
			}
		};
		refreshHandler.post(loadIssuesRunnable);
	}

	private void stopAutoRefresh() {
		autoRefreshCancelled = true;
	}

	private void setupPullToRefresh() {
		swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeRefresh.setOnRefreshListener(this);
		issuesList.setOnScrollListener(new DisableRefreshOnNormalScrolling());
	}

	private void setUpNotificationManager() {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final int delay = Integer.parseInt(prefs.getString(
				getString(R.string.key_notifications_interval), "1"));
		notificationManager = new NotificationServiceManager(delay,
				CriticalIssuesFetchService.class);
		if (notifiedCriticalIssueKeys != null) {
			notificationManager.setCriticalIssueKeys(notifiedCriticalIssueKeys);
		}
		enableNotifications = prefs.getBoolean(
				getString(R.string.key_enable_notifications), false);
	}

	private void connectListAdapter() {
		adapter = new IssuesListArrayAdapter(this, new JiraIssue[] {});
		issuesList.setAdapter(adapter);
	}

	private void loadIssues() {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		final String projectKey = prefs.getString(
				getString(R.string.key_project), "none");
		final TextView errorView = (TextView) findViewById(R.id.issuesError);
		if (!swipeRefresh.isRefreshing()) {
			swipeRefresh.post(new Runnable() {
				@Override
				public void run() {
					swipeRefresh.setRefreshing(true);
				}
			});
		}

		errorView.setVisibility(View.GONE);
		api.getAssignedInProgressIssuess(projectKey, new IssuesListener(
				adapter, swipeRefresh), new IssuesErrorListener(swipeRefresh,
				errorView));
	}

	@Override
	public void onRefresh() {
		loadIssues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();
		if (id == R.id.settingsAction) {
			final Intent settingsIntent = new Intent(this,
					MainPreferenceActivity.class);
			startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

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