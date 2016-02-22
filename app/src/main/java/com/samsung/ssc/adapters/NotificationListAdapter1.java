package com.samsung.ssc.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.NotificationData;

public class NotificationListAdapter1 extends BaseAdapter {

	private ArrayList<NotificationData> notificationDatas;
	private LayoutInflater inflater;

	public NotificationListAdapter1(
			ArrayList<NotificationData> al_notificationDatas, Context context,
			int type) {
		super();
		this.notificationDatas = al_notificationDatas;
		this.inflater = LayoutInflater.from(context);

		Collections.sort(notificationDatas, new NotificationDataComparator());

		int size = notificationDatas.size();
		ArrayList<NotificationData> temp_al_notificationDatas = new ArrayList<NotificationData>();
		Date tempDate = null;
		for (int i = 0; i < size; i++) {
			NotificationData data = notificationDatas.get(i);
			try {
				if (tempDate == null) {

					NotificationData tempData = (NotificationData) data.clone();
					tempData.setHeaderType(true);
					temp_al_notificationDatas.add(tempData);
					tempDate = getOnlyDate(data.getNotificationDate());
				} else if (!tempDate.equals(getOnlyDate(data
						.getNotificationDate()))) {
					NotificationData tempData = (NotificationData) data.clone();
					tempData.setHeaderType(true);
					temp_al_notificationDatas.add(tempData);
					tempDate = getOnlyDate(data.getNotificationDate());
				}

			} catch (Exception e) {

				e.printStackTrace();
			} 

			data.setHeaderType(false);
			temp_al_notificationDatas.add(data);

		}

		notificationDatas.clear();
		notificationDatas = temp_al_notificationDatas;

	}

	// public void setNotificationDatas(
	// ArrayList<NotificationData> notificationDatas) {
	// this.notificationDatas = notificationDatas;
	// }

	@Override
	public int getCount() {
		return notificationDatas.size();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public NotificationData getItem(int position) {

		return notificationDatas.get(position);
	}

	

	@Override
	public int getItemViewType(int position) {

		return notificationDatas.get(position).isHeaderType() ? 1 : 0;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderHeader holderHeader;
		ViewHolderItem holderItem;

		final NotificationData item = notificationDatas.get(position);

		if (1 == getItemViewType(position)) {

			try {
				if (convertView == null) {

					convertView = inflater.inflate(
							R.layout.notifications_list_item_header, parent,
							false);
					holderHeader = new ViewHolderHeader(convertView);
					convertView.setTag(holderHeader);

				} else {
					holderHeader = (ViewHolderHeader) convertView.getTag();
				}

				String st_day = getDayFormDate(item.getNotificationDate());
				if (st_day != null) {
					holderHeader.tv_header.setText(st_day);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		} else {

			try {
				if (convertView == null) {

					convertView = inflater.inflate(
							R.layout.notifications_list_item1, parent, false);
					holderItem = new ViewHolderItem(convertView);
					convertView.setTag(holderItem);

				} else {
					holderItem = (ViewHolderItem) convertView.getTag();
				}

				holderItem.tvTitle.setText(item.getPushNotificationMessage()
						.split("~")[0]);

				String st_time = getTimeFormDate(item.getNotificationDate());
				if (st_time != null) {
					holderItem.tvTime.setText(st_time);
				}

				if (item.getReadStatus() == 0) {
					holderItem.tvTitle.setTypeface(null, Typeface.BOLD);
					 convertView.setBackgroundResource(android.R.color.white);
				} else {
					holderItem.tvTitle.setTypeface(null, Typeface.NORMAL);
					 convertView.setBackgroundResource(R.color.dark_grey);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	

		return convertView;
	}

	class ViewHolderItem {
		public ViewHolderItem(View convertView) {

			tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title_notificationListItem);
			tvTime = (TextView) convertView
					.findViewById(R.id.tv_time_notificationListItem);

		}

		TextView tvTitle, tvTime;

	}

	class ViewHolderHeader {

		TextView tv_header;

		public ViewHolderHeader(View convertView) {

			tv_header = (TextView) convertView
					.findViewById(R.id.tv_header_notificationListItem);

		}

	}

	class NotificationDataComparator implements Comparator<NotificationData> {
		@Override
		public int compare(NotificationData o1, NotificationData o2) {

			try {
				DateFormat dateFormat = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm:ss");
				Date date1 = dateFormat.parse(o1.getNotificationDate());
				Date date2 = dateFormat.parse(o2.getNotificationDate());

				if (date1.after(date2)) {
					return -1;
				} else if (date1.before(date2)) {
					return 1;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	public Date getOnlyDate(String st_date) {

		Date req_date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date temp_date = dateFormat.parse(st_date);
			String newDateString = new SimpleDateFormat("dd/MM/yyyy")
					.format(temp_date);
			DateFormat req_dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			req_date = req_dateFormat.parse(newDateString);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return req_date;
	}

	public String getDayFormDate(String st_date) {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = null;
		String newDateString = null;
		try {
			date = dateFormat.parse(st_date);
			newDateString = new SimpleDateFormat("d MMMMM yyyy").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return newDateString;
	}

	public String getTimeFormDate(String st_date) {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = null;
		String newTimeString = null;
		try {
			date = dateFormat.parse(st_date);
			newTimeString = new SimpleDateFormat("HH:mm:ss").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return newTimeString;
	}

}
