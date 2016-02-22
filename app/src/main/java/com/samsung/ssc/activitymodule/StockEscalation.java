package com.samsung.ssc.activitymodule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentResolver;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.OrderBookingAdapter;
import com.samsung.ssc.adapters.StockEscalationAddAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.ModuleCode;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.OrderResponseMasterTableColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.P1CategoryList;
import com.samsung.ssc.dto.P2CategoryList;
import com.samsung.ssc.dto.SKUProductList;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MySpinner;
import com.samsung.ssc.util.StockEscalationArrayList;

public class StockEscalation extends BaseActivity implements LoaderCallbacks<Cursor>  {

	private ListView mOrderBooking, mOrderBookingAdd;
	private AlertDialog helpDialog;
	private OrderBookingAdapter adpter;
	private StockEscalationAddAdapter adapterAdd;
	private ArrayList<P1CategoryList> prodCategoryP1;
	private ArrayList<P2CategoryList> prodCategoryP2;
	private ArrayList<SKUProductList> prodCategoryP3;
	
	private MySpinner mySpinnerP1;
	private MySpinner mySpinnerp2;
	private boolean checkPosition = false;
	private TextView totalQty, totalAnount;
	private DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private long mActivityID = -1;
	private ActivityDataMasterModel mActivityData;
	private int mModuleCode;

	private static final int ORDER_TYPE = 1;
	private final int LOADER_PRODUCT_CATAGORY_P2_ID = 2;
	private final int LOADER_PRODUCT_CATAGORY_P1_ID = 1;
	private final int LOADER_ORDER_RESPONSE_ID = 3;
	private final int LOADER_FIND_SKU_ID = 4;
	
	private final int  LOADER_ID_GET_ACTIVITY_ID = 5;

	private StockEscalationArrayList stockEscalationData = StockEscalationArrayList.getInstance();
	private ContentResolver mContentResolver;
	private boolean isProductPriceNeeded = true;

	/**
	 * This handler handles message passed after data inserted in database
	 * though background thread
	 * 
	 * @author sabyasachi.b
	 * 
	 */
	/*private static class InsertDataHandler extends Handler {

		WeakReference<StockEscalation> stockEscalation;

		public InsertDataHandler(StockEscalation stockEscalation) {

			this.stockEscalation = new WeakReference<StockEscalation>(
					stockEscalation);
		}

		@Override
		public void handleMessage(Message msg) {

			StockEscalation stockEscalationInstance = stockEscalation.get();

			boolean isSuccess = msg.getData().getBoolean("is_success");

			if (isSuccess) {

				int syncStatus = msg.getData().getInt("sync_status");

				stockEscalationInstance.finish();

				if (syncStatus == SyncUtils.SYNC_STATUS_SAVED) {
					
					Helper.showCustomToast(stockEscalationInstance, R.string.sync_message_data_save,  Toast.LENGTH_LONG);
					
					

				} else if (syncStatus == SyncUtils.SYNC_STATUS_SUBMIT) {
					
					Helper.showCustomToast(stockEscalationInstance, R.string.sync_message_data_submit,  Toast.LENGTH_LONG);
					

				}

			}

			else {
				
				Helper.showCustomToast(stockEscalationInstance, R.string.error_occoured,  Toast.LENGTH_LONG);
				
			}

		};
	}*/

	/**
	 * This handler handles message passed after data retrieved from database
	 * though background thread
	 * 
	 * @author sabyasachi.b
	 * 
	 */
/*	private static class RetriveHandler extends Handler {

		WeakReference<StockEscalation> stockEscalation;

		public RetriveHandler(StockEscalation stockEscalation) {

			this.stockEscalation = new WeakReference<StockEscalation>(stockEscalation);
		}

		@Override
		public void handleMessage(Message msg) {

			StockEscalation stockEscalationInstance = stockEscalation.get();
			
			if (stockEscalationInstance != null
					&& stockEscalationInstance.stockEscalationData.size() > 0) {

				stockEscalationInstance.addHeaderFooter();

				stockEscalationInstance.adapterAdd = new StockEscalationAddAdapter(
						stockEscalationInstance,
						stockEscalationInstance.mActivityID);

				stockEscalationInstance.mOrderBookingAdd
						.setAdapter(stockEscalationInstance.adapterAdd);

				stockEscalationInstance.refreshTotal();
				
			}
		};
	}*/

