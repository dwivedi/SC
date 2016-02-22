package com.samsung.ssc.activitymodule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork1;
import com.samsung.ssc.io.PostDataToNetwork2;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyTabFactory;

public class SODetailActivity extends BaseActivity implements
OnTabChangeListener, OnPageChangeListener {
	
	private TabHost mTabHost;
	private MyPageAdapter pageAdapter;
	private ViewPager mViewPager;
	private EditText mEtSoNumber;
	private Button mSubmitButton;
	private String cookies;
	private String endDate;
	private String startDate;
	private SODetailFragment1 rlsFragment;
	private SODetailFragment2 gcicFragment;

	@Override
	public void init() {

		super.init();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_so_detail1);
		mViewPager = (ViewPager)findViewById(R.id.viewpager_sodetail);
		setUpView();
		GetStartAndEndDates();
		initialiseTabHost();
		setUpViewPager();
		
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			onSubmitButtonClick();
			
		}
	});
 }



	public void onSubmitButtonClick() {

		String soNumber = mEtSoNumber.getText().toString().trim();

		// Hide key board
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEtSoNumber.getWindowToken(), 0);
		// 4194987490
		if (soNumber.isEmpty()) {
			Helper.showCustomToast(getApplicationContext(),
					R.string.please_type_service_order_number,
					Toast.LENGTH_LONG);
		} else {

			if (Helper.isOnline(getApplicationContext())) {
				callWebServiceRLS(soNumber);
				CallWebServiceGCICCookies(soNumber);
			} else {
				Helper.showAlertDialog(
						SODetailActivity.this,
						SSCAlertDialog.WARNING_TYPE,
						getString(R.string.error3),
						getString(R.string.error2),
						getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {

								sdAlertDialog.dismiss();

							}
						}, false, null, null);
			}
		}

	}

	private void CallWebServiceGCICCookies(final String soNumber) {

		PostDataToNetwork1 pdn = new PostDataToNetwork1(SODetailActivity.this,
				getString(R.string.loadingmessage), new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						try {
							if (result != null) {
								cookies = result.toString();
								callWebServiceHTMLGCIC(soNumber);
								// Map<String, String> cookiesMap =
								// parse(result.toString());
								// Toast.makeText(SODetailActivity.this,
								// "cookies"+cookies, Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(SODetailActivity.this,
										"Cookies not aviliable",
										Toast.LENGTH_LONG).show();
							}

						} catch (Exception e) {
							Helper.printStackTrace(e);
						}

					}
				});
		pdn.setProcessVisible(false);
		pdn.setConfig(getString(R.string.url_gcic_search_cookies));
		pdn.execute();
	}

	protected void callWebServiceHTMLGCIC(String soNumber) {


		PostDataToNetwork2 pdn = new PostDataToNetwork2(SODetailActivity.this,
				getResources().getString(R.string.loadingmessage),
				new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {

						try {
					
							if (result != null) {

								String name = makeFragmentName(
										mViewPager.getId(), 1);
								Fragment gcicFragmentViewPager = getSupportFragmentManager()
										.findFragmentByTag(name);
								((SODetailFragment2) gcicFragmentViewPager).setGCICResponce(result);

							} else {

								Helper.showCustomToast(SODetailActivity.this,
										R.string.networkserverbusy,
										Toast.LENGTH_LONG);
							}

						} catch (Exception e) {
							Helper.printStackTrace(e);
						}

					}
				});

		JSONObject jobj = null;
		try {
			jobj = new JSONObject();
			jobj.put("repairnum", soNumber);
			jobj.put("sdate", startDate);
			jobj.put("edate", endDate);

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		pdn.setCookies(cookies);
		pdn.setConfig(getString(R.string.url_gcic_search_service));
		pdn.execute(jobj);
	
	}

	protected String[] parseResponseOfHTML(String result) {

		String[] finalResult = null;

		try {
			int startIndex = result.indexOf("listdata[0] = new Array");
			// int endIndex = result.indexOf(startIndex,
			// startIndex+result.indexOf("\"\");"));

			if (startIndex != -1) {

				String subValue = result.substring(startIndex);

				StringBuilder sb = new StringBuilder();
				char[] cs = subValue.toCharArray();
				String key = null, value = null;

				for (int i = 0; i < cs.length; i++) {

					char c = cs[i];
					if (c == '(') {
						sb.setLength(0);
					} else if (c == ')') {
						String subArray = sb.toString();
						sb.setLength(0);
						finalResult = subArray.split(",");
						break;
					} else {
						sb.append(c);
					}
				}

			} else {

			}

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

		return finalResult;
	}

	private void callWebServiceRLS(String sONumber) {
	 
				JSONObject jobj = new JSONObject();

				try {
					jobj.put(WebConfig.WebParams.USER_ID, Helper
							.getStringValuefromPrefs(SODetailActivity.this,
									SharedPreferencesKey.PREF_USERID));
					jobj.put(WebConfig.WebParams.KEY_SO_NUMBER, sONumber);

				} catch (Exception e) {
					Helper.printStackTrace(e);
				}
				
				VolleyPostDataToNetwork pdn=new VolleyPostDataToNetwork(SODetailActivity.this,getString(R.string.loadingmessage), new VolleyGetDataCallBack() {
					
					@Override
					public void processResponse(Object result) {
						
						if (result != null) {

							// parseResponse(result);
							String name = makeFragmentName(mViewPager.getId(),0);
							Fragment rlsFragmentViewPager = getSupportFragmentManager()
									.findFragmentByTag(name);
							((SODetailFragment1) rlsFragmentViewPager)
									.setRLSResponce(result);

						} else {

							Helper.showCustomToast(SODetailActivity.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}
					}
					
					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						
					}
				});
				
				pdn.setRequestData(jobj);
				pdn.setConfig(getString(R.string.url),
						WebConfig.WebMethod.GET_RLSSO_DETAILS);
				pdn.callWebService();
				
				
			}


	private static String makeFragmentName(int viewId, int position) {
	    return "android:switcher:" + viewId + ":" + position;
	} 



	private void setUpView() {
		
		mEtSoNumber = (EditText)findViewById(R.id.et_soNumberSoDetail);
		mSubmitButton = (Button)findViewById(R.id.btnLoginSubmit);
	
	}

	private void setUpViewPager() {
		// Fragments and ViewPager Initialization
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pageAdapter);
		mViewPager.setOnPageChangeListener(SODetailActivity.this);
	}
	
	private List<Fragment> getFragments() {
		
		List<Fragment> fList = new ArrayList<Fragment>();

		rlsFragment = new SODetailFragment1();
		/*Bundle bundle = new Bundle();
		bundle.putString("RLSResponse", null);
		rlsFragment.setArguments(bundle);;*/
		fList.add(rlsFragment);

		gcicFragment = new SODetailFragment2();
		//gcicFragment.setArguments(null);
		fList.add(gcicFragment);

		return fList;
	}

	private void initialiseTabHost() {

		try {

			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();

			addTab(SODetailActivity.this,
					this.mTabHost,this.mTabHost.newTabSpec(getResources().getString(R.string.so_detail)).setIndicator(
									getResources().getString(R.string.so_detail)));
			
			addTab(SODetailActivity.this,
					this.mTabHost,this.mTabHost.newTabSpec(getResources().getString(R.string.so_search)).setIndicator(
									getResources().getString(R.string.so_search)));

			mTabHost.setOnTabChangedListener(this);

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	// Method to add a TabHost
	private void addTab(SODetailActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;
		private Object response;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = this.fragments.get(position);
			return fragment;
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
		
	public void	upDateText(Object response){
		this.response=response;
		notifyDataSetChanged();
		
			
		}
	
	} 
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		int pos = this.mViewPager.getCurrentItem();
		this.mTabHost.setCurrentTab(pos);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabChanged(String tabId) {
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
	}
	
	private void GetStartAndEndDates() {

		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			endDate = df.format(c.getTime());
			c.add(Calendar.DATE, -30);
			startDate = df.format(c.getTime());

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

	}

}
