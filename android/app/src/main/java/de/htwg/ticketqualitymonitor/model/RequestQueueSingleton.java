package de.htwg.ticketqualitymonitor.model;

import net.jcip.annotations.ThreadSafe;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

/**
 * Singleton to create a request queue and share it among the application parts.
 * This improves performance by reusing a {@link RequestQueue}
 */
@ThreadSafe
public class RequestQueueSingleton {
	private static RequestQueue INSTANCE;

	/**
	 * Creates if not already created a new request queue.
	 *
	 * @param context
	 *            The context at which the request queue will be created.
	 * @return A new or existing request queue.
	 */
	synchronized public static RequestQueue init(Context context) {
		if (INSTANCE == null) {
			INSTANCE = Volley.newRequestQueue(context);
		}
		return INSTANCE;
	}

	/**
	 * Returns an existing request queue.
	 *
	 * @return The existing request queue.
	 * @throws IllegalStateException
	 *             If no instance was created before by calling init().
	 */
	synchronized public static RequestQueue getInstance()
			throws IllegalStateException {
		if (INSTANCE == null) {
			throw new IllegalStateException("Call init(Context context) first");
		}
		return INSTANCE;
	}

}
