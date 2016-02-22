package com.samsung.ssc;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.CustomUI.ExtendedListView;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.BeatApprovalAdapter;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.constants.WebConfig.WebMethod;
import com.samsung.ssc.dto.EmployeeListDto;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class BeatApproval extends BaseActivity implements View.OnClickListener {

	private ExtendedListView beatList;
	private HashMap<Integer, EmployeeListDto> employeeList;
	private final int STATUS_APPROVE = 1;
	private final int STATUS_REJECT = 2;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.beat_approval1);
		((TextView) this.findViewById(R.id.tv_title_sdActionBar))
				.setText(R.string.beatapproval);

		// setCentretext(getString(R.string.beatapproval));
		Button approve = (Button) findViewById(R.id.approveBtn);
		Button reject = (Button) findViewById(R.id.rejectBtn);
		Button cancel = (Button) findViewById(R.id.cancelBtn);

		TableRow tb = (TableRow) findViewById(R.id.butonlayout);

		approve.setOnClickListener(this);
		reject.setOnClickListener(this);
		cancel.setOnClickListener(this);

		tb.setVisibility(View.INVISIBLE);
		approve.setVisibility(View.INVISIBLE);
		reject.setVisibility(View.INVISIBLE);
		cancel.setVisibility(View.INVISIBLE);

		beatList = (ExtendedListView) findViewById(R.id.listView);
		getEmployeeList();
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
		default:
			break;
		}

	}

	private void getEmployeeList() {

		/*
		 * PostDataToNetwork pdn = new PostDataToNetwork(BeatApproval.this,
		 * getString(R.string.loadingmessage), new GetDataCallBack() {
		 * 
		 * @Override public void processResponse(Object result) { if (result !=
		 * null) {
		 * 
		 * employeeList = new FetchingdataParser(
		 * BeatApproval.this).getEmployeeList(result .toString());
		 * 
		 * if (employeeList != null && employeeList.size() > 0 &&
		 * employeeList.get(0).isSuccess()) {
		 * 
		 * populateListView();
		 * 
		 * } else {
		 * 
		 * 
		 * Helper.showAlertDialog(BeatApproval.this,
		 * SmartDostAlertDialog.ERROR_TYPE, getString(R.string.error3),
		 * getString(R.string.datanotavailable), getString(R.string.ok), new
		 * SmartDostAlertDialog.OnSDAlertDialogClickListener() {
		 * 
		 * @Override public void onClick(SmartDostAlertDialog sdAlertDialog) {
		 * sdAlertDialog.dismiss(); finish();
		 * 
		 * } }, false, null, null); }
		 * 
		 * }
		 * 
		 * else {
		 * 
		 * 
		 * Helper.showCustomToast(BeatApproval.this, R.string.networkserverbusy,
		 * Toast.LENGTH_LONG);
		 * 
		 * } } });
		 */
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(BeatApproval.this,
							SharedPreferencesKey.PREF_USERID));// 21

		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		/*
		 * pdn.setConfig(getString(R.string.url), "GetCoverageUsers");
		 * Helper.printLog("Request_url", jsonobj.toString());
		 * pdn.execute(jsonobj);
		 */

		VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
				BeatApproval.this, getString(R.string.loadingmessage),
				new VolleyGetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						if (result != null) {

							employeeList = new FetchingdataParser(
									BeatApproval.this).getEmployeeList(result
									.toString());

							if (employeeList != null && employeeList.size() > 0
									&& employeeList.get(0).isSuccess()) {

								populateListView();

							} else {

								Helper.showAlertDialog(
										BeatApproval.this,
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

							Helper.showCustomToast(BeatApproval.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}

					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});

		pdn.setRequestData(jsonobj);
		pdn.setConfig(getString(R.string.url), WebMethod.GET_COVERAGE_USER);
		pdn.callWebService();
	}

	protected void populateListView() {

		BeatApprovalAdapter adapter = new BeatApprovalAdapter(
				BeatApproval.this, employeeList);
		beatList.setAdapter(adapter);
		beatList.setOnPositionChangedListener(new ExtendedListView.OnPositionChangedListener() {

			@Override
			public void onPositionChanged(ExtendedListView listView,
					int position, View scrollBarPanel) {
				((TextView) scrollBarPanel).setText(employeeList.get(position)
						.getFirstName());
			}
		});
		adapter.notifyDataSetChanged();
		beatList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BeatApproval.this, BeatsDetail.class);
				intent.putExtra("emp_user_code", employeeList.get(position)
						.getUserId());
				startActivityForResult(intent, 999);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 999) {
			if (resultCode == 100) {
				getEmployeeList();
			}
		}
	}

	private void updatePendingCoverage(int status) {
/*
		PostDataToNetwork pdn = new PostDataToNetwork(BeatApproval.this,
				getString(R.string.loadingmessage), new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {

							ResponseDto obj = new FetchingdataParser(
									BeatApproval.this).getResponseResult(result
									.toString());
							if (obj.isSuccess()) {

								Helper.showAlertDialog(
										BeatApproval.this,
										SmartDostAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										obj.getMessage(),
										getString(R.string.ok),
										new SmartDostAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SmartDostAlertDialog sdAlertDialog) {
												sdAlertDialog.dismiss();
												finish();

											}
										}, false, null, null);

							} else {

								Helper.showAlertDialog(
										BeatApproval.this,
										SmartDostAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										obj.getMessage(),
										getString(R.string.ok),
										new SmartDostAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SmartDostAlertDialog sdAlertDialog) {
												sdAlertDialog.dismiss();
											}
										}, false, null, null);

							}
						}

						else {
							Helper.showCustomToast(BeatApproval.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}
					}
				});*/

		ArrayList<Integer> list = new ArrayList<Integer>();
		JSONObject mainObj = new JSONObject();

		try {

			if (status == STATUS_APPROVE) {

				for (int i = 0; i < employeeList.size(); i++) {

					if (employeeList.get(i).isStatus()) {

						if (employeeList.get(i).getUserId() != null) {
							list.add(Integer.valueOf(employeeList.get(i)
									.getUserId()));
							mainObj.put(WebConfig.WebParams.USER_ID, Long
									.valueOf(Helper.getStringValuefromPrefs(
											BeatApproval.this,
											SharedPreferencesKey.PREF_USERID)));
							mainObj.put("userIDList", new JSONArray(list));
							mainObj.put("status", STATUS_APPROVE);
						}
					}
				}

			} else if (status == STATUS_REJECT) {

				for (int i = 0; i < employeeList.size(); i++) {

					if (employeeList.get(i).isStatus()) {

						if (employeeList.get(i).getUserId() != null) {
							list.add(Integer.valueOf(employeeList.get(i)
									.getUserId()));
							mainObj.put(WebConfig.WebParams.USER_ID, Long
									.valueOf(Helper.getStringValuefromPrefs(
											BeatApproval.this,
											SharedPreferencesKey.PREF_USERID)));
							mainObj.put("userIDList", new JSONArray(list));
							mainObj.put("status", STATUS_REJECT);
						}
					}
				}

			}

		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
/*
		pdn.setConfig(getString(R.string.url),
			WebMethod.UPDATE_PENDING_COVERAGE);
		Helper.printLog("input_UpdatePendingCoverage:", mainObj.toString());

		if (list.size() > 0) {
			pdn.execute(mainObj);
		} else {
			Helper.showCustomToast(BeatApproval.this,
					R.string.please_select_user, Toast.LENGTH_SHORT);

		}*/

		
		VolleyPostDataToNetwork pdn=new VolleyPostDataToNetwork(BeatApproval.this, getString(R.string.loadingmessage), new VolleyGetDataCallBack() {
			
			@Override
			public void processResponse(Object result) {
				if (result != null) {

					ResponseDto obj = new FetchingdataParser(
							BeatApproval.this).getResponseResult(result
							.toString());
					if (obj.isSuccess()) {

						Helper.showAlertDialog(
								BeatApproval.this,
								SSCAlertDialog.ERROR_TYPE,
								getString(R.string.error3),
								obj.getMessage(),
								getString(R.string.ok),
								new SSCAlertDialog.OnSDAlertDialogClickListener() {

									@Override
									public void onClick(
											SSCAlertDialog sdAlertDialog) {
										sdAlertDialog.dismiss();
										finish();

									}
								}, false, null, null);

					} else {

						Helper.showAlertDialog(
								BeatApproval.this,
								SSCAlertDialog.ERROR_TYPE,
								getString(R.string.error3),
								obj.getMessage(),
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

				else {
					Helper.showCustomToast(BeatApproval.this,
							R.string.networkserverbusy,
							Toast.LENGTH_LONG);

				}
				
			}
			
			@Override
			public void onError(VolleyError error) {
				
				
			}
		});
		
		pdn.setConfig(getString(R.string.url),
				WebMethod.UPDATE_PENDING_COVERAGE);
			Helper.printLog("input_UpdatePendingCoverage:", mainObj.toString());

			if (list.size() > 0) {
				pdn.setRequestData(mainObj);
				pdn.callWebService();
			} else {
				Helper.showCustomToast(BeatApproval.this,
						R.string.please_select_user, Toast.LENGTH_SHORT);

			}
		
	}

}
