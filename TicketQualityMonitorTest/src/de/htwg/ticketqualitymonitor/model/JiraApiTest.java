package de.htwg.ticketqualitymonitor.model;

import android.test.AndroidTestCase;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
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
		final String uri = "http://localhost/";
		final JiraApi api = new JiraApi(uri, USER, PASS, createQueue());

		assertEquals(uri + URI_SUFFIX, api.getUri());
		assertEquals(USER, api.getUser());
		assertEquals(PASS, api.getPass());
	}

	public void testMissingTrailingSlash() {
		final String uri = "http://localhost";
		final JiraApi api = new JiraApi(uri, USER, PASS, createQueue());

		assertEquals(uri + "/" + URI_SUFFIX, api.getUri());
	}

	public void testMissingLeadingHttp() {
		final String uri = "localhost/";
		final JiraApi api = new JiraApi(uri, USER, PASS, createQueue());

		assertEquals("http://" + uri + URI_SUFFIX, api.getUri());
	}

	private void setListenerCalled(boolean value) {
		listenerCalled = value;
	}

	public void testGetRequestWithCredentials() throws Exception {
		final String uri = "http://localhost/";
		final String resource = "res";
		final JiraApi api = new JiraApi(uri, USER, PASS, createQueue());
		final GsonRequest<String> request = api.createBaseRequest(resource,
				String.class, new Listener<String>() {
					@Override
					public void onResponse(String res) {
					}
				}, createFailFastErrorListener());

		final String userPass = (USER + ":" + PASS);
		final String expectedCredentials = "Basic "
				+ Base64.encodeToString(userPass.getBytes("UTF-8"),
						Base64.NO_WRAP);

		assertEquals(expectedCredentials,
				request.getHeaders().get("Authorization"));
		assertEquals(uri + URI_SUFFIX + resource, request.getUrl());
	}

	public void testGetRequestListener() {
		final String uri = "http://localhost/";
		final String expectedResponse = "resp";
		final JiraApi api = new JiraApi(uri, USER, PASS, createQueue());
		final GsonRequest<String> request = api.createBaseRequest("/res",
				String.class, new Listener<String>() {
					@Override
					public void onResponse(String res) {
						assertEquals(expectedResponse, res);
						setListenerCalled(true);
					}
				}, createFailFastErrorListener());

		setListenerCalled(false);
		request.deliverResponse(expectedResponse);
		assertTrue(listenerCalled);
	}

	private ErrorListener createFailFastErrorListener() {
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				fail("an error ocurred");
			}
		};
	}

}
