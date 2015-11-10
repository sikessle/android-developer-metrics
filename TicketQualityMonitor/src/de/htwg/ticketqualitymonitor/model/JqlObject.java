package de.htwg.ticketqualitymonitor.model;

import java.util.Arrays;

/**
 * Wrapper type to handle JQL results.
 */
public class JqlObject {
	private JiraIssue[] issues;

	public JqlObject() {
	}

	public JiraIssue[] getIssues() {
		return Arrays.copyOf(issues, issues.length);
	}

	public void setIssues(JiraIssue[] issues) {
		this.issues = Arrays.copyOf(issues, issues.length);
	}
}
