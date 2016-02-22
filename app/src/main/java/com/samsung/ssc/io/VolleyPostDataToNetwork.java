package com.samsung.ssc.io;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.util.Helper;

public class VolleyPostDataToNetwork {

	private String url;
	private String pageurl;
	private Context context;
	private ProgressDialog progressDialog;
	private VolleyGetDataCallBack callBack;
	private JSONObject jsonData;
	private boolean showDefultProcess = true;

	protected void setProgresDialogProperties(String message) {

		if (showDefultProcess) {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				/*
				 * progressDialog = new ProgressDialog(context,
				 * ProgressDialog.THEME_HOLO_LIGHT);
				 */

				progressDialog = SSCProgressDialog.ctor(context);

			} else {
				progressDialog = new ProgressDialog(context);
				progressDialog.setProgress(0);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage(message);
				progressDialog.setCancelable(false);
			}
		}
	}

	public VolleyPostDataToNetwork(Context context, String message,
			VolleyGetDataCallBack callBack) {
		this.callBack = callBack;
		this.context = context;
		setProgresDialogProperties(message);
	}

	public void setConfig(String url, String pageurl) {
		this.url = url;
		this.pageurl = pageurl;
	}

	public void setRequestData(JSONObject data) {
		jsonData = data;
	}

	public void callWebService() {

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.POST, url + pageurl, jsonData,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						sendResponse(jsonObject);
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						callBack.onError(error);
						try {
							if (progressDialog != null) {
								progressDialog.dismiss();
								progressDialog = null;
							}
						} catch (Exception e) {
							Helper.printStackTrace(e);
						}

					}
				}

		) {

			@Override
			public String getBodyContentType() {
				return "application/json; charset=utf-8";
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> params = new HashMap<String, String>();

				params.put(WebConfig.WebParams.APIKEY, Helper
						.getStringValuefromPrefs(context,
								SharedPreferencesKey.PREF_APIKEY));
				params.put(WebConfig.WebParams.APITOKEN, Helper
						.getStringValuefromPrefs(context,
								SharedPreferencesKey.PREF_APITOKEN));

				if (!Helper.getStringValuefromPrefs(context,
						SharedPreferencesKey.PREF_USERID).equals("")) {

					params.put(WebConfig.WebParams.USER_ID, Helper
							.getStringValuefromPrefs(context,
									SharedPreferencesKey.PREF_USERID));

				} else {
					params.put(WebConfig.WebParams.USER_ID, "0");
				}

				return params;
			}

		};

		// remove caching
		jsObjRequest.setShouldCache(false);
		// Wait 30 seconds and don't retry more than once
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Access the RequestQueue through your singleton class.
		VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);

		if (showDefultProcess) {

			progressDialog.show();
		}

	}

	private void sendResponse(JSONObject jsonObject )
	{
		callBack.processResponse(jsonObject);
		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

	}
	
	public void setProcessVisible(boolean isVisible) {
		this.showDefultProcess = isVisible;
	}
}
