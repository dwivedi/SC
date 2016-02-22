package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.UnusedMandatoryModuleAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.ModuleCode;
import com.samsung.ssc.constants.QuestionType;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.Option;
import com.samsung.ssc.dto.Question;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.OnGridItemClick;
import com.samsung.ssc.util.PopupVisibilityListener;
import com.samsung.ssc.util.ViewComponentsUtil;

public class ActivityDashboardChild extends BaseActivity implements
		LoaderCallbacks<Cursor>,PopupVisibilityListener {

	private ArrayList<Module> modulesList;
	private ModuleGridAdapter adapter;
	private Stack<Integer> stackGridItems;
	SSCAlertDialog mDownloadListDialog = null;

	private int subParentID = -1;
	long mStoreID;

	private TextView tv_title;
	private HashMap<Integer, String> name_map = new HashMap<Integer, String>();
	private Bundle mBundle;
	private long mActivityID;
	private final int LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE = 1;
	private final int QUESTIONIRE_TYPE_TEMP_MODULECODE = -1;
	private Map<Integer, Integer> mapDownloadStatus;
	
	private boolean isPopupVisible = false;

	@Override
	public void init() {
		super.init();

		setContentView(R.layout.activitydashboard1);
		Toolbar toolbar  = (Toolbar)this.findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});


		tv_title = (TextView) findViewById(R.id.tv_title_sdActionBar);

		getSupportLoaderManager().initLoader(
				LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE, null, this);
		isPopupVisible = false;

	}

	private void setUpView() {

		mBundle = getIntent().getExtras();
		StoreBasicModel storeBasicModel = mBundle
				.getParcelable(IntentKey.KEY_STORE_BASIC);
		mStoreID = storeBasicModel.getStoreID();
		if (!isPopupVisible) {

			DatabaseHelper.getConnection(this).insertStoreAssessment(mStoreID);

		}
		stackGridItems = new Stack<Integer>();
		GridView moduleGridView = (GridView) findViewById(R.id.gridmenu);
		super.setParentLayout(moduleGridView);

		adapter = new ModuleGridAdapter();
		moduleGridView.setAdapter(adapter);

		int rootParentModuleID = getIntent().getExtras().getInt(
				IntentKey.KEY_MODULE_ID);
		try {

			modulesList = DatabaseHelper.getConnection(getApplicationContext())
					.getUserModuleByParentId(rootParentModuleID, mStoreID);

			if (!Helper
					.getBoolValueFromPrefs(
							getApplicationContext(),
							SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE)) {

				for (int i = 0; i < modulesList.size(); i++) {
					Module module = modulesList.get(i);
					if (module.getModuleCode() == ModuleCode.MENU_DISPLAYSHARE) {
						modulesList.remove(i);
						break;
					}
				}
				for (int i = 0; i < modulesList.size(); i++) {
					Module module = modulesList.get(i);
					if (module.getModuleCode() == ModuleCode.MENU_COUNTERSHARE) {
						modulesList.remove(i);
						break;
					}
				}
			}

			if (!Helper.getBoolValueFromPrefs(getApplicationContext(),
					SharedPreferencesKey.PREF_STORE_IS_PLANOGRAM)) {

				for (int i = 0; i < modulesList.size(); i++) {
					Module module = modulesList.get(i);
					if (module.getModuleCode() == ModuleCode.MENU_PALNOGRAM) {
						modulesList.remove(i);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		adapter.addAll(modulesList);

		moduleGridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							View convertView, int position, long id) {

						Module modulesDataModal = (Module) parent
								.getItemAtPosition(position);

						onGridItemClick(modulesDataModal);
					}
				});

		ImageButton ib_up = (ImageButton) findViewById(R.id.ib_up_sdActionBar);
		ib_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onBackPressed();
			}
		});

		if (name_map.containsKey(rootParentModuleID)) {

			tv_title.setText(name_map.get(rootParentModuleID));
			getSupportActionBar().setTitle(name_map.get(rootParentModuleID));
		} else {
			name_map.put(rootParentModuleID, "Activity");
			getSupportActionBar().setTitle(storeBasicModel.getStoreName());
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (stackGridItems != null && stackGridItems.size() > 0) {

			retrivePreviousModules();

		} else {
			setUpView();
		}

	}

	protected void onGridItemClick(Module modulesDataModal) {
		// Modified by Dwivedi Ji: 8/9/2015 for make store hybrid type
		if (modulesDataModal.getModuleType() == 1) {

			questionPopupWindow(modulesDataModal);

		} else if (modulesDataModal.isQuestionType()) {

			subParentID = -1;

			int parentId = modulesDataModal.getParentModuleID();

			if (!stackGridItems.contains(parentId)) {
				stackGridItems.add(parentId);
			}

			Intent intent = new Intent(ActivityDashboardChild.this,
					QuestionnaireActivity.class);
			intent.putExtra(IntentKey.MOUDLE_POJO, modulesDataModal);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);

		} else {

			showChildModuleOrActivityType(modulesDataModal);
		}
	}

	private void showChildModuleOrActivityType(
			Module modulesDataModal) {
		subParentID = modulesDataModal.getModuleID();
		int parentID = modulesDataModal.getParentModuleID();
		ArrayList<Module> subChildModule = DatabaseHelper
				.getConnection(this).getUserModuleByParentId(subParentID,
						mStoreID);
		if (!Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE)) {

			for (int i = 0; i < subChildModule.size(); i++) {
				Module module = subChildModule.get(i);
				if (module.getModuleCode() == ModuleCode.MENU_DISPLAYSHARE) {
					subChildModule.remove(i);
					break;
				}
			}
			for (int i = 0; i < subChildModule.size(); i++) {
				Module module = subChildModule.get(i);
				if (module.getModuleCode() == ModuleCode.MENU_COUNTERSHARE) {
					subChildModule.remove(i);
					break;
				}
			}
		}

		if (!Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_STORE_IS_PLANOGRAM)) {

			for (int i = 0; i < subChildModule.size(); i++) {
				Module module = subChildModule.get(i);
				if (module.getModuleCode() == ModuleCode.MENU_PALNOGRAM) {
					subChildModule.remove(i);
					break;
				}
			}
		}

		if (subChildModule.size() > 0) {

			if (!stackGridItems.contains(parentID)) {
				stackGridItems.push(parentID);

				String module_name = modulesDataModal.getName();
				tv_title.setText(module_name);
				getSupportActionBar().setTitle(module_name);
				name_map.put(subParentID, module_name);
			}
			restoreGridAdapter(subChildModule);

		} else {
			subParentID = -1;
			int parentId = modulesDataModal.getParentModuleID();

			/*
			 * if (!stackGridItems.contains(parentId)) {
			 * stackGridItems.push(parentId); }
			 */
			if (modulesDataModal.getModuleCode() != ModuleCode.MENU_SYNCING) {
				stackGridItems.push(parentId);
			}

			new OnGridItemClick(ActivityDashboardChild.this, mBundle)
					.onActivityItemClick(modulesDataModal);
		}
	}

	private void questionPopupWindow(final Module module) {

		final ActivityDataMasterModel mActivityData = Helper
				.getActivityDataMasterModel(getApplicationContext(), module);

		mActivityData.setSyncStatus(SyncUtils.SYNC_STATUS_SUBMIT);
		
		if (mActivityData != null) {

			mActivityID = DatabaseHelper.getConnection(getApplicationContext())
					.getActivityIdIfExist(mActivityData);

			ArrayList<Question> independentQuestions = DatabaseHelper
					.getConnection(getApplicationContext()).getQuestionsData2(
							module.getModuleID(), 0, mActivityID);

			ViewComponentsUtil viewUtil = new ViewComponentsUtil(
					ActivityDashboardChild.this, mStoreID, mActivityID); // SDCE-4364

			View view = viewUtil.drawdynamicComponents(independentQuestions
					.get(0));

			showPopupSmartDost(module, mActivityData, view);

			// showPopupNative(module, mActivityData, view);
		}

	}

	private void showPopupNative(final Module module,
			final ActivityDataMasterModel mActivityData, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityDashboardChild.this);

		final LinearLayout containerLinearLayout = new LinearLayout(
				getApplicationContext());
		if (view != null) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(Helper.dpToPixel(getApplicationContext(), 10),
					Helper.dpToPixel(getApplicationContext(), 10),
					Helper.dpToPixel(getApplicationContext(), 10),
					Helper.dpToPixel(getApplicationContext(), 10));

			containerLinearLayout.addView(view, params);

			builder.setView(containerLinearLayout);
		}

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				LinearLayout childRootView = containerLinearLayout
						.getChildAt(0) instanceof LinearLayout ? (LinearLayout) containerLinearLayout
						.getChildAt(0) : null;

				Question questionModal = (Question) childRootView.getTag();
				if (questionModal != null) {
					boolean isMandatory = questionModal.isMandatory();
					if (questionModal.getQuestionTypeId() == QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON) {
						try {

							RadioGroup rg = (RadioGroup) childRootView
									.getChildAt(1);

							if (isMandatory
									&& rg.getCheckedRadioButtonId() == -1) {
								dialog.dismiss();
								return;
							}

							if (rg.getCheckedRadioButtonId() != -1) {

								JSONArray questionJson = new JSONArray();
								RadioButton radioButton = (RadioButton) rg
										.findViewById(rg
												.getCheckedRadioButtonId());
								String text = radioButton.getText().toString();

								boolean isAffirmative = ((Option) radioButton
										.getTag()).isIsAffirmative();

								JSONObject questionJsonObject = new JSONObject();
								questionJsonObject.put(
										WebConfig.WebParams.SURVEYQUESTIONID,
										questionModal.getSurveyQuestionId());
								questionJsonObject.put(
										WebConfig.WebParams.USERRESPONSE, text);

								questionJsonObject
										.put(WebConfig.WebParams.QUESTION_TYPE_ID,
												QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON);

								questionJson.put(questionJsonObject);

								if (mActivityID == -1) {

									mActivityID = DatabaseHelper.getConnection(
											getApplicationContext())
											.insertActivtyDataMaster(
													mActivityData);
								}

								DatabaseHelper.getConnection(
										getApplicationContext())
										.insertQuestionAnswerResponse(
												mActivityID, questionJson);
								DatabaseHelper
										.getConnection(getApplicationContext())
										.updateStoreWiseModuleMandatoryStatus(
												module, isAffirmative, mStoreID);

								if (isAffirmative) {
									showChildModuleOrActivityType(module);

								} else {
									dialog.dismiss();
								}

								if (module.isStoreWise()) {
									DatabaseHelper.getConnection(
											getApplicationContext())
											.updateStoreModulesActivity(
													mStoreID,
													module.getModuleID(),
													mActivityID);

								}

							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		builder.create().show();
	}

	private void showPopupSmartDost(final Module module,
			final ActivityDataMasterModel mActivityData, View view) {

		SSCAlertDialog dialogFrame = new SSCAlertDialog(
				ActivityDashboardChild.this,
				SSCAlertDialog.LEYOUT_FRAME_TYPE)
				.setTitleText("Permission")
				.setLayoutFrameView(view)

				.setEnableConfirmButton(true)
				.setConfirmText("OK")
				.setConfirmClickListener(
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {

								LinearLayout containerLinearLayout = sdAlertDialog
										.getLayoutFrameView();

								if (containerLinearLayout != null) {
									LinearLayout childRootView = containerLinearLayout
											.getChildAt(0) instanceof LinearLayout ? (LinearLayout) containerLinearLayout
											.getChildAt(0) : null;

									Question questionModal = (Question) childRootView
											.getTag();
									if (questionModal != null) {
										boolean isMandatory = questionModal
												.isMandatory();
										if (questionModal.getQuestionTypeId() == QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON) {
											try {

												RadioGroup rg = (RadioGroup) childRootView
														.getChildAt(1);

												if (isMandatory
														&& rg.getCheckedRadioButtonId() == -1) {
													sdAlertDialog.dismiss();
													return;
												}

												if (rg.getCheckedRadioButtonId() != -1) {

													JSONArray questionJson = new JSONArray();
													RadioButton radioButton = (RadioButton) rg.findViewById(rg
															.getCheckedRadioButtonId());
													String text = radioButton
															.getText()
															.toString();

													boolean isAffirmative = ((Option) radioButton
															.getTag())
															.isIsAffirmative();

													JSONObject questionJsonObject = new JSONObject();
													questionJsonObject
															.put(WebConfig.WebParams.SURVEYQUESTIONID,
																	questionModal
																			.getSurveyQuestionId());
													questionJsonObject
															.put(WebConfig.WebParams.USERRESPONSE,
																	text);

													questionJsonObject
															.put(WebConfig.WebParams.QUESTION_TYPE_ID,
																	QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON);

													questionJson
															.put(questionJsonObject);

													if (mActivityID == -1) {

														mActivityID = DatabaseHelper
																.getConnection(
																		getApplicationContext())
																.insertActivtyDataMaster(
																		mActivityData);
													} else {
														DatabaseHelper
																.getConnection(
																		getApplicationContext())
																.updateActivtyDataMaster(
																		mActivityID,
																		mActivityData
																				.getSyncStatus());

													}

													DatabaseHelper
															.getConnection(
																	getApplicationContext())
															.insertQuestionAnswerResponse(
																	mActivityID,
																	questionJson);
													DatabaseHelper
															.getConnection(
																	getApplicationContext())
															.updateStoreWiseModuleMandatoryStatus(
																	module,
																	isAffirmative,
																	mStoreID);

													if (isAffirmative) {
														showChildModuleOrActivityType(module);
													} else {
														ActivityDashboardChild.this
																.onPause(); // SDCE-4366
														ActivityDashboardChild.this
																.onResume();
													}
													sdAlertDialog.dismiss();

													if (module.isStoreWise()) {
														DatabaseHelper
																.getConnection(
																		getApplicationContext())
																.updateStoreModulesActivity(
																		mStoreID,
																		module.getModuleID(),
																		mActivityID);

													}

												}
											} catch (Exception e1) {
												e1.printStackTrace();
											}
										}
									}

								}
							}

						}).showCancelButton(false).setCancelText(null)
				.setCancelClickListener(null);

		dialogFrame.show();

	}

	@Override
	public void onBackPressed() {

		if (stackGridItems.size() > 0) {

			retrivePreviousModules();

		} else {

			ArrayList<Module> moduleList = DatabaseHelper
					.getConnection(getApplicationContext())
					.getStoreWisePendingMandatoryModule1(mStoreID);

			moduleList = deleteUnasignedModules(moduleList);

			if (moduleList.size() > 0) {

				showPendingMandatoryModules(moduleList);

			} else {

				DatabaseHelper.getConnection(this).updateStoreAssessment(
						mStoreID);
				finish();
			}

		}

	}

	private void retrivePreviousModules() {

		subParentID = stackGridItems.pop();

		if (name_map.containsKey(subParentID)) {
			String module_name = name_map.get(subParentID);
			tv_title.setText(module_name);
			getSupportActionBar().setTitle(module_name);
		}

		ArrayList<Module> subChildModule = DatabaseHelper
				.getConnection(this).getUserModuleByParentId(subParentID,
						mStoreID);
		adapter.clean();
		adapter.addAll(subChildModule);

	}

	private void restoreGridAdapter(ArrayList<Module> subChildModule) {
		adapter.clean();
		adapter.addAll(subChildModule);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (stackGridItems.size() != 0 && subParentID != -1
				&& !stackGridItems.contains(subParentID)) {
			stackGridItems.push(subParentID);
		}
	}

	private class ModuleGridAdapter extends BaseAdapter {
		private ArrayList<Module> dataset;
		private LayoutInflater inflater;
	
		
		ModuleGridAdapter() {
			dataset = new ArrayList<Module>();
			this.inflater = LayoutInflater.from(getApplicationContext());

		}

		@Override
		public int getCount() {
			if(dataset==null)
			{
				return 0;
			}
			return dataset.size();
		}

		@Override
		public Object getItem(int index) {
			return dataset.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			 int downloadStatus = 1;

			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.dashboardview, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Module moduleDisplayDto = dataset.get(position);

			if (mapDownloadStatus != null
					&& (mapDownloadStatus.containsKey(moduleDisplayDto
							.getModuleCode()) | moduleDisplayDto
							.isQuestionType()))

			{
				if (moduleDisplayDto.isQuestionType()) {
					downloadStatus = mapDownloadStatus
							.get(QUESTIONIRE_TYPE_TEMP_MODULECODE);

				} else {
					downloadStatus = mapDownloadStatus.get(moduleDisplayDto
							.getModuleCode());
				}

			}

			String icon_name = moduleDisplayDto.getIconName();
			if (icon_name != null && !icon_name.isEmpty()) {
				String iconSplitpng = icon_name.split("\\.")[0];

				int resourceID = getResources().getIdentifier(iconSplitpng,
						"drawable", getPackageName());

				if (moduleDisplayDto.isIsMandatory()) {
					if (moduleDisplayDto.isActivityIDAvailable()) {

						if (moduleDisplayDto.getSyncStatus() == SyncUtils.SYNC_STATUS_SUBMIT) {
							holder.icon
									.setBackgroundDrawable(ActivityDashboardChild.this
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_green_border));
						} else if (moduleDisplayDto.getSyncStatus() == SyncUtils.SYNC_STATUS_SAVED) {
							holder.icon
									.setBackgroundDrawable(ActivityDashboardChild.this
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_yellow_border));
						}

					} else {
						holder.icon
								.setBackgroundDrawable(ActivityDashboardChild.this
										.getResources()
										.getDrawable(
												R.drawable.grid_item_selector_red_border));
					}

				} else {
					holder.icon
							.setBackgroundDrawable(ActivityDashboardChild.this
									.getResources().getDrawable(
											R.drawable.grid_item_selector));
				}

				if (resourceID != 0) {

					// holder.icon.setImageResource(resourceID);

					Drawable drawable = getApplicationContext().getResources()
							.getDrawable(resourceID);
					holder.icon.setImageDrawable(drawable);

				} else {
					Drawable drawable = getApplicationContext().getResources()
							.getDrawable(R.drawable.refresh);
					holder.icon.setImageDrawable(drawable); // Default
															// icon
				}
			} else {
				Drawable drawable = getApplicationContext().getResources()
						.getDrawable(R.drawable.refresh);
				holder.icon.setImageDrawable(drawable); // Default
														// icon
			}

			holder.text.setText(moduleDisplayDto.getName());
			// convertView.startAnimation(animation);

			if (downloadStatus == 0) {
				Drawable drawable = holder.icon.getDrawable();
				drawable = Helper.convertDrawableToGrayScale(drawable);
				if (drawable != null) {
					holder.icon.setImageDrawable(drawable);

				}

				holder.icon.setFocusable(true);
				holder.icon.setFocusableInTouchMode(true);
				
			}
			else
			{

				holder.icon.setFocusable(false);
				holder.icon.setFocusableInTouchMode(false);
			}
			return convertView;
		}

		private class ViewHolder {
			ViewHolder(View convertView) {
				this.text = (TextView) convertView
						.findViewById(R.id.grid_item_label);
				this.icon = (ImageButton) convertView
						.findViewById(R.id.grid_item_image);

				this.icon.setFocusable(false);
				this.icon.setFocusableInTouchMode(false);
				
				
			}

			TextView text;
			ImageButton icon;
		}

		void addAll(List<Module> moduleDataModals) {

			this.dataset.addAll(moduleDataModals);
			notifyDataSetChanged();
		}

		void clean() {
			this.dataset.clear();

		}

		@SuppressWarnings({ "unchecked", "unused" })
		public ArrayList<Module> getAll() {
			return (ArrayList<Module>) this.dataset.clone();
		}

		

	}

	protected void parseFileResponse(Object result) {
		new FetchingdataParser(this).getDisplayModules(result.toString());

	}

	private void showPendingMandatoryModules(
			ArrayList<Module> modules) {

		final UnusedMandatoryModuleAdapter unUsedmoduleAdapter = new UnusedMandatoryModuleAdapter(
				this);

		unUsedmoduleAdapter.addItems(modules);

		mDownloadListDialog = new SSCAlertDialog(
				ActivityDashboardChild.this, SSCAlertDialog.LIST_TYPE)
				.setTitleText(getString(R.string.pending_mandatory_modules))
				.setListAdapter(unUsedmoduleAdapter)
				/*
				 * .setListItemClickListener(new OnItemClickListener() {
				 * 
				 * @Override public void onItemClick(AdapterView<?> parent, View
				 * view, int position, long id) {
				 * 
				 * Module moduleDisplayDto = (Module)
				 * parent .getItemAtPosition(position); if (mDownloadListDialog
				 * != null) { mDownloadListDialog.dismiss(); }
				 * 
				 * if (moduleDisplayDto.isQuestionType()) { Intent intent = new
				 * Intent( ActivityDashboardChild.this,
				 * QuestionnaireActivity.class);
				 * intent.putExtra(IntentKey.MOUDLE_POJO, moduleDisplayDto);
				 * startActivity(intent);
				 * overridePendingTransition(R.anim.right_in, R.anim.left_out);
				 * 
				 * } else { new OnGridItemClick(ActivityDashboardChild.this,
				 * mBundle) .onActivityItemClick(moduleDisplayDto);
				 * 
				 * }
				 * 
				 * } })
				 */
				.setEnableConfirmButton(true)
				.setConfirmText("OK")
				.setConfirmClickListener(
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {

								sdAlertDialog.dismiss();
								if (stackGridItems.size() > 0) {
									retrivePreviousModules();

								} else {

									DatabaseHelper.getConnection(
											ActivityDashboardChild.this)
											.updateStoreAssessment(mStoreID);
									finish();
								}

							}

						})
				.showCancelButton(true)
				.setCancelText(getString(R.string.cancel))
				.setCancelClickListener(
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();

							}
						});

		if (unUsedmoduleAdapter.getDataSet().size() > 0) {
			mDownloadListDialog.show();
		} else {
			DatabaseHelper.getConnection(this).updateStoreAssessment(mStoreID);
			finish();
		}

	}

	private ArrayList<Module> deleteUnasignedModules(
			ArrayList<Module> subChildModule) {

		if (!Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE)) {

			for (int i = 0; i < subChildModule.size(); i++) {
				Module module = subChildModule.get(i);
				if (module.getModuleCode() == ModuleCode.MENU_DISPLAYSHARE) {
					subChildModule.remove(i);
					break;
				}

			}
			for (int i = 0; i < subChildModule.size(); i++) {
				Module module = subChildModule.get(i);
				if (module.getModuleCode() == ModuleCode.MENU_COUNTERSHARE) {
					subChildModule.remove(i);
					break;
				}

			}

		}

		if (!Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_STORE_IS_PLANOGRAM)) {

			for (int i = 0; i < subChildModule.size(); i++) {
				Module module = subChildModule.get(i);
				if (module.getModuleCode() == ModuleCode.MENU_PALNOGRAM) {
					subChildModule.remove(i);
					break;
				}
			}
		}

		return subChildModule;

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle arg1) {

		CursorLoader cursorLoader = null;
		if (loaderId == LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE) {

			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_DOWNLOAD_DATA_SINGLE_SERVICE, null,
					null, null, null);

		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor arg1) {

		switch (loader.getId()) {
		case LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE:
			
			Cursor cursor = getContentResolver().query(
					ProviderContract.URI_MODULE_DATA_DOWNLOAD_STATUS, null,
					null, null, null);

			mapDownloadStatus= DatabaseUtilMethods
					.getDownloadStatusMapFromCursor(cursor);
			
			adapter.notifyDataSetChanged();
			

			break;

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void isPopupVisible(boolean isVisible) {

		isPopupVisible = isVisible;
	}


}
