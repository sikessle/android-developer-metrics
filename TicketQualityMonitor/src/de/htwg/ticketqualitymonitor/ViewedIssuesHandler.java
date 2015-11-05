package de.htwg.ticketqualitymonitor;

import java.util.HashSet;
import java.util.Set;

import de.htwg.ticketqualitymonitor.model.JiraIssue;
import de.htwg.ticketqualitymonitor.model.JiraIssueCategory;

public class ViewedIssuesHandler {

	public static Set<String> getRelevantUniqueIssueIdents(JiraIssue[] issues,
			double thresholdGreen, double thresholdYellow) {
		final Set<String> criticalIssues = new HashSet<>();
		JiraIssueCategory category;

		for (final JiraIssue issue : issues) {
			category = JiraIssueCategory.fromIssue(issue, thresholdGreen,
					thresholdYellow);
			if (category == JiraIssueCategory.RED) {
				criticalIssues.add(issue.getKey()
						+ issue.getSpentTimeHoursPerUpdate());
			}
		}

		return criticalIssues;
	}

}
