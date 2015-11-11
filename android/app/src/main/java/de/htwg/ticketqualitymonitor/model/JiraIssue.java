package de.htwg.ticketqualitymonitor.model;

import java.util.concurrent.TimeUnit;

/**
 * Represents a Jira issues resource with some, not all, properties. This class
 * will be instantiated by GSON and filled via reflection.
 */
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

	public void setKey(String key) {
		this.key = key;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}

	public String getAssignee() {
		return fields.assignee.name;
	}

	public double getSpentHoursPerUpdate() {
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

		public void setAssignee(Assignee assignee) {
			this.assignee = assignee;
		}

		public void setWorklog(Worklog worklog) {
			this.worklog = worklog;
		}

		public void setTimetracking(Timetracking timetracking) {
			this.timetracking = timetracking;
		}

	}

	public static class Assignee {
		private String name;

		public Assignee() {
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static class Worklog {
		private int total;

		public Worklog() {
		}

		public void setTotal(int total) {
			this.total = total;
		}

	}

	public static class Timetracking {
		private long timeSpentSeconds;

		public Timetracking() {
		}

		public void setTimeSpentSeconds(long timeSpentSeconds) {
			this.timeSpentSeconds = timeSpentSeconds;
		}

	}

}