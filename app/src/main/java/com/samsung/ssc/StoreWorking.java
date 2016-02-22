/**
 * 
 */
package com.samsung.ssc;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.activitymodule.ActivityDashboardChild;
import com.samsung.ssc.activitymodule.Geotag;
import com.samsung.ssc.activitymodule.OutletProfile;
import com.samsung.ssc.activitymodule.OutletProfile2;
import com.samsung.ssc.adapters.OutletWorkingOtherStoreAdapter;
import com.samsung.ssc.adapters.SeparatedListAdapter;
import com.samsung.ssc.adapters.SeparatedListItemAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncMaster;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyLocation;
import com.samsung.ssc.util.MyLocation.LocationResult;
import com.samsung.ssc.util.OnOkListener;
import com.samsung.ssc.util.SyncPreparationCompeteCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author d.ashish
 * 
 */
public class StoreWorking extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private boolean isLocationFinded = false;
	private ContentResolver mContentResolver;
	protected Location mLocation;
	private ListView lvStoreList;
	private AutoCompleteTextView mActv_cities;
	private EditText mEt_other_stores;
	private OutletWorkingOtherStoreAdapter mAdapter;
	private static final int LOADER_STORES = 1;
	// private final int LOADER_GET_OTHER_STORES_CITY = 3;
	private final int LOADER_GET_OTHER_STROES_BY_CITY = 4;

	private String mSearchedCityName = "";
	private ArrayAdapter<String> mAdapterCity;
	private LoaderManager mLoaderManager;
	private List<StoreBasicModel> listBasicModel;
	private ProgressDialog progressDialog;
	private final static int REQUEST_CODE_GPS_SETTING = 446;
	private boolean isMovedToNextActivity=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_store_working);

		Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
		toolbar.setTitle("Store Working");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mContentResolver = getContentResolver();

		setUpView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// reset
		isMovedToNextActivity=false;

		//mActv_cities.setText("");
	//	mEt_other_stores.setText("");
		
		
		if (checkLocationEnabled()) {
			fetchinglocation();
		} else {
			moveToLocatinSettingScreen();
		}
	}
	
	/*@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		if (isLocationFinded) {
			initializeStoreLoader();
		}

	}*/

	private void getTodaysStoresFromServerVolley() {

		VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
				StoreWorking.this,
				StoreWorking.this
						.getStringFromResource(R.string.loadingmessage),
				new VolleyGetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						try {
							JSONObject jsonObject = new JSONObject(result
									.toString());

							if (jsonObject.getBoolean("IsSuccess")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("Result");
								if (jsonArray != null && jsonArray.length() > 0) {

									ContentValues[] contentValues = DatabaseUtilMethods
											.getContentValuesStoresBaic(
													jsonArray,
													Constants.STORE_TYPE_TODAY,
													mContentResolver,
													StoreWorking.this
															.getApplicationContext());

									mContentResolver.bulkInsert(
											ProviderContract.URI_STORES_BASICS,
											contentValues);

								}

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onError(VolleyError error) {

					}

				});

		JSONObject jsonobj = new JSONObject();
		try {

			jsonobj.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(StoreWorking.this,
							SharedPreferencesKey.PREF_USERID)));

		} catch (NumberFormatException e) {
			Helper.printStackTrace(e);
		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getStringFromResource(R.string.url),
				"GetStoresForTodayBeat");
		pdn.setRequestData(jsonobj);
		pdn.callWebService();

	}

	private void fetchinglocation() {
		showProgress();
		final MyLocation myloc = new MyLocation();

		LocationResult lr = new LocationResult() {
			@Override
			public void gotLocation(Location location) {

				hideProgress();
				
				if (!isLocationFinded) {
				
					if (location != null) {
						try {
							isLocationFinded = true;
							//setUpView();
							initializeStoreLoader();

							if (Helper.isOnline(getApplicationContext())
									&& Helper
											.getStringValuefromPrefs(
													StoreWorking.this,
													SharedPreferencesKey.PREF_IS_ROAMING_PROFILE)
											.equalsIgnoreCase("false")
									&& DatabaseHelper.getConnection(
											getApplicationContext())
											.isTodaysBeatEmpty()) {

								getTodaysStoresFromServerVolley();
								

							}

							mLocation = location;
							if (myloc != null) {
								myloc.removeListener();

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {

					
						
						
						if(!isMovedToNextActivity)
						{
							Helper.showNotCancelableAlertDialog(
									StoreWorking.this,
									SSCAlertDialog.ERROR_TYPE,
									"Error",
									"Unable to fetch your current location",
									"OK",
									new SSCAlertDialog.OnSDAlertDialogClickListener() {

										@Override
										public void onClick(
												SSCAlertDialog sdAlertDialog) {

											sdAlertDialog.dismiss();

											StoreWorking.this.finish();

										}
									}, false, null, null);
						}
					

					}

				}

			}

		};
		myloc.getLocation(StoreWorking.this, lr);
	}

	private void initializeStoreLoader() {
		mLoaderManager = getSupportLoaderManager();

		
		Loader<Cursor> loader = mLoaderManager.getLoader(LOADER_STORES);
		if (loader == null) {
			mLoaderManager.initLoader(LOADER_STORES, null, this);
		} else {
			mLoaderManager.restartLoader(LOADER_STORES, null, this);
		}

	}

	private void setUpView() {

		lvStoreList = (ListView) this.findViewById(R.id.listViewStoreList);
		lvStoreList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					StoreBasicModel selectedStore = (StoreBasicModel) parent
							.getItemAtPosition(position);
					onListItemsClick(selectedStore, position);

				} catch (Exception e) {
				}

			}
		});

		mActv_cities = (AutoCompleteTextView) this
				.findViewById(R.id.actv_cityName_otherStoreFragment);

		mEt_other_stores = (EditText) this
				.findViewById(R.id.et_stroeName_otherStoreFragment);

		//initializeStoreLoader();
		// mLoaderManager.initLoader(LOADER_GET_OTHER_STORES_CITY, null, this);

		mActv_cities.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					InputMethodManager imm = (InputMethodManager) StoreWorking.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mActv_cities.getWindowToken(),
							0);
					mSearchedCityName = (String) parent
							.getItemAtPosition(position);
					if (!mSearchedCityName.equalsIgnoreCase("")) {
						mEt_other_stores.setText("");

						Loader loader = mLoaderManager
								.getLoader(LOADER_GET_OTHER_STROES_BY_CITY);
						if (loader == null) {
							mLoaderManager.initLoader(
									LOADER_GET_OTHER_STROES_BY_CITY, null,
									StoreWorking.this);
						} else {
							mLoaderManager.restartLoader(
									LOADER_GET_OTHER_STROES_BY_CITY, null,
									StoreWorking.this);
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		mEt_other_stores.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				try {
					mAdapter.filter(mEt_other_stores.getText().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mActv_cities.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				try {
					if (mActv_cities.getText().toString().equalsIgnoreCase("")) {
						if (mAdapter != null && listBasicModel != null) {

							List<StoreBasicModel> listSotreBasics = new ArrayList<StoreBasicModel>();
							listSotreBasics.addAll(listBasicModel);
							mAdapter.addSotres(listSotreBasics);
							mEt_other_stores.setText("");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void onListItemsClick(final StoreBasicModel storeBasicModel,
			int position) {

		if (storeBasicModel.getStoreType().equalsIgnoreCase(
				Constants.STORE_TYPE_OTHER)) {

			if (storeBasicModel != null) {

				if (Helper.getBoolValueFromPrefs(StoreWorking.this,
						SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
					moveToNextActivity(storeBasicModel);

				} else {
					syncDataIfAvailable(storeBasicModel);

				}

			}

		} else if (Helper.getBoolValueFromPrefs(StoreWorking.this,
				SharedPreferencesKey.PREF_IS_GEO_FENCING_APPLICABLE)
				&& storeBasicModel.getStoreType().equalsIgnoreCase(
						Constants.STORE_TYPE_TODAY)
				&& (storeBasicModel.getStoreDistance() <= Constants.STORE_FENCE_DISTANCE | storeBasicModel
				// 1000 indicate store is not geo freezed
						.getStoreDistance() == 1000)) {
			if (Helper.getBoolValueFromPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
				moveToNextActivity(storeBasicModel);

			} else {
				syncDataIfAvailable(storeBasicModel);

			}

		} else if (!Helper.getBoolValueFromPrefs(StoreWorking.this,
				SharedPreferencesKey.PREF_IS_GEO_FENCING_APPLICABLE)
				&& storeBasicModel.getStoreType().equalsIgnoreCase(
						Constants.STORE_TYPE_TODAY)) {

			if (Helper.getBoolValueFromPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
				moveToNextActivity(storeBasicModel);

			} else {
				syncDataIfAvailable(storeBasicModel);

			}

		} else {
			Helper.showAlertDialog(StoreWorking.this,
					SSCAlertDialog.ERROR_TYPE, "Alert!",
					"Selected store is not in "
							+ Constants.STORE_FENCE_DISTANCE
							+ " KM range, kindly select stores within "
							+ Constants.STORE_FENCE_DISTANCE + " KM only.",
					"OK",
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();

						}
					}, false, null, null);
		}

	}

	private void moveToNextActivity(StoreBasicModel storeBasicModel) {
		 isLocationFinded = false;
		 isMovedToNextActivity=true;
		 
		saveStoreProfilePrefresnce(storeBasicModel);
		if (true){
			Intent intent = new Intent(StoreWorking.this, OutletProfile.class);
			intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
			startActivity(intent);
		}else

		if (Helper.getBoolValueFromPrefs(StoreWorking.this,
				SharedPreferencesKey.PREF_IS_STORE_PROFILE_VISIBLE)) {
			Intent intent = new Intent(StoreWorking.this, OutletProfile.class);
			intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
			startActivity(intent);
		} else {

			if (Helper.getBoolValueFromPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_IS_GEO_TAG_MANDATORY)) {
				Intent intent = new Intent(StoreWorking.this, Geotag.class);
				intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
				startActivity(intent);
			} else {
				DatabaseHelper.getConnection(StoreWorking.this)
						.updateStoreCoverge(storeBasicModel.getStoreID());

				Intent intent = new Intent(StoreWorking.this,
						ActivityDashboardChild.class);
				intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
				intent.putExtra(IntentKey.KEY_MODULE_ID, 4);
				startActivity(intent);

			}

		}

		mEt_other_stores.setText("");
		mActv_cities.setText("");
		
		//
	}

	private void syncDataIfAvailable(final StoreBasicModel storeBasicModel) {
		long mStoreID = storeBasicModel.getStoreID();

		String pref_stroeID = Helper.getStringValuefromPrefs(StoreWorking.this,
				SharedPreferencesKey.PREF_STOREID);

		// if previously visited store is not the current selected store
		if (!Helper.getStringValuefromPrefs(StoreWorking.this,
				SharedPreferencesKey.PREF_STOREID).equalsIgnoreCase("")
				&& Long.parseLong(pref_stroeID) != mStoreID) {

			Map<String, ArrayList<String>> pandingMoudleList = DatabaseHelper
					.getConnection(StoreWorking.this)
					.getPendingMendantoryModuleList();

			if (pandingMoudleList.size() == 0) {
				final OnOkListener okListener = new OnOkListener() {

					@Override
					public void onOKPressed() {

						try {

							moveToNextActivity(storeBasicModel);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				final SyncMaster master1 = new SyncMaster(StoreWorking.this);

				SyncPreparationCompeteCallback callback = new SyncPreparationCompeteCallback() {

					@Override
					public void onComplete(
							ArrayList<ActivityDataMasterModel> data) {

						if (data != null && data.size() > 0) {

							if (!Helper.isOnline(StoreWorking.this)) {

								Toast.makeText(StoreWorking.this,
										R.string.not_online, Toast.LENGTH_LONG)
										.show();

							} else {

								master1.sync(data, false, okListener);
							}

						} else {

							try {

								moveToNextActivity(storeBasicModel);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};

				master1.getSyncData(callback);

			} else {

				SeparatedListAdapter adapter = new SeparatedListAdapter(
						StoreWorking.this);
				Set<String> keySet = pandingMoudleList.keySet();
				for (String key : keySet) {
					pandingMoudleList.get(key);
					SeparatedListItemAdapter itemAdapter = new SeparatedListItemAdapter(
							StoreWorking.this, 0, pandingMoudleList.get(key));
					adapter.addSection(key, itemAdapter);
				}

				SSCAlertDialog alertDialog = new SSCAlertDialog(
						StoreWorking.this, SSCAlertDialog.LIST_TYPE)
						.setTitleText("Pending Modules")
						.setListAdapter(adapter)
						.setEnableConfirmButton(true)
						.setConfirmText("OK")
						.setConfirmClickListener(
								new SSCAlertDialog.OnSDAlertDialogClickListener() {

									@Override
									public void onClick(
											SSCAlertDialog sdAlertDialog) {

										sdAlertDialog.dismiss();

									}

								}).showCancelButton(false).setCancelText(null)
						.setCancelClickListener(null);
				alertDialog.setCancelable(false);

				alertDialog.show();

			}

		} else {
			try {

				/*
				 * getTodaySingleStoreBasicPlusDetailDataFromDB( mStoreID, new
				 * GetTodaySingleStoreBaiscPlusDetailDataFromDBHandler(
				 * OutletWorkingFragmentTodayStores.this));
				 */

				moveToNextActivity(storeBasicModel);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stores the store profile detail in shared preference
	 * 
	 * @param storeDataModal
	 */
	private void saveStoreProfilePrefresnce(StoreBasicModel storeDataModal) {

		try {

			Helper.saveLongValueInPref(StoreWorking.this,
					SharedPreferencesKey.PREF_SELECTED_OUTLET_STORE_ID,
					(int) storeDataModal.getStoreID());

			Helper.saveBoolValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_ROAMINGUSER, true);
			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_STOREID,
					String.valueOf(storeDataModal.getStoreID()));
			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_COVERAGEID,
					String.valueOf(storeDataModal.getCoverageID()));

			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_ACCOUNTNAME, storeDataModal
							.getStoreName().toString());
			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_STORECODE, storeDataModal
							.getStoreCode().toString());
			Helper.saveBoolValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_IS_FREEZE,
					storeDataModal.isIsFreeze());
			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_GEO_LATTITUDE,
					String.valueOf(storeDataModal.getFreezeLattitude()));
			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_GEO_LOGNITUDE,
					String.valueOf(storeDataModal.getFreezeLongitude()));
			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_STORE_CHANNEL_TYPE,
					String.valueOf(storeDataModal.getChannelType()));

			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_STORE_STORE_CLASS,
					String.valueOf(storeDataModal.getStoreClass()));

			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_STORE_STORE_SIZE,
					String.valueOf(storeDataModal.getStoreSize()));

			Helper.saveBoolValueInPrefs(
					StoreWorking.this,
					SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE,
					storeDataModal.isIsDisplayCounterShare());
			Helper.saveBoolValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_STORE_IS_PLANOGRAM,
					storeDataModal.isIsPlanogram());

			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_COVERAGEID, "");
			Helper.saveStringValueInPrefs(StoreWorking.this,
					SharedPreferencesKey.PREF_BEATPLANDATE, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		Loader<Cursor> loader = null;

		switch (loaderID) {
		case LOADER_STORES:

		
			loader = new CursorLoader(StoreWorking.this,
					ProviderContract.URI_STORES_BASICS, null, null, null, null);
			break;

		/*
		 * case LOADER_GET_OTHER_STORES_CITY:
		 * 
		 * loader = new CursorLoader(StoreWorking.this,
		 * ProviderContract.URI_OTHER_STORES_CITIES, null, null, null, null);
		 * 
		 * break;
		 */

		case LOADER_GET_OTHER_STROES_BY_CITY:

			String selectionForcity = StoreBasicColulmns.KEY_BEAT_CITY + "=?";

			String selectionArgsForCity[] = { mSearchedCityName };

			loader = new CursorLoader(StoreWorking.this,
					ProviderContract.URI_GET_OTHER_STORES_BASICS_BY_CITY, null,
					selectionForcity, selectionArgsForCity, null);

			break;

		default:
			break;
		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (loader != null) {

			switch (loader.getId()) {
			case LOADER_STORES:

				if (cursor != null && cursor.moveToFirst()) {

					listBasicModel = DatabaseUtilMethods
							.getStoresBasics(
									StoreWorking.this,
									cursor,
									Helper.getBoolValueFromPrefs(
											getApplicationContext(),
											SharedPreferencesKey.PREF_IS_GEO_FENCING_APPLICABLE),
									mLocation);
					Collections
							.sort(listBasicModel, new StoreColorComparator());

					mAdapter = new OutletWorkingOtherStoreAdapter(
							getApplicationContext(), listBasicModel);
					lvStoreList.setAdapter(mAdapter);

					ArrayList<String> citys = new ArrayList<String>();
					for (int i = 0; i < listBasicModel.size(); i++) {
						StoreBasicModel modal = listBasicModel.get(i);
						String city = modal.getCityName();
						if (!citys.contains(city)) {
							citys.add(city);
						}
					}

					mAdapterCity = new ArrayAdapter<String>(StoreWorking.this,
							android.R.layout.simple_dropdown_item_1line,
							citys.toArray(new String[citys.size()]));
					if (mActv_cities != null) {
						mActv_cities.setAdapter(mAdapterCity);
					}
				}

				break;

			/*
			 * case LOADER_GET_OTHER_STORES_CITY:
			 * 
			 * List<String> lvCities = DatabaseUtilMethods
			 * .getOtherStoreCitiesfromCursor(cursor);
			 * 
			 * mAdapterCity = new ArrayAdapter<String>(StoreWorking.this,
			 * android.R.layout.simple_dropdown_item_1line, lvCities);
			 * 
			 * if (mActv_cities != null) {
			 * mActv_cities.setAdapter(mAdapterCity); }
			 * 
			 * break;
			 */

			case LOADER_GET_OTHER_STROES_BY_CITY:

				/*
				 * List<StoreBasicModel> listBasicModelByCity =
				 * DatabaseUtilMethods .getStoresBasics(cursor,
				 * Constants.STORE_TYPE_TODAY);
				 * 
				 * if (mAdapter != null) {
				 * mAdapter.addSotres(listBasicModelByCity); }
				 */

				List<StoreBasicModel> listBasicModel = DatabaseUtilMethods
						.getStoresBasics(
								StoreWorking.this,
								cursor,
								Helper.getBoolValueFromPrefs(
										getApplicationContext(),
										SharedPreferencesKey.PREF_IS_GEO_FENCING_APPLICABLE),
								mLocation);
				Collections.sort(listBasicModel, new StoreComparator());

				if(mAdapter==null)
				{
					mAdapter = new OutletWorkingOtherStoreAdapter(
							getApplicationContext(), listBasicModel);
					lvStoreList.setAdapter(mAdapter);
				}
				else
				{
					mAdapter.addSotres(listBasicModel);
				}
				

			default:
				break;
			}
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		

	}

	class StoreComparator implements Comparator<StoreBasicModel> {

		@Override
		public int compare(StoreBasicModel lhs, StoreBasicModel rhs) {

			String lhsStoreType = lhs.getStoreType();
			String rhsStoreType = rhs.getStoreType();

			if (lhsStoreType.equals(rhsStoreType)) {

				double lhsStoreDistance = lhs.getStoreDistance();
				double rhsStoreDistance = rhs.getStoreDistance();

				if (lhsStoreDistance < rhsStoreDistance) {
					return -1;
				} else if (lhsStoreDistance > rhsStoreDistance) {
					return 1;
				} else {
					return 0;
				}

			}

			if (lhsStoreType.equals(Constants.STORE_TYPE_TODAY))
				return -1;
			if (rhsStoreType.equals(Constants.STORE_TYPE_TODAY))
				return 1;

			return 0;

		}
	}

	class StoreColorComparator implements Comparator<StoreBasicModel> {

		@Override
		public int compare(StoreBasicModel lhs, StoreBasicModel rhs) {

			int lhsStoreColorIndex = lhs.getStoreColorIndex();
			int rhsStoreColorIndex = rhs.getStoreColorIndex();

			if (lhsStoreColorIndex == rhsStoreColorIndex) {
				if (lhs.getStoreDistance() < rhs.getStoreDistance()) {
					return -1;
				} else if (lhs.getStoreDistance() > rhs.getStoreDistance()) {
					return 1;
				} else {
					return 0;
				}

			} else {
				if (lhsStoreColorIndex < rhsStoreColorIndex) {
					return -1;
				} else if (lhsStoreColorIndex > rhsStoreColorIndex) {
					return 1;
				}
			}

			return 0;

		}

	}

	private void showProgress() {
		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				/*
				 * progressDialog = new ProgressDialog(context,
				 * ProgressDialog.THEME_HOLO_LIGHT);
				 */

				progressDialog = SSCProgressDialog.ctor(this);

			} else {
				progressDialog = new ProgressDialog(this);
				progressDialog.setProgress(0);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(false);
			}

			progressDialog.show();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void hideProgress() {

		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

	}

	private boolean checkLocationEnabled() {

		LocationManager locationManager = null;

		boolean gpsEnabled = false;
		boolean networkEnabled = false;

		if (locationManager == null) {
			locationManager = (LocationManager) this
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

			return false;
		}

		return true;
	}

	private void moveToLocatinSettingScreen() {

		Helper.showNotCancelableAlertDialog(
				this,
				SSCAlertDialog.WARNING_TYPE,
				this.getString(R.string.error3),
				getString(R.string.error4),
				getString(R.string.ok),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						Intent callGPSSettingIntent = new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivityForResult(callGPSSettingIntent,
								REQUEST_CODE_GPS_SETTING);

					}
				}, true, this.getString(R.string.cancel),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						finish();
					}
				});
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_GPS_SETTING) {

			if (checkLocationEnabled()) {

				fetchinglocation();

			} else {
				moveToLocatinSettingScreen();

			}
		}
	}*/
}