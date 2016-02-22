package com.samsung.ssc.activitymodule;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.ProductMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.StorePerformaceColumns;
import com.samsung.ssc.dto.SKUProductList;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.dto.StorePerformanceModel;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OutletProfileFragmentPerformanceNew extends Fragment implements
		OnClickListener, LoaderCallbacks<Cursor> {

	private View _rootView;
	private ViewHolder holder;

	private OutletProfile2 mParentActivity;
	private ProgressDialog progressDialog;

	private StorePerformanceModel mStorePerformance;
	private StoreBasicModel mStoreBasic;
	private boolean mIsCancelButtonNeeded = false;
	private final int LOADER_ID = 1;
	private final int LOADER_SECTION_1 = 2;
	private final int LOADER_SECTION_2 = 3;
	private final int LOADER_SECTION_3 = 4;
	private final int LOADER_SECTION_4 = 5;
	private LinearLayout mLLContainerSection1, mLLContainerSection2,
			mLLContainerSection3, mLLContainerSection4;
	private LayoutInflater mInflater;
	private Button btNext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (_rootView == null) {
			_rootView = inflater.inflate(
					R.layout.outlet_profile_fragment_performance_new,
					container, false);

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		holder = new ViewHolder(_rootView);

		Bundle bundle = mParentActivity.getIntent().getExtras();

		mIsCancelButtonNeeded = bundle
				.getBoolean(IntentKey.KEY_CANCEL_BUTTON_NEEDED);

		mInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		mLLContainerSection1 = (LinearLayout) _rootView
				.findViewById(R.id.ll_container_section1);
		mLLContainerSection2 = (LinearLayout) _rootView
				.findViewById(R.id.ll_container_section2);

		mLLContainerSection3 = (LinearLayout) _rootView
				.findViewById(R.id.ll_container_section3);
		mLLContainerSection4 = (LinearLayout) _rootView
				.findViewById(R.id.ll_container_section4);

		btNext = (Button) _rootView
				.findViewById(R.id.btnNext_outletProfileFragmentPerformance);

		btNext.setOnClickListener(this);

		mIsCancelButtonNeeded = bundle
				.getBoolean(IntentKey.KEY_CANCEL_BUTTON_NEEDED);

		if (mIsCancelButtonNeeded) {

			btNext.setText(R.string.cancel);

		}
		return _rootView;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// get the instance of the parent activity
		mParentActivity = (OutletProfile2) activity;

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {

			// check if performance data already retrieved in previous attempt
			if (mLLContainerSection1 != null
					&& mLLContainerSection1.getChildCount() == 0) {
				Bundle bundle = mParentActivity.getIntent().getExtras();
				mStoreBasic = bundle.getParcelable(IntentKey.KEY_STORE_BASIC);

				if (mStoreBasic != null) {

					LoaderManager loaderManager = mParentActivity
							.getSupportLoaderManager();

					loaderManager.initLoader(LOADER_ID, null, this);

					/*
					 * getSingleStoreDetailData(storeID, new
					 * StorePerformanceDataHandlerDBPlusServer(this));
					 */

				}

			}

		}

	}

	private void getStorePerformanceFromServerByStoreId() {

		VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
				mParentActivity,
				mParentActivity.getStringFromResource(R.string.loadingmessage),
				new VolleyGetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						try {
							JSONObject json = new JSONObject(result.toString());
							if (json.getBoolean("IsSuccess")) {

								if (!json.isNull("Result")) {
									final ContentValues[] contentValue = DatabaseUtilMethods.getContentValuePerformanceArray(
											json.getJSONArray("Result"),
											mStoreBasic.getStoreID());

									if (contentValue != null
											&& contentValue.length > 0) {

										Thread thread = new Thread(
												new Runnable() {

													@Override
													public void run() {
														mParentActivity
																.getContentResolver()
																.bulkInsert(
																		ProviderContract.URI_STORE_PERFORMANCE,
																		contentValue);

													}
												});

										thread.start();

									}

								} else {
									// setUpView();
								}
							} else {
								// setUpView();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(mParentActivity,
							SharedPreferencesKey.PREF_USERID)));
			jsonobj.put(WebConfig.WebParams.STOREID, mStoreBasic.getStoreID());

		} catch (NumberFormatException e) {
			Helper.printStackTrace(e);
		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}

		pdn.setConfig(mParentActivity.getStringFromResource(R.string.url),
				WebConfig.WebMethod.GET_STORE_PERFORMANCE);

		pdn.setRequestData(jsonobj);
		pdn.callWebService();

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		Loader loader = null;
		switch (arg0) {

		case LOADER_ID:
			String selection = StorePerformaceColumns.KEY_BEAT_STORE_ID + "=?";
			String[] whereArgs = { mStoreBasic.getStoreID() + "" };

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_STORE_PERFORMANCE, null, selection,
					whereArgs, null);

			break;

		case LOADER_SECTION_1:

			String[] whereArg = { String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()) };

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_STORE_PERFORMANCE_SECTION_1, null,
					null, whereArg, null);

			break;

		case LOADER_SECTION_2:

			String[] whereArgSec2 = { String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()) };

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_STORE_PERFORMANCE_SECTION_2, null,
					null, whereArgSec2, null);

			break;

		case LOADER_SECTION_3:

			String[] whereArgSec3 = { String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()) };

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_STORE_PERFORMANCE_SECTION_3, null,
					null, whereArgSec3, null);

			break;

		case LOADER_SECTION_4:

			String[] whereArgSec4 = { String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()),
					String.valueOf(mStoreBasic.getStoreID()) };

			loader = new CursorLoader(mParentActivity,
					ProviderContract.URI_STORE_PERFORMANCE_SECTION_4, null,
					null, whereArgSec4, null);

			break;

		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

		switch (arg0.getId()) {
		case LOADER_ID:

			if (DatabaseUtilMethods.isStorePerformanceExist(cursor)) {

				callPerformancSectionLoaders();
			} else {
				if (mParentActivity.isOnline()) {

					getStorePerformanceFromServerByStoreId();

				} else {

					Helper.showAlertDialog(
							mParentActivity,
							SSCAlertDialog.ERROR_TYPE,
							mParentActivity.getString(R.string.error1),
							mParentActivity.getString(R.string.error2),
							mParentActivity.getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {

									sdAlertDialog.dismiss();

								}
							}, false, null, null);
				}

			}

			break;

		case LOADER_SECTION_1:
			parseDataSection1(cursor);
			break;

		case LOADER_SECTION_2:
			parseDataSection2(cursor);
			break;

		case LOADER_SECTION_3:
			parseDataSection3(cursor);
			break;

		case LOADER_SECTION_4:
			parseDataSection4(cursor);
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	class ViewHolder {

		public ViewHolder(View rootView) {

		}

	}

	private void parseDataSection1(Cursor cursor) {
		if (mLLContainerSection1 != null && cursor != null) {

			if (cursor.moveToFirst()) {
				do {
					try {

						View singleRow = mInflater
								.inflate(
										R.layout.outlet_profile_peformance_row_section2,
										null);

						String st_product_type = cursor
								.getString(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_PRODUCT_TYPE));

						String st_transaction_type = cursor
								.getString(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_TRANSACTION_TYPE));

						int mtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_UNIT));

						double mtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_VALUE));

						int lmtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_UNIT));

						double lmtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_VALUE));

						TextView tv_transcation_type = (TextView) singleRow
								.findViewById(R.id.tv_transactionType_performanceRowSec2);

						TextView tv_mtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_mtdunit_performanceRowSec2);

						TextView tv_mtd_value = (TextView) singleRow
								.findViewById(R.id.tv_mtdvalue_performanceRowSec2);

						TextView tv_lmtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_lmtdunit_performanceRowSec2);

						TextView tv_lmtd_value = (TextView) singleRow
								.findViewById(R.id.tv_lmtdvalue_performanceRowSec2);

						tv_transcation_type.setText(st_product_type + " "
								+ st_transaction_type);

						tv_mtd_unit.setText(String.valueOf(mtd_unit));

						tv_mtd_value.setText(String.valueOf(Helper.round(
								mtd_value, 2)));

						tv_lmtd_unit.setText(String.valueOf(lmtd_unit));

						tv_lmtd_value.setText(String.valueOf(Helper.round(
								lmtd_value, 2)));

						mLLContainerSection1.addView(singleRow);

					} catch (Exception e) {
						Helper.printLog("getSKUProductList",
								"" + e.getMessage());
						Helper.printStackTrace(e);
						;
					}

				} while (cursor.moveToNext());

			}
		}
	}

	private void parseDataSection2(Cursor cursor) {
		if (mLLContainerSection2 != null && cursor != null) {

			if (cursor.moveToFirst()) {
				do {
					try {

						View singleRow = mInflater
								.inflate(
										R.layout.outlet_profile_peformance_row_section2,
										null);

						String st_transaction_type = cursor
								.getString(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_TRANSACTION_TYPE));

						int mtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_UNIT));

						double mtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_VALUE));

						int lmtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_UNIT));

						double lmtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_VALUE));

						TextView tv_transcation_type = (TextView) singleRow
								.findViewById(R.id.tv_transactionType_performanceRowSec2);

						TextView tv_mtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_mtdunit_performanceRowSec2);

						TextView tv_mtd_value = (TextView) singleRow
								.findViewById(R.id.tv_mtdvalue_performanceRowSec2);

						TextView tv_lmtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_lmtdunit_performanceRowSec2);

						TextView tv_lmtd_value = (TextView) singleRow
								.findViewById(R.id.tv_lmtdvalue_performanceRowSec2);

						tv_transcation_type.setText(st_transaction_type);

						tv_mtd_unit.setText(String.valueOf(mtd_unit));

						tv_mtd_value.setText(String.valueOf(Helper.round(
								mtd_value, 2)));

						tv_lmtd_unit.setText(String.valueOf(lmtd_unit));

						tv_lmtd_value.setText(String.valueOf(Helper.round(
								lmtd_value, 2)));

						mLLContainerSection2.addView(singleRow);

					} catch (Exception e) {
						Helper.printLog("getSKUProductList",
								"" + e.getMessage());
						Helper.printStackTrace(e);
						;
					}

				} while (cursor.moveToNext());

			}
		}
	}

	private void parseDataSection3(Cursor cursor) {
		if (mLLContainerSection3 != null && cursor != null) {

			if (cursor.moveToFirst()) {
				do {
					try {

						View singleRow = mInflater
								.inflate(
										R.layout.outlet_profile_peformance_row_section2,
										null);

						String st_product_group = cursor
								.getString(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_PRODUCT_GROUP));

						int mtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_UNIT));

						double mtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_VALUE));

						int lmtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_UNIT));

						double lmtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_VALUE));

						TextView tv_transcation_type = (TextView) singleRow
								.findViewById(R.id.tv_transactionType_performanceRowSec2);

						TextView tv_mtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_mtdunit_performanceRowSec2);

						TextView tv_mtd_value = (TextView) singleRow
								.findViewById(R.id.tv_mtdvalue_performanceRowSec2);

						TextView tv_lmtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_lmtdunit_performanceRowSec2);

						TextView tv_lmtd_value = (TextView) singleRow
								.findViewById(R.id.tv_lmtdvalue_performanceRowSec2);

						tv_transcation_type.setText(st_product_group.trim());

						tv_mtd_unit.setText(String.valueOf(mtd_unit));

						tv_mtd_value.setText(String.valueOf(Helper.round(
								mtd_value, 2)));

						tv_lmtd_unit.setText(String.valueOf(lmtd_unit));

						tv_lmtd_value.setText(String.valueOf(Helper.round(
								lmtd_value, 2)));

						mLLContainerSection3.addView(singleRow);

					} catch (Exception e) {
						Helper.printLog("getSKUProductList",
								"" + e.getMessage());
						Helper.printStackTrace(e);
						;
					}

				} while (cursor.moveToNext());

			}
		}
	}

	private void parseDataSection4(Cursor cursor) {
		if (mLLContainerSection4 != null && cursor != null) {

			if (cursor.moveToFirst()) {
				do {
					try {

						View singleRow = mInflater
								.inflate(
										R.layout.outlet_profile_peformance_row_section2,
										null);

						String st_product_group = cursor
								.getString(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_PRODUCT_GROUP));

						int mtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_UNIT));

						double mtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_MTD_VALUE));

						int lmtd_unit = cursor
								.getInt(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_UNIT));

						double lmtd_value = cursor
								.getDouble(cursor
										.getColumnIndex(StorePerformaceColumns.KEY_LMTD_VALUE));

						TextView tv_transcation_type = (TextView) singleRow
								.findViewById(R.id.tv_transactionType_performanceRowSec2);

						TextView tv_mtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_mtdunit_performanceRowSec2);

						TextView tv_mtd_value = (TextView) singleRow
								.findViewById(R.id.tv_mtdvalue_performanceRowSec2);

						TextView tv_lmtd_unit = (TextView) singleRow
								.findViewById(R.id.tv_lmtdunit_performanceRowSec2);

						TextView tv_lmtd_value = (TextView) singleRow
								.findViewById(R.id.tv_lmtdvalue_performanceRowSec2);

						tv_transcation_type.setText(st_product_group.trim());

						tv_mtd_unit.setText(String.valueOf(mtd_unit));

						tv_mtd_value.setText(String.valueOf(Helper.round(
								mtd_value, 2)));

						tv_lmtd_unit.setText(String.valueOf(lmtd_unit));

						tv_lmtd_value.setText(String.valueOf(Helper.round(
								lmtd_value, 2)));

						mLLContainerSection4.addView(singleRow);

					} catch (Exception e) {
						Helper.printLog("getSKUProductList",
								"" + e.getMessage());
						Helper.printStackTrace(e);
						;
					}

				} while (cursor.moveToNext());

			}
		}
	}

	private void callPerformancSectionLoaders() {
		LoaderManager loaderManager = mParentActivity.getSupportLoaderManager();

		Loader loader = loaderManager.getLoader(LOADER_SECTION_1);

		if (loader != null) {
			loaderManager.restartLoader(LOADER_SECTION_1, null, this);
		} else {
			loaderManager.initLoader(LOADER_SECTION_1, null, this);
		}

		loaderManager.initLoader(LOADER_SECTION_2, null, this);
		loaderManager.initLoader(LOADER_SECTION_3, null, this);
		loaderManager.initLoader(LOADER_SECTION_4, null, this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btnNext_outletProfileFragmentPerformance:

			onNextClick();

			break;

		}

	}

	/**
	 * Called when next button on the screen get called
	 */
	private void onNextClick() {

		if (mIsCancelButtonNeeded && mParentActivity != null) {

			mParentActivity.finish();
			return;

		}

		if (mStoreBasic == null) {
			return;
		}

		if (Helper.getBoolValueFromPrefs(getActivity(),
				SharedPreferencesKey.PREF_IS_GEO_TAG_MANDATORY)) {

			Intent intent = new Intent(getActivity(), Geotag.class);
			intent.putExtra(IntentKey.KEY_STORE_BASIC, mStoreBasic);
			startActivity(intent);

			getActivity().finish();
		} else {

			DatabaseHelper.getConnection(getActivity()).updateStoreCoverge(
					mStoreBasic.getStoreID());

			Intent intent = new Intent(getActivity(),
					ActivityDashboardChild.class);

			intent.putExtra(IntentKey.KEY_STORE_BASIC, mStoreBasic);
			intent.putExtra(IntentKey.KEY_MODULE_ID, 4);
			startActivity(intent);
			getActivity().finish();

		}

	}

}
