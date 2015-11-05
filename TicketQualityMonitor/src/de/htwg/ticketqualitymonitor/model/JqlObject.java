package de.htwg.ticketqualitymonitor.model;

import java.util.Arrays;

import net.jcip.annotations.Immutable;

/**
 * Wrapper type to handle JQL results.
 */
@Immutable
public class JqlObject {
	private JiraIssue[] issues;

	public JqlObject() {
	}

	public JiraIssue[] getIssues() {
		return Arrays.copyOf(issues, issues.length);
	}
}
