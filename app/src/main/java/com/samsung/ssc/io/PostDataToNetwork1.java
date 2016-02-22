package com.samsung.ssc.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.util.Helper;

public class PostDataToNetwork1 extends AsyncTask<JSONObject, String, Object> {

	private String url;
	//private String pageurl;
	private Context context;
	private ProgressDialog progressDialog;
	private GetDataCallBack callBack;
	private boolean showDefultProcess = true;

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

	public PostDataToNetwork1(Context context, String message,
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

	public void setProcessVisible(boolean isVisible) {
		this.showDefultProcess = isVisible;
	}

	@Override
	protected Object doInBackground(JSONObject... params) {
		Object result = null;
		try {
			result = postMethod(url);

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

	public String postMethod(String conUrl) throws Exception {
	
		//String conUrl1 = "http://cybersvc2.samsungcsportal.com/cyberservice/csc/account/withoutm.jsp?locale_id=in_en&cid=in_mobileapp_gsssamsungcares_tracking_20150922";

		HttpClient httpclient = new DefaultHttpClient();
		URI u = new URI(conUrl);
		
		HttpPost httppost = new HttpPost(u);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 24000;

		HttpConnectionParams.setConnectionTimeout(httpParameters,timeoutConnection);
		
		// Set the default socket timeout (SO_TIMEOUT)  
		// in milliseconds which is the timeout for waiting for data. 
		int timeoutSocket = 24000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept","application/json, text/javascript, */*;q=0.01");
		HttpResponse response = httpclient.execute(httppost);
		Header[] cookies = response.getHeaders("Set-Cookie"); //semicolon-delimited keys and value
		
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < cookies.length; i++) {
			Header header = cookies[i];
			if (!"Set-Cookie".equals(header.getName())) {
				sb.append(header.getName()).append("=");
			}
			sb.append(header.getValue());
			if (i<cookies.length-1) {
				sb.append(";");
			}
		}
		//System.out.println(sb.toString());
		
		httpclient.getConnectionManager().shutdown();
		
		return sb.toString();

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

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);

		Helper.showCustomToast(context, R.string.security_exception,
				Toast.LENGTH_LONG);

	}
}
