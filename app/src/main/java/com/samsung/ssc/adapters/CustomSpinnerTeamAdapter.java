package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.activitymodule.TeamMasterModel;

public class CustomSpinnerTeamAdapter extends ArrayAdapter<TeamMasterModel> {


	LayoutInflater lInflater;
	Context ctx;
	List<TeamMasterModel> mTeams;
	String mTeamName;
	int mTeamID;

	public CustomSpinnerTeamAdapter(Context context, List<TeamMasterModel> teams) {
		super(context, android.R.layout.simple_list_item_1, teams);
		lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = context;
		this.mTeams = teams;
	}

	
	
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

	


		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.spinner_item, parent, false);
		}

		TextView textView = (TextView) view.findViewById(R.id.text1);
		mTeamName = mTeams.get(position).getTeamName();
		mTeamID = mTeams.get(position).getTeamID();
		if (mTeamName != null) {
			textView.setText(mTeamName);
			view.setTag(mTeamID);
		}

		
		
		
		return view;
	    
	    
	  
	}
	
	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.spinner_item, parent, false);
		}

		TextView textView = (TextView) view.findViewById(R.id.text1);
		mTeamName = mTeams.get(position).getTeamName();
		mTeamID = mTeams.get(position).getTeamID();
		if (mTeamName != null) {
			textView.setText(mTeamName);
			view.setTag(mTeamID);
		}

		
		
		
		return view;
	}

}
