package com.samsung.ssc;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.DealerHistoryAdapter;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.constants.WebConfig.WebParams;
import com.samsung.ssc.dto.DealerHistoryDataDto;
import com.samsung.ssc.dto.ResponseTypeDto;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class DealerHistory extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dealer_history);
		if (isOnline()) {
			getHistoryofDealers();
		} else {
			Helper.showAlertDialog(DealerHistory.this,
					SSCAlertDialog.ERROR_TYPE,
					getString(R.string.error1), getString(R.string.error2),
					getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();

						}
					}, false, null, null);

		}

	}

	public void onOKButtonClicked(View view) {
		finish();

	}

	private void getHistoryofDealers() {

		PostDataToNetwork pdn = new PostDataToNetwork(DealerHistory.this,
				getResources().getString(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						if (result != null) {

							try {
								Gson gson = new Gson();
								Type type = new TypeToken<ResponseTypeDto<DealerHistoryDataDto>>() {}.getType();
								ResponseTypeDto<DealerHistoryDataDto> data = gson.fromJson(result.toString(), type);

								if (data.isSuccess()) {
									ArrayList<DealerHistoryDataDto> historydetails = data
											.getResult();

									if (historydetails != null
											&& historydetails.size() > 0) {
										ListView listview_history = (ListView) findViewById(R.id.listview_history);
										DealerHistoryAdapter dealerhistorylistadapter = new DealerHistoryAdapter(
												DealerHistory.this, historydetails);
										listview_history
												.setAdapter(dealerhistorylistadapter);

									} else {
										Helper.showErrorAlertDialog(
												DealerHistory.this,
												getResources().getString(
														R.string.error3),
												getResources().getString(
														R.string.datanotavailable),
												new OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();

													}
												});

									}

								} else {

									Helper.showErrorAlertDialog(
											DealerHistory.this,
											getString(R.string.error3),
											getString(R.string.please_contact_admin));
								}
							} catch (JsonSyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							Toast.makeText(
									DealerHistory.this,
									getResources().getString(
											R.string.networkserverbusy),
									Toast.LENGTH_LONG).show();
						}
					}
				});

		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebParams.EMPLCODE, Helper.getStringValuefromPrefs(
					this, SharedPreferencesKey.PREF_EMPID));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		pdn.setConfig(getString(R.string.url),
				WebConfig.WebMethod.GET_MDMDEALER_HISTORY);

		pdn.execute(jsonobj);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
