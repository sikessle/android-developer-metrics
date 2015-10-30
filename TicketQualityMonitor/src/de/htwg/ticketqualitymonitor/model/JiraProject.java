package de.htwg.ticketqualitymonitor.model;

public class JiraProject {

	private String id;
	private String key;
	private String name;
	private String description;

	/**
	 * For GSON
	 */
	public JiraProject() {
	}

	public JiraProject(String id, String key, String name, String description) {
		this.id = id;
		this.key = key;
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

}
