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
 * This fragment shows the main preferences and handles the interaction with
 * items.
 */
public class MainPreferenceFragment extends PreferenceFragment implements
		OnPreferenceClickListener, OnSharedPreferenceChangeListener {

	private JiraApi api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.getDefaultSharedPreferences(getActivity())
				.registerOnSharedPreferenceChangeListener(this);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		init();
	}

	@Override
	public void onResume() {
		super.onResume();

		PreferenceManager.getDefaultSharedPreferences(getActivity())
				.registerOnSharedPreferenceChangeListener(this);

		init();
	}

	@Override
	public void onPause() {
		super.onPause();

		PreferenceManager.getDefaultSharedPreferences(getActivity())
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	private void init() {
		api = JiraApiFactory.createInstance(getActivity());
		initProjectList();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		init();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// Kill the dialog and reload the projects (retry of failed load)
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