package com.samsung.ssc.reports;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class DashboardReports extends BaseActivity {

	private WebView webView;
	private ProgressBar progress;
	private String url;
	private Button close;

	@Override
	public void init() {

		super.init();
		setContentView(R.layout.dashboard_report);
		setCurrentContext(DashboardReports.this);

		webView = (WebView) this.findViewById(R.id.myWebView);
		progress = (ProgressBar) findViewById(R.id.progressBarreport);
		close = (Button) findViewById(R.id.close);
		close.setOnClickListener(DashboardReports.this);
		webView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});

		getReportLink();

	}

	private void getReportLink() {

		PostDataToNetwork pdn = new PostDataToNetwork(DashboardReports.this,
				getStringFromResource(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						if (result != null) {

							ResponseDto rsponce = new FetchingdataParser(
									DashboardReports.this)
									.getResponseResult(result.toString());

							if (rsponce.isSuccess()) {
								TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
								String iemi = mTelephonyMgr.getDeviceId()
										.toString();
 
								String apiKey = Helper.getStringValuefromPrefs(
										getApplicationContext(),
										SharedPreferencesKey.PREF_APIKEY);
								String apiToken = Helper
										.getStringValuefromPrefs(
												getApplicationContext(),
												SharedPreferencesKey.PREF_APITOKEN);

								url = rsponce.getSingleResult().toString()
										+ iemi + "&APIKey=" + apiKey + "&APIToken="
										+ apiToken;

								if (url != null) {
									loadPage(url);
								}

							} else {
						 
								
								Helper.showAlertDialog(DashboardReports.this, SSCAlertDialog.ERROR_TYPE, getString(R.string.error7), rsponce.getMessage(), getString(R.string.ok), new SSCAlertDialog.OnSDAlertDialogClickListener() {
									
									@Override
									public void onClick(SSCAlertDialog sdAlertDialog) {
										sdAlertDialog.dismiss();
										finish();
										
									}
								}, false, null, null);
							}
						} else {
							
							
							
							Helper.showCustomToast(DashboardReports.this, R.string.networkserverbusy, Toast.LENGTH_LONG);
							
						}

					}

				});
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(DashboardReports.this,
							SharedPreferencesKey.PREF_USERID)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		pdn.setConfig(getStringFromResource(R.string.url),
				"GetReportDashboardURL");
		pdn.execute(jsonobj);

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void loadPage(String url) {

		webView.setWebViewClient(new MyWebViewClient());

		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setBuiltInZoomControls(true);
		setting.setBuiltInZoomControls(true);

		webView.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView view, int prog) {

				progress.setProgress(prog * 100);

			}
		});

		webView.loadUrl(url);

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progress.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progress.setProgress(100);
			progress.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.close:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack() == true) {
			webView.goBack();
		} else {
			finish();
		}
	}
}
