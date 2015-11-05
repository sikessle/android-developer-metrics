package de.htwg.ticketqualitymonitor;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import de.htwg.ticketqualitymonitor.model.JiraIssue;
import de.htwg.ticketqualitymonitor.model.JiraIssueCategory;

public class ViewedIssuesHandler {

	private static final String STORE_VIEWED_ISSUES_KEY = "de.htwg.ticketqualitymonitor.viewed_issues";

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

	public static SharedPreferences getStore(Context context) {
		return context.getSharedPreferences(
				ViewedIssuesHandler.STORE_VIEWED_ISSUES_KEY, 0);

	}

	public static void storeRelevantUniqueIssueIdents(Context context,
			JiraIssue[] issues, double thresholdGreen, double thresholdYellow) {

		final Set<String> relevantIssues = getRelevantUniqueIssueIdents(issues,
				thresholdGreen, thresholdYellow);
		final Editor editor = getStore(context).edit();

		for (final String issueIdent : relevantIssues) {
			editor.putString(issueIdent, ".");
		}

		editor.apply();
	}

	public static boolean storeContainsAllRelevantIssues(Context context,
			JiraIssue[] issues, double thresholdGreen, double thresholdYellow) {

		final Set<String> relevantIssues = getRelevantUniqueIssueIdents(issues,
				thresholdGreen, thresholdYellow);
		final SharedPreferences store = getStore(context);

		return store.getAll().keySet().containsAll(relevantIssues);
	}

}
