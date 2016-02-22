package com.samsung.ssc.activitymodule;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class OutletProfile extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_profile);

        setUpView();

    }

    private void setUpView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager_outletProfile);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayoutOutletProfile);

        setSupportActionBar(toolbar);

        try {

            mTabLayout.addTab(mTabLayout.newTab().setText(getStringFromResource(R.string.outlet_profile_basic)).setContentDescription("Basic Info"));
            if (Helper.getBoolValueFromPrefs(this, SharedPreferencesKey.PREF_SHOW_PERFRMANCE_TAB)) {
                mTabLayout.addTab(mTabLayout.newTab().setText(getStringFromResource(R.string.outlet_profile_performance)).setContentDescription("Performance Info"));
            }
            mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int pos = tab.getPosition();
                    mViewPager.setCurrentItem(pos);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                /**
                 * @param tab
                 */
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });



            List<Fragment> fragments = getFragments();
            MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
            mViewPager.setAdapter(pageAdapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        OutletProfileFragmentBasic1 f1 = new OutletProfileFragmentBasic1();
        fList.add(f1);

        if (Helper.getBoolValueFromPrefs(this, SharedPreferencesKey.PREF_SHOW_PERFRMANCE_TAB)) {
            OutletProfileFragmentPerformance1 f2 = new OutletProfileFragmentPerformance1();
            fList.add(f2);
        }

        return fList;
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


}
