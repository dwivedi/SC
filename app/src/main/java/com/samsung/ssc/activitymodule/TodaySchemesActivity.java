package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.TodaySchemeAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.TodaySchemesModel;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class TodaySchemesActivity extends BaseActivity {

	ListView mLvSchemes;
	TextView mTvNoSchemes;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		setContentView(R.layout.activity_schemes);
		mLvSchemes = (ListView) findViewById(R.id.lv_schemes);
		mTvNoSchemes=(TextView)findViewById(R.id.tv_no_schmes_available_activity_scheme);

		
		if (Helper.isOnline(getApplicationContext())) {
			callWebService();
		} else {

			Helper.showAlertDialog(this, SSCAlertDialog.ERROR_TYPE,
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

	private void callWebService() {

		/*
		 * PostDataToNetwork pdn = new PostDataToNetwork(
		 * TodaySchemesActivity.this, getResources().getString(
		 * R.string.loadingmessage), new GetDataCallBack() {
		 * 
		 * @Override public void processResponse(Object result) { if (result !=
		 * null) {
		 * 
		 * parseResponse(result);
		 * 
		 * } else {
		 * 
		 * Helper.showCustomToast(TodaySchemesActivity.this,
		 * R.string.networkserverbusy, Toast.LENGTH_LONG);
		 * 
		 * } } });
		 */
		JSONObject jobj = new JSONObject();

		try {
			jobj.put(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(TodaySchemesActivity.this,
							SharedPreferencesKey.PREF_USERID));

			jobj.put(WebConfig.WebParams.ROLE_ID_CAPS, Long.valueOf(Helper
					.getIntValueFromPrefs(TodaySchemesActivity.this,
							SharedPreferencesKey.PREF_ROLEID)));

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
	/*	pdn.setConfig(getString(R.string.url),
				WebConfig.WebMethod.GET_TODAY_SCHEMES);
		pdn.execute(jobj);*/
		
		
		VolleyPostDataToNetwork pdn=new VolleyPostDataToNetwork(TodaySchemesActivity.this, getString(R.string.loadingmessage), new VolleyGetDataCallBack() {
			
			@Override
			public void processResponse(Object result) {
				if (result != null) {

					parseResponse(result);

				} else {

					Helper.showCustomToast(TodaySchemesActivity.this,
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
				WebConfig.WebMethod.GET_TODAY_SCHEMES);
		
		pdn.callWebService();

	}

	private void parseResponse(Object response) {
		try {
			JSONObject jsonObject = new JSONObject(response.toString());
			boolean isSuccess = jsonObject.getBoolean("IsSuccess");
			if (isSuccess) {

				JSONArray result = jsonObject.isNull("Result") ? null
						: jsonObject.getJSONArray("Result");
				if (result != null) {

					if(result.length()!=0)
					{
						showData(result);	
					}else
					{
						mTvNoSchemes.setVisibility(View.VISIBLE);
					}
					
					
				} else {
					Helper.showAlertDialog(
							this,
							SSCAlertDialog.WARNING_TYPE,
							this.getString(R.string.error3),
							this.getString(R.string.no_data_availble),
							this.getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {

									sdAlertDialog.dismiss();

								}
							}, false, null, null);

				}

			} else {
				Helper.showAlertDialog(
						this,
						SSCAlertDialog.ERROR_TYPE,
						this.getString(R.string.error7),
						this.getString(R.string.error_contact_admin),
						this.getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {

								sdAlertDialog.dismiss();

							}
						}, false, null, null);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showData(JSONArray result) {

		int length = result.length();

		List<TodaySchemesModel> listTodaySchemes = new ArrayList<TodaySchemesModel>();

		for (int i = 0; i < length; i++) {

			try {
				TodaySchemesModel singleTodayScheme = new TodaySchemesModel();

				JSONObject singleScheme = result.getJSONObject(i);

				String htmlFileName = singleScheme.isNull("HTMLFilename") ? ""
						: singleScheme.getString("HTMLFilename");
				int schemID = singleScheme.getInt("SDSchemeID");

				String schemeTitle = singleScheme.isNull("Title") ? ""
						: singleScheme.getString("Title");

				String validFromDate = singleScheme.isNull("strDateValidFrom") ? ""
						: singleScheme.getString("strDateValidFrom");

				String validToDate = singleScheme.isNull("strDateValidTo") ? ""
						: singleScheme.getString("strDateValidTo");

				singleTodayScheme.hTMLFilename = htmlFileName;
				singleTodayScheme.schemeID = schemID;
				singleTodayScheme.title = schemeTitle;
				singleTodayScheme.dateValidFrom = validFromDate;
				singleTodayScheme.dateValidTo = validToDate;

				listTodaySchemes.add(singleTodayScheme);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		

		TodaySchemeAdapter adapter = new TodaySchemeAdapter(
				getApplicationContext(), listTodaySchemes);

		mLvSchemes.setAdapter(adapter);

		mLvSchemes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					
					TodaySchemesModel todayScheme=(TodaySchemesModel) parent.getItemAtPosition(position);
					
					Intent intent = new Intent(TodaySchemesActivity.this,
							SchemeDetailActivity.class);
					intent.putExtra(IntentKey.TODAY_SCHEME_HTML_FILE_NAME, todayScheme.hTMLFilename);
					
					startActivity(intent);
				} catch (Exception e) {
					
					e.printStackTrace();
				}

			}
		});

		
		

	}

}
