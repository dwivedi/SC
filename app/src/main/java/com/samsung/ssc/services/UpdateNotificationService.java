package com.samsung.ssc.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.NotificaitonsUpdateRequest;
import com.samsung.ssc.dto.NotificationData;
import com.samsung.ssc.util.Helper;

public class UpdateNotificationService extends IntentService {

	public UpdateNotificationService(String name) {
		super(name);
	}

	public UpdateNotificationService() {
		super("UpdateNotificationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		NotificaitonsUpdateRequest notificaitonsUpdateRequest = new NotificaitonsUpdateRequest(
				getApplicationContext());

		int serviceID = intent.getIntExtra(IntentKey.NOTIFICATION_SERVICE_ID,
				-1);
		int id = intent.getIntExtra(IntentKey.NOTIFICATION_ID, -1);

		ArrayList<NotificationData> notificationDatas = new ArrayList<NotificationData>();
		NotificationData data = new NotificationData(
				UpdateNotificationService.this);

		data.setNotificationServiceID(serviceID);
		data.setNotificationID(id);
		data.setReadStatus(1);

		notificationDatas.add(data);
		notificaitonsUpdateRequest.setNotificationDatas(notificationDatas);

		Gson gson = new Gson();
		String jsonString = gson.toJson(notificaitonsUpdateRequest);
		try {
			postMethod(getString(R.string.url) + "UpdateNotificationStatus",
					jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String postMethod(String conUrl, String nvps) throws Exception {
		String result = null;
		InputStream is = null;

		HttpClient httpclient = new DefaultHttpClient();
		URI u = new URI(conUrl);
		HttpPost httppost = new HttpPost(u);
		httppost.setHeader(WebConfig.WebParams.APIKEY, Helper.getStringValuefromPrefs(
				UpdateNotificationService.this, SharedPreferencesKey.PREF_APIKEY));
		httppost.setHeader(WebConfig.WebParams.APITOKEN, Helper.getStringValuefromPrefs(
				UpdateNotificationService.this, SharedPreferencesKey.PREF_APITOKEN));
		
		if (!Helper.getStringValuefromPrefs(UpdateNotificationService.this, SharedPreferencesKey.PREF_USERID)
				.equals("")) {
    		
    		httppost.setHeader(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(UpdateNotificationService.this, SharedPreferencesKey.PREF_USERID));
			
		} else {
			httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
		}
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");
		httppost.setEntity(new ByteArrayEntity(nvps.getBytes("UTF8")));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		
		if (entity != null) {
			is = entity.getContent();
			result = getStringfromInputStream(is);
			entity.consumeContent();
		}
		httpclient.getConnectionManager().shutdown();
		return result;

	}

	private String getStringfromInputStream(InputStream is) {

		String result = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Helper.printStackTrace(e);
			Helper.printLog("Loading Runnable Error converting result DM",
					e.toString());
		}
		return result;

	}

}
