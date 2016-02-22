package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.OutletWorkingOtherStoreAdapter;
import com.samsung.ssc.adapters.SeparatedListAdapter;
import com.samsung.ssc.adapters.SeparatedListItemAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncMaster;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.OnOkListener;
import com.samsung.ssc.util.SyncPreparationCompeteCallback;

public class OutletWorkingFragmentOtherStores extends Fragment implements
		LoaderCallbacks<Cursor> {

	private OutletWorking1 mParentActivity;
	private View _rootView;
	private ListView mLv_other_stores;
	private EditText mEt_other_stores;
	private AutoCompleteTextView mActv_cities;
	private List<StoreBasicModel> mLsOtherStoreBasicByCity,
			mLsOtherStoreBasicPermanent;
	private List<String> mLsCities;
	private OutletWorkingOtherStoreAdapter mAdapter;
	private final int LOADER_GET_OTHER_STROES = 2;
	private final int LOADER_GET_OTHER_STORES_CITY = 3;
	private final int LOADER_GET_OTHER_STROES_BY_CITY = 4;
	private ArrayAdapter<String> mAdapterCity;
	private String mSearchedCityName = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mParentActivity = (OutletWorking1) getActivity();

		if (_rootView == null) {
			_rootView = inflater.inflate(
					R.layout.outlet_working_fragment_other_stores, container,
					false);

			setUpView();

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		getLoaderManager().initLoader(LOADER_GET_OTHER_STORES_CITY, null, this);
		
		return _rootView;

	}

	private void setUpView() {

		mLv_other_stores = (ListView) _rootView
				.findViewById(R.id.lv_otherStoreFragment);

		mLv_other_stores.setAdapter(mAdapter);

		mActv_cities = (AutoCompleteTextView) _rootView
				.findViewById(R.id.actv_cityName_otherStoreFragment);

		mEt_other_stores = (EditText) _rootView
				.findViewById(R.id.et_stroeName_otherStoreFragment);

		// text change listener for edit text search

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

		mLv_other_stores.setOnItemClickListener(new OnItemClickListener() {

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

		mActv_cities.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					InputMethodManager imm = (InputMethodManager) mParentActivity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mActv_cities.getWindowToken(),
							0);
					mSearchedCityName = (String) parent
							.getItemAtPosition(position);
					if (!mSearchedCityName.equalsIgnoreCase("")) {
						mEt_other_stores.setText("");
                       
						
						/*
						 * getOtherStoresByCity(new
						 * OtherStoresByCityDataHandler(
						 * OutletWorkingFragmentOtherStores.this),
						 * selectedCity);
						 */

						Loader loader = getLoaderManager().getLoader(
								LOADER_GET_OTHER_STROES_BY_CITY);
						if (loader == null) {
							getLoaderManager().initLoader(
									LOADER_GET_OTHER_STROES_BY_CITY, null,
									OutletWorkingFragmentOtherStores.this);
						} else {
							getLoaderManager().restartLoader(
									LOADER_GET_OTHER_STROES_BY_CITY, null,
									OutletWorkingFragmentOtherStores.this);
						}

					}

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
						if (mAdapter != null
								&& mLsOtherStoreBasicPermanent != null) {

							List<StoreBasicModel> listSotreBasics = new ArrayList<StoreBasicModel>();
							listSotreBasics.addAll(mLsOtherStoreBasicPermanent);
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

	@Override
	public void onResume() {
		super.onResume();

		LoaderManager loaderManager = mParentActivity.getSupportLoaderManager();
		Loader<Cursor> loader = loaderManager
				.getLoader(LOADER_GET_OTHER_STROES);
		// check if loader is already created earlier
		if (loader == null) {
			loaderManager.initLoader(LOADER_GET_OTHER_STROES, null, this);
		} /*else if (loader.isStarted()) {
			loaderManager.restartLoader(LOADER_GET_OTHER_STROES, null, this);
		}*/

		

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private void onListItemsClick(final StoreBasicModel storeBasicModel,
			int position) {

		if (storeBasicModel != null) {

			if (Helper.getBoolValueFromPrefs(mParentActivity,
					SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
				moveToNextActivity(storeBasicModel);

			} else {
				syncDataIfAvailable(storeBasicModel);

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

				Intent intent = new Intent(getActivity(),
						ActivityDashboardChild.class);
				intent.putExtra(IntentKey.KEY_STORE_BASIC, storeBasicModel);
				intent.putExtra(IntentKey.KEY_MODULE_ID, 4);
				startActivity(intent);

			}

		}

		mEt_other_stores.setText("");
		//
	}

	private void syncDataIfAvailable(final StoreBasicModel storeBasicModel) {
		long mStoreID = storeBasicModel.getStoreID();

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

	/**
	 * Stores the store profile detail in shared preference
	 * 
	 * @param storeDataModal
	 */
	private void saveStoreProfilePrefresnce(StoreBasicModel storeDataModal) {

		try {
			 

			Helper.saveLongValueInPref(mParentActivity,
					SharedPreferencesKey.PREF_SELECTED_OUTLET_STORE_ID,
					(int) storeDataModal.getStoreID());

		
			Helper.saveBoolValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_ROAMINGUSER, true);
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STOREID,
					String.valueOf(storeDataModal.getStoreID()));
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_COVERAGEID,
					String.valueOf(storeDataModal.getCoverageID()));

			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_ACCOUNTNAME, storeDataModal
							.getStoreName().toString());
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORECODE, storeDataModal
							.getStoreCode().toString());
			Helper.saveBoolValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_IS_FREEZE,
					storeDataModal.isIsFreeze());
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_GEO_LATTITUDE,
					String.valueOf(storeDataModal.getFreezeLattitude()));
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_GEO_LOGNITUDE,
					String.valueOf(storeDataModal.getFreezeLongitude()));
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_CHANNEL_TYPE,
					String.valueOf(storeDataModal.getChannelType()));

			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_STORE_CLASS,
					String.valueOf(storeDataModal.getStoreClass()));

			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_STORE_SIZE,
					String.valueOf(storeDataModal.getStoreSize()));

			Helper.saveBoolValueInPrefs(
					mParentActivity,
					SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE,
					storeDataModal.isIsDisplayCounterShare());
			Helper.saveBoolValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_STORE_IS_PLANOGRAM,
					storeDataModal.isIsPlanogram());

			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_COVERAGEID, "");
			Helper.saveStringValueInPrefs(mParentActivity,
					SharedPreferencesKey.PREF_BEATPLANDATE, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {

		Loader<Cursor> loader = null;

		switch (id) {
		case LOADER_GET_OTHER_STROES:

			String selection = StoreBasicColulmns.KEY_BEAT_TYPE + "=?";

			String selectionArgs[] = { Constants.STORE_TYPE_OTHER };

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_STORES_BASICS, null, selection,
					selectionArgs, null);

			break;

		case LOADER_GET_OTHER_STORES_CITY:

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_OTHER_STORES_CITIES, null, null, null, null);

			break;

		case LOADER_GET_OTHER_STROES_BY_CITY:

			String selectionForcity = StoreBasicColulmns.KEY_BEAT_TYPE
					+ "=? AND " + StoreBasicColulmns.KEY_BEAT_CITY + "=?";

			String selectionArgsForCity[] = { Constants.STORE_TYPE_OTHER,
					mSearchedCityName };

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_GET_OTHER_STORES_BASICS_BY_CITY, null,
					selectionForcity, selectionArgsForCity, null);

			break;
		}

		return loader;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (cursor != null && cursor.moveToFirst()) {

			switch (loader.getId()) {
			case LOADER_GET_OTHER_STROES:

				List<StoreBasicModel> listBasicModel = DatabaseUtilMethods
						.getStoresBasics(cursor, Constants.STORE_TYPE_TODAY);

				// this list is used to restore the original store list again.
				mLsOtherStoreBasicPermanent = new ArrayList<StoreBasicModel>();
				mLsOtherStoreBasicPermanent.addAll(listBasicModel);

				mAdapter = new OutletWorkingOtherStoreAdapter(mParentActivity,
						listBasicModel);

				mLv_other_stores.setAdapter(mAdapter);

				break;
			case LOADER_GET_OTHER_STORES_CITY:

				List<String> lvCities = DatabaseUtilMethods
						.getOtherStoreCitiesfromCursor(cursor);

				mAdapterCity = new ArrayAdapter<String>(mParentActivity,
						android.R.layout.simple_dropdown_item_1line, lvCities);

				if (mActv_cities != null) {
					mActv_cities.setAdapter(mAdapterCity);
				}

				break;

			case LOADER_GET_OTHER_STROES_BY_CITY:

				List<StoreBasicModel> listBasicModelByCity = DatabaseUtilMethods
						.getStoresBasics(cursor, Constants.STORE_TYPE_TODAY);

				if (mAdapter != null) {
					mAdapter.addSotres(listBasicModelByCity);
				}

				break;
			}

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}
