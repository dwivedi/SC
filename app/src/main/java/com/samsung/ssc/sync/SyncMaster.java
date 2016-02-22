package com.samsung.ssc.sync;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.ModuleCode;
import com.samsung.ssc.constants.QuestionType;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.PlanogramCompitiorResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramProductResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramResponseMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.StoreGeoTagResponseMasterColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.UploadImageMultipartDataModal;
import com.samsung.ssc.dto.UploadMuliplePartImageRepeaterDataModal;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.OnOkListener;
import com.samsung.ssc.util.SyncPreparationCompeteCallback;

public class SyncMaster {

	private Activity mContext;

	private boolean shouldClearDataAfterSync;

	private ArrayList<ActivityDataMasterModel> activitiesForSync = new ArrayList<ActivityDataMasterModel>();;

	private OnOkListener okListener;

	private SyncListNotificationAdapter syncListAdapter;

	private SSCAlertDialog syncListDialog;

	public SyncMaster(Activity activity) {
		this.mContext = activity;
	}

	/**
	 * This method calls respective sync methods based on the module saved in
	 * table
	 */

	public synchronized void sync(
			final ArrayList<ActivityDataMasterModel> activitiesForSync,
			boolean shouldClearDataAfterSync, OnOkListener okListener) {

		if (okListener != null) {
			this.okListener = okListener;
		}
		this.shouldClearDataAfterSync = shouldClearDataAfterSync;
		registerBroadcastReceiver();
		showSyncingModules1();

		Thread thread = new Thread() {
			@Override
			public void run() {

				/**
				 * sync for Question type data
				 */
				syncForQuestionAnswerResponse1(activitiesForSync);

				/**
				 * Sync for Activity type Data
				 */

				for (ActivityDataMasterModel activityDataMasterModel : activitiesForSync) {

					int moduleCode = activityDataMasterModel.getModuleCode();

					if (moduleCode == ModuleCode.MENU_GEOTAG) {
						callWebServiceForGEOTag(activityDataMasterModel);
					} else if (moduleCode == ModuleCode.MENU_DISPLAYSHARE) {
						callWebServiceForComptition(activityDataMasterModel, 1);
					} else if (moduleCode == ModuleCode.MENU_COUNTERSHARE) {
						callWebServiceForComptition(activityDataMasterModel, 2);
					} else if (moduleCode == ModuleCode.MENU_STOCK_ESCALATION) {
						callWebServiceForOrder(activityDataMasterModel, 1);
					} else if (moduleCode == ModuleCode.MENU_ORDER_BOOKING) {
						callWebServiceForOrder(activityDataMasterModel, 2);
					} else if (moduleCode == ModuleCode.MENU_SELL_OUT_PROJECTION) {
						callWebServiceForOrder(activityDataMasterModel, 3);
					} else if (moduleCode == ModuleCode.MENU_COLLECTION) {
						callWebServiceForCollection(activityDataMasterModel);
					} else if (moduleCode == ModuleCode.MENU_PALNOGRAM) {
						callWebServiceForPlanogramData(activityDataMasterModel);
					} else if (moduleCode == ModuleCode.MENU_OD_EOL) {
						callWebServiceForEOLOrderBookingData(activityDataMasterModel);
					} else if (moduleCode == ModuleCode.MENU_PRODUCT_AUDIT) {
						callWebServiceForProductAudit(activityDataMasterModel);
					}

				}

			}

		};

		thread.start();
	}

