package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.activitymodule.OutletWorking1.MyPageAdapter;
import com.samsung.ssc.activitymodule.RaceProductAuditCartActivity.GetProductAuditHandler;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyTabFactory;

public class RaceProductAuditCartActivity1 extends BaseActivity implements
		OnTabChangeListener, OnPageChangeListener,LoaderCallbacks<Cursor> {

	private TabHost mTabHost;

	private ViewPager mViewPager;

	public MyPageAdapter pageAdapter;
	
	 long mActivityID = -1;
	private ActivityDataMasterModel mActivityData;
	private final int LOADER_ID_GET_ACTIVITY_ID = 1;
	@Override
	public void init() {
		super.init();

		hideKeyboard();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.race_cart_activity);

		mViewPager = (ViewPager) findViewById(R.id.viewpager_outletProfile);

		initialiseTabHost();
		setUpViewPager();
		getBundleValue();
		setUpView();
		
	}

	private void setUpView() {
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

	private void getBundleValue() {
		Bundle bundle = getIntent().getExtras();
		mActivityID = bundle.getLong("ACTIVITY_ID");
		mActivityData = (ActivityDataMasterModel) bundle
				.getSerializable("ACTIVITY_DATA");

	}
	

	
	
	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		RaceAVCartFragment f1 = new RaceAVCartFragment();
		fList.add(f1);

		RaceHACartFragment f2 = new RaceHACartFragment();

		fList.add(f2);

		return fList;
	}

	private void setUpViewPager() {
		// Fragments and ViewPager Initialization
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pageAdapter);
		mViewPager.setOnPageChangeListener(RaceProductAuditCartActivity1.this);
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

	// Tabs Creation
	private void initialiseTabHost() {

		try {

			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();



				addTab(this,
						this.mTabHost,
						this.mTabHost.newTabSpec(
								getResources().getString(R.string.av))
								.setIndicator(
										getResources().getString(R.string.av)));

			

			addTab(this,
					this.mTabHost,
					this.mTabHost.newTabSpec(
							getResources().getString(R.string.ha))
							.setIndicator(

							getResources().getString(R.string.ha)));

			mTabHost.setOnTabChangedListener(this);

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	// Method to add a TabHost
	private void addTab(RaceProductAuditCartActivity1 activity,
			TabHost tabHost, TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	@Override
	public void onBackPressed() {

		finish();

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

	private void hideKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
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
			
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	
	
}
