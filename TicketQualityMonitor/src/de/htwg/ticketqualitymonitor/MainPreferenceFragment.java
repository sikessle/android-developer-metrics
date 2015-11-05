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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.getDefaultSharedPreferences(getActivity())
				.registerOnSharedPreferenceChangeListener(this);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		initProjectList();
	}

	private void initProjectList() {
		final String keyProjectList = getString(R.string.key_project);
		final String errorMessage = getString(R.string.preference_error_projects);
		final ListPreference prefList = (ListPreference) findPreference(keyProjectList);
		final JiraApi api = JiraApiFactory.getInstance(getActivity());
		// Shows loading indicator text
		prefList.setSummary(getString(R.string.loading));
		// Will be set by the error listener if an error occurs, so it can be
		// handled in this class by allowing the user to reload the list by
		// clicking on the projects preference list.
		prefList.setOnPreferenceClickListener(null);

		api.getProjects(new ProjectsListener(prefList),
				new ProjectsErrorListener(this, prefList, errorMessage));
	}

	@Override
	public void onResume() {
		super.onResume();
		getPrefs().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPrefs().unregisterOnSharedPreferenceChangeListener(this);
	}

	private SharedPreferences getPrefs() {
		return PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		final String keyUri = getString(R.string.key_jira_host);
		final String keyUser = getString(R.string.key_jira_username);
		final String keyPass = getString(R.string.key_jira_password);
		final String keyProject = getString(R.string.key_project);
		final String keyThresholdGreen = getString(R.string.key_color_threshold_green);
		final String keyThresholdYellow = getString(R.string.key_color_threshold_yellow);
		final String keyEnableNotifications = getString(R.string.key_enable_notifications);

		if (keyUri.equals(key) || keyUser.equals(key) || keyPass.equals(key)) {
			initProjectList();
		}

		if (keyThresholdGreen.equals(key) || keyThresholdYellow.equals(key)
				|| keyProject.equals(key)) {
			ViewedIssuesHandler.clearSeenIssues(getActivity());
		}

		if (keyEnableNotifications.equals(key)) {
			NotificationServiceManager
					.startOrStopBasedOnPreference(getActivity());
		}

		if (keyProject.equals(key)) {
			final Preference pref = findPreference(key);
			final ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry());
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// Kill the dialog and reload the projects (retry of failed load)
		((ListPreference) preference).getDialog().dismiss();
		initProjectList();
		return false;
	}

}