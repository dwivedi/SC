package com.samsung.ssc.activitymodule;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;

public class SchemeDetailActivity extends BaseActivity {

	private WebView webView;
	private Button close;
	private ProgressBar progress;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		setContentView(R.layout.dashboard_report);

		webView = (WebView) findViewById(R.id.myWebView);
		progress = (ProgressBar) findViewById(R.id.progressBarreport);
		close = (Button) findViewById(R.id.close);

		progress.setMax(100);
		String schemeHtmlFileName = getIntent().getExtras().getString(
				IntentKey.TODAY_SCHEME_HTML_FILE_NAME);

		if (schemeHtmlFileName == null) {
			return;
		}


		 String url = getString(R.string.url_file_processor_consolebase)+"id="+schemeHtmlFileName+"&type=Schemes";

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

		class WebViewController extends WebViewClient {

			
			
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

		webView.setWebViewClient(new WebViewController());
		webView.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView view, int prog) {

				progress.setProgress(prog);

			}
		});

		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setBuiltInZoomControls(true);
		webView.loadUrl(url);
	

	}

	@Override
	public void onBackPressed() {

		if (webView != null && webView.canGoBack() == true) {
			webView.goBack();
		} else {
			finish();
		}
	}

}
