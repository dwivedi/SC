package com.samsung.ssc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

import com.samsung.ssc.LMS.LMSListActivity;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.LoginResponse;
import com.samsung.ssc.util.Helper;

public class Announcement extends Activity {
	
	//private TextView announcementTxt;
	private LoginResponse mLoginResponse;
	private WebView announcementWebView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


			requestWindowFeature(Window.FEATURE_NO_TITLE);



			setContentView(R.layout.announcement_activity);
			//announcementTxt = (TextView)findViewById(R.id.announcementTxt);
			announcementWebView = (WebView)findViewById(R.id.announcementWV);

			Bundle extras = getIntent().getExtras();
			mLoginResponse = (LoginResponse)extras.getSerializable(IntentKey.LOGIN_DATA);
			String announcementString;
			if(mLoginResponse.isHasNewAnnouncment())
			{
				Helper.setAnnouncement(getApplicationContext(), mLoginResponse.getAnnouncementMessage());
				announcementString=mLoginResponse.getAnnouncementMessage();

			}
			else
			{
				announcementString=Helper.getAnnouncement(getApplicationContext());

			}
			//<img src="/SmartDost_Ce_Dev/
			//<img src="http://107.110.101.188/SmartDost_Ce_Dev/
			// <img src="http://107.110.101.188/SmartDost_Ce_Dev/
			//announcementString=announcementString.replace("<img src=\"/SmartDost_Ce_Dev/", "<img src=\"http://107.110.101.188/SmartDost_Ce_Dev/");
			announcementString=announcementString.replace(getResources().getString(R.string.webview_image_from_replace).trim(),getResources().getString(R.string.webview_image_to_url).trim());


			//announcementTxt.setText(Html.fromHtml(announcementString));

		/*
		 * Comment for samsung ace user webview.db and webviewCache.db
		 *
		 */

			// Announcement.this.deleteDatabase("webview.db");
			// Announcement.this.deleteDatabase("webviewCache.db");
			announcementWebView.clearHistory();
			announcementWebView.clearCache(true);
			announcementWebView.getSettings().setJavaScriptEnabled(true);
			announcementWebView.getSettings().setAllowFileAccess(true);

			announcementWebView.loadData(announcementString, "text/html", "UTF-8");

			Button ok = (Button)findViewById(R.id.okBtn);
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					moveToNextActivity(mLoginResponse);
					finish();

				}
			});

	}


	

	
	private void moveToNextActivity(LoginResponse mLoginResponse) {

 	/*	if (mLoginResponse.isAttendanceMarked()) {
			String attendanceType = mLoginResponse.getAttendanceType();
			if (attendanceType.equals("1")) {
				
				Helper.saveIntgerValueInPrefs(this,SharedPreferencesKey.PREF_MARK_ATTENDANCE, 1);
			    startActivity(new Intent(this, MainMenuActivityListGrid.class));
				finish();
				
				
				
			} else if (attendanceType.equals("2")) {
				Helper.saveIntgerValueInPrefs(this,
						SharedPreferencesKey.PREF_MARK_ATTENDANCE, 0);
			 
				moveAttendanceActivity(mLoginResponse);

			}
 			
 			
		} else {
			 
			moveAttendanceActivity(mLoginResponse);
		}*/
		
		
		if (mLoginResponse.isAttendanceMandate()) {
			if (mLoginResponse.isAttendanceMarked()) {
				String attendanceType = mLoginResponse.getAttendanceType();
				if (attendanceType.equals("1")) {

					moveToMainMenuActivity();

				} else if (attendanceType.equals("2")) {
					Helper.saveIntgerValueInPrefs(this,
							SharedPreferencesKey.PREF_MARK_ATTENDANCE, 0);

					moveAttendanceActivity(mLoginResponse);

				}

			} else {

				moveAttendanceActivity(mLoginResponse);
			}
		} else {

			if (Helper.getBoolValueFromPrefs(this,
					SharedPreferencesKey.PREF_IS_DATE_CHANGED)) {
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						DatabaseHelper.getConnection(getApplicationContext())
								.clearCompletedDataFramResponseTables();

						Helper.saveBoolValueInPrefs(Announcement.this,
								SharedPreferencesKey.PREF_IS_DATE_CHANGED,
								false);
						
						moveToMainMenuActivity();

					}
				});

			} else {
				moveToMainMenuActivity();
			}

		}
		

	}

	private void moveToMainMenuActivity() {
		Helper.saveIntgerValueInPrefs(this,
				SharedPreferencesKey.PREF_MARK_ATTENDANCE, 1);
		startActivity(new Intent(this, MainMenuActivityListGrid.class));


		finish();
	}
	
	private void moveAttendanceActivity(LoginResponse mLoginResponse) {
		
		Intent intent = new Intent(this, AttendanceActivity.class);
		Bundle extras = new Bundle();
		extras.putString("USER_NAME", mLoginResponse
				.getFirstName()
				+ " "
				+ mLoginResponse.getLastName());
		extras.putString("USER_CONTACT_NUMBER", mLoginResponse
				.getMobileCalling());
		intent.putExtras(extras);
		startActivity(intent);
		
	}
	
	@Override
	public void onBackPressed() {
		
		
	}
	
} 