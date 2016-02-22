package com.samsung.ssc.activitymodule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.QuestionType;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseConstants;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.StoreModuleMasterColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.Option;
import com.samsung.ssc.dto.Question;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.ScalingUtilities;
import com.samsung.ssc.util.ViewComponentsUtil;
import com.samsung.ssc.util.ScalingUtilities.ScalingLogic;

public class QuestionnaireActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	protected static LinearLayout containerLinearLayout;
	private int moduleId;
	private ViewComponentsUtil viewUtil;
	private ActivityDataMasterModel mActivityData;
	private long mActivityID = -1;
	private ScrollView containerScrollView;
	private long mStoreID;
	private ContentResolver mContentResolver;
	private static final int LOADER_ID = 1;
	private static final int LOADER_ID_GET_ACTIVITY_ID = 2;

	ProgressDialog progressDialog;
	Handler mHandler;
	protected ArrayList<Question> independentQuestions;
	private Module mModule;

	@Override
	public void onCreate(Bundle  bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.activity_questionnaire1);

		Toolbar toolbar =(Toolbar)this.findViewById(R.id.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportActionBar(toolbar);

		getBundleValue();

	}

	private void initialize() {
		File folder = new File(FileDirectory.DIRECTORY_SURVEY_QUESTION_IMAGES
				+ "/" + mStoreID);

		if (!folder.exists())
			folder.mkdirs();

		viewUtil = new ViewComponentsUtil(QuestionnaireActivity.this, mStoreID,
				mActivityID);

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);

				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

				if (independentQuestions != null
						&& independentQuestions.size() > 0) {
					for (Question question : independentQuestions) {

						View view = viewUtil.drawdynamicComponents(question);
						if (view != null) {
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);
							params.setMargins(Helper.dpToPixel(
									QuestionnaireActivity.this, 10), Helper
									.dpToPixel(QuestionnaireActivity.this, 10),
									Helper.dpToPixel(
											QuestionnaireActivity.this, 10),
									Helper.dpToPixel(
											QuestionnaireActivity.this, 10));
							containerLinearLayout.addView(view, params);
						}
					}

				} else {

					Helper.showAlertDialog(
							QuestionnaireActivity.this,
							SSCAlertDialog.ERROR_TYPE,
							getString(R.string.error3),
							getString(R.string.no_option_found),
							getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();
									finish();
								}
							}, false, null, null);

				}

			}
		};

		getSupportLoaderManager().initLoader(LOADER_ID, null, this);

	}
 

	public void onCancelClick(View v) {

		Helper.showAlertDialog(
				QuestionnaireActivity.this,
				SSCAlertDialog.WARNING_TYPE,
				getString(R.string.cancel),
				getString(R.string.are_you_sure_you_want_to_navigate_away_from_this_page_saved_data_will_be_lost_),
				getString(R.string.ok),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						finish();
					}
				}, true, getString(R.string.cancel),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});
	}

	@Override
	public void onBackPressed() {

		Helper.showAlertDialog(
				QuestionnaireActivity.this,
				SSCAlertDialog.WARNING_TYPE,
				getString(R.string.cancel),
				getString(R.string.are_you_sure_you_want_to_navigate_away_from_this_page_saved_data_will_be_lost_),
				getString(R.string.ok),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						finish();
					}
				}, true, getString(R.string.cancel),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});
	}

	private void setUpViews() {

		mContentResolver = getApplicationContext().getContentResolver();

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
			Button bt_proceed = (Button) findViewById(R.id.bt_proceed_questionnaireActivty);
			bt_proceed.setText(getString(R.string.proceed));
		}
		containerLinearLayout = (LinearLayout) findViewById(R.id.activity_questionnaire_linearlayout_container);
		containerLinearLayout.removeAllViews();
		containerScrollView = (ScrollView) this
				.findViewById(R.id.activity_questionnaire_scrolllayout_container);

	}

	public void onButtonTakeAction(final int syncStatus) {

		new AsyncTask<Void, View, Boolean>() {

			private ProgressDialog progressDialog;
			private boolean isEmptyError;

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					isEmptyError = false;
					JSONArray questionJson = new JSONArray();

					if (containerLinearLayout != null
							&& containerLinearLayout.getChildCount() > 0) {

						int childCount = containerLinearLayout.getChildCount();
						boolean dataStatus = false;
						for (int i = 0; i < childCount; i++) {
							LinearLayout childRootView = (LinearLayout) containerLinearLayout
									.getChildAt(i);

							if (childRootView.getTag() instanceof Question) {
								dataStatus = saveAnswers(childRootView,
										questionJson, syncStatus);
								if (!dataStatus) {

									publishProgress(childRootView);

									return dataStatus;
								}
							}
						}

						if (dataStatus) {
							if (questionJson.length() > 0) {
								mActivityData.setSyncStatus(syncStatus);
								if (mActivityID == -1) {
									
									ContentValues contentValues = DatabaseUtilMethods
											.getActivityDataContetnValueArray(mActivityData);
									Uri uri = getContentResolver()
											.insert(ProviderContract.URI_ACTIVITY_DATA_RESPONSE,
													contentValues);
									mActivityID = ContentUris.parseId(uri);

								} else {
									
									String where = ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
											+ "=?";
									String[] selectionArgs = new String[] { String
											.valueOf(mActivityID) };

									ContentValues contentValues = DatabaseUtilMethods
											.getActivityDataContentValueUpdateStatus(syncStatus);
									getContentResolver()
											.update(ProviderContract.URI_ACTIVITY_DATA_RESPONSE,
													contentValues, where,
													selectionArgs);

								}
																

								String whereClause = DatabaseConstants.QuestionAnswerResponseColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
										+ "=?";
								String[] whereArgs = new String[] { String
										.valueOf(mActivityID) };

								mContentResolver
										.delete(ProviderContract.DELELTE_SURVEY_QUESTION_ANSWER_RESPONSE_URI,
												whereClause, whereArgs);

								mContentResolver
										.bulkInsert(
												ProviderContract.INSERT_SURVEY_QUESTION_ANSWER_RESPONSE_URI,
												
												DatabaseUtilMethods
														.getQuestionAnswerResponse(
																questionJson,
																mActivityID));
								if (mModule.isStoreWise()) {

									ContentValues contentValues = new ContentValues();
									contentValues
											.put(StoreModuleMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID,
													mActivityID);
									mContentResolver
											.update(ProviderContract.URI_STORE_MODULE_MASTER,
													contentValues,
													StoreModuleMasterColumns.KEY_STORE_ID
															+ " =? "
															+ " AND "
															+ StoreModuleMasterColumns.KEY_MODULE_ID
															+ " = ?",
													new String[] {
															String.valueOf(mStoreID),
															String.valueOf(moduleId) });

								}

							 
								return true;
							} else {
								isEmptyError = true;
								return false;
							}
						} else {
							return false;
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}

			protected void onProgressUpdate(View... values) {

				final View view = values[0];

				containerScrollView.post(new Runnable() {

					@Override
					public void run() {
						containerScrollView.scrollTo(0, getRelativeTop(view));

						view.setBackgroundResource(R.drawable.questionire_item_background_error);

						final TransitionDrawable background = (TransitionDrawable) view
								.getBackground();
						background.startTransition(1500);
					}
				});

			}

			protected void onPreExecute() {

				try {
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {

						progressDialog = SSCProgressDialog
								.ctor(QuestionnaireActivity.this);

					} else {
						progressDialog = new ProgressDialog(
								QuestionnaireActivity.this);
						progressDialog.setProgress(0);
						progressDialog.setMax(100);
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog
								.setMessage(getString(R.string.saving_question_data));
						progressDialog.setCancelable(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			protected void onPostExecute(Boolean result) {
				try {
					progressDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (result.booleanValue()) {

					Helper.showCustomToast(getApplicationContext(),
							R.string.data_saved_successfully, Toast.LENGTH_LONG);

					finish();
				} else {
					if (isEmptyError) {
						Helper.showCustomToast(getApplicationContext(),
								R.string.no_data_to_proceed, Toast.LENGTH_LONG);

					} else {
						Helper.showCustomToast(
								getApplicationContext(),
								R.string.please_attend_the_mandatory_question_to_proceed_,
								Toast.LENGTH_LONG);

					}

				}
			}

		}.execute();

	}

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

		}

		else {
			onButtonTakeAction(SyncUtils.SYNC_STATUS_SUBMIT);
		}

	}

	private boolean saveAnswers(LinearLayout childRootView,
			JSONArray questionJson, int syncStatus) {
		try {
			Question questionModal = (Question) childRootView.getTag();

			boolean isMandatory = questionModal.isMandatory();
			isMandatory = isMandatory
					&& syncStatus == SyncUtils.SYNC_STATUS_SUBMIT;
			switch (questionModal.getQuestionTypeId()) {

			case QuestionType.SURVEY_QUESTION_TYPE_CALENDAR:

				try {

					EditText etCakendar = (EditText) childRootView
							.getChildAt(1);

					if (isMandatory
							&& etCakendar.getText().toString()
									.equalsIgnoreCase("DD/MM/YYYY")) {
						return false;
					}

					if (!etCakendar.getText().toString()
							.equalsIgnoreCase("DD/MM/YYYY")) {

						JSONObject questionJsonObject = new JSONObject();
						int surveyQuestionId = questionModal
								.getSurveyQuestionId();
						if (surveyQuestionId == -1) {
							questionJsonObject.put(
									WebConfig.WebParams.SURVEY_QUESTION_SUB_ID,
									questionModal.getSubQuestionID());
						}
						questionJsonObject.put(
								WebConfig.WebParams.SURVEYQUESTIONID,
								surveyQuestionId);
						questionJsonObject.put(
								WebConfig.WebParams.USERRESPONSE, etCakendar
										.getText().toString().trim());

						questionJsonObject.put(
								WebConfig.WebParams.QUESTION_TYPE_ID,
								QuestionType.SURVEY_QUESTION_TYPE_CALENDAR);

						questionJson.put(questionJsonObject);

					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;

			case QuestionType.SURVEY_QUESTION_TYPE_CHECKBOX:
				try {
					LinearLayout checkBoxContainer = (LinearLayout) childRootView
							.getChildAt(1);

					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < checkBoxContainer.getChildCount(); j++) {
						if (((CheckBox) checkBoxContainer.getChildAt(j))
								.isChecked()) {
							Option option = (Option) checkBoxContainer
									.getChildAt(j).getTag();
							sb.append(option.getOptionValue() + "@");
						}
					}

					if (isMandatory && sb.length() < 1) {
						return false;
					}

					if (sb.length() > 1) {
						JSONObject questionJsonObject = new JSONObject();
						questionJsonObject.put(
								WebConfig.WebParams.SURVEYQUESTIONID,
								questionModal.getSurveyQuestionId());
						questionJsonObject.put(
								WebConfig.WebParams.USERRESPONSE, sb.toString()
										.trim().substring(0, sb.length() - 1)
										.trim());

						questionJsonObject.put(
								WebConfig.WebParams.QUESTION_TYPE_ID,
								QuestionType.SURVEY_QUESTION_TYPE_CHECKBOX);

						questionJson.put(questionJsonObject);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			case QuestionType.SURVEY_QUESTION_TYPE_REPEATOR:

				Spinner spinnerRepeater = (Spinner) childRootView.getChildAt(1);
				int tagData = (Integer) spinnerRepeater.getTag();
				if (isMandatory && tagData == -1) {
					return false;
				}
				if (tagData != -1) {

					JSONObject questionJsonObject = new JSONObject();

					questionJsonObject.put(
							WebConfig.WebParams.SURVEYQUESTIONID,
							questionModal.getSurveyQuestionId());
					questionJsonObject.put(WebConfig.WebParams.USERRESPONSE,
							String.valueOf(tagData));

					questionJsonObject.put(
							WebConfig.WebParams.QUESTION_TYPE_ID,
							QuestionType.SURVEY_QUESTION_TYPE_REPEATOR);

					questionJson.put(questionJsonObject);

					LinearLayout repeateLayoutContainer = (LinearLayout) childRootView
							.getChildAt(2);

					int totalRepeateCount = repeateLayoutContainer
							.getChildCount();

					for (int i = 0; i < totalRepeateCount; i++) {
						LinearLayout view = (LinearLayout) repeateLayoutContainer
								.getChildAt(i);
						boolean b = saveAnswers(view, questionJson, syncStatus);
						if (!b) {
							return false;
						}
					}

				}

				break;
			case QuestionType.SURVEY_QUESTION_TYPE_DROPDOWN:

				try {

					Spinner spinner = (Spinner) childRootView.getChildAt(1);
					String tag = (String) spinner.getTag();
					if (isMandatory && tag == null) {
						return false;
					}

					if (tag != null) {

						JSONObject questionJsonObject = new JSONObject();

						questionJsonObject.put(
								WebConfig.WebParams.SURVEYQUESTIONID,
								questionModal.getSurveyQuestionId());

						questionJsonObject.put(
								WebConfig.WebParams.USERRESPONSE, tag);

						questionJsonObject.put(
								WebConfig.WebParams.QUESTION_TYPE_ID,
								QuestionType.SURVEY_QUESTION_TYPE_DROPDOWN);

						questionJson.put(questionJsonObject);

					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				break;
			case QuestionType.SURVEY_QUESTION_TYPE_NUMERICTEXT:

				try {
					EditText ed = (EditText) childRootView.getChildAt(1);

					if (isMandatory && ed.getText().toString().length() < 1) {
						return false;
					}

					if (ed.getText().toString().length() > 0) {
						JSONObject questionJsonObject = new JSONObject();
						int surveyQuestionId = questionModal
								.getSurveyQuestionId();
						questionJsonObject.put(
								WebConfig.WebParams.SURVEYQUESTIONID,
								surveyQuestionId);
						if (surveyQuestionId == -1) {
							questionJsonObject.put(
									WebConfig.WebParams.SURVEY_QUESTION_SUB_ID,
									questionModal.getSubQuestionID());
						}

						questionJsonObject.put(
								WebConfig.WebParams.USERRESPONSE, ed.getText()
										.toString());

						questionJsonObject.put(
								WebConfig.WebParams.QUESTION_TYPE_ID,
								QuestionType.SURVEY_QUESTION_TYPE_NUMERICTEXT);

						questionJson.put(questionJsonObject);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			case QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX:

				try {
					ImageView iv = (ImageView) childRootView.getChildAt(1);
					try {
						String imagePath = (String) iv.getTag();

						if (isMandatory && imagePath == null) {
							return false;
						}
						if (imagePath != null) {

							JSONObject questionJsonObject = new JSONObject();
							int surveyQuestionId = questionModal
									.getSurveyQuestionId();
							questionJsonObject.put(
									WebConfig.WebParams.SURVEYQUESTIONID,
									surveyQuestionId);
							if (surveyQuestionId == -1) {
								questionJsonObject
										.put(WebConfig.WebParams.SURVEY_QUESTION_SUB_ID,
												questionModal
														.getSubQuestionID());
							}
							questionJsonObject
									.put(WebConfig.WebParams.USERRESPONSE,
											imagePath);

							questionJsonObject
									.put(WebConfig.WebParams.QUESTION_TYPE_ID,
											QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX);

							questionJson.put(questionJsonObject);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			case QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON:
				try {

					RadioGroup rg = (RadioGroup) childRootView.getChildAt(1);
					if (isMandatory && rg.getCheckedRadioButtonId() == -1) {
						return false;
					}

					if (rg.getCheckedRadioButtonId() != -1) {

						RadioButton radioButton = (RadioButton) rg
								.findViewById(rg.getCheckedRadioButtonId());
						String text = radioButton.getText().toString();

						JSONObject questionJsonObject = new JSONObject();
						questionJsonObject.put(
								WebConfig.WebParams.SURVEYQUESTIONID,
								questionModal.getSurveyQuestionId());
						questionJsonObject.put(
								WebConfig.WebParams.USERRESPONSE, text);

						questionJsonObject.put(
								WebConfig.WebParams.QUESTION_TYPE_ID,
								QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON);

						questionJson.put(questionJsonObject);

						/**
						 * Fetch the data from the visible dependent Question of
						 * this option.IF any
						 */
						Option option = (Option) radioButton.getTag();
						LinearLayout bodylinearLayoutChild = (LinearLayout) childRootView
								.findViewWithTag(option.getSurveyOptionId());
						if (bodylinearLayoutChild != null
								&& bodylinearLayoutChild.getVisibility() == View.VISIBLE) {
							for (int i = 0; i < bodylinearLayoutChild
									.getChildCount(); i++) {
								LinearLayout dependentQuestionRootLayout = (LinearLayout) bodylinearLayoutChild
										.getChildAt(i);
								boolean a = saveAnswers(
										dependentQuestionRootLayout,
										questionJson, syncStatus);
								if (!a) {
									return a;
								}

							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			case QuestionType.SURVEY_QUESTION_TYPE_RATING:

				try {
					RatingBar ratingBar = (RatingBar) childRootView
							.getChildAt(1);
					float rating = ratingBar.getRating();

					if (isMandatory && rating == 0.0) {
						return false;
					}

					if (rating != 0.0) {
						JSONObject questionJsonObject = new JSONObject();
						int surveyQuestionId = questionModal
								.getSurveyQuestionId();
						questionJsonObject.put(
								WebConfig.WebParams.SURVEYQUESTIONID,
								surveyQuestionId);
						if (surveyQuestionId == -1) {
							questionJsonObject.put(
									WebConfig.WebParams.SURVEY_QUESTION_SUB_ID,
									questionModal.getSubQuestionID());
						}

						questionJsonObject.put(
								WebConfig.WebParams.USERRESPONSE,
								String.valueOf(rating));

						questionJsonObject.put(
								WebConfig.WebParams.QUESTION_TYPE_ID,
								QuestionType.SURVEY_QUESTION_TYPE_RATING);
						questionJson.put(questionJsonObject);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case QuestionType.SURVEY_QUESTION_TYPE_TEXTBOX:
				try {
					EditText ed1 = (EditText) childRootView.getChildAt(1);

					if (isMandatory && ed1.getText().toString().length() < 1) {
						return false;
					}

					if (ed1.getText().toString().length() > 0) {
						JSONObject questionJsonObject = new JSONObject();
						int surveyQuestionId = questionModal
								.getSurveyQuestionId();
						questionJsonObject.put(
								WebConfig.WebParams.SURVEYQUESTIONID,
								surveyQuestionId);
						if (surveyQuestionId == -1) {
							questionJsonObject.put(
									WebConfig.WebParams.SURVEY_QUESTION_SUB_ID,
									questionModal.getSubQuestionID());
						}

						questionJsonObject.put(
								WebConfig.WebParams.USERRESPONSE, ed1.getText()
										.toString());

						questionJsonObject.put(
								WebConfig.WebParams.QUESTION_TYPE_ID,
								QuestionType.SURVEY_QUESTION_TYPE_TEXTBOX);

						questionJson.put(questionJsonObject);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case QuestionType.SURVEY_QUESTION_TYPE_TOGGLEBUTTON:
				try {
					ToggleButton toggleButton = (ToggleButton) childRootView
							.getChildAt(1);
					String userResponse = "OFF";
					if (toggleButton.isChecked()) {
						userResponse = "ON";
					}

					JSONObject questionJsonObject = new JSONObject();
					int surveyQuestionId = questionModal.getSurveyQuestionId();
					questionJsonObject.put(
							WebConfig.WebParams.SURVEYQUESTIONID,
							surveyQuestionId);
					if (surveyQuestionId == -1) {
						questionJsonObject.put(
								WebConfig.WebParams.SURVEY_QUESTION_SUB_ID,
								questionModal.getSubQuestionID());
					}

					questionJsonObject.put(WebConfig.WebParams.USERRESPONSE,
							userResponse);

					questionJsonObject.put(
							WebConfig.WebParams.QUESTION_TYPE_ID,
							QuestionType.SURVEY_QUESTION_TYPE_TOGGLEBUTTON);

					questionJson.put(questionJsonObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private void getBundleValue() {

		Intent intent = getIntent();

		 mModule = (Module) intent
				.getParcelableExtra(IntentKey.MOUDLE_POJO);

		if (mModule != null) {

			moduleId = mModule.getModuleID();

			mActivityData = Helper.getActivityDataMasterModel(
					getApplicationContext(), mModule);
			if (mActivityData != null) {

				/*
				 * mActivityID = DatabaseHelper.getConnection(
				 * getApplicationContext()).getActivityIdIfExist(
				 * mActivityData);
				 */

				getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVITY_ID,
						null, this);
			}

			try {

				((TextView) this.findViewById(R.id.tv_title_sdActionBar))
						.setText(mModule.getName());

				getSupportActionBar().setTitle(mModule.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (mModule.isStoreWise()) {
				mStoreID = Helper.getLongValueFromPrefs(getApplicationContext(),
						SharedPreferencesKey.PREF_SELECTED_OUTLET_STORE_ID);
			} else {
				mStoreID = 0;
			}
		}
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			handleCameraRequest(requestCode, data);
		}
	}

	private void handleCameraRequest(final int requestCode, final Intent data) {
		try {
			if (containerLinearLayout != null) {
				if (containerLinearLayout.getChildCount() > 0) {
					int childCount = containerLinearLayout.getChildCount();
					for (int i = 0; i < childCount; i++) {

						LinearLayout childRootView = (LinearLayout) containerLinearLayout
								.getChildAt(i);
						boolean isImageSet = setImageToContainer(childRootView,
								requestCode, data);
						if (isImageSet) {
							break;
						}
					}
				}

			}
		} catch (NullPointerException e) {
			Helper.printStackTrace(e);
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
	}

	private Bitmap compressBitmapImage(String path,
			Bitmap scaledOrUnscaledBitmap) {
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			scaledOrUnscaledBitmap
					.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return scaledOrUnscaledBitmap;
	}

	private Bitmap loadPrescaledBitmap(String path, int desireWidth,
			int desireHeight) {

		Bitmap scaledBitmap = null;
		Bitmap unscaledBitmap = null;

		try {
			// Part 1: Decode image
			unscaledBitmap = ScalingUtilities.decodeFile(path, desireWidth,
					desireHeight, ScalingLogic.FIT);

			if (!(unscaledBitmap.getWidth() <= desireWidth && unscaledBitmap
					.getHeight() <= desireHeight)) {
				// Part 2: Scale image
				scaledBitmap = ScalingUtilities.createScaledBitmap(
						unscaledBitmap, desireWidth, desireHeight,
						ScalingLogic.FIT);

			} else {

				return compressBitmapImage(path, unscaledBitmap);
			}

		} catch (Throwable e) {
		}

		return compressBitmapImage(path, scaledBitmap);
	}

	protected boolean setImageToContainer(LinearLayout childRootView,
			int questionID, Intent data) {

		boolean isImageSetToContainer = false;
		try {
			if (childRootView.getTag() != null
					&& childRootView.getTag() instanceof Question) {
				final Question questionModal = (Question) childRootView
						.getTag();

				if (questionModal.getQuestionTypeId() == QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX) {

					if (questionModal.getSurveyQuestionId() == questionID) {

						try {
							final ImageView imageView = (ImageView) childRootView
									.getChildAt(1);
							/*
							 * String imagePath = Environment
							 * .getExternalStorageDirectory() + "/STORE_IMAGES/"
							 * + questionModal.getSurveyQuestionId() + ".png";
							 */
							String imagePath = FileDirectory.DIRECTORY_SURVEY_QUESTION_IMAGES
									+ "/"
									+ String.valueOf(mStoreID)
									+ "/"
									+ questionModal.getSurveyQuestionId()
									+ ".png";

							imageView.setTag(imagePath);
							Bitmap bitamp = loadPrescaledBitmap(imagePath,
									Constants.QUESTION_IMAGE_WIDTH,
									Constants.QUESTION_IMAGE_HEIGHT);

							if (bitamp != null) {
								imageView.setImageBitmap(ThumbnailUtils
										.extractThumbnail(bitamp, 120, 120));
							}

							isImageSetToContainer = true;

						} catch (Exception e) {
							Helper.printStackTrace(e);
						}
					}

				} else if (questionModal.getQuestionTypeId() == QuestionType.SURVEY_QUESTION_TYPE_RADIOBUTTON) {

					RadioGroup rg = (RadioGroup) childRootView.getChildAt(1);
					if (rg.getCheckedRadioButtonId() != -1) {
						RadioButton radioButton = (RadioButton) rg
								.findViewById(rg.getCheckedRadioButtonId());
						Option option = (Option) radioButton.getTag();
						LinearLayout bodylinearLayoutChild = (LinearLayout) childRootView
								.findViewWithTag(option.getSurveyOptionId());

						if (bodylinearLayoutChild != null
								&& bodylinearLayoutChild.getVisibility() == View.VISIBLE) {

							for (int j = 0; j < bodylinearLayoutChild
									.getChildCount(); j++) {
								LinearLayout dependentQuestionRootLayout = (LinearLayout) bodylinearLayoutChild
										.getChildAt(j);
								boolean isImageSet = setImageToContainer(
										dependentQuestionRootLayout,
										questionID, data);
								if (isImageSet) {
									isImageSetToContainer = true;
									break;
								}
							}
						}
					}
				} else if (questionModal.getQuestionTypeId() == QuestionType.SURVEY_QUESTION_TYPE_REPEATOR) {

					LinearLayout layoutRepeateContainer = (LinearLayout) childRootView
							.getChildAt(2);

					int repeateChildCount = layoutRepeateContainer
							.getChildCount();

					for (int i = 0; i < repeateChildCount; i++) {

						LinearLayout pictureTypeViewChild = (LinearLayout) layoutRepeateContainer
								.getChildAt(i);
						Question question = (Question) pictureTypeViewChild
								.getTag();
						int surveyQuestionID = question.getSurveyQuestionId();
						int questionType = question.getQuestionTypeId();
						if (surveyQuestionID == -1
								&& questionType == QuestionType.SURVEY_QUESTION_TYPE_PICTUREBOX) {

							final ImageView imageView = (ImageView) pictureTypeViewChild
									.getChildAt(1);

							String imagePath = FileDirectory.DIRECTORY_SURVEY_QUESTION_IMAGES
									+ "/"
									+ String.valueOf(mStoreID)
									+ "/"
									+ questionModal.getSurveyQuestionId()
									+ "_"
									+ i + "_" + mStoreID + ".png";

							/*
							 * imagePath = Environment
							 * .getExternalStorageDirectory() + "/STORE_IMAGES/"
							 * + questionModal.getSurveyQuestionId()+"_"+i+
							 * "_"+Helper
							 * .getIntValueFromPrefs(QuestionnaireActivity.this,
							 * SharedPreferencesKey
							 * .PREF_SELECTED_OUTLET_STORE_ID)+".png";
							 */
							Bitmap bitamp = loadPrescaledBitmap(imagePath,
									Constants.QUESTION_IMAGE_WIDTH,
									Constants.QUESTION_IMAGE_HEIGHT);

							if (bitamp != null) {
								imageView.setTag(imagePath);
								imageView.setImageBitmap(ThumbnailUtils
										.extractThumbnail(bitamp, 120, 120));
							}

						}

					}

				}
			}
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		return isImageSetToContainer;
	}

	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float) width / (float) height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

	public void radioButtonClicked(RadioButton radioButton) {
		try {
			Option option = (Option) radioButton.getTag();

			for (int i = 0; i < containerLinearLayout.getChildCount(); i++) {

				if (containerLinearLayout.getChildAt(i).getTag() instanceof Option) {
					if (((Option) containerLinearLayout.getChildAt(i).getTag())
							.getSurveyOptionId() == option.getSurveyOptionId()) {
						((LinearLayout) containerLinearLayout.getChildAt(i))
								.setVisibility(View.VISIBLE);
					} else {

						RadioGroup radioGroup = (RadioGroup) radioButton
								.getParent();

						for (int j = 0; j < radioGroup.getChildCount(); j++) {
							RadioButton radioButton2 = (RadioButton) radioGroup
									.getChildAt(j);

							LinearLayout linearLayout = (LinearLayout) containerLinearLayout
									.getChildAt(i);
							Option option1 = (Option) linearLayout.getTag();

							if (((Option) radioButton2.getTag())
									.getSurveyOptionId() == option1
									.getSurveyOptionId()) {
								((LinearLayout) containerLinearLayout
										.getChildAt(i))
										.setVisibility(View.GONE);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getRelativeTop(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getTop();
		else
			return myView.getTop() + getRelativeTop((View) myView.getParent());
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {

		Loader loader = null;
		switch (loaderID) {
		case LOADER_ID:

			String[] args = { mActivityID + "", moduleId + "", +0 + "" };

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.GET_SURVEY_QUESTIONS_CONTENT_URI, null,
					null, args, null);
			break;

		case LOADER_ID_GET_ACTIVITY_ID:

			String[] arg = { String.valueOf(mActivityData.getStoreID()),
					String.valueOf(mActivityData.getModuleCode()) };

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null,
					arg, null);
			break;
		default:
			break;
		}

		return loader;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {

		switch (loader.getId()) {
		case LOADER_ID:

			if (cursor != null && cursor.getCount() != 0) {

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					progressDialog = SSCProgressDialog
							.ctor(QuestionnaireActivity.this);
				} else {
					progressDialog = new ProgressDialog(
							QuestionnaireActivity.this);
				}
				progressDialog.setProgress(0);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage(QuestionnaireActivity.this
						.getResources().getString(R.string.loadingmessage));
				progressDialog.setCancelable(false);
				progressDialog.show();
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {

						independentQuestions = DatabaseUtilMethods
								.getQuestionsData(cursor);

						mHandler.sendEmptyMessage(0);

					}
				});

				thread.start();

			}
			break;

		case LOADER_ID_GET_ACTIVITY_ID:

			if(cursor!=null&&cursor.moveToFirst())
			{
				mActivityID = DatabaseUtilMethods.getActivityID(cursor);	
			}
		
			setUpViews();
			initialize();
		

			break;
		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
