package com.samsung.ssc;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.samsung.ssc.adapters.NotificationListAdapter1;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.dto.GetNotificationResponse;
import com.samsung.ssc.dto.GetNotificationsRequest;
import com.samsung.ssc.dto.NotificationData;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class NotificationsListFragment2 extends BaseActivity implements
		GetDataCallBack, OnClickListener, OnCheckedChangeListener {

	private ListView listView;
	private Button loadMoreButton;
	private ArrayList<NotificationData> notificationList = new ArrayList<NotificationData>();
	private int notificationType;
	private String kasName;

	OnItemClickListener mListViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			try {

				// if the item is a header type, do nothing
				if (view.findViewById(R.id.tv_header_notificationListItem) != null) {
					return;
				}

				NotificationData data = (NotificationData) parent
						.getItemAtPosition(position);

				Intent viewDetailsNotificationIntent;

				// for sales notification
				if (notificationType != -1 && notificationType == 8) {
					viewDetailsNotificationIntent = new Intent(
							NotificationsListFragment2.this,
							EOLNotificationShow.class);

					viewDetailsNotificationIntent
							.putExtra(IntentKey.NOTIFICATION_ID,
									data.getNotificationID());

					viewDetailsNotificationIntent
							.putExtra(IntentKey.NOTIFICATION_MESSAGE_BODY,
									data.getBody());

				} else {
					viewDetailsNotificationIntent = new Intent(
							NotificationsListFragment2.this,
							NotificationShow.class);
				}

				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_MESSAGE_TITLE, data
								.getPushNotificationMessage().split("~")[0]);
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_MESSAGE_BODY, data
								.getPushNotificationMessage().split("~")[1]);
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_MESSAGE_TYPE,
						data.getNotificationType());
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_SERVICE_ID,
						data.getNotificationServiceID());
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_READ_STATUS,
						data.getReadStatus());

				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_MESSAGE_TYPE, notificationType);

				NotificationsListFragment2.this.startActivityForResult(
						viewDetailsNotificationIntent, 001);
			} catch (Exception e) {

				return;
			}

		}

	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.notifications_list_layout2);
		listView = (ListView) findViewById(R.id.notificationsListView);
		listView.setOnItemClickListener(mListViewItemClickListener);

		loadMoreButton = (Button) findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(this);

		notificationType = getIntent().getByteExtra(
				IntentKey.NOTIFICATION_MESSAGE_TYPE, (byte) -1);

		kasName = getIntent().getStringExtra(IntentKey.KAS_NAME);

		TextView tv_name = (TextView) findViewById(R.id.tv_title_sdActionBar);

		tv_name.setText(kasName + " " + getString(R.string.kas));

		getNotifications(0, 10);

	}

	private int getSmallestServiceId() {
		int notificationServiceID = notificationList.get(
				notificationList.size() - 1).getNotificationServiceID();
		/*
		 * for (NotificationData data : notificationList) { if
		 * (data.getNotificationServiceID() <= notificationServiceID) {
		 * notificationServiceID = data.getNotificationServiceID(); } }
		 */

		return notificationServiceID;
	}

	private void getNotifications(int notificationID, int rowCounter) {
		PostDataToNetwork dataToNetwork = new PostDataToNetwork(
				NotificationsListFragment2.this, "Loading...", this);
		dataToNetwork.setConfig(getString(R.string.url), "GetNotifications");
		GetNotificationsRequest getNotificationsRequest = new GetNotificationsRequest(
				getApplicationContext());
		getNotificationsRequest.setRowCounter(rowCounter);
		getNotificationsRequest.setNotificationType(notificationType);
		getNotificationsRequest.setLastNotificationServiceID(notificationID);
		Gson gson = new Gson();
		String jsonString = gson.toJson(getNotificationsRequest);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			dataToNetwork.execute(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void processResponse(Object result) {
		if (result != null) {
			String jsonResult = result.toString();
			if (!TextUtils.isEmpty(jsonResult)) {
				Gson gson = new Gson();

				try {
					GetNotificationResponse getNotificationResponse = (GetNotificationResponse) gson
							.fromJson(jsonResult, GetNotificationResponse.class);
					if (getNotificationResponse.isSuccess()) {
						ArrayList<NotificationData> arrayList = getNotificationResponse
								.getNotificationDatas();

						if (arrayList.size() != 0) {

							notificationList.addAll(arrayList);
							populateListView((ArrayList<NotificationData>) notificationList
									.clone());

						} else {

							Helper.showCustomToast(getApplicationContext(),
									R.string.no_data_received,
									Toast.LENGTH_SHORT);

						}
					} else {

						Helper.showCustomToast(getApplicationContext(),
								getNotificationResponse.getMessage(),
								Toast.LENGTH_SHORT);

					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	private void populateListView(ArrayList<NotificationData> notificationDatas) {
		NotificationListAdapter1 adapter = new NotificationListAdapter1(
				notificationDatas, NotificationsListFragment2.this,
				notificationType);
		clearListView();
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	// to clear the listView
	private void clearListView() {
		listView.setAdapter(null);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.loadMoreButton:
			handleLoadMore();
			break;
		default:
			break;
		}
	}

	private void handleLoadMore() {
		if (notificationList != null && notificationList.size() > 0) {
			int notificationID = getSmallestServiceId();
			getNotifications(notificationID, 10);
		} else {
			getNotifications(0, 10);
		}
	}

	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		int size = notificationList.size();
		notificationList.clear();
		getNotifications(0, size);
	}
}