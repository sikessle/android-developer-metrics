package de.htwg.ticketqualitymonitor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;
import de.htwg.ticketqualitymonitor.model.JiraIssue;

/**
 * Starting activity which display a list of assigned issues and their remaining
 * costs update rate.
 */
public class MainActivity extends Activity implements
		OnSharedPreferenceChangeListener {

	private ArrayAdapter<JiraIssue> adapter;
	private JiraApi api;
	private NotificationServiceManager notificationManager;
	private static final long SERVICE_INTERVAL_MINUTES = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		api = JiraApiFactory.createInstance(this);
		setUpNotificationManager();
		connectAdapter();

		// Listen to preference changes
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	private void setUpNotificationManager() {
		notificationManager = new NotificationServiceManager(
				SERVICE_INTERVAL_MINUTES, IssuesNotificationService.class);
		notificationManager.stop(this);
	}

	private void connectAdapter() {
		adapter = new IssuesListArrayAdapter(this, new JiraIssue[] {});
		((ListView) findViewById(R.id.issuesList)).setAdapter(adapter);
	}

	@Override
	protected void onStart() {
		super.onStart();

		showProgressBarOnEmptyIssuesList();
		loadIssues();
	}

	private void showProgressBarOnEmptyIssuesList() {
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		final ListView listView = (ListView) findViewById(R.id.issuesList);
		listView.setEmptyView(progressBar);
	}

	private void loadIssues() {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		final String projectKey = prefs.getString(
				getString(R.string.key_project), "none");
		final String errorMessage = getString(R.string.issues_error);

		api.getAssignedInProgressIssuess(projectKey,
				new IssuesListener(adapter), new IssuesErrorListener(
						errorMessage));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		api = JiraApiFactory.createInstance(this);
		connectAdapter();
		loadIssues();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Start notification service if app is paused
		notificationManager.start(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		// Cancel notification service if app is active
		notificationManager.stop(this);
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

}