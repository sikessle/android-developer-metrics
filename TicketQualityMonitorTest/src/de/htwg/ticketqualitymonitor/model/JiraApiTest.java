package de.htwg.ticketqualitymonitor.model;

import android.test.AndroidTestCase;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;

public class JiraApiTest extends AndroidTestCase {

	private static final String URI_SUFFIX = "rest/api/2/";
	private static final String USER = "user";
	private static final String PASS = "pass";
	private boolean listenerCalled;

	private RequestQueue createQueue() {
		return Volley.newRequestQueue(getContext());
	}

	public void testValidConstructorArgs() {
		String uri = "http://localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS, createQueue());

		assertEquals(uri + URI_SUFFIX, api.getUri());
		assertEquals(USER, api.getUser());
		assertEquals(PASS, api.getPass());
	}

	public void testMissingTrailingSlash() {
		String uri = "http://localhost";
		JiraApi api = new JiraApi(uri, USER, PASS, createQueue());

		assertEquals(uri + "/" + URI_SUFFIX, api.getUri());
	}

	public void testMissingLeadingHttp() {
		String uri = "localhost/";
		JiraApi api = new JiraApi(uri, USER, PASS, createQueue());

		assertEquals("http://" + uri + URI_SUFFIX, api.getUri());
	}

	private void setListenerCalled(boolean value) {
		listenerCalled = value;
	}

	public void testGetRequestWithCredentials() throws Exception {
		String uri = "http://localhost/";
		String resource = "res";
		JiraApi api = new JiraApi(uri, USER, PASS, createQueue());
		GsonRequest<String> request = api.getRequestWithCredentials(resource,
				String.class, new Listener<String>() {
			@Override
			public void onResponse(String res) {
			}
		});

		String userPass = (USER + ":" + PASS);
		String expectedCredentials = "Basic "
				+ Base64.encodeToString(userPass.getBytes("UTF-8"),
						Base64.NO_WRAP);

		assertEquals(expectedCredentials,
				request.getHeaders().get("Authorization"));
		assertEquals(uri + URI_SUFFIX + resource, request.getUrl());
	}

	public void testGetRequestListener() {
		String uri = "http://localhost/";
		final String expectedResponse = "resp";
		JiraApi api = new JiraApi(uri, USER, PASS, createQueue());
		GsonRequest<String> request = api.getRequestWithCredentials("/res",
				String.class, new Listener<String>() {
			@Override
			public void onResponse(String res) {
				assertEquals(expectedResponse, res);
				setListenerCalled(true);
			}
		});

		setListenerCalled(false);
		request.deliverResponse(expectedResponse);
		assertTrue(listenerCalled);
	}

}
