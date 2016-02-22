package com.samsung.ssc.util;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;

public class MyLocation {
	private LocationManager locationManager;
	private LocationResult locationResult;
	private boolean gpsEnabled=false;
	private boolean networkEnabled=false;
    private final static int STARTLOC=446;
    

    public void getLocation(final Context context, LocationResult result)
    {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult=result;
        if(locationManager == null){
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        //exceptions will be thrown if provider is not permitted.
        try{
        	gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){
        	
        }
        try{networkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gpsEnabled || !networkEnabled){
        	
        	
        	
        
        	
        	Helper.showAlertDialog(context, SSCAlertDialog.WARNING_TYPE, context.getString(R.string.error3), context.getString(R.string.error4), context.getString(R.string.ok), new SSCAlertDialog.OnSDAlertDialogClickListener() {
				
				@Override
				public void onClick(SSCAlertDialog sdAlertDialog) {
					// TODO Auto-generated method stub
					sdAlertDialog.dismiss();
					 Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		        	 ((Activity) context).startActivityForResult(callGPSSettingIntent,STARTLOC);
					
				}
			}, true, context.getString(R.string.cancel), new SSCAlertDialog.OnSDAlertDialogClickListener() {
				
				@Override
				public void onClick(SSCAlertDialog sdAlertDialog) {
					// TODO Auto-generated method stub
					sdAlertDialog.dismiss();
					((Activity) context).finish();
				}
			});
        	

        }
        if(gpsEnabled &&  networkEnabled && locationManager != null){
        	 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
           	 locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
           	 new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
						if(locationListenerGps!=null){
				             locationManager.removeUpdates(locationListenerGps);

						}
						if(locationListenerNetwork!=null){
				             locationManager.removeUpdates(locationListenerNetwork);

						}

		             Location net_loc = null, gps_loc = null;
		             if(gpsEnabled){
		            	 if(locationManager!= null){
			                 gps_loc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		            	 }
		            	 }
		             if(networkEnabled){
		            	 if(locationManager!= null){
		                 net_loc=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		            	 }
		             }
		             //if there are both values use the latest one
		             if(gps_loc!=null && net_loc!=null){
		                 if(gps_loc.getTime()>net_loc.getTime()){
		                     locationResult.gotLocation(gps_loc);
		                 }
		                 else{
		                     locationResult.gotLocation(net_loc);
		                 }
		                 return;
		             }

		             if(gps_loc!=null ){
		                 locationResult.gotLocation(gps_loc);
		                 return;
		             }
		             if(net_loc!=null){
		                 locationResult.gotLocation(net_loc);
		                 return;
		             }
		             locationResult.gotLocation(null);
		        
					
				}
			}, 20000);
           	 
           	 
           	 
        } 
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if(location!=null){
            	locationResult.gotLocation(location);
            	}
            if(locationListenerGps!=null){
                locationManager.removeUpdates(this);
                locationManager.removeUpdates(locationListenerGps);
            }

        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
        	 if(location!=null){
             	locationResult.gotLocation(location);
             	}
             if(locationListenerNetwork!=null){
                 locationManager.removeUpdates(this);
                 locationManager.removeUpdates(locationListenerNetwork);
             }
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };


    public static abstract class LocationResult{
        public abstract void gotLocation(Location location);
    }
    
    public void removeListener(){
    	if(locationManager!=null){
       	 locationManager.removeUpdates(locationListenerNetwork);
       	 locationManager.removeUpdates(locationListenerGps);
       	 locationManager=null;
        }
    	
    	if (locationListenerNetwork != null) {
    		 locationListenerNetwork = null;
    		 
         }
    	 
         if (locationListenerGps != null) {
        	 locationListenerGps = null;
         }
     }
 }