package com.samsung.ssc;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.SeparatedListAdapter;
import com.samsung.ssc.adapters.SeparatedListItemAdapter;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.sync.SyncMaster;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyLocation;
import com.samsung.ssc.util.SyncPreparationCompeteCallback;
import com.samsung.ssc.util.MyLocation.LocationResult;

public class AttendanceActivity extends BaseActivity {

	private RadioGroup rbAttendaceOption;
	private Spinner spinnerDaySelection;
	private boolean attendanceMark;
	private double mLongitude = 0, mLatitude = 0;
	ProgressDialog dialog;
	private MyLocation myloc;
	private int MAX_INTIALIZATION_TIME = 5000; // 5 seconds

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.activity_attendance2);

		dialog = ProgressDialog.show(this, "", getString(R.string.intializing),
				true);
		dialog.show();

		fetchinglocation();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {

				if (myloc != null) {
					myloc.removeListener();
				}
				if (dialog.isShowing()) {
					dialog.dismiss();
					setUpView();
				}

			}
		}, MAX_INTIALIZATION_TIME);

	}

	private void fetchinglocation() {

		myloc = new MyLocation();

		LocationResult lr = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				if (location != null) {
					try {

						mLatitude = location.getLatitude();
						mLongitude = location.getLongitude();

						if (myloc != null) {
							myloc.removeListener();

							myloc = null;
						}

						if (dialog.isShowing()) {
							dialog.dismiss();
							setUpView();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		myloc.getLocation(AttendanceActivity.this, lr);
	}

	private void setUpView() {

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			((TextView) this
					.findViewById(R.id.activity_attendance_textView_userName))
					.setText(bundle.getString("USER_NAME"));
			((TextView) this
					.findViewById(R.id.activity_attendance_textView_userContactNumber))
					.setText(bundle.getString("USER_CONTACT_NUMBER"));
		}

		attendanceMark = true;

		spinnerDaySelection = (Spinner) this
				.findViewById(R.id.activity_attendance_spinner_DaysList);
		rbAttendaceOption = (RadioGroup) this
				.findViewById(R.id.activity_attendance_radioGroup_Option_attendance);
		rbAttendaceOption
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (group.getCheckedRadioButtonId()) {
						case R.id.activity_attendance_radioButton_MeetingMarket:
							spinnerDaySelection.setVisibility(View.GONE);
							break;
						case R.id.activity_attendance_radioButton_LeaveOff:
							spinnerDaySelection.setVisibility(View.VISIBLE);
							break;
						default:
							break;
						}
					}
				});

		if (isOnline(this)) {
			// checkForUnsyncedData();
			syncProcess();
		}

	}

	private void syncProcess() {

		ArrayList<ActivityDataMasterModel> activitiesForSync = DatabaseHelper
				.getConnection(getApplicationContext())
				.getActivityDataForSync();

		if (!activitiesForSync.isEmpty()) {

			Helper.showConfirmationDialog(AttendanceActivity.this,
					R.string.sync_permission_dialog_title,
					R.string.sync_permission_dialog_message,
					R.string.sync_permission_dialog_button_one,
					R.string.sync_permission_dialog_button_two,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Map<String, ArrayList<String>> pandingMoudleList = DatabaseHelper
									.getConnection(getApplicationContext())
									.getPendingMendantoryModuleList();

							if (pandingMoudleList.size() == 0) {

								final SyncMaster master = new SyncMaster(
										AttendanceActivity.this);
								SyncPreparationCompeteCallback callback1 = new SyncPreparationCompeteCallback() {

									@Override
									public void onComplete(
											final ArrayList<ActivityDataMasterModel> data) {

										if (data != null && data.size() > 0) {

											master.sync(data, false, null);
										}

									}
								};

								master.getSyncData(callback1);
							} else {

								SeparatedListAdapter adapter = new SeparatedListAdapter(
										AttendanceActivity.this);
								Set<String> keySet = pandingMoudleList.keySet();
								for (String key : keySet) {
									pandingMoudleList.get(key);
									SeparatedListItemAdapter itemAdapter = new SeparatedListItemAdapter(
											AttendanceActivity.this, 0,
											pandingMoudleList.get(key));
									adapter.addSection(key, itemAdapter);
								}

								SSCAlertDialog alertDialog = new SSCAlertDialog(
										AttendanceActivity.this,
										SSCAlertDialog.LIST_TYPE)
										.setTitleText("Pending Modules")
										.setListAdapter(adapter)
										.setEnableConfirmButton(true)
										.setConfirmText("OK")
										.setConfirmClickListener(
												new SSCAlertDialog.OnSDAlertDialogClickListener() {

													@Override
													public void onClick(
															SSCAlertDialog sdAlertDialog) {

														sdAlertDialog.dismiss();

													}

												}).showCancelButton(false)
										.setCancelText(null)
										.setCancelClickListener(null);
								alertDialog.setCancelable(false);

								alertDialog.show();

							}

						}
					}, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

		}
	}

	/**
	 ** @param view
	 */
	public void onClickSubmit(View view) {

		if (isOnline(view.getContext())) {
			if (isInputValid()) {

				PostDataToNetwork pdn = new PostDataToNetwork(this,
						getString(R.string.loadingmessage),
						new GetDataCallBack() {
							@Override
							public void processResponse(Object result) {
								if (result != null) {
									try {
										parseResponse(result);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}
						});

				JSONObject rootJsonObject = new JSONObject();
				JSONObject jsonobj = new JSONObject();

				try {
					switch (rbAttendaceOption.getCheckedRadioButtonId()) {
					case R.id.activity_attendance_radioButton_MeetingMarket:

						jsonobj.put(WebConfig.WebParams.ISATTENDANCE, "1");
						jsonobj.put(WebConfig.WebParams.LATTITUDE, mLatitude);
						jsonobj.put(WebConfig.WebParams.LONGITUDE, mLongitude);

						jsonobj.put(WebConfig.WebParams.REMARKS, "Present");
						jsonobj.put("UserID", Helper.getStringValuefromPrefs(
								getApplicationContext(),
								SharedPreferencesKey.PREF_USERID));
						rootJsonObject.put(WebConfig.WebParams.NOOFDAYS, 0);
						attendanceMark = true;
						break;
					case R.id.activity_attendance_radioButton_LeaveOff:
						jsonobj.put(WebConfig.WebParams.ISATTENDANCE, "0");
						jsonobj.put(WebConfig.WebParams.REMARKS, "apply leave");
						jsonobj.put("UserID", Helper.getStringValuefromPrefs(
								getApplicationContext(),
								SharedPreferencesKey.PREF_USERID));

						rootJsonObject.put(WebConfig.WebParams.NOOFDAYS,
								spinnerDaySelection.getSelectedItem()
										.toString());
						attendanceMark = false;
						break;

					default:
						break;
					}

					rootJsonObject.put(WebConfig.WebParams.USERATTENDANCE,
							jsonobj);
				} catch (JSONException e) {
					Helper.printStackTrace(e);
				}
				pdn.setConfig(getString(R.string.url), "MarkAttendance");
				pdn.execute(rootJsonObject);
			}
		}

	}

	private void parseResponse(Object result) throws JSONException {

		try {

			if (result != null) {
				JSONObject jsonObject = new JSONObject(result.toString());
				if (jsonObject.getBoolean("IsSuccess")) {

					String attendaceResponse = jsonObject
							.getString("SingleResult");
					if (attendaceResponse.equals("1")) {
						if (attendanceMark) {

							Helper.saveIntgerValueInPrefs(this,
									SharedPreferencesKey.PREF_MARK_ATTENDANCE,
									1);
						} else {
							Helper.saveIntgerValueInPrefs(this,
									SharedPreferencesKey.PREF_MARK_ATTENDANCE,
									0);
						}

					 

						new Handler().post(new Runnable() {

							@Override
							public void run() {
								DatabaseHelper.getConnection(
										getApplicationContext())
										.clearCompletedDataFramResponseTables();
							}
						});

						startActivity(new Intent(getApplicationContext(),
								MainMenuActivityListGrid.class));

					} else if (attendaceResponse.equals("3")) {

						Helper.showAlertDialog(
								AttendanceActivity.this,
								SSCAlertDialog.ERROR_TYPE,
								getString(R.string.error3),
								jsonObject.getString("Message"),
								getString(R.string.ok),
								new SSCAlertDialog.OnSDAlertDialogClickListener() {

									@Override
									public void onClick(
											SSCAlertDialog sdAlertDialog) {

										sdAlertDialog.dismiss();

									}
								}, false, null, null);
					}
				} else {

					Helper.showAlertDialog(this,
							SSCAlertDialog.ERROR_TYPE,
							getString(R.string.error3),
							jsonObject.getString("Message"), null, null, false,
							null, null);
				}
			} else {
				Helper.showAlertDialog(this, SSCAlertDialog.ERROR_TYPE,
						getString(R.string.error3), "Error", null, null, false,
						null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isInputValid() {

		if (rbAttendaceOption.getCheckedRadioButtonId() < 0) {

			Helper.showAlertDialog(this, SSCAlertDialog.ERROR_TYPE,
					getString(R.string.error3), getString(R.string.error9),
					null, null, false, null, null);

			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed() {

		Helper.showAlertDialog(AttendanceActivity.this,
				SSCAlertDialog.WARNING_TYPE, getString(R.string.error10),
				getString(R.string.error12), getString(R.string.yes),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						if (myloc != null) {
							myloc.removeListener();
						}
						logOut();
					}
				}, true, getString(R.string.no),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});

	}

	private void logOut() {

		Helper.clearPreferencesData(AttendanceActivity.this);

		Helper.cancelSync(this);
		
		Intent intent = new Intent(AttendanceActivity.this,
				LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("logout", true);
		finish();
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myloc != null) {
			myloc.removeListener();
		}
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private boolean isOnline(Context context) {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null) {
			flag = cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}
		return flag;
	}
}
