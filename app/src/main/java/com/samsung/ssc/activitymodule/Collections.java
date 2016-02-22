package com.samsung.ssc.activitymodule;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.CollectionAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.CollectionResponseTableColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.CollectionDataDto;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.PaymentModes;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.CollectionArrayList;
import com.samsung.ssc.util.Helper;

public class Collections extends BaseActivity implements OnClickListener,LoaderCallbacks<Cursor> {

	private CollectionAdapter mCollectionListAdapter;
	
	
	private static final int LOADER_ID_PAYMENT_MODES = 1;
	private static final int LOADER_ID_COLLECTION_RESPONSE = 2;
	private static final int  LOADER_ID_GET_ACTIVITY_ID = 3;

	private CollectionArrayList mCollectionData = CollectionArrayList
			.getInstance();
	private long mActivityID=-1;
	private ActivityDataMasterModel mActivityData;

	private StoreBasicModel mStoreDetails;

	public static ArrayList<PaymentModes> mPaymentModeList;

	@Override
	public void init() {
		super.init();

		setContentView(R.layout.collection1);
		getBundleValue();

		

	}

	private void getDataFromDatabase() {

		mCollectionData.clear(); // if mActivity id is eques with -1 then we
									// should not call Database
		if (mActivityID != -1) {
			
			getSupportLoaderManager().initLoader(LOADER_ID_COLLECTION_RESPONSE, null, this);
			
			
		}

	}

	/**
	 * Init view
	 */
	private void setUpView() {

		
		getSupportLoaderManager().initLoader(LOADER_ID_PAYMENT_MODES, null, this);

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
			Button btn = (Button) findViewById(R.id.approveBtn);
			btn.setText(getString(R.string.proceed));
		}

		ListView mListViewCollection = (ListView) findViewById(R.id.listView);
		setUpHeaderFooterView(mListViewCollection);

