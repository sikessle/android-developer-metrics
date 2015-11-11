package de.htwg.ticketqualitymonitor.model;

import java.util.Arrays;
import java.util.List;

import android.test.AndroidTestCase;

import com.android.volley.Response.Listener;

public class JqlIssueListenerTest extends AndroidTestCase {

	public void testGetIssues() throws NoSuchFieldException,
			IllegalAccessException, IllegalArgumentException {
		final JqlObject expectedJqlObject = new JqlObject();
		final JiraIssue[] expectedIssues = new JiraIssue[] {};

		expectedJqlObject.setIssues(expectedIssues);

		final TestListener wrappedListener = new TestListener(expectedIssues);
		final JqlIssueListener listener = new JqlIssueListener(wrappedListener);

		listener.onResponse(expectedJqlObject);
	}

	public static class TestListener implements Listener<JiraIssue[]> {

		private final List<JiraIssue> expectedIssues;

		public TestListener(JiraIssue[] expectedIssues) {
			this.expectedIssues = Arrays.asList(expectedIssues);
		}

		@Override
		public void onResponse(JiraIssue[] issues) {
			final List<JiraIssue> actualIssues = Arrays.asList(issues);

			assertEquals(expectedIssues, actualIssues);
		}

	}
}