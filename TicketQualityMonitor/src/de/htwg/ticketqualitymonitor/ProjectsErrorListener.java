package de.htwg.ticketqualitymonitor;

import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceClickListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

public class ProjectsErrorListener implements ErrorListener {
	private ListPreference prefList;
	private String errorMessage;
	private OnPreferenceClickListener errorClickListener;

	public ProjectsErrorListener(OnPreferenceClickListener errorClickListener,
			ListPreference prefList, String errorMessage) {
		this.errorClickListener = errorClickListener;
		this.prefList = prefList;
		this.errorMessage = errorMessage;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		prefList.setSummary(errorMessage);
		prefList.setEntries(new String[] {});
		prefList.setEntryValues(new String[] {});
		prefList.setOnPreferenceClickListener(errorClickListener);
	}

}