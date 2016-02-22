package com.samsung.ssc;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.samsung.ssc.adapters.TerritoriesListAdapter;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.util.Helper;

public class MyTerritoriesActivity extends BaseActivity {

	private ExpandableListView territoryExpandableListView;
	private TerritoriesListAdapter territoriesListAdapter;
	private int lastExpandedPosition = -1;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.activity_territorries);


		setUpViews();
		
	}

	private void setUpViews() {
		territoryExpandableListView = (ExpandableListView) findViewById(R.id.activity_territorries_expandablelistview_disty);
		territoryExpandableListView.setGroupIndicator(null);
		territoryExpandableListView.setClickable(true);

		territoryExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {

						if (lastExpandedPosition != -1
								&& groupPosition != lastExpandedPosition) {
							territoryExpandableListView
									.collapseGroup(lastExpandedPosition);
						}
						lastExpandedPosition = groupPosition;

					}
				});
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (Helper.isOnline(MyTerritoriesActivity.this)) {
			String url = getResources().getString(R.string.url)
					+ "GetMyTerritory";
			new GetDistyAsyncTask().execute(url);
		} else {
			showErrorDialog("No Internet Connection!");
		}
	}

	private void getTerriToriesList(JSONArray territories) {
		LinkedHashMap<String, HashMap<String, String>> rootmap = new LinkedHashMap<String, HashMap<String, String>>();
		try {
			for (int i = 0; i < territories.length(); i++) {
				HashMap<String, String> childMap = new HashMap<String, String>();
				JSONObject jsonObject = territories.getJSONObject(i);

				if (jsonObject.has("ActiveND")
						&& (jsonObject.get("ActiveND"))!=null) {
					childMap.put("ActiveND", jsonObject.get("ActiveND")
							.toString());
				}

				if (jsonObject.has("DistyCode")
						&& (jsonObject.get("DistyCode"))!=null) {
					childMap.put("DistyCode", jsonObject.get("DistyCode")
							.toString());
				}

				if (jsonObject.has("DistyWOS")
						&& (jsonObject.get("DistyWOS"))!=null) {
					childMap.put("DistyCode", jsonObject.get("DistyCode")
							.toString());
				}

				if (jsonObject.has("NotBilledSPPDLRs")
						&& (jsonObject.get("NotBilledSPPDLRs"))!=null) {
					childMap.put("NotBilledSPPDLRs",
							jsonObject.get("NotBilledSPPDLRs").toString());
				}

				if (jsonObject.has("SPPSellOut")
						&& (jsonObject.get("SPPSellOut"))!=null) {
					childMap.put("SPPSellOut", jsonObject.get("SPPSellOut")
							.toString());
				}

				if (jsonObject.has("SPPSellThru")
						&& (jsonObject.get("SPPSellThru"))!=null) {
					childMap.put("SPPSellThru", jsonObject.get("SPPSellThru")
							.toString());
				}

				if (jsonObject.has("TTLSPPDLRs")
						&& (jsonObject.get("TTLSPPDLRs"))!=null) {
					childMap.put("TTLSPPDLRs", jsonObject.get("TTLSPPDLRs")
							.toString());
				}

				if (jsonObject.has("TTLSellOut")
						&& (jsonObject.get("TTLSellOut"))!=null) {
					childMap.put("TTLSellOut", jsonObject.get("TTLSellOut")
							.toString());
				}
				if (jsonObject.has("TTLSellThru")
						&& (jsonObject.get("TTLSellThru"))!=null) {
					childMap.put("TTLSellThru", jsonObject.get("TTLSellThru")
							.toString());
				}
				if (jsonObject.has("TotalND")
						&& (jsonObject.get("TotalND"))!=null) {
					childMap.put("TotalND", jsonObject.get("TotalND")
							.toString());
				}
				rootmap.put(jsonObject.get("DistyName").toString(), childMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((rootmap != null) && (rootmap.size() > 0)) {

			// ArrayList<Territory> territories = dupJSON();
			territoriesListAdapter = new TerritoriesListAdapter(
					MyTerritoriesActivity.this, rootmap);

			territoryExpandableListView.setAdapter(territoriesListAdapter);

			territoryExpandableListView.expandGroup(0);
		}

	}

	private class GetDistyAsyncTask extends AsyncTask<String, Void, JSONArray> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MyTerritoriesActivity.this,
					"Wait", "Loading...", true, true);
		}

		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray territories = new JSONArray();
			try {

				String userId = Helper.getStringValuefromPrefs(
						MyTerritoriesActivity.this, SharedPreferencesKey.PREF_USERID);

				JSONObject object = new JSONObject();
				object.put("userID", userId);

				String responseString = Helper.postMethod(
						MyTerritoriesActivity.this, params[0], object);

				territories = getJsonFromstring(responseString);
			} catch (Exception e) {
				showErrorDialog("Server Error!");
			}
			return territories;
		}

		@Override
		protected void onPostExecute(JSONArray territories) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			if (territories != null) {
				getTerriToriesList(territories);
			} else {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MyTerritoriesActivity.this);
				dialog.setMessage("No Results!");
				dialog.show();
			}
		}
	}

	private JSONArray getJsonFromstring(String responseString) {

		JSONObject jsonObject;
		JSONArray moduleJsonArray = null;
		try {
			jsonObject = new JSONObject(responseString);
			moduleJsonArray = jsonObject.getJSONArray("Result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return moduleJsonArray;
	}

	public void showErrorDialog(String msg) {
	
		Helper.showCustomToast(getApplicationContext(), msg, Toast.LENGTH_LONG);
		
	}
}
