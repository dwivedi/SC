package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources.NotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyTabFactory;

 public class OutletProfile2 extends BaseActivity implements
		OnTabChangeListener, OnPageChangeListener{

	private TabHost mTabHost;

	private ViewPager mViewPager;

	public MyPageAdapter pageAdapter;

	@Override
	public void init() {
		super.init();

		/*this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		setContentView(R.layout.outlet_profile1);

		mViewPager = (ViewPager) findViewById(R.id.viewpager_outletProfile);

		initialiseTabHost();
		setUpViewPager();

	}
	
	 
	

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		OutletProfileFragmentBasic1 f1 = new OutletProfileFragmentBasic1();
		fList.add(f1);
		
		if(Helper.getBoolValueFromPrefs(this, SharedPreferencesKey.PREF_SHOW_PERFRMANCE_TAB))
		{
			OutletProfileFragmentPerformance1 f2 = new OutletProfileFragmentPerformance1();
			fList.add(f2);
		}
		
		return fList;
	}

	private void setUpViewPager() {
		// Fragments and ViewPager Initialization
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pageAdapter);
		mViewPager.setOnPageChangeListener(OutletProfile2.this);
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
							getResources().getString(
									R.string.outlet_profile_basic))
							.setIndicator(
									getResources().getString(
											R.string.outlet_profile_basic)));

			
			if(Helper.getBoolValueFromPrefs(this, SharedPreferencesKey.PREF_SHOW_PERFRMANCE_TAB))
			{
				addTab(this,
						this.mTabHost,
						this.mTabHost
								.newTabSpec(
										getResources()
												.getString(
														R.string.outlet_profile_performance))
								.setIndicator(

										getResources()
												.getString(
														R.string.outlet_profile_performance)));
			}
			
			
		

			mTabHost.setOnTabChangedListener(this);

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	// Method to add a TabHost
	private void addTab(OutletProfile2 activity, TabHost tabHost,
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

}
