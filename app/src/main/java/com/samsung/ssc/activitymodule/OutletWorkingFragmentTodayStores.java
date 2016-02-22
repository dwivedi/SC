package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.OutletWorkingAdapter1;
import com.samsung.ssc.adapters.SeparatedListAdapter;
import com.samsung.ssc.adapters.SeparatedListItemAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncMaster;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.OnOkListener;
import com.samsung.ssc.util.SyncPreparationCompeteCallback;

public class OutletWorkingFragmentTodayStores extends Fragment implements
		LoaderCallbacks<Cursor> {

	private OutletWorking1 mParentActivity;
	private View _rootView;
	private ListView mListViewTodayStores;
	private TextView mTextViewTodayStoreNotAvailble;

	private long mStoreID;
	private final int LOADER_ID = 1;
	private ContentResolver mContentResolver;

	public void onResume() {
		super.onResume();
		
		LoaderManager loaderManager = mParentActivity
				.getSupportLoaderManager();
		Loader<Cursor> loader = loaderManager.getLoader(LOADER_ID);
		// check if loader is already created earlier
		if (loader == null) {
			loaderManager.initLoader(LOADER_ID, null, this);
		} else {
			// restart loader for re-querying to change color for visited
			// stores to green
			loaderManager.restartLoader(LOADER_ID, null, this);
		}

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
	

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OutletWorking1) {
			mParentActivity = (OutletWorking1) activity;
			mContentResolver = mParentActivity.getContentResolver();
		}
	}

	/**
	 * Save data in pref
	 * 
	 * @param storeBasicModel
	 */
	private void saveStoreProfilePrefresnce(StoreBasicModel storeBasicModel) {
		try {

			Helper.saveLongValueInPref(mParentActivity,
					SharedPreferencesKey.PREF_SELECTED_OUTLET_STORE_ID,
					(int) storeBasicModel.getStoreID());

		 
			Helper.saveBoolValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_ROAMINGUSER, false);
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STOREID,
					String.valueOf(storeBasicModel.getStoreID()));
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_COVERAGEID,
					String.valueOf(storeBasicModel.getCoverageID()));

			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_ACCOUNTNAME, storeBasicModel
							.getStoreName().toString());
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORECODE, storeBasicModel
							.getStoreCode().toString());
			Helper.saveBoolValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_IS_FREEZE,
					storeBasicModel.isIsFreeze());
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_GEO_LATTITUDE,
					String.valueOf(storeBasicModel.getFreezeLattitude()));
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_GEO_LOGNITUDE,
					String.valueOf(storeBasicModel.getFreezeLongitude()));
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_CHANNEL_TYPE,
					String.valueOf(storeBasicModel.getChannelType()));

			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_STORE_CLASS,
					String.valueOf(storeBasicModel.getStoreClass()));

			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_STORE_SIZE,
					String.valueOf(storeBasicModel.getStoreSize()));

			Helper.saveBoolValueInPrefs(
					mParentActivity,
					SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE,
					storeBasicModel.isIsDisplayCounterShare());
			Helper.saveBoolValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_IS_PLANOGRAM,
					storeBasicModel.isIsPlanogram());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (_rootView == null) {
			_rootView = inflater.inflate(
					R.layout.outlet_working_fragment_today_stores, container,
					false);

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		setUpView();

		return _rootView;

	}

	/**
	 * Get reference of the UI elements
	 */
	private void setUpView() {
		mListViewTodayStores = (ListView) _rootView
				.findViewById(R.id.lv_todaystoreFragment);

		mTextViewTodayStoreNotAvailble = (TextView) _rootView
				.findViewById(R.id.tv_store_not_available_todaystoreFragment);

		mListViewTodayStores.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				try {
					StoreBasicModel todayStoreBasicModel = (StoreBasicModel) parent
							.getItemAtPosition(position);
					onListItemsClick(todayStoreBasicModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	private void getTodaysStoresFromServerVolley() {

		VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
				mParentActivity,
				mParentActivity.getStringFromResource(R.string.loadingmessage),
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
													mParentActivity
															.getApplicationContext());

									mContentResolver.bulkInsert(
											ProviderContract.URI_STORES_BASICS,
											contentValues);

								} else {
									mTextViewTodayStoreNotAvailble
											.setVisibility(View.VISIBLE);
								}

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
					.getStringValuefromPrefs(mParentActivity,
							SharedPreferencesKey.PREF_USERID)));

		} catch (NumberFormatException e) {
			Helper.printStackTrace(e);
		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(mParentActivity.getStringFromResource(R.string.url),
				"GetStoresForTodayBeat");
		pdn.setRequestData(jsonobj);
		pdn.callWebService();

	}

	/**
	 * called when a single store is clicked form today's store
	 * 
	 * @param storeBasicModel
	 *            basic data of the store which is clicked by the user
	 * 
	 */

	private void onListItemsClick(final StoreBasicModel storeBasicModel) {

		if (storeBasicModel != null) {

			if (Helper.getBoolValueFromPrefs(mParentActivity,
					SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
				moveToNextActivity(storeBasicModel);

			} else {
				syncDataIfAvailable(storeBasicModel);

			}

		}
	}

	private void syncDataIfAvailable(final StoreBasicModel storeBasicModel) {
		mStoreID = storeBasicModel.getStoreID();

		String pref_stroeID = Helper.getStringValuefromPrefs(mParentActivity,
				SharedPreferencesKey.PREF_STOREID);

		if (!Helper.getStringValuefromPrefs(mParentActivity,
				SharedPreferencesKey.PREF_STOREID).equalsIgnoreCase("")
				&& Long.parseLong(pref_stroeID) != mStoreID) {
			Map<String, ArrayList<String>> pandingMoudleList = DatabaseHelper
					.getConnection(mParentActivity)
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

				final SyncMaster master1 = new SyncMaster(mParentActivity);

				SyncPreparationCompeteCallback callback = new SyncPreparationCompeteCallback() {

					@Override
					public void onComplete(
							ArrayList<ActivityDataMasterModel> data) {

						if (data != null && data.size() > 0) {

							if (!Helper.isOnline(mParentActivity)) {

								Toast.makeText(mParentActivity,
										R.string.not_online, Toast.LENGTH_LONG)
										.show();

							} else {

								master1.sync(data, false, okListener);
							}

						} else {

							try {
							
									/*
									 * getTodaySingleStoreBasicPlusDetailDataFromDB(
									 * mStoreID, new
									 * GetTodaySingleStoreBaiscPlusDetailDataFromDBHandler
									 * (
									 * OutletWorkingFragmentTodayStores.this));
									 */

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
						mParentActivity);
				Set<String> keySet = pandingMoudleList.keySet();
				for (String key : keySet) {
					pandingMoudleList.get(key);
					SeparatedListItemAdapter itemAdapter = new SeparatedListItemAdapter(
							mParentActivity, 0, pandingMoudleList.get(key));
					adapter.addSection(key, itemAdapter);
				}

				SSCAlertDialog alertDialog = new SSCAlertDialog(
						mParentActivity, SSCAlertDialog.LIST_TYPE)
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
					 * getTodaySingleStoreBasicPlusDetailDataFromDB( mStoreID,
					 * new GetTodaySingleStoreBaiscPlusDetailDataFromDBHandler(
					 * OutletWorkingFragmentTodayStores.this));
					 */

					moveToNextActivity(storeBasicModel);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void moveToNextActivity(StoreBasicModel storeBasicModel) {
		saveStoreProfilePrefresnce(storeBasicModel);

		if (Helper.getBoolValueFromPrefs(mParentActivity,
				SharedPreferencesKey.PREF_IS_STORE_PROFILE_VISIBLE)) {
			Intent intent = new Intent(mParentActivity, OutletProfile2.class);
			intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
			startActivity(intent);
		} else {

			if (Helper.getBoolValueFromPrefs(mParentActivity,
					SharedPreferencesKey.PREF_IS_GEO_TAG_MANDATORY)) {
				Intent intent = new Intent(mParentActivity, Geotag.class);
				intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
				startActivity(intent);
			} else {
				DatabaseHelper.getConnection(mParentActivity)
						.updateStoreCoverge(storeBasicModel.getStoreID());

				// genarateSuravyResponseIDForStore(storeBasicModel);

				Intent intent = new Intent(getActivity(),
						ActivityDashboardChild.class);
				intent.putExtra(IntentKey.KEY_MODULE_ID, 4);
				intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
				startActivity(intent);
			}

		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		String selection = StoreBasicColulmns.KEY_BEAT_TYPE + "=?";

		String selectionArgs[] = { Constants.STORE_TYPE_TODAY };

		return new CursorLoader(mParentActivity,
				ProviderContract.URI_STORES_BASICS, null, selection,
				selectionArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {

		processCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	private void processCursor(Cursor cursor) {

		if (cursor != null && cursor.moveToFirst()) {

			List<StoreBasicModel> listBasicModel = DatabaseUtilMethods
					.getStoresBasics(cursor, Constants.STORE_TYPE_TODAY);
			mListViewTodayStores.setAdapter(new OutletWorkingAdapter1(
					mParentActivity, R.layout.beat_today, listBasicModel));

		} else {

			if (Helper.isOnline(mParentActivity)) {
				getTodaysStoresFromServerVolley();

			} else {

				Helper.showAlertDialog(
						mParentActivity,
						SSCAlertDialog.ERROR_TYPE,
						getString(R.string.error1),
						getString(R.string.error2),
						getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();

							}
						}, false, null, null);

			}

		}

	}

}
