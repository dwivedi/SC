package com.samsung.ssc.activitymodule;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.SchemeAvailableAdapter;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.SchemeAvailableDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class SchemeAvailable extends BaseActivity {
	private String storeId;
	private ListView lvScheme;
	private List<SchemeAvailableDto>schemeResult;
	@Override
	public void init() {
		super.init();
		setContentView(R.layout.schemavailable1);
		setCurrentContext(SchemeAvailable.this);
 		lvScheme=(ListView)findViewById(R.id.lvscheme);
		storeId=Helper.getStringValuefromPrefs(SchemeAvailable.this, SharedPreferencesKey.PREF_STOREID);
		if(storeId!=null){
			if (isOnline()) {	
				getScheme(storeId);
			} else {
		 
				Helper.showAlertDialog(SchemeAvailable.this, SSCAlertDialog.ERROR_TYPE, getString(R.string.error1), getString(R.string.error2), getString(R.string.ok), new SSCAlertDialog.OnSDAlertDialogClickListener() {
					
					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						finish();
						
					}
				}, false, null, null);
			}
		}
		lvScheme.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if(schemeResult!=null){
					Bundle bundel=new Bundle();	
					bundel.putString("Description", schemeResult.get(arg2).getDescription().toString());
					bundel.putString("Title", schemeResult.get(arg2).getTitle().toString());
					bundel.putString("StartDate", schemeResult.get(arg2).getSchemeStartDate().toString());
					bundel.putString("ExpiryDate", schemeResult.get(arg2).getSchemeExpiryDate().toString());
					Intent intent=new Intent(SchemeAvailable.this,SchemeDetails.class);
					intent.putExtras(bundel);
					startActivity(intent);
				}
				
				
			}
		});
		
	}
	
	/*
	 * Retrieve data for Scheme
	 */

	private void getScheme(String storeId) {

		PostDataToNetwork pdn = new PostDataToNetwork(SchemeAvailable.this,
				getStringFromResource(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						if (result != null) {
							Helper.printLog("resul ", "resul scheme"+result.toString());
							schemeResult = new FetchingdataParser(SchemeAvailable.this).parsSchemeAvailable(result.toString());
							if(schemeResult.size()>0){
								for(int i=0;i<schemeResult.size();i++){
									if(!schemeResult.get(i).isSuccess()){
										
										Helper.showCustomToast(SchemeAvailable.this, schemeResult.get(i).getMessage(), Toast.LENGTH_LONG);
										
										return;
									}
									
								}
								lvScheme.setAdapter(new SchemeAvailableAdapter(SchemeAvailable.this, R.layout.schemeavailablelist, schemeResult));
								
							}else {
								 
								
								Helper.showAlertDialog(SchemeAvailable.this, SSCAlertDialog.ERROR_TYPE, getString(R.string.error3), getString(R.string.datanotavailable), getString(R.string.ok), new SSCAlertDialog.OnSDAlertDialogClickListener() {
									
									@Override
									public void onClick(SSCAlertDialog sdAlertDialog) {
										sdAlertDialog.dismiss();
										finish();
										
									}
								}, false, null, null);
							}
						}else {
							Toast.makeText(SchemeAvailable.this,getStringFromResource(R.string.networkserverbusy),Toast.LENGTH_LONG).show();
						}

					}

				});
		JSONObject jsonobj = new JSONObject();
		try {
			
			jsonobj.put("storeID", Integer.parseInt(storeId));
			jsonobj.put(WebConfig.WebParams.USER_ID,Long.valueOf(Helper.getStringValuefromPrefs(SchemeAvailable.this, SharedPreferencesKey.PREF_USERID)) );
			printLog("result", jsonobj.toString());
		} catch (NumberFormatException e) {
			Helper.printStackTrace(e);
		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getStringFromResource(R.string.url),"GetStoreSchemes");

		pdn.execute(jsonobj);

	}
	
	
	
	
}
