package de.htwg.ticketqualitymonitor;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.Immutable;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import de.htwg.ticketqualitymonitor.model.JiraIssue;
import de.htwg.ticketqualitymonitor.model.JiraIssueCategory;

/**
 * Takes care of viewed issues (i.e. to notify about new, unseen issues).
 */
@Immutable
public class ViewedIssuesHandler {

	private static final String STORE_VIEWED_ISSUES_KEY = "de.htwg.ticketqualitymonitor.viewed_issues";

	private static Set<String> getRelevantIssues(JiraIssue[] issues,
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

	/**
	 * The store which is responsible for storing the viewed issues.
	 */
	public static SharedPreferences getStore(Context context) {
		return context.getSharedPreferences(
				ViewedIssuesHandler.STORE_VIEWED_ISSUES_KEY, 0);

	}

	/**
	 * Filters the given issues and stores all relevant ones in the store.
	 */
	public static void storeRelevantIssues(Context context, JiraIssue[] issues) {
		final double thresholdGreen = getThresholdGreen(context);
		final double thresholdYellow = getThresholdYellow(context);

		final Set<String> relevantIssues = getRelevantIssues(issues,
				thresholdGreen, thresholdYellow);
		final Editor editor = getStore(context).edit();

		for (final String issueIdent : relevantIssues) {
			editor.putString(issueIdent, ".");
		}

		editor.apply();
	}

	/**
	 * Checks if the store contains all of the relevant issues (from the given
	 * issues).
	 */
	public static boolean storeContainsAllRelevantIssues(Context context,
			JiraIssue[] issues) {

		final double thresholdGreen = getThresholdGreen(context);
		final double thresholdYellow = getThresholdYellow(context);

		final Set<String> relevantIssues = getRelevantIssues(issues,
				thresholdGreen, thresholdYellow);
		final SharedPreferences store = getStore(context);

		return store.getAll().keySet().containsAll(relevantIssues);
	}

	/**
	 * Returns the threshold for an issue to be categorized as green.
	 */
	public static double getThresholdGreen(Context context) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return Double.parseDouble(prefs.getString(
				context.getString(R.string.key_color_threshold_green), "2.0"));
	}

	/**
	 * Returns the threshold for an issue to be categorized as yellow.
	 */
	public static double getThresholdYellow(Context context) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return Double.parseDouble(prefs.getString(
				context.getString(R.string.key_color_threshold_yellow), "2.0"));
	}

}