		mCollectionListAdapter = new CollectionAdapter(this, mCollectionData);
		mListViewCollection.setAdapter(mCollectionListAdapter);

	}

	/**
	 * @param mListViewCollection
	 * @param inflater
	 */
	private void setUpHeaderFooterView(ListView mListViewCollection) {
		LayoutInflater inflater = getLayoutInflater();

		LinearLayout mListHeaderView = (LinearLayout) inflater.inflate(
				R.layout.collection_header_items, null);

		EditText etDealerCode = (EditText) mListHeaderView
				.findViewById(R.id.dealerCode);
		EditText etDealerName = (EditText) mListHeaderView
				.findViewById(R.id.dealerName);

		etDealerCode.setHint(Helper.getStringValuefromPrefs(Collections.this,
				SharedPreferencesKey.PREF_STORECODE));
		etDealerName.setHint(Helper.getStringValuefromPrefs(Collections.this,
				SharedPreferencesKey.PREF_ACCOUNTNAME));
		etDealerName.setHint(mStoreDetails.getStoreName());

		mListViewCollection.addHeaderView(mListHeaderView);

		Button btnAddItems = (Button) inflater.inflate(
				R.layout.collection_footer_items, null);

		mListViewCollection.addFooterView(btnAddItems);

		btnAddItems.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(Collections.this,
						AddCollectionDialog.class);
				startActivityForResult(i, 1001);
			}
		});
	}

	private void getBundleValue() {
		Intent intent = getIntent();

		Module module = (Module) intent
				.getParcelableExtra(IntentKey.MOUDLE_POJO);

		if (module != null) {
			mActivityData = Helper.getActivityDataMasterModel(
					getApplicationContext(), module);
			if (mActivityData != null) {
				
				/*mActivityID = DatabaseHelper.getConnection(
						getApplicationContext()).getActivityIdIfExist(
						mActivityData);
				*/
				
				getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVITY_ID, null,
						this);

			}
			setCurrentContext(Collections.this);

			mStoreDetails = intent.getParcelableExtra(IntentKey.KEY_STORE_BASIC);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == 1001) {

			if (data != null) {

				CollectionDataDto collObj = new CollectionDataDto();
				Bundle bundel = data.getExtras();

				if (bundel.getString("editmode") != null
						&& bundel.getString("editmode").equals("editmode")) {// check
																				// edit
																				// mode
					collObj.setMode(bundel.getString("paymentMode"));
					collObj.setModeId(bundel.getString("paymentModeId"));
					collObj.setAmount(bundel.getString("amount"));
					collObj.setTransId(bundel.getString("transId")
							.toUpperCase());
					collObj.setDisplayDate((bundel.getString("transDate")));
					collObj.setDescription(bundel.getString("description"));
					mCollectionData.set(bundel.getInt("editposition"), collObj);

					mCollectionListAdapter.notifyDataSetChanged();

				} else {
					collObj.setMode(bundel.getString("paymentMode"));
					collObj.setModeId(bundel.getString("paymentModeId"));
					collObj.setAmount(bundel.getString("amount"));
					collObj.setTransId(bundel.getString("transId")
							.toUpperCase());
					collObj.setDisplayDate((bundel.getString("transDate")));
					collObj.setDescription(bundel.getString("description"));
					mCollectionData.add(collObj);

					mCollectionListAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	public void refreshList() {
		mCollectionListAdapter.notifyDataSetChanged();

		if (releaseMode) {

		}

	}

	private void saveCollectionData(int syncStatus) {

		try {

			ArrayList<JSONObject> elements = new ArrayList<JSONObject>();

			int count = mCollectionData.size();

			for (int i = 0; i < count; i++) {

				JSONObject jsonobj = new JSONObject();

				jsonobj.put(WebConfig.WebParams.USER_ID_CAPS, Long.valueOf(Helper
						.getStringValuefromPrefs(Collections.this,
								SharedPreferencesKey.PREF_USERID)));
				if (!Helper.isNullOrEmpty(Helper.getStringValuefromPrefs(
						Collections.this, SharedPreferencesKey.PREF_COVERAGEID))) {
					jsonobj.put(WebConfig.WebParams.COVERAGEPLANID, Long.valueOf(Helper
							.getStringValuefromPrefs(Collections.this,
									SharedPreferencesKey.PREF_COVERAGEID)));
				}
				jsonobj.put("StoreID", Long.valueOf(Helper
						.getStringValuefromPrefs(Collections.this,
								SharedPreferencesKey.PREF_STOREID)));

				jsonobj.put(WebConfig.WebParams.USER_ROLE_ID_CAPS, Integer.valueOf(Helper
						.getStringValuefromPrefs(Collections.this,
								SharedPreferencesKey.PREF_ROLEIDSTOREWISE)));

				CollectionDataDto modal = (CollectionDataDto) mCollectionData
						.get(i);

				jsonobj.put("PaymentModeID", Integer.valueOf(modal.getModeId()));
				jsonobj.put("Amount", Long.valueOf(modal.getAmount()));
				jsonobj.put("Comments", modal.getDescription());
				jsonobj.put("PaymentDate", (modal.getPaymentDate()));
				jsonobj.put("TransactionID", modal.getTransId());
				jsonobj.put("PaymentMode", modal.getMode());
				elements.add(jsonobj);
			}

			JSONArray jsonArrayResponse = new JSONArray(elements);

			if (jsonArrayResponse.length() > 0) {
				if (mActivityID == -1) {
					// ganrate activity id
					mActivityData.setSyncStatus(syncStatus);
					
					/*mActivityID = DatabaseHelper.getConnection(
							getApplicationContext()).insertActivtyDataMaster(
							mActivityData);*/
					

					ContentValues contentValues = DatabaseUtilMethods.getActivityDataContetnValueArray(mActivityData);
					Uri uri = getContentResolver().insert(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues);
					mActivityID = ContentUris.parseId(uri);

					
					
				} else {
					// update acitivty id with new sync status
					/*DatabaseHelper.getConnection(getApplicationContext())
							.updateActivtyDataMaster(mActivityID, syncStatus);*/

					String where = ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID+ "=?";
					String[] selectionArgs = new String[] { String.valueOf(mActivityID) };
					
					
					ContentValues contentValues = DatabaseUtilMethods.getActivityDataContentValueUpdateStatus(syncStatus);
					getContentResolver().update(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues, where, selectionArgs);
				
				
				}
				// insert into transaction table
				try {
					
					
					getContentResolver().delete(ProviderContract.URI_COLLECTION_RESPONSE, CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID+ "= ?", new String[] { String.valueOf(mActivityID) });
					
					ContentValues[] values = DatabaseUtilMethods.getContentValueCollectionResponse(this,mActivityID,jsonArrayResponse);
					getContentResolver().bulkInsert(ProviderContract.URI_COLLECTION_RESPONSE, values);
					
				/*	DatabaseHelper.getConnection(getApplicationContext())
							.insertCollectionResponse(mActivityID,
									jsonArrayResponse);*/
				} catch (Exception e) {
					e.printStackTrace();
				}

				Helper.showCustomToast(getApplicationContext(),
						R.string.data_saved_successfully, Toast.LENGTH_LONG);

				finish();
			} else {
				if (mActivityID != -1) {
					// delete activity id
					DatabaseHelper
							.getConnection(this)
							.deleteDataFromCollectionResponseAndActivityDataMasterTable(
									mActivityID);
					Toast.makeText(getApplicationContext(),
							"Data Saved Successfully", Toast.LENGTH_SHORT)
							.show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"No Data to Proceed", Toast.LENGTH_LONG).show();
				}
			}

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
	}

	@Override
	public void onBackPressed() {

		backcall();

	}

	private void backcall() {

		if (mCollectionData.size() > 0) {

			Helper.showAlertDialog(
					Collections.this,
					SSCAlertDialog.NORMAL_TYPE,
					getString(R.string.error10),
					getString(R.string.save_msg),
					getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();

							finish();

						}
					}, true, getString(R.string.cancel),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();
						}
					});

		} else {
			finish();
		}

	}

	public void onCanceButtonlClick1(View view) {
		backcall();
	}

	public void onProceedButtonClick(View view) {

	

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {

			DialogInterface.OnClickListener buttonSubmitListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					saveCollectionData(SyncUtils.SYNC_STATUS_SUBMIT);

				}
			};

			DialogInterface.OnClickListener buttonSaveListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					saveCollectionData(SyncUtils.SYNC_STATUS_SAVED);
				}
			};

			Helper.showThreeButtonConfirmationDialog(this,
					R.string.sync_confirmation_dialog_title,
					R.string.sync_confirmation_dialog_message,
					R.string.sync_confirmation_dialog_button_one,
					R.string.sync_confirmation_dialog_button_two,
					R.string.sync_confirmation_dialog_button_negative,
					buttonSubmitListerner, buttonSaveListerner, null);

		}
		else
		{
			saveCollectionData(SyncUtils.SYNC_STATUS_SUBMIT);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		Loader<Cursor> loader = null;
		switch (loaderID) {
		case LOADER_ID_PAYMENT_MODES:
			
			loader = new CursorLoader(this, ProviderContract.URI_PAYMENT_MODES, null, null, null, null);
			
			break;
			
		case LOADER_ID_COLLECTION_RESPONSE:
			
			loader = new CursorLoader(this, ProviderContract.URI_COLLECTION_RESPONSE, null, null, new String[]{String.valueOf(mActivityID)}, null);

			break;
			
			
		case LOADER_ID_GET_ACTIVITY_ID:
			
			String[] arg = { String.valueOf(mActivityData.getStoreID()),
					String.valueOf(mActivityData.getModuleCode()) };
			
			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null, arg,
					null);

			break;
		default:
			break;
		}
		
		 	
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		
		switch (loader.getId()) {
		case LOADER_ID_PAYMENT_MODES:
			
			if (cursor!=null && cursor.moveToFirst()) {
				
				/*mPaymentModeList = DatabaseHelper.getConnection(this)
						.getPaymentModeList();*/
				
			 	mPaymentModeList =  DatabaseUtilMethods.getPaymentModesFromCursor(cursor); 
				 
			}
			
			break;
			
		case LOADER_ID_COLLECTION_RESPONSE:
			if (cursor!=null && cursor.moveToFirst()) {
				
				
				
				JSONArray mData = 	DatabaseUtilMethods.getCollectionResponseFromCursor(this,cursor);
				
				/*JSONArray mData = DatabaseHelper.getConnection(
						getApplicationContext()).getCollectionResponse(mActivityID);*/
				if (mData.length() > 0) {
					mCollectionData.addAll(mData);
				}
				 
			}
			
			break;
		case LOADER_ID_GET_ACTIVITY_ID:
			if (cursor != null && cursor.getCount() > 0) {
				
				 mActivityID = DatabaseUtilMethods.getActivityID(cursor);
				
			}
			 getDataFromDatabase();
			 setUpView();
            break;
	 
		default:
			break;
		}
		
		 
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}
}
