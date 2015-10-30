package de.htwg.ticketqualitymonitor.model;

import android.test.AndroidTestCase;

public class JiraApiTest extends AndroidTestCase {

	private static final String URI = "http://metaproject.in.fhkn.de:8080/";
	private static final String URI_SUFFIX = "rest/api/2/";
	private static final String USER = "sikessle";
	private static final String PASS = "sikessle";

	public void testValidConstructorArgs() {
		String uri = "http://localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals(uri + URI_SUFFIX, api.getUri());
		assertEquals(USER, api.getUser());
		assertEquals(PASS, api.getPass());
	}

	public void testMissingTrailingSlash() {
		// missing trailing slash
		String uri = "http://localhost";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals(uri + "/" + URI_SUFFIX, api.getUri());
	}

	public void testMissingLeadingHttp() {
		// missing leading http://
		String uri = "localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals("http://" + uri + URI_SUFFIX, api.getUri());
	}

}
