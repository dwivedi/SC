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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.util.Helper;

/**
 * 
 * @author Nitin
 * 
 */
public class PostDataToNetworkImage extends AsyncTask<byte[], String, Object> {
	private String url;
	private String pageurl;
	private Context context;
	private ProgressDialog progressDialog;
	private GetDataCallBack callBack;

	protected void setProgresDialogProperties(String message) {

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			/*progressDialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);*/
			
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

	public PostDataToNetworkImage(Context context, String message,
			GetDataCallBack callBack) {
		this.callBack = callBack;
		this.context = context;
		setProgresDialogProperties(message);
	}

	public void setConfig(String url, String pageurl) {
		this.url = url;
		this.pageurl = pageurl;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		progressDialog.show();
	}

	@Override
	protected Object doInBackground(byte[]... params) {
		Object result = null;
		try {
			result = postMethod(url + pageurl, params[0]);

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		return result;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		callBack.processResponse(result);
		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
	}

	public String postMethod(String conUrl, byte[] nvps) throws Exception {
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
				Helper.getStringValuefromPrefs(context, SharedPreferencesKey.PREF_APIKEY));
		httppost.setHeader(WebConfig.WebParams.APITOKEN, Helper.getStringValuefromPrefs(
				context, SharedPreferencesKey.PREF_APITOKEN));

	    
    	if (!Helper.getStringValuefromPrefs(context, SharedPreferencesKey.PREF_USERID)
				.equals("")) {
    		
    		httppost.setHeader(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(context, SharedPreferencesKey.PREF_USERID));
			
		} else {
			httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
		}
		httppost.setHeader("Content-Type", "image/jpeg");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");
		httppost.setEntity(new ByteArrayEntity(nvps));
		

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

	public String getStringfromInputStream(InputStream is) {

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
