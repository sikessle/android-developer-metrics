package de.htwg.ticketqualitymonitor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import de.htwg.ticketqualitymonitor.model.JiraApi;

public class MainActivity extends Activity implements
OnSharedPreferenceChangeListener {

	private JiraApi api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Listen to preference changes
		PreferenceManager.getDefaultSharedPreferences(this)
		.registerOnSharedPreferenceChangeListener(this);
		initApiFromPrefs();
	}

	private void initApiFromPrefs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String host = prefs.getString("jira_host", "http://localhost/");
		String user = prefs.getString("jira_username", "admin");
		String pass = prefs.getString("jira_password", "admin");

		api = new JiraApi(host, user, pass, this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		initApiFromPrefs();
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this,
					MainPreferenceActivity.class);
			startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}