package de.htwg.ticketqualitymonitor;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import de.htwg.ticketqualitymonitor.model.JiraApi;
import de.htwg.ticketqualitymonitor.model.JiraApiFactory;

/**
 * This fragment shows the preferences for the first header.
 */
public class MainPreferenceFragment extends PreferenceFragment implements
OnSharedPreferenceChangeListener, OnPreferenceClickListener {

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
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		api = JiraApiFactory.createInstance(getActivity());
		initProjectList();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		((ListPreference) preference).getDialog().dismiss();
		initProjectList();
		return false;
	}

	private void initProjectList() {
		final String keyProjectList = getString(R.string.key_project);
		final String errorMessage = getString(R.string.preference_error_projects);
		final ListPreference prefList = (ListPreference) findPreference(keyProjectList);
		// Shows loading indicator
		prefList.setSummary(getString(R.string.loading));
		// Will be set by the error listener if an error occurs, so it can be
		// handled in this class by allowing the user to reload the list by
		// clicking on the projects preference list.
		prefList.setOnPreferenceClickListener(null);

		api.getProjects(new ProjectsListener(prefList),
				new ProjectsErrorListener(this, prefList, errorMessage));
	}

}