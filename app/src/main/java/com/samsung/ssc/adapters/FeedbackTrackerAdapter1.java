package com.samsung.ssc.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.FMSStatusDataModal;
import com.samsung.ssc.dto.FeedbackTrackerDto1;
import com.samsung.ssc.util.CompareFeedbackTrackerDtoByCategory;
import com.samsung.ssc.util.CompareFeedbackTrackerDtoByStatus;
import com.samsung.ssc.util.CompareFeedbackTrackerDtoByTeam;
import com.samsung.ssc.util.CompareFeedbackTrackerDtoByUpdatedOnDate;

public class FeedbackTrackerAdapter1 extends BaseAdapter {

	private ArrayList<FeedbackTrackerDto1> mDataSet;
	private LayoutInflater inflater;

	private HashMap<Integer, String> mStatusMap;
	private int visibility;
	private boolean mSortUpdatedDate = true;
	private boolean mSortStatus = true;
	private boolean mSortCategory = true;
	private boolean mSortTeam = true;
	

	public FeedbackTrackerAdapter1(Context context) {
		inflater = LayoutInflater.from(context);
		mDataSet = new ArrayList<FeedbackTrackerDto1>();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataSet.size();
	}
	
	public int getLastFeedbackId() {

		int lastID;
		if (this.mDataSet.size() > 0) {
			lastID = (int) this.mDataSet.get(getCount() - 1).getFeedbackID();
		} else {
			lastID = 0;
		}
		return lastID;
	}

	@Override
	public FeedbackTrackerDto1 getItem(int position) {
		// TODO Auto-generated method stub
		return mDataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.feedback_tracker_list_row,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FeedbackTrackerDto1 item = mDataSet.get(position);

		try {
			holder.tvTeamName.setText(item.getTeamName());
		} catch (Exception e) {
			holder.tvTeamName.setText(String.valueOf(item.getFeedbackTeamID()));
		}
		try {
			holder.tvCategoryName.setText(item.getFeedbackCategoryName());
		} catch (Exception e) {
			holder.tvCategoryName.setText(String.valueOf(item
					.getFeedbackCatID()));
		}
		try {
			holder.tvStatus.setText(mStatusMap.get(item.getCurrentStatusID()));
		} catch (Exception e) {
			holder.tvStatus.setText(String.valueOf(item.getCurrentStatusID()));
		}
		holder.tvUpdatedOn.setText(item.getLastUpdatedOn());
		holder.cbAction.setTag(position);
		holder.cbAction.setVisibility(visibility == View.GONE ? View.INVISIBLE
				: visibility);
		holder.cbAction.setChecked(item.isChecked());
		holder.cbAction.setTag(position);
		holder.cbAction
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int getPosition = (Integer) buttonView.getTag();
						getItem(getPosition).setChecked(buttonView.isChecked());

						if (isChecked) {
							holder.layoutDetailsContainer
									.setVisibility(View.VISIBLE);
						} else {
							holder.layoutDetailsContainer
									.setVisibility(View.GONE);
						}

					}
				});

		if (item.isChecked()) {
			holder.layoutDetailsContainer.setVisibility(View.VISIBLE);
		} else {
			holder.layoutDetailsContainer.setVisibility(View.GONE);
		}

		holder.tvCreatedBy.setText(item.getCreatedBy());
		holder.tvCreatedOn.setText(item.getCreatedOn());
		holder.tvFeedbackSince.setText(String.valueOf(item.getPendingSince())
				+ " Days");
		holder.tvRemark.setText(item.getRemarks());

		return convertView;
	}

	public void setActionVisiblity(int visibility) {

		this.visibility = visibility;

	}

	class ViewHolder {
		private TextView tvTeamName;
		private TextView tvCategoryName;
		private TextView tvStatus;
		private TextView tvUpdatedOn;
		private TextView tvCreatedBy;
		private TextView tvCreatedOn;
		private TextView tvFeedbackSince;
		private TextView tvRemark;
		private ViewGroup layoutDetailsContainer;
		private CheckBox cbAction;

		public ViewHolder(View convertView) {

			tvTeamName = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowTeam);
			tvCategoryName = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowCategory);
			tvStatus = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowStatus);
			tvUpdatedOn = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowUpdateOn);
			tvCreatedBy = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowDetailCreatedBy);
			tvCreatedOn = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowDetailCreatedOn);
			tvFeedbackSince = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowDetailFeedbackScince);
			tvRemark = (TextView) convertView
					.findViewById(R.id.textViewFMSTrackerFeedbackRowDetailFeedbackRemark);
			layoutDetailsContainer = (ViewGroup) convertView
					.findViewById(R.id.linerlayoutFMSTrackerFeedbackRowDetailContainer);
			cbAction = (CheckBox) convertView
					.findViewById(R.id.checkBoxFMSTrackerFeedbackRowAction);

		}

	}

	public void addStatusMap(List<FMSStatusDataModal> mStausList) {
		this.mStatusMap = new HashMap<Integer, String>();
		for (FMSStatusDataModal fmsStatusDataModal : mStausList) {
			this.mStatusMap.put(fmsStatusDataModal.getStausId(), fmsStatusDataModal.getStatusName());
		}
	}

	public void addDataSet(FeedbackTrackerDto1 modal) {

		this.mDataSet.add(modal);
		notifyDataSetChanged();

	}

	public void addDataSet(ArrayList<FeedbackTrackerDto1> modal) {

		mDataSet.addAll(modal);
		notifyDataSetChanged();

	}

	public ArrayList<FeedbackTrackerDto1> getSelectedItem() {

		ArrayList<FeedbackTrackerDto1> selectedItem = new ArrayList<FeedbackTrackerDto1>();
		for (FeedbackTrackerDto1 feedbackTrackerDto1 : mDataSet) {
			if (feedbackTrackerDto1.isChecked()) {
				selectedItem.add(feedbackTrackerDto1);
			}

		}
		return selectedItem;
	}

	public void clearDataSet() {
		this.mDataSet.clear();

	}

	public void sortByTeam() {

		if (mDataSet.size() > 1) {
			Collections.sort(mDataSet, new CompareFeedbackTrackerDtoByTeam(mSortTeam));
			mSortTeam = !mSortTeam;
			notifyDataSetChanged();
		}

	}

	public void sortByCategoryName() {
		if (mDataSet.size() > 1) {
			Collections.sort(mDataSet,
					new CompareFeedbackTrackerDtoByCategory(mSortCategory));
			mSortCategory = !mSortCategory;
			notifyDataSetChanged();
		}
	}

	public void sortByStatus() {
		if (mDataSet.size() > 1) {
			Collections.sort(mDataSet, new CompareFeedbackTrackerDtoByStatus(mSortStatus));
			mSortStatus = !mSortStatus;
			notifyDataSetChanged();
		}
	}

	public void sortByUpdatedOnDate() {
		if (mDataSet.size() > 1) {
			
			Collections.sort(mDataSet,
					new CompareFeedbackTrackerDtoByUpdatedOnDate(mSortUpdatedDate));
			mSortUpdatedDate = !mSortUpdatedDate;
			notifyDataSetChanged();
		}
	}
}
