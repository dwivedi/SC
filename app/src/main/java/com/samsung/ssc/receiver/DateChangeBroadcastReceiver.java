package com.samsung.ssc.receiver;

import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DateChangeBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Helper.saveBoolValueInPrefs(context,
				SharedPreferencesKey.PREF_IS_DATE_CHANGED, true);
		
		Log.e("", "ACTION_DATE_CHANGED received");
		// sabyascahi 
		
	}

}
