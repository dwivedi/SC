package com.samsung.ssc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import com.samsung.ssc.R;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.SharedPreferencesKey;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

	private static CustomExceptionHandler mThis;
	private UncaughtExceptionHandler defaultUEH;
	private Context mContext;

	public CustomExceptionHandler(Context ctx) {
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		mContext = ctx;
	}
	
	
	public static CustomExceptionHandler getInstance(Context ctx) {
	    if(mThis==null){
	    	mThis = new CustomExceptionHandler(ctx);
	    }
		return mThis; 

	}

	/*public  void uncaughtException(Thread t, Throwable e) {
		
		System.out.println("uncaughtException");
		StackTraceElement[] arr = e.getStackTrace();
		String report = "UserId:\t"+ Helper.getStringValuefromPrefs(mContext,Constants.PREF_USERID) +"\n";
		report+="IMEI:\t"+ ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() +"\n";
		report+="App Version:\t" + Helper.getStringValuefromPrefs(mContext,Constants.PREF_VERSION_NO)+ "\n";
		report+="URL:\t"+ mContext.getString(R.string.url)+"\n";
		report +=e.toString() + "\n\n";
		report += "--------- Stack trace ---------\n\n";
		for (int i = 0; i < arr.length; i++) {
			report += "    " + arr[i].toString() + "\n";
		}
		report += "-------------------------------\n\n";

		report += "--------- Cause ---------\n\n";
		Throwable cause = e.getCause();
		if (cause != null) {
			report += cause.toString() + "\n\n";
			arr = cause.getStackTrace();
			for (int i = 0; i < arr.length; i++) {
				report += "    " + arr[i].toString() + "\n";
			}
		}
		report += "-------------------------------\n\n";

		writeFile(report);
		//defaultUEH.uncaughtException(t, e);
		
	}*/

	/**
	 * Method to create a directory if does not exist and put a log file in it
	 * for crash.
	 * 
	 * @param report
	 */
	private void writeFile(String report) {
		try {
			String filename = "ErrorLog_"+Helper.getStringValuefromPrefs(mContext, SharedPreferencesKey.PREF_USERID)+".txt";
 			File folder = new File(FileDirectory.DIRECTORY_ERROR_LOG);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			File myFile = new File(folder, filename);
			if (!myFile.exists()){
				myFile.createNewFile();
			}
			byte[] data = report.getBytes();
			FileOutputStream fos = new FileOutputStream(myFile);
			fos.write(data);
			fos.flush();
			fos.close();
			sendEmail(myFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 private void sendEmail(File file){
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
 			emailIntent.setType("plain/text");
//			String to[] = {"neelam.g@partner.samsung.com"};
			String to[] = {"smartdost.ce@samsung.com"};
			emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
			String cc[] = {"sabyasachi.b@partner.samsung.com","d.ashish@partner.samsung.com","s.ankit3@partner.samsung.com"};
			emailIntent .putExtra(Intent.EXTRA_CC, cc);
			emailIntent.putExtra(Intent.EXTRA_TEXT, "PFA!");
			// the attachment
		 
			Uri uri =Uri.fromFile(file);
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			// the mail subject
			emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Report Issue(SmartDost CE)");
			mContext.startActivity(emailIntent);
		}


	@Override
	public void uncaughtException(Thread thread, Throwable e) {/*
		
		StackTraceElement[] arr = e.getStackTrace();
		String report = "UserId:\t"+ Helper.getStringValuefromPrefs(mContext,SharedPreferencesKey.PREF_USERID) +"\n";
		report+="IMEI:\t"+ ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() +"\n";
		report+="App Version:\t" + Helper.getStringValuefromPrefs(mContext,SharedPreferencesKey.PREF_VERSION_NO)+ "\n";
		report+="Available Internal Memory Size:\t" + formatSize(getAvailableInternalMemorySize())+ "\n";
		report+="Total Internal Memory Size:\t" + formatSize(getTotalInternalMemorySize())+ "\n";
		report+="URL:\t"+ mContext.getString(R.string.url)+"\n";
		report +=e.toString() + "\n\n";
		report += "--------- Stack trace ---------\n\n";
		for (int i = 0; i < arr.length; i++) {
			report += "    " + arr[i].toString() + "\n";
		}
		report += "-------------------------------\n\n";

		report += "--------- Cause ---------\n\n";
		Throwable cause = e.getCause();
		if (cause != null) {
			report += cause.toString() + "\n\n";
			arr = cause.getStackTrace();
			for (int i = 0; i < arr.length; i++) {
				report += "    " + arr[i].toString() + "\n";
			}
		}
		report += "-------------------------------\n\n";

		
		
		writeFile(report);
		
		defaultUEH.uncaughtException(thread, e);
		*/
	}
	
	private  String formatSize(long size) {
        String suffix = null;
 
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            } 
        } 
 
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
 
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        } 
 
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    } 
	
	
	private long getAvailableInternalMemorySize() { 
        try {
			StatFs stat = new StatFs(Environment.getDataDirectory().getPath()); 
			
			return stat.getAvailableBlocks() * stat.getBlockSize();
		} catch (Exception e) {
			return 0;
		} 
    } 
     
    private long getTotalInternalMemorySize() { 
        try {
			StatFs stat = new StatFs(Environment.getDataDirectory().getPath()); 
			return stat.getBlockCount() * stat.getBlockSize();
		} catch (Exception e) {
			return 0;
		} 
    } 
	
	
}