package de.htwg.ticketqualitymonitor.model;

import java.util.ArrayList;
import java.util.List;

public class JiraApi {

	private static final String URI_API_SUFFIX = "rest/api/2/";
	private static final String PROJECTS = "project";

	private final String uri;
	private final String user;
	private final String pass;

	public JiraApi(String uri, String user, String pass) {
		this.uri = sanitizeUri(uri);
		this.user = user;
		this.pass = pass;
	}

	private String sanitizeUri(String possibleUri) {
		final StringBuilder result = new StringBuilder(possibleUri);
		final String prefix = "http://";
		final String suffix = "/";

		if (!possibleUri.startsWith(prefix)) {
			result.insert(0, prefix);
		}

		if (!possibleUri.endsWith(suffix)) {
			result.append(suffix);
		}

		result.append(URI_API_SUFFIX);
		return result.toString();
	}

	public List<JiraProject> getProjects() {
		List<JiraProject> result = new ArrayList<JiraProject>();
		return result;
	}

	public String getUri() {
		return uri;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

}
