package com.samsung.ssc.download;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.samsung.ssc.constants.DownloadMasterCodes;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataMasterColumns;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class DownloadMasterInsertTableData
		implements
		DownloadMasterInsert<Context, JSONObject, Entry<Character, String>, Boolean> {

	@Override
	public void insertData(Context context, JSONObject singleResultData,
			Entry<Character, String> entry, Boolean status) {

		if (singleResultData != null) {

			ContentResolver contentResolver = context.getContentResolver();

			switch (entry.getKey()) {
			case DownloadMasterCodes.USER_MODULES:
				try {

					JSONArray jArray = singleResultData.getJSONArray("Result");

					ContentValues[] contentValues = DatabaseUtilMethods
							.getContetnValueUserModulesFromJSON(jArray);

					contentResolver.bulkInsert(
							ProviderContract.USER_MODULES_URI, contentValues);

					String where = "ModuleID NOT IN (SELECT UMM.ModuleID FROM UserModuleMaster UMM WHERE UMM.IsMandatory = ? )";
					contentResolver.delete(
							ProviderContract.URI_STORE_MODULE_MASTER, where,
							new String[] { "1" });

				} catch (JSONException e) {

				}
				break;

			case DownloadMasterCodes.OTHER_BEAT:

				try {
					JSONArray jsonArray = singleResultData
							.getJSONArray("Result");
					if (jsonArray != null) {

						ContentValues[] contentValues = DatabaseUtilMethods
								.getContentValuesStoresBaic(jsonArray,
										Constants.STORE_TYPE_OTHER, null,
										context);

						contentResolver.bulkInsert(
								ProviderContract.URI_STORES_BASICS,
								contentValues);

					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				break;

			case DownloadMasterCodes.PRODUCT_LIST:

				try {
					JSONArray productArray = singleResultData
							.getJSONArray("Result");

					ContentValues[] contentValues = DatabaseUtilMethods
							.getProductsContentValuesFromJson(productArray);

					if (contentValues != null) {
						contentResolver.bulkInsert(
								ProviderContract.URI_PRODUCTS, contentValues);
					}
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.PAYMENT_MODE:

				try {
					JSONArray jsonArray = singleResultData
							.getJSONArray("Result");
					ContentValues contentValues[] = DatabaseUtilMethods
							.getsContentValuesPaymentModseFromJson(jsonArray);

					if (contentValues != null) {
						contentResolver.bulkInsert(
								ProviderContract.URI_PAYMENT_MODES,
								contentValues);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;

			case DownloadMasterCodes.COMPETITOR:

				try {

					JSONArray jsonArray = singleResultData
							.getJSONArray("Result");
					ContentValues contentValues[] = DatabaseUtilMethods
							.getContentValuesCompetitorsFromJson(jsonArray);

					if (contentValues != null) {
						contentResolver
								.bulkInsert(ProviderContract.URI_COMPETITORS,
										contentValues);
					}

				} catch (Exception e) {
					Helper.printStackTrace(e);
				}

				break;

			case DownloadMasterCodes.COMPETITION_PRODUCT_GROUP:

				try {
					JSONArray compProductGroupArray = singleResultData
							.getJSONArray("Result");
					ContentValues contentValues[] = DatabaseUtilMethods
							.getContentValuesCompProductGroupFromJson(compProductGroupArray);

					if (contentValues != null) {
						contentResolver.bulkInsert(
								ProviderContract.URI_COMP_PRODUCT_GROUP,
								contentValues);
					}

				} catch (JSONException e1) {
					Helper.printStackTrace(e1);
				}

				break;

			case DownloadMasterCodes.SURVEY_QUESTIONS:
				try {
					JSONArray surveyQuestionObj = singleResultData
							.getJSONArray("Result");

					List<ContentValues[]> list_arr_ContentValues = DatabaseUtilMethods
							.getSurveyQuestionData(surveyQuestionObj);

					contentResolver.bulkInsert(
							ProviderContract.SURVEY_QUESTION_CONTENT_URI,
							list_arr_ContentValues.get(0));

					contentResolver
							.bulkInsert(
									ProviderContract.SURVEY_QUESTION_OPTIONS_CONTENT_URI,
									list_arr_ContentValues.get(1));

				} catch (Exception e) {

				}

			case DownloadMasterCodes.PLANOGRAM_CLASS:

				try {
					JSONArray planogramClassObj = singleResultData
							.getJSONArray("Result");
					ContentValues contentValues[] = DatabaseUtilMethods
							.getContentValuesPalnogramClassFromJson(planogramClassObj);

					if (contentValues != null) {
						contentResolver.bulkInsert(
								ProviderContract.URI_PALNOGRAM_CLASS,
								contentValues);
					}

				} catch (Exception e) {

				}

				break;

			case DownloadMasterCodes.PLANOGRAM_PRODUCT:

				try {
					JSONArray planogramProductObj = singleResultData
							.getJSONArray("Result");
					ContentValues[] contentValues = DatabaseUtilMethods
							.getContentValuesPlanogramProduct1(planogramProductObj);

					if (contentValues != null) {
						contentResolver.bulkInsert(
								ProviderContract.URI_PALNOGRAM_PRODUCT,
								contentValues);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				break;

			case DownloadMasterCodes.COMPETITOR_PRODUCT_GROUP_MAPPING:

				try {

					JSONArray comProductGroupMaping = singleResultData
							.getJSONArray("Result");
					ContentValues[] contentValues = DatabaseUtilMethods
							.getContentValuesCompetitorProductGroupMappingFromJson(comProductGroupMaping);

					if (contentValues != null) {
						contentResolver
								.bulkInsert(
										ProviderContract.URI_COMPETITOR_PRODUCT_GROUP_MAPPING,
										contentValues);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				break;

			case DownloadMasterCodes.MSS_CATEGORY:

				try {
					JSONArray rootJSONArrayCategory = singleResultData
							.getJSONArray("Result");
					ContentValues[] listCategory = DatabaseUtilMethods
							.getContentValueMSSCategory(rootJSONArrayCategory);
					contentResolver.bulkInsert(
							ProviderContract.URI_FEEDBACK_CATEGORY,
							listCategory);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.MSS_TEAM:

				try {
					JSONArray rootJSONArrayTeam = singleResultData
							.getJSONArray("Result");
					ContentValues[] listTeam = DatabaseUtilMethods
							.getContentValueMSSTeam(rootJSONArrayTeam);
					contentResolver.bulkInsert(
							ProviderContract.URI_FEEDBACK_TEAMS, listTeam);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.MSS_TYPE:

				try {
					JSONArray rootJSONArrayType = singleResultData
							.getJSONArray("Result");
					ContentValues[] listType = DatabaseUtilMethods
							.getContentValueMSSType(rootJSONArrayType);
					contentResolver.bulkInsert(
							ProviderContract.URI_FEEDBACK_TYPE, listType);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.MSS_STATUS:

				try {
					JSONArray rootJSONArrayStatus = singleResultData
							.getJSONArray("Result");
					ContentValues[] listStatus = DatabaseUtilMethods
							.getContentValueMSSStatus(rootJSONArrayStatus);
					contentResolver.bulkInsert(
							ProviderContract.URI_FEEDBACK_STATUS, listStatus);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.EOL_SCHEMES:

				try {

					JSONArray eolSchemeJsonArrayObj = singleResultData
							.getJSONArray("Result");
					ArrayList<ContentValues[]> eolList = DatabaseUtilMethods
							.getContentValuesEOLSchemeDetailAndHeaderFromJson(eolSchemeJsonArrayObj);

					contentResolver.bulkInsert(
							ProviderContract.URI_EOL_SCHEME_HEADER,
							eolList.get(0));

					contentResolver.bulkInsert(
							ProviderContract.URI_EOL_SCHEME_DETAIL,
							eolList.get(1));

				} catch (Exception e) {
				}

				break;

			case DownloadMasterCodes.RACE_POSM_MASTER:

				try {
					JSONArray jsonPOSMArray = singleResultData
							.getJSONArray("Result");

					contentResolver
							.bulkInsert(
									ProviderContract.URI_RACE_POSM,
									DatabaseUtilMethods
											.getPosmMasterContentValuesFromJson(jsonPOSMArray));

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;

			case DownloadMasterCodes.RACE_FIXTURE_MASTER:

				try {
					JSONArray rootJSONArrayFixtureMaster = singleResultData
							.getJSONArray("Result");
					ContentValues[] listRaceFixure = DatabaseUtilMethods
							.getFixtureMasterContentValuesFromJson(rootJSONArrayFixtureMaster);
					contentResolver.bulkInsert(
							ProviderContract.URI_RACE_FIXTURE, listRaceFixure);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.RACE_POSM_PRODUCT_MAPPING:

				try {
					JSONArray rootJSONArrayProductMapping = singleResultData
							.getJSONArray("Result");
					ContentValues[] listProductMapping = DatabaseUtilMethods
							.getPOSMProductMappingMasterContentValuesFromJson(rootJSONArrayProductMapping);
					contentResolver.bulkInsert(
							ProviderContract.URI_RACE_POSM_PRODUCT_MAPPING,
							listProductMapping);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.RACE_BRAND_MASTER:

				try {
					JSONArray rootJSONArrayBrand = singleResultData
							.getJSONArray("Result");
					ContentValues[] listBrand = DatabaseUtilMethods
							.getBrandMasterContentValuesFromJson(rootJSONArrayBrand);
					contentResolver.bulkInsert(ProviderContract.URI_RACE_BRAND,
							listBrand);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.RACE_PRODUCT_CATEGORY:

				try {
					JSONArray rootJSONArrayProductCategory = singleResultData
							.getJSONArray("Result");
					ContentValues[] listProductCateogory = DatabaseUtilMethods
							.getBrandProductCategoryMasterContentValuesFromJson(rootJSONArrayProductCategory);
					contentResolver.bulkInsert(
							ProviderContract.URI_RACE_PRODUCT_CATEGORY,
							listProductCateogory);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.RACE_BRAND_CATEGORY_MAPPING:

				try {
					JSONArray rootJSONArrayBrandCategory = singleResultData
							.getJSONArray("Result");
					ContentValues[] listBrandCategory = DatabaseUtilMethods
							.getBrandCategorymappingMasterContentValuesFromJson(rootJSONArrayBrandCategory);
					contentResolver.bulkInsert(
							ProviderContract.URI_RACE_BRAND_CATEGORY_MAPPING,
							listBrandCategory);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				break;

			case DownloadMasterCodes.RACE_PRODUCT:

				try {
				/*	JSONObject jsonResponseJsonArray = singleResultData
							.getJSONObject("Result");
					// JSONObject jsonResponseJson =
					// singleResultData.getJSONObject("SingleResult");
					JSONArray jsonResponseProductArray = jsonResponseJsonArray
							.getJSONArray("Products");*/
					JSONArray jsonResponseJsonArray = singleResultData
							.getJSONArray("Result");
					if (jsonResponseJsonArray != null) {

						/*
						 * DatabaseHelper.getConnection(context)
						 * .insertRaceProductMaster( jsonResponseProductArray);
						 */

						contentResolver
								.bulkInsert(
										ProviderContract.URI_RACE_BRAND_PRODUCT,
										DatabaseUtilMethods
												.getProductMasterContentValuesFromJson(jsonResponseJsonArray));
					}

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;

				case DownloadMasterCodes.EXPENSE_TYPE_MASTER:

					try {
						JSONArray expenseTypeMasterArray = singleResultData
								.getJSONArray("Result");

						ContentValues[] contentValues = DatabaseUtilMethods
								.getExpenseTypeMasterContentValuesFromJson(expenseTypeMasterArray);

						if (contentValues != null) {
							contentResolver.bulkInsert(
									ProviderContract.URI_EXPENSE_TYPE_MASTER, contentValues);
						}
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					break;



				default:
				break;
			}

			try {
				String maxModifiedDate = singleResultData
						.isNull("MaxModifiedDate") ? null : singleResultData
						.getString("MaxModifiedDate");
				try {
					if (maxModifiedDate != null) {
						updateStatusDownloadDataMaster(context, entry.getKey(),
								maxModifiedDate, status);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	private void storeInDB(Context context, JSONArray surveyQuestionObj) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateStatusDownloadDataMaster(Context context, char dataCode,
			String lastUpdatedDate, boolean status) {

		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put(
					DownloadDataMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP,
					lastUpdatedDate);
			initialValues
					.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
							status);

			String whereClause = DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE
					+ "=?";
			String[] whereArgs = new String[] { String.valueOf(dataCode) };

			context.getContentResolver().update(
					ProviderContract.URI_DOWNLOAD_DATA_SINGLE_SERVICE,
					initialValues, whereClause, whereArgs);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
