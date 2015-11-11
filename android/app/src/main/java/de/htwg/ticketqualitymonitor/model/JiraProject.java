package de.htwg.ticketqualitymonitor.model;

import net.jcip.annotations.Immutable;

/**
 * Represents a Jira project resource with some, not all, properties. This class
 * will be instantiated by GSON and filled via reflection.
 */
@Immutable
public class JiraProject {

	private String id;
	private String key;
	private String name;

	/**
	 * For GSON
	 */
	public JiraProject() {
	}

	public String getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

}
