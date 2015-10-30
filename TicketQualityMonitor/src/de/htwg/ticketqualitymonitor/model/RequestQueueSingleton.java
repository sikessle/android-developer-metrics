package de.htwg.ticketqualitymonitor.model;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

public class RequestQueueSingleton {
	private static RequestQueue INSTANCE;

	public static RequestQueue init(Context context) {
		if (INSTANCE == null) {
			INSTANCE = Volley.newRequestQueue(context);
		}
		return INSTANCE;
	}

	public static RequestQueue getInstance() {
		if (INSTANCE == null) {
			throw new IllegalStateException("Call init(Context context) first");
		}
		return INSTANCE;
	}

}
