package com.samsung.ssc;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.NotificationDTO;
import com.samsung.ssc.dto.NotificationData;

public class GCMBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent!=null) {
			String notificatinText = intent.getStringExtra("message");
			try {
				notificatinText = URLDecoder.decode(notificatinText, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e);
			}
			try {
				Gson gson = new Gson();
				NotificationDTO notificationsDto = gson.fromJson(
						notificatinText, NotificationDTO.class);
				if (notificationsDto.getNotificationType().equals("1"))//In case of General only By Dhiraj 29-Jan-2015
				{
					notificationsDto.setNotificationServiceID("-1");
				}
				generateNotification(context, notificationsDto);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	 
	private void generateNotification(final Context context,
			NotificationDTO notificationsDto) {
		try {
			final NotificationData notificationData = notificationsDto
					.getNotificationData();
			int icon = R.drawable.ic_launcher;
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			String title = context.getString(R.string.app_name);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context).setSmallIcon(icon).setContentTitle(title)
					.setContentText(notificationData.getTitle());

			long[] pattern = { 0, 1000, 50 };
			mBuilder.setVibrate(pattern);
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			mBuilder.setSound(alert);

			Intent viewDetailsNotificationIntent;
			/**
			 * If notifacation type is 8 then we have to move EOL notification
			 */
			if (notificationsDto.getNotificationType().equalsIgnoreCase("8")) {

				viewDetailsNotificationIntent = new Intent(context,
						EOLNotificationShow.class);

				
				// for scheme create type notification
				if (notificationsDto.getNotificationID().equals("1")) {
					
					viewDetailsNotificationIntent.putExtra("scheme_id",
							new JSONObject(notificationData.getBody().trim())
									.getInt("SchemeID"));


					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								DatabaseHelper.getConnection(context)
										.insertEOLScheemData(
												notificationData.getBody()
														.trim());
							} catch (JSONException e) {

								e.printStackTrace();
							}

						}
					});

					thread.start();

				}

				// for scheme update type notification
				else if (notificationsDto.getNotificationID().equals("2")) {
					
					viewDetailsNotificationIntent.putExtra("scheme_id",
							new JSONObject(notificationData.getBody().trim())
									.getInt("SchemeID"));

					

					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {

							try {

								int schemID = new JSONObject(notificationData
										.getBody().trim()).getInt("SchemeID");

								JSONArray array = new JSONArray();
								array.put(schemID);

								DatabaseHelper.getConnection(context)
										.deleteSchemes(array,false);

								DatabaseHelper.getConnection(context)
										.insertEOLScheemData(
												notificationData.getBody()
														.trim());

							} catch (JSONException e) {

								e.printStackTrace();
							}

						}
					});

					thread.start();

				}

				// for order type
				// notification
				else if (notificationsDto.getNotificationID().equals("3")) {
					
 
				}

				// scheme expire notification
				else if (notificationsDto.getNotificationID().equals("4")) {
					

					final JSONArray array = new JSONObject(notificationData
							.getBody().trim()).getJSONArray("SchemeIDs");

					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								DatabaseHelper.getConnection(context)
										.deleteSchemes(array,true);
							} catch (Exception e) {

								e.printStackTrace();
							}

						}
					});

					thread.start();
					
					return;

				}

			}

			else {
				viewDetailsNotificationIntent = new Intent(context,
						NotificationShow.class);
			}

			viewDetailsNotificationIntent.putExtra(
					IntentKey.NOTIFICATION_MESSAGE_TITLE, notificationData
							.getTitle().trim());
			viewDetailsNotificationIntent.putExtra(
					IntentKey.NOTIFICATION_MESSAGE_BODY, notificationData
							.getBody().trim());
			viewDetailsNotificationIntent.putExtra(
					IntentKey.NOTIFICATION_MESSAGE_TYPE,
					Integer.valueOf(notificationsDto.getNotificationType()));

			try {
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_ID,
						Integer.parseInt(notificationsDto.getNotificationID()));
			} catch (Exception e) {
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_ID, -1);

			}

			try {
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_SERVICE_ID, Integer
								.parseInt(notificationsDto
										.getNotificationServiceID()));
			} catch (Exception e) {
				viewDetailsNotificationIntent.putExtra(
						IntentKey.NOTIFICATION_SERVICE_ID, -1);

			}

			viewDetailsNotificationIntent.putExtra("FROM_GCM", true);

			Intent viewNotificationListIntent = new Intent(context,
					KPIListActivity.class);

			viewDetailsNotificationIntent
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Random generator = new Random();
			PendingIntent viewDetailsPendingIntent = PendingIntent.getActivity(
					context, generator.nextInt(),
					viewDetailsNotificationIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			PendingIntent viewNotificationsIntent = PendingIntent.getActivity(
					context, generator.nextInt(), viewNotificationListIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(viewDetailsPendingIntent);
			mBuilder.setAutoCancel(true);

			mBuilder.addAction(R.drawable.view_more, "View More",
					viewNotificationsIntent);
			mBuilder.addAction(R.drawable.view_details, "View Detail",
					viewDetailsPendingIntent);
			notificationManager.notify(generator.nextInt(), mBuilder.build());

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
