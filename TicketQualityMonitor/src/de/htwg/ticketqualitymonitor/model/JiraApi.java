package de.htwg.ticketqualitymonitor.model;

import android.util.Log;

public class JiraApi {

	private final String URI;
	private final String USER;
	private final String PASS;

	public JiraApi(String uri, String user, String pass) {
		URI = uri;
		USER = user;
		PASS = pass;

		Log.i(JiraApi.class.getSimpleName(), "api data: " + USER + ":" + PASS
				+ "@" + URI);
	}

}
