package de.htwg.ticketqualitymonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.htwg.ticketqualitymonitor.model.JiraIssue;
import de.htwg.ticketqualitymonitor.model.JiraIssueCategory;

/**
 * Adapter to display a list of Jira issues.
 */
public class IssuesListArrayAdapter extends ArrayAdapter<JiraIssue> implements
		OnSharedPreferenceChangeListener {

	private final Comparator<JiraIssue> comparator = new HighestFirstComparator();
	private final int colorGreen;
	private final int colorYellow;
	private final int colorRed;
	private double thresholdGreen;
	private double thresholdYellow;

	public IssuesListArrayAdapter(Context context, JiraIssue[] issues) {
		// Convert the issues array to an array list because ArrayAdapter will
		// convert an array to an AbstractList which cannot be modified later
		// on (this would render the add, addAll etc. methods useless).
		super(context, 0, new ArrayList<JiraIssue>(Arrays.asList(issues)));

		final Resources resources = context.getResources();
		colorGreen = resources.getColor(R.color.issue_green);
		colorYellow = resources.getColor(R.color.issue_yellow);
		colorRed = resources.getColor(R.color.issue_red);

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.registerOnSharedPreferenceChangeListener(this);
		setupThresholds();
	}

	private void setupThresholds() {
		thresholdGreen = ViewedIssuesHandler.getThresholdGreen(getContext());
		thresholdYellow = ViewedIssuesHandler.getThresholdYellow(getContext());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		final String keyGreen = getContext().getString(
				R.string.key_color_threshold_green);
		final String keyYellow = getContext().getString(
				R.string.key_color_threshold_green);

		if (keyGreen.equals(key) || keyYellow.equals(key)) {
			setupThresholds();
			notifyDataSetChanged();
		}
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

	private void fillView(View rootView, JiraIssue issue) {
		// Lookup view for data population
		final TextView issueItemName = (TextView) rootView
				.findViewById(R.id.issueItemName);
		final TextView issueItemDescription = (TextView) rootView
				.findViewById(R.id.issueItemDescription);

		// Populate the data into the template view using the data object
		issueItemName.setText(issue.getKey());
		issueItemDescription.setText(Html.fromHtml(buildDescription(issue)));
	}

	private String buildDescription(JiraIssue issue) {
		final Context ctx = getContext();
		final StringBuilder sb = new StringBuilder();

		sb.append(ctx.getString(R.string.hours_per_update));
		sb.append(String.format("<b>%.2f</b>",
				issue.getSpentTimeHoursPerUpdate()));
		sb.append("\n");
		sb.append("<b>" + ctx.getString(R.string.assignee) + "</b>");
		sb.append(issue.getAssignee());

		return sb.toString();
	}

	private void setColor(View rootView, JiraIssue issue) {
		final View itemRoot = rootView.findViewById(R.id.issueItem);
		final JiraIssueCategory category = JiraIssueCategory.fromIssue(issue,
				thresholdGreen, thresholdYellow);

		switch (category) {
		case GREEN:
			itemRoot.setBackgroundColor(colorGreen);
			break;
		case YELLOW:
			itemRoot.setBackgroundColor(colorYellow);
			break;
		case RED:
			itemRoot.setBackgroundColor(colorRed);
			break;
		}
	}

	@Override
	public void notifyDataSetChanged() {
		setNotifyOnChange(false);
		sort(comparator);
		super.notifyDataSetChanged();
	}

	private class HighestFirstComparator implements Comparator<JiraIssue> {

		@Override
		public int compare(JiraIssue lhs, JiraIssue rhs) {
			final double lhsRate = lhs.getSpentTimeHoursPerUpdate();
			final double rhsRate = rhs.getSpentTimeHoursPerUpdate();

			if (lhsRate > rhsRate) {
				return -1;
			}
			if (rhsRate < lhsRate) {
				return 1;
			}
			return 0;
		}

	}

}
