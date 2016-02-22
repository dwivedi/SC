package com.samsung.ssc.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.samsung.ssc.constants.DownloadCode;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataMasterColumns;
import com.samsung.ssc.dto.GetPlanogramProductMasterSingleResult;
import com.samsung.ssc.dto.GetPlanogramProductResponse;
import com.samsung.ssc.dto.PlanogramProductDataModal;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleySingleton;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncStatusNotificationModel;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

public class DownloadDataLooper {

	private Handler backgroundHandler, uIHandler;
	private Context context;
	private Handler handlerLooper;

	public DownloadDataLooper(Handler uIHandler, Context context,
			Handler handlerLooper) {

		this.uIHandler = uIHandler;
		this.context = context;
		this.handlerLooper = handlerLooper;
		this.backgroundThread.start();

	}

	Thread backgroundThread = new Thread() {

		@Override
		public void run() {
			try {
				// preparing a looper on current thread
				// the current thread is being detected implicitly
				Looper.prepare();

				// now, the handler will automatically bind to the
				// Looper that is attached to the current thread
				// You don't need to specify the Looper explicitly
				backgroundHandler = new Handler();

				// notify the ui handlerLooper to start add new task

				Message message = handlerLooper.obtainMessage();
				Bundle data = new Bundle();
				data.putBoolean("LOOPER_PRAPERED", true);
				message.setData(data);

				handlerLooper.sendMessage(message);

				// After the following line the thread will start
				// running the message loop and will not normally
				// exit the loop unless a problem happens or you
				// quit() the looper (see below)
				Looper.loop();
			} catch (Throwable t) {

			}
		}
	};

	public void setTask(final JSONObject json, final String url,
			final String serviceName,
			final SyncStatusNotificationModel syscStatusModel) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				String result = null;

				try {
					result = postMethod(url, json);

				} catch (Exception e) {
					result = null;
				}