	@Override
	public void init() {
		super.init();

		setContentView(R.layout.bookorder1);
		getBundleValue();
		mContentResolver = getApplicationContext().getContentResolver();

		if (mModuleCode != 0) {
			if (mModuleCode == ModuleCode.MENU_STOCK_ESCALATION) {
				// setCentretext("Stock Escalation");
				((TextView) this.findViewById(R.id.tv_title_sdActionBar))
						.setText("Stock Escalation");
			}
		}

		// dbq = new DatabaseQuery(BookOrder.this);
		// prodCategoryP1 = dbq.getP1ProductList();

		/*
		 * prodCategoryP1 = DatabaseHelper.getConnection(StockEscalation.this)
		 * .getProductTypeList();
		 */

		getSupportLoaderManager().initLoader(LOADER_PRODUCT_CATAGORY_P1_ID, null, StockEscalation.this);

		// offSite = getIntent().getBooleanExtra("checkoffsite", false);

		mySpinnerP1 = (MySpinner) findViewById(R.id.spinnerone);
		mySpinnerp2 = (MySpinner) findViewById(R.id.spinnertwo);

		// addItemsOnSpinnerPOne(prodCategoryP1);

		mOrderBookingAdd = (ListView) findViewById(R.id.lvorderbookingadd);

		mySpinnerP1.setOnItemSelectedListener(new OnItemSelectedListener() {

			private Loader<Cursor> loaderProductCat2;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 > 0) {
					mySpinnerp2.setEnabled(true);

					Bundle bundle = new Bundle();
					bundle.putString("KEY_PRODUCT1",
							prodCategoryP1.get(arg2 - 1).getProductTypeCode());

					if (loaderProductCat2 == null) {
						loaderProductCat2 = getSupportLoaderManager().initLoader(
								LOADER_PRODUCT_CATAGORY_P2_ID, bundle, StockEscalation.this);
					} else {
						getSupportLoaderManager().restartLoader(LOADER_PRODUCT_CATAGORY_P2_ID,
								bundle, StockEscalation.this);
					}

					/*
					 * prodCategoryP2 = DatabaseHelper.getConnection(
					 * arg1.getContext()).getProductListGroup(
					 * prodCategoryP1.get(arg2 - 1).getProductTypeCode());
					 * 
					 * addItemsOnSpinnerPTwo(prodCategoryP2);
					 */
					checkPosition = true;
				}

				if (checkPosition && arg2 == 0) {
					mySpinnerp2.setEnabled(true);
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(getStringFromResource(R.string.productcategoryP2));
					addtempDataTwoSpinner(temp);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		mySpinnerp2.setOnItemSelectedListener(new OnItemSelectedListener() {

			private Loader<Cursor> loaderSkuCode;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 > 0) {

					/*
					 * FindSKU skuList = new FindSKU(arg2); skuList.execute();
					 */

					Bundle bundle = new Bundle();
					String productCatP2 = prodCategoryP2.get(arg2 - 1)
							.getProductGroupCode();
					bundle.putString("KEY_PRODUCT_CATEGORY2", productCatP2);

					if (loaderSkuCode == null) {
						loaderSkuCode = getSupportLoaderManager().initLoader(
								LOADER_FIND_SKU_ID, bundle, StockEscalation.this);
					} else {
						getSupportLoaderManager().restartLoader(LOADER_FIND_SKU_ID,
								bundle, StockEscalation.this);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	//	getDataFromDataBase();

	}

	
	/**
	 * This method get data from database through background thread
	 * 
	 * @param handler
	 *            It is the handler which will receive the message send by
	 *            background thread
	 */

	private void getDataFromDataBase() {

		stockEscalationData.removeAll();

		if (mActivityID != -1) {

			Bundle bundle = new Bundle();
			bundle.putLong("KEY_Activity_ID", mActivityID);
			bundle.putInt("KEY_ORDER_TYPE", ORDER_TYPE);

			getSupportLoaderManager().initLoader(LOADER_ORDER_RESPONSE_ID, bundle, this);

			/*
			 * Thread thread = new Thread(new Runnable() {
			 * 
			 * @Override public void run() {
			 * 
			 * JSONArray jArray = DatabaseHelper.getConnection(
			 * getApplicationContext()).getOrderRespose( mActivityID,
			 * ORDER_TYPE, true);
			 * 
			 * for (int i = 0; i < jArray.length(); i++) {
			 * 
			 * try { SKUProductList obj = new SKUProductList();
			 * 
			 * JSONObject jsonObject = jArray.getJSONObject(i);
			 * 
			 * obj.setSKUCode(jsonObject
			 * .getString(WebConfig.WebParams.SKUCODE));
			 * obj.setProductID(jsonObject
			 * .getInt(WebConfig.WebParams.PRODUCTID) + "");
			 * obj.setQuantity(jsonObject .getInt(WebConfig.WebParams.QUANTITY)
			 * + "");
			 * 
			 * obj.setDealerPrice(decimalFormat.format(jsonObject
			 * .getDouble(WebConfig.WebParams.PRICE)));
			 * 
			 * obj.setProductTypeID(jsonObject
			 * .getInt(WebConfig.WebParams.PRODUCTTYPEID) + "");
			 * obj.setProductCategoryID(jsonObject
			 * .getInt(WebConfig.WebParams.PRODUCTCATEGORYID) + "");
			 * obj.setProductGroupID(jsonObject
			 * .getInt(WebConfig.WebParams.PRODUCTGROUPID) + "");
			 * 
			 * stockEscalationData.add(obj);
			 * 
			 * } catch (JSONException e) { e.printStackTrace(); }
			 * 
			 * }
			 * 
			 * handler.sendEmptyMessage(0);
			 * 
			 * } });
			 * 
			 * thread.start();
			 */

		}
	}

	/**
	 * Get data of from bundle
	 */
	private void getBundleValue() {

		Intent intent = getIntent();

		Module module = (Module) intent
				.getParcelableExtra(IntentKey.MOUDLE_POJO);

		mModuleCode = module.getModuleCode();

		mActivityData = Helper.getActivityDataMasterModel(
				getApplicationContext(), module);

		if (mActivityData != null) {
			/*
			 * mActivityID =
			 * DatabaseHelper.getConnection(getApplicationContext())
			 * .getActivityIdIfExist(mActivityData)
			 */

			getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVITY_ID, null,
					StockEscalation.this);

		}

	}

	/**
	 *  add items into spinner one dynamically
	 * @param list 
	 */
	private void addItemsOnSpinnerPOne(ArrayList<P1CategoryList> list) {

		List<String> p1categoryName = new ArrayList<String>();
		p1categoryName.add(getStringFromResource(R.string.productcategoryP1));

		for (int i = 0; i < list.size(); i++) {
			p1categoryName.add(list.get(i).getProductTypeName().toString());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, p1categoryName);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// mySpinnerP1.setSelection(1);
		// mySpinnerP1.setPrompt(getString(R.string.productcategoryP1));
		mySpinnerP1.setAdapter(dataAdapter);
		mySpinnerp2.setEnabled(false);

	}

	/** 
	 * add items into spinner Two dynamically
	 * @param list
	 */
	private void addItemsOnSpinnerPTwo(ArrayList<P2CategoryList> list) {
		if (list != null) {
			List<String> p2ProductGroupName = new ArrayList<String>();
			p2ProductGroupName
					.add(getStringFromResource(R.string.productcategoryP2));
			for (int i = 0; i < list.size(); i++) {
				p2ProductGroupName.add(list.get(i).getProductGroupName()
						.toString());
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, p2ProductGroupName);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mySpinnerp2.setAdapter(dataAdapter);
		} else {
			List<String> p2ProductGroupName = new ArrayList<String>();
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, p2ProductGroupName);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mySpinnerp2.setAdapter(dataAdapter);
		}

	}

	/**
	 *  Show popup dialog to take order
	 * @param skuProductList Product list
	 */
	private void showPopUp(ArrayList<SKUProductList> skuProductList) {
		LayoutInflater inflater = getLayoutInflater();
		final View PopupLayout = inflater.inflate(R.layout.bookorder_listview,
				null);

		mOrderBooking = (ListView) PopupLayout
				.findViewById(R.id.lvorderbooking);

		ViewGroup header = (ViewGroup) inflater.inflate(
				R.layout.bookorder_headerview, mOrderBooking, false);

		// ViewGroup footer = (ViewGroup)
		// inflater.inflate(R.layout.bookorder_footerview, mOrderBooking,
		// false);

		Button add = (Button) PopupLayout.findViewById(R.id.add);
		Button cancel = (Button) PopupLayout.findViewById(R.id.cancel);

		add.setOnClickListener(this);
		cancel.setOnClickListener(this);
		mOrderBooking.addHeaderView(header, null, false);
		// mOrderBooking.addFooterView(footer, null, false);

		adpter = new OrderBookingAdapter(this, R.layout.bookorderitem,
				skuProductList);

		mOrderBooking.setAdapter(adpter);

		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		helpBuilder.setView(PopupLayout);
		helpDialog = helpBuilder.create();
		helpDialog.setCancelable(false);
		helpDialog.setInverseBackgroundForced(true);
		helpDialog.show();
		helpDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

	}

	@Override
	public void onClick(View v) {

		super.onClick(v);

		switch (v.getId()) {

		case R.id.add:

			//mOrderBookingAdd.setVisibility(View.VISIBLE);

			ArrayList<SKUProductList> items = adpter.getItems();
			// bookingOrderData = new ArrayList<SKUProductList>();

			for (int i = 0; i < items.size(); i++) {

				SKUProductList obj = new SKUProductList();

				if (items.get(i).getQuantity().toString().equals("0")
						|| items.get(i).getQuantity().toString().equals("")
						|| items.get(i).getQuantity().toString().equals("00")) {

					printLog("empty", "empty string");

				} else {

					obj.setSKUCode(items.get(i).getSKUCode().toString());
					obj.setProductID(items.get(i).getProductID().toString());
					obj.setQuantity(items.get(i).getQuantity().toString());
					Double qty = Double.valueOf(items.get(i).getQuantity()
							.toString().trim());
					Double price = Double.valueOf(items.get(i).getDealerPrice()
							.toString().trim());
					Double finalPrice = qty * price;
					// String result = String.valueOf(finalPrice);
					String result = decimalFormat.format(finalPrice);

					obj.setDealerPrice(result);
					obj.setProductTypeID(items.get(i).getProductTypeID()
							.toString());
					obj.setProductCategoryID(items.get(i)
							.getProductCategoryID().toString());
					obj.setProductGroupID(items.get(i).getProductGroupID()
							.toString());
					stockEscalationData.add(obj);

				}
			}
			if (stockEscalationData.size() < 1) {

				Helper.showCustomToast(StockEscalation.this, R.string.qtyempty,
						Toast.LENGTH_LONG);
				return;
			}

			addHeaderFooter();

			refreshTotal();

			helpDialog.dismiss();

			adapterAdd = new StockEscalationAddAdapter(this, mActivityID);
			mOrderBookingAdd.setAdapter(adapterAdd);

			break;

		case R.id.cancel:
			helpDialog.dismiss();

			break;

		case R.id.proceed:

			onProceedButtonClick();

			break;

		case R.id.addcancel:
			backcall();
			break;

		default:
			break;
		}
	}

	/**
	 * This method save data in database
	 * 
	 * @param list
	 *            the data to be saved
	 * @param syncStatus
	 *            the status for which data base to be saved
	 */
	private void onButtonTakeAction(final StockEscalationArrayList list,
			int syncStatus) {

		try {

			// set sync status
			mActivityData.setSyncStatus(syncStatus);

			ArrayList<JSONObject> elements = new ArrayList<JSONObject>();

			for (int i = 0; i < list.size(); i++) {

				JSONObject jsonobj = new JSONObject();

				jsonobj.put(WebConfig.WebParams.ORDERBOOKINGID, Long.valueOf(1));

				jsonobj.put(WebConfig.WebParams.PRODUCTTYPEID, Integer
						.parseInt(((SKUProductList) list.get(i))
								.getProductTypeID()));

				jsonobj.put(WebConfig.WebParams.PRODUCTGROUPID, Integer
						.parseInt(((SKUProductList) list.get(i))
								.getProductGroupID()));

				int productCatID;

				try {
					productCatID = Integer.parseInt(((SKUProductList) list
							.get(i)).getProductCategoryID());
				} catch (Exception e2) {

					e2.printStackTrace();
					productCatID = 1;
				}

				jsonobj.put(WebConfig.WebParams.PRODUCTCATEGORYID, productCatID);

				 
				jsonobj.put(WebConfig.WebParams.ORDERNO, "1");

				int productID;
				try {
					productID = Integer.parseInt(((SKUProductList) list.get(i))
							.getProductID());
				} catch (Exception e1) {

					e1.printStackTrace();

					productID = 0;
				}

				jsonobj.put(WebConfig.WebParams.PRODUCTID, productID);

				jsonobj.put(WebConfig.WebParams.QUANTITY, Integer
						.parseInt(((SKUProductList) list.get(i)).getQuantity()));

				jsonobj.put(WebConfig.WebParams.ORDER_TYPE, ORDER_TYPE);

				jsonobj.put(WebConfig.WebParams.STORE_ID_CAPS, Helper
						.getStringValuefromPrefs(StockEscalation.this,
								SharedPreferencesKey.PREF_STOREID));
				jsonobj.put(WebConfig.WebParams.USER_ID_CAPS, Helper
						.getStringValuefromPrefs(StockEscalation.this,
								SharedPreferencesKey.PREF_USERID));
				jsonobj.put(WebConfig.WebParams.USER_ROLE_ID_CAPS, Helper
						.getStringValuefromPrefs(StockEscalation.this,
								SharedPreferencesKey.PREF_ROLEIDSTOREWISE));
				if (!Helper.getStringValuefromPrefs(StockEscalation.this,
						SharedPreferencesKey.PREF_COVERAGEID).equals("")) {
					try {
						jsonobj.put(WebConfig.WebParams.COVERAGEPLANID, Integer
								.parseInt(Helper.getStringValuefromPrefs(
										StockEscalation.this,
										SharedPreferencesKey.PREF_COVERAGEID)));
					} catch (Exception e) {

						e.printStackTrace();

						jsonobj.put(WebConfig.WebParams.COVERAGEPLANID, 0);
					}
				}else {

					jsonobj.put(WebConfig.WebParams.COVERAGEPLANID, 0);
				}

				double price = Double
						.parseDouble(((SKUProductList) list.get(i))
								.getDealerPrice());

				jsonobj.put(WebConfig.WebParams.PRICE,
						Double.parseDouble(decimalFormat.format(price)));

				jsonobj.put(WebConfig.WebParams.SKUCODE,
						((SKUProductList) list.get(i)).getSKUCode());

				elements.add(jsonobj);

			}

			JSONArray jArray = new JSONArray(elements);

			insertDataInDataBaseDatabase(
					syncStatus, jArray);

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

	}

	/**
	 * This method insert data in database
	 * 
	 * @param handler
	 *            this will handle message after data insertion in data base and
	 *            post message in UI thread
	 * @param syncStatus
	 *            the sync status of the data to be inserted
	 * @param jDataArray
	 *            the data to be inserted
	 */
	private void insertDataInDataBaseDatabase(final int syncStatus,final JSONArray jDataArray) {

		try {
			if (mActivityID == -1) {
				// generate activity id
				/*mActivityID = DatabaseHelper.getConnection(
						getApplicationContext()).insertActivtyDataMaster(
						mActivityData);*/
				
				ContentValues contentValues = DatabaseUtilMethods.getActivityDataContetnValueArray(mActivityData);
				Uri uri = mContentResolver.insert(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues);
				mActivityID = ContentUris.parseId(uri);

			} else {
				
				String where = ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID+ "=?";
				String[] selectionArgs = new String[] { String.valueOf(mActivityID) };
				
				
				ContentValues contentValues = DatabaseUtilMethods.getActivityDataContentValueUpdateStatus(syncStatus);
				mContentResolver.update(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues, where, selectionArgs);
				
				/*DatabaseHelper.getConnection(getApplicationContext())
						.updateActivtyDataMaster(mActivityID, syncStatus);*/
			}

			// *************************
			// first delete data
			String whereClause = OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
					+ "=? AND "
					+ OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE
					+ "=?";
			String[] whereArgs = new String[] { String.valueOf(mActivityID),
					String.valueOf(ORDER_TYPE) };
			mContentResolver.delete(
					ProviderContract.DELETE_STOCK_ESCALATION_ORDER_RESPONSE,
					whereClause, whereArgs);

			// insert into transaction table

			ContentValues[] contentValues = getContetnValueArray(mActivityID,
					jDataArray);
			int insertOrder = mContentResolver.bulkInsert(
					ProviderContract.INSERT_STOCK_ESCALATION_ORDER_RESPONSE,
					contentValues);

			if (insertOrder > 0) {

				if (syncStatus == SyncUtils.SYNC_STATUS_SAVED) {

					Helper.showCustomToast(StockEscalation.this,
							R.string.sync_message_data_save, Toast.LENGTH_LONG);

				} else if (syncStatus == SyncUtils.SYNC_STATUS_SUBMIT) {

					Helper.showCustomToast(StockEscalation.this,
							R.string.sync_message_data_submit,
							Toast.LENGTH_LONG);

				}

				StockEscalation.this.finish();

			} else {

				Helper.showCustomToast(StockEscalation.this,
						R.string.error_occoured, Toast.LENGTH_LONG);
			}

			/*
			 * boolean result = DatabaseHelper.getConnection(
			 * getApplicationContext()).insertOrderRespnse( mActivityID,
			 * ORDER_TYPE, jDataArray);
			 * 
			 * Message msg = handler.obtainMessage(); Bundle bundle = new
			 * Bundle(); bundle.putBoolean("is_success", result);
			 * bundle.putInt("sync_status", syncStatus);
			 * 
			 * msg.setData(bundle); handler.sendMessage(msg);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

 


 

	

	

	protected ContentValues[] getContetnValueArray(long activityID, JSONArray jArray) {
		// TODO Auto-generated method stub
		 
		int count = jArray.length();
		
		ContentValues[] mContentValueArray  = new ContentValues[count];
		
		for (int i = 0; i < count; i++) {

			try {

				ContentValues initialValues = new ContentValues();
				JSONObject jsonObject = jArray.getJSONObject(i);

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
								activityID);
				initialValues
						.put(OrderResponseMasterTableColumns.KEY_USER_ROLE_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_USER_ROLE_ID));
				initialValues
						.put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_QUANTITY,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_QUANTITY));
				initialValues
						.put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDERNO,
								jsonObject
										.getString(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDERNO));
				initialValues
						.put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID));

				try {
					initialValues
							.put(OrderResponseMasterTableColumns.KEY_BEAT_STORE_ID,
									jsonObject
											.getInt(OrderResponseMasterTableColumns.KEY_BEAT_STORE_ID));
				} catch (JSONException e) {
					e.printStackTrace();

					initialValues.put(
							OrderResponseMasterTableColumns.KEY_BEAT_STORE_ID,
							0);
				}


				initialValues
						.put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_USER_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_USER_ID));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_ID));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID,
								jsonObject
										.getInt(OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_PRICE,
								jsonObject
										.getDouble(OrderResponseMasterTableColumns.KEY_ORDER_MASTER_PRICE));

				initialValues
						.put(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE,
								jsonObject
										.getString(OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE));

				mContentValueArray[i] = initialValues;

			} catch (Exception e) {

				Helper.printStackTrace(e);

			}

		}

		return mContentValueArray;
	}


	@Override
	public void onBackPressed() {

		backcall();

	}

	/**
	 * Show confirmation dialog to user
	 */
	private void backcall() {

		if (stockEscalationData != null && stockEscalationData.size() > 0) {

			Helper.showAlertDialog(
					StockEscalation.this,
					SSCAlertDialog.WARNING_TYPE,
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

	/*private class FindSKU extends AsyncTask<Object, Object, Object> {

		ProgressDialog progDialog = new ProgressDialog(StockEscalation.this);

		int position;

		private DatabaseHelper databaseHelper;

		FindSKU(int position) {

			this.position = position;
			databaseHelper = DatabaseHelper.getConnection(StockEscalation.this);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("Loading...");
			progDialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {

			// prodCategoryP3 =
			// dbq.getSKUProductList(prodCategoryP2.get(position -
			// 1).getProductGroupCode());
			 
			 
			prodCategoryP3 = databaseHelper.getSKUProductList(prodCategoryP2
					.get(position - 1).getProductGroupCode());

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			showPopUp(prodCategoryP3);

			progDialog.cancel();
		}

	}
*/
	/**
	 * This method refresh the total amount and quantity each time it is called
	 */
	public void refreshTotal() {

		if (stockEscalationData.size() > 0) {

			double total = 0;
			int quentity = 0;
			for (int i = 0; i < stockEscalationData.size(); i++) {
				int qty = Integer
						.parseInt(((SKUProductList) stockEscalationData.get(i))
								.getQuantity());
				double tot = Double
						.parseDouble(((SKUProductList) stockEscalationData
								.get(i)).getDealerPrice());

				quentity = quentity + qty;
				total = total + tot;
			}

			if (totalAnount != null && totalQty != null) {
				totalAnount.setText("Total Amount: "
						+ decimalFormat.format(total));
				totalQty.setText("Total Qty: " + quentity);
			}
		}

	}

	/**
	 * Add header and footer to selected data by the user
	 */
	private void addHeaderFooter() {

		if (mOrderBookingAdd.getHeaderViewsCount() == 0) {

			LayoutInflater inflater = getLayoutInflater();
			ViewGroup header = (ViewGroup) inflater.inflate(
					R.layout.bookorderadd_header, mOrderBookingAdd, false);
			ViewGroup footer = (ViewGroup) inflater.inflate(
					R.layout.bookorderadd_footer, mOrderBookingAdd, false);
			Button proceed = (Button) footer.findViewById(R.id.proceed);
			Button cancel = (Button) footer.findViewById(R.id.addcancel);

			if(Helper.getBoolValueFromPrefs(getApplicationContext(), SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS))
			{
				proceed.setText(getString(R.string.proceed));
			}
			
			totalQty = (TextView) footer.findViewById(R.id.totalQty);
			totalAnount = (TextView) footer.findViewById(R.id.totalAmount);

			proceed.setOnClickListener(this);
			cancel.setOnClickListener(this);

			mOrderBookingAdd.addHeaderView(header, null, false);
			mOrderBookingAdd.addFooterView(footer, null, false);

		}
	}

	private void addtempDataTwoSpinner(ArrayList<String> list) {
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinnerp2.setAdapter(dataAdapter);
		mySpinnerp2.setEnabled(false);

	}

	/**
	 * This method show the alert dialog which ask the user about the operation
	 * on the data
	 */
	private void onProceedButtonClick() {

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
			DialogInterface.OnClickListener buttonSubmitListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					onButtonTakeAction(adapterAdd.getItems(),
							SyncUtils.SYNC_STATUS_SUBMIT);

				}
			};

			DialogInterface.OnClickListener buttonSaveListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					onButtonTakeAction(adapterAdd.getItems(),
							SyncUtils.SYNC_STATUS_SAVED);
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

		else {
			onButtonTakeAction(adapterAdd.getItems(),
					SyncUtils.SYNC_STATUS_SUBMIT);

		}
	}


	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
		// TODO Auto-generated method stub

		Loader cursorLoader = null;

		if (loaderId == LOADER_PRODUCT_CATAGORY_P1_ID) {
			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.STOCK_ESCALATION_PRODUCT_CATAGORY_P1_URI,
					null, null, null, null);
		} else if (loaderId == LOADER_PRODUCT_CATAGORY_P2_ID) {

			String product1 = bundle.getString("KEY_PRODUCT1");
			String[] arg = { product1 };
			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.STOCK_ESCALATION_PRODUCT_CATAGORY_P2_URI,
					null, null, arg, null);
		} else if (loaderId == LOADER_ORDER_RESPONSE_ID) {

			String mActivityID1 = String.valueOf(bundle
					.getLong("KEY_Activity_ID"));
			String orderType = String.valueOf(bundle.getInt("KEY_ORDER_TYPE"));

			String[] arg = { mActivityID1, orderType };

			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.STOCK_ESCALATION_ORDER_RESPONSE_URI, null,
					null, arg, null);
			
		} else if (loaderId == LOADER_FIND_SKU_ID) {

			String prodCat2 = bundle.getString("KEY_PRODUCT_CATEGORY2");
			String[] arg2 = { prodCat2 };
			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.STOCK_ESCALATION_FIND_SKU, null, null,
					arg2, null);
		} else if (loaderId == LOADER_ID_GET_ACTIVITY_ID) {

			String[] arg = { String.valueOf(mActivityData.getStoreID()),
					String.valueOf(mActivityData.getModuleCode()) };
			
			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null, arg,
					null);

		}

		return cursorLoader;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loaderId, final Cursor cursor) {
		// TODO Auto-generated method stub
		
		if (loaderId.getId() == LOADER_PRODUCT_CATAGORY_P1_ID) {
			
			if (cursor != null &&cursor.getCount() > 0) {
				
				prodCategoryP1 = DatabaseUtilMethods.getProductCatagoryP1(cursor);
				addItemsOnSpinnerPOne(prodCategoryP1);
				
			}else {
				Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_SHORT).show();
				
			}
		} else if (loaderId.getId() == LOADER_PRODUCT_CATAGORY_P2_ID) {

			if (cursor != null && cursor.getCount() > 0) {
				prodCategoryP2 = DatabaseUtilMethods
						.getProductCatagoryP2(cursor);
				
				addItemsOnSpinnerPTwo(prodCategoryP2);
			} else {
				
				Toast.makeText(getApplicationContext(), "No Product Catagory P2", Toast.LENGTH_SHORT).show();
			}

		} else if (loaderId.getId() == LOADER_ORDER_RESPONSE_ID) {

			if (cursor != null && cursor.getCount() > 0) {

				JSONArray jArray = DatabaseUtilMethods.getOrderResposeJSONFormCursor(cursor,
						isProductPriceNeeded);
				for (int i = 0; i < jArray.length(); i++) {

					try {
						SKUProductList obj = new SKUProductList();

						JSONObject jsonObject = jArray.getJSONObject(i);

						obj.setSKUCode(jsonObject
								.getString(WebConfig.WebParams.SKUCODE));
						obj.setProductID(jsonObject
								.getInt(WebConfig.WebParams.PRODUCTID) + "");
						obj.setQuantity(jsonObject
								.getInt(WebConfig.WebParams.QUANTITY) + "");

						obj.setDealerPrice(decimalFormat.format(jsonObject
								.getDouble(WebConfig.WebParams.PRICE)));

						obj.setProductTypeID(jsonObject
								.getInt(WebConfig.WebParams.PRODUCTTYPEID) + "");
						obj.setProductCategoryID(jsonObject
								.getInt(WebConfig.WebParams.PRODUCTCATEGORYID)
								+ "");
						obj.setProductGroupID(jsonObject
								.getInt(WebConfig.WebParams.PRODUCTGROUPID)
								+ "");

						stockEscalationData.add(obj);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				if (stockEscalationData.size() > 0) {
					 addHeaderFooter();

					adapterAdd = new StockEscalationAddAdapter(
							StockEscalation.this, mActivityID);

					mOrderBookingAdd.setAdapter(adapterAdd);

					refreshTotal();
				}

			} else {
				Toast.makeText(getApplicationContext(), "No Record Found",
						Toast.LENGTH_SHORT).show();
			}
		}else if (loaderId.getId() == LOADER_FIND_SKU_ID) {
			
			if (cursor != null && cursor.getCount() > 0) {
				
				prodCategoryP3 = DatabaseUtilMethods.getSKUProductList(cursor);
				showPopUp(prodCategoryP3);
				
			} else {
				Toast.makeText(getApplicationContext(), "No Record Found",
						Toast.LENGTH_SHORT).show();
			}
		} else if(loaderId.getId() == LOADER_ID_GET_ACTIVITY_ID){
			
			if (cursor != null && cursor.getCount() > 0) {
				
				 mActivityID = DatabaseUtilMethods.getActivityID(cursor);
				
			}
			 getDataFromDataBase();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
		
		
	}
 }
