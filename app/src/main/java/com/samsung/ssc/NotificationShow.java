package com.samsung.ssc;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.activitymodule.FeedbackDetailActivity;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.dto.NotificationType;
import com.samsung.ssc.services.UpdateNotificationService;
import com.samsung.ssc.util.Helper;

public class NotificationShow extends BaseActivity {
	private TextView titlenotification;
	private TextView bodynotification;
	private Button exit;
	private Button viewMore;
	private boolean fromGCM = false;
	private int notificationServiceID = 0;
	private int notificationId = 0;
	private int type;
	private int readStatus;
	private String messagetitle;
	private String body;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notification_view1);
		setCurrentContext(NotificationShow.this);
		titlenotification = (TextView) findViewById(R.id.titlenotification);
		bodynotification = (TextView) findViewById(R.id.bodynotification);

		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(this);

		viewMore = (Button) findViewById(R.id.viewMore);
		viewMore.setOnClickListener(this);

		getValuesFromIntent();

		if (fromGCM) {
			viewMore.setVisibility(View.VISIBLE);
		} else {
			viewMore.setVisibility(View.GONE);
		}

		 
		if (readStatus!=1) {
			updateReadStatusOnServer();
		}
		try {
			NotificationType notificationType = NotificationType.FMS;
			if (type != -1 && type == notificationType.getNotificationType()) {
				hyperLinkIdsInTextView(messagetitle, body);
			} else {
				titlenotification.setText(messagetitle);
				bodynotification.setText(Html.fromHtml(body));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getValuesFromIntent() {
		Intent receivedIntent = getIntent();

		this.type = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_MESSAGE_TYPE, -1);
		this.fromGCM = receivedIntent.getBooleanExtra("FROM_GCM", false);
		this.readStatus = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_READ_STATUS, -1);
		this.notificationServiceID = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_SERVICE_ID, -1);
		this.messagetitle = receivedIntent
				.getStringExtra(IntentKey.NOTIFICATION_MESSAGE_TITLE);
		this.body = receivedIntent
				.getStringExtra(IntentKey.NOTIFICATION_MESSAGE_BODY);
		this.notificationId = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_ID, -1);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.exit:
			handleExitClick();
			break;
		case R.id.viewMore:
			
			handleViewMore();
			break;
		default:
			break;
		}
	}

	private void handleViewMore() {


		if (!Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)
				.equalsIgnoreCase("")) {
			try {
				 
				Intent intent = new Intent(NotificationShow.this, KPIListActivity1.class);
				startActivity(intent);
				finish();

			} catch (NumberFormatException e) {
			
				Helper.showCustomToast(getApplicationContext(), R.string.user_id_not_in_format, Toast.LENGTH_LONG);
			}
		}else{

			Helper.showCustomToast(getApplicationContext(), R.string.you_are_logged_out, Toast.LENGTH_LONG);
		}
	
		
	}

	private void handleExitClick() {
		finishActivity();
	}

	protected void finishActivity() {
		if (fromGCM) {
			finish();
		} else {
			if (notificationServiceID != -1 || notificationServiceID != 0) {
				Intent resultIntent = new Intent();
				resultIntent.putExtra(IntentKey.NOTIFICATION_SERVICE_ID,
						notificationServiceID);
				resultIntent.putExtra(IntentKey.NOTIFICATION_MESSAGE_TYPE,
						this.type);
				setIntent(resultIntent);

				finish();
			}
		}
	}

	private void updateReadStatusOnServer() {

		if (!Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)
				.equalsIgnoreCase("")) {
			try {
				 
				Intent updateServiceIntent = new Intent(NotificationShow.this,
						UpdateNotificationService.class);
				updateServiceIntent.putExtra(IntentKey.NOTIFICATION_SERVICE_ID,
						notificationServiceID);
				updateServiceIntent.putExtra(IntentKey.NOTIFICATION_ID,
						notificationId);
				startService(updateServiceIntent);

			} catch (NumberFormatException e) {
				
				Helper.showCustomToast(getApplicationContext(), R.string.user_id_not_in_format, Toast.LENGTH_SHORT);
				
			}
		}else{

		
			
			Helper.showCustomToast(getApplicationContext(), R.string.you_are_logged_out, Toast.LENGTH_SHORT);
			
			
		}
	}

	@Override
	public void onBackPressed() {
		finishActivity();
	}

	private void hyperLinkIdsInTextView(String messageTitle, String body) {
		// hide the scroll view and un hide the
		titlenotification.setText(messageTitle);
		String message = body;

		if (fromGCM) {
			// as it is sending un wanted html tags
			//message = Html.fromHtml(body).toString();
			
			// Html.fromHtml(body) causing </b> tag to be removed so using 'replace' method
			message=body.replace("&nbsp;", " "); //&nbsp; used in html for space
			
		}
		
		
		final ArrayList<String> hyperLinkIds = new ArrayList<String>();

		String hyperLinksArray[] = message.split("<b>");
		for (String str : hyperLinksArray) {
			if (str.contains("</b>")) {
				str = str.substring(0, str.indexOf("</b>"));
				hyperLinkIds.add(str);
			}
		}

		message = message.replaceAll("<b>", " ");
		message = message.replaceAll("</b>", " ");
		message = message.trim();

		int startIndex = 0;
		String arrToRemove[] = new String[hyperLinkIds.size()];
		int i = 0;
		for (final String str : hyperLinkIds) {
			startIndex = message.indexOf(str, startIndex);
			arrToRemove[i] = "," + str.split(",")[1];
			i++;
		}

		for (int j = 0; j < arrToRemove.length; j++) {
			message = message.replace(arrToRemove[j], "");
		}
		startIndex = 0;
		SpannableString ss = new SpannableString(message);
		for (final String str : hyperLinkIds) {
			startIndex = message.indexOf(str.split(",")[0].trim(), startIndex);
			ss.setSpan(new ClickableSpan() {
				public void onClick(View view) {
					

					


					if (!Helper.getStringValuefromPrefs(view.getContext(), SharedPreferencesKey.PREF_USERID)
							.equalsIgnoreCase("")) {
						try {
							String serviceId = str.split(",")[1].trim();
							Intent intent = new Intent(NotificationShow.this,
									FeedbackDetailActivity.class);
							intent.putExtra("FeedbackID", Long.valueOf(serviceId));
							startActivity(intent);

						} catch (NumberFormatException e) {
						
							Helper.showCustomToast(getApplicationContext(), R.string.user_id_not_in_format, Toast.LENGTH_SHORT);
						}
					}else{

						Helper.showCustomToast(getApplicationContext(), R.string.you_are_logged_out, Toast.LENGTH_SHORT);
					}
				

					
				
					

				}
			}, startIndex, (startIndex + (str.split(",")[0].trim()).length()),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		bodynotification.setText(ss);
		bodynotification.setClickable(true);
		bodynotification.setMovementMethod(LinkMovementMethod.getInstance());

		// check for sdk version 4.3
		if (Build.VERSION.SDK_INT == 18) {
			bodynotification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					


					if (!Helper.getStringValuefromPrefs(view.getContext(), SharedPreferencesKey.PREF_USERID)
							.equalsIgnoreCase("")) {
						try {
							
							Intent intent = new Intent(NotificationShow.this,
									ServiceIdList.class);
							intent.putExtra("notification_list",
									(String[]) hyperLinkIds
											.toArray(new String[hyperLinkIds.size()]));
							startActivity(intent);

						} catch (NumberFormatException e) {
							
							
							Helper.showCustomToast(getApplicationContext(), R.string.user_id_not_in_format, Toast.LENGTH_SHORT);
						}
					}else{

						Helper.showCustomToast(getApplicationContext(), R.string.you_are_logged_out, Toast.LENGTH_SHORT);
					}
				

					
				}
			});
		}

	}
}
