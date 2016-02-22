package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyTabFactory;

public class OutletWorking1 extends BaseActivity implements
		OnTabChangeListener, OnPageChangeListener {

	private TabHost mTabHost;

	private ViewPager mViewPager;

	public MyPageAdapter pageAdapter;

	@Override
	public void init() {
		super.init();

		hideKeyboard(); 
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.outletworking2);

		mViewPager = (ViewPager) findViewById(R.id.viewpager_outletProfile);

		initialiseTabHost();
		setUpViewPager();

	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		// if user is not a roaming profile
		if (Helper.getStringValuefromPrefs(OutletWorking1.this,
				SharedPreferencesKey.PREF_IS_ROAMING_PROFILE).equalsIgnoreCase("false")) {
			OutletWorkingFragmentTodayStores f1 = new OutletWorkingFragmentTodayStores();
			fList.add(f1);
		}

		OutletWorkingFragmentOtherStores f2 = new OutletWorkingFragmentOtherStores();

		fList.add(f2);

		return fList;
	}

	private void setUpViewPager() {
		// Fragments and ViewPager Initialization
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pageAdapter);
		mViewPager.setOnPageChangeListener(OutletWorking1.this);
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

			// if user is not a roaming profile
			if (Helper.getStringValuefromPrefs(OutletWorking1.this,
					SharedPreferencesKey.PREF_IS_ROAMING_PROFILE)
					.equalsIgnoreCase("false")) {

				addTab(this,
						this.mTabHost,
						this.mTabHost
								.newTabSpec(
										getResources().getString(
												R.string.todays_planned_stores))
								.setIndicator(
										getResources().getString(
												R.string.todays_planned_stores)));

			}

			addTab(this,
					this.mTabHost,
					this.mTabHost.newTabSpec(
							getResources().getString(R.string.other_stores))
							.setIndicator(

							getResources().getString(R.string.other_stores)));

			mTabHost.setOnTabChangedListener(this);

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	// Method to add a TabHost
	private void addTab(OutletWorking1 activity, TabHost tabHost,
			TabHost.TabSpec tabSpec) {
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
	
	

}
