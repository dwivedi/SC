package com.samsung.ssc.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.util.Helper;

public class PostDataToNetwork2 extends AsyncTask<JSONObject, String, Object> {

	private String url;
	//private String pageurl;
	private Context context;
	private ProgressDialog progressDialog;
	private GetDataCallBack callBack;
	private boolean showDefultProcess = true;
	private String cookies;

	protected void setProgresDialogProperties(String message) {

		if (showDefultProcess) {
			try {
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
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setMessage(message);
					progressDialog.setCancelable(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public PostDataToNetwork2(Context context, String message,
			GetDataCallBack callBack) {
		this.callBack = callBack;
		this.context = context;
		setProgresDialogProperties(message);
	}

	public void setConfig(String url) {
		this.url = url;
	//	this.pageurl = pageurl;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (showDefultProcess) {
			progressDialog.show();
		}
	}
	
	public void setCookies(String cookies) {
		// TODO Auto-generated method stub
		this.cookies = cookies;
	}
	

	public void setProcessVisible(boolean isVisible) {
		this.showDefultProcess = isVisible;
	}

	@Override
	protected Object doInBackground(JSONObject... params) {
		Object result = null;
		try {
			result = postMethod(url , params[0]);
			

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

	public String postMethod(String conUrl, JSONObject nvps) throws Exception {

		String result = null;
		InputStream is = null;
		
		HttpClient httpclient = new DefaultHttpClient();
		//String conUrl1 = "http://cybersvc2.samsungcsportal.com/cyberservice/csc/account/withoutm_search_list.jsp";
		URI u = new URI(conUrl);
		
		HttpPost httppost = new HttpPost(u);
		
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 24000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,timeoutConnection);
		int timeoutSocket = 24000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httppost.setHeader("Cookie", cookies);
		httppost.setHeader("Content-Type","application/x-www-form-urlencoded");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");

		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("sdate", nvps.getString("sdate")));
			nameValuePairs.add(new BasicNameValuePair("edate", nvps.getString("edate")));
			nameValuePairs.add(new BasicNameValuePair("repairnum", nvps.getString("repairnum")));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpResponse response = httpclient.execute(httppost);
 		String res = EntityUtils.toString(response.getEntity());
 
		return res;
	}
	
	public String getStringfromInputStream(InputStream is) {

		String result = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
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

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);

		Helper.showCustomToast(context, R.string.security_exception,
				Toast.LENGTH_LONG);
	}
  }
