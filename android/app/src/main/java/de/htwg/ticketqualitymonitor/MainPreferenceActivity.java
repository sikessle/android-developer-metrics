package de.htwg.ticketqualitymonitor;

import android.app.Activity;
import android.os.Bundle;

/**
 * Dispays the main preferences screen.
 */
public class MainPreferenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new MainPreferenceFragment())
		.commit();
	}

}