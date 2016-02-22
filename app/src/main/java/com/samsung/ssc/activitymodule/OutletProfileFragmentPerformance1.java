package com.samsung.ssc.activitymodule;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.StorePerformaceColumns;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.dto.StorePerformanceModel;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

public class OutletProfileFragmentPerformance1 extends Fragment implements
		OnClickListener, LoaderCallbacks<Cursor> {

	private View _rootView;
	private ViewHolder holder;

	private OutletProfile mParentActivity;
	private ProgressDialog progressDialog;

	private StorePerformanceModel mStorePerformance;
	private StoreBasicModel mStoreBasic;
	private boolean mIsCancelButtonNeeded = false;
	private final int LOADER_ID = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (_rootView == null) {
			_rootView = inflater.inflate(
					R.layout.outlet_profile_fragment_performance, container,
					false);

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		holder = new ViewHolder(_rootView);

		Bundle bundle = mParentActivity.getIntent().getExtras();

		mIsCancelButtonNeeded = bundle
				.getBoolean(IntentKey.KEY_CANCEL_BUTTON_NEEDED);

		if (mIsCancelButtonNeeded) {
		
				holder.btNext.setText(R.string.cancel);
			

		}
		return _rootView;

	}

	/**
	 * Initialize views and set up onclick listener.
	 */

	private void setUpView() {

		holder.btNext.setOnClickListener(this);

		if (mStorePerformance == null) {

			if (holder.mtdButton.getVisibility() == View.VISIBLE) {
				holder.mtdButton.setVisibility(View.GONE);
			}

			if (holder.lmtdButton.getVisibility() == View.VISIBLE) {
				holder.lmtdButton.setVisibility(View.GONE);
			}

			return;
		}

		if (holder.mtdButton.getVisibility() == View.GONE) {
			holder.mtdButton.setVisibility(View.VISIBLE);
		}

		if (holder.lmtdButton.getVisibility() == View.GONE) {
			holder.lmtdButton.setVisibility(View.VISIBLE);
		}

		holder.acMtdPurchase.setText(String.valueOf(mStorePerformance
				.getACMTDPurchase()));
		holder.acMtdSell.setText(String.valueOf(mStorePerformance
				.getACMTDSale()));
		holder.avMtdPurchase.setText(String.valueOf(mStorePerformance
				.getAVMTDPurchase()));
		holder.avMtdSell.setText(String.valueOf(mStorePerformance
				.getAVMTDSale()));
		holder.haMtdPurchase.setText(String.valueOf(mStorePerformance
				.getHAMTDPurchase()));
		holder.haMtdSell.setText(String.valueOf(mStorePerformance
				.getHAMTDSale()));

		holder.acLMtdPurchase.setText(String.valueOf(mStorePerformance
				.getACLMTDPurchase()));
		holder.acLMtdSell.setText(String.valueOf(mStorePerformance
				.getACLMTDSale()));
		holder.avLMtdPurchase.setText(String.valueOf(mStorePerformance
				.getAVLMTDPurchase()));
		holder.avLMtdSell.setText(String.valueOf(mStorePerformance
				.getAVLMTDSale()));
		holder.haLMtdPurchase.setText(String.valueOf(mStorePerformance
				.getHALMTDPurchase()));
		holder.haLMtdSell.setText(String.valueOf(mStorePerformance
				.getHALMTDSale()));

		holder.outletTarget.setText(String.valueOf(mStorePerformance
				.getTarget()));

		try {
			float totalSales = mStorePerformance.getACMTDSale()
					+ mStorePerformance.getAVMTDSale()
					+ mStorePerformance.getHAMTDSale();

			float totalpurchase = mStorePerformance.getACMTDPurchase()
					+ mStorePerformance.getAVMTDPurchase()
					+ mStorePerformance.getHAMTDPurchase();

			holder.totalsales.setText(Float.toString(totalSales));
			holder.totalPurchase.setText(Float.toString(totalpurchase));
		} catch (Exception e) {
			e.printStackTrace();
		}

		holder.ach.setText(String.valueOf(mStorePerformance.getAch()));
		String date = mStorePerformance.getLastVisitedDate();
		if (!Helper.isNullOrEmpty(date)) {
			holder.lastVisitedDate.setText(date);
		}

		holder.mtdButton.setOnClickListener(this);
		holder.lmtdButton.setOnClickListener(this);

	}

	/**
	 * Holds the reference of all the UI elements used by screen
	 * 
	 * @author sabyasachi.b
	 * 
	 */
	class ViewHolder {

		TextView outletTarget, totalsales, totalPurchase, ach, lastVisitedDate;
		TextView acMtdPurchase, acMtdSell, avMtdPurchase, avMtdSell,
				haMtdPurchase, haMtdSell;
		TextView acLMtdPurchase, acLMtdSell, avLMtdPurchase, avLMtdSell,
				haLMtdPurchase, haLMtdSell;

		Button mtdButton, lmtdButton, btNext;

		LinearLayout mtdLinearLayout, lmtdLinearLayout;

		public ViewHolder(View rootView) {

			acMtdPurchase = (TextView) rootView
					.findViewById(R.id.acmdtpurchase);
			acMtdSell = (TextView) rootView.findViewById(R.id.acmdtSale);
			avMtdPurchase = (TextView) rootView
					.findViewById(R.id.avmdtpurchase);
			avMtdSell = (TextView) rootView.findViewById(R.id.avmdtSale);
			haMtdPurchase = (TextView) rootView
					.findViewById(R.id.hamdtpurchase);
			haMtdSell = (TextView) rootView.findViewById(R.id.hamdtSale);

			acLMtdPurchase = (TextView) rootView
					.findViewById(R.id.aclmdtpurchase);
			acLMtdSell = (TextView) rootView.findViewById(R.id.aclmdtSale);
			avLMtdPurchase = (TextView) rootView
					.findViewById(R.id.avlmdtpurchase);
			avLMtdSell = (TextView) rootView.findViewById(R.id.avlmdtSale);
			haLMtdPurchase = (TextView) rootView
					.findViewById(R.id.halmdtpurchase);
			haLMtdSell = (TextView) rootView.findViewById(R.id.halmdtSale);

			outletTarget = (TextView) rootView.findViewById(R.id.outletTarget);

			totalsales = (TextView) rootView.findViewById(R.id.totalSale);
			totalPurchase = (TextView) rootView
					.findViewById(R.id.totalPurchase);

			ach = (TextView) rootView.findViewById(R.id.ach);
			lastVisitedDate = (TextView) rootView
					.findViewById(R.id.lastVisitedDate);

			mtdButton = (Button) rootView
					.findViewById(R.id.activity_outlet_profile_button_mtd_records);
			lmtdButton = (Button) rootView
					.findViewById(R.id.activity_outlet_profile_button_lmtd_records);

			mtdLinearLayout = (LinearLayout) rootView
					.findViewById(R.id.activity_outlet_profile_linearlayout_mtd_records);
			lmtdLinearLayout = (LinearLayout) rootView
					.findViewById(R.id.activity_outlet_profile_linearlayout_lmtd_records);

			btNext = (Button) rootView
					.findViewById(R.id.btnNext_outletProfileFragmentPerformance);

		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.activity_outlet_profile_button_mtd_records:

			if (mStorePerformance == null) {
				return;
			}
			holder.mtdButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_green_color));
			holder.mtdButton.setTextColor(getResources()
					.getColor(R.color.white));

			holder.lmtdButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_blue_color));
			holder.lmtdButton.setTextColor(getResources().getColor(
					R.color.white));

			holder.mtdLinearLayout.setVisibility(View.VISIBLE);
			if (holder.lmtdLinearLayout.getVisibility() == View.VISIBLE) {
				holder.lmtdLinearLayout.setVisibility(View.GONE);
			}
			try {
				float totalSales = mStorePerformance.getACMTDSale()
						+ mStorePerformance.getAVMTDSale()
						+ mStorePerformance.getHAMTDSale();

				float totalpurchase = mStorePerformance.getACMTDPurchase()
						+ mStorePerformance.getAVMTDPurchase()
						+ mStorePerformance.getHAMTDPurchase();

				holder.totalsales.setText(Float.toString(totalSales));
				holder.totalPurchase.setText(Float.toString(totalpurchase));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.activity_outlet_profile_button_lmtd_records:

			if (mStorePerformance == null) {
				return;
			}

			holder.mtdButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_blue_color));
			holder.mtdButton.setTextColor(getResources()
					.getColor(R.color.white));

			holder.lmtdButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_green_color));
			holder.lmtdButton.setTextColor(getResources().getColor(
					R.color.white));

			holder.lmtdLinearLayout.setVisibility(View.VISIBLE);

			if (holder.mtdLinearLayout.getVisibility() == View.VISIBLE) {
				holder.mtdLinearLayout.setVisibility(View.GONE);
			}
			try {
				float totalSales = mStorePerformance.getACLMTDSale()
						+ mStorePerformance.getAVLMTDSale()
						+ mStorePerformance.getHALMTDSale();

				float totalpurchase = mStorePerformance.getACLMTDPurchase()
						+ mStorePerformance.getAVLMTDPurchase()
						+ mStorePerformance.getHALMTDPurchase();

				holder.totalsales.setText(Float.toString(totalSales));
				holder.totalPurchase.setText(Float.toString(totalpurchase));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.btnNext_outletProfileFragmentPerformance:

			onNextClick();

			break;

		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// get the instance of the parent activity
		mParentActivity = (OutletProfile) activity;

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

	/**
	 * This method generate survey response id
	 */

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {

			// check if performance data already retrieved in previous attempt
			if (mStorePerformance == null) {
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

	

	private void showProgressDialog() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			progressDialog = SSCProgressDialog.ctor(mParentActivity);

		} else {
			progressDialog = new ProgressDialog(mParentActivity);
			progressDialog.setProgress(0);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(mParentActivity
					.getString(R.string.loadingmessage));
			progressDialog.setCancelable(false);
		}

		if (progressDialog != null)

		{
			progressDialog.show();
		}

	}

	private void dismissDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		progressDialog = null;
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

								if (!json.isNull("SingleResult")) {
									final ContentValues contentValue = DatabaseUtilMethods.getContentValuePerformance(json
											.getJSONObject("SingleResult"));
									
									

									if (contentValue != null) {

										Thread thread = new Thread(
												new Runnable() {

													@Override
													public void run() {
														mParentActivity
																.getContentResolver()
																.insert(ProviderContract.URI_STORE_PERFORMANCE,
																		contentValue);

													}
												});

										thread.start();

									}

								} else {
									setUpView();
								}
							} else {
								setUpView();
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
				WebConfig.WebMethod.DISPLAY_STORE_PROFILE);

		pdn.setRequestData(jsonobj);
		pdn.callWebService();

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		String selection = StorePerformaceColumns.KEY_BEAT_STORE_ID + "=?";
		String[] whereArgs = { mStoreBasic.getStoreID() + "" };

		return new CursorLoader(mParentActivity,
				ProviderContract.URI_STORE_PERFORMANCE, null, selection,
				whereArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

		if (cursor != null && cursor.moveToFirst()) {
			
			mStorePerformance = DatabaseUtilMethods
					.getStorePerformanceFromCursor(cursor);

			if (mStorePerformance != null) {

				setUpView();

			}
		} else {
			if (mParentActivity.isOnline()) {

				getStorePerformanceFromServerByStoreId();

			} else {

				setUpView();

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

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}
