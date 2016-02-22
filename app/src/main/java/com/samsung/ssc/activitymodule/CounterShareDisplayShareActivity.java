package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.ModuleCode;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.CounterShareDisplayShareResponseMasterColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Child;
import com.samsung.ssc.dto.CompetitionProductGroupDto;
import com.samsung.ssc.dto.Group;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Helper;

@SuppressLint("UseSparseArrays")
public class CounterShareDisplayShareActivity extends FragmentActivity
		implements LoaderCallbacks<Cursor> {

	private static final int LOADER_ID_COMP_PRODUCT_GROUP = 1;
	private static final int LOADER_ID_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE = 2;
//	private static final int LOADER_ID_GET_ACTIVITY_ID = 3;

	private int mComProductGroupId;
	protected int mLastSelectedCommProductGroupId;
	private LinearLayout comProductGroupLayoutContainer;
	private MyAdapter mAdapter;

	private ArrayList<CompetitionProductGroupDto> compProductGroupList;
	private ExpandableListView mExpandableListView;
	private int optionType = 1;

	private JSONArray mJArrayHistory;
	private TextView mTextViewTitle;
	private HashMap<Integer, ArrayList<Group>> mMapProductGroup1;
	private InputMethodManager inputManager;
	private Window window;
	private long mActivityID;
	private int mModuleCode;
	private ActivityDataMasterModel mActivityData;
	private ArrayList<Group> groups;

	// private ArrayList<Group> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.setScreenShotOff(this);
		setContentView(R.layout.counter_display_share);

		getBundleValue();

		setUpView();

	}

	private void setUpView() {
		inputManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		window = getWindow();

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
			Button saveButton = (Button) findViewById(R.id.saveBtnCounterDisplayShare);
			saveButton.setText(getString(R.string.proceed));
		}

		mTextViewTitle = (TextView) findViewById(R.id.headercentretextCounterDisplayShar);
		mExpandableListView = (ExpandableListView) findViewById(R.id.elvCounterDisplayShare);
		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						try {

							if (window.getCurrentFocus() != null) {
								inputManager.hideSoftInputFromWindow(
										getCurrentFocus().getWindowToken(), 0);
								getCurrentFocus().clearFocus();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						try {

							if (window.getCurrentFocus() != null) {
								inputManager.hideSoftInputFromWindow(
										getCurrentFocus().getWindowToken(), 0);
								// getCurrentFocus().clearFocus();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

		mExpandableListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (getCurrentFocus() != null) {
					inputManager.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(), 0);
					// getCurrentFocus().clearFocus();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}

		});

		mMapProductGroup1 = new HashMap<Integer, ArrayList<Group>>();

		mAdapter = new MyAdapter();

		mExpandableListView.setAdapter(mAdapter);

		if (mModuleCode != 0) {

			if (mModuleCode == ModuleCode.MENU_DISPLAYSHARE) {
				mTextViewTitle.setText("Display Share");
				optionType = 1;
			} else if (mModuleCode == ModuleCode.MENU_COUNTERSHARE) {
				mTextViewTitle.setText("Counter Share");
				optionType = 2;

			}

			getSupportLoaderManager().initLoader(LOADER_ID_COMP_PRODUCT_GROUP,
					null, this);

		}

	}

	/**
	 * Gets bundled data form intent
	 */
	public void getBundleValue() {
		Intent intent = getIntent();

		Module module = (Module) intent
				.getParcelableExtra(IntentKey.MOUDLE_POJO);
		mModuleCode = module.getModuleCode();

		mActivityData = Helper.getActivityDataMasterModel(
				getApplicationContext(), module);

		if (mActivityData != null) {

			mActivityID = DatabaseHelper.getConnection(getApplicationContext())
					.getActivityIdIfExist(mActivityData);

		/*	getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVITY_ID,
					null, this);*/

		}

	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setUpComProductGroupView() {

		/*
		 * compProductGroupList = DatabaseHelper.getConnection(
		 * getApplicationContext()).getCompetitionProductGroup();
		 */
		comProductGroupLayoutContainer = (LinearLayout) this
				.findViewById(R.id.llComProductGroupContainer);
		int comProductGroupCount = compProductGroupList.size();

		if (comProductGroupCount == 0) {

			Helper.showCustomToast(getApplicationContext(),
					R.string.download_authorization_not_assign_to_you,
					Toast.LENGTH_LONG);

			finish();
			return;
		}

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

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

			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				button.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.headerblue));
			} else {
				button.setBackground(getResources().getDrawable(
						R.drawable.headerblue));

			}

			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mLastSelectedCommProductGroupId = mComProductGroupId;

					try {
						mComProductGroupId = Integer.parseInt((String) v
								.getTag());

						maintainHistory();

						if (mMapProductGroup1.containsKey(mComProductGroupId)) {

							showMaintainedHistory();

						} else {
							setUpListView(mComProductGroupId);
						}

						setButtonSelector(v);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
					}
				}

			});

			comProductGroupLayoutContainer.addView(button);

		}

		mComProductGroupId = Integer
				.parseInt((String) (((Button) comProductGroupLayoutContainer
						.getChildAt(0)).getTag()));
		mLastSelectedCommProductGroupId = Integer
				.parseInt((String) (((Button) comProductGroupLayoutContainer
						.getChildAt(0)).getTag()));

		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			((Button) comProductGroupLayoutContainer.getChildAt(0))
					.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.buttonpresssed));
		} else {
			((Button) comProductGroupLayoutContainer.getChildAt(0))
					.setBackground(getResources().getDrawable(
							R.drawable.buttonpresssed));
		}
		setUpListView(mComProductGroupId);
	}

	/**
	 * Saved user input data in hashmap temporarily while navigating
	 */
	private void maintainHistory() {

		@SuppressWarnings("unchecked")
		ArrayList<Group> groupList = (ArrayList<Group>) mAdapter.getItems()
				.clone();

		mMapProductGroup1.put(mLastSelectedCommProductGroupId, groupList);

	}

	/**
	 * Save user input data from hashmap data temporarily stored while
	 * navigating
	 */
	private void showMaintainedHistory() {

		ArrayList<Group> groupList1 = mMapProductGroup1.get(mComProductGroupId);

		mAdapter.addItems(groupList1);

	}

	/**
	 * Get compititor list by com product group id
	 * 
	 * @param comProductGroupId2
	 */
	private void setUpListView(int comProductGroupId2) {

		try {

			this.groups = DatabaseHelper.getConnection(getApplicationContext())
					.getGroupsDataByComProductGroupId(mModuleCode,
							comProductGroupId2, getApplicationContext());
			this.groups = showValueFromStoredHistoryData(this.groups);
			mAdapter.addItems(groups);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private ArrayList<Group> showValueFromStoredHistoryData(
			ArrayList<Group> groups) {

		if (mJArrayHistory == null) {
			return groups;
		} else if (mJArrayHistory.length() == 0) {
			return groups;
		}

		try {

			int productGroupId, surveyQuestionId, competitorId, historyJSONarrLength;

			historyJSONarrLength = mJArrayHistory.length();

			for (Group singleGroup : groups) {

				productGroupId = singleGroup.getProductGroupId();
				surveyQuestionId = singleGroup.getSurveyQuestionId();

				for (Child singleChild : singleGroup.getChilds()) {

					competitorId = Integer.parseInt(singleChild
							.getCompetitorID());

					for (int i = 0; i < historyJSONarrLength; i++) {

						JSONObject jsonObject = mJArrayHistory.getJSONObject(i);

						if (jsonObject.getInt("ProductGroupID") == productGroupId
								&& jsonObject.getInt("SurveyQuestionID") == surveyQuestionId
								&& jsonObject.getInt("CompetitorID") == competitorId) {

							singleChild.setValue(jsonObject
									.getInt("UserResponse") + "");

							// break out from inner loop
							break;

						}
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return groups;
	}

	/**
	 * Expandable list view adapter.
	 * 
	 * @author d.ashish
	 * 
	 */

	class MyAdapter extends BaseExpandableListAdapter {

		private ArrayList<Group> group;
		private LayoutInflater inflater;

		public MyAdapter() {
			super();
			this.group = new ArrayList<Group>();
			this.inflater = LayoutInflater.from(getApplicationContext());

		}

		public void addItems(ArrayList<Group> group) {

			if (group != null) {

				clearItems();
				this.group.addAll(group);
				notifyDataSetChanged();
			}

		}

		public void clearItems() {
			this.group.clear();
			notifyDataSetChanged();
		}

		public ArrayList<Group> getItems() {
			return this.group;
		}

		@Override
		public int getGroupCount() {
			return group.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return group.get(groupPosition).getChilds().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return group.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {

			return group.get(groupPosition).getChilds().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return group.get(groupPosition).getSurveyQuestionId();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {

			return Integer.parseInt(group.get(groupPosition).getChilds()
					.get(childPosition).getCompetitorID());
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			ViewHolderGroup holderGroup;

			if (convertView == null) {

				convertView = inflater.inflate(
						R.layout.expandable_group_layout, null);
				holderGroup = new ViewHolderGroup(convertView);
				convertView.setTag(holderGroup);

			} else {
				holderGroup = (ViewHolderGroup) convertView.getTag();
			}

			((TextView) convertView).setText(group.get(groupPosition)
					.getQuestion());

			return convertView;
		}

		class ViewHolderGroup {

			TextView tvGroupTiitle;

			public ViewHolderGroup(View convertView) {

				this.tvGroupTiitle = (TextView) convertView;

			}

		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			final Child child = group.get(groupPosition).getChilds()
					.get(childPosition);

			final ViewHodlerChild hodlerChild;
			convertView = inflater.inflate(R.layout.expandable_child_layout,
					null);
			hodlerChild = new ViewHodlerChild(convertView);

			convertView.setTag(hodlerChild);

			try {
				if (child != null) {
					hodlerChild.tvChildLable.setText(child.getName());
					hodlerChild.etVaules.setText(child.getValue());
					hodlerChild.etVaules
							.addTextChangedListener(new TextWatcher() {

								@Override
								public void onTextChanged(CharSequence s,
										int start, int before, int count) {

								}

								@Override
								public void beforeTextChanged(CharSequence s,
										int start, int count, int after) {

								}

								@Override
								public void afterTextChanged(Editable s) {

									try {
										if (s != null) {
											child.setValue(s.toString());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	class ViewHodlerChild {
		public ViewHodlerChild(View convertView) {
			this.tvChildLable = (TextView) convertView
					.findViewById(R.id.tvCompetitorName);

			this.etVaules = (EditText) convertView
					.findViewById(R.id.etCompetitorValue);

		}

		TextView tvChildLable;
		EditText etVaules;

	}

	/**
	 * This is called when product group at bottom of the screen touched
	 * 
	 * @param v
	 *            the view button that selected
	 */

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	private void setButtonSelector(View v) {
		mComProductGroupId = Integer.parseInt((String) ((Button) v).getTag());
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			((Button) v).setBackgroundDrawable(v.getContext().getResources()
					.getDrawable(R.drawable.buttonpresssed));

		} else {
			((Button) v).setBackground(v.getContext().getResources()
					.getDrawable(R.drawable.buttonpresssed));

		}

		int childCount = comProductGroupLayoutContainer.getChildCount();
		for (int j = 0; j < childCount; j++) {
			Button siblingChilds = (Button) comProductGroupLayoutContainer
					.getChildAt(j);
			if (Integer.parseInt((String) siblingChilds.getTag()) != mComProductGroupId) {

				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					siblingChilds.setBackgroundDrawable(siblingChilds
							.getContext().getResources()
							.getDrawable(R.drawable.headerblue));

				} else {
					siblingChilds.setBackground(siblingChilds.getContext()
							.getResources().getDrawable(R.drawable.headerblue));

				}

			}

		}
	}

	/**
	 * Called when user either select Save/Submit
	 * 
	 * @param syncStatus
	 *            Status for the data to be saved in database Save/Submit
	 */

	private void onButtonTakeAction(int syncStatus) {

		try {

			// set sync status
			mActivityData.setSyncStatus(syncStatus);

			mLastSelectedCommProductGroupId = mComProductGroupId;
			maintainHistory();

			try {

				ArrayList<JSONObject> elements = new ArrayList<JSONObject>();

				int count = compProductGroupList.size();

				int userRoleID = Integer.valueOf(Helper
						.getStringValuefromPrefs(
								CounterShareDisplayShareActivity.this,
								SharedPreferencesKey.PREF_ROLEIDSTOREWISE));

				int competitionType = optionType;
				int productGroupID;
				int productTypeID;
				int storeId;

				try {
					storeId = Integer.parseInt(Helper.getStringValuefromPrefs(
							CounterShareDisplayShareActivity.this,
							SharedPreferencesKey.PREF_STOREID));
				} catch (NumberFormatException e2) {
					storeId = -1;
				}

				int userId = Integer.valueOf(Helper.getStringValuefromPrefs(
						CounterShareDisplayShareActivity.this,
						SharedPreferencesKey.PREF_USERID));

				for (int i = 0; i < count; i++) {
					CompetitionProductGroupDto dataSet = compProductGroupList
							.get(i);
					int comProductGroupNodeId = Integer.parseInt(dataSet
							.getCompProductGroupID());

					productGroupID = comProductGroupNodeId;
					productTypeID = comProductGroupNodeId;

					if (mMapProductGroup1.containsKey(comProductGroupNodeId)) {

						ArrayList<Group> groups = mMapProductGroup1
								.get(comProductGroupNodeId);

						Group singleGroup;
						ArrayList<Child> childern;

						Child singleChild;
						String competitorID;

						int surveyQuestionID;

						int userResponse;
						String userResponseTemp;

						if (!groups.isEmpty()) {

							for (int j = 0; j < groups.size(); j++) {
								singleGroup = groups.get(j);
								surveyQuestionID = singleGroup
										.getSurveyQuestionId();

								childern = singleGroup.getChilds();

								boolean anyChildHasValue = singleGroup
										.anyChildHasValue();

								if (!childern.isEmpty()) {

									JSONObject jsonobj;

									for (int k = 0; k < childern.size(); k++) {

										jsonobj = new JSONObject();

										singleChild = childern.get(k);
										competitorID = singleChild
												.getCompetitorID();

										productGroupID = productTypeID = comProductGroupNodeId;

										userResponseTemp = singleChild
												.getValue();

										if (Helper
												.isNullOrEmpty(userResponseTemp)
												&& !anyChildHasValue) {
											continue;
										} else if (Helper
												.isNullOrEmpty(userResponseTemp)
												&& anyChildHasValue) {
											userResponseTemp = "0";
										}

										try {
											userResponse = Integer
													.parseInt(userResponseTemp);
										} catch (NumberFormatException e1) {
											userResponse = 0;
										}

										jsonobj.put("UserRoleID", userRoleID);

										jsonobj.put("SurveyQuestionID",
												surveyQuestionID);
										jsonobj.put("CompetitionType",
												competitionType);
										jsonobj.put("ProductGroupID",
												productGroupID);
										jsonobj.put("UserResponse",
												userResponse);

										jsonobj.put("StoreID", storeId);

										try {
											jsonobj.put("CompetitorID", Integer
													.parseInt(competitorID));
										} catch (Exception e) {
											jsonobj.put("CompetitorID", -1);
										}
										jsonobj.put("UserID", userId);
										jsonobj.put("ProductTypeID",
												productTypeID);
										if (!Helper
												.isNullOrEmpty(Helper
														.getStringValuefromPrefs(
																CounterShareDisplayShareActivity.this,
																SharedPreferencesKey.PREF_COVERAGEID))) {
											jsonobj.put(
													WebConfig.WebParams.COVERAGEPLANID,
													Long.valueOf(Helper
															.getStringValuefromPrefs(
																	CounterShareDisplayShareActivity.this,
																	SharedPreferencesKey.PREF_COVERAGEID)));
										}

										elements.add(jsonobj);

									}

								}

							}

						}

					}

				}

				JSONArray jArray = new JSONArray(elements);

				if (jArray.length() == 0) {

					Helper.showCustomToast(getApplicationContext(),
							R.string.no_data_received, Toast.LENGTH_LONG);

					return;
				}

				if (mActivityID == -1) {
					// generate activity id

				/*	mActivityID = DatabaseHelper.getConnection(
							getApplicationContext()).insertActivtyDataMaster(
							mActivityData);*/
					
					ContentValues contentValues = DatabaseUtilMethods.getActivityDataContetnValueArray(mActivityData);
					Uri uri = getContentResolver().insert(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues);
					mActivityID = ContentUris.parseId(uri);
					
				} else {
				/*	DatabaseHelper.getConnection(getApplicationContext())
							.updateActivtyDataMaster(mActivityID, syncStatus);*/
					
					
					String where = ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID+ "=?";
					String[] selectionArgs = new String[] { String.valueOf(mActivityID) };
					
					
					ContentValues contentValues = DatabaseUtilMethods.getActivityDataContentValueUpdateStatus(syncStatus);
					getContentResolver().update(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues, where, selectionArgs);
					
					
				}
				// insert into transation table
				String whereClause = CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
						+ "=? AND "
						+ CounterShareDisplayShareResponseMasterColumns.KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE
						+ "=?";
				String[] whereArgs = new String[] {
						String.valueOf(mActivityID), String.valueOf(optionType) };

				getContentResolver()
						.delete(ProviderContract.URI_COUTER_SHARE_DISPLAY_SHARE_RESPONSE,
								whereClause, whereArgs);

				ContentValues[] values = DatabaseUtilMethods
						.getContentValueCounterShareDisplayShareResponse(this,
								mActivityID, jArray);
				getContentResolver()
						.bulkInsert(
								ProviderContract.URI_COUTER_SHARE_DISPLAY_SHARE_RESPONSE,
								values);

				/*
				 * DatabaseHelper.getConnection(getApplicationContext())
				 * .insertCounterShareDisplayShareRespnse(mActivityID,
				 * optionType, jArray);
				 */

				if (syncStatus == SyncUtils.SYNC_STATUS_SAVED) {
					Toast.makeText(getApplicationContext(),
							R.string.sync_message_data_save, Toast.LENGTH_LONG)
							.show();

				} else if (syncStatus == SyncUtils.SYNC_STATUS_SUBMIT) {
					Toast.makeText(getApplicationContext(),
							R.string.sync_message_data_submit,
							Toast.LENGTH_LONG).show();

				}

				finish();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), R.string.storing_failed,
					Toast.LENGTH_LONG).show();
		}

	}

	public void onCanceButtonlClick(View view) {
		finish();
	}

	/**
	 * Called when the proceed button get clicked
	 * 
	 * @param view
	 *            Button object which get clicked.
	 */
	public void onProceedButtonClick(View view) {

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {

			DialogInterface.OnClickListener buttonSubmitListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					onButtonTakeAction(SyncUtils.SYNC_STATUS_SUBMIT);

				}
			};

			DialogInterface.OnClickListener buttonSaveListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					onButtonTakeAction(SyncUtils.SYNC_STATUS_SAVED);
				}
			};

			Helper.showThreeButtonConfirmationDialog(this,
					R.string.sync_confirmation_dialog_title,
					R.string.sync_confirmation_dialog_message,
					R.string.sync_confirmation_dialog_button_one,
					R.string.sync_confirmation_dialog_button_two,
					R.string.sync_confirmation_dialog_button_negative,
					buttonSubmitListerner, buttonSaveListerner, null);

		} else {
			onButtonTakeAction(SyncUtils.SYNC_STATUS_SUBMIT);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {

		Loader<Cursor> loader = null;
		switch (loaderID) {
		case LOADER_ID_COMP_PRODUCT_GROUP:

			loader = new CursorLoader(this,
					ProviderContract.URI_COMP_PRODUCT_GROUP, null, null, null,
					null);

			break;

		case LOADER_ID_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE:

			loader = new CursorLoader(this,
					ProviderContract.URI_COUTER_SHARE_DISPLAY_SHARE_RESPONSE,
					null, null, new String[] { String.valueOf(mActivityID),
							String.valueOf(optionType) }, null);

			break;

	/*	case LOADER_ID_GET_ACTIVITY_ID:

			String[] arg = { String.valueOf(mActivityData.getStoreID()),
					String.valueOf(mActivityData.getModuleCode()) };

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null, arg,
					null);
			break;*/

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
					getSupportLoaderManager().initLoader(
							LOADER_ID_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE,
							null, this);
				}
				setUpComProductGroupView();
			}

			break;

		case LOADER_ID_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE:
			if (cursor != null && cursor.moveToFirst()) {

				mJArrayHistory = DatabaseUtilMethods
						.getCounterShareDisplayShareResponseFromCursor(cursor);

				this.groups = showValueFromStoredHistoryData(this.groups);

				mAdapter.notifyDataSetChanged();

			}
			break;

	/*	case LOADER_ID_GET_ACTIVITY_ID:

			if (cursor != null && cursor.getCount() > 0) {

				mActivityID = DatabaseUtilMethods.getActivityID(cursor);
				setUpView();

			}

			break;
*/
		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
