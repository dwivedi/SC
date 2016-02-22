package com.samsung.ssc.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.ssc.dto.NotificationData;

public class NotificationListAdapter extends BaseAdapter {

	private ArrayList<NotificationData> notificationDatas;
	private LayoutInflater inflater;

	public NotificationListAdapter(
			ArrayList<NotificationData> notificationDatas, Context context,int type) {
		super();
		this.notificationDatas = notificationDatas;
 		this.inflater = LayoutInflater.from(context);
	}

	public void setNotificationDatas(
			ArrayList<NotificationData> notificationDatas) {
		this.notificationDatas = notificationDatas;
	}

	@Override
	public int getCount() {
		return notificationDatas.size();
	}

	@Override
	public NotificationData getItem(int position) {

		return notificationDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		TextView iD = null,subject = null,date = null;
//		
//		if(convertView == null)
//		{
//			convertView = (View) inflater.inflate(R.layout.notifications_list_item, null);
//		}
//		iD = (TextView)convertView.findViewById(R.id.iD);
//		subject = (TextView)convertView.findViewById(R.id.subject);
//		date = (TextView)convertView.findViewById(R.id.date);
//		
//		final NotificationData data = getItem(position);
//		
//		iD.setText(String.valueOf(position));
//		subject.setText(data.getPushNotificationMessage().split("~")[0]);
//		date.setText(data.getNotificationDate());
//		if(data.getReadStatus() == 0)
//		{
//			
//			convertView.setBackgroundResource(android.R.color.white);
//		}else
//		{
//			convertView.setBackgroundResource(android.R.color.darker_gray);
//		}			
//		
//		convertView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent viewDetailsNotificationIntent = new Intent(context,NotificationShow.class);
//				viewDetailsNotificationIntent.putExtra(Constants.NOTIFICATION_MESSAGE_TITLE,data.getPushNotificationMessage().split("~")[0]);
//				viewDetailsNotificationIntent.putExtra(Constants.NOTIFICATION_MESSAGE_BODY,data.getPushNotificationMessage().split("~")[1]);
//				viewDetailsNotificationIntent.putExtra(Constants.NOTIFICATION_MESSAGE_TYPE, data.getNotificationType());
//				viewDetailsNotificationIntent.putExtra(Constants.NOTIFICATION_SERVICE_ID, data.getNotificationServiceID());
//				viewDetailsNotificationIntent.putExtra(Constants.NOTIFICATION_READ_STATUS, data.getReadStatus());
//				viewDetailsNotificationIntent.putExtra(Constants.NOTIFICATION_MESSAGE_TYPE, type);
//				Activity  activity=	(Activity)context;
//				activity.startActivityForResult(viewDetailsNotificationIntent, 001);
//			}
//		});
//		
//		return convertView;
//	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = (View) inflater.inflate(
					android.R.layout.simple_list_item_2, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		final NotificationData data = getItem(position);

		holder.tvTitle.setText(data.getPushNotificationMessage().split("~")[0]);
		holder.tvMessage .setText(data.getNotificationDate());

		if (data.getReadStatus() == 0) {
			holder.tvTitle.setTypeface(null, Typeface.BOLD);
//			convertView.setBackgroundResource(android.R.color.white);
		} else {
			holder.tvTitle.setTypeface(null, Typeface.NORMAL);
//			convertView.setBackgroundResource(android.R.color.darker_gray);
		}

		return convertView;
	}
	
	class ViewHolder{
		public ViewHolder(View convertView) {

			tvTitle = (TextView) convertView
					.findViewById(android.R.id.text1);
			tvMessage = (TextView) convertView.findViewById(android.R.id.text2);
			tvTitle.setTextColor(Color.BLACK);
			tvTitle.setSingleLine(true);
			tvMessage.setTextColor(Color.BLACK);
			tvMessage.setSingleLine(true);
		}

		TextView tvTitle,tvMessage;
		
		
	}
	
	
}
