package com.samsung.ssc.activitymodule;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork1;
import com.samsung.ssc.io.PostDataToNetwork2;
import com.samsung.ssc.util.Helper;

public class SODetailFragment2 extends Fragment {

	private View _rootView;

	private TextView tvModelcode;
	private TextView tvRequestedate;
	private TextView tvTrackingno;
	private TextView tvRepairstatus;

	private String startDate;
	private String endDate;

	private EditText mEtSoNumber;
	private String cookies;

	private String soNumber;

	private  LinearLayout mLLContainer;

 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (_rootView == null) {
			_rootView = inflater.inflate(R.layout.fragment_so_detail2,
					container, false);

			setUpBasicView();

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		return _rootView;
	}

	private void setUpBasicView() {

		mLLContainer = (LinearLayout) _rootView
				.findViewById(R.id.ll_containerSoDetailWeb);
		
		//Button mbtnLoginSubmitWeb = (Button)_rootView.findViewById(R.id.btnLoginSubmitWeb);
		
		/*mEtSoNumber = (EditText) _rootView
				.findViewById(R.id.et_soNumberSoDetailWeb);*/
		tvModelcode = (TextView) _rootView.findViewById(R.id.tv_modelcode);
		tvRequestedate = (TextView) _rootView
				.findViewById(R.id.tv_requestedate);
		tvTrackingno = (TextView) _rootView.findViewById(R.id.tv_trackingno);
		tvRepairstatus = (TextView) _rootView
				.findViewById(R.id.tv_repairstatus);
		
		
		/*mbtnLoginSubmitWeb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmitButtonClickWeb();
			}
		});*/
		

	//	GetStartAndEndDates();
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

 

	public void onSubmitButtonClickWeb() {

		soNumber = mEtSoNumber.getText().toString().trim();
		// Hide key board
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEtSoNumber.getWindowToken(), 0);
		// 4194987490
		if (soNumber.isEmpty()) {
			Helper.showCustomToast(getActivity().getApplicationContext(),
					R.string.please_type_service_order_number,
					Toast.LENGTH_LONG);
		} else {

			if (Helper.isOnline(getActivity().getApplicationContext())) {
				// callWebService(soNumber);
				// callWebServiceHTML(soNumber);
				//CallWebServiceCookies();

			} else {
				Helper.showAlertDialog(
						getActivity(),
						SSCAlertDialog.WARNING_TYPE,
						getActivity().getString(R.string.error3),
						getActivity().getString(R.string.error2),
						getActivity().getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								clearLastSyncData();
								sdAlertDialog.dismiss();

							}

						
						}, false, null, null);
			}
		}
	}

	private void CallWebServiceCookies() {

		PostDataToNetwork1 pdn = new PostDataToNetwork1(getActivity(),
				getString(R.string.loadingmessage), new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						try {
							if (result != null) {
								cookies = result.toString();
								callWebServiceHTML(soNumber);
								// Map<String, String> cookiesMap =
								// parse(result.toString());
								// Toast.makeText(SODetailActivity.this,
								// "cookies"+cookies, Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getActivity(),
										"Data not aviliable", Toast.LENGTH_LONG)
										.show();
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

	private void callWebServiceHTML(String soNumber) {

		PostDataToNetwork2 pdn = new PostDataToNetwork2(getActivity(),
				getResources().getString(R.string.loadingmessage),
				new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {

						try {
							if (result != null) {
								String[] finalValue = parseResponseOfHTML(result
										.toString());
								if (finalValue != null) {
									showDataFromHtmlArray(finalValue);
								} else {

									Helper.showAlertDialog(
											getActivity(),
											SSCAlertDialog.ERROR_TYPE,
											getActivity().getString(R.string.error7),
											getActivity()
													.getString(R.string.error_contact_admin),
													getActivity().getString(R.string.ok),
											new SSCAlertDialog.OnSDAlertDialogClickListener() {

												@Override
												public void onClick(
														SSCAlertDialog sdAlertDialog) {

													sdAlertDialog.dismiss();

												}
											}, false, null, null);

								
								}
							} else {

								Helper.showCustomToast(getActivity(),
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
	
		private void clearLastSyncData() {
			tvModelcode.setText("");
			tvRequestedate.setText("");
			tvTrackingno.setText("");
			tvRepairstatus.setText("");
		}
	

	protected void showDataFromHtmlArray(String[] finalValue) {

		 

		try {

			if (finalValue[2] != null) {
				
				String modelCode1 = finalValue[2].length() > 0 ? finalValue[2]: "" ;
				String modelCode6;
				try {
					modelCode6 = finalValue[6].length() > 0 ? finalValue[6]: "";
				} catch (Exception e) {
					modelCode6 ="";
					e.printStackTrace();
				}
				tvModelcode.setText(modelCode1 + modelCode6);
			}
			if (finalValue[3].length() > 0) {
				tvRequestedate
						.setText(finalValue[3].length() > 0 ? finalValue[3]
								: "");
			}
			if (finalValue[1] != null) {
				tvTrackingno.setText(finalValue[1].length() > 0 ? finalValue[1]
						: "");
			}
			if (finalValue[4] != null) {
				tvRepairstatus
						.setText(finalValue[4].length() > 0 ? finalValue[4]
								: "");
			}

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
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

	public void setGCICResponce(Object result) {
		
		
		if (result != null) {
			String[] finalValue = parseResponseOfHTML(result.toString());
			if (finalValue != null) {
				showDataFromHtmlArray(finalValue);
			} else {
				clearLastSyncData();
				alertBoxGcicData();
			

			}

		} else {
			clearLastSyncData();
			alertBoxGcicData();
		}
		
	}

	private void alertBoxGcicData() {
		Helper.showAlertDialog(getActivity(),
				SSCAlertDialog.WARNING_TYPE,
				getActivity().getString(R.string.error3), getActivity()
						.getString(R.string.no_data_availble_gcic),
				getActivity().getString(R.string.ok),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {

						sdAlertDialog.dismiss();

					}
				}, false, null, null);
	}

}