package com.samsung.ssc.activitymodule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramCompitiorResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramProductResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramResponseMasterTableColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.CompetitionProductGroupDto;
import com.samsung.ssc.dto.CompetitosrList;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.PlanogramProductDataModal;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Helper;

public class PalnogramActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private static final int LOADER_ID_COMP_PRODUCT_GROUP = 1;
	private static final int LOADER_ID_PLANOGRAM_RESPONSE = 2;
	private static final int LOADER_ID_PLANOGRAM_PRODUCT_RESPONSE = 3;
	private static final int LOADER_ID_PLANOGRAM_COMPETITORS_RESPONSE = 4;
	private static final int LOADER_ID_GET_ACTIVITY_ID = 5;

	private LinearLayout compitiorlayoutContainer;
	protected int comProductGroupId;
	protected int lastSelectedCommProductGroupId;

	private LinearLayout comProductGroupLayoutContainer;
	private PlanogramProductListAdapter adapter;
	private TextView tvClassName;
	private HashMap<Integer, HashMap<Integer, Integer>> historyCompitiorsValue;
	private String channelType;
	private HashMap<Integer, ArrayList<PlanogramProductDataModal>> historyProductCode;
	private HashMap<Integer, String[]> historyClassData;
	private ArrayList<CompetitionProductGroupDto> compProductGroupList;
	// private int surveyResponseId = 0;
	private String storeSize;
	private String storeClass;
	private ActivityDataMasterModel mActivityData;
	private long mActivityID=-1;
	private JSONArray rootResponseJSONArray;
	private String[] planResponseIDs;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.palnogram_activity);

		getBundleValue();

		setUpView();
	}

	private void getBundleValue() {

		Intent intent = getIntent();

		Module module = (Module) intent
				.getParcelableExtra(IntentKey.MOUDLE_POJO);

		mActivityData = Helper.getActivityDataMasterModel(
				getApplicationContext(), module);

		if (mActivityData != null) {

			/*
			 * mActivityID =
			 * DatabaseHelper.getConnection(getApplicationContext())
			 * .getActivityIdIfExist(mActivityData);
			 */

			getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVITY_ID,
					null, this);

		}

	}

	private void setUpView() {

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
			Button mBt_Proceed = (Button) findViewById(R.id.saveBtn);
			mBt_Proceed.setText(getString(R.string.proceed));
		}

		adapter = new PlanogramProductListAdapter();
		ListView productListView = (ListView) this
				.findViewById(R.id.listViewPalnogramProductList);
		productListView.setAdapter(adapter);
		tvClassName = (TextView) this.findViewById(R.id.tvPalnogramClassName);
		Button cancelButton = (Button) findViewById(R.id.cancelBtn);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PalnogramActivity.this.finish();
			}
		});

		channelType = Helper.getStringValuefromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_STORE_CHANNEL_TYPE);
		storeClass = Helper.getStringValuefromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_STORE_STORE_CLASS);
		storeSize = Helper.getStringValuefromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_STORE_STORE_SIZE);

		historyCompitiorsValue = new HashMap<Integer, HashMap<Integer, Integer>>();
		historyProductCode = new HashMap<Integer, ArrayList<PlanogramProductDataModal>>();
		historyClassData = new HashMap<Integer, String[]>();

		getSupportLoaderManager().initLoader(LOADER_ID_COMP_PRODUCT_GROUP,
				null, this);

		if (channelType.equals("BS")) {
			// channelType = "BS";
			((Button) this.findViewById(R.id.btnPlanogramDisplayPlanogram))
					.setVisibility(View.GONE);
			onDisplayPlanogramButtonClick(null);
		}

	}

	private void createHistoryFromDatabase() {

		getSupportLoaderManager().initLoader(LOADER_ID_PLANOGRAM_RESPONSE,
				null, this);

	 

	}

	private void processHistoryData(JSONArray rootJsonArray)
			throws JSONException {

		int count = rootJsonArray.length();
		for (int i = 0; i < count; i++) {

			JSONObject nodeJsonObject = rootJsonArray.getJSONObject(i);
			if (nodeJsonObject.length() > 0) {
				int itemComProductGroupId = -1;
				try {
					itemComProductGroupId = nodeJsonObject
							.getInt("CompProductGroupID");
				} catch (JSONException e) {
					itemComProductGroupId = -1;
				}
				if (itemComProductGroupId != -1) {

					if (nodeJsonObject.has("PlanogramProductResponses")) {
						JSONArray jsonArrayProduct = nodeJsonObject
								.getJSONArray("PlanogramProductResponses");
						if (jsonArrayProduct != null) {
							int productCount = jsonArrayProduct.length();
							ArrayList<PlanogramProductDataModal> itemProductArray = new ArrayList<PlanogramProductDataModal>();
							for (int j = 0; j < productCount; j++) {

								JSONObject productItemJson = jsonArrayProduct
										.getJSONObject(j);
								PlanogramProductDataModal object = new PlanogramProductDataModal();
								object.setSelected(productItemJson
										.getBoolean("IsAvailable"));
								object.setProductCode(productItemJson
										.getString("ProductCode"));
								itemProductArray.add(object);
							}

							historyProductCode.put(itemComProductGroupId,
									itemProductArray);

						}
					}

					if (nodeJsonObject.has("PlanogramCompititorResponses")) {
						try {
							JSONArray jsonArrayCompititor = nodeJsonObject
									.getJSONArray("PlanogramCompititorResponses");

							if (jsonArrayCompititor != null) {
								int compititorCount = jsonArrayCompititor
										.length();
								HashMap<Integer, Integer> value = new HashMap<Integer, Integer>();
								for (int j = 0; j < compititorCount; j++) {
									JSONObject compititorItemJson = jsonArrayCompititor
											.getJSONObject(j);
									value.put(compititorItemJson
											.getInt("CompetitorID"),
											compititorItemJson.getInt("Value"));
								}

								historyCompitiorsValue.put(
										itemComProductGroupId, value);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (nodeJsonObject.has("Class")
							&& nodeJsonObject.has("ClassID")) {
						String[] classData = {
								nodeJsonObject.getString("Class"),
								String.valueOf(nodeJsonObject.getInt("ClassID")) };
						historyClassData.put(itemComProductGroupId, classData);
					}
				}
			}
		}

		setUpComProductGroup();
		showFromHistory();
	}

	@SuppressWarnings("deprecation")
	private void setUpComProductGroup() {

		try {
			/*
			 * compProductGroupList = DatabaseHelper.getConnection(
			 * getApplicationContext()).getCompetitionProductGroup();
			 */
			comProductGroupLayoutContainer = (LinearLayout) this
					.findViewById(R.id.palnogramProductCompGroupListContainer);
			comProductGroupLayoutContainer.removeAllViews();
			int comProductGroupCount = compProductGroupList.size();

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

			// params.width = 0;
			params.weight = 1;

			params.gravity = Gravity.CENTER_HORIZONTAL;

			for (int i = 0; i < comProductGroupCount; i++) {
				CompetitionProductGroupDto comProductGroup = compProductGroupList
						.get(i);
				Button button = new Button(getApplicationContext());
				button.setText(comProductGroup.getProductGroupName());
				button.setTag(comProductGroup.getCompProductGroupID());
				button.setLayoutParams(params);
				button.setTextColor(getResources().getColor(R.color.white));

				button.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.headerblue));
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						lastSelectedCommProductGroupId = comProductGroupId;
						setButtonSelector(v);
						mantainHistory();

						showFromHistory();
					}

				});

				comProductGroupLayoutContainer.addView(button);

			}

			comProductGroupId = Integer
					.parseInt((String) (((Button) comProductGroupLayoutContainer
							.getChildAt(0)).getTag()));
			lastSelectedCommProductGroupId = Integer
					.parseInt((String) (((Button) comProductGroupLayoutContainer
							.getChildAt(0)).getTag()));
			((Button) comProductGroupLayoutContainer.getChildAt(0))
					.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.buttonpresssed));

			if (!channelType.equalsIgnoreCase("BS")) {
				setUpCompetitorListView(comProductGroupId);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

	}

	@SuppressWarnings("deprecation")
	private void setButtonSelector(View v) {
		try {
			comProductGroupId = Integer
					.parseInt((String) ((Button) v).getTag());
			((Button) v).setBackgroundDrawable(v.getContext().getResources()
					.getDrawable(R.drawable.buttonpresssed));

			int childCount = comProductGroupLayoutContainer.getChildCount();
			for (int j = 0; j < childCount; j++) {
				Button siblingChilds = (Button) comProductGroupLayoutContainer
						.getChildAt(j);
				if (Integer.parseInt((String) siblingChilds.getTag()) != comProductGroupId) {

					siblingChilds.setBackgroundDrawable(siblingChilds
							.getContext().getResources()
							.getDrawable(R.drawable.headerblue));
				}

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
	}

	protected void showFromHistory() {
		if (!channelType.equals("BS")) {
			setUpCompetitorListView(comProductGroupId);

			if (!historyCompitiorsValue.isEmpty()) {

				if (historyCompitiorsValue.containsKey(comProductGroupId)) {

					HashMap<Integer, Integer> map = historyCompitiorsValue
							.get(comProductGroupId);
					Set<Integer> keySet = map.keySet();
					for (Integer key : keySet) {
						((EditText) compitiorlayoutContainer
								.findViewWithTag(String.valueOf(key)))
								.setText(String.valueOf(map.get(key)));
					}
				}
			}

		} else {

		}

		if (!historyProductCode.isEmpty()) {
			if (historyProductCode.containsKey(comProductGroupId)) {
				ArrayList<PlanogramProductDataModal> dataSet = historyProductCode
						.get(comProductGroupId);
				adapter.addDataSet(dataSet);
			} else {
				adapter.clearDataSet();
				if (channelType.equals("BS")) {
					onDisplayPlanogramButtonClick(null);

				}
			}

		}

		if (!historyClassData.isEmpty()) {
			if (historyClassData.containsKey(comProductGroupId)) {
				String[] classData = historyClassData.get(comProductGroupId);
				tvClassName.setText(classData[0]);
				tvClassName.setTag(classData[1]);
			} else {
				tvClassName.setText("");
				tvClassName.setTag(null);
			}
		}

	}

	protected void mantainHistory() {

		if (!channelType.endsWith("BS")) {
			HashMap<Integer, Integer> value = new HashMap<Integer, Integer>();
			int count = compitiorlayoutContainer.getChildCount();
			for (int i = 0; i < count; i++) {
				LinearLayout linerLayout = (LinearLayout) compitiorlayoutContainer
						.getChildAt(i);
				EditText ed = (EditText) linerLayout.getChildAt(1);
				int text = Integer
						.parseInt(ed.getText().toString().length() > 0 ? ed
								.getText().toString() : "0");

				value.put(Integer.parseInt((String) ed.getTag()), text);
				ed.setText("");
			}
			historyCompitiorsValue.put(lastSelectedCommProductGroupId, value);
		}
		ArrayList<PlanogramProductDataModal> productList = adapter.getDataSet();
		if (productList != null) {
			historyProductCode.put(lastSelectedCommProductGroupId, productList);
		}
		if (tvClassName.getTag() != null) {
			String[] classData = { tvClassName.getText().toString(),
					(String) tvClassName.getTag() };
			historyClassData.put(lastSelectedCommProductGroupId, classData);

		}
	}

	private void onSaveButtonClick(int syncStatus) {

		try {
			JSONArray rootJsonArray = new JSONArray();

			int count = compProductGroupList.size();
			if (count != 0) {

				for (int i = 0; i < count; i++) {
					CompetitionProductGroupDto dataSet = compProductGroupList
							.get(i);
					int comProductGroupNodeId = Integer.parseInt(dataSet
							.getCompProductGroupID());

					JSONObject noteJsonObject = new JSONObject();

					if (historyClassData.containsKey(comProductGroupNodeId)) {
						int classId = Integer.parseInt(historyClassData
								.get(comProductGroupNodeId)[1]);
						String className = historyClassData
								.get(comProductGroupNodeId)[0];
						if (classId != -1) {

							noteJsonObject.put("ClassID", classId);
							noteJsonObject.put("Class", className);
							noteJsonObject.put("CompProductGroupID",
									comProductGroupNodeId);

							if (!historyCompitiorsValue.isEmpty()) {
								if (historyCompitiorsValue
										.containsKey(comProductGroupNodeId)) {
									HashMap<Integer, Integer> comititorsValue = historyCompitiorsValue
											.get(comProductGroupNodeId);
									JSONArray compititorJsonArray = new JSONArray();
									Set<Integer> keySet = comititorsValue
											.keySet();
									for (Integer compititorKey : keySet) {
										JSONObject jsonObjectCompitior = new JSONObject();
										int value = comititorsValue
												.get(compititorKey);
										jsonObjectCompitior.put("CompetitorID",
												compititorKey);
										jsonObjectCompitior.put("Value", value);
										compititorJsonArray
												.put(jsonObjectCompitior);
									}
									noteJsonObject.put(
											"PlanogramCompititorResponses",
											compititorJsonArray);
								}
							}

							noteJsonObject.put("Adherence", 0);
							if (!historyProductCode.isEmpty()) {
								if (historyProductCode
										.containsKey(comProductGroupNodeId)) {
									ArrayList<PlanogramProductDataModal> productValues = historyProductCode
											.get(comProductGroupNodeId);
									int productCount = productValues.size();
									int selectedCount = 0;
									JSONArray productJsonArray = new JSONArray();
									for (int j = 0; j < productCount; j++) {
										JSONObject productJsonItem = new JSONObject();
										PlanogramProductDataModal productDataSet = productValues
												.get(j);
										productJsonItem.put("IsAvailable",
												productDataSet.isSelected());
										productJsonItem
												.put("ProductCode",
														productDataSet
																.getProductCode());
										if (productDataSet.isSelected()) {
											selectedCount++;
										}
										productJsonArray.put(productJsonItem);
									}
									noteJsonObject.put(
											"PlanogramProductResponses",
											productJsonArray);

									try {
										if (selectedCount == 0) {
											noteJsonObject.put("Adherence",
													0.0d);
										} else {
											float precent = (selectedCount * 100)
													/ productCount;
											noteJsonObject.put("Adherence",
													precent);
										}

									} catch (Exception e) {
										noteJsonObject.put("Adherence", 0.0d);
									}
								}
							}
						}
					}

					rootJsonArray.put(noteJsonObject);

				}

				if (!rootJsonArray.toString().equals("[{},{},{},{},{}]")) {
					/*
					 * FileWriter fw = new FileWriter(PalnogramActivity.this,
					 * Constants.PLANOGRAM_RESPONSE_FILE,
					 * rootJsonArray.toString(), true); fw.execute();
					 */

					if (mActivityID == -1) {
						// ganrate activity id

						mActivityData.setSyncStatus(syncStatus);
						
/*						mActivityID = DatabaseHelper.getConnection(
								getApplicationContext())
								.insertActivtyDataMaster(mActivityData);*/
						
						
						ContentValues contentValues = DatabaseUtilMethods.getActivityDataContetnValueArray(mActivityData);
						Uri uri = getContentResolver().insert(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues);
						mActivityID = ContentUris.parseId(uri);
						
						
					} else {
						/*DatabaseHelper.getConnection(getApplicationContext())
								.updateActivtyDataMaster(mActivityID,
										syncStatus);*/
						
						
						String where = ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID+ "=?";
						String[] selectionArgs = new String[] { String.valueOf(mActivityID) };
						
						
						ContentValues contentValues = DatabaseUtilMethods.getActivityDataContentValueUpdateStatus(syncStatus);
						getContentResolver().update(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues, where, selectionArgs);
					
						
						
					}

					getStatusFromDatabase(new SaveDataHandler(
							PalnogramActivity.this), rootJsonArray);

				} else {
					Toast.makeText(getApplicationContext(), "No Data to save",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "No Data to Proceed",
						Toast.LENGTH_LONG).show();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static class SaveDataHandler extends Handler {

		WeakReference<PalnogramActivity> planogramActivity;

		public SaveDataHandler(PalnogramActivity pa) {
			planogramActivity = new WeakReference<PalnogramActivity>(pa);
		}

		@Override
		public void handleMessage(Message msg) {

			boolean isSuccess = msg.getData().getBoolean("isSuccess");

			if (isSuccess) {

				Toast.makeText(planogramActivity.get(),
						"Data save successfully", Toast.LENGTH_LONG).show();

				PalnogramActivity activity = planogramActivity.get();

				if (activity != null) {
					activity.finish();
				}

			}
		};
	}

	private void getStatusFromDatabase(final Handler handler,
			final JSONArray rootJsonArray) {

		Thread mStatusThread = new Thread() {
			@Override
			public void run() {

				try {

					getContentResolver()
							.delete(ProviderContract.URI_PLANOGRAM_RESPONSE_URI,
									PlanogramResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
											+ " = ?",
									new String[] { String.valueOf(mActivityID) });

					getContentResolver()
							.delete(ProviderContract.URI_PLANOGRAM_PRODUCT_RESPONSE_URI,
									PlanogramProductResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
											+ " = ?",
									new String[] { String.valueOf(mActivityID) });

					getContentResolver()
							.delete(ProviderContract.URI_PLANOGRAM_COMPETITORS_RESPONSE_URI,
									PlanogramCompitiorResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
											+ " = ?",
									new String[] { String.valueOf(mActivityID) });

					int count = rootJsonArray.length();

					for (int i = 0; i < count; i++) {

						JSONObject rootJsonItem = rootJsonArray
								.getJSONObject(i);

						if (rootJsonItem != null && rootJsonItem.length() > 0) {
							ContentValues valuesPlanogramResponse = new ContentValues();

							valuesPlanogramResponse
									.put(PlanogramResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
											mActivityID);

							valuesPlanogramResponse
									.put(PlanogramResponseMasterTableColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
											rootJsonItem
													.getInt("CompProductGroupID"));
							valuesPlanogramResponse
									.put(PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
											rootJsonItem.getString("Class"));
							valuesPlanogramResponse
									.put(PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE,
											rootJsonItem.getDouble("Adherence"));
							valuesPlanogramResponse
									.put(PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID,
											rootJsonItem.getInt("ClassID"));

							Uri uri = getContentResolver()
									.insert(ProviderContract.URI_PLANOGRAM_RESPONSE_URI,
											valuesPlanogramResponse);

							long planResponseId = ContentUris.parseId(uri);

							if (rootJsonItem.has("PlanogramProductResponses")) {
								JSONArray planogramProductResponses = rootJsonItem
										.getJSONArray("PlanogramProductResponses");
								if (planogramProductResponses != null) {
									int countPlanogramProductResponses = planogramProductResponses
											.length();

									ContentValues[] contentValuesProduct = new ContentValues[countPlanogramProductResponses];

									for (int j = 0; j < countPlanogramProductResponses; j++) {
										ContentValues initialValuesProduct = new ContentValues();
										JSONObject jsonObjectItemProduct = planogramProductResponses
												.getJSONObject(j);
										initialValuesProduct
												.put(PlanogramProductResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
														mActivityID);
										initialValuesProduct
												.put(PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
														planResponseId);
										initialValuesProduct
												.put(PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
														jsonObjectItemProduct
																.getString("ProductCode"));
										initialValuesProduct
												.put(PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE,
														jsonObjectItemProduct
																.getBoolean("IsAvailable") ? 1
																: 0);

										contentValuesProduct[j] = initialValuesProduct;

									}

									getContentResolver()
											.bulkInsert(
													ProviderContract.URI_PLANOGRAM_PRODUCT_RESPONSE_URI,
													contentValuesProduct);
								}

							}
							if (rootJsonItem
									.has("PlanogramCompititorResponses")) {
								JSONArray planogramCompititorResponses = rootJsonItem
										.getJSONArray("PlanogramCompititorResponses");

								if (planogramCompititorResponses != null) {

									int countPlanogramCompititorResponses = planogramCompititorResponses
											.length();

									ContentValues[] contentValuesCompetitor = new ContentValues[countPlanogramCompititorResponses];

									for (int j = 0; j < countPlanogramCompititorResponses; j++) {
										ContentValues initialValuesCompitior = new ContentValues();
										JSONObject jsonObjectItemCompititor = planogramCompititorResponses
												.getJSONObject(j);

										initialValuesCompitior
												.put(PlanogramCompitiorResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
														mActivityID);
										initialValuesCompitior
												.put(PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
														planResponseId);
										initialValuesCompitior
												.put(PlanogramCompitiorResponseMasterColumns.KEY_COMETITOR_ID,
														jsonObjectItemCompititor
																.getInt("CompetitorID"));
										initialValuesCompitior
												.put(PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE,
														jsonObjectItemCompititor
																.getInt("Value"));

										contentValuesCompetitor[j] = initialValuesCompitior;

									}

									getContentResolver()
											.bulkInsert(
													ProviderContract.URI_PLANOGRAM_COMPETITORS_RESPONSE_URI,
													contentValuesCompetitor);
								}

							}
						}
					}

					/*
					 * boolean isSuccess = DatabaseHelper.getConnection(
					 * getApplicationContext()).insertPlanogramResponse(
					 * mActivityID, rootJsonArray);
					 */
					Message msg = handler.obtainMessage();
					Bundle bundle = new Bundle();

					bundle.putBoolean("isSuccess", true);

					msg.setData(bundle);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		mStatusThread.start();

	}

	private void setUpCompetitorListView(int comProductGroupId2) {

		compitiorlayoutContainer = (LinearLayout) this
				.findViewById(R.id.palnogramCompitorListContainer);
		compitiorlayoutContainer.removeAllViews();
		ArrayList<CompetitosrList> competitorsListData = DatabaseHelper
				.getConnection(getApplicationContext())
				.getCompititorByCompProductGroupId(comProductGroupId2);

		int competitorsCount = competitorsListData.size();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

		// params.width = 0;
		params.weight = 1;

		params.gravity = Gravity.CENTER_HORIZONTAL;

		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(2);
		for (int i = 0; i < competitorsCount; i++) {

			CompetitosrList competitorsData = competitorsListData.get(i);

			LinearLayout layout = new LinearLayout(getApplicationContext());
			layout.setLayoutParams(params);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setBackgroundResource(R.drawable.palnogram_edittext);

			TextView tvCom = new TextView(getApplicationContext());
			tvCom.setTextColor(getResources().getColor(R.color.customblue));
			tvCom.setText(competitorsData.getName());
			tvCom.setLayoutParams(params);

			EditText editText = new EditText(getApplicationContext());
			editText.setTag(competitorsData.getCompetitorID());
			editText.setMaxLines(1);
			editText.setEms(3);

			editText.setFilters(filterArray);
			editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			editText.setLayoutParams(params);

			layout.addView(tvCom, 0);
			layout.addView(editText, 1);

			compitiorlayoutContainer.addView(layout);
		}

	}

	public void onDisplayPlanogramButtonClick(View view) {

		try {
			String className = getClassName();

			ArrayList<PlanogramProductDataModal> productCodeList = DatabaseHelper
					.getConnection(getApplicationContext()).getProductCodeList(
							className, channelType, comProductGroupId);

			if (productCodeList.size() < 1) {
				adapter.clearDataSet();
			}
			adapter.addDataSet(productCodeList);

			historyProductCode.put(comProductGroupId, adapter.getDataSet());

			if (!channelType.endsWith("BS")) {
				HashMap<Integer, Integer> value = new HashMap<Integer, Integer>();
				int count = compitiorlayoutContainer.getChildCount();
				for (int i = 0; i < count; i++) {
					LinearLayout linerLayout = (LinearLayout) compitiorlayoutContainer
							.getChildAt(i);
					EditText ed = (EditText) linerLayout.getChildAt(1);
					int text = Integer.parseInt(ed.getText().toString()
							.length() > 0 ? ed.getText().toString() : "0");
					value.put(Integer.parseInt((String) ed.getTag()), text);

				}
				historyCompitiorsValue.put(comProductGroupId, value);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

	}

	private String getClassName() {

		if (channelType.equals("BS")) {

			String[] classData = { storeClass + storeSize, "-5" };
			String className = classData[0];
			tvClassName.setText(className);
			tvClassName.setTag(classData[1]);
			historyClassData.put(comProductGroupId, classData);

			return className;
		} else {

			try {
				EditText editTextSamsung = (EditText) compitiorlayoutContainer
						.findViewWithTag("1");
				if (editTextSamsung != null) {
					String samsungClassRangeText = editTextSamsung.getText()
							.toString();
					String className = "N/A";
					if (samsungClassRangeText.length() > 0) {
						try {
							int classRange = Integer
									.parseInt(samsungClassRangeText);

							int compGroupId = comProductGroupId;
							String[] classData = DatabaseHelper.getConnection(
									getApplicationContext())
									.getPlanogramClassName(compGroupId,
											classRange, channelType);
							className = classData[0];
							tvClassName.setText(className);
							tvClassName.setTag(classData[1]);
							historyClassData.put(compGroupId, classData);

						} catch (NumberFormatException e) {
							Toast.makeText(getApplicationContext(),
									"Please insert valid input",
									Toast.LENGTH_SHORT).show();
						}

					}
					return className;
				}
				return "N/A";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "N/A";
	}

	class PlanogramProductListAdapter extends BaseAdapter {

		private ArrayList<PlanogramProductDataModal> dataSet = null;
		LayoutInflater inflater;

		public PlanogramProductListAdapter() {
			inflater = LayoutInflater.from(getApplicationContext());
			this.dataSet = new ArrayList<PlanogramProductDataModal>();
		}

		public void addDataSet(ArrayList<PlanogramProductDataModal> dataSet) {
			this.dataSet.clear();
			this.dataSet.addAll(dataSet);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return dataSet.size();
		}

		public void clearDataSet() {
			this.dataSet.clear();
			notifyDataSetChanged();
		}

		@Override
		public Object getItem(int position) {
			return dataSet.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHandler handler;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.planogram_product_list_view, null);
				handler = new ViewHandler();
				handler.tvProductName = (TextView) convertView
						.findViewById(R.id.textViewPlanogramProductName);
				handler.cbProductSelected = (CheckBox) convertView
						.findViewById(R.id.checkBoxPlanogramSelected);
				handler.cbProductSelected
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								int getPosition = (Integer) buttonView.getTag(); 
								dataSet.get(getPosition).setSelected(
										buttonView.isChecked()); // Set the
							 
							}
						});
				convertView.setTag(handler);

				convertView.setTag(R.id.textViewPlanogramProductName,
						handler.tvProductName);
				convertView.setTag(R.id.checkBoxPlanogramSelected,
						handler.cbProductSelected);
			} else {
				handler = (ViewHandler) convertView.getTag();
			}
			handler.cbProductSelected.setTag(position);  
														 
			PlanogramProductDataModal item = dataSet.get(position);
			handler.tvProductName.setText(item.getProductCode());
			handler.cbProductSelected.setChecked(item.isSelected());

			return convertView;
		}

		class ViewHandler {
			TextView tvProductName;
			CheckBox cbProductSelected;
		}

		@SuppressWarnings("unchecked")
		public ArrayList<PlanogramProductDataModal> getDataSet() {
			return (ArrayList<PlanogramProductDataModal>) this.dataSet.clone();
		}

	}

	public void onProceedButtonClick(View view) {

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
			DialogInterface.OnClickListener buttonSubmitListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					onSaveButtonClick(SyncUtils.SYNC_STATUS_SUBMIT);

				}
			};

			DialogInterface.OnClickListener buttonSaveListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					onSaveButtonClick(SyncUtils.SYNC_STATUS_SAVED);
				}
			};

			Helper.showThreeButtonConfirmationDialog(this,
					R.string.sync_confirmation_dialog_title,
					R.string.sync_confirmation_dialog_message,
					R.string.sync_confirmation_dialog_button_one,
					R.string.sync_confirmation_dialog_button_two,
					R.string.sync_confirmation_dialog_button_negative,
					buttonSubmitListerner, buttonSaveListerner, null);
		}

		else {
			onSaveButtonClick(SyncUtils.SYNC_STATUS_SUBMIT);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		Loader<Cursor> loader = null;
		switch (loaderID) {
		case LOADER_ID_COMP_PRODUCT_GROUP:
			loader = new CursorLoader(this,
					ProviderContract.URI_COMP_PRODUCT_GROUP, null, null, null,
					null);
			break;

		case LOADER_ID_PLANOGRAM_RESPONSE:

			loader = new CursorLoader(
					this,
					ProviderContract.URI_PLANOGRAM_RESPONSE_URI,
					new String[] {
							PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
							PlanogramResponseMasterTableColumns.KEY_COMPETETION_PRODUCT_GROUP_ID,
							PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_PRODUCT_MASTER_CLASS,
							PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_CLASS_MASTER_CLASS_ID,
							PlanogramResponseMasterTableColumns.KEY_PLANOGRAM_RESPONSE_MASTER_ADHERENCE },
					PlanogramResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
							+ "=?",
					new String[] { String.valueOf(mActivityID) }, null);

			break;

		case LOADER_ID_PLANOGRAM_PRODUCT_RESPONSE:

			String selection1 = PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
					+ " in (";
			for (int i = 0; i < planResponseIDs.length; i++) {
				selection1 += "?, ";
			}
			selection1 = selection1.substring(0, selection1.length() - 2) + ")";
			loader = new CursorLoader(
					this,
					ProviderContract.URI_PLANOGRAM_PRODUCT_RESPONSE_URI,
					new String[] {
							PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
							PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_MASTER_PRODUCT_CODE,
							PlanogramProductResponseMasterColumns.KEY_PLANOGRAM_PRODUCT_RESPONSE_MASTER_IS_AVAILABLE },
					selection1, planResponseIDs, null);
			break;

		case LOADER_ID_PLANOGRAM_COMPETITORS_RESPONSE:
			String selection2 = PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID
					+ " in (";
			for (int i = 0; i < planResponseIDs.length; i++) {
				selection2 += "?, ";
			}
			selection2 = selection2.substring(0, selection2.length() - 2) + ")";
			loader = new CursorLoader(
					this,
					ProviderContract.URI_PLANOGRAM_COMPETITORS_RESPONSE_URI,
					new String[] {
							PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_RESPONSE_MASTER_PLAN_RESPONSE_ID,
							PlanogramCompitiorResponseMasterColumns.KEY_PLANOGRAM_COMPITITORS_RESPONSE_MASTER_PLAN_VALUE,
							PlanogramCompitiorResponseMasterColumns.KEY_COMETITOR_ID },
					selection2, planResponseIDs, null);
			break;

		case LOADER_ID_GET_ACTIVITY_ID:

			String[] arg = { String.valueOf(mActivityData.getStoreID()),
					String.valueOf(mActivityData.getModuleCode()) };

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null, arg,
					null);

			break;

		default:
			break;
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch (loader.getId()) {
		case LOADER_ID_COMP_PRODUCT_GROUP:

			if (cursor != null && cursor.moveToFirst()) {
				compProductGroupList = DatabaseUtilMethods
						.getCounterShareDisplayShareCompProductGroupListFromCursor(cursor);

				if (mActivityID != -1) {

					createHistoryFromDatabase();

				} else {
					setUpComProductGroup();
				}

			}

			break;

		case LOADER_ID_PLANOGRAM_RESPONSE:

			if (cursor != null && cursor.moveToFirst()) {
				rootResponseJSONArray = DatabaseUtilMethods
						.getPlanogramRootResponseFromCursor(cursor);

				planResponseIDs = new String[rootResponseJSONArray.length()];

				for (int i = 0; i < planResponseIDs.length; i++) {
					try {
						planResponseIDs[i] = String
								.valueOf(rootResponseJSONArray.getJSONObject(i)
										.getInt("PlanResponseID"));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				getSupportLoaderManager().initLoader(
						LOADER_ID_PLANOGRAM_PRODUCT_RESPONSE, null, this);
			}

			break;

		case LOADER_ID_PLANOGRAM_PRODUCT_RESPONSE:

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
						rootItem.put("PlanogramProductResponses",
								productJsonArray);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
			getSupportLoaderManager().initLoader(
					LOADER_ID_PLANOGRAM_COMPETITORS_RESPONSE, null, this);
			break;
		case LOADER_ID_PLANOGRAM_COMPETITORS_RESPONSE:

			if (cursor != null && cursor.moveToFirst()) {
				JSONArray planogramCompetitorResponse = DatabaseUtilMethods
						.getPlanogramCompetitorsResponseFromCursor(cursor);

				for (int i = 0; i < rootResponseJSONArray.length(); i++) {
					try {
						JSONObject rootItem = rootResponseJSONArray
								.getJSONObject(i);

						JSONArray productJsonArray = new JSONArray();

						int planResponseID = rootItem.getInt("PlanResponseID");

						for (int j = 0; j < planogramCompetitorResponse
								.length(); j++) {

							int planResponseIDProduct = planogramCompetitorResponse
									.getJSONObject(j).getInt("PlanResponseID");
							if (planResponseID == planResponseIDProduct) {
								productJsonArray
										.put(planogramCompetitorResponse
												.getJSONObject(j));
							}

						}

						rootItem.put("PlanogramCompititorResponses",
								productJsonArray);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			try {
				processHistoryData(rootResponseJSONArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
			
		  case LOADER_ID_GET_ACTIVITY_ID:
			if (cursor != null && cursor.getCount() > 0) {
				
				 mActivityID = DatabaseUtilMethods.getActivityID(cursor);
				
				 
			}
			
			setUpView();
			
            break;
			
		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
