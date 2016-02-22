package com.samsung.ssc.sync;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samsung.ssc.R;

public class SyncListNotificationAdapter extends BaseAdapter {

	ArrayList<SyncStatusNotificationModel> mData;
	Context mContext;
	LayoutInflater mInflater;

	public SyncListNotificationAdapter(Context context) {
		mData = new ArrayList<SyncStatusNotificationModel>();
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.sync_notification_layout,
					null);
		}
		TextView tvTitle = (TextView) convertView
				.findViewById(R.id.tv_name_syncNotifacation_layout);
		LinearLayout llIcon = (LinearLayout) convertView
				.findViewById(R.id.ll_status_icon_syncNotifacation_layout);
		SyncStatusNotificationModel syncData = mData.get(position);
		tvTitle.setText(syncData.getModuleName());

		if (syncData.getStatus() == SyncUtils.SYNC_STARTED) {

			setProgress(llIcon);
		} else if (syncData.getStatus() == SyncUtils.SYNC_STARTED) {

		} else {

			setImage(llIcon, syncData.getStatus());

		}

		return convertView;
	}

	private void setImage(LinearLayout ll, int syncStatus) {
		ll.removeAllViews();

		ImageView imageView = new ImageView(mContext);
		if (syncStatus == SyncUtils.SYNC_STATUS_SYNC_COMPLETED) {
			imageView.setImageResource(R.drawable.sync_complete);
		} else if (syncStatus == SyncUtils.SYNC_STATUS_ERROR) {
			imageView.setImageResource(R.drawable.ic_menu_close_clear_cancel);
		}

		ll.addView(imageView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
	}

	private void setProgress(LinearLayout ll) {
		ll.removeAllViews();
		ProgressBar progressBar = new ProgressBar(mContext, null,
				android.R.attr.progressBarStyleSmall);
		progressBar.setIndeterminate(true);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);

		ll.addView(progressBar, params);
	}

	public void addItem(SyncStatusNotificationModel item) {
		if (mData.contains(item)) {
			mData.remove(item);
		}

		mData.add(item);

		notifyDataSetChanged();
	}

	public void addItem1(SyncStatusNotificationModel item) {

		mData.add(item);

		notifyDataSetChanged();
	}

	public boolean isAllSuccesull() {
		for (SyncStatusNotificationModel item : mData) {

			if (item.getStatus() == SyncUtils.SYNC_STATUS_ERROR) {
				return false;
			}

		}
		return true;
	}

}