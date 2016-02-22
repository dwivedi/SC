package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.SchemeAvailableDto;

public class SchemeAvailableAdapter extends ArrayAdapter<SchemeAvailableDto>{
	private final Context context;
	private final List<SchemeAvailableDto> values;
	public SchemeAvailableAdapter(Context pContext, int textViewResourceId,List<SchemeAvailableDto> pObjects) {
		super(pContext,R.layout.beat_today, pObjects);
		context=pContext;
		values=pObjects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.schemeavailablelist, parent, false);
	    TextView title = (TextView) rowView.findViewById(R.id.tvtitle);
	    TextView description = (TextView) rowView.findViewById(R.id.tvDescription);
	    TextView schemeID = (TextView) rowView.findViewById(R.id.tvSchemeID);
	    title.setText(values.get(position).getTitle().toString());
	    description.setText(values.get(position).getDescription().toString());
	    schemeID.setText(values.get(position).getSchemeID().toString());
	   
	return rowView;
	}
}
