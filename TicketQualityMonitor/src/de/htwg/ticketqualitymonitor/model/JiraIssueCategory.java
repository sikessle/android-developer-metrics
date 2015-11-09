package de.htwg.ticketqualitymonitor.model;

public enum JiraIssueCategory {
	GREEN, YELLOW, RED;

	public static JiraIssueCategory fromIssue(JiraIssue issue,
			double thresholdGreen, double thresholdYellow) {
		final double hoursPerUpdate = issue.getSpentHoursPerUpdate();

		if (hoursPerUpdate <= thresholdGreen) {
			return GREEN;
		} else if (hoursPerUpdate <= thresholdYellow) {
			return YELLOW;
		}
		return RED;
	}
}
