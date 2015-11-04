package de.htwg.ticketqualitymonitor.model;

import java.util.concurrent.TimeUnit;

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
		if (fields.worklog.total == 0) {
			return 0.;
		}
		return TimeUnit.SECONDS.toMinutes(fields.timetracking.timeSpentSeconds)
				/ 60. / fields.worklog.total;
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
		private int total;

		public Worklog() {
		}
	}

	public static class Timetracking {
		private long timeSpentSeconds;

		public Timetracking() {
		}
	}

}