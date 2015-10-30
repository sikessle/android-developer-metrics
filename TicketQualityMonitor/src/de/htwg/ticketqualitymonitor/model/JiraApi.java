package de.htwg.ticketqualitymonitor.model;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class JiraApi {

	private static final String URI_API_SUFFIX = "rest/api/2/";
	private static final String PROJECTS = "project";

	private final String uri;
	private final String user;
	private final String pass;
	private final RequestQueue requestQueue;
	private ErrorListener errorListener;
	private final Map<String, String> credentials;

	public JiraApi(String uri, String user, String pass,
			RequestQueue requestQueue) {
		this.uri = sanitizeUri(uri);
		this.user = user;
		this.pass = pass;
		this.requestQueue = requestQueue;
		errorListener = new LogErrorListener();
		credentials = new HashMap<String, String>();
		initCredentialsHeaders();
	}

	public void setErrorListener(ErrorListener listener) {
		errorListener = listener != null ? listener : errorListener;
	}

	private void initCredentialsHeaders() {
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
	protected <T> GsonRequest<T> getRequestWithCredentials(String resource,
			Class<T> clazz, Listener<T> listener) {
		Map<String, String> headers = new HashMap<String, String>(credentials);
		return new GsonRequest<T>(uri + resource, clazz, headers, listener,
				errorListener);
	}

	public void getProjects(Listener<JiraProject[]> listener) {
		GsonRequest<JiraProject[]> req = getRequestWithCredentials(PROJECTS,
				JiraProject[].class, listener);
		requestQueue.add(req);
	}

	private static class LogErrorListener implements Response.ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(JiraApi.class.getSimpleName(), error.getMessage());
		}

	}
}
