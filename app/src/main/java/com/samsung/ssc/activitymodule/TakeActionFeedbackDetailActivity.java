package com.samsung.ssc.activitymodule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.constants.MSSStatus;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class TakeActionFeedbackDetailActivity extends Activity {

	private RadioGroup mRg_etr_rfc, mRg_close_reopen, mRg_etr2_rfc2,
			mRg_closeAgree_closeDisagree, mRg_only_rfc, mRg_only_rfc_2;

	private Button mBt_setTime;
	private EditText mEtRemarks;
	private int mCurrentStatus, mFeedbackUserID, mSpocID, mNewStatusID = -1,
			mUserIdPendingWith;
	private RadioGroup.OnCheckedChangeListener check_Change_listener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			switch (checkedId) {
			case R.id.rb_etr_take_action:

				mNewStatusID = MSSStatus.PENDING_FOR_RFC;
				mUserIdPendingWith = mSpocID;
				showSetTimeButton();

				break;

			case R.id.rb_etr2_take_action:

				mNewStatusID = MSSStatus.PENDING_FOR_RFC_2;
				mUserIdPendingWith = mSpocID;
				showSetTimeButton();
				break;

			case R.id.rb_rfc_take_action:

				mNewStatusID = MSSStatus.PENDING_FOR_CLOSURE;
				mUserIdPendingWith = mFeedbackUserID;

				hideSetTimeButton();

				break;

			case R.id.rb_rfc2_take_action:
				mUserIdPendingWith = mFeedbackUserID;
				mNewStatusID = MSSStatus.PENDING_FOR_CLOSURE_2;

				hideSetTimeButton();

				break;

			case R.id.rb_close_take_action:

				mNewStatusID = MSSStatus.CLOSED;
				mUserIdPendingWith = mFeedbackUserID;
				break;
			case R.id.rb_re_open_take_action:

				mNewStatusID = MSSStatus.PENDING_FOR_ETR_2;
				mUserIdPendingWith = mSpocID;
				break;

			case R.id.rb_close_agree_take_action:

				mNewStatusID = MSSStatus.CLOSE_WITH_AGREE;
				mUserIdPendingWith = mFeedbackUserID;
				break;
			case R.id.rb_close_disagree_take_action:

				mNewStatusID = MSSStatus.CLOSE_WITH_DISAGREE;
				mUserIdPendingWith = mFeedbackUserID;
				break;

			case R.id.rb_only_rfc_take_action:

				mNewStatusID = MSSStatus.PENDING_FOR_CLOSURE;
				mUserIdPendingWith = mFeedbackUserID;
				break;

			case R.id.rb_only_rfc_2_take_action:

				mNewStatusID = MSSStatus.PENDING_FOR_CLOSURE_2;
				mUserIdPendingWith = mFeedbackUserID;
				break;
			default:
				break;
			}

		}

	};

	private Long mFeedbackID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.setScreenShotOff(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_take_action_feedback_detail);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			mCurrentStatus = bundle.getInt("current_status");
			mFeedbackUserID = bundle.getInt("feedback_user_id");
			mSpocID = bundle.getInt("spoc_id");
			mFeedbackID = bundle.getLong("feedbackID");

		}

		setUpViews();
	}

	private void setUpViews() {

		mRg_etr_rfc = (RadioGroup) findViewById(R.id.rg_etr_rfc_take_action);
		mRg_close_reopen = (RadioGroup) findViewById(R.id.rg_close_reopen_take_action);
		mRg_etr2_rfc2 = (RadioGroup) findViewById(R.id.rg_etr2_rfc2_take_action);
		mRg_closeAgree_closeDisagree = (RadioGroup) findViewById(R.id.rg_agree_disagree_take_action);
		mRg_only_rfc = (RadioGroup) findViewById(R.id.rg_only_rfc_take_action);
		mRg_only_rfc_2 = (RadioGroup) findViewById(R.id.rg_only_rfc_2_take_action);

		mEtRemarks = (EditText) findViewById(R.id.et_remark_take_action);

		mBt_setTime = (Button) findViewById(R.id.bt_set_time_take_action);

		// set up on check change listener
		mRg_etr_rfc.setOnCheckedChangeListener(check_Change_listener);
		mRg_close_reopen.setOnCheckedChangeListener(check_Change_listener);
		mRg_etr2_rfc2.setOnCheckedChangeListener(check_Change_listener);
		mRg_closeAgree_closeDisagree
				.setOnCheckedChangeListener(check_Change_listener);
		mRg_only_rfc.setOnCheckedChangeListener(check_Change_listener);
		mRg_only_rfc_2.setOnCheckedChangeListener(check_Change_listener);

		int logginedUserId;
		try {
			logginedUserId = Integer.parseInt(Helper.getStringValuefromPrefs(
					getApplicationContext(), SharedPreferencesKey.PREF_USERID));

			showViewsAccordingToStatus(logginedUserId);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	/*
	 * This method hide view according to user and status
	 */
	private void showViewsAccordingToStatus(int loginedUserId) {

		if (mFeedbackUserID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_CLOSURE) {

			mRg_close_reopen.setVisibility(View.VISIBLE);

		} else if (mFeedbackUserID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_CLOSURE_2) {

			mRg_closeAgree_closeDisagree.setVisibility(View.VISIBLE);

		} else if (mSpocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_ETR) {

			mRg_etr_rfc.setVisibility(View.VISIBLE);
			// mBt_setTime.setVisibility(View.VISIBLE);

		} else if (mSpocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_ETR_2) {

			mRg_etr2_rfc2.setVisibility(View.VISIBLE);

			// mBt_setTime.setVisibility(View.VISIBLE);
		}

		else if (mSpocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_RFC) {
			mRg_only_rfc.setVisibility(View.VISIBLE);

		} else if (mSpocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_RFC_2) {
			mRg_only_rfc_2.setVisibility(View.VISIBLE);

		}
	}

	@SuppressLint("NewApi")
	protected void showDialog3(final Date minDate) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);

		DatePickerDialog dialog = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

						public void onDateSet(final DatePicker view,
								final int year, int monthOfYear,
								final int dayOfMonth) {

							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.YEAR, year);
							cal.set(Calendar.MONTH, monthOfYear);
							cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							cal.set(Calendar.HOUR_OF_DAY, 0);
							cal.set(Calendar.MINUTE, 0);
							cal.set(Calendar.SECOND, 0);
							cal.set(Calendar.MILLISECOND, 0);
							Date date = cal.getTime();

							DateFormat dateFormat = new SimpleDateFormat(
									"dd/MMM/yyyy", Locale.ENGLISH);

							mBt_setTime.setText(dateFormat.format(date));
							mBt_setTime.setTag(date);

						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));

			// (picker is a DatePicker)
			try {
				dialog.getDatePicker().setCalendarViewShown(false);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (minDate != null) {
				dialog.getDatePicker().setMinDate(minDate.getTime());
			}

		} else {
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view,
								int selectedYear, int selectedMonth,
								int selectedDate) {

							if (minDate != null) {
								Calendar cal2 = Calendar.getInstance();
								cal2.setTime(minDate);

								System.out.println("Min " + selectedYear + "="
										+ cal2.get(Calendar.YEAR));
								System.out.println("Min " + selectedMonth + "="
										+ cal2.get(Calendar.MONTH));
								System.out.println("Min " + selectedDate + "="
										+ cal2.get(Calendar.DATE));

								if (selectedYear >= cal2.get(Calendar.YEAR)
										&& selectedMonth >= cal2
												.get(Calendar.MONTH)
										&& selectedDate >= cal2
												.get(Calendar.DATE)) {

									Calendar cal = Calendar.getInstance();
									cal.set(Calendar.YEAR, selectedYear);
									cal.set(Calendar.MONTH, selectedMonth);
									cal.set(Calendar.DAY_OF_MONTH, selectedDate);
									cal.set(Calendar.HOUR_OF_DAY, 0);
									cal.set(Calendar.MINUTE, 0);
									cal.set(Calendar.SECOND, 0);
									cal.set(Calendar.MILLISECOND, 0);
									Date date = cal.getTime();

									DateFormat dateFormat = new SimpleDateFormat(
											"dd/MMM/yyyy", Locale.ENGLISH);

									mBt_setTime.setText(dateFormat.format(date));
									mBt_setTime.setTag(date);

								} else {
									Helper.showCustomToast(getApplicationContext(), R.string.min_date_error, Toast.LENGTH_SHORT);
								}

							} else {

								Calendar cal = Calendar.getInstance();
								cal.set(Calendar.YEAR, selectedYear);
								cal.set(Calendar.MONTH, selectedMonth);
								cal.set(Calendar.DAY_OF_MONTH, selectedDate);
								cal.set(Calendar.HOUR_OF_DAY, 0);
								cal.set(Calendar.MINUTE, 0);
								cal.set(Calendar.SECOND, 0);
								cal.set(Calendar.MILLISECOND, 0);
								Date date = cal.getTime();

								DateFormat dateFormat = new SimpleDateFormat(
										"dd/MMM/yyyy", Locale.ENGLISH);

								mBt_setTime.setText(dateFormat.format(date));
								mBt_setTime.setTag(date);

							}

						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
							.get(Calendar.DAY_OF_MONTH)) {
				@Override
				public void onDateChanged(DatePicker view, int year, int month,
						int date) {

					if (minDate != null) {
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(minDate);

						System.out.println("Min " + year + "="
								+ cal2.get(Calendar.YEAR));
						System.out.println("Min " + month + "="
								+ cal2.get(Calendar.MONTH));
						System.out.println("Min " + date + "="
								+ cal2.get(Calendar.DATE));

						if (year >= cal2.get(Calendar.YEAR)
								&& month >= cal2.get(Calendar.MONTH)
								&& date >= cal2.get(Calendar.DATE)) {

							view.updateDate(year, month, date);
						} else {

							Helper.showCustomToast(getApplicationContext(), R.string.min_date_error, Toast.LENGTH_SHORT);
							
						}
					} else {
						view.updateDate(year, month, date);
					}

				}
			};
		}

		dialog.show();

	}

	/**
	 * "ETRDate": "", "FeedbackID": 30, "NewStatusID": 5, "Remarks":
	 * "Feedback REOpEND", "UserIdPendingWith": 1311 },{
	 * 
	 * @param view
	 */

	public void onSubmitClick(View view) {

		JSONObject parmsJsonObject = new JSONObject();

		if (mNewStatusID == -1) {
			
			Helper.showCustomToast(getApplicationContext(), R.string.please_select_action, Toast.LENGTH_SHORT);
			
			return;
		} else if (mEtRemarks.getText().toString().equals("")) {
			

			Helper.showCustomToast(getApplicationContext(), R.string.please_enter_remarks, Toast.LENGTH_SHORT);
			
			return;
		} else if (mBt_setTime.getVisibility() == View.VISIBLE) {
			if (mBt_setTime.getText().toString().equalsIgnoreCase("Set Date")) {
				Helper.showCustomToast(getApplicationContext(), R.string.please_enter_date, Toast.LENGTH_SHORT);
				return;
			}

		}

		try {
			JSONObject arrayItemJson = new JSONObject();
			if (mBt_setTime.getVisibility() == View.VISIBLE) {
				if (!mBt_setTime.getText().toString()
						.equalsIgnoreCase("Set Date")) {
					arrayItemJson.put("ETRDate", mBt_setTime.getText()
							.toString());
					arrayItemJson.put("FeedbackID", mFeedbackID);
					arrayItemJson.put("NewStatusID", mNewStatusID);
					arrayItemJson.put("Remarks", mEtRemarks.getText()
							.toString());
					arrayItemJson.put("UserIdPendingWith", mUserIdPendingWith);
				}

			} else {
				arrayItemJson.put("ETRDate", "");
				arrayItemJson.put("FeedbackID", mFeedbackID);
				arrayItemJson.put("NewStatusID", mNewStatusID);
				arrayItemJson.put("Remarks", mEtRemarks.getText().toString());
				arrayItemJson.put("UserIdPendingWith", mUserIdPendingWith);
			}

			JSONArray jsonArrayFeedback = new JSONArray();
			jsonArrayFeedback.put(0, arrayItemJson);
			parmsJsonObject.put("Feedback", jsonArrayFeedback);
			parmsJsonObject.put("roleID",
					Helper.getIntValueFromPrefs(this, SharedPreferencesKey.PREF_ROLEID));
			parmsJsonObject
					.put("userID", Helper.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID));

			PostDataToNetwork dataToNetwork = new PostDataToNetwork(this,
					"Update", new GetDataCallBack() {

						@Override
						public void processResponse(Object result) {

							if (result != null) {

								try {
									JSONObject jsonObject = new JSONObject(
											result.toString());
									boolean isSuccess = jsonObject
											.getBoolean("IsSuccess");

									if (isSuccess) {

										Helper.showCustomToast(
												getApplicationContext(),
												R.string.updated_successfully,
												Toast.LENGTH_SHORT);

										setResult(RESULT_OK, getIntent());
										finish();
									} else {
										Helper.showCustomToast(
												getApplicationContext(),
												R.string.request_not_successful,
												Toast.LENGTH_SHORT);

									}
								} catch (JSONException e) {

									e.printStackTrace();
									Helper.showCustomToast(
											getApplicationContext(),
											R.string.error_contact_admin,
											Toast.LENGTH_SHORT);

								}

							} else {
								Helper.showCustomToast(
										getApplicationContext(),
										R.string.error_contact_admin,
										Toast.LENGTH_SHORT);
							}

						}
					});
			dataToNetwork.setConfig(getResources().getString(R.string.url),
					"UpdateFeedbacks");
			dataToNetwork.execute(parmsJsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void onSetDateClick(View view) {
		// showDatePickerDialog();

		showDialog3(Calendar.getInstance().getTime());
	}

	private void hideSetTimeButton() {
		if (mBt_setTime.getVisibility() == View.VISIBLE) {
			mBt_setTime.setVisibility(View.GONE);
			mBt_setTime.setText("Set Date");
		}

	}

	private void showSetTimeButton() {
		if (mBt_setTime.getVisibility() == View.GONE) {
			mBt_setTime.setVisibility(View.VISIBLE);
		}

	}
}
