package de.htwg.ticketqualitymonitor.model;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;

public class JiraApi {

	private static final String URI_API_SUFFIX = "rest/api/2/";
	private static final String PROJECTS = "project";
	private static final int TIMEOUT_MS = 4000;
	private static final int MAX_RETRIES = 0;

	private final String uri;
	private final String user;
	private final String pass;
	private final RequestQueue requestQueue;
	private final Map<String, String> credentials;
	private RetryPolicy retryPolicy;

	public JiraApi(String uri, String user, String pass,
			RequestQueue requestQueue) {
		this.uri = sanitizeUri(uri);
		this.user = user;
		this.pass = pass;
		this.requestQueue = requestQueue;
		credentials = new HashMap<String, String>();
		initBasicAuthHeader();
		retryPolicy = new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

		Log.i(JiraApi.class.getSimpleName(), "Connection data: " + this.user
				+ ":" + this.pass + "@" + this.uri);
	}

	private void initBasicAuthHeader() {
		String userPass = (user + ":" + pass);
		String userPassBase64;
		try {
			userPassBase64 = Base64.encodeToString(userPass.getBytes("UTF-8"),
					Base64.NO_WRAP);
		} catch (UnsupportedEncodingException e) {
			Log.e(JiraApi.class.getSimpleName(), e.getMessage());
			throw new RuntimeException(e);
		}
		credentials.put("Authorization", "Basic " + userPassBase64);
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

	public String getUri() {
		return uri;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	/**
	 * @param resource
	 *            The resource to aquire: i.e. "project". The base uri will be
	 *            prepended.
	 */
	protected <T> GsonRequest<T> createBaseRequest(String resource,
			Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		Map<String, String> headers = new HashMap<String, String>(credentials);
		GsonRequest<T> request = new GsonRequest<T>(uri + resource, clazz,
				headers, listener, errorListener);
		request.setRetryPolicy(retryPolicy);

		Log.i(JiraApi.class.getSimpleName(), "Request created");

		return request;
	}

	public void getProjects(Listener<JiraProject[]> listener,
			ErrorListener errorListener) {
		GsonRequest<JiraProject[]> req = createBaseRequest(PROJECTS,
				JiraProject[].class, listener, errorListener);
		requestQueue.add(req);
	}
}
