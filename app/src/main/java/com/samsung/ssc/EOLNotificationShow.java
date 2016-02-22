package com.samsung.ssc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.EOLSchemeDTO;
import com.samsung.ssc.dto.EOLSchemeDetailDTO;
import com.samsung.ssc.dto.EOLSchemeOrderDTO;
import com.samsung.ssc.services.UpdateNotificationService;
import com.samsung.ssc.util.Helper;

public class EOLNotificationShow extends BaseActivity implements
		OnTabChangeListener, OnPageChangeListener {

	boolean fromGCM = false;
	private int notificationServiceID = 0;
	private int notificationId = 0;
	private int mNotificatioType;
	private int readStatus;
	private String body;
	LinearLayout scheme_header_container;
	ListView mLvSchemeDetail;
	int schemeID;

	private TabHost mTabHost;
	private ViewPager mViewPager;
	MyPageAdapter pageAdapter;
	EOLSchemeDTO mEolSchemeDto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_eol_notification1);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		getValuesFromIntent();

		// Tab Initialization
		initialiseTabHost();

		setCurrentContext(EOLNotificationShow.this);

		TextView tv_title = (TextView) findViewById(R.id.tv_title_sdActionBar);

		// new scheme
		if (notificationId != -1 && notificationId == 1) {
			tv_title.setText(R.string.eol_scheme_detail_header);
		}
		// updated scheme
		else if (notificationId != -1 && notificationId == 2) {
			tv_title.setText(R.string.eol_updated_scheme_detail_header);

		}
		// new ordered scheme
		else if (notificationId != -1 && notificationId == 3) {
			tv_title.setText(R.string.eol_scheme_order_header);

		}

		if (fromGCM && notificationId != 3) { // if user is coming form GCM
												// notification oand
												// notification
												// is not eol product order type
												// then get the
												// data from sqlite database
			new GetSchemeAsyncTask().execute(schemeID);
		}

		else {
			createEOLSchemeDTOFromJson();
			setUpViewPager();
		}

		if (readStatus != 1) {
			updateReadStatusOnServer();
		}

	}

	private void getValuesFromIntent() {
		Intent receivedIntent = getIntent();

		this.mNotificatioType = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_MESSAGE_TYPE, -1);
		this.fromGCM = receivedIntent.getBooleanExtra("FROM_GCM", false);
		this.readStatus = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_READ_STATUS, -1);
		this.notificationServiceID = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_SERVICE_ID, -1);
		receivedIntent
				.getStringExtra(IntentKey.NOTIFICATION_MESSAGE_TITLE);
		this.body = receivedIntent
				.getStringExtra(IntentKey.NOTIFICATION_MESSAGE_BODY);
		this.notificationId = receivedIntent.getIntExtra(
				IntentKey.NOTIFICATION_ID, -1);

		this.schemeID = receivedIntent.getIntExtra("scheme_id", -1);

	}

	private class GetSchemeAsyncTask extends
			AsyncTask<Integer, Void, EOLSchemeDTO> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				/*progressDialog = new ProgressDialog(context,
						ProgressDialog.THEME_HOLO_LIGHT);*/
				
				progressDialog = SSCProgressDialog.ctor(EOLNotificationShow.this);
				
			} else {
			progressDialog = new ProgressDialog(EOLNotificationShow.this);
			progressDialog.setProgress(0);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(getResources().getString(
					R.string.loadingmessage));
			progressDialog.setCancelable(false);
			
			}
			progressDialog.show();
		}

		@Override
		protected EOLSchemeDTO doInBackground(Integer... params) {
			EOLSchemeDTO eolScheme = null;
			try {
				eolScheme = DatabaseHelper.getConnection(
						getApplicationContext()).getScheemData(params[0]);

				return eolScheme;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return eolScheme;
		}

		@Override
		protected void onPostExecute(EOLSchemeDTO result) {

			super.onPostExecute(result);
			progressDialog.dismiss();

			if (result != null) {

				// if scheme product order notification
				// if (notificationId == 3) {
				//
				// result = setProductOrDerData(result);
				// }

				mEolSchemeDto = result;

				setUpViewPager();
				// showScheme(result);
			}

		}

	}

	private void setUpViewPager() {
		// Fragments and ViewPager Initialization
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pageAdapter);
		mViewPager.setOnPageChangeListener(EOLNotificationShow.this);
	}

	/**
	 * iT SETS THE PRODUCT ORDER DATA IN EOLSchemeDTO FOR SCHEME ORDER
	 * NOTIFICATION
	 * 
	 * @param result
	 *            EOLSchemeDTO Object returned by
	 * @return EOLSchemeDTO after appending Order data on it.
	 */
	private EOLSchemeDTO setProductOrDerData(EOLSchemeDTO result) {

		if (body != null) {
			try {

				ArrayList<EOLSchemeOrderDTO> orderList = new ArrayList<EOLSchemeOrderDTO>();
				JSONObject json = new JSONObject(body);
				JSONArray jArray = json.getJSONArray("EOLOrderBooking");
				int length = jArray.length();

				if (length > 0) {
					for (int i = 0; i < length; i++) {
						JSONObject jObject = jArray.getJSONObject(i);

						EOLSchemeOrderDTO order = new EOLSchemeOrderDTO();
						order.setBasicModelCode(jObject
								.getString("BasicModelCode"));
						order.setActualSupport(jObject.getInt("ActualSupport"));
						order.setOrderQunatity(jObject.getInt("OrderQuantity"));
						order.setSchemeID(jObject.getInt("SchemeID"));
						order.setStoreID(jObject.getInt("StoreID"));
						order.setStoreName(jObject.getString("StoreName"));
						orderList.add(order);
					}

					result.setSchemeOrders(orderList);
				}

			} catch (JSONException e) {

				e.printStackTrace();

			}

		}

		return result;

	}

	public void onExitClick(View view) {
		handleExitClick();
	}

	public void onViewMoreClick(View view) {

		handleViewMore();

	}

	private void handleViewMore() {

		if (!Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)
				.equalsIgnoreCase("")) {
			try {
				 
				Intent intent = new Intent(EOLNotificationShow.this,
						KPIListActivity1.class);
				startActivity(intent);
				finish();

			} catch (NumberFormatException e) {
				
				Helper.showCustomToast(getApplicationContext(),R.string.user_id_not_in_format, Toast.LENGTH_LONG);
				
			}
		} else {
			Helper.showCustomToast(getApplicationContext(),R.string.you_are_logged_out, Toast.LENGTH_LONG);
		}

	}

	private void handleExitClick() {
		finishActivity();
	}

	protected void finishActivity() {
		if (fromGCM) {
			finish();
		} else {
			if (notificationServiceID != -1 || notificationServiceID != 0) {
				Intent resultIntent = new Intent();
				resultIntent.putExtra(IntentKey.NOTIFICATION_SERVICE_ID,
						notificationServiceID);
				resultIntent.putExtra(IntentKey.NOTIFICATION_MESSAGE_TYPE,
						this.mNotificatioType);
				setIntent(resultIntent);

				finish();
			}
		}
	}

	class MyTabFactory implements TabContentFactory {

		private final Context mContext;

		public MyTabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		int pos = this.mViewPager.getCurrentItem();
		this.mTabHost.setCurrentTab(pos);

	}

	@Override
	public void onPageSelected(int arg0) {

	}

	@Override
	public void onTabChanged(String tabId) {
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);

	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		EOLSchemeDetailFragment f1 = new EOLSchemeDetailFragment();
		EOLSchemeProductFragment f2 = new EOLSchemeProductFragment();
		fList.add(f1);
		fList.add(f2);

		if (notificationId != -1 && notificationId == 3) {
			EOLSchemeOrderFragment f3 = new EOLSchemeOrderFragment();
			fList.add(f3);
		}

		return fList;
	}

	// Tabs Creation
	private void initialiseTabHost() {

		try {

			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();

			AddTab(this,
					this.mTabHost,
					this.mTabHost.newTabSpec(
							getResources()
									.getString(R.string.eol_scheme_detail))
							.setIndicator(
									getResources().getString(
											R.string.eol_scheme_detail)));

			AddTab(this,
					this.mTabHost,
					this.mTabHost.newTabSpec(
							getResources().getString(
									R.string.eol_product_unit_support))
							.setIndicator(

									getResources().getString(
											R.string.eol_product_unit_support)));

			if (notificationId != -1 && notificationId == 3) {
				AddTab(this,
						this.mTabHost,
						this.mTabHost.newTabSpec(
								getResources().getString(
										R.string.eol_ordered_product))
								.setIndicator(
										getResources().getString(
												R.string.eol_ordered_product)));

			}

			mTabHost.setOnTabChangedListener(this);

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	// Method to add a TabHost
	private void AddTab(EOLNotificationShow activity, TabHost tabHost,
			TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	/**
	 * This method create EOLSchemeDTO from the json data
	 */
	private void createEOLSchemeDTOFromJson() {

		try {
			EOLSchemeDTO schemeDTO = new EOLSchemeDTO();

			JSONObject jsonObject = new JSONObject(body);

			schemeDTO.setSchemeID(jsonObject.getInt("SchemeID"));
			schemeDTO.setSchemeNumber(jsonObject.getString("SchemeNumber"));
			schemeDTO.setSchemeFrom(jsonObject.getString("strSchemeFrom"));
			schemeDTO.setSchemeTo(jsonObject.getString("strSchemeTo"));

			schemeDTO.setOrderFrom(jsonObject.getString("strOrderFrom"));
			schemeDTO.setOrderTo(jsonObject.getString("strOrderTo"));
			schemeDTO.setPUMINumber(jsonObject.getString("PUMINumber"));
			schemeDTO.setPumiDate(jsonObject.getString("strPUMIDate"));
			schemeDTO.setProductType(jsonObject.getString("ProductType"));

			schemeDTO.setProductCategory(jsonObject
					.getString("ProductCategory"));
			schemeDTO.setProductGroup(jsonObject.getString("ProductGroup"));
			schemeDTO.setCreatedDate(jsonObject.getString("strCreatedDate"));
			schemeDTO.setModifiedDate(jsonObject.getString("strModifiedDate"));

			JSONArray jsonDetailsObjectArray = jsonObject
					.getJSONArray("EOLSchemeDetails");
			if (jsonDetailsObjectArray != null) {
				int countDetails = jsonDetailsObjectArray.length();
				if (countDetails > 0) {

					ArrayList<EOLSchemeDetailDTO> al_scheme_detail = new ArrayList<EOLSchemeDetailDTO>();

					for (int j = 0; j < countDetails; j++) {
						JSONObject jsonObjectDetails = jsonDetailsObjectArray
								.getJSONObject(j);

						EOLSchemeDetailDTO detail = new EOLSchemeDetailDTO();

						detail.setSchemeID(jsonObjectDetails.getInt("SchemeID"));
						detail.setBasicModelCode(jsonObjectDetails
								.getString("BasicModelCode"));
						detail.setQuatity(jsonObjectDetails.getInt("Quantity"));
						detail.setSupport(jsonObjectDetails.getInt("Support"));

						al_scheme_detail.add(detail);

					}
					schemeDTO.setScheemDetails(al_scheme_detail);

				}

			}

			mEolSchemeDto = setProductOrDerData(schemeDTO);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void updateReadStatusOnServer() {

		if (!Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)
				.equalsIgnoreCase("")) {
			try {
				 
				Intent updateServiceIntent = new Intent(
						EOLNotificationShow.this,
						UpdateNotificationService.class);
				updateServiceIntent.putExtra(IntentKey.NOTIFICATION_SERVICE_ID,
						notificationServiceID);
				updateServiceIntent.putExtra(IntentKey.NOTIFICATION_ID,
						notificationId);
				startService(updateServiceIntent);

			} catch (NumberFormatException e) {
				Helper.showCustomToast(getApplicationContext(),R.string.user_id_not_in_format, Toast.LENGTH_LONG);
			}
		} else {

			Helper.showCustomToast(getApplicationContext(),R.string.you_are_logged_out, Toast.LENGTH_LONG);
		}
	}

}