package de.htwg.ticketqualitymonitor;

import android.preference.ListPreference;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;

public class ProjectsErrorListener implements ErrorListener {
	private ListPreference prefList;
	private String errorMessage;

	public ProjectsErrorListener(ListPreference prefList, String errorMessage) {
		this.prefList = prefList;
		this.errorMessage = errorMessage;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		prefList.setSummary(errorMessage);
		prefList.setEntries(new String[] {});
		prefList.setEntryValues(new String[] {});
		// prefList.setOnPreferenceClickListener(onPreferenceClickListener);
	}

}