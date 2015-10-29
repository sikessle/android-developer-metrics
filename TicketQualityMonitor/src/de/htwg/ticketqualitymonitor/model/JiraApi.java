package de.htwg.ticketqualitymonitor.model;

public class JiraApi {

	private final String URI;
	private final String USER;
	private final String PASS;

	public JiraApi(String uri, String user, String pass) {
		URI = uri;
		USER = user;
		PASS = pass;
	}

}
