package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;

public class RaceCustomSpinnerProductGroupAdapter extends ArrayAdapter<String> {

	LayoutInflater lInflater;
	Context ctx;
	List<String> mLVProductGroup;
	String mProductGroupName;

	public RaceCustomSpinnerProductGroupAdapter(Context context, List<String> productGroup) {
		super(context, android.R.layout.simple_list_item_1, productGroup);
		lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = context;
		this.mLVProductGroup = productGroup;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.spinner_item, parent, false);
		}

		TextView textView = (TextView) view.findViewById(R.id.text1);
		mProductGroupName = mLVProductGroup.get(position);
		
		if (mProductGroupName != null) {
			textView.setText(mProductGroupName);
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
		mProductGroupName = mLVProductGroup.get(position);
		
		if (mProductGroupName != null) {
			textView.setText(mProductGroupName);
			
		}
		return view;
	}

}
