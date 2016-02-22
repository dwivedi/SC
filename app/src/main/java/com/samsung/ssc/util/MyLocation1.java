package com.samsung.ssc.util;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.util.MyLocation.LocationResult;

public class MyLocation1 implements ConnectionCallbacks,
		OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	private LocationManager locationManager;
	private LocationResult locationResult;
	private boolean gpsEnabled = false;
	private boolean networkEnabled = false;
	private final static int STARTLOC = 446;

	private static final String TAG = "LocationActivity";
	private static final long INTERVAL = 1000 * 10;
	private static final long FASTEST_INTERVAL = 1000 * 5;
	
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private Context context;
	private String mLastUpdateTime;

	public void getLocation(final Context context, LocationResult result) {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.
		
		this.context = context;
		locationResult = result;
		if (locationManager == null) {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		}

		// exceptions will be thrown if provider is not permitted.
		try {
			gpsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {

		}
		try {
			networkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gpsEnabled || !networkEnabled) {

			Helper.showAlertDialog(
					context,
					SSCAlertDialog.WARNING_TYPE,
					context.getString(R.string.error3),
					context.getString(R.string.error4),
					context.getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							// TODO Auto-generated method stub
							sdAlertDialog.dismiss();
							Intent callGPSSettingIntent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							((Activity) context).startActivityForResult(
									callGPSSettingIntent, STARTLOC);

						}
					}, true, context.getString(R.string.cancel),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							// TODO Auto-generated method stub
							sdAlertDialog.dismiss();
							((Activity) context).finish();
						}
					});

		}
		if (gpsEnabled && networkEnabled && locationManager != null) {

			createLocationRequest();
			buildGoogleApiClient(context);
			connectGoogleApiClient();

		}
	}

/*	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			if (location != null) {
				locationResult.gotLocation(location);
			}
			if (locationListenerGps != null) {
				locationManager.removeUpdates(this);
				locationManager.removeUpdates(locationListenerGps);
			}

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};*/

/*	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			if (location != null) {
				locationResult.gotLocation(location);
			}
			if (locationListenerNetwork != null) {
				locationManager.removeUpdates(this);
				locationManager.removeUpdates(locationListenerNetwork);
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};*/
	

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}

/*	public void removeListener() {
		if (locationManager != null) {
			locationManager.removeUpdates(locationListenerNetwork);
			locationManager.removeUpdates(locationListenerGps);
			locationManager = null;
		}

		if (locationListenerNetwork != null) {
			locationListenerNetwork = null;

		}

		if (locationListenerGps != null) {
			locationListenerGps = null;
		}
	}*/

	public void buildGoogleApiClient(Context context) {

		/**
		 * Builds a GoogleApiClient. Uses the {@code #addApi} method to request
		 * the LocationServices API.
		 */


		Log.i("TAG", "Building GoogleApiClient");
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();

	}

	public void connectGoogleApiClient() {

		mGoogleApiClient.connect();
	}

	protected void createLocationRequest() {

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	@Override
	public void onConnected(Bundle arg0) {

		PendingResult<Status> pendingResult = LocationServices.FusedLocationApi
				.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
						this);
		Log.d(TAG, "Location update started ..............: ");

	}

	@Override
	public void onLocationChanged(Location location) {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
		mGoogleApiClient.disconnect();

		if (context instanceof Activity) {
			Activity mActivity = (Activity) context;
			if (mActivity.isFinishing()) {
				return;
			}
		}
		if (locationResult != null) {
			locationResult.gotLocation(location);
		}
		// TODO Auto-generated method stub
		// Log.d(TAG,
		// "Firing onLocationChanged..............................................");
		// mCurrentLocation = location;
		// mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();

	}
 

}