package com.samsung.ssc.EMS;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.LMS.LeaveListFilterActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.dto.EMSExpenseDetail;

public class ExpenseListingActivity extends BaseActivity
{
	private ExpenseListingFragment mExpenseListingFragment;
	private ViewPager mViewPager ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_listing1);

		setUpView();

	}

	
	private void setUpView() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.expense_listing));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



		 TabLayout tabLayout = (TabLayout) findViewById(R.id.expense_status_tab_layout);
		
		 // Setup the viewPager
		 mViewPager = (ViewPager) findViewById(R.id.expense_list_view_pager);

		mViewPager.setOffscreenPageLimit(4);
        ExpenseListFragmentPageAdapter pagerAdapter = new ExpenseListFragmentPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        // By using this method the tabs will be populated according to viewPager's count and
        // with the name from the pagerAdapter getPageTitle()
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        
        // This method ensures that tab selection events update the ViewPager and page changes update the selected tab.
        tabLayout.setupWithViewPager(mViewPager);
        
        mViewPager.addOnPageChangeListener(new ExpenseCategoryOnPageChangeListener(tabLayout));  

       // use own OnTabSelectedListener  
        tabLayout.setOnTabSelectedListener(new MyOnTabSelectedListener()); 
        
        mViewPager.setCurrentItem(0);   
        tabLayout.getTabAt(0).select();
		
	}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_ems_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sync:
                onSyncClick();
                break;
            case R.id.menu_search_ems:

                onSearchExpenseBtnClick();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ExpenseCategoryOnPageChangeListener implements ViewPager.OnPageChangeListener {
	  
		private TabLayout mTabLayout;

	    public ExpenseCategoryOnPageChangeListener(TabLayout tabLayout) {
	        this.mTabLayout = tabLayout;
	    }

	    @Override
	    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	    }

	    @Override
	    public void onPageSelected(int position) {
	        
	    	if(mTabLayout != null) {
	            mTabLayout.getTabAt(position).select();           
	            int index = mViewPager.getCurrentItem();
		        ExpenseListFragmentPageAdapter adapter = ((ExpenseListFragmentPageAdapter)mViewPager.getAdapter());
		       
		       // mExpenseListingFragment = (ExpenseListingFragment) adapter.getItem(index);
		        mExpenseListingFragment = adapter.getFragmentInstanceAtPosition(index);
		        mExpenseListingFragment.expenseDataNotAvailableFetchFromDB();
	        }
	        
	    }

	    @Override
	    public void onPageScrollStateChanged(int state) {

	    }
	}

	private class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
	    @Override
	    public void onTabSelected(TabLayout.Tab tab) {
	        int position = tab.getPosition();
	        if (mViewPager.getCurrentItem() != position) {
	            mViewPager.setCurrentItem(position, true);
	        }
	    }

	    @Override
	    public void onTabUnselected(TabLayout.Tab tab) {

	    }

	    @Override
	    public void onTabReselected(TabLayout.Tab tab) {

	    }
	}
	
	 private class ExpenseListFragmentPageAdapter extends FragmentStatePagerAdapter {
		 
		 private SparseArray<ExpenseListingFragment> registeredFragments = new SparseArray<ExpenseListingFragment>();

	        public ExpenseListFragmentPageAdapter(FragmentManager fm) {
	            super(fm);
	        }
	        
	        ExpenseListingFragment getFragmentInstanceAtPosition(int position)
	        {
	        	if(registeredFragments != null && registeredFragments.size() > 0)
	        	  return registeredFragments.get(position);
	        	else 
	        	  return null;
	        }
	        

	        @Override
	        public Fragment getItem(int position) {
	        	
	        	//position++;
	        	
	        int tabID;
	        
	    	switch(position) {

            case 0:
            	 
            	 tabID = EMSConstants.EXPENSE_TAB_SUBMITTED;
            	 
            	 break;
            	
            case 1:
            	
            	tabID = EMSConstants.EXPENSE_TAB_PENDING;
            	break;
            	
            case 2: 
            	
            	 tabID = EMSConstants.EXPENSE_TAB_APPROVED;
            	 break;
            	
            case 3: 
            	
            	tabID = EMSConstants.EXPENSE_TAB_NOTIFICATIONS;
            	break;
            	
            	
             default:
            	tabID = EMSConstants.EXPENSE_TAB_SUBMITTED;
            	break;
        }
	     	
	    	  ExpenseListingFragment expenseListingFragment = ExpenseListingFragment.newInstance(tabID);
	    	 
	    	  if(position == 0)
	    		  
	    		  mExpenseListingFragment = expenseListingFragment;
	    	  
	    	  registeredFragments.put(position, expenseListingFragment);
	        	
	    	  return expenseListingFragment;      	
	           
	        }

	        @Override
	        public int getCount() {
	            
	        	return EMSConstants.EXPENSE_TAB_COUNT;
	        	
	        }
	        
	        

	        @Override
	        public CharSequence getPageTitle(int position) {
	        	
	        	
	        	
	        	switch(position) {

	                case 0:
	                	return getString(R.string.submitted);
	                	
	                case 1:
	                	return getString(R.string.pending);
	                	
	                case 2: 
	                	return getString(R.string.approved);
	                	
	                case 3: 
	                	return getString(R.string.notifications);
	                	
	                default:return getString(R.string.submitted);
	                
	            }
	        }
	    }
	
	
	public void onNewExpenseClick(View view){

		Intent newExpenseIntent = new Intent(this, NewExpenseActivity.class);
		startActivity(newExpenseIntent);

	}

	 public void launchUpdateExpenseClick(EMSExpenseDetail inEMSExpenseDetail){

			Intent newExpenseIntent = new Intent(this, NewExpenseActivity.class);
			newExpenseIntent.putExtra("EMSExpenseDetail", inEMSExpenseDetail);
			startActivity(newExpenseIntent);

		}

    private void onSyncClick() {
        if (mExpenseListingFragment != null)
            mExpenseListingFragment.performExpensesSync();

    }

    private void onSearchExpenseBtnClick() {

        Intent newExpenseIntent = new Intent(this, SearchExpenseListingActivity.class);

        startActivity(newExpenseIntent);
    }


    public void onBackBtnClick(View view)
	 {
		 finish();
	 }
		
}
