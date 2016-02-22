package com.samsung.ssc.activitymodule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.database.DatabaseConstants.StoreGeoTagResponseMasterColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyLocation1;
import com.samsung.ssc.util.ScalingUtilities;
import com.samsung.ssc.util.MyLocation1.LocationResult;
import com.samsung.ssc.util.ScalingUtilities.ScalingLogic;

public class Geotag extends BaseActivity implements LoaderCallbacks<Cursor> {

	private ImageView imageViewPhoto;
	private Button mButtonNext;

	private File mGeoTagImagePath = null;
	private static final int REQUEST_CODE_GEO_TAG_IMAGE = 2332;
	private TextView mUserProfileUserLatLocation;
	private TextView mUserProfileUserLongLocation;

	private final static int REQUEST_CODE_GPS_SETTING = 446;
	private GoogleMap map;
	private double latitude, longitude;
	private MyLocation1 myloc;
	private TextView mUserProfileUserAddress;
	private RelativeLayout uploadPicRelativeLayout;
	private boolean userOptionForGEOFrzing = false;
	protected boolean isLocationGet;
	private Location mLocation;
	private StoreBasicModel mStoreBasic;
	private ActivityDataMasterModel mActivityData;
	private long mActivityID = -1;
	boolean isRaceProfile;
	private final int LOADER_ID_GET_ACTIVTY_ID = 1;
	private boolean isUserOptionResponseNeeded = false;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.geotag);
		setUpView();
		getBundleValue();

		isLocationGet = false;

	}

	private void getBundleValue() {

		mStoreBasic = getIntent().getExtras().getParcelable(
				IntentKey.KEY_STORE_BASIC);

		boolean isGeoPhotomandate = Helper.getBoolValueFromPrefs(
				getApplicationContext(),
				SharedPreferencesKey.PREF_IS_GEO_PHOTO_MANDATORY);

		if (isGeoPhotomandate) {
			uploadPicRelativeLayout.setVisibility(View.VISIBLE);
		} else {
			uploadPicRelativeLayout.setVisibility(View.GONE);
		}

		isRaceProfile = Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_RACE_PROFILE);

		// geo pic in mandatory for race profile
		if (isRaceProfile
				&& uploadPicRelativeLayout.getVisibility() != View.VISIBLE) {
			uploadPicRelativeLayout.setVisibility(View.VISIBLE);
		}

		/**
		 * ganrate id
		 */

		Module module = new Module();
		// Geo tag is not module that can be aasigend to user. It is created at
		// runtime if the user has geo tag mandaotry so these values were taken
		// from the deleted geo tag module from SQL Server
		module.setModuleCode(10);
		module.setModuleID(11);
		module.setParentModuleID(9);

		module.setName("GEO TAG");
		module.setIsMandatory(false);
		module.setQuestionType(false);
		module.setStoreWise(true);

		module.setSequence(0);
		mActivityData = Helper.getActivityDataMasterModel(
				getApplicationContext(), module);

		if (mActivityData != null) {

			getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVTY_ID,
					null, this);

			/*
			 * mActivityID =
			 * DatabaseHelper.getConnection(getApplicationContext())
			 * .getActivityIdIfExist(mActivityData);
			 */
		}
	}

	private void setUpView() {

		// setCentretext(getStringFromResource(R.string.geotag));
		imageViewPhoto = (ImageView) this
				.findViewById(R.id.imageViewGeoTapPhoto);
		mButtonNext = (Button) this.findViewById(R.id.btnGeoTagNext);
		uploadPicRelativeLayout = (RelativeLayout) findViewById(R.id.activity_geotag_relativelayout_upload_picture);
		imageViewPhoto.setClickable(false);
		mUserProfileUserLatLocation = (TextView) findViewById(R.id.userProfileUserLatLocationValue);
		mUserProfileUserLongLocation = (TextView) findViewById(R.id.userProfileUserLongLocationValue);
		mUserProfileUserAddress = (TextView) findViewById(R.id.userProfileUserAddressValue);

	}

	@Override
	public boolean validation() {

		if (imageViewPhoto.getTag() != null) {
			return true;

		} else {
			Helper.showAlertDialog(Geotag.this,
					SSCAlertDialog.ERROR_TYPE,
					getString(R.string.error3), getString(R.string.error6),
					getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();

						}
					}, false, null, null);

			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_GPS_SETTING) {
			String provider = Settings.Secure.getString(getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

			if (provider != null) {
				if (Helper.getBoolValueFromPrefs(Geotag.this,
						SharedPreferencesKey.PREF_IS_FREEZE)) {
					fetchLocation(true);

				} else {
					fetchLocation(false);

				}

			} else {
				Helper.showErrorAlertDialog(Geotag.this, getResources()
						.getString(R.string.error3),
						getResources().getString(R.string.error4),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(callGPSSettingIntent,
										REQUEST_CODE_GPS_SETTING);

							}
						});

			}
		}

		switch (resultCode) {

		case RESULT_OK:
			if (requestCode == REQUEST_CODE_GEO_TAG_IMAGE) {
				try {

					Bitmap bitmap = loadPrescaledBitmap(
							mGeoTagImagePath.getAbsolutePath(),
							Constants.QUESTION_IMAGE_WIDTH,
							Constants.QUESTION_IMAGE_HEIGHT);

					if (bitmap == null) {
						bitmap = BitmapFactory.decodeResource(getResources(),
								R.drawable.camera);
						imageViewPhoto.setTag(null);
					} else {
						imageViewPhoto.setImageBitmap(bitmap);
						imageViewPhoto.setTag(mGeoTagImagePath);
					}

				} catch (Exception e) {

					Helper.showCustomToast(getApplicationContext(),
							R.string.unable_to_create_geo_tag,
							Toast.LENGTH_LONG);

				}

			}
			break;
		case RESULT_CANCELED:
			if (requestCode == REQUEST_CODE_GEO_TAG_IMAGE) {

				imageViewPhoto.setImageResource(R.drawable.camera);
				imageViewPhoto.setTag(null);

			}
			break;
		default:
			break;
		}

	}

	private void moveToActivityDashboard() {

		try {

			ContentValues values = new ContentValues();
			values.put(StoreBasicColulmns.KEY_BEAT_IS_COVERAGE, true);
			String where = StoreBasicColulmns.KEY_BEAT_STORE_ID + "=?";
			String selectionArgs[] = new String[] { String.valueOf(mStoreBasic
					.getStoreID()) };
			getContentResolver().update(ProviderContract.URI_STORES_BASICS,
					values, where, selectionArgs);

			/*
			 * DatabaseHelper.getConnection(getApplicationContext())
			 * .updateStoreCoverge(mStoreBasic.getStoreID());
			 */

			mActivityData.setSyncStatus(SyncUtils.SYNC_STATUS_SUBMIT);

			boolean isGeoPhotoMendatory = Helper.getBoolValueFromPrefs(
					getApplicationContext(),
					SharedPreferencesKey.PREF_IS_GEO_PHOTO_MANDATORY);
			boolean isGeoTagMendatory = Helper.getBoolValueFromPrefs(
					getApplicationContext(),
					SharedPreferencesKey.PREF_IS_GEO_TAG_MANDATORY);
			boolean isGeoTagFreezed = Helper.getBoolValueFromPrefs(
					getApplicationContext(),
					SharedPreferencesKey.PREF_IS_FREEZE);

			double latitudeValByServer = Double.parseDouble(Helper
					.getStringValuefromPrefs(Geotag.this,
							SharedPreferencesKey.PREF_GEO_LATTITUDE));
			double longnitudeValByServer = Double.parseDouble(Helper
					.getStringValuefromPrefs(Geotag.this,
							SharedPreferencesKey.PREF_GEO_LOGNITUDE));

			// JSONObject jsonObject = new JSONObject();

			ContentValues contentValues = new ContentValues();

			// jsonObject.put("StoreID", mActivityData.getStoreID());
			contentValues.put("StoreID", mActivityData.getStoreID());

			// jsonObject.put("Latitude", latitude);
			contentValues.put("Latitude", latitude);

			// jsonObject.put("Longitude", longitude);
			contentValues.put("Longitude", longitude);

			// jsonObject.put("UserOption", userOptionForGEOFrzing);
			if (isUserOptionResponseNeeded) {
				contentValues.put("UserOption", userOptionForGEOFrzing);
			} else {
				contentValues.put("UserOption", -1);
			}

			if (mGeoTagImagePath != null) {
				// jsonObject.put("GeoImage",
				// mGeoTagImagePath.getAbsolutePath());
				contentValues.put("GeoImage",
						mGeoTagImagePath.getAbsolutePath());

			}
			// jsonObject.put("IsGeoTagMandatory", isGeoTagMendatory);
			contentValues.put("IsGeoTagMandatory", isGeoTagMendatory);

			// jsonObject.put("IsGeoPhotoMandatory", isGeoPhotoMendatory);
			contentValues.put("IsGeoPhotoMandatory", isGeoPhotoMendatory);

			// jsonObject.put("IsGeoTagFreezed", isGeoTagFreezed);
			contentValues.put("IsGeoTagFreezed", isGeoTagFreezed);

			// jsonObject.put("FreezedLatitude", latitudeValByServer);
			contentValues.put("FreezedLatitude", latitudeValByServer);

			// jsonObject.put("FreezedLongitude", longnitudeValByServer);
			contentValues.put("FreezedLongitude", longnitudeValByServer);

			if (mActivityID == -1) {

				/*
				 * mActivityID = DatabaseHelper.getConnection(
				 * getApplicationContext()).insertActivtyDataMaster(
				 * mActivityData);
				 */

				ContentValues activtyDataContentValues = DatabaseUtilMethods
						.getActivityDataContetnValueArray(mActivityData);
				Uri uri = getContentResolver().insert(
						ProviderContract.URI_ACTIVITY_DATA_RESPONSE,
						activtyDataContentValues);
				mActivityID = ContentUris.parseId(uri);

				// insert store geo
				// jsonObject.put("ActivityID", mActivityID);
				contentValues.put("ActivityID", mActivityID);

				/*
				 * DatabaseHelper.getConnection(getApplicationContext())
				 * .insertStoreGeoTag(jsonObject);
				 */

				getContentResolver().insert(
						ProviderContract.URI_STORE_GEO_TAG_RESPONSE,
						contentValues);

			} else {
				// jsonObject.put("ActivityID", mActivityID);
				// contentValues.put("ActivityID", mActivityID);

				// update store geo
				/*
				 * DatabaseHelper.getConnection(getApplicationContext())
				 * .updateStoreGeoTag(jsonObject);
				 */

				getContentResolver()
						.update(ProviderContract.URI_STORE_GEO_TAG_RESPONSE,
								contentValues,
								StoreGeoTagResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
										+ " = ?",
								new String[] { String.valueOf(mActivityID) });

			}

			Intent intent = new Intent(Geotag.this,
					ActivityDashboardChild.class);
			intent.putExtra(IntentKey.KEY_STORE_BASIC, mStoreBasic);
			intent.putExtra(IntentKey.KEY_MODULE_ID, 4);
			startActivity(intent);

			finish();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * To get the location of user
	 * 
	 * @param isFreez
	 *            , if true the compare the location of user to server received
	 *            location
	 */
	private void fetchLocation(final boolean isFreez) {

		myloc = new MyLocation1();

		LocationResult lr = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				if (location != null) {
					try {
						mLocation = location;

						if (!isLocationGet) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							setUpMapIfNeeded(location);
							mUserProfileUserLatLocation.setText("" + latitude);
							if (isFreez) {
								compareLocatons();
							}
							mUserProfileUserLongLocation
									.setVisibility(View.VISIBLE);
							mUserProfileUserLongLocation
									.setText("" + longitude);
							new GetAddressTask(Geotag.this).execute(location);
						//	myloc.removeListener();
							isLocationGet = true;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		myloc.getLocation(Geotag.this, lr);
	}

	protected void compareLocatons() {

		double latitudeValByServer = Double.parseDouble(Helper
				.getStringValuefromPrefs(Geotag.this,
						SharedPreferencesKey.PREF_GEO_LATTITUDE));
		double longnitudeValByServer = Double.parseDouble(Helper
				.getStringValuefromPrefs(Geotag.this,
						SharedPreferencesKey.PREF_GEO_LOGNITUDE));
		double latServer = Double.parseDouble(new DecimalFormat("#.####")
				.format(latitudeValByServer));

		double longServer = Double.parseDouble(new DecimalFormat("#.####")
				.format(longnitudeValByServer));

		double latClient = Double.parseDouble(new DecimalFormat("#.####")
				.format(latitude));
		double longClinet = Double.parseDouble(new DecimalFormat("#.####")
				.format(longitude));

		if (latServer == latClient && longServer == longClinet) {

			/**
			 * if user is not a Race profile then move to activity dash board
			 * without user intervention but for Race profile Geo Pic has to be
			 * provided by user.
			 * 
			 */

			if (!isRaceProfile) {

				Helper.showAlertDialog(
						Geotag.this,
						SSCAlertDialog.WARNING_TYPE,
						"Conformation",
						"If you do not want to cover this outlet . ",
						"YES",
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();
								moveToActivityDashboard();
							}
						},
						true,
						"NO",
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();
								finish();
							}
						});

			}

		} else {
			showGEOAlertDialog(latitudeValByServer, longnitudeValByServer);
		}

	}

	private void showGEOAlertDialog(double latitudeValByServer,
			double longnitudeValByServer) {
		// isGeoMatched = true;
		Location locationServer = new Location("");
		locationServer.setLatitude(latitudeValByServer);
		locationServer.setLongitude(longnitudeValByServer);

		Location locationClient = new Location("");
		locationClient.setLatitude(latitude);
		locationClient.setLongitude(longitude);

		String latServer = new DecimalFormat("#.####")
				.format(latitudeValByServer);
		String longServer = new DecimalFormat("#.####")
				.format(longnitudeValByServer);

		String latClient = new DecimalFormat("#.####").format(latitude);
		String longClinet = new DecimalFormat("#.####").format(longitude);

		float distanceInMeters = locationServer.distanceTo(locationClient);

		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			builder = new AlertDialog.Builder(this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		} else {
			builder = new AlertDialog.Builder(this);

		}
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("GEO FREEZING");
		/*
		 * String storeName = Helper.getStringValuefromPrefs(Geotag.this,
		 * Constants.PREF_ACCOUNTNAME);
		 */String storeName = mStoreBasic.getStoreName();
		double destanceKM = (distanceInMeters * 0.001);
		DecimalFormat df = new DecimalFormat("#.0000");
		String distanceLocation = df.format(destanceKM);

		String red = storeName + "\nPresent Location:[" + latClient + ", "
				+ longClinet + "]\nLocation of Store [" + latServer + ","
				+ longServer + "] \nDistance From Store " + distanceLocation
				+ "km\n\n\nYes (to proceeds with Geo Tag updation) "
				+ "\nNo (to proceeds without updating Geo Tag) " + "\n"
				+ "If you do not want to cover this outlet please press back. ";

		String subRed = "If you do not want to cover this outlet please press back. ";
		SpannableString blueSpannable = new SpannableString(red);
		blueSpannable.setSpan(new ForegroundColorSpan(Color.RED), red.length()
				- subRed.length(), red.length(), 0);

		/*
		 * builder.setMessage(storeName + "\nPresent Location:[" + latClient +
		 * ", " + longClinet + "]\nLocation of Store [" + latServer + "," +
		 * longServer + "] \nDistance From Store " + distanceLocation +
		 * "km\n\n\nYes (to proceeds with Geo Tag updation) " +
		 * "\nNo (to proceeds without updating Geo Tag) " +
		 * "\n"+"If you do not want to cover this outlet please press back. ");
		 */

		builder.setMessage(blueSpannable);

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				userOptionForGEOFrzing = true;
			}
		});

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				userOptionForGEOFrzing = false;

				if (!isRaceProfile) {
					moveToActivityDashboard();
				}

			}
		});
		builder.create().show();
		isUserOptionResponseNeeded = true;

	}

	private void setUpMapIfNeeded(Location loc) {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			// map =((MapFragment)
			// getFragmentManager().findFragmentById(R.id.map)).getMap();
			Fragment fragment = getSupportFragmentManager().findFragmentById(
					R.id.map);
			SupportMapFragment mapFragment = (SupportMapFragment) fragment;
			map = mapFragment.getMap();

			// Check if we were successful in obtaining the map.
			if (map != null) {

				map.setMyLocationEnabled(false);
				map.getUiSettings().setCompassEnabled(true);
				// The Map is verified. It is now safe to manipulate the map.
				map.addMarker(new MarkerOptions().position(new LatLng(loc
						.getLatitude(), loc.getLongitude())));
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						loc.getLatitude(), loc.getLongitude()), 14));
				// map.getUiSettings().setScrollGesturesEnabled(false);
				// map.getUiSettings().setZoomControlsEnabled(false);
				map.getUiSettings().setAllGesturesEnabled(false);
				map.getUiSettings().setZoomControlsEnabled(false);

			}
		}
	}

	public void onPhotoCaptureClick(View view) {
		try {
			String fileName = "geotag_"
					+ Helper.getStringValuefromPrefs(Geotag.this,
							SharedPreferencesKey.PREF_STOREID) + ".jpg";
			File folder = new File(FileDirectory.DIRECTORY_GEOTAG_IMAGE);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			mGeoTagImagePath = new File(folder, fileName);
			if (!mGeoTagImagePath.exists()) {
				mGeoTagImagePath.createNewFile();
			}

			Uri mImageUri = Uri.fromFile(mGeoTagImagePath);

			Intent cameraActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraActivity.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageUri);
			startActivityForResult(cameraActivity, REQUEST_CODE_GEO_TAG_IMAGE);

		} catch (IOException e) {

			Helper.showCustomToast(getApplicationContext(),
					R.string.unable_to_create_geo_tag, Toast.LENGTH_LONG);
		}
	}

	public void onNextButtonClick(View view) {
		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_GEO_PHOTO_MANDATORY)) {
			if (validation()) {

				moveToActivityDashboard();
			}
		} else if (isRaceProfile) {
			if (validation()) {

				moveToActivityDashboard();
			}
		}

		else {
			moveToActivityDashboard();
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//myloc.removeListener();

	}

	protected class GetAddressTask extends AsyncTask<Location, Void, String> {

		// Store the context passed to the AsyncTask when the system
		// instantiates it.
		Context localContext;

		// Constructor called by the system to instantiate the task
		public GetAddressTask(Context context) {

			// Required by the semantics of AsyncTask
			super();

			// Set a Context for the background task
			localContext = context;
		}

		/**
		 * Get a geocoding service instance, pass latitude and longitude to it,
		 * format the returned address, and return the address to the UI thread.
		 */
		@Override
		protected String doInBackground(Location... params) {
			/*
			 * Get a new geocoding service instance, set for localized
			 * addresses. This example uses android.location.Geocoder, but other
			 * geocoders that conform to address standards can also be used.
			 */
			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

			// Get the current location from the input parameter list
			Location location = params[0];

			// Create a list to contain the result address
			List<Address> addresses = null;

			// Try to get an address for the current location. Catch IO or
			// network problems.
			try {

				/*
				 * Call the synchronous getFromLocation() method with the
				 * latitude and longitude of the current location. Return at
				 * most 1 address.
				 */
				addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);

				// Catch network or other I/O problems.
			} catch (IOException exception1) {

				// Log an error and return an error message

				// print the stack trace
				exception1.printStackTrace();

				// Return an error message
				return null;

				// Catch incorrect latitude or longitude values
			} catch (IllegalArgumentException exception2) {

				// Log the error and print the stack trace
				exception2.printStackTrace();

				return null;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {

				// Get the first address
				Address address = addresses.get(0);

				StringBuilder strReturnedAddress = new StringBuilder();
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
					strReturnedAddress.append(address.getAddressLine(i));
				}

				String string = strReturnedAddress.toString();

				// Return the text
				return string;
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result != null) {

				mUserProfileUserAddress.setText("" + result);
			} else
				mUserProfileUserAddress.setText("Address not found");

			mButtonNext.setVisibility(View.VISIBLE);
			imageViewPhoto.setClickable(true);
		}

	}

	private Bitmap loadPrescaledBitmap(String path, int desireWidth,
			int desireHeight) {

		Bitmap scaledBitmap = null;
		Bitmap unscaledBitmap = null;

		try {
			// Part 1: Decode image
			unscaledBitmap = ScalingUtilities.decodeFile(path, desireWidth,
					desireHeight, ScalingLogic.FIT);

			if (!(unscaledBitmap.getWidth() <= desireWidth && unscaledBitmap
					.getHeight() <= desireHeight)) {
				// Part 2: Scale image
				scaledBitmap = ScalingUtilities.createScaledBitmap(
						unscaledBitmap, desireWidth, desireHeight,
						ScalingLogic.FIT);

			} else {

				return compressBitmapImage(path, unscaledBitmap);
			}

		} catch (Throwable e) {
		}

		return compressBitmapImage(path, scaledBitmap);
	}

	private Bitmap compressBitmapImage(String path,
			Bitmap scaledOrUnscaledBitmap) {
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			scaledOrUnscaledBitmap
					.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return scaledOrUnscaledBitmap;
	}

	public void onGetAddressClick(View view) {

		if (mLocation != null) {
			new GetAddressTask(Geotag.this).execute(mLocation);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {

		if (id == LOADER_ID_GET_ACTIVTY_ID) {
			String[] arg = { String.valueOf(mActivityData.getStoreID()),
					String.valueOf(mActivityData.getModuleCode()) };

			return new CursorLoader(getApplicationContext(),
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null,
					arg, null);
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (loader.getId() == LOADER_ID_GET_ACTIVTY_ID) {

			if (cursor != null && cursor.getCount() > 0) {

				mActivityID = DatabaseUtilMethods.getActivityID(cursor);

			}

			if (Helper.getBoolValueFromPrefs(Geotag.this,
					SharedPreferencesKey.PREF_IS_FREEZE)) {
				fetchLocation(true);
			} else {
				fetchLocation(false);
			}

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
