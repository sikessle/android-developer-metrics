package de.htwg.ticketqualitymonitor.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class JiraApi {

	private static final String URI_API_SUFFIX = "rest/api/2/";
	private static final String PROJECTS = "project";

	private final String uri;
	private final String user;
	private final String pass;
	private final RequestQueue requestQueue;
	private final ErrorListener errorListener;

	public JiraApi(String uri, String user, String pass, Context context) {
		this.uri = sanitizeUri(uri);
		this.user = user;
		this.pass = pass;
		this.requestQueue = Volley.newRequestQueue(context);
		errorListener = new MyErrorListener();
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

	public void getProjects(Listener<JiraProject[]> listener) {
		GsonRequest<JiraProject[]> req = new GsonRequest<JiraProject[]>(uri
				+ PROJECTS, JiraProject[].class, listener, errorListener);
		requestQueue.add(req);
	}

	private static class MyErrorListener implements Response.ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(JiraApi.class.getSimpleName(), error.getMessage());
		}

	}
}
