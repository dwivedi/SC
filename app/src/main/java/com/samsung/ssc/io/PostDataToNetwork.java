package com.samsung.ssc.io;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.util.Helper;

import org.json.JSONObject;

public class PostDataToNetwork extends AsyncTask<JSONObject, String, Object> {

	private String url;
	private String pageurl;
	private Context context;
	private ProgressDialog progressDialog;
	private GetDataCallBack callBack;

	protected void setProgresDialogProperties(String message) {

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



	public PostDataToNetwork(Context context, String message,
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

		try {
				progressDialog.show();

		}catch (Exception e)
		{

		}
	}
/*
	public void setProcessVisible(boolean isVisible) {
		this.showDefultProcess = isVisible;
	}*/

	@Override
	protected Object doInBackground(JSONObject... params) {
		Object result = null;
		try {

			result = PostToNetwork
					.postMethod(url + pageurl, params[0], context);

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

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);

		Helper.showCustomToast(context, R.string.security_exception,
				Toast.LENGTH_LONG);

	}
}
