package de.htwg.ticketqualitymonitor.model;

import java.util.concurrent.CountDownLatch;

import android.test.AndroidTestCase;

import com.android.volley.Response.Listener;

public class JiraApiTest extends AndroidTestCase {

	private static final String URI = "http://metaproject.in.fhkn.de:8080/";
	private static final String URI_SUFFIX = "rest/api/2/";
	private static final String USER = "sikessle";
	private static final String PASS = "sikessle";

	private JiraApi api;
	private CountDownLatch latch;

	@Override
	public void setUp() throws Exception {
		api = new JiraApi(URI, USER, PASS, getContext());
		latch = new CountDownLatch(1);
	}

	public void testValidConstructorArgs() {
		String uri = "http://localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS, getContext());

		assertEquals(uri + URI_SUFFIX, api.getUri());
		assertEquals(USER, api.getUser());
		assertEquals(PASS, api.getPass());
	}

	public void testMissingTrailingSlash() {
		String uri = "http://localhost";
		JiraApi api = new JiraApi(uri, USER, PASS, getContext());

		assertEquals(uri + "/" + URI_SUFFIX, api.getUri());
	}

	public void testMissingLeadingHttp() {
		String uri = "localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS, getContext());

		assertEquals("http://" + uri + URI_SUFFIX, api.getUri());
	}

	public void testGetProjects() throws InterruptedException {
		api.getProjects(new Listener<JiraProject[]>() {
			@Override
			public void onResponse(JiraProject[] projects) {
				System.out.println(projects[0].getKey());
				latch.countDown();
			}
		});
		latch.await();
	}
}
