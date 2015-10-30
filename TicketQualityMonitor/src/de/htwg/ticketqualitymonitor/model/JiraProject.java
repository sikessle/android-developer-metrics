package de.htwg.ticketqualitymonitor.model;

public class JiraProject {

	private final String id;
	private final String key;
	private final String name;

	public JiraProject(String id, String key, String name) {
		this.id = id;
		this.key = key;
		this.name = name;
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
