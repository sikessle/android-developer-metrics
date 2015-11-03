package de.htwg.ticketqualitymonitor.model;

import java.util.Arrays;

import net.jcip.annotations.Immutable;

/**
 * Represents a Jira issues resource with some, not all, properties. This class
 * will be instantiated by GSON and filled via reflection.
 */
@Immutable
public class JiraIssue {

	private final String name = "AUMEFUENF-27";
	private JiraWorkLog[] worklogs;

	/**
	 * For GSON
	 */
	public JiraIssue() {
	}

	public String getName() {
		return name;
	}

	public JiraWorkLog[] getWorklogs() {
		return Arrays.copyOf(worklogs, worklogs.length);
	}

	public double getWorklogUpdatesPerDay() {
		// TODO calculate
		return 2.0;
	}

}