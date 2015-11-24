package de.htwg.ticketqualitymonitor;

import android.preference.ListPreference;
import android.util.Log;

import com.android.volley.Response.Listener;

import de.htwg.ticketqualitymonitor.model.JiraProject;

/**
 * Handles the successful request of projects from the Jira API.
 */
public class ProjectsListener implements Listener<JiraProject[]> {
	private final ListPreference prefList;

	/**
	 * @param prefList
	 *            A list to fill with the projects.
	 */
	public ProjectsListener(ListPreference prefList) {
		this.prefList = prefList;
	}

	@Override
	public void onResponse(JiraProject[] projects) {
		// Fill the projects into the preference list.
		final String entries[] = new String[projects.length];
		final String entryValues[] = new String[projects.length];

		for (int i = 0; i < projects.length; i++) {
			entries[i] = projects[i].getName();
			entryValues[i] = projects[i].getKey();
		}

		prefList.setEntries(entries);
		prefList.setEntryValues(entryValues);
		prefList.setSummary(prefList.getEntry());

		Log.i(ProjectsListener.class.getSimpleName(), "Projects loaded");
	}

}