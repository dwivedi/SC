package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.FeedbackTypeMasterModel;

public class CustomSpinneFeedbackTypeAdapter extends
		ArrayAdapter<FeedbackTypeMasterModel> {

	LayoutInflater lInflater;
	Context ctx;
	List<FeedbackTypeMasterModel> mFeedbackTypeMasterModel;
	String mFeedbackTypeName,mSampleImageName;
	int mFeedbackTypeID;

	public CustomSpinneFeedbackTypeAdapter(
			List<FeedbackTypeMasterModel> feedbackTypeMasterModel,
			Context context) {
		super(context, android.R.layout.simple_list_item_1,
				feedbackTypeMasterModel);

		lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ctx = context;
		mFeedbackTypeMasterModel = feedbackTypeMasterModel;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
	
		View view = convertView;
		try {
			if (view == null) {
				view = lInflater.inflate(R.layout.spinner_item, parent, false);
			}

			TextView textView = (TextView) view.findViewById(R.id.text1);
			mFeedbackTypeName = mFeedbackTypeMasterModel.get(position).getFeedbackTypeName();
			mFeedbackTypeID= mFeedbackTypeMasterModel.get(position).getFeedbackTypeID();
			mSampleImageName= mFeedbackTypeMasterModel.get(position).getSampleImageName();
			
			if (mFeedbackTypeName != null) {
				textView.setText(mFeedbackTypeName);
			    view.setTag(mFeedbackTypeID);
			    if(!mSampleImageName.equalsIgnoreCase("null"))
			    {
			    	view.setTag(R.string.sample_image_tag, mSampleImageName);
			    }
			    
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return view;

	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		View view = convertView;
		try {
			if (view == null) {
				view = lInflater.inflate(R.layout.spinner_item, parent, false);
			}

			TextView textView = (TextView) view.findViewById(R.id.text1);
			mFeedbackTypeName = mFeedbackTypeMasterModel.get(position).getFeedbackTypeName();
			mFeedbackTypeID= mFeedbackTypeMasterModel.get(position).getFeedbackTypeID();
			mSampleImageName= mFeedbackTypeMasterModel.get(position).getSampleImageName();
			
			if (mFeedbackTypeName != null) {
				textView.setText(mFeedbackTypeName);
			    view.setTag(mFeedbackTypeID);
			    if(!mSampleImageName.equalsIgnoreCase("null"))
			    {
			    	view.setTag(R.string.sample_image_tag, mSampleImageName);
			    }
			    
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return view;

	
	}

}
