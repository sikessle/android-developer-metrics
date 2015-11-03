package de.htwg.ticketqualitymonitor.model;

import net.jcip.annotations.Immutable;

/**
 * Represents a Jira issues resource with some, not all, properties. This class
 * will be instantiated by GSON and filled via reflection.
 */
@Immutable
public class JiraIssue {

	private String key;
	private Fields fields;

	/**
	 * For GSON
	 */
	public JiraIssue() {
	}

	public String getKey() {
		return key;
	}

	public String getAssignee() {
		return fields.assignee.name;
	}

	public double getSpentTimeHoursPerUpdate() {
		return fields.timetracking.timeSpentSeconds / 60. / 60.
				/ fields.worklog.worklogs.length;
	}

	public static class Fields {
		private Assignee assignee;
		private Worklog worklog;
		private Timetracking timetracking;

		public Fields() {
		}
	}

	public static class Assignee {
		private String name;

		public Assignee() {
		}
	}

	public static class Worklog {
		private WorklogItem[] worklogs;

		public Worklog() {
		}
	}

	public static class WorklogItem {
		public WorklogItem() {
		}
	}

	public static class Timetracking {
		private long timeSpentSeconds;

		public Timetracking() {
		}
	}

}