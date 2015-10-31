package de.htwg.ticketqualitymonitor;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import de.htwg.ticketqualitymonitor.model.JiraApi;

/**
 * This fragment shows the preferences for the first header.
 */
public class MainPreferenceFragment extends PreferenceFragment
		implements OnSharedPreferenceChangeListener {

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
		final String keyProjectList = getString(R.string.key_project);
		final String errorMessage = getString(R.string.preference_error_projects);
		final ListPreference prefList = (ListPreference) findPreference(keyProjectList);
		// Shows loading indicator
		prefList.setSummary(getString(R.string.loading));

		api.getProjects(new ProjectsListener(prefList),
				new ProjectsErrorListener(prefList, errorMessage));
	}

}