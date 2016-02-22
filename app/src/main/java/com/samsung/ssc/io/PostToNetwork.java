package com.samsung.ssc.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.util.Helper;

public class PostToNetwork {

	Context mContext;

	public static String postMethod(String conUrl, JSONObject nvps,
			Context context) throws Exception {

		String result = null;
		InputStream is = null;

		HttpClient httpclient = new DefaultHttpClient();
		URI u = new URI(conUrl);
		HttpPost httppost = new HttpPost(u);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 24000;

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);

		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 24000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		httppost.setHeader(WebConfig.WebParams.APIKEY, Helper
				.getStringValuefromPrefs(context.getApplicationContext(),
						SharedPreferencesKey.PREF_APIKEY));
		httppost.setHeader(WebConfig.WebParams.APITOKEN, Helper
				.getStringValuefromPrefs(context.getApplicationContext(),
						SharedPreferencesKey.PREF_APITOKEN));

		if (!Helper.getStringValuefromPrefs(context,
				SharedPreferencesKey.PREF_USERID).equals("")) {

			httppost.setHeader(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(context,
							SharedPreferencesKey.PREF_USERID));

		} else {
			httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
		}

		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");
		httppost.setEntity(new ByteArrayEntity(nvps.toString().getBytes("UTF8")));

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

	public static String postMethod(String conUrl, JSONObject nvps,
			Context context, Bundle bundle) throws Exception {

		String result = null;
		InputStream is = null;

		HttpClient httpclient = new DefaultHttpClient();
		URI u = new URI(conUrl);
		HttpPost httppost = new HttpPost(u);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 24000;

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);

		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 24000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);


		httppost.setHeader(WebConfig.WebParams.APIKEY,
				bundle.getString(SharedPreferencesKey.PREF_APIKEY));

		httppost.setHeader(WebConfig.WebParams.APITOKEN,
				bundle.getString(SharedPreferencesKey.PREF_APITOKEN));

		if (!bundle.getString(SharedPreferencesKey.PREF_USERID).equals("")) {

			httppost.setHeader(WebConfig.WebParams.USER_ID,
					bundle.getString(SharedPreferencesKey.PREF_USERID));

		} else {
			httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
		}

		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");
		httppost.setEntity(new ByteArrayEntity(nvps.toString().getBytes("UTF8")));

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

	private static String getStringfromInputStream(InputStream is) {

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
		}
		return result;

	}

}
