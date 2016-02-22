package com.samsung.ssc;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.samsung.ssc.adapters.KPIListAdapter1;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.dto.GetKASResponse;
import com.samsung.ssc.dto.GetKPIRequest;
import com.samsung.ssc.dto.KPIData;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class KPIListActivity1 extends BaseActivity implements GetDataCallBack {

	//private ListView listView;
	//private ArrayList<KPIData> kpiDatas = new ArrayList<KPIData>();
 
	private KPIListAdapter1 mListAdapter;

	@Override
	public void init() {
		super.init();
		
		setUpView();
		
	}
	
	private void setUpView() {
		setContentView(R.layout.kpi_list_activity1);
		ListView listView = (ListView) findViewById(R.id.kpiListView);
		
		 mListAdapter = new KPIListAdapter1(this);
		listView.setAdapter(mListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				try {
					KPIData item = (KPIData)parent.getItemAtPosition(position);
					
					if (item!=null) {
						 
						Intent intent = new Intent(KPIListActivity1.this,NotificationsListFragment2.class);
						intent.putExtra(IntentKey.NOTIFICATION_MESSAGE_TYPE, item.getNotificationType());
						intent.putExtra(IntentKey.KAS_NAME, item.getNotificationTypeDescription());
						KPIListActivity1.this.startActivity(intent);	
					}
				} catch (Exception e) {
					Helper.printStackTrace(e);
				}
			}
		});		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (!Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)
				.equalsIgnoreCase("")) {
			
			getKasList();
			
		}else{

			Helper.showCustomToast(getApplicationContext(), R.string.you_are_logged_out, Toast.LENGTH_SHORT);
			
		}
		
	}

	private void getKasList() {
		
		GetKPIRequest getKPIRequest = new GetKPIRequest(KPIListActivity1.this);
		Gson gson = new Gson();
		String jsonString = gson.toJson(getKPIRequest);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}

		PostDataToNetwork dataToNetwork = new PostDataToNetwork(
				KPIListActivity1.this, "Loading...", KPIListActivity1.this);
		dataToNetwork.setConfig(getString(R.string.url),
				"NotificationTypeMaster");
		dataToNetwork.execute(jsonObject);
	}

	@Override
	public void processResponse(Object result) {
		if (result != null) {
			String jsonResult = result.toString();
			if (!TextUtils.isEmpty(jsonResult)) {
				Gson gson = new Gson();

				try {
					GetKASResponse getKASResponse = (GetKASResponse) gson
							.fromJson(jsonResult, GetKASResponse.class);
					if (getKASResponse.isSuccess()) {
						ArrayList<KPIData> kpiDatas = getKASResponse.getNotificationDatas();

						if (!kpiDatas.isEmpty()) {
							
							
							mListAdapter.addItem(kpiDatas);

							
						} else {
							
							Helper.showCustomToast(getApplicationContext(), R.string.no_data_received, Toast.LENGTH_SHORT);
						}
					} else {
						Helper.showCustomToast(getApplicationContext(), getKASResponse.getMessage(), Toast.LENGTH_SHORT);
						
						
					}
				} catch (Exception exception) {
					Helper.printStackTrace(exception);
					
				}
			}
		}
	}
}
