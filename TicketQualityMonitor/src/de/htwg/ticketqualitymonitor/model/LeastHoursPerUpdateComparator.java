package de.htwg.ticketqualitymonitor.model;

import java.util.Comparator;

public class LeastHoursPerUpdateComparator implements Comparator<JiraIssue> {

	@Override
	public int compare(JiraIssue lhs, JiraIssue rhs) {
		final double lhsRate = lhs.getSpentHoursPerUpdate();
		final double rhsRate = rhs.getSpentHoursPerUpdate();

		if (lhsRate > rhsRate) {
			return -1;
		}
		if (lhsRate < rhsRate) {
			return 1;
		}
		return 0;
	}

}