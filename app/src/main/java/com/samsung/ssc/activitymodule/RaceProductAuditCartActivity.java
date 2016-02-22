package com.samsung.ssc.activitymodule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.adapters.CustomProductAduitAddCartAdapter;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.RaceProductAddToCartDTO;
import com.samsung.ssc.provider.ProviderContract;

public class RaceProductAuditCartActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private CustomProductAduitAddCartAdapter productCartAdapter;
	private long mActivityID = -1;
	private ActivityDataMasterModel mActivityData;
	protected ArrayList<RaceProductAddToCartDTO> cartItemList;
	protected TextView tv_no_item_product_audit_cart;
	private final int LOADER_ID_GET_ACTIVITY_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.product_audit_cart);

		getBundleValue();
		setUpView();

	}

	private void getBundleValue() {
		Bundle bundle = getIntent().getExtras();
		mActivityID = bundle.getLong("ACTIVITY_ID");
		mActivityData = (ActivityDataMasterModel) bundle
				.getSerializable("ACTIVITY_DATA");

	}

	private void setUpView() {

		cartItemList = new ArrayList<RaceProductAddToCartDTO>();
		ListView lvRaceProductAuditCart = (ListView) findViewById(R.id.lv_race_productaudit_cart);

		lvRaceProductAuditCart
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						RaceProductAddToCartDTO modal = (RaceProductAddToCartDTO) parent
								.getItemAtPosition(position);

						onListItemClick(modal);
					}

				});

		tv_no_item_product_audit_cart = (TextView) findViewById(R.id.tv_no_item_product_audit_cart);
		productCartAdapter = new CustomProductAduitAddCartAdapter(
				RaceProductAuditCartActivity.this,
				tv_no_item_product_audit_cart, mActivityID);
		lvRaceProductAuditCart.setAdapter(productCartAdapter);

		if (mActivityData != null) {

			/*
			 * mActivityID =
			 * DatabaseHelper.getConnection(getApplicationContext())
			 * .getActivityIdIfExist(mActivityData);
			 */

			getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVITY_ID,
					null, this);

		}

	}

	static class GetProductAuditHandler extends Handler {

		WeakReference<RaceProductAuditCartActivity> eolActivity;

		public GetProductAuditHandler(RaceProductAuditCartActivity fms) {

			eolActivity = new WeakReference<RaceProductAuditCartActivity>(fms);
		}

		@Override
		public void handleMessage(Message msg) {

			boolean data_available = msg.getData().getBoolean("data_available");

			if (data_available) {

				final RaceProductAuditCartActivity activityRefrence = eolActivity
						.get();

				if (activityRefrence!=null) {
					if (activityRefrence.cartItemList != null
							&& activityRefrence.cartItemList.isEmpty()) {
						activityRefrence.tv_no_item_product_audit_cart
								.setVisibility(View.VISIBLE);
					} else {
						activityRefrence.productCartAdapter
								.addItems(activityRefrence.cartItemList);
					}
				}

			}

		};

	}

	private void getProductAuditFromDatabase(final Handler handler) {
		Thread mThread = new Thread() {
			@Override
			public void run() {

				try {

					/*cartItemList = DatabaseHelper.getConnection(
							getApplicationContext())
							.getProductAuditAddToCartItemDetail(mActivityID);*/

					Cursor cursor=getContentResolver().query(
							ProviderContract.URI_RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS, null,
							null, new String[] { String.valueOf(mActivityID) },
							null);
					
					if(cursor!=null)
					{
						cartItemList=DatabaseUtilMethods.getProductAuditCartFromCursor(cursor);
						DatabaseUtilMethods.getProductAuditCartForAVFromCursor(cursor);
						cursor.close();
					}

					Message msg = handler.obtainMessage();
					Bundle bundle = new Bundle();

					bundle.putBoolean("data_available", true);

					msg.setData(bundle);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		mThread.start();

	}

	private void onListItemClick(RaceProductAddToCartDTO modal) {

		Intent intent = new Intent(RaceProductAuditCartActivity.this,
				RaceProductAuditCartItemUpdateActivity.class);
		intent.putExtra("STOCK_AUDIT_ID", modal.getStockAuditid());
		intent.putExtra("PRODUCT_NAME", modal.getProductName());
		startActivity(intent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		String[] arg = { String.valueOf(mActivityData.getStoreID()),
				String.valueOf(mActivityData.getModuleCode()) };

		return new CursorLoader(getApplicationContext(),
				ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null, arg,
				null);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {

			mActivityID = DatabaseUtilMethods.getActivityID(cursor);
			if (mActivityID != -1) {
				getProductAuditFromDatabase(new GetProductAuditHandler(this));
			}
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}
}