				databaseInsertion(serviceName, result, syscStatusModel);
			}

		};

		try {
			backgroundHandler.post(runnable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void databaseInsertion(final String serviceName, String result,
			SyncStatusNotificationModel syscStatusModel) {

		Message messge = uIHandler.obtainMessage();
		Bundle bundle = new Bundle();

		if (serviceName.equalsIgnoreCase(DownloadCode.PRODUCT_LIST)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						JSONArray productArray = new FetchingdataParser(context)
								.getProductListData(result);

						ContentValues[] contentValues = DatabaseUtilMethods
								.getProductsContentValuesFromJson(productArray);

						if (contentValues != null) {
							context.getContentResolver().bulkInsert(
									ProviderContract.URI_PRODUCTS,
									contentValues);
						}

						/*
						 * DatabaseHelper.getConnection(context)
						 * .insertProductMaster(productArray);
						 */

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.PRODUCT_LIST,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.PRODUCT_LIST,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name", "Download Products");
						bundle.putBoolean("is_success", true);

						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.PRODUCT_LIST, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.PRODUCT_LIST, false);

						bundle.putString("seriver_name", "Download Products");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block

					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.PRODUCT_LIST, false);
					 */

					updateStatusDownloadDataMaster(DownloadCode.PRODUCT_LIST,
							false);

					bundle.putString("seriver_name", "Download Products");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);

				}

			} else {

				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.PRODUCT_LIST, false);
				 */
				updateStatusDownloadDataMaster(DownloadCode.PRODUCT_LIST, false);

				bundle.putString("seriver_name", "Download Products");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		}

		else if (serviceName.equalsIgnoreCase(DownloadCode.PAYMENT_MODE)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						/*
						 * ArrayList<PaymentModes> paymentModeResult = new
						 * FetchingdataParser( context).getPaymentModes(result);
						 * 
						 * DatabaseHelper.getConnection(context)
						 * .insertPaymentModes(paymentModeResult);
						 */

						ContentValues contentValues[] = DatabaseUtilMethods
								.getsContentValuesPaymentModseFromJson(jsonObject
										.getJSONArray("Result"));

						if (contentValues != null) {
							context.getContentResolver().bulkInsert(
									ProviderContract.URI_PAYMENT_MODES,
									contentValues);
						}

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.PAYMENT_MODE,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.PAYMENT_MODE,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name", "Payment Modes");
						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.PAYMENT_MODE, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.PAYMENT_MODE, false);

						bundle.putString("seriver_name", "Payment Modes");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {

					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.PAYMENT_MODE, false);
					 */
					updateStatusDownloadDataMaster(DownloadCode.PAYMENT_MODE,
							false);

					bundle.putString("seriver_name", "Payment Modes");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.PAYMENT_MODE, false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.PAYMENT_MODE, false);

				bundle.putString("seriver_name", "Payment Modes");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName.equalsIgnoreCase(DownloadCode.COMPETITOR)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						/*
						 * FetchingdataParser parser = new FetchingdataParser(
						 * context); JSONArray jsonArray =
						 * parser.getCompetitorList(result);
						 * 
						 * if (jsonArray != null) {
						 * 
						 * DatabaseHelper.getConnection(context)
						 * .insertCompetitor(jsonArray);
						 * 
						 * }
						 */

						ContentValues contentValues[] = DatabaseUtilMethods
								.getContentValuesCompetitorsFromJson(jsonObject
										.getJSONArray("Result"));

						if (contentValues != null) {
							context.getContentResolver().bulkInsert(
									ProviderContract.URI_COMPETITORS,
									contentValues);
						}

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.COMPETITOR,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(DownloadCode.COMPETITOR,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name", "Competitors");
						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.COMPETITOR, false);
						 */

						updateStatusDownloadDataMaster(DownloadCode.COMPETITOR,
								false);

						bundle.putString("seriver_name", "Competitors");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.COMPETITOR, false);
					 */

					updateStatusDownloadDataMaster(DownloadCode.COMPETITOR,
							false);

					bundle.putString("seriver_name", "Competitors");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster( DownloadCode.COMPETITOR,
				 * false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.COMPETITOR, false);

				bundle.putString("seriver_name", "Competitors");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName
				.equalsIgnoreCase(DownloadCode.COMPETITION_PRODUCT_GROUP)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						/*
						 * FetchingdataParser parser = new FetchingdataParser(
						 * context); ArrayList<CompetitionProductGroupDto>
						 * compProductGroupList = parser
						 * .parseCompetitionProductGroupDto(result);
						 * 
						 * DatabaseHelper.getConnection(context)
						 * .insertCompetitionProductGroup(
						 * compProductGroupList);
						 */

						ContentValues contentValues[] = DatabaseUtilMethods
								.getContentValuesCompProductGroupFromJson(jsonObject
										.getJSONArray("Result"));

						if (contentValues != null) {
							context.getContentResolver().bulkInsert(
									ProviderContract.URI_COMP_PRODUCT_GROUP,
									contentValues);
						}

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.COMPETITION_PRODUCT_GROUP,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.COMPETITION_PRODUCT_GROUP,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name",
								"Competition Product Group");

						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.COMPETITION_PRODUCT_GROUP, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.COMPETITION_PRODUCT_GROUP, false);

						bundle.putString("seriver_name",
								"Competition Product Group");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.COMPETITION_PRODUCT_GROUP, false);
					 */

					updateStatusDownloadDataMaster(
							DownloadCode.COMPETITION_PRODUCT_GROUP, false);

					bundle.putString("seriver_name",
							"Competition Product Group");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.COMPETITION_PRODUCT_GROUP, false);
				 */

				updateStatusDownloadDataMaster(
						DownloadCode.COMPETITION_PRODUCT_GROUP, false);

				bundle.putString("seriver_name", "Competition Product Group");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName.equalsIgnoreCase(DownloadCode.OTHER_BEAT)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						// to use for outlet working 2 based in fragment
						/*
						 * DatabaseHelper.getConnection(
						 * context.getApplicationContext())
						 * .insertStoresBasic(jsonObject,
						 * Constants.STORE_TYPE_OTHER);
						 */

						JSONArray jsonArray = jsonObject.getJSONArray("Result");
						if (jsonArray != null) {

							ContentValues[] contentValues = DatabaseUtilMethods
									.getContentValuesStoresBaic(jsonArray,
											Constants.STORE_TYPE_OTHER, null,
											context);

							ContentResolver contentResolver = context
									.getContentResolver();
							contentResolver.bulkInsert(
									ProviderContract.URI_STORES_BASICS,
									contentValues);

						}

						Helper.saveBoolValueInPrefs(
								context,
								SharedPreferencesKey.PREF_MARK_FILE_WRITE_OTHER_BEAT,
								true);

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.OTHER_BEAT,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(DownloadCode.OTHER_BEAT,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name", "Dealer List");
						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.OTHER_BEAT, false);
						 */

						updateStatusDownloadDataMaster(DownloadCode.OTHER_BEAT,
								false);

						bundle.putString("seriver_name", "Dealer List");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.OTHER_BEAT, false);
					 */
					updateStatusDownloadDataMaster(DownloadCode.OTHER_BEAT,
							false);

					bundle.putString("seriver_name", "Dealer List");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster( DownloadCode.OTHER_BEAT,
				 * false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.OTHER_BEAT, false);

				bundle.putString("seriver_name", "Dealer List");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName.equalsIgnoreCase(DownloadCode.SURVEY_QUESTIONS)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						ResponseDto obj = new FetchingdataParser(context)
								.getResponseResult(result.toString());

						storeInDB(obj.getResult());

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.SURVEY_QUESTIONS,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.SURVEY_QUESTIONS,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name", "Survey Questions");
						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.SURVEY_QUESTIONS, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.SURVEY_QUESTIONS, false);

						bundle.putString("seriver_name", "Survey Questions");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {

					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.SURVEY_QUESTIONS, false);
					 */

					updateStatusDownloadDataMaster(
							DownloadCode.SURVEY_QUESTIONS, false);

					bundle.putString("seriver_name", "Survey Questions");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.SURVEY_QUESTIONS, false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.SURVEY_QUESTIONS,
						false);

				bundle.putString("seriver_name", "Survey Questions");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName.equalsIgnoreCase(DownloadCode.PLANOGRAM_CLASS)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						/*
						 * JSONArray resultArray = new
						 * FetchingdataParser(context)
						 * .getPlanogramDataListData(result);
						 * 
						 * if (resultArray != null) {
						 * DatabaseHelper.getConnection(context)
						 * .insertPlanogramClassMaster(resultArray); }
						 */

						ContentValues contentValues[] = DatabaseUtilMethods
								.getContentValuesPalnogramClassFromJson(jsonObject
										.getJSONArray("Result"));

						if (contentValues != null) {
							context.getContentResolver().bulkInsert(
									ProviderContract.URI_PALNOGRAM_CLASS,
									contentValues);
						}

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.PLANOGRAM_CLASS,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.PLANOGRAM_CLASS,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name", "Planogram Class");
						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.PLANOGRAM_CLASS, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.PLANOGRAM_CLASS, false);

						bundle.putString("seriver_name", "Planogram Class");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.PLANOGRAM_CLASS, false);
					 */

					updateStatusDownloadDataMaster(
							DownloadCode.PLANOGRAM_CLASS, false);

					bundle.putString("seriver_name", "Planogram Class");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.PLANOGRAM_CLASS, false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.PLANOGRAM_CLASS,
						false);

				bundle.putString("seriver_name", "Planogram Class");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName.equalsIgnoreCase(DownloadCode.PLANOGRAM_PRODUCT)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						GetPlanogramProductResponse getPlanogramProductResponse = new FetchingdataParser(
								context).getPlanogramDataResponse(result);
						GetPlanogramProductMasterSingleResult singleResult = getPlanogramProductResponse
								.getMasterSingleResult();

						ArrayList<PlanogramProductDataModal> dataModals = singleResult
								.getDataModals();

						if (dataModals == null) {

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.PLANOGRAM_PRODUCT, false);
							 */

							updateStatusDownloadDataMaster(
									DownloadCode.PLANOGRAM_PRODUCT, false);

							bundle.putString("seriver_name",
									"Planogram Product");
							bundle.putBoolean("is_success", false);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_ERROR);

						} else {

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertPlanogramProductMaster(dataModals);
							 */

							ContentValues[] contentValues = DatabaseUtilMethods
									.getContentValuesPlanogramProduct(dataModals);

							if (contentValues != null) {
								context.getContentResolver().bulkInsert(
										ProviderContract.URI_PALNOGRAM_PRODUCT,
										contentValues);
							}

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.PLANOGRAM_PRODUCT,
							 * jsonObject.getBoolean("IsSuccess"));
							 */

							updateStatusDownloadDataMaster(
									DownloadCode.PLANOGRAM_PRODUCT,
									jsonObject.getBoolean("IsSuccess"));

							bundle.putString("seriver_name",
									"Planogram Product");
							bundle.putBoolean("is_success", true);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);
						}

					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.PLANOGRAM_PRODUCT, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.PLANOGRAM_PRODUCT, false);

						bundle.putString("seriver_name", "Planogram Product");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.PLANOGRAM_PRODUCT, false);
					 */

					updateStatusDownloadDataMaster(
							DownloadCode.PLANOGRAM_PRODUCT, false);

					bundle.putString("seriver_name", "Planogram Product");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster("H", false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.PLANOGRAM_PRODUCT,
						false);

				bundle.putString("seriver_name", "Planogram Product");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		}

		else if (serviceName
				.equalsIgnoreCase(DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						/*
						 * DatabaseHelper.getConnection(context)
						 * .insertCompetitorProductMapping(jsonObject);
						 */

						ContentValues[] contentValues = DatabaseUtilMethods
								.getContentValuesCompetitorProductGroupMappingFromJson(jsonObject
										.getJSONArray("Result"));

						if (contentValues != null) {
							context.getContentResolver()
									.bulkInsert(
											ProviderContract.URI_COMPETITOR_PRODUCT_GROUP_MAPPING,
											contentValues);
						}

						/*
						 * DatabaseHelper .getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name",
								"Competitor Product Group Mapping");
						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

					} else {
						/*
						 * DatabaseHelper .getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING,
						 * false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING,
								false);

						bundle.putString("seriver_name",
								"Competitor Product Group Mapping");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper .getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING, false);
					 */

					updateStatusDownloadDataMaster(
							DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING,
							false);

					bundle.putString("seriver_name",
							"Competitor Product Group Mapping");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING, false);
				 */
				updateStatusDownloadDataMaster(
						DownloadCode.COMPETITOR_PRODUCT_GROUP_MAPPING, false);

				bundle.putString("seriver_name",
						"Competitor Product Group Mapping");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		}

		else if (serviceName.equalsIgnoreCase(DownloadCode.FMS)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						JSONObject jsonDataRoot = jsonObject
								.getJSONObject("SingleResult");

						if (jsonDataRoot != null) {

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertFeedbackMasters(jsonDataRoot);
							 */

							try {
								ContentResolver contentResolver = context
										.getContentResolver();

								List<ContentValues[]> list = DatabaseUtilMethods
										.getContentValuesFMS(jsonDataRoot);

								contentResolver.bulkInsert(
										ProviderContract.URI_FEEDBACK_STATUS,
										list.get(0));

								contentResolver.bulkInsert(
										ProviderContract.URI_FEEDBACK_TEAMS,
										list.get(1));
								contentResolver.bulkInsert(
										ProviderContract.URI_FEEDBACK_CATEGORY,
										list.get(2));
								contentResolver.bulkInsert(
										ProviderContract.URI_FEEDBACK_TYPE,
										list.get(3));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.FMS,
							 * jsonObject.getBoolean("IsSuccess"));
							 */

							updateStatusDownloadDataMaster(DownloadCode.FMS,
									jsonObject.getBoolean("IsSuccess"));

							bundle.putString("seriver_name", "MSS");
							bundle.putBoolean("is_success", true);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

						} else {

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.FMS, false);
							 */

							updateStatusDownloadDataMaster(DownloadCode.FMS,
									false);

							bundle.putString("seriver_name", "MSS");
							bundle.putBoolean("is_success", false);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_ERROR);

						}

					} else {

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster( DownloadCode.FMS,
						 * false);
						 */

						updateStatusDownloadDataMaster(DownloadCode.FMS, false);

						bundle.putString("seriver_name", "MSS");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {

					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster( DownloadCode.FMS,
					 * false);
					 */

					updateStatusDownloadDataMaster(DownloadCode.FMS, false);

					bundle.putString("seriver_name", "MSS");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {

				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(DownloadCode.FMS, false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.FMS, false);

				bundle.putString("seriver_name", "MSS");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		}

		else if (serviceName.equalsIgnoreCase(DownloadCode.EOL_SCHEMES)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						JSONArray jsonResponseJsonArray = jsonObject
								.getJSONArray("Result");

						if (jsonResponseJsonArray != null) {

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertEOLScheemData(jsonResponseJsonArray);
							 */

							ArrayList<ContentValues[]> eolList = DatabaseUtilMethods
									.getContentValuesEOLSchemeDetailAndHeaderFromJson(jsonResponseJsonArray);

							context.getContentResolver().bulkInsert(
									ProviderContract.URI_EOL_SCHEME_HEADER,
									eolList.get(0));

							context.getContentResolver().bulkInsert(
									ProviderContract.URI_EOL_SCHEME_DETAIL,
									eolList.get(1));

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.EOL_SCHEMES,
							 * jsonObject.getBoolean("IsSuccess"));
							 */

							updateStatusDownloadDataMaster(
									DownloadCode.EOL_SCHEMES,
									jsonObject.getBoolean("IsSuccess"));

							bundle.putString("seriver_name", "EOL Schemes");
							bundle.putBoolean("is_success", true);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

						} else {

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.EOL_SCHEMES, false);
							 */

							updateStatusDownloadDataMaster(
									DownloadCode.EOL_SCHEMES, false);

							bundle.putString("seriver_name", "EOL Schemes");
							bundle.putBoolean("is_success", false);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_ERROR);

						}
					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.EOL_SCHEMES, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.EOL_SCHEMES, false);

						bundle.putString("seriver_name", "EOL Schemes");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.EOL_SCHEMES, false);
					 */

					updateStatusDownloadDataMaster(DownloadCode.EOL_SCHEMES,
							false);

					bundle.putString("seriver_name", "EOL Schemes");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster( DownloadCode.EOL_SCHEMES,
				 * false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.EOL_SCHEMES, false);

				bundle.putString("seriver_name", "EOL Schemes");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName.equalsIgnoreCase(DownloadCode.RACE_MASTER)) {

			if (result != null) {

				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						JSONObject jsonResponseJsonArray = jsonObject
								.getJSONObject("SingleResult");

						JSONArray jsonResponseBrandCategoryArray = jsonResponseJsonArray
								.getJSONArray("BrandCategoryMapping");
						JSONArray jsonResponseBrandMasterArray = jsonResponseJsonArray
								.getJSONArray("BrandMaster");
						JSONArray jsonResonceFixtureArray = jsonResponseJsonArray
								.getJSONArray("FixtureMaster");
						JSONArray jsonResoncePOSMArray = jsonResponseJsonArray
								.getJSONArray("POSMMaster");
						JSONArray jsonResonceProductCategoryArray = jsonResponseJsonArray
								.getJSONArray("ProductCategory");

						JSONArray jsonResoncePosmProductMappingArray = jsonResponseJsonArray
								.getJSONArray("POSMProductMapping");

						if (jsonResponseJsonArray != null) {

							ContentResolver contentResolver = context
									.getContentResolver();

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertBrandCategoryMapping(
							 * jsonResponseBrandCategoryArray);
							 */

							contentResolver
									.bulkInsert(
											ProviderContract.URI_RACE_BRAND_CATEGORY_MAPPING,
											DatabaseUtilMethods
													.getBrandCategorymappingMasterContentValuesFromJson(jsonResponseBrandCategoryArray));
							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertBrandMaster(
							 * jsonResponseBrandMasterArray);
							 */
							contentResolver
									.bulkInsert(
											ProviderContract.URI_RACE_BRAND,
											DatabaseUtilMethods
													.getBrandMasterContentValuesFromJson(jsonResponseBrandMasterArray));

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertFixtureMaster( jsonResonceFixtureArray);
							 */

							contentResolver
									.bulkInsert(
											ProviderContract.URI_RACE_FIXTURE,
											DatabaseUtilMethods
													.getFixtureMasterContentValuesFromJson(jsonResonceFixtureArray));

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertPOSMMaster(jsonResoncePOSMArray);
							 */

							contentResolver
									.bulkInsert(
											ProviderContract.URI_RACE_POSM,
											DatabaseUtilMethods
													.getPosmMasterContentValuesFromJson(jsonResoncePOSMArray));

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertBrandProductCategoryMaster(
							 * jsonResonceProductCategoryArray);
							 */

							contentResolver
									.bulkInsert(
											ProviderContract.URI_RACE_PRODUCT_CATEGORY,
											DatabaseUtilMethods
													.getBrandProductCategoryMasterContentValuesFromJson(jsonResonceProductCategoryArray));

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertPosmProductMappingMaster(
							 * jsonResoncePosmProductMappingArray);
							 */

							contentResolver
									.bulkInsert(
											ProviderContract.URI_RACE_POSM_PRODUCT_MAPPING,
											DatabaseUtilMethods
													.getPOSMProductMappingMasterContentValuesFromJson(jsonResoncePosmProductMappingArray));

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.RACE_MASTER,
							 * jsonObject.getBoolean("IsSuccess"));
							 */

							updateStatusDownloadDataMaster(
									DownloadCode.RACE_MASTER,
									jsonObject.getBoolean("IsSuccess"));

							bundle.putString("seriver_name", "Race Master");
							bundle.putBoolean("is_success", true);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

						}
					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.RACE_MASTER, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.RACE_MASTER, false);

						bundle.putString("seriver_name", "Race Master");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {

					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.RACE_MASTER, false);
					 */

					updateStatusDownloadDataMaster(DownloadCode.RACE_MASTER,
							false);

					bundle.putString("seriver_name", "Race Master");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster( DownloadCode.RACE_MASTER,
				 * false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.RACE_MASTER, false);

				bundle.putString("seriver_name", "Race Master");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);

			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		} else if (serviceName.equalsIgnoreCase(DownloadCode.RACE_PRODUCT)) {

			if (result != null) {

				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						JSONObject jsonResponseJson = jsonObject
								.getJSONObject("SingleResult");
						JSONArray jsonResponseProductArray = jsonResponseJson
								.getJSONArray("Products");

						if (jsonResponseJson != null) {

							/*
							 * DatabaseHelper.getConnection(context)
							 * .insertRaceProductMaster(
							 * jsonResponseProductArray);
							 */

							context.getContentResolver()
									.bulkInsert(
											ProviderContract.URI_RACE_BRAND_PRODUCT,
											DatabaseUtilMethods
													.getProductMasterContentValuesFromJson(jsonResponseProductArray));

							/*
							 * DatabaseHelper.getConnection(context)
							 * .setDownloadStatusInDownloadMaster(
							 * DownloadCode.RACE_PRODUCT,
							 * jsonObject.getBoolean("IsSuccess"));
							 */

							updateStatusDownloadDataMaster(
									DownloadCode.RACE_PRODUCT,
									jsonObject.getBoolean("IsSuccess"));

							bundle.putString("seriver_name", "Race Product");
							bundle.putBoolean("is_success", true);
							syscStatusModel
									.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);

						}
					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.RACE_PRODUCT, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.RACE_PRODUCT, false);

						bundle.putString("seriver_name", "Race Product");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {
					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.RACE_PRODUCT, false);
					 */
					updateStatusDownloadDataMaster(DownloadCode.RACE_PRODUCT,
							false);

					bundle.putString("seriver_name", "Race Product");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.RACE_PRODUCT, false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.RACE_PRODUCT, false);

				bundle.putString("seriver_name", "Race Product");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);
		}

		else if (serviceName.equalsIgnoreCase(DownloadCode.USER_MODULES)) {

			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("IsSuccess")) {

						JSONArray jArray = jsonObject.getJSONArray("Result");
						
						ContentValues[] contentValues = DatabaseUtilMethods
								.getContetnValueUserModulesFromJSON(jArray);

						context.getContentResolver().bulkInsert(
								ProviderContract.USER_MODULES_URI,
								contentValues);
						
					

						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.SURVEY_QUESTIONS,
						 * jsonObject.getBoolean("IsSuccess"));
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.USER_MODULES,
								jsonObject.getBoolean("IsSuccess"));

						bundle.putString("seriver_name", "Modules");
						bundle.putBoolean("is_success", true);
						syscStatusModel
								.setStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);
						//DELETE FROM StoreModuleMaster  WHERE ModuleID NOT IN (SELECT UMM.ModuleID FROM UserModuleMaster UMM WHERE UMM.IsMandatory =1  )  
						String where = "ModuleID NOT IN (SELECT UMM.ModuleID FROM UserModuleMaster UMM WHERE UMM.IsMandatory = ? )";
						context.getContentResolver().delete(
								ProviderContract.URI_STORE_MODULE_MASTER,
								where, new String[] { "1" });


					} else {
						/*
						 * DatabaseHelper.getConnection(context)
						 * .setDownloadStatusInDownloadMaster(
						 * DownloadCode.SURVEY_QUESTIONS, false);
						 */

						updateStatusDownloadDataMaster(
								DownloadCode.USER_MODULES, false);

						bundle.putString("seriver_name", "Modules");
						bundle.putBoolean("is_success", false);
						syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
					}
				} catch (JSONException e) {

					/*
					 * DatabaseHelper.getConnection(context)
					 * .setDownloadStatusInDownloadMaster(
					 * DownloadCode.SURVEY_QUESTIONS, false);
					 */

					updateStatusDownloadDataMaster(
							DownloadCode.USER_MODULES, false);

					bundle.putString("seriver_name", "Modules");
					bundle.putBoolean("is_success", false);
					syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
				}

			} else {
				/*
				 * DatabaseHelper.getConnection(context)
				 * .setDownloadStatusInDownloadMaster(
				 * DownloadCode.SURVEY_QUESTIONS, false);
				 */

				updateStatusDownloadDataMaster(DownloadCode.USER_MODULES,
						false);

				bundle.putString("seriver_name", "Modules");
				bundle.putBoolean("is_success", false);
				syscStatusModel.setStatus(SyncUtils.SYNC_STATUS_ERROR);
			}

			messge.setData(bundle);
			uIHandler.sendMessage(messge);

		}

	}

	public void quitLooper() {
		backgroundHandler.getLooper().quit();

	}

	private void storeInDB(String result) {
		

		try {

			ContentResolver contentResolver = context.getContentResolver();

			List<ContentValues[]> list_arr_ContentValues = DatabaseUtilMethods
					.getSurveyQuestionData(result);

			contentResolver.bulkInsert(
					ProviderContract.SURVEY_QUESTION_CONTENT_URI,
					list_arr_ContentValues.get(0));

			contentResolver.bulkInsert(
					ProviderContract.SURVEY_QUESTION_OPTIONS_CONTENT_URI,
					list_arr_ContentValues.get(1));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String postMethod(String conUrl, JSONObject nvps) throws Exception {

		String result = null;
		InputStream is = null;

		HttpClient httpclient = new DefaultHttpClient();
		URI u = new URI(conUrl);
		HttpPost httppost = new HttpPost(u);
		httppost.setHeader(WebConfig.WebParams.APIKEY, Helper
				.getStringValuefromPrefs(context,
						SharedPreferencesKey.PREF_APIKEY));
		httppost.setHeader(WebConfig.WebParams.APITOKEN, Helper
				.getStringValuefromPrefs(context,
						SharedPreferencesKey.PREF_APITOKEN));

		if (!Helper.getStringValuefromPrefs(context,
				SharedPreferencesKey.PREF_USERID).equals("")) {

			httppost.setHeader(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(context,
							SharedPreferencesKey.PREF_USERID));

		} else {
			httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
		}
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");
		httppost.setEntity(new ByteArrayEntity(nvps.toString().getBytes("UTF8")));
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Helper.printStackTrace(e);
			Helper.printLog("Loading Runnable Error converting result DM",
					e.toString());
		}
		return result;

	}

	public boolean isOnline() {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null) {
			flag = cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}
		return flag;
	}

	private void updateStatusDownloadDataMaster(String dataCode, boolean status) {

		try {
			ContentValues initialValues = new ContentValues();

			initialValues
					.put(DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS,
							status);

			String whereClause = DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE
					+ "=?";
			String[] whereArgs = new String[] { dataCode };

			context.getContentResolver().update(
					ProviderContract.URI_DOWNLOAD_DATA_SINGLE_SERVICE,
					initialValues, whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
