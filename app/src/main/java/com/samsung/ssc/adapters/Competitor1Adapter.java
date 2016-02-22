package com.samsung.ssc.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.samsung.ssc.R;

public class Competitor1Adapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;
	//private ArrayList<CompetitosrList>  _listDataChild;
	
	public Competitor1Adapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		
		final ChildHolder holder;
		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			holder = new ChildHolder();
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.competitor_list_item, null);

			holder.tvCompetitorName = (TextView) convertView
					.findViewById(R.id.lblListItem);

			holder.edInput = (EditText) convertView
					.findViewById(R.id.editText1);

			convertView.setTag(holder);
			
		} else {
			holder = (ChildHolder) convertView.getTag();
		}

		holder.tvCompetitorName.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder;
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {

			groupHolder = new GroupHolder();
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.competitior_list_group, null);
			groupHolder.tvHeader = (TextView) convertView
					.findViewById(R.id.lblListHeader);
			convertView.setTag(groupHolder);

		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		
		groupHolder.tvHeader.setTypeface(null, Typeface.BOLD);
		groupHolder.tvHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ChildHolder {

		TextView tvCompetitorName;
		EditText edInput;
	}

	class GroupHolder {

		TextView tvHeader;
	}

}
