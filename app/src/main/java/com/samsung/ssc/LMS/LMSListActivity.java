package com.samsung.ssc.LMS;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.dto.LMSLeaveDataModal;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LMSListActivity extends BaseActivity implements OnLeaveListFragmentInteractionListener {

    protected static final String KEY_LEAVE_DATA = "KEY_LEAVE_DATA";
    private static final int REQUEST_CREATE_LMS = 1;
    private static final int REQUEST_ACTION_LMS = 2;
    private ViewPager mViewPager;
    private MyPageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lms_list);

        setUpView();
    }

    private void setUpView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LMS");
         setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewpagerLMSList);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayoutLMSList);
        try {

            mTabLayout.addTab(mTabLayout.newTab().setText("Leave Request").setContentDescription("Leave Request"));
            mTabLayout.addTab(mTabLayout.newTab().setText("Leave Pending").setContentDescription("Leave Pending"));
            mTabLayout.addTab(mTabLayout.newTab().setText("Notification").setContentDescription("Notification"));
            mTabLayout.addTab(mTabLayout.newTab().setText("Acted").setContentDescription("Acted"));

            mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int pos = tab.getPosition();
                    mViewPager.setCurrentItem(pos);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                    getCurrentFragment().notifyDataSetChange();

                }

                /**
                 * @param tab
                 */
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            List<Fragment> fragments = getFragments();
            mPageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
            mViewPager.setAdapter(mPageAdapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


        FloatingActionButton fabCreateNewLeave = (FloatingActionButton) this.findViewById(R.id.fabCreateNewLMS);
        fabCreateNewLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), LMSCreateLeaveActivity.class);
                startActivityForResult(i, REQUEST_CREATE_LMS);
            }
        });

         getLeavesData();

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.KEY_PAGE_ID, LMSConstants.TAB_SUBMITTED);
        LMSLeaveListFragment f1 = new LMSLeaveListFragment();
        f1.setArguments(bundle);
        fList.add(f1);

        bundle = new Bundle();
        bundle.putInt(IntentKey.KEY_PAGE_ID, LMSConstants.TAB_PENDING);
        f1 = new LMSLeaveListFragment();
        f1.setArguments(bundle);
        fList.add(f1);

        bundle = new Bundle();
        bundle.putInt(IntentKey.KEY_PAGE_ID, LMSConstants.TAB_NOTIFICATION);
        f1 = new LMSLeaveListFragment();
        f1.setArguments(bundle);
        fList.add(f1);


        bundle = new Bundle();
        bundle.putInt(IntentKey.KEY_PAGE_ID, LMSConstants.TAB_ACTED);
        f1 = new LMSLeaveListFragment();
        f1.setArguments(bundle);
        fList.add(f1);

        return fList;
    }

    @Override
    public void onFragmentInteraction(int tabID,LMSLeaveDataModal lMSLeaveDataModal) {


        Intent intent = new Intent();
        if (tabID==LMSConstants.TAB_PENDING) {
            intent.setClass(this, LMSLeaveActionActivity.class);
            intent.putExtra(KEY_LEAVE_DATA, lMSLeaveDataModal);
            startActivityForResult(intent,REQUEST_ACTION_LMS);
        } else {
            intent.setClass(this, LMSLeaveDetailActivity.class);
            intent.putExtra(KEY_LEAVE_DATA, lMSLeaveDataModal);
            startActivity(intent);
        }



    }

    private void getLeaveConfigurationData() {

        boolean isLeaveData = DatabaseHelper.getConnection(this).isLeaveTypeData();
        if (!isLeaveData) {
            PostDataToNetwork network = new PostDataToNetwork(LMSListActivity.this, "Getting Configuration Data", new GetDataCallBack() {


                @Override
                public void processResponse(Object result) {

                    if (result != null) {
                        ResponseDto response = new FetchingdataParser(LMSListActivity.this).getResponseResult(result.toString());

                        if (response.isSuccess()) {
                            try {
                                JSONArray rootJSONArray = new JSONArray(response.getResult());

                                ContentValues[] valuesLMSData = DatabaseUtilMethods.getContentValueLMSLeaveConfiguration(rootJSONArray);


                                getContentResolver().bulkInsert(ProviderContract.URI_LMS_LEAVE_CONFIGURATION, valuesLMSData);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Helper.showAlertDialog(LMSListActivity.this, SSCAlertDialog.ERROR_TYPE, "Error", response.getMessage(), "OK", new SSCAlertDialog.OnSDAlertDialogClickListener() {
                                @Override
                                public void onClick(SSCAlertDialog sdAlertDialog) {
                                    sdAlertDialog.dismiss();
                                }
                            }, false, null, null);
                        }


                    }

                }
            });
            network.setConfig(getString(R.string.url), WebConfig.WebMethod.GET_LEAVE_TYPE_COFIGURATION);
            try {
                JSONObject request = new JSONObject();

                request.put(WebConfig.WebParams.USER_ID, Integer.parseInt(Helper.getStringValuefromPrefs(getApplicationContext(), SharedPreferencesKey.PREF_USERID)));
                request.put(WebConfig.WebParams.ROLE_ID, Helper.getIntValueFromPrefs(getApplicationContext(), SharedPreferencesKey.PREF_ROLEID));


                network.execute(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getLeaveLeaveTypeData() {

        PostDataToNetwork network = new PostDataToNetwork(LMSListActivity.this, "Getting LMS Type Data", new GetDataCallBack() {

            @Override
            public void processResponse(Object result) {

                if (result != null) {
                    ResponseDto response = new FetchingdataParser(LMSListActivity.this).getResponseResult(result.toString());

                    if (response.isSuccess()) {
                        try {
                            JSONArray rootJSONArray = new JSONArray(response.getResult());
                            ContentValues[] valuesLMSData = DatabaseUtilMethods.getContentValueLMSLeaveType(rootJSONArray);
                            getContentResolver().bulkInsert(ProviderContract.URI_LMS_LEAVE_TYPE, valuesLMSData);

                            getLeaveConfigurationData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Helper.showAlertDialog(LMSListActivity.this, SSCAlertDialog.ERROR_TYPE, "Error", response.getMessage(), "OK", new SSCAlertDialog.OnSDAlertDialogClickListener() {
                            @Override
                            public void onClick(SSCAlertDialog sdAlertDialog) {
                                sdAlertDialog.dismiss();
                            }
                        }, false, null, null);
                    }
                }
            }
        });
        network.setConfig(getString(R.string.url), WebConfig.WebMethod.GET_LEAVE_TYPE_MASTER);
        try {
            JSONObject request = new JSONObject();

            request.put(WebConfig.WebParams.USER_ID, Integer.parseInt(Helper.getStringValuefromPrefs(getApplicationContext(),SharedPreferencesKey.PREF_USERID)));
            request.put(WebConfig.WebParams.ROLE_ID, Helper.getIntValueFromPrefs(getApplicationContext(), SharedPreferencesKey.PREF_ROLEID));


            network.execute(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentRefresh() {

        getLeavesData();

    }

    LMSLeaveListFragment getCurrentFragment(){
        LMSLeaveListFragment mCurrentFragment = (LMSLeaveListFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
        return mCurrentFragment;
     }

    class MyPageAdapter extends FragmentPagerAdapter {private List<Fragment> fragments;

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

    private void getLeavesData() {

        try {
            getLeaveLeaveTypeData();

            String lastSyncDate = DatabaseHelper.getConnection(this).getLastSyncDate();
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -4);
            String dateLast = format.format(cal.getTime());
            String currentDate = format.format(Calendar.getInstance().getTime());


            JSONObject lmsLeaveRequest = new JSONObject();
            lmsLeaveRequest.put("StartDate", dateLast);
            lmsLeaveRequest.put("EndDate", currentDate);
            lmsLeaveRequest.put("LastSyncDateTime", lastSyncDate != null ? lastSyncDate : JSONObject.NULL);  // ToDO Will be cahnge

            JSONObject request = new JSONObject();
            request.put(WebConfig.WebParams.USER_ID, Integer.parseInt(Helper.getStringValuefromPrefs(LMSListActivity.this, SharedPreferencesKey.PREF_USERID)));
            request.put(WebConfig.WebParams.ROLE_ID, Helper.getIntValueFromPrefs(LMSListActivity.this, SharedPreferencesKey.PREF_ROLEID));

            request.put("LMSLeaveRequest", lmsLeaveRequest);


            PostDataToNetwork postDataToNetwork = new PostDataToNetwork(this, "", new GetDataCallBack() {
                @Override
                public void processResponse(Object result) {

                    try {

                        ResponseDto response = new FetchingdataParser(LMSListActivity.this).getResponseResult(result.toString());
                        if (response!= null){
                            processResponses(response);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            postDataToNetwork.setConfig(getString(R.string.url), WebConfig.WebMethod.GET_LEAVE_DATA);
            postDataToNetwork.execute(request);



        }catch (Exception e){
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CREATE_LMS:
                    getLeavesData();
                    break;
                case REQUEST_ACTION_LMS:
                    getLeavesData();
                    break;
            }

        }
    }

    private void processResponses(ResponseDto response) {
        if (response.isSuccess()) {
            try {
                JSONArray rootJSONArray = new JSONArray(response.getResult());

                List<ContentValues[]> list = DatabaseUtilMethods.getContentValueLMSLeaveResponse(rootJSONArray);

                ContentValues[] valuesLMSData = list.get(0);
                ContentValues[] valuesLMSDateData = list.get(1);
                ContentValues[] valuesLMSStatusLog = list.get(2);


                getContentResolver().bulkInsert(ProviderContract.URI_LMS_LIST, valuesLMSData);
                getContentResolver().bulkInsert(ProviderContract.URI_LMS_DATE_LIST, valuesLMSDateData);
                getContentResolver().bulkInsert(ProviderContract.URI_LMS_STATUS_LOG, valuesLMSStatusLog);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{
            Helper.showAlertDialog(LMSListActivity.this, SSCAlertDialog.ERROR_TYPE, "Error", response.getMessage(), "OK", new SSCAlertDialog.OnSDAlertDialogClickListener() {
                @Override
                public void onClick(SSCAlertDialog sdAlertDialog) {
                    sdAlertDialog.dismiss();
                }
            }, false, null, null);
        }
        getCurrentFragment();
        if(getCurrentFragment()!=null) getCurrentFragment().getLeaveData();
    }

}
