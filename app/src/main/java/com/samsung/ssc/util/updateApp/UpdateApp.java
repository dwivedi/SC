package com.samsung.ssc.util.updateApp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

public class UpdateApp extends Activity implements CallBackUpdateApp {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = getIntent().getExtras().getString("url");
		if (url != null) {
			UpdateAsyncTask task = new UpdateAsyncTask(UpdateApp.this, this,
					url);
			task.execute();
		}
	}

	@Override
	public void processResponse() {
		File SDCardRoot = Environment.getExternalStorageDirectory();
		File file = new File(SDCardRoot, "File.apk");
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}

interface CallBackUpdateApp {
	/**
	 * Used getting response from async task and sending back to parent activity
	 * 
	 * @param response
	 *            : response received by webService
	 */
	public void processResponse();
}
