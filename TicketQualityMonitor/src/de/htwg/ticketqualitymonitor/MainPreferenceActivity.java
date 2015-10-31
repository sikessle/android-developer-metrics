package de.htwg.ticketqualitymonitor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraProject;

public class MainPreferenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new MainFragment()).commit();
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class MainFragment extends PreferenceFragment implements
	OnSharedPreferenceChangeListener {

		private JiraApi api;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Listen to preference changes
			PreferenceManager.getDefaultSharedPreferences(getActivity())
			.registerOnSharedPreferenceChangeListener(this);
			api = JiraApiFactory.createInstance(getActivity());

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);

			initProjectList();
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			api = JiraApiFactory.createInstance(getActivity());
			initProjectList();
		}

		private void initProjectList() {
			String keyProjectList = getString(R.string.key_project);
			final ListPreference prefList = (ListPreference) findPreference(keyProjectList);
			api.getProjects(new ProjectsListener(prefList),
					new ProjectsErrorListener());
		}

	}

	private static class ProjectsListener implements Listener<JiraProject[]> {

		private ListPreference prefList;

		public ProjectsListener(ListPreference prefList) {
			this.prefList = prefList;
		}

		@Override
		public void onResponse(JiraProject[] projects) {
			String entries[] = new String[projects.length];
			String entryValues[] = new String[projects.length];

			for (int i = 0; i < projects.length; i++) {
				entries[i] = projects[i].getName();
				entryValues[i] = projects[i].getKey();
			}

			prefList.setEntries(entries);
			prefList.setEntryValues(entryValues);
			prefList.setSummary(prefList.getEntry());

			Log.i(MainPreferenceActivity.class.getSimpleName(),
					"Projects loaded");
		}

	}

	private static class ProjectsErrorListener implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e("Preferences Invalid:", error.toString());
		}

	}

}