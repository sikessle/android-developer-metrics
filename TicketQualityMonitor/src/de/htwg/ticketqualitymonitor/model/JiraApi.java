package de.htwg.ticketqualitymonitor.model;

import org.json.JSONObject;

import android.util.Log;

public class JiraApi {

	private final String URI;
	private final String USER;
	private final String PASS;

	private static final String PROJECTS = "";

	public JiraApi(String uri, String user, String pass) {
		URI = sanitizeUri(uri);
		USER = user;
		PASS = pass;

		Log.i(JiraApi.class.getSimpleName(), "api data: " + USER + ":" + PASS
				+ "@" + URI);
	}

	public String getUri() {
		return URI;
	}

	public String getUser() {
		return USER;
	}

	public String getPass() {
		return PASS;
	}

	private String sanitizeUri(String possibleUri) {
		final StringBuilder uri = new StringBuilder(possibleUri);
		final String prefix = "http://";
		final String suffix = "/";

		if (!possibleUri.startsWith(prefix)) {
			uri.insert(0, prefix);
		}

		if (!possibleUri.endsWith(suffix)) {
			uri.append(suffix);
		}

		return uri.toString();
	}

}
