package de.htwg.ticketqualitymonitor;

import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.htwg.ticketqualitymonitor.model.JiraIssue;

/**
 * Adapter to display a list of Jira issues.
 */
public class JiraIssuesListArrayAdapter extends ArrayAdapter<JiraIssue> {

	private final Comparator<JiraIssue> comparator = new JiraIssueComparator();

	public JiraIssuesListArrayAdapter(Context context, JiraIssue[] issues) {
		super(context, 0, issues);
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

		return convertView;
	}

	private void fillView(View convertView, JiraIssue issue) {
		// Lookup view for data population
		final TextView issueItemName = (TextView) convertView
				.findViewById(R.id.issueItemName);
		final TextView issueItemDescription = (TextView) convertView
				.findViewById(R.id.issueItemDescription);

		// Populate the data into the template view using the data object
		issueItemName.setText(issue.getKey());
		issueItemDescription.setText(buildDescription(issue));
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
