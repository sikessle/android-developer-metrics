package de.htwg.ticketqualitymonitor.model;

import android.test.AndroidTestCase;

public class JiraApiTest extends AndroidTestCase {

	private static final String USER = "user";
	private static final String PASS = "password";

	public void testValidConstructorArgs() {
		String uri = "http://localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals(uri, api.getUri());
		assertEquals(USER, api.getUser());
		assertEquals(PASS, api.getPass());
	}

	public void testMissingTrailingSlash() {
		// missing trailing slash
		String uri = "http://localhost";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals(uri + "/", api.getUri());
	}

	public void testMissingLeadingHttp() {
		// missing leading http://
		String uri = "localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS);

		assertEquals("http://" + uri, api.getUri());
	}

}
