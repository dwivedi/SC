package com.samsung.ssc.download;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.samsung.ssc.R;
import com.samsung.ssc.constants.DownloadMasterCodes;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.PostToNetwork;
import com.samsung.ssc.sync.adapter.SyncAdapter;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map.Entry;

public class DownloadMasterGetTableData
		implements
		DownloadMasterTable<Context, JSONObject, Entry<Character, String>, Bundle> {

	DownloadMasterInsert<Context, JSONObject, Entry<Character, String>, Boolean> insert = new DownloadMasterInsertTableData();

	@Override
	public JSONObject getData(Context context, Entry<Character, String> entry,
			Bundle bundle) {

		boolean hasMoreRow = false;
		int count = 0;
		String maxModifiedDateTemp = "";
		try {
			Log.e("sync adapter", entry.getKey()
					+ " modules Downloading started");

			RequestData requestData = getRequestParms(context, entry, bundle);
			Log.e("sync adapter", entry.getKey() + " modules request string "
					+ requestData.parms);
			do {
				// Communication with Service
				String value = PostToNetwork.postMethod(requestData.url,
						requestData.parms, context, bundle);

				ResponseDto responseModal = new FetchingdataParser(context)
						.getResponseResult(value);

				if (responseModal.isSuccess()) {
					JSONObject singleResultData = new JSONObject(responseModal
							.getSingleResult());
					String maxModifiedDate = singleResultData
							.isNull("MaxModifiedDate") ? null
							: singleResultData.getString("MaxModifiedDate");
					hasMoreRow = singleResultData.getBoolean("HasMoreRows");

					insert.insertData(context, singleResultData, entry,
							responseModal.isSuccess());

					if (hasMoreRow) {
						if (maxModifiedDate == null)
							count = count + Constants.ROW_COUNT_DOWNLOAD_DATA;

						else if (maxModifiedDateTemp.equals(maxModifiedDate))
							count = count + Constants.ROW_COUNT_DOWNLOAD_DATA;

						else
							count = 0;

						requestData.parms.put(
								WebConfig.WebParams.START_ROW_INDEX, count);
						requestData.parms.put(
								WebConfig.WebParams.LAST_UPDATED_DATE,
								maxModifiedDate == null ? JSONObject.NULL
										: maxModifiedDate);
						maxModifiedDateTemp = maxModifiedDate == null ? ""
								: maxModifiedDate;

					}

				} else {
					hasMoreRow = false;
				}

				Log.e("sync adapter", entry.getKey()
						+ " modules called, has more rows " + hasMoreRow
						+ " and is success is " + responseModal.isSuccess());

			} while (hasMoreRow&&!SyncAdapter.isSyncStopped);
		} catch (Exception e) {
			Log.e("sync adapter", entry.getKey()
					+ " modules called, has exception " + e.toString());
		}

		Log.e("sync adapter", entry.getKey() + " modules End");
		return null;
	}

	private RequestData getRequestParms(Context context,
			Entry<Character, String> entry, Bundle bundle) throws JSONException {

		JSONObject jsonObjectsParems = new JSONObject();

		/*
		 * jsonObjectsParems.put(WebConfig.WebParams.USER_ID, Helper
		 * .getStringValuefromPrefs(context, SharedPreferencesKey.PREF_USERID));
		 */

		jsonObjectsParems.put(WebConfig.WebParams.USER_ID,
				bundle.getString(SharedPreferencesKey.PREF_USERID));

		jsonObjectsParems.put(WebConfig.WebParams.ROW_COUNT,
				com.samsung.ssc.util.Constants.ROW_COUNT_DOWNLOAD_DATA);

		jsonObjectsParems.put(WebConfig.WebParams.START_ROW_INDEX, 0);

		jsonObjectsParems.put(WebConfig.WebParams.LAST_UPDATED_DATE,
				entry.getValue() == null ? JSONObject.NULL : entry.getValue());

		StringBuilder url = new StringBuilder(context.getString(R.string.url));

		Character code = entry.getKey();

		switch (code) {
		case DownloadMasterCodes.USER_MODULES:

			/*
			 * jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID_CAPS, Long
			 * .valueOf(Helper.getIntValueFromPrefs(context,
			 * SharedPreferencesKey.PREF_ROLEID)));
			 */

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID_CAPS, Long
					.valueOf(bundle.getInt(SharedPreferencesKey.PREF_ROLEID)));

			url.append(WebConfig.WebMethod.GET_USER_MODULES);
			break;
		case DownloadMasterCodes.OTHER_BEAT:

			/*
			 * jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Helper
			 * .getIntValueFromPrefs(context,
			 * SharedPreferencesKey.PREF_ROLEID));
			 */

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID,
					bundle.getInt(SharedPreferencesKey.PREF_ROLEID));

			url.append(WebConfig.WebMethod.DEALERS_LIST_BASED_ON_CITY);

			break;
		case DownloadMasterCodes.PAYMENT_MODE:

			/*
			 * jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper
			 * .getStringValuefromPrefs(context,
			 * SharedPreferencesKey.PREF_COMPANYID));
			 */

			jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID,
					bundle.getString(SharedPreferencesKey.PREF_COMPANYID));

			url.append(WebConfig.WebMethod.GET_PAYMENT_MODES);

			break;

		case DownloadMasterCodes.PRODUCT_LIST:

			/*
			 * jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper
			 * .getStringValuefromPrefs(context,
			 * SharedPreferencesKey.PREF_COMPANYID));
			 */

			jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID,
					bundle.getString(SharedPreferencesKey.PREF_COMPANYID));

			url.append(WebConfig.WebMethod.GET_PRODUCT_LIST);

			break;

		case DownloadMasterCodes.COMPETITOR:

			/*
			 * jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper
			 * .getStringValuefromPrefs(context,
			 * SharedPreferencesKey.PREF_COMPANYID));
			 */

			jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID,
					bundle.getString(SharedPreferencesKey.PREF_COMPANYID));

			url.append(WebConfig.WebMethod.GET_COMPETITORS);

			break;

		case DownloadMasterCodes.COMPETITION_PRODUCT_GROUP:

			/*
			 * jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper
			 * .getStringValuefromPrefs(context,
			 * SharedPreferencesKey.PREF_COMPANYID));
			 */

			jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID,
					bundle.getString(SharedPreferencesKey.PREF_COMPANYID));

			url.append(WebConfig.WebMethod.GET_COMPETITION_PRODUCT_GROUP);
			break;

		case DownloadMasterCodes.SURVEY_QUESTIONS:

			/*
			 * jsonObjectsParems.put(WebConfig.WebParams.USERROLEID, Helper
			 * .getStringValuefromPrefs(context,
			 * SharedPreferencesKey.PREF_ROLEIDSTOREWISE));
			 */

			
			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID_CAPS, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			
			jsonObjectsParems
					.put(WebConfig.WebParams.USERROLEID,
							bundle.getString(SharedPreferencesKey.PREF_ROLEIDSTOREWISE));

			url.append(WebConfig.WebMethod.GET_SURVEY_QUESTIONS);

			break;

		case DownloadMasterCodes.PLANOGRAM_CLASS:

			jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper
					.getStringValuefromPrefs(context,
							SharedPreferencesKey.PREF_COMPANYID));

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_PLANOGRAM_CLASS_MASTERS);

			break;
		case DownloadMasterCodes.PLANOGRAM_PRODUCT:

			jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper
					.getStringValuefromPrefs(context,
							SharedPreferencesKey.PREF_COMPANYID));

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_PLANOGRAM_PRODUCT_MASTERS);

			break;
		case DownloadMasterCodes.COMPETITOR_PRODUCT_GROUP_MAPPING:

			jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper
					.getStringValuefromPrefs(context,
							SharedPreferencesKey.PREF_COMPANYID));

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));

			url.append(WebConfig.WebMethod.GET_COMPETITOR_PRODUCT_GROUP_MAPPING);

			break;

		case DownloadMasterCodes.MSS_CATEGORY:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Helper
					.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID));

			url.append(WebConfig.WebMethod.GET_MSS_CATEGORY_MASTER);

			break;
		case DownloadMasterCodes.MSS_TEAM:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Helper
					.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID));

			url.append(WebConfig.WebMethod.GET_MSS_TEAM_MASTER);

			break;
		case DownloadMasterCodes.MSS_TYPE:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Helper
					.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID));

			url.append(WebConfig.WebMethod.GET_MSS_TYPE_MASTER);

			break;
		case DownloadMasterCodes.MSS_STATUS:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Helper
					.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID));

			url.append(WebConfig.WebMethod.GET_MSS_STATUS_MASTER);

			break;
		case DownloadMasterCodes.EOL_SCHEMES:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));

			
			
			url.append(WebConfig.WebMethod.GET_EOL_SCHEMES);
			break;

		case DownloadMasterCodes.RACE_POSM_MASTER:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_RACE_POSM_MASTERS);

			break;

		case DownloadMasterCodes.RACE_FIXTURE_MASTER:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_RACE_FIXTURE_MASTER);

			break;

		case DownloadMasterCodes.RACE_POSM_PRODUCT_MAPPING:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_RACE_POSM_PRODUCT_MAPPING);

			break;

		case DownloadMasterCodes.RACE_BRAND_MASTER:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_RACE_BRAND_MASTER);

			break;

		case DownloadMasterCodes.RACE_PRODUCT_CATEGORY:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_RACE_PRODUCT_CATEGORY);

			break;

		case DownloadMasterCodes.RACE_BRAND_CATEGORY_MAPPING:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));
			url.append(WebConfig.WebMethod.GET_RACE_BRAND_CATEGORY_MAPPING);

			break;

		case DownloadMasterCodes.RACE_PRODUCT:

			jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long
					.valueOf(Helper.getIntValueFromPrefs(context,
							SharedPreferencesKey.PREF_ROLEID)));

			/*jsonObjectsParems.put(WebConfig.WebParams.LAST_CATEGORY_ID,
					Long.valueOf("-1"));
			jsonObjectsParems.put(WebConfig.WebParams.LAST_TYPE_ID,
					Long.valueOf("-1"));
			jsonObjectsParems.put(WebConfig.WebParams.ROW_COUNTER,
					Long.valueOf("10000"));*/

			url.append(WebConfig.WebMethod.GET_RACE_PRODUCT_MASTERS);
			break;

			case DownloadMasterCodes.EXPENSE_TYPE_MASTER:

				jsonObjectsParems.put(WebConfig.WebParams.ROLE_ID, Long.valueOf(Helper.getIntValueFromPrefs(context,SharedPreferencesKey.PREF_ROLEID)));
				jsonObjectsParems.put(WebConfig.WebParams.COMPANY_ID, Helper.getStringValuefromPrefs(context,SharedPreferencesKey.PREF_COMPANYID));
				url.append(WebConfig.WebMethod.GET_EXPENCE_MASTER_DATA);

				break;

		default:
			break;
		}

		return new RequestData(url.toString(), jsonObjectsParems);

	}

	class RequestData {
		public RequestData(String url, JSONObject jsonobj) {
			this.url = url;
			this.parms = jsonobj;
		}

		String url;
		JSONObject parms;
	}
}
