package com.samsung.ssc;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.CustomExceptionHandler;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyGestureDetector;

public class BaseActivity extends AppCompatActivity implements
		View.OnClickListener, OnItemSelectedListener, OnCheckedChangeListener,
		android.widget.CompoundButton.OnCheckedChangeListener {

	/**
	 * Context of current screen
	 */

	public static boolean releaseMode = true;
	private static int counter = 0;
	private GestureDetector gestureDetector;

	/**
	 * Set Current context
	 * 
	 * @param currentContext
	 *            * : Current context
	 */
	public void setCurrentContext(Context currentContext) {

	}

	/*
	 * set the center text in the header of screens
	 * 
	 * public void setCentretext(String headercentretext) {
	 * 
	 * 
	 * }
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (gestureDetector.onTouchEvent(event))
			return false;
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// only for gingerbread and newer versions
		Helper.setScreenShotOff(this);

		if (checkIfLogoutTrue()) {
			return;
		}

		/*if (releaseMode) {
			if (counter == 0
					&& !(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
				Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
						this));
				System.out.println("releaseMode");
				counter++;
			}
		}*/
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (checkIfLogoutTrue()) {
			return;
		}
	}

	/**
	 * Used to initialize various elements of screens
	 */
	@SuppressWarnings("deprecation")
	public void init() {

		gestureDetector = new GestureDetector(new MyGestureDetector(this));

	}

	/**
	 * To check Internet connection status
	 * 
	 * @return true : if Internet connection is available
	 */

	public boolean isOnline() {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null) {
			flag = cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}
		return flag;
	}

	/**
	 * Set Data on screen
	 */

	/**
	 * Used for screen validations
	 */
	public boolean validation() {
		return true;
	}

	/**
	 * @param StringCode
	 * @return : string value of id from resource
	 */
	public String getStringFromResource(int StringCode) {
		return getResources().getString(StringCode);
	}

	/*
	 * (non-Javadoc)Used for taking values of Spinner/DropDown
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

	}

	/*
	 * (non-Javadoc) Used for setting default value of spinner
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/*
	 * (non-Javadoc) RadioGroup Input Reader
	 * 
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

	/*
	 * (non-Javadoc) CheckBox Change Input Listener
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	/*
	 * used to print logtrace
	 */
	public void printLog(String tag, String msg) {
		if (!BaseActivity.releaseMode) {
			Log.d(tag, msg);
		}

	}

	@Override
	public void onClick(View arg0) {

	}

	@SuppressWarnings("static-access")
	private boolean checkIfLogoutTrue() {
		Helper.saveCurrentInteractionTime(getApplicationContext());

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				Constants.IS_SESSION_LOGOUT)) {

			Helper.cancelSync(this);

			Helper.saveBoolValueInPrefs(getApplicationContext(),
					Constants.IS_SESSION_LOGOUT, false);
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			Helper.showCustomToast(getApplicationContext(),
					R.string.you_have_been_logged_out, Toast.LENGTH_LONG);
			return true;
		}

		return false;
	}

	public void setParentLayout(View parentView) {
		// TODO Auto-generated method stub

		parentView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event))
					return false;
				return false;
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		//overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
}
