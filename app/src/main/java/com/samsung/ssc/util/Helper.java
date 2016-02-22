package com.samsung.ssc.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SyncInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.GCMRegIdService;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.RacePOSMMasterDTO;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

	public static void ragisterGCM(Context context) {
		/**
		 * Register for GSM
		 */
		if ((Helper.getStringValuefromPrefs(context,
				SharedPreferencesKey.KEY_APP_REGISTRATION_ID)).equals("")) {
			context.startService(new Intent(context, GCMRegIdService.class));
		} else {
			Helper.printLog("-------Reg Key--------->", Helper
					.getStringValuefromPrefs(context,
							SharedPreferencesKey.KEY_APP_REGISTRATION_ID));
		}
	}

	public void copyDirectoryOneLocationToAnotherLocation(File sourceLocation,
			File targetLocation) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdirs();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < sourceLocation.listFiles().length; i++) {

				copyDirectoryOneLocationToAnotherLocation(new File(
						sourceLocation, children[i]), new File(targetLocation,
						children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}

	}

	public static double distance(double lat1, double lon1, double lat2,
			double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static byte[] getByteArray(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
		byte[] byteArray = stream.toByteArray();
		try {
			stream.flush();
			stream.close();
		} catch (IOException e) {
			Helper.printStackTrace(e);
		}
		return byteArray;
	}

	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	/**
	 * Show the default android dialog.
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param okBtnText
	 */
	// ImageLoader imgLoader;
	public static void showErrorAlertDialog(Context context, String title,
			String msg) {

		AlertDialog.Builder dlgAlert;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			dlgAlert = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			dlgAlert = new AlertDialog.Builder(context);
		}
		if (msg != null) {
			dlgAlert.setMessage(msg);
		}
		if (title != null) {
			dlgAlert.setTitle(title);
		}
		dlgAlert.setCancelable(true);
		DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		};

		dlgAlert.setPositiveButton(context.getResources()
				.getString(R.string.ok), mOnClickListener);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();

	}

	public static void writeDataToFileINExternalStorage(String mfileName,
			String data) {
		try {
			File myFile = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "my" + File.separator + mfileName);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			myOutWriter.close();
			fOut.close();
		} catch (Exception e) {
			System.out.println("e: " + e);
		}
	}

	public static void printStackTrace(Exception e) {
		if (BaseActivity.releaseMode) {
			e.printStackTrace();
		} else {
			try {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				writeDataToFileINExternalStorage("Exception.txt",
						exceptionAsString);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param mDialogClickListener
	 */
	public static void showErrorAlertDialog(Context context, String title,
			String message, DialogInterface.OnClickListener mDialogClickListener) {
		AlertDialog.Builder dlgAlert;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			dlgAlert = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			dlgAlert = new AlertDialog.Builder(context);
		}
		dlgAlert.setIcon(R.drawable.ic_launcher);
		if (!title.equalsIgnoreCase("")) {

			dlgAlert.setTitle(title);
		}
		dlgAlert.setCancelable(true);
		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton("OK", mDialogClickListener);
		dlgAlert.setCancelable(false);
		dlgAlert.create().show();
	}

	/**
	 * this function is Display a common Dialog Box in whole application.
	 * 
	 * @param context
	 *            context of the activity
	 * @param message
	 *            message to be shown in dialog
	 * @param mCancelListener
	 * @param OkButtonText
	 *            button text
	 * */
	public static void showConfirmationDialog(Context context, String title,
			String message, String okButtonText, String CancelButtonText,
			DialogInterface.OnClickListener mDialogClickListener,
			DialogInterface.OnClickListener mCancelListener) {
		AlertDialog.Builder dlgAlert;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			dlgAlert = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			dlgAlert = new AlertDialog.Builder(context);
		}
		dlgAlert.setIcon(R.drawable.ic_launcher);
		if (!title.equalsIgnoreCase("")) {

			dlgAlert.setTitle(title);
		}
		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton(okButtonText, mDialogClickListener);
		if (mCancelListener != null) {
			dlgAlert.setNegativeButton(CancelButtonText, mCancelListener);
		} else {
			dlgAlert.setNegativeButton(CancelButtonText,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
		}
		dlgAlert.setCancelable(false);
		dlgAlert.create().show();
	}

	/**
	 * this function is Display a common Dialog Box in whole application.
	 * 
	 * @param context
	 *            context of the activity
	 * @param message
	 *            message to be shown in dialog
	 * @param mCancelListener
	 * @param OkButtonText
	 *            button text
	 * */
	public static void showConfirmationDialog(Context context, int title,
			int message, int okButtonText, int CancelButtonText,
			DialogInterface.OnClickListener mDialogClickListener,
			DialogInterface.OnClickListener mCancelListener) {
		AlertDialog.Builder dlgAlert;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			dlgAlert = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			dlgAlert = new AlertDialog.Builder(context);
		}
		dlgAlert.setIcon(R.drawable.ic_launcher);
		dlgAlert.setTitle(title);

		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton(okButtonText, mDialogClickListener);
		if (mCancelListener != null) {
			dlgAlert.setNegativeButton(CancelButtonText, mCancelListener);
		} else {
			dlgAlert.setNegativeButton(CancelButtonText,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
		}
		dlgAlert.setCancelable(false);
		dlgAlert.create().show();
	}

	public static void showConfirmationDialog1(Context context, String title,
			String message, String okButtonText, String CancelButtonText,
			DialogInterface.OnClickListener mDialogClickListener,
			DialogInterface.OnClickListener mCancelListener) {
		AlertDialog.Builder dlgAlert;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			dlgAlert = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			dlgAlert = new AlertDialog.Builder(context);
		}
		dlgAlert.setIcon(R.drawable.ic_launcher);
		if (!title.equalsIgnoreCase("")) {

			dlgAlert.setTitle(title);
		}
		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton(okButtonText, mDialogClickListener);
		/*
		 * if (mCancelListener != null) {
		 * dlgAlert.setNegativeButton(CancelButtonText, mCancelListener); } else
		 * { dlgAlert.setNegativeButton(CancelButtonText, new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss();
		 * 
		 * } }); }
		 */
		dlgAlert.setCancelable(false);
		dlgAlert.create().show();
	}

	public static void showThreeButtonConfirmationDialog(Context context,
			int title, int message, int buttononeText, int buttonTwoText,
			int buttonNegativeText,
			DialogInterface.OnClickListener buttonOneListerner,
			DialogInterface.OnClickListener buttonTwoListener,
			DialogInterface.OnClickListener buttonNegativeListener) {
		AlertDialog.Builder dlgAlert;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			dlgAlert = new AlertDialog.Builder(context,
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			dlgAlert = new AlertDialog.Builder(context);
		}
		dlgAlert.setIcon(R.drawable.ic_launcher);
		if (title != 0) {

			dlgAlert.setTitle(title);
		}
		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton(buttononeText, buttonOneListerner);
		dlgAlert.setNeutralButton(buttonTwoText, buttonTwoListener);
		if (buttonNegativeListener != null) {
			dlgAlert.setNegativeButton(buttonNegativeText,
					buttonNegativeListener);
		} else {
			dlgAlert.setNegativeButton(buttonNegativeText,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
		}
		dlgAlert.setCancelable(false);
		dlgAlert.create().show();
	}

	/**
	 * check the validity of a string
	 * 
	 * @param input
	 *            input string object
	 * @return true if valid else false
	 */
	public static boolean checkString(String input) {
		if (input != null && !input.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param context
	 *            -- context of the current activity
	 * @param viewResId
	 *            -- layout resource id
	 * @param duration
	 *            -- Either for SHORT or LONG duration.
	 */

	public static void showToast(Context context, int duration, int viewResId) {

		// create the view
		LayoutInflater factory = LayoutInflater.from(context);
		final View view = factory.inflate(viewResId, null);

		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setGravity(
				Gravity.TOP | Gravity.CENTER_HORIZONTAL,
				10,
				(int) context.getResources().getDimension(
						R.dimen.reportee_list_item_height));
		toast.setDuration(duration);
		toast.show();
	}

	/**
	 * Showing the Toast message
	 * 
	 * @param context
	 *            context of the activity
	 * @param msg
	 *            message to be shown
	 * @param duration
	 *            defining duration with either long or short
	 */
	public static void showToast(Context context, String msg, int duration) {

		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();
	}

	public static void showCustomToast(Context context, int msg, int duration) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View view = factory.inflate(R.layout.custom_toast_layout, null);

		// Set text
		TextView text = (TextView) view.findViewById(R.id.tv);
		text.setText(msg);
		// Toast
		Toast toast = new Toast(context);

		toast.setGravity(
				Gravity.TOP | Gravity.CENTER_HORIZONTAL,
				10,
				(int) context.getResources().getDimension(
						R.dimen.reportee_list_item_height));

		toast.setDuration(duration);
		toast.setView(view);
		toast.show();
	}

	public static void showCustomToast(Context context, String msg, int duration) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View view = factory.inflate(R.layout.custom_toast_layout, null);

		// Set text
		TextView text = (TextView) view.findViewById(R.id.tv);
		text.setText(msg);
		// Toast
		Toast toast = new Toast(context);

		toast.setGravity(
				Gravity.TOP | Gravity.CENTER_HORIZONTAL,
				10,
				(int) context.getResources().getDimension(
						R.dimen.reportee_list_item_height));

		toast.setDuration(duration);
		toast.setView(view);
		toast.show();
	}

	/**
	 * save string type data in prefrences
	 * 
	 * @param mContext
	 *            context of the activity
	 * @param strName
	 *            parameter name to identify save value
	 * @param strValue
	 *            value to be save
	 */
	public static void saveStringValueInPrefs(Context mContext, String strName,
			String strValue) {
		Editor editor = Helper.getEditor(mContext);
		editor.putString(strName, strValue);
		editor.commit();
	}

	/**
	 * save integer value to the preferences(internal memory)
	 * 
	 * @param mContext
	 *            context of the activity
	 * @param intName
	 *            parameter name to identify save value
	 * @param value
	 *            save value
	 */
	public static void saveIntgerValueInPrefs(Context mContext, String intName,
			int value) {
		Editor editor = Helper.getEditor(mContext);
		editor.putInt(intName, value);
		editor.commit();
	}

	public static void saveBoolValueInPrefs(Context mContext, String strName,
			boolean strValue) {
		Editor editor = Helper.getEditor(mContext);
		editor.putBoolean(strName, strValue);
		editor.commit();
	}

	/*
	 * Save the double value in prefrence
	 */

	public static void saveLongValueInPref(Context mContext, String strName,
			long strValue) {

		Editor editor = Helper.getEditor(mContext);
		editor.putLong(strName, strValue);
		editor.commit();
	}

	public static long getLongValueFromPrefs(Context mContext, String strName) {
		SharedPreferences preferences = getPrefrences(mContext);
		long value = preferences.getLong(strName, -1);
		return value;
	}

	/**
	 * get integer value from the preferences"internal memory"
	 * 
	 * @param mContext
	 *            context of the activity
	 * @param strName
	 *            parameter name to identify save value
	 * @return
	 */
	public static int getIntValueFromPrefs(Context mContext, String strName) {
		SharedPreferences preferences = getPrefrences(mContext);
		int value = preferences.getInt(strName, -1);
		return value;
	}

	public static boolean getBoolValueFromPrefs(Context mContext, String strName) {
		SharedPreferences preferences = getPrefrences(mContext);
		boolean value = preferences.getBoolean(strName, false);
		return value;
	}

	/**
	 * save string type data in prefrences
	 * 
	 * @param mContext
	 *            context of the activity
	 * @param strName
	 *            parameter name to identify save value
	 * @param strValue
	 *            value to be save
	 * 
	 */
	public static String getStringValuefromPrefs(Context mContext,
			String strName) {
		SharedPreferences preferences = getPrefrences(mContext);
		String strValue = preferences.getString(strName, "");
		return strValue;
	}

	/**
	 * This Function used to get the prefrences editor.
	 * 
	 * @param context
	 *            context of the activity
	 * @return editor
	 */
	public static Editor getEditor(Context context) {
		SharedPreferences sharedPreferences = Helper.getPrefrences(context);
		return sharedPreferences.edit();
	}

	/**
	 * this function return the preferences's Object.
	 * 
	 * @param context
	 *            context of the activity
	 * @return
	 */
	public static SharedPreferences getPrefrences(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				context.getString(R.string.PREFERENCE_NAME),
				Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
		return sharedPreferences;
	}

	public static boolean isOnline(Context mContext) {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null) {
			flag = cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}
		return flag;
	}

	/**
	 * replace special character
	 * 
	 * @param str
	 *            string which character are suppose to replace
	 * @return String string with replace character
	 */
	public static String replaceSpecialChars(final String str) {
		String url = "";
		try {
			url = new String(str.trim().replace(" ", "%20").replace("&", "%26")
					.replace(",", "%2c").replace("(", "%28")
					.replace(")", "%29").replace("!", "%21")
					.replace("=", "%3D").replace("<", "%3C")
					.replace(">", "%3E").replace("#", "%23")
					.replace("$", "%24").replace("'", "%27")
					.replace("*", "%2A").replace("-", "%2D")
					.replace(".", "%2E").replace("/", "%2F")
					.replace(":", "%3A").replace(";", "%3B")
					.replace("+", "%2B").replace("?", "%3F")
					.replace("@", "%40").replace("[", "%5B")
					.replace("\\", "%5C").replace("]", "%5D")
					.replace("_", "%5F").replace("`", "%60")
					.replace("{", "%7B").replace("|", "%7C")
					.replace("}", "%7D"));
		} catch (Exception e) {
			Log.d("", "" + e);
		}
		return url;
	}

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password
	 */
	public static boolean validate(final String password) {

		Pattern pattern = Pattern
				.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();

	}

	public static boolean isNullOrEmpty(String myString) {
		return myString == null || "".equals(myString)
				|| myString.equals("null") || myString.equals("0") ;
	}

	/*
	 * used to print logtrace
	 */
	public static void printLog(String tag, String msg) {
		if (!BaseActivity.releaseMode) {
			Log.d(tag, msg);
		}

	}

	/**
	 * write data in SD card
	 * 
	 * @param data
	 * @param filename
	 */
	public static void writedata(String data, String filename) {
		File myFile = new File(Environment.getExternalStorageDirectory() + "/"
				+ filename);

		try {
			myFile.createNewFile();
			FileOutputStream fOut;
			fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			myOutWriter.close();
			fOut.close();
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

	}

	/**
	 * 
	 * @param email
	 * @return true if mail Id is valid
	 */
	public static boolean checkEmail(String email) {

		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

	}

	/**
	 * 
	 * @param timeInMilliSec
	 *            time in milliseconds
	 * @return Date and time
	 */

	public static String millisecondToDate(String timeInMilliSec) {
		String timeFormatNow = "dd-MMM-yyyy";
		long yourmilliseconds = 0;
		if (!timeInMilliSec.equalsIgnoreCase("")) {
			yourmilliseconds = Long.parseLong(timeInMilliSec);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormatNow);

		Date resultdate = new Date(yourmilliseconds);

		return sdf.format(resultdate);

	}

	public static String postMethod(Context mContext, String conUrl,
			JSONObject nvps) throws Exception {
		String result = null;
		InputStream is = null;

		HttpClient httpclient = new DefaultHttpClient();
		URI u = new URI(conUrl);
		HttpPost httppost = new HttpPost(u);
		httppost.setHeader(WebConfig.WebParams.APIKEY, Helper
				.getStringValuefromPrefs(mContext,
						SharedPreferencesKey.PREF_APIKEY));
		httppost.setHeader(WebConfig.WebParams.APITOKEN, Helper
				.getStringValuefromPrefs(mContext,
						SharedPreferencesKey.PREF_APITOKEN));

		if (!Helper.getStringValuefromPrefs(mContext,
				SharedPreferencesKey.PREF_USERID).equals("")) {

			httppost.setHeader(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_USERID));

		} else {
			httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
		}
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");
		httppost.setEntity(new ByteArrayEntity(nvps.toString().getBytes("UTF8")));
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

	public static String getStringfromInputStream(InputStream is) {

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
			Helper.printLog("Loading Runnable Error converting result DM",
					e.toString());
		}
		return result;

	}

	/*
	 * Clear all Preferences value
	 */
	public static void clearPreferencesData(Context mContext) {/*
																 * 
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .PREF_EMPID,
																 * "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .PREF_APIKEY,
																 * "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_APITOKEN
																 * , "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .PREF_USERID,
																 * "");
																 * saveIntgerValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .PREF_ROLEID,
																 * 0);
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_PARTNERID
																 * , "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_ROLEIDSTOREWISE
																 * , ""); //
																 * saveStringValueInPrefs
																 * (mContext,
																 * Constants
																 * .PREF_STOREID
																 * ,"");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_COVERAGEID
																 * , "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_BEATPLANDATE
																 * , "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_STORECODE
																 * , ""); //
																 * saveStringValueInPrefs
																 * (mContext,
																 * Constants
																 * .PREF_ACCOUNTNAME
																 * , "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_VERSION_NO
																 * , "");
																 * saveStringValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_DISPALYDATE
																 * , "");
																 * saveBoolValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_ROAMINGUSER
																 * , false);
																 * saveIntgerValueInPrefs
																 * (mContext,
																 * SharedPreferencesKey
																 * .
																 * PREF_SURVEYRESPONSEIDPARTNER
																 * , 0);
																 */
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static ArrayList<String> getStringToArrayList(String data) {

		ArrayList<String> list = new ArrayList<String>();
		String newdata = data.replace("[", "").replace("]", "")
				.replace(", ", ",");

		for (String s : newdata.split(",")) {
			list.add(s);
		}

		return list;

	}

	public static ArrayList<Integer> getStringToIntegerArrayList(String data) {

		ArrayList<Integer> list = new ArrayList<Integer>();
		String newdata = data.replace("[", "").replace("]", "")
				.replace(", ", ",");

		for (String s : newdata.split(",")) {
			list.add(new Integer(s));
		}

		return list;

	}

	/**
	 * 
	 * @param ctx
	 * @param filename
	 * @return
	 */
	public static boolean isExistFile(Activity ctx, String filename) {
		boolean flag = true;
		File file = ctx.getFileStreamPath(filename);
		if (!file.exists()) {
			flag = false;
		}
		return flag;

	}

	/**
	 * 
	 * @param ctx
	 * @param filename
	 */
	public static void deleteFiles(Activity ctx, String filename) {
		File file = ctx.getFileStreamPath(filename);
		if (file.exists()) {
			file.delete();
		}
	}

	public static Bitmap rotateBitmap(Bitmap source, float angle) {
		try {
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			Bitmap b = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), matrix, true);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

	public static void saveCurrentInteractionTime(Context context) {

		long previousInteractionTime = Helper.getLongValueFromPrefs(context,
				SharedPreferencesKey.CURRENT_INTERACTION_TIME);
		long currentInteractionTime = System.currentTimeMillis();

		if (previousInteractionTime == -1) {
			return;
		}

		Helper.saveLongValueInPref(context,
				SharedPreferencesKey.CURRENT_INTERACTION_TIME,
				currentInteractionTime);
		if ((currentInteractionTime - previousInteractionTime) >= (6000000))// 2
		{
			Helper.saveBoolValueInPrefs(context, Constants.IS_SESSION_LOGOUT,
					true);
		}
	}

	public static Bitmap rotateImageAccordingToEXIF(Bitmap bm, String image_path) {
		ExifInterface ei = null;

		try {
			ei = new ExifInterface(image_path);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				bm = rotateBitmap(bm, 90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				bm = rotateBitmap(bm, 180);

				break;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bm;
	}

	public static String createBase64String(Bitmap bitmap) {

		if (bitmap != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					byteArrayOutputStream);
			byte[] byteArray = byteArrayOutputStream.toByteArray();
			return Base64.encodeToString(byteArray, Base64.DEFAULT);
		}

		return null;

	}

	public static String getBase64StringFormBitmap(Bitmap bitmap) {

		String encoded = null;
		if (bitmap != null) {

			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return encoded;
	}

	public static Bitmap creatBitmapFormImagepath(String image_path) {

		Bitmap rotatedbitmap = null;

		final int IMAGE_MAX_WIDTH_HEIGHT = 300;
		if (image_path != null) {

			try {
				// First decode with inJustDecodeBounds=true to check dimensions
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(image_path, options);
				// Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options,
						IMAGE_MAX_WIDTH_HEIGHT, IMAGE_MAX_WIDTH_HEIGHT);

				// Decode bitmap with inSampleSize set
				options.inJustDecodeBounds = false;

				Bitmap bm = BitmapFactory.decodeFile(image_path, options);
				rotatedbitmap = Helper.rotateImageAccordingToEXIF(bm,
						image_path);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return rotatedbitmap;

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			inSampleSize = (int) Math.pow(
					2,
					(int) Math.round(Math.log(reqWidth
							/ (double) Math.max(height, width))
							/ Math.log(0.5)));
		}

		return inSampleSize;
	}

	/**
	 * 
	 * @param context
	 *            application context
	 * @param moduleID
	 *            the id of the module from which this method get called
	 * @param syncStatus
	 *            sync status of the entry
	 * @return ActivityDataMasterModel object or null in case exception occurs
	 */

	public static ActivityDataMasterModel getActivityDataMasterModel(
			Context context, Module module) {

		int userID;
		int storeID;
		long coverageID = -1;

		int surveyResponseID = 0;
		try {
			userID = Integer.parseInt(Helper.getStringValuefromPrefs(context,
					SharedPreferencesKey.PREF_USERID));

			if (module.isStoreWise()) {
				/*
				 * surveyResponseID = Helper.getIntValueFromPrefs(context,
				 * SharedPreferencesKey.PREF_SURVEYRESPONSEID);
				 */

				surveyResponseID = -1;

				storeID = Helper.getStringValuefromPrefs(context,
						SharedPreferencesKey.PREF_STOREID).equals("") ? 0
						: Integer.parseInt(Helper.getStringValuefromPrefs(
								context, SharedPreferencesKey.PREF_STOREID));
			} else {
				surveyResponseID = 0;
				storeID = 0;
			}

			if (!Helper.isNullOrEmpty(Helper.getStringValuefromPrefs(context,
					SharedPreferencesKey.PREF_COVERAGEID))) {

				coverageID = Long.parseLong(Helper.getStringValuefromPrefs(
						context, SharedPreferencesKey.PREF_COVERAGEID));

			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ActivityDataMasterModel activityData = new ActivityDataMasterModel();

		activityData.setUserID(userID);
		activityData.setModuleCode(module.getModuleCode());
		activityData.setCreatedDate("");
		activityData.setModifiedDate("");

		if (module.isStoreWise()) {
			activityData.setSurveyResponseID(surveyResponseID);
			activityData.setCoverageID(coverageID);
			activityData.setStoreID(storeID);

		} else {
			activityData.setSurveyResponseID(0);
			activityData.setCoverageID(0);
			activityData.setStoreID(0);

		}

		return activityData;
	}

	// RELEASE_CHECK
	public static void setScreenShotOff(Context context) {
		/*
		 * try { if (android.os.Build.VERSION.SDK_INT >=
		 * android.os.Build.VERSION_CODES.HONEYCOMB) { ((Activity)
		 * context).getWindow().setFlags(
		 * WindowManager.LayoutParams.FLAG_SECURE,
		 * WindowManager.LayoutParams.FLAG_SECURE); }
		 * 
		 * } catch (Exception e) {
		 * 
		 * e.printStackTrace(); }
		 */

	}

	
	public static void setKeyBoardShow(Context context, boolean isShow) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isShow) {
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		}else{
			imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	public static String getDeviceModelName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	@SuppressWarnings("deprecation")
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ip = Formatter.formatIpAddress(inetAddress
								.hashCode());

						return ip;
					}
				}
			}
		} catch (SocketException ex) {

		}
		return null;
	}

	public static int dpToPixel(Context context, int dp) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				r.getDisplayMetrics());
		return (int) px;
	}

	public static void showAlertDialog(
			Context context,
			int dialogType,
			String titleText,
			String messageText,
			String confirmText,
			SSCAlertDialog.OnSDAlertDialogClickListener confirmClickListener,
			boolean showCancelButton,
			String cancelText,
			SSCAlertDialog.OnSDAlertDialogClickListener cancelClickListener) {

		new SSCAlertDialog(context, dialogType).setTitleText(titleText)
				.setContentText(messageText).setCancelText(cancelText)
				.setConfirmText(confirmText).showCancelButton(showCancelButton)
				.setCancelClickListener(cancelClickListener)
				.setConfirmClickListener(confirmClickListener).show();

	}

	public static void showAlertDialog(
			Context context,
			int dialogType,
			String titleText,
			String messageText,
			String confirmText,
			View layoutFrame,
			SSCAlertDialog.OnSDAlertDialogClickListener confirmClickListener,
			boolean showCancelButton,
			String cancelText,
			SSCAlertDialog.OnSDAlertDialogClickListener cancelClickListener) {

		new SSCAlertDialog(context, dialogType).setTitleText(titleText)
				.setContentText(messageText).setCancelText(cancelText)
				.setConfirmText(confirmText).showCancelButton(showCancelButton)
				.setCancelClickListener(cancelClickListener)
				.setConfirmClickListener(confirmClickListener)
				.setLayoutFrameView(layoutFrame).show();

	}

	public static void showNotCancelableAlertDialog(
			Context context,
			int dialogType,
			String titleText,
			String messageText,
			String confirmText,
			SSCAlertDialog.OnSDAlertDialogClickListener confirmClickListener,

			boolean showCancelButton,
			String cancelText,
			SSCAlertDialog.OnSDAlertDialogClickListener cancelClickListener) {

		new SSCAlertDialog(context, dialogType).setTitleText(titleText)
				.setContentText(messageText).setCancelText(cancelText)
				.setConfirmText(confirmText).showCancelButton(showCancelButton)
				.setCancelClickListener(cancelClickListener)
				.setConfirmClickListener(confirmClickListener)
				.setNotCancelable().show();

	}

	public static String getAnnouncement(Context context) {
		return getStringValuefromPrefs(context,
				SharedPreferencesKey.PREF_ANNOUNCEMENT);
	}

	public static void setAnnouncement(Context context, String announcement) {
		saveStringValueInPrefs(context, SharedPreferencesKey.PREF_ANNOUNCEMENT,
				announcement);
	}

	public static boolean hasPendingMandatoryModules(
			List<Module> mData) {
		for (Module module : mData) {
			if (module.isIsMandatory()) {
				if (!module.isActivityIDAvailable()) {
					return true;
				}
				if (module.getSyncStatus() != SyncUtils.SYNC_STATUS_SUBMIT) {
					return true;
				}
			}
		}

		return false;

	}

	public static Object getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o;
			}
		}
		return null;
	}

	public static ArrayList<RacePOSMMasterDTO> cloneList(
			List<RacePOSMMasterDTO> list) {
		ArrayList<RacePOSMMasterDTO> clone = new ArrayList<RacePOSMMasterDTO>(
				list.size());
		for (RacePOSMMasterDTO item : list) {
			try {
				clone.add((RacePOSMMasterDTO) item.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return clone;
	}

	public static String getFormatedDate(String dateString) {
		// TODO Auto-generated method stub

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		java.util.Date dt;
		try {
			dt = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			dt = new Date();
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());

		return dateFormat.format(dt);

	}

	public static boolean isDeviceRooted() {
		return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
				|| checkRootMethod4();
	}

	private static boolean checkRootMethod1() {
		String buildTags = android.os.Build.TAGS;
		return buildTags != null && buildTags.contains("test-keys");
	}

	private static boolean checkRootMethod2() {
		String[] paths = { "/system/app/Superuser.apk", "/sbin/su",
				"/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
				"/data/local/bin/su", "/system/sd/xbin/su",
				"/system/bin/failsafe/su", "/data/local/su" };
		for (String path : paths) {
			if (new File(path).exists())
				return true;
		}
		return false;
	}

	private static boolean checkRootMethod3() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(
					new String[] { "/system/xbin/which", "su" });
			BufferedReader in = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			if (in.readLine() != null)
				return true;
			return false;
		} catch (Throwable t) {
			return false;
		} finally {
			if (process != null)
				process.destroy();
		}
	}

	private static boolean checkRootMethod4() {
		boolean found = false;
		if (!found) {
			String[] places = { "/sbin/", "/system/bin/", "/system/xbin/",
					"/data/local/xbin/", "/data/local/bin/",
					"/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/" };
			for (String where : places) {
				if (new File(where + "su").exists()) {
					found = true;

					break;
				}
			}
		}
		return found;
	}

	public static void deleteFiles(String path) {

		File file = new File(path);

		if (file.exists()) {
			String deleteCmd = "rm -r " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (IOException e) {
				Helper.printStackTrace(e);
				;
			}
		}
	}

	// TODO Auto-generated method stub
	public static String getAppVersion(Context context) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			String version = "" + pInfo.versionName;
			Helper.saveStringValueInPrefs(context,
					SharedPreferencesKey.PREF_VERSION_NO, version);

			return version;
		} catch (NameNotFoundException e) {
			Helper.printStackTrace(e);
		}
		return null;
	}

	public static void backupDatabase(Context context) throws IOException {
		// Open your local db as the input stream
		String inFileName = "/data/data/com.samsung.ssc/databases/SMARTDOST_CE_DATABASE";
		File dbFile = new File(inFileName);
		FileInputStream fis = new FileInputStream(dbFile);

		String outFileName = Environment.getExternalStorageDirectory()
				+ "/SMARTDOST_CE_DATABASE";
		// Open the empty db as the output stream
		OutputStream output = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		// Close the streams
		output.flush();
		output.close();
		fis.close();

		sendEmail(context, new File(outFileName));

	}

	private static void sendEmail(Context context, File file) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		// String to[] = {"neelam.g@partner.samsung.com"};
		String to[] = { "d.ashish@partner.samsung.com" };
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		

		emailIntent.putExtra(Intent.EXTRA_TEXT, "PFA!");
		// the attachment

		Uri uri = Uri.fromFile(file);
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		// the mail subject
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SSC Database");
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(emailIntent);
	}

	public static Drawable convertDrawableToGrayScale(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		Drawable res = drawable.mutate();
		res.setColorFilter(Color.GRAY, Mode.SRC_IN);
		return res;
	}

	public static boolean isSyncActive(Account account, String authority) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return isSyncActiveHoneycomb(account, authority);
		} else {
			@SuppressWarnings("deprecation")
			SyncInfo currentSync = ContentResolver.getCurrentSync();
			return currentSync != null && currentSync.account.equals(account)
					&& currentSync.authority.equals(authority);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static boolean isSyncActiveHoneycomb(Account account,
			String authority) {
		for (SyncInfo syncInfo : ContentResolver.getCurrentSyncs()) {
			if (syncInfo.account.equals(account)
					&& syncInfo.authority.equals(authority)) {
				return true;
			}
		}
		return false;
	}

	public static Account getAccount(Context context) {

		Account account;
		AccountManager accountManager = (AccountManager) context
				.getSystemService(Context.ACCOUNT_SERVICE);
		Account[] accounts = accountManager
				.getAccountsByType(Constants.ACCOUNT_TYPE);

		if (accounts.length == 0 || accounts[0] == null) {
			account = CreateSyncAccount(context);
		} else {
			account = accounts[0];
		}

		return account;
	}

	public static Account CreateSyncAccount(Context context) {
		// Create the account type and default account
		Account newAccount = new Account(Constants.ACCOUNT,
				Constants.ACCOUNT_TYPE);
		AccountManager accountManager = (AccountManager) context
				.getSystemService(context.ACCOUNT_SERVICE);
		// Get an instance of the Android account manager

		/*
		 * Add the account and account type, no password or user data If
		 * successful, return the Account object, otherwise report an error.
		 */
		if (accountManager.addAccountExplicitly(newAccount, null, null)) {
			/*
			 * If you don't set android:syncable="true" in in your <provider>
			 * element in the manifest, then call context.setIsSyncable(account,
			 * AUTHORITY, 1) here.
			 */
			System.out.println("Done");

		} else {
			/*
			 * The account exists or some other error occurred. Log this, report
			 * it, or handle it internally.
			 */
			System.out.println("Error");
		}

		return newAccount;
	}

	public static void cancelSync(Context context) {

		ContentResolver.cancelSync(getAccount(context),
				ProviderContract.AUTHORITY);
	}

	public static boolean getSyncFlag() {

		try {
			File file = Environment.getExternalStorageDirectory();
			File filename = new File(file, "SyncFile");
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fis);
			Boolean boole = (Boolean) in.readObject();

			in.close();
			return boole;
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return false;

	}

	public static void changeSyncFlag(boolean value) {
		try {
			File file = Environment.getExternalStorageDirectory();
			File filename = new File(file, "SyncFile");
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(new Boolean(value));
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();

	}


	public static long getDateFromString(String dateString,String formte){

		try {
			DateFormat df = new SimpleDateFormat(formte);
			Date date =  df.parse(dateString);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;


	}

	public static String getDateStringFromDate(Date date,String formte){

        DateFormat df = new SimpleDateFormat(formte);
        String reportDate = df.format(date);

        return reportDate;
	}

	public static String longDateToString(long date)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		Date d = (Date) c.getTime();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		return format.format(d); //this variable time contains the time in the format of "day/month/year".

	}


	public static String longDateToUIDateString(long date)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		Date d = (Date) c.getTime();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
		return format.format(d); //this variable time contains the time in the format of "day/month/year".

	}

	public static long dateStringToLong(String dateString)
	{
		try
		{

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = sdf.parse(dateString);
			return  date.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;

	}


}
