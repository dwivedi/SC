package com.samsung.ssc.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;

public class TerritoriesListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private HashMap<String, HashMap<String, String>> rootmap;

	public TerritoriesListAdapter(Context context,
			LinkedHashMap<String, HashMap<String, String>> rootmap) {
		this.rootmap = rootmap;
		this.context = context;

	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		String v = (String) getChild(groupPosition, childPosition);
		String[] k = v.split("-");
		ChildViewholder viewholder = new ChildViewholder();
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.layout_territory_list_child, null);

			viewholder.stringTextView = (TextView) convertView
					.findViewById(R.id.layout_territory_textview_string);
			viewholder.valueTextView = (TextView) convertView
					.findViewById(R.id.layout_territory_textview_value);

			convertView.setTag(viewholder);
		} else {
			viewholder = (ChildViewholder) convertView.getTag();
		}

		viewholder.stringTextView.setText(k[0]);
		viewholder.valueTextView.setText(k[1]);

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ParentViewholder viewholder = new ParentViewholder();

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.layout_territory_list_parent, null);
			viewholder.distyNameTextView = (TextView) convertView
					.findViewById(R.id.layout_territory_textview_disty_name);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ParentViewholder) convertView.getTag();
		}

		viewholder.distyNameTextView
				.setText(getGroup(groupPosition).toString());
		viewholder.distyNameTextView.setTag(isExpanded);
		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		ArrayList<String> childKeys = new ArrayList<String>(rootmap.get(
				new ArrayList<String>(rootmap.keySet()).get(groupPosition))
				.keySet());
		ArrayList<String> childValues = new ArrayList<String>(rootmap.get(
				new ArrayList<String>(rootmap.keySet()).get(groupPosition))
				.values());

		return (childKeys.get(childPosition) + "-" + childValues
				.get(childPosition));
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		HashMap<String, String> childMap = rootmap.get(new ArrayList<String>(
				rootmap.keySet()).get(groupPosition));

		return childMap.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		ArrayList<String> groupKeys = new ArrayList<String>(rootmap.keySet());

		return groupKeys.get(groupPosition);
	}

	@Override
	public int getGroupCount() {

		return rootmap.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {

		super.onGroupExpanded(groupPosition);

	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public class ChildViewholder {
		private TextView stringTextView;
		private TextView valueTextView;
	}

	public class ParentViewholder {
		private TextView distyNameTextView;
	}

}