	protected void callWebServiceForProductAudit(
			ActivityDataMasterModel activityDataMasterModel) {

		long mActivityID = activityDataMasterModel.getActivityID();

		
		/*  JSONArray result = DatabaseHelper.getConnection(mContext)
		  .getRaceResponseData(mActivityID);
		 */

		Cursor cursor=mContext.getContentResolver().query(
				ProviderContract.URI_RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC,
				null, null, new String[] { String.valueOf(mActivityID) }, null);

		JSONArray result= DatabaseUtilMethods.getJSONArrayRaceAuditResponseFromCursor(cursor);	
		cursor.close();

		JSONObject jsonObject = new JSONObject();
		
		try {
			// JSONArray rootJson = new JSONArray(result.toString());

			String webServiceMethod = WebConfig.WebMethod.SUBMIT_PRODUCT_AUDIT;
			long[] mActivityIDs = new long[] { mActivityID };
			jsonObject.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_USERID)));
			jsonObject.put(WebConfig.WebParams.SURVEY_RESPONSE_ID,
					activityDataMasterModel.getSurveyResponseID());
			JSONObject obj = new JSONObject(); // temp object
			obj.put(WebConfig.WebParams.STOCK_AUDIT_SUMMARY, result);
			jsonObject.put(WebConfig.WebParams.AUDIT_RESPONSE, obj);

			startService(mActivityIDs, webServiceMethod, jsonObject, false,
					activityDataMasterModel.getModuleName() + "["
							+ activityDataMasterModel.getStoreName() + "]");

		} catch (JSONException e) {

		}

	}

	protected void callWebServiceForGEOTag(
			ActivityDataMasterModel activityDataMasterModel) {
		try {
			long mActivityID = activityDataMasterModel.getActivityID();
			JSONObject jObject = DatabaseHelper.getConnection(mContext)
					.getStoreGeoTagData(mActivityID);

			if (jObject != null) {
				long[] activityIDs = new long[] { mActivityID };
				ArrayList<UploadImageMultipartDataModal> imageUploadDataImages = new ArrayList<UploadImageMultipartDataModal>();
				UploadImageMultipartDataModal object = new UploadImageMultipartDataModal();

				object.setUserId(Long.valueOf(Helper.getStringValuefromPrefs(
						mContext, SharedPreferencesKey.PREF_USERID)));
				object.setUserResponse(jObject.getString("GeoImage"));
				object.setLatitude(jObject.getDouble("Latitude"));
				object.setLongitude(jObject.getDouble("Longitude"));
				object.setStoreID(jObject.getInt("StoreID"));
				object.setType(-1);
				try {
					object.setUserOption(String.valueOf(jObject
							.getBoolean("UserOption")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				object.setSurveyResponseId(-1);
				object.setSurveyQuestionId(-1);

				imageUploadDataImages.add(object);

				String serviceURL = mContext.getString(R.string.url)
						+ WebConfig.WebMethod.UPLOAD_GEO_IMAGE_STREAM;

				Intent service = new Intent(mContext, SyncService.class);
				service.putExtra(SyncUtils.ACTIVITY_ID, activityIDs);
				service.putExtra(SyncUtils.CONFUGRATION_URL, serviceURL);
				service.putExtra(SyncUtils.REQUEST_STRING,
						imageUploadDataImages);
				service.putExtra(SyncUtils.REQUEST_STRING_IS_IMAGE_TYPE, true);
				service.putExtra(SyncUtils.SYNC_SERVICE,
						"GEO TAG [" + jObject.getString("StoreName") + "]");
				mContext.startService(service);

			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void callWebServiceForGEOTagC(
			ActivityDataMasterModel activityDataMasterModel) {
		try {
			long mActivityID = activityDataMasterModel.getActivityID();

			String[] projection = {
					StoreGeoTagResponseMasterColumns.KEY_BEAT_STORE_ID,
					StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LATITUDE,
					StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LONGITUDE,
					StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_IMAGE,
					StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_USER_OPTION };

			String selection = StoreGeoTagResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID;
			String selectionArgs[] = { String.valueOf(mActivityID) };

			Cursor cursor = mContext.getContentResolver().query(
					ProviderContract.URI_STORE_GEO_TAG_RESPONSE, projection,
					selection, selectionArgs, null);

			/*
			 * JSONObject jObject = DatabaseHelper.getConnection(mContext)
			 * .getStoreGeoTagData(mActivityID);
			 */

			if (cursor != null && cursor.moveToFirst()) {
				long[] activityIDs = new long[] { mActivityID };
				ArrayList<UploadImageMultipartDataModal> imageUploadDataImages = new ArrayList<UploadImageMultipartDataModal>();
				UploadImageMultipartDataModal object = new UploadImageMultipartDataModal();

				object.setUserId(Long.valueOf(Helper.getStringValuefromPrefs(
						mContext, SharedPreferencesKey.PREF_USERID)));

				/*
				 * object.setUserResponse(jObject.getString("GeoImage"));
				 * object.setLatitude(jObject.getDouble("Latitude"));
				 * object.setLongitude(jObject.getDouble("Longitude"));
				 * object.setStoreID(jObject.getInt("StoreID"));
				 */

				object.setUserResponse(cursor.getString(cursor
						.getColumnIndex(StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_IMAGE)) != null ? cursor.getString(cursor
						.getColumnIndex(StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_IMAGE))
						: "");

				object.setLongitude(cursor.getDouble(cursor
						.getColumnIndex(StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LONGITUDE)));

				object.setLatitude(cursor.getDouble(cursor
						.getColumnIndex(StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LATITUDE)));

				object.setStoreID(cursor.getInt(cursor
						.getColumnIndex(StoreGeoTagResponseMasterColumns.KEY_BEAT_STORE_ID)));

				object.setUserOption(cursor.getString(cursor
						.getColumnIndex(StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_USER_OPTION)));

				object.setType(-1);
				object.setSurveyResponseId(-1);
				object.setSurveyQuestionId(-1);

				imageUploadDataImages.add(object);

				String serviceURL = mContext.getString(R.string.url)
						+ WebConfig.WebMethod.UPLOAD_GEO_IMAGE_STREAM;

				Intent service = new Intent(mContext, SyncService.class);
				service.putExtra(SyncUtils.ACTIVITY_ID, activityIDs);
				service.putExtra(SyncUtils.CONFUGRATION_URL, serviceURL);
				service.putExtra(SyncUtils.REQUEST_STRING,
						imageUploadDataImages);
				service.putExtra(SyncUtils.REQUEST_STRING_IS_IMAGE_TYPE, true);
				service.putExtra(SyncUtils.SYNC_SERVICE, "GEO TAG DATA");
				mContext.startService(service);
				cursor.close();

			}
			
			

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * EOL response data
	 * 
	 * @param activityDataMasterModel
	 */
	protected void callWebServiceForEOLOrderBookingData(
			ActivityDataMasterModel activityDataMasterModel) {

		long mActivityID = activityDataMasterModel.getActivityID();
		JSONArray result = DatabaseHelper.getConnection(mContext)
				.getEOLOrderBoolingResponseData(mActivityID);

		JSONObject jsonObject = new JSONObject();
		try {
			JSONArray rootJson = new JSONArray(result.toString());
			jsonObject.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_USERID)));
			jsonObject.put("eolOrders", rootJson);
			String webServiceMethod = WebConfig.WebMethod.SUBMIT_EOL_ORDER;
			long[] mActivityIDs = new long[] { mActivityID };

			startService(mActivityIDs, webServiceMethod, jsonObject, false,
					activityDataMasterModel.getModuleName() + "["
							+ activityDataMasterModel.getStoreName() + "]");

		} catch (JSONException e) {

		}

	}

	/**
	 * Calls service to send planogram data to server
	 * 
	 * @param activityDataMasterModel
	 *            the activity module for which planogram data will be send
	 */
	private void callWebServiceForPlanogramData(
			ActivityDataMasterModel activityDataMasterModel) {

		long mActivityID = activityDataMasterModel.getActivityID();

		JSONArray result = DatabaseHelper.getConnection(mContext)
				.getPlanogramResponseIsAvilable(mActivityID);

		JSONObject jsonObject = new JSONObject();

		try {

			// JSONArray result = getPlanogramResponse(mActivityID);

			if (result == null) {
				return;
			}

			JSONArray rootJson = new JSONArray(result.toString());

			// rootJson = processJson(rootJson);
			jsonObject.put(WebConfig.WebParams.COMPANY_ID, Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_COMPANYID));
			jsonObject.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_USERID)));
			jsonObject.put(WebConfig.WebParams.ROLE_ID, Long.valueOf(Helper
					.getIntValueFromPrefs(mContext,
							SharedPreferencesKey.PREF_ROLEID)));
			jsonObject.put("PlanogramResponse", rootJson);

			String webServiceMethod = WebConfig.WebMethod.SUBMIT_PLANOGRAM;
			long[] mActivityIDs = new long[] { mActivityID };

			startService(mActivityIDs, webServiceMethod, jsonObject, false,
					activityDataMasterModel.getModuleName() + "["
							+ activityDataMasterModel.getStoreName() + "]");

		} catch (JSONException e) {

		}

	}

	/**
	 * Calls service to send Collection data to server
	 * 
	 * @param activityDataMasterModel
	 *            the activity module for which Collection data will be send
	 */
	private void callWebServiceForCollection(
			ActivityDataMasterModel activityDataMasterModel) {
		long activityID = activityDataMasterModel.getActivityID();

		if (activityID != -1) {
			try {

				/*
				 * JSONArray itemsArray = DatabaseHelper.getConnection(mContext)
				 * .getCollectionResponse(activityID);
				 */

				Cursor cursor = mContext.getContentResolver().query(
						ProviderContract.URI_COLLECTION_RESPONSE, null, null,
						new String[] { String.valueOf(activityID) }, null);

				JSONArray itemsArray = DatabaseUtilMethods
						.getCollectionResponseFromCursor(mContext, cursor);

			
					cursor.close();
				

				if (itemsArray.length() == 0) {
					return;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("collection", itemsArray);

				String webServiceMethod = WebConfig.WebMethod.SUBMIT_COLLECTION_SURVEY;
				long[] mActivityIDs = new long[] { activityID };

				startService(mActivityIDs, webServiceMethod, jsonObject, false,
						activityDataMasterModel.getModuleName() + "["
								+ activityDataMasterModel.getStoreName() + "]");

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
			}
		}

	}

	private JSONArray getPlanogramResponse(long activityID) {
		JSONArray rootResponseJSONArray = null;
		String[] planResponseIDs = null;

		/*
		 * JSONArray result = DatabaseHelper.getConnection(mContext)
		 * .getPlanogramResponseIsAvilable(mActivityID);
		 */

		// start of planogram response
		Cursor cursor = mContext
				.getContentResolver()
				.query(ProviderContract.URI_PLANOGRAM_RESPONSE_URI,
						new String[] {
								PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
								PlanogramResponseMasterTableColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
								PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
								PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID,
								PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE },
						PlanogramResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
								+ "=?",
						new String[] { String.valueOf(activityID) }, null);

		if (cursor != null && cursor.moveToFirst()) {
			rootResponseJSONArray = DatabaseUtilMethods
					.getPlanogramRootResponseFromCursor(cursor);

			planResponseIDs = new String[rootResponseJSONArray.length()];

			for (int i = 0; i < planResponseIDs.length; i++) {
				try {
					planResponseIDs[i] = String.valueOf(rootResponseJSONArray
							.getJSONObject(i).getInt("PlanResponseID"));

				} catch (JSONException e) {

					e.printStackTrace();
				}
			}

			cursor.close();

		}
		// end of planogram response

		if (planResponseIDs == null) {
			return null;
		}

		// start of planogram product response

		String selection1 = PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
				+ " in (";
		for (int i = 0; i < planResponseIDs.length; i++) {
			selection1 += "?, ";
		}
		selection1 = selection1.substring(0, selection1.length() - 2) + ")";

		mContext.getContentResolver()
				.query(ProviderContract.URI_PLANOGRAM_PRODUCT_RESPONSE_URI,
						new String[] {
								PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
								PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
								PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE },
						selection1, planResponseIDs, null);

		if (cursor != null && cursor.moveToFirst()) {
			JSONArray planogramProductResponse = DatabaseUtilMethods
					.getPlanogramProductResponseFromCursor(cursor);

			for (int i = 0; i < rootResponseJSONArray.length(); i++) {
				try {
					JSONObject rootItem = rootResponseJSONArray
							.getJSONObject(i);

					JSONArray productJsonArray = new JSONArray();

					int planResponseID = rootItem.getInt("PlanResponseID");

					for (int j = 0; j < planogramProductResponse.length(); j++) {

						int planResponseIDProduct = planogramProductResponse
								.getJSONObject(j).getInt("PlanResponseID");
						if (planResponseID == planResponseIDProduct) {
							productJsonArray.put(planogramProductResponse
									.getJSONObject(j));
						}
					}
					rootItem.put("PlanogramProductResponses", productJsonArray);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			cursor.close();
		}

		// end of planogram product response

		// start of planogram competitor response

		String selection2 = PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
				+ " in (";
		for (int i = 0; i < planResponseIDs.length; i++) {
			selection2 += "?, ";
		}
		selection2 = selection2.substring(0, selection2.length() - 2) + ")";

		cursor = mContext
				.getContentResolver()
				.query(ProviderContract.URI_PLANOGRAM_COMPETITORS_RESPONSE_URI,
						new String[] {
								PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
								PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE,
								PlanogramCompitiorResponseMasterColumns.KEY_COMETITOR_ID },
						selection2, planResponseIDs, null);

		if (cursor != null && cursor.moveToFirst()) {
			JSONArray planogramProductResponse = DatabaseUtilMethods
					.getPlanogramProductResponseFromCursor(cursor);

			for (int i = 0; i < rootResponseJSONArray.length(); i++) {
				try {
					JSONObject rootItem = rootResponseJSONArray
							.getJSONObject(i);

					JSONArray productJsonArray = new JSONArray();

					int planResponseID = rootItem.getInt("PlanResponseID");

					for (int j = 0; j < planogramProductResponse.length(); j++) {

						int planResponseIDProduct = planogramProductResponse
								.getJSONObject(j).getInt("PlanResponseID");
						if (planResponseID == planResponseIDProduct) {
							productJsonArray.put(planogramProductResponse
									.getJSONObject(j));
						}
					}

					rootItem.put("PlanogramProductResponses", productJsonArray);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			cursor.close();
		}
		// end of planogram competitor response

		// start planogram product response

		return rootResponseJSONArray;

	}

	/**
	 * This method register local broadcast receiver to receive information
	 * service
	 */
	public void registerBroadcastReceiver() {

		LocalBroadcastManager.getInstance(mContext).registerReceiver(
				mMessageReceiver,
				new IntentFilter(SyncUtils.SYNC_ACTION_UPDATE));

		LocalBroadcastManager.getInstance(mContext)
				.registerReceiver(mMessageReceiver,
						new IntentFilter(SyncUtils.SYNC_ACTION_START));

		LocalBroadcastManager.getInstance(mContext).registerReceiver(
				mMessageReceiver,
				new IntentFilter(SyncUtils.SYNC_ACTION_COMPLETED));

	}

	/**
	 * This method get the data to that are submitted by the user
	 * 
	 * @return ActivityDataMasterModel list
	 */
	public synchronized void getSyncData(SyncPreparationCompeteCallback callback) {

		new SyncPreparationAsyncTask(callback).execute();

	}

	/**
	 * Gets activity ids that needed to be synced from database
	 * 
	 * @author d.ashish
	 * 
	 */

	class SyncPreparationAsyncTask extends AsyncTask<Void, Void, Void> {

		SyncPreparationCompeteCallback callback;

		public SyncPreparationAsyncTask(SyncPreparationCompeteCallback callback) {
			this.callback = callback;
		}

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {

				progressDialog = SSCProgressDialog.ctor(mContext);
			} else {

				progressDialog = new ProgressDialog(mContext);
				progressDialog.setProgress(0);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage(mContext
						.getString(R.string.preparation_for_sync));
				progressDialog.setCancelable(false);
			}

			if (!mContext.isFinishing()) {
				progressDialog.show();
			}

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				JSONArray requestData = new JSONArray();
				List<ActivityDataMasterModel> storeIDs = DatabaseHelper
						.getConnection(mContext).getActivityStoreIDs();
				int count = storeIDs.size();
				if (count > 0) {
					for (int i = 0; i < count; i++) {

						try {
							JSONObject jsonObject = new JSONObject();

							jsonObject.put(WebConfig.WebParams.STORE_ID_CAPS,
									storeIDs.get(i).getStoreID());
							jsonObject.put(WebConfig.WebParams.USER_ID_CAPS,
									storeIDs.get(i).getUserID());
							jsonObject
									.put(WebConfig.WebParams.RACE_PROFILE,
											Helper.getBoolValueFromPrefs(
													mContext,
													SharedPreferencesKey.PREF_IS_RACE_PROFILE));
							jsonObject.put(
									WebConfig.WebParams.ASSESSMENT_START_TIME,
									Helper.getFormatedDate(storeIDs.get(i)
											.getAssessmentStartTime()));
							jsonObject.put(
									WebConfig.WebParams.ASSESSMENT_END_TIME,
									Helper.getFormatedDate(storeIDs.get(i)
											.getAssessmentEndTime()));

							if (storeIDs.get(i).getCoverageID() != 0) {
								jsonObject.put(
										WebConfig.WebParams.COVERAGEPLANID,
										storeIDs.get(i).getCoverageID());
							}
							requestData.put(jsonObject);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					JSONObject nvps = new JSONObject();
					nvps.put(WebConfig.WebParams.STORE_SURVEY, requestData);
					String responseString = postMethod(
							mContext.getString(R.string.url)
									+ WebConfig.WebMethod.SAVE_STORE_SURVEY_RESPONSES,
							nvps);
					JSONObject responseJsonObject = new JSONObject(
							responseString.toString());
					if (responseJsonObject.getBoolean("IsSuccess")) {

						JSONArray result = responseJsonObject
								.getJSONArray("Result");
						DatabaseHelper
								.getConnection(mContext)
								.updateActivtyDataMasterServeyResponseID(result);
					} else
						return null;
				}

				activitiesForSync = DatabaseHelper.getConnection(mContext)
						.getActivityDataForSync();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			callback.onComplete(activitiesForSync);

		}

		private String postMethod(String conUrl, JSONObject nvps)
				throws Exception {

			String result = null;
			InputStream is = null;

			HttpClient httpclient = new DefaultHttpClient();
			URI u = new URI(conUrl);
			HttpPost httppost = new HttpPost(u);
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 24000;

			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			httppost.setHeader(WebConfig.WebParams.APIKEY, Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_APIKEY));
			httppost.setHeader(WebConfig.WebParams.APITOKEN, Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_APITOKEN));

			if (!Helper.getStringValuefromPrefs(mContext,
					SharedPreferencesKey.PREF_USERID).equals("")) {

				httppost.setHeader(WebConfig.WebParams.USER_ID, Helper
						.getStringValuefromPrefs(mContext,
								SharedPreferencesKey.PREF_USERID));
			} else {
				httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
			}

			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Accept",
					"application/json, text/javascript, */*;q=0.01");
			httppost.setEntity(new ByteArrayEntity(nvps.toString().getBytes(
					"UTF8")));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				is = entity.getContent();
				result = getStringfromInputStream(is);
				entity.consumeContent();
			}
			httpclient.getConnectionManager().shutdown();
			return result;

		}

		private String getStringfromInputStream(InputStream is) {

			String result = null;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				Helper.printStackTrace(e);
			}
			return result;

		}
	}

	private void showSyncingModules1() {

		syncListAdapter = new SyncListNotificationAdapter(mContext);
		syncListDialog = new SSCAlertDialog(mContext,
				SSCAlertDialog.LIST_TYPE)
				.setTitleText(mContext.getString(R.string.sync_list))
				.setListAdapter(syncListAdapter)
				.setEnableConfirmButton(false)
				.setConfirmText("OK")
				.setConfirmClickListener(
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();
								if (okListener != null) {
									okListener.onOKPressed();

								}
							}

						}).showCancelButton(false).setCancelText(null)
				.setCancelClickListener(null);

		syncListDialog.setCancelable(false);
		syncListDialog.show();
		syncListDialog.setEnableConfirmButton(false);

	}

	private void syncForQuestionAnswerResponse(
			ArrayList<ActivityDataMasterModel> activitiesForSync) {

		ArrayList<Long> activityIDForQuestionModules = new ArrayList<Long>();

		JSONArray questionResponseJSONText = new JSONArray();
		JSONArray questionResponseJSONImage = new JSONArray();
		for (ActivityDataMasterModel activityDataMasterModel : activitiesForSync) {

			if (activityDataMasterModel.isQuestionModule()) {

				long activityID = activityDataMasterModel.getActivityID();
				activityIDForQuestionModules.add(activityID);
				HashMap<String, JSONArray> map = DatabaseHelper.getConnection(
						mContext).getQuestionAnswerResponseForSync(activityID);
				try {
					getTextQuestionRequestJSON(questionResponseJSONText, map);
					getImageQuestionRequestJSON(questionResponseJSONImage, map);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		long[] activityIDs = new long[activityIDForQuestionModules.size()];
		for (int i = 0; i < activityIDForQuestionModules.size(); i++) {
			activityIDs[i] = activityIDForQuestionModules.get(i);
		}
		if (activityIDs.length > 0) {
			callWebServiceForQuestionData(activityIDs,
					questionResponseJSONText, questionResponseJSONImage);
		}
	}

	private void syncForQuestionAnswerResponse1(

	ArrayList<ActivityDataMasterModel> activitiesForSync) {

		ArrayList<Long> activityIDForQuestionModules = new ArrayList<Long>();

		JSONArray questionResponseJSONForAllActivity = new JSONArray();
		for (ActivityDataMasterModel activityDataMasterModel : activitiesForSync) {

			if (activityDataMasterModel.isQuestionModule()) {

				long activityID = activityDataMasterModel.getActivityID();
				
				activityIDForQuestionModules.add(activityID);
				
			/*	  JSONArray questionsJSONArray = DatabaseHelper.getConnection(
				  mContext).getQuestionAnswerResponseForSync1(activityID);
				 
*/
				Cursor cursor = mContext.getContentResolver().query(
						ProviderContract.URI_SURVEY_QUESTION_ANSWER_RESPONSE,
						null, null,
						new String[] { String.valueOf(activityID) }, null);

				JSONArray questionsJSONArray = DatabaseUtilMethods
						.getSurveyQuestionAnswerResponseFromCursor(cursor);

				

					cursor.close();
				

				try {
					getTextQuestionRequestJSON1(
							questionResponseJSONForAllActivity,
							questionsJSONArray);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		long[] activityIDs = new long[activityIDForQuestionModules.size()];
		for (int i = 0; i < activityIDForQuestionModules.size(); i++) {
			activityIDs[i] = activityIDForQuestionModules.get(i);
		}
		if (activityIDs.length > 0) {
			callWebServiceForQuestionData1(activityIDs,
					questionResponseJSONForAllActivity);
		}
	}

	private void getImageQuestionRequestJSON(
			JSONArray questionResponseJSONImage, HashMap<String, JSONArray> map)
			throws JSONException {

		JSONArray arrayImage = map.get("QUESTION_RESPONSE_IMAGE");

		for (int i = 0; i < arrayImage.length(); i++) {
			questionResponseJSONImage.put(arrayImage.getJSONObject(i));
		}

	}

	private void getTextQuestionRequestJSON(JSONArray questionResponseJSONText,
			HashMap<String, JSONArray> map) throws JSONException {

		JSONArray questionsJSONArray = map.get("QUESTION_RESPONSE_TEXT");

		for (int i = 0; i < questionsJSONArray.length(); i++) {

			JSONObject itemJsonQuestion = questionsJSONArray.getJSONObject(i);

			int questionTypeID = itemJsonQuestion.getInt("QuestionTypeID");
			if (questionTypeID == QuestionType.SURVEY_QUESTION_TYPE_REPEATOR) {
				int surveyQuestionID = itemJsonQuestion
						.getInt("SurveyQuestionID");

				JSONArray surveyRepeatResponses = new JSONArray();

				for (int j = 0; j < questionsJSONArray.length(); j++) {

					JSONObject itemQuestionForSubQuestionID = questionsJSONArray
							.getJSONObject(j);
					int surveyQuestionIDLocal = itemQuestionForSubQuestionID
							.getInt("SurveyQuestionID");
					if (surveyQuestionIDLocal == -1) {

						String subQuestionID = itemQuestionForSubQuestionID
								.getString("SurveyQuestionSubID");
						if (subQuestionID.startsWith(surveyQuestionID + "_")) {
							JSONObject repeateResponse = new JSONObject();
							repeateResponse.put("UserResponse",
									itemQuestionForSubQuestionID
											.getString("UserResponse"));
							repeateResponse.put("SurveyQuestionRepeaterID",
									subQuestionID);

							surveyRepeatResponses.put(repeateResponse);
						}
					}
				}

				itemJsonQuestion.put("SurveyRepeatResponses",
						surveyRepeatResponses);

			}
		}
		for (int i = 0; i < questionsJSONArray.length(); i++) {

			JSONObject itemJsonQuestion = questionsJSONArray.getJSONObject(i);
			if (itemJsonQuestion.getInt("SurveyQuestionID") != -1) {
				questionResponseJSONText.put(itemJsonQuestion);
			}
		}

	}

	private void getTextQuestionRequestJSON1(
			JSONArray questionResponseJSONForAllActivity,
			JSONArray questionsJSONArray) throws JSONException {

		for (int i = 0; i < questionsJSONArray.length(); i++) {

			JSONObject itemJsonQuestion = questionsJSONArray.getJSONObject(i);

			int questionTypeID = itemJsonQuestion.getInt("QuestionTypeID");
			if (questionTypeID == QuestionType.SURVEY_QUESTION_TYPE_REPEATOR) {
				int surveyQuestionID = itemJsonQuestion
						.getInt("SurveyQuestionID");

				JSONArray surveyRepeatResponses = new JSONArray();
				int counter = 1;
				for (int j = 0; j < questionsJSONArray.length(); j++) {

					JSONObject itemQuestionForSubQuestionID = questionsJSONArray
							.getJSONObject(j);
					int surveyQuestionIDLocal = itemQuestionForSubQuestionID
							.getInt("SurveyQuestionID");
					if (surveyQuestionIDLocal == -1) {

						String subQuestionID = itemQuestionForSubQuestionID
								.getString("SurveyQuestionSubID");
						if (subQuestionID.startsWith(surveyQuestionID + "_")) {
							JSONObject repeateResponse = new JSONObject();
							repeateResponse.put("UserResponse",
									itemQuestionForSubQuestionID
											.getString("UserResponse"));
							repeateResponse.put("SurveyQuestionRepeaterID1",
									subQuestionID);
							repeateResponse.put("SurveyQuestionRepeaterID",
									counter);
							surveyRepeatResponses.put(repeateResponse);
							counter++;
						}
					}
				}

				itemJsonQuestion.put("SurveyRepeatResponses",
						surveyRepeatResponses);

			}
		}
		for (int i = 0; i < questionsJSONArray.length(); i++) {

			JSONObject itemJsonQuestion = questionsJSONArray.getJSONObject(i);
			if (itemJsonQuestion.getInt("SurveyQuestionID") != -1) {
				questionResponseJSONForAllActivity.put(itemJsonQuestion);
			}
		}

	}

	private void callWebServiceForComptition(
			final ActivityDataMasterModel activityDataMasterModel,
			final int optionType) {

		try {
			long activityID = activityDataMasterModel.getActivityID();
			String webServiceMethod = "SubmitCompetitionBooked";
			/*
			 * JSONArray data = DatabaseHelper.getConnection(mContext)
			 * .getCounterShareDisplayShareRespose(activityID, optionType);
			 */

			Cursor cursor = mContext.getContentResolver().query(
					ProviderContract.URI_COUTER_SHARE_DISPLAY_SHARE_RESPONSE,
					null,
					null,
					new String[] { String.valueOf(activityID),
							String.valueOf(optionType) }, null);

			JSONArray data = DatabaseUtilMethods
					.getCounterShareDisplayShareJSONResponseFromCursor(cursor);

			JSONObject jsonObject = new JSONObject();
			jsonObject.putOpt("competitions", data);

			long[] mActivityIDs = new long[] { activityID };

			startService(mActivityIDs, webServiceMethod, jsonObject, false,
					activityDataMasterModel.getModuleName() + "["
							+ activityDataMasterModel.getStoreName() + "]");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

	}

	private void callWebServiceForOrder(
			ActivityDataMasterModel activityDataMasterModel, int orderType) {
		try {
			long mActivityID = activityDataMasterModel.getActivityID();

			String webServiceMethod = "SubmitOrderBooking";

			/*
			 * JSONArray data = DatabaseHelper.getConnection(mContext)
			 * .getOrderRespose(mActivityID, orderType, false);
			 */

			Cursor cursor = mContext.getContentResolver().query(
					ProviderContract.STOCK_ESCALATION_ORDER_RESPONSE_URI,
					null,
					null,
					new String[] { String.valueOf(mActivityID),
							String.valueOf(orderType) }, null);

			JSONArray data = DatabaseUtilMethods.getOrderResposeJSONFormCursor(
					cursor, false);

			JSONObject jsonObject = new JSONObject();
			jsonObject.putOpt("orders", data);

			long[] mActivityIDs = new long[] { mActivityID };

			String serviceName = "";

			switch (orderType) {
			case 1:
				serviceName = "Stock Escalation";
				break;
			case 2:
				serviceName = "Order Booking";
				break;
			case 3:
				serviceName = "Sellout Projection";
				break;

			default:
				serviceName = "";
				break;
			}
			startService(mActivityIDs, webServiceMethod, jsonObject, false,
					activityDataMasterModel.getModuleName() + "["
							+ activityDataMasterModel.getStoreName() + "]");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

	}

	private void startService(long[] mActivityID, String webMethodName,
			JSONObject jsonObject, boolean isImageType, String syncServiceTag) {

		String confugrationURL = mContext.getString(R.string.url)
				+ webMethodName;

		Intent service = new Intent(mContext, SyncService.class);
		service.putExtra(SyncUtils.ACTIVITY_ID, mActivityID);
		service.putExtra(SyncUtils.CONFUGRATION_URL, confugrationURL);
		service.putExtra(SyncUtils.REQUEST_STRING, jsonObject.toString());
		service.putExtra(SyncUtils.REQUEST_STRING_IS_IMAGE_TYPE, isImageType);
		service.putExtra(SyncUtils.SYNC_SERVICE, syncServiceTag);
		mContext.startService(service);
	}

	private void callWebServiceForQuestionData1(long[] activityIDs,
			JSONArray questionResponseJSONForAllActivity) {

		HashMap<String, JSONArray> map = getQuestionResponseForActivityForTextAndImage(questionResponseJSONForAllActivity);

		try {
			JSONObject requestString = new JSONObject();

			JSONArray questionResponseJSONText = map
					.get("QUESTION_RESPONSE_TEXT");

			JSONArray questionResponseJSONImage = map
					.get("QUESTION_RESPONSE_IMAGE");

			requestString.put("activities", questionResponseJSONText);
			requestString.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_USERID)));

			String webServiceMethod = "SaveSurveyUserResponse";

			if (questionResponseJSONForAllActivity.length() > 0) {
				startService(activityIDs, webServiceMethod, requestString,
						false, "Question Data Text");
			}
			if (questionResponseJSONImage.length() > 0) {
				sendImageQuestionData1(activityIDs, questionResponseJSONImage);
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void sendImageQuestionData1(long[] activityIDs,
			JSONArray questionResponseJSONImage) {
		try {
			ArrayList<UploadImageMultipartDataModal> imageUploadDataImages = new ArrayList<UploadImageMultipartDataModal>();
			for (int i = 0; i < questionResponseJSONImage.length(); i++) {

				JSONObject questionJ = questionResponseJSONImage
						.getJSONObject(i);

				int questionTypeID = questionJ.getInt("QuestionTypeID");
				UploadImageMultipartDataModal object = new UploadImageMultipartDataModal();
				if (questionTypeID == QuestionType.SURVEY_QUESTION_TYPE_REPEATOR) {

					object.setSurveyQuestionId(questionJ
							.getInt(WebConfig.WebParams.SURVEYQUESTIONID));

					object.setSurveyResponseId(questionJ
							.getInt(WebConfig.WebParams.SURVEYRESPONSEID));
					object.setUserId(Long.valueOf(Helper
							.getStringValuefromPrefs(mContext,
									SharedPreferencesKey.PREF_USERID)));

					object.setType(2);

					object.setLatitude(-1);
					object.setLongitude(-1);
					object.setStoreID(-1);
					object.setUserOption("");

					object.setUserResponse(questionJ
							.getString(WebConfig.WebParams.USERRESPONSE));

					JSONArray surveyRepeatResponses = questionJ
							.getJSONArray("SurveyRepeatResponses");
					ArrayList<UploadMuliplePartImageRepeaterDataModal> arrayListMulitpleRepeater = new ArrayList<UploadMuliplePartImageRepeaterDataModal>();
					for (int j = 0; j < surveyRepeatResponses.length(); j++) {
						JSONObject jsonObject = surveyRepeatResponses
								.getJSONObject(j);
						UploadMuliplePartImageRepeaterDataModal item = new UploadMuliplePartImageRepeaterDataModal();
						item.setImagePath(jsonObject.getString("UserResponse"));
						item.setImageSubID(jsonObject
								.getString("SurveyQuestionRepeaterID"));
						arrayListMulitpleRepeater.add(item);
					}

					object.setArrList(arrayListMulitpleRepeater);

				} else {

					object.setSurveyQuestionId(questionJ
							.getInt(WebConfig.WebParams.SURVEYQUESTIONID));
					object.setSurveyResponseId(questionJ
							.getInt(WebConfig.WebParams.SURVEYRESPONSEID));
					object.setUserId(Long.valueOf(Helper
							.getStringValuefromPrefs(mContext,
									SharedPreferencesKey.PREF_USERID)));
					object.setType(1);

					object.setLatitude(-1);
					object.setLongitude(-1);
					object.setStoreID(-1);
					object.setUserOption("");

					object.setUserResponse(questionJ
							.getString(WebConfig.WebParams.USERRESPONSE));
				}

				imageUploadDataImages.add(object);
			}

			String serviceURL = mContext.getString(R.string.url)
					+ "UploadImageStream";

			Intent service = new Intent(mContext, SyncService.class);
			service.putExtra(SyncUtils.ACTIVITY_ID, activityIDs);
			service.putExtra(SyncUtils.CONFUGRATION_URL, serviceURL);
			service.putExtra(SyncUtils.REQUEST_STRING, imageUploadDataImages);
			service.putExtra(SyncUtils.REQUEST_STRING_IS_IMAGE_TYPE, true);
			service.putExtra(SyncUtils.SYNC_SERVICE, "Question Data Image");
			mContext.startService(service);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private HashMap<String, JSONArray> getQuestionResponseForActivityForTextAndImage(
			JSONArray questionResponseJSONForAllActivity) {

		try {
			JSONArray questionJsonArrayText = new JSONArray();
			JSONArray questionJsonArrayImage = new JSONArray();

			int count = questionResponseJSONForAllActivity.length();

			for (int i = 0; i < count; i++) {

				JSONObject itemJSON = questionResponseJSONForAllActivity
						.getJSONObject(i);

				if (itemJSON.getInt("QuestionTypeID") == QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX) {
					questionJsonArrayImage.put(itemJSON);
				} else if (itemJSON.getInt("QuestionTypeID") == QuestionType.SURVEY_QUESTION_TYPE_REPEATOR) {

					if (itemJSON.getInt("RepeaterTypeID") == QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX) {
						questionJsonArrayImage.put(itemJSON);
					} else {
						questionJsonArrayText.put(itemJSON);
					}
				} else {
					questionJsonArrayText.put(itemJSON);
				}

			}

			HashMap<String, JSONArray> map = new HashMap<String, JSONArray>();
			map.put("QUESTION_RESPONSE_TEXT", questionJsonArrayText);
			map.put("QUESTION_RESPONSE_IMAGE", questionJsonArrayImage);

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	private void callWebServiceForQuestionData(long[] activityIDs,
			JSONArray questionResponseJSONText,
			JSONArray questionResponseJSONImage) {

		try {
			JSONObject requestString = new JSONObject();

			requestString.put("activities", questionResponseJSONText);
			requestString.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(mContext,
							SharedPreferencesKey.PREF_USERID)));

			String webServiceMethod = "SaveSurveyUserResponse";

			if (questionResponseJSONText.length() > 0) {
				startService(activityIDs, webServiceMethod, requestString,
						false, "Question Data Text");
			}
			if (questionResponseJSONImage.length() > 0) {
				sendImageQuestionData(activityIDs, questionResponseJSONImage);
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Calls service to send Questionnaire image data to serviceSend
	 * questionnaire image data to server
	 * 
	 * @param activityIDs
	 *            activity ids of the data
	 * @param questionResponseJSONImage
	 *            send Questionnaire image data
	 */

	private void sendImageQuestionData(long[] activityIDs,
			JSONArray questionResponseJSONImage) {
		try {
			ArrayList<UploadImageMultipartDataModal> imageUploadDataImages = new ArrayList<UploadImageMultipartDataModal>();
			for (int i = 0; i < questionResponseJSONImage.length(); i++) {
				JSONObject questionJ = questionResponseJSONImage
						.getJSONObject(i);
				UploadImageMultipartDataModal object = new UploadImageMultipartDataModal();
				object.setSurveyQuestionId(questionJ
						.getInt(WebConfig.WebParams.SURVEYQUESTIONID));
				object.setSurveyResponseId(questionJ
						.getInt(WebConfig.WebParams.SURVEYRESPONSEID));
				object.setUserId(Long.valueOf(Helper.getStringValuefromPrefs(
						mContext, SharedPreferencesKey.PREF_USERID)));
				object.setType(1);

				object.setLatitude(-1);
				object.setLongitude(-1);
				object.setStoreID(-1);
				object.setUserOption("");

				object.setUserResponse(questionJ
						.getString(WebConfig.WebParams.USERRESPONSE));
				imageUploadDataImages.add(object);
			}

			String serviceURL = mContext.getString(R.string.url)
					+ "UploadImageStream";

			Intent service = new Intent(mContext, SyncService.class);
			service.putExtra(SyncUtils.ACTIVITY_ID, activityIDs);
			service.putExtra(SyncUtils.CONFUGRATION_URL, serviceURL);
			service.putExtra(SyncUtils.REQUEST_STRING, imageUploadDataImages);
			service.putExtra(SyncUtils.REQUEST_STRING_IS_IMAGE_TYPE, true);
			service.putExtra(SyncUtils.SYNC_SERVICE, "Question Data Image");
			mContext.startService(service);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This receives the broadcast data send by {@link SyncService}
	 */
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

		SyncStatusNotificationModel syscStatusModel = null;

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			if (action.equalsIgnoreCase(SyncUtils.SYNC_ACTION_UPDATE)) {

				String serviceName = intent.getExtras().getString(
						SyncUtils.SYNC_SERVICE);
				int syncStatusCode = intent.getExtras().getInt(
						SyncUtils.SYNC_STATUS);
				switch (syncStatusCode) {

				case SyncUtils.SYNC_STATUS_ERROR:

					if (syscStatusModel != null) {
						syscStatusModel.setModuleName(serviceName);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
						syncListAdapter.addItem(syscStatusModel);
					}
					break;
				case SyncUtils.SYNC_STATUS_SYNC_COMPLETED:

					if (syscStatusModel != null) {
						syscStatusModel.setModuleName(serviceName);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);
						syncListAdapter.addItem(syscStatusModel);
					}

					break;

				default:
					break;
				}
			} else if (action.equalsIgnoreCase(SyncUtils.SYNC_ACTION_START)) {

				String serviceName = intent.getExtras().getString(
						SyncUtils.SYNC_SERVICE);

				syscStatusModel = new SyncStatusNotificationModel();
				syscStatusModel.setModuleName(serviceName);
				syscStatusModel.setStatus(SyncUtils.SYNC_STARTED);
				syncListAdapter.addItem(syscStatusModel);

			} else if (action.equalsIgnoreCase(SyncUtils.SYNC_ACTION_COMPLETED)) {

				String serviceName = intent.getExtras().getString(
						SyncUtils.SYNC_SERVICE);

				syncListDialog.setEnableConfirmButton(true);
				LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
						mMessageReceiver);

				Helper.showCustomToast(context,
						R.string.sync_process_completed, Toast.LENGTH_LONG);

				if (shouldClearDataAfterSync) {

					DatabaseHelper.getConnection(context)
							.clearActivityDataAndAllResponceTableData();

					DatabaseHelper.getConnection(context)
							.deleteStoreAssessment();

				}

			}

		}
	};

}
