package de.htwg.ticketqualitymonitor.model;

import net.jcip.annotations.Immutable;

/**
 * Represents a Jira issues resource with some, not all, properties. This class
 * will be instantiated by GSON and filled via reflection.
 */
@Immutable
public class JiraIssue {

	private final String name = "ISSUEDummy";
	private JiraWorkLogs[] worklogs;

	/**
	 * For GSON
	 */
	public JiraIssue() {
	}

	public String getName() {
		return name;
	}

	public JiraWorkLogs[] getWorklogs() {
		return worklogs;
	}

}