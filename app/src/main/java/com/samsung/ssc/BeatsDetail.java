package com.samsung.ssc;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.CustomUI.ExtendedListView;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.BeatDetailsAdapter;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.dto.UserBeatDetailDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class BeatsDetail extends BaseActivity implements View.OnClickListener {

	private ExtendedListView beatListView;
	private ArrayList<UserBeatDetailDto> beatList;
	final int STATUS_APPROVE = 1;
	final int STATUS_REJECT = 2;
	private String screenType;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.beat_approval1);
		Intent intent = getIntent();
		screenType = intent.getStringExtra("beattype");
		
		

		Button approve = (Button) findViewById(R.id.approveBtn);
		Button reject = (Button) findViewById(R.id.rejectBtn);
		Button cancel = (Button)findViewById(R.id.cancelBtn);

		if (!Helper.isNullOrEmpty(screenType)
				&& screenType.equals("beatsummary")) {
			
			((TextView)this.findViewById(R.id.tv_title_sdActionBar)).setText(R.string.beatsummary);
			
			//setCentretext(getString(R.string.beatsummary));
			 findViewById(R.id.butonlayout).setVisibility(View.GONE);
			 
			
		} else {
			//setCentretext(getString(R.string.beatapproval));
			((TextView)this.findViewById(R.id.tv_title_sdActionBar)).setText(R.string.beatapproval);
		}

		approve.setOnClickListener(this);
		reject.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		getUserBeatDetailList();
	}
	
	@Override
	public void onClick(View v) {

		int key = v.getId();

		switch (key) {

		case R.id.approveBtn:
			updatePendingCoverage(STATUS_APPROVE);
			break;

		case R.id.rejectBtn:
			updatePendingCoverage(STATUS_REJECT);
			break;

		case R.id.cancelBtn:
			finish();
			break;
		}

	}

	private void getUserBeatDetailList() {

		PostDataToNetwork pdn = new PostDataToNetwork(BeatsDetail.this,
				getString(R.string.loadingmessage), new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {

							Helper.printLog("Tag", result.toString());

							beatList = new FetchingdataParser(BeatsDetail.this)
									.getUserBeatDetailList(result.toString());

							if (beatList != null && beatList.size() > 0
									&& beatList.get(0).isSuccess()) {

							populateListView();

							} else {

							 
								
								Helper.showAlertDialog(
										BeatsDetail.this,
										SSCAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										getString(R.string.datanotavailable),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {
												sdAlertDialog.dismiss();
												finish();

											}
										}, false, null, null);
							}

						}

						else {
							Helper.showCustomToast(BeatsDetail.this, R.string.networkserverbusy, Toast.LENGTH_LONG);
						}
					}
				});
		JSONObject jsonobj = new JSONObject();
		

		if (!Helper.isNullOrEmpty(screenType)
				&& screenType.equals("beatsummary")) {

			try {
				jsonobj.put(WebConfig.WebParams.USER_ID, Helper.getStringValuefromPrefs(BeatsDetail.this, SharedPreferencesKey.PREF_USERID));
			} catch (JSONException e) {
				Helper.printStackTrace(e);
			}
			pdn.setConfig(getString(R.string.url), "GetApprovedUserBeatDetails");

		} else {

			try {
				Intent intent = getIntent();
				String userID_emp = intent.getStringExtra("emp_user_code");

				jsonobj.put(WebConfig.WebParams.USER_ID, userID_emp);

			} catch (JSONException e) {
				Helper.printStackTrace(e);
			}
			pdn.setConfig(getString(R.string.url), "GetUserBeatDetails");

		}

		pdn.execute(jsonobj);

	}

	protected void populateListView() {
		final ArrayList<UserBeatDetailDto> storesData_new = new ArrayList<UserBeatDetailDto>();

		for (int i = 0; i < beatList.size(); i++) {
			
			UserBeatDetailDto userObj = new UserBeatDetailDto();
			userObj.setDateRange(beatList.get(i).getCoverageDate());
			
			String storeName = "1. ";
			int count = 1;
			for (int j = 0; j < beatList.get(i).getStoreList().size(); j++) {

				{

					if (count == 1) {
						storeName = storeName + beatList.get(i).getStoreList().get(j).toString().trim() + "\n"
								+ (++count) + ". ";
					} else {
						storeName = storeName + beatList.get(i).getStoreList().get(j).toString().trim() + "\n"
								+ count + ". ";
					}

					count++;

				}
			}
			
			if (count > 9) {
				userObj.setStoreName(storeName.substring(0, storeName.length() - 4));
			} else {
				userObj.setStoreName(storeName.substring(0, storeName.length() - 3));
			}

			storesData_new.add(userObj);
			
		}

		// Add a leave related data in last row
		UserBeatDetailDto leaveData = new UserBeatDetailDto();
		leaveData.setDateRange("Beat Plan Summary:");
		
		leaveData.setStoreName("Total Assigned Outlet: " + beatList.get(0).getTotalAssignedOutlet() + "\n" +  "Total Working Days: " + beatList.get(0).getTotalWorkingDays() + "\n"	+ "Total Outlet Planned: "	+ beatList.get(0).getTotalOutletPlanned() + "\n"
				+ "Total Offs: " + beatList.get(0).getTotalOff() + "\n" + "Off Days: " + beatList.get(0).getLeaveDetail());
		
		storesData_new.add(leaveData);
		beatListView = (ExtendedListView)findViewById(R.id.listView);
		beatListView.setOnPositionChangedListener(new ExtendedListView.OnPositionChangedListener() {
			
			@Override
			public void onPositionChanged(ExtendedListView listView, int position,
					View scrollBarPanel) {
				try {
					((TextView) scrollBarPanel).setText(storesData_new.get(position).getDateRange());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		BeatDetailsAdapter adapter = new BeatDetailsAdapter(BeatsDetail.this,
				storesData_new, false);

		beatListView.setAdapter(adapter);

		adapter.notifyDataSetChanged();
		
	}



	private void updatePendingCoverage(int status) {

		PostDataToNetwork pdn = new PostDataToNetwork(BeatsDetail.this,
				getString(R.string.loadingmessage), new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {

							ResponseDto obj = new FetchingdataParser(
									BeatsDetail.this).getResponseResult(result
									.toString());
							if (obj.isSuccess()) {

								 

								Helper.showAlertDialog(
										BeatsDetail.this,
										SSCAlertDialog.SUCCESS_TYPE,
										getString(R.string.sucess),
										obj.getMessage(),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {
												setResult(100);
												sdAlertDialog.dismiss();
												finish();

											}
										}, false, null, null);

							} else {
							 

								Helper.showAlertDialog(BeatsDetail.this, SSCAlertDialog.ERROR_TYPE, getString(R.string.error3), obj.getMessage(), getString(R.string.ok), new SSCAlertDialog.OnSDAlertDialogClickListener() {
									
									@Override
									public void onClick(SSCAlertDialog sdAlertDialog) {
										sdAlertDialog.dismiss();
 									}
								}, false, null, null);

							}
						}

						else {
							Helper.showCustomToast(BeatsDetail.this, R.string.networkserverbusy, Toast.LENGTH_LONG);
						}
					}
				});

		ArrayList<Integer> list = new ArrayList<Integer>();
		JSONObject mainObj = new JSONObject();

		try {

			Intent intent = getIntent();
			String userID_emp = intent.getStringExtra("emp_user_code");
			
			list.add(Integer.valueOf(userID_emp));
			mainObj.put(WebConfig.WebParams.USER_ID,Long.valueOf(Helper.getStringValuefromPrefs(BeatsDetail.this, SharedPreferencesKey.PREF_USERID)) );
			mainObj.put("userIDList", new JSONArray(list));
			mainObj.put("status", status);
			

		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}

		pdn.setConfig(getString(R.string.url), "UpdatePendingCoverage");
		Helper.printLog("input_UpdatePendingCoverage:", mainObj.toString());
		pdn.execute(mainObj);

	}
}
