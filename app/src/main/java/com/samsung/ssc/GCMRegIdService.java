package com.samsung.ssc;

import java.io.IOException;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

/**
 * Gsm registering process
 * 
 * @author vasingh
 * 
 */

public class GCMRegIdService extends IntentService {
	
	
    public static final String EXTRA_MESSAGE = "message";
    
    private String PROPERTY_APP_VERSION = "appVersion";
    /**
     * Tag used on log messages.
     */
   private static final String TAG = "GCM Demo";

    private GoogleCloudMessaging gcm;
    private String regid;
	
	
	public GCMRegIdService() {
		super("GCMRegIdService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		registrationWithGCM();
		Helper.printLog("GCMRegId Service started", "Start");
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public void registrationWithGCM() { // Check device for Play Services APK.
										// If check succeeds, proceed with GCM
										// registration.

	if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(GCMRegIdService.this);

			if (Helper.isNullOrEmpty(regid)/*regid != null || regid != ""*/) {
				Helper.printLog(TAG, "Reg Id: " + regid);
				registerInBackground();
			}
		} else {
			Helper.printLog(TAG, "No valid Google Play Services APK found.");
		}
		}
	
	
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        
        if (resultCode != ConnectionResult.SUCCESS) {
        	
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                Helper.showToast(GCMRegIdService.this, PLAY_SERVICES_RESOLUTION_REQUEST, Toast.LENGTH_LONG);
            	Helper.printLog("Msg", "This device i supported.");
            } else {
            	Helper.printLog(TAG, "This device is not supported.");
                
            }
            
            return false;
        }
        return true;
    }
    
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        
        int appVersion = getAppVersion(context);
                
        Helper.printLog(TAG, "Saving regId on app version " + appVersion);
        Helper.saveStringValueInPrefs(context, SharedPreferencesKey.KEY_APP_REGISTRATION_ID, regId);
        Helper.saveIntgerValueInPrefs(context, PROPERTY_APP_VERSION, appVersion);
        
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
    	
       
        String registrationId = Helper.getStringValuefromPrefs(context, SharedPreferencesKey.KEY_APP_REGISTRATION_ID);
        
        if (registrationId == null) {
        	Helper.printLog(TAG, "Registration not found.");
            return "";
        }
       
        int registeredVersion = Helper.getIntValueFromPrefs(context, PROPERTY_APP_VERSION);
        
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
        	Helper.printLog(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
       
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(GCMRegIdService.this);
                    }
                    regid = gcm.register(Constants.SENDER_ID);
                    
                    if(!Helper.isNullOrEmpty(regid)){
                    	if(Helper.isOnline(GCMRegIdService.this)){
                    		sendRegistrationIdToBackend();
                    	}
                    }
                } catch (IOException ex) {
                    Helper.printLog("Error in registerInBackground", ex.getMessage());
                }
               
            
    }
	
    
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
    	try{
			JSONObject jsonobj = new JSONObject();
			jsonobj.put(WebConfig.WebParams.USER_ID, Helper.getStringValuefromPrefs(GCMRegIdService.this, SharedPreferencesKey.PREF_USERID));
			jsonobj.put("registrationId", regid);
			
			
			String res = Helper.postMethod(GCMRegIdService.this, GCMRegIdService.this.getString(R.string.url)	+ "UpdateAndroidRegistrationId", jsonobj);
			
			FetchingdataParser parser = new FetchingdataParser(GCMRegIdService.this);
			
			ResponseDto response = parser.getResponseResult(res);
			
			boolean result = Boolean.parseBoolean(response.getSingleResult());
			
			if(result){
				storeRegistrationId(GCMRegIdService.this, regid);
			}
			
			Helper.printLog("UpdateAndroidRegistrationId Input", jsonobj.toString());
			Helper.printLog("UpdateAndroidRegistrationId output", res.toString());
			
    	}catch(Exception e){
    		Helper.printStackTrace(e);
    	}
    	
    	
    }



	
	
}

