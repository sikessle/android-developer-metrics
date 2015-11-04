package de.htwg.ticketqualitymonitor;

import java.util.Comparator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.htwg.ticketqualitymonitor.model.JiraIssue;

/**
 * Adapter to display a list of Jira issues.
 */
public class IssuesListArrayAdapter extends ArrayAdapter<JiraIssue> {

	private final Comparator<JiraIssue> comparator = new JiraIssueComparator();
	private final int colorGreen;
	private final int colorYellow;
	private final int colorRed;
	private final double thresholdGreen;
	private final double thresholdYellow;

	public IssuesListArrayAdapter(Context context, JiraIssue[] issues) {
		super(context, 0, issues);
		final Resources resources = context.getResources();
		colorGreen = resources.getColor(R.color.issue_green);
		colorYellow = resources.getColor(R.color.issue_yellow);
		colorRed = resources.getColor(R.color.issue_red);

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		thresholdGreen = Double.parseDouble(prefs.getString(
				context.getString(R.string.key_color_threshold_green), "1.0"));
		thresholdYellow = Double.parseDouble(prefs.getString(
				context.getString(R.string.key_color_threshold_yellow), "2.0"));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		final JiraIssue issue = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_issue, parent, false);
		}
		fillView(convertView, issue);
		setColor(convertView, issue);

		return convertView;
	}

	private void fillView(View viewItemParent, JiraIssue issue) {
		// Lookup view for data population
		final TextView issueItemName = (TextView) viewItemParent
				.findViewById(R.id.issueItemName);
		final TextView issueItemDescription = (TextView) viewItemParent
				.findViewById(R.id.issueItemDescription);

		// Populate the data into the template view using the data object
		issueItemName.setText(issue.getKey());
		issueItemDescription.setText(buildDescription(issue));
	}

	private void setColor(View viewItemParent, JiraIssue issue) {
		final double hoursPerUpdate = issue.getSpentTimeHoursPerUpdate();

		if (hoursPerUpdate <= thresholdGreen) {
			viewItemParent.setBackgroundColor(colorGreen);
		} else if (hoursPerUpdate <= thresholdYellow) {
			viewItemParent.setBackgroundColor(colorYellow);
		} else {
			viewItemParent.setBackgroundColor(colorRed);
		}
	}

	private String buildDescription(JiraIssue issue) {
		final Context ctx = getContext();
		final StringBuilder sb = new StringBuilder();

		sb.append(ctx.getString(R.string.hours_per_update));
		sb.append(" ");
		sb.append(issue.getSpentTimeHoursPerUpdate());
		sb.append("\n");
		sb.append(ctx.getString(R.string.assignee));
		sb.append(": ");
		sb.append(issue.getAssignee());

		return sb.toString();
	}

	@Override
	public void notifyDataSetChanged() {
		sort(comparator);
		super.notifyDataSetChanged();
	}

	private class JiraIssueComparator implements Comparator<JiraIssue> {

		@Override
		public int compare(JiraIssue lhs, JiraIssue rhs) {
			final double lhsRate = lhs.getSpentTimeHoursPerUpdate();
			final double rhsRate = lhs.getSpentTimeHoursPerUpdate();

			if (lhsRate < rhsRate) {
				return -1;
			}
			if (rhsRate > lhsRate) {
				return 1;
			}
			return 0;
		}

	}
}
