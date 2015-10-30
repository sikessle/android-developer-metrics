package de.htwg.ticketqualitymonitor.model;

import java.util.List;

import android.test.AndroidTestCase;

public class JiraApiTest extends AndroidTestCase {

	private static final String URI = "http://metaproject.in.fhkn.de:8080/";
	private static final String URI_SUFFIX = "rest/api/2/";
	private static final String USER = "sikessle";
	private static final String PASS = "sikessle";

	private JiraApi api;

	@Override
	public void setUp() throws Exception {
		api = new JiraApi(URI, USER, PASS);
	}

	public void testValidConstructorArgs() {
		String uri = "http://localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals(uri + URI_SUFFIX, api.getUri());
		assertEquals(USER, api.getUser());
		assertEquals(PASS, api.getPass());
	}

	public void testMissingTrailingSlash() {
		String uri = "http://localhost";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals(uri + "/" + URI_SUFFIX, api.getUri());
	}

	public void testMissingLeadingHttp() {
		String uri = "localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals("http://" + uri + URI_SUFFIX, api.getUri());
	}

	public void testGetProjects() {
		List<JiraProject> projects = api.getProjects();
		assertEquals(1, projects.size());
		assertEquals("TEST", projects.get(0).getKey());
	}

}
