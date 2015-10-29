package de.htwg.ticketqualitymonitor.model;

import android.test.AndroidTestCase;

public class JiraApiTest extends AndroidTestCase {

	public void testValidConstructorArgs() {
		String uri = "http://localhost/";
		String user = "user";
		String pass = "password";
		JiraApi api = new JiraApi(uri, user, pass);

		assertEquals(uri, api.getUri());
		assertEquals(user, api.getUser());
		assertEquals(pass, api.getPass());
	}

	public void testMissingTrailingSlash() {
		// missing trailing slash
		String uri = "http://localhost";
		String user = "user";
		String pass = "password";
		JiraApi api = new JiraApi(uri, user, pass);

		assertEquals(uri + "/", api.getUri());
	}

	public void testMissingLeadingHttp() {
		// missing leading http://
		String uri = "localhost/";
		String user = "user";
		String pass = "password";
		JiraApi api = new JiraApi(uri, user, pass);

		assertEquals("http://" + uri, api.getUri());
	}

}
