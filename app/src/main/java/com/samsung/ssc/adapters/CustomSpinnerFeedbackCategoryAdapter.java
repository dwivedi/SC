package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.FeedbackCategoryMasterModel;

public class CustomSpinnerFeedbackCategoryAdapter extends
		ArrayAdapter<FeedbackCategoryMasterModel> {

	LayoutInflater lInflater;
	Context ctx;
	List<FeedbackCategoryMasterModel> mFeedbackCategories;
	String mFeedbackCatName;
	int mFeedbackCatID;

	public CustomSpinnerFeedbackCategoryAdapter(
			List<FeedbackCategoryMasterModel> feedbackCategories,
			Context context) {
		super(context, android.R.layout.simple_list_item_1, feedbackCategories);
		lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = context;
		mFeedbackCategories = feedbackCategories;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.spinner_item, parent, false);
		}

		TextView textView = (TextView) view.findViewById(R.id.text1);
		mFeedbackCatName = mFeedbackCategories.get(position).getFeedbackCatName();
		mFeedbackCatID = mFeedbackCategories.get(position).getFeedbackCatID();
		if (mFeedbackCatName != null) {
			textView.setText(mFeedbackCatName);

			view.setTag(mFeedbackCatID);

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
		mFeedbackCatName = mFeedbackCategories.get(position).getFeedbackCatName();
		mFeedbackCatID = mFeedbackCategories.get(position).getFeedbackCatID();
		if (mFeedbackCatName != null) {
			textView.setText(mFeedbackCatName);
			view.setTag(mFeedbackCatID);

			
		}

		
		return view;
	}

}
