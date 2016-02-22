package com.samsung.ssc.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.BadgeButton;
import com.samsung.ssc.dto.KPIData;

public class KPIListAdapter extends BaseAdapter {

	private ArrayList<KPIData> mDataSet;
	private LayoutInflater inflater;
 
	public KPIListAdapter(Context context) {
		super();
		this.mDataSet = new ArrayList<KPIData>();
		this.inflater = LayoutInflater.from(context);

 	}

	@Override
	public int getCount() {
		return mDataSet.size();
	}

	@Override
	public KPIData getItem(int position) {
		return mDataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

/*	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);
		TextView kpiName = null, unRead = null;

		if (convertView == null) {
			convertView = (View) inflater.inflate(R.layout.kpi_list_item, null);
		}
		kpiName = (TextView) convertView.findViewById(R.id.kpiName);
		unRead = (TextView) convertView.findViewById(R.id.unread);
//		total = (TextView) convertView.findViewById(R.id.total);

		final KPIData data = getItem(position);

		kpiName.setText(String.valueOf(data.getNotificationTypeDescription()));
		unRead.setText(String.valueOf(data.getUnreadCount()));
//		total.setText(String.valueOf(data.getTotalCount()));
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context,NotificationsListFragment1.class);
				intent.putExtra(Constants.NOTIFICATION_MESSAGE_TYPE, data.getNotificationType());
				intent.putExtra(Constants.KAS_NAME, data.getNotificationTypeDescription());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
*/	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.kpi_list_item1, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		KPIData item = mDataSet.get(position);
		holder.badgeButton.setBadgeText(String.valueOf(item.getUnreadCount()));
		holder.badgeButton.setBtnClickText(item
				.getNotificationTypeDescription());

		return convertView;
	}
	
	class ViewHolder{

		BadgeButton badgeButton;
		 
		public ViewHolder(View convertView) {
			badgeButton = (BadgeButton)convertView.findViewById(R.id.notificationTextBadgeRowItem);
			 
			
		}
		
	}

	public void addItem(ArrayList<KPIData> kpiDatas) {
		
		this.mDataSet.clear();
		this.mDataSet.addAll(kpiDatas);
		notifyDataSetChanged();
		
	}
}
