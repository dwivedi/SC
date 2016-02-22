package com.samsung.ssc.activitymodule;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.adapters.FeedbackTrackerAdapter1;
import com.samsung.ssc.constants.MSSStatus;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig.WebMethod;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackCategoryMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackTypeMasterColumns;
import com.samsung.ssc.dto.FMSStatusDataModal;
import com.samsung.ssc.dto.FeedbackCategoryMasterModel;
import com.samsung.ssc.dto.FeedbackTrackerDto1;
import com.samsung.ssc.dto.FeedbackTypeMasterModel;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

public class FMSFeedbackTracker1 extends BaseActivity implements
		android.view.View.OnTouchListener, LoaderCallbacks<Cursor> {

	private static final int MAX_LOAD_ROW_COUNT = 10;

	protected static final int FEEDBACK_DETAIL_REQUEST_CODE = 0;

	private LinearLayout linerLayoutFMSTrackerSearchViewSection;

	boolean isSearchVisible = false;

	private Animation slideDownAnim;

	private Animation slideUpAnim;

	private List<TeamMasterModel> mListTeam;

	private List<FeedbackCategoryMasterModel> mListFeedbackCategory;

	private List<FeedbackTypeMasterModel> mListFeedbackType;

	private ArrayList<Integer> mSelectedTeamIds;

	private ArrayList<Integer> mSelectedCategoryIds;

	private ArrayList<Integer> mSelectedCategoryTypeIds;

	private ArrayList<Integer> mSelectedStatusIds;

	private List<FMSStatusDataModal> mStausList;

	// private StausAdapter mStatusAdapter;

	private int mPendingWithOption = 1;

	private EditText editTextFMSTrackerCategorySelection;

	private EditText editTextFMSTrackerTeamSelection;

	private EditText editTextFMSTrackerCategoryTypeSelection;

	private EditText editTextFMSTrackerFromDate;

	private EditText editTextFMSTrackerToDate;

	private EditText editTextFMSTrackerStatusSelection;

	private TextView textViewFMSTrackerSelectedStatus;

	private FeedbackTrackerAdapter1 mFeedbackTrackerAdapter;

	private RadioButton mRadioButtonPendingWithMe;

	private boolean mHasLoadMore = true;

	private LinearLayout mLinerLayoutFMSTrackerFeedbackActionPanel;

	private RelativeLayout linerLayoutFMSTrackerSearchResultViewSection;

	private Thread mTeamThread;

	private TextView mScrollTextViewFMSTrackerPendingCount;

	private int[] mTempFeedbackTeamIDs;

	private int[] mTempFeedbackCatIDs;

	private int[] mTempFeedbackTypeIDs;

	private int[] mTempStatusID;

	private int mTempPendingWithType;

	private String mTempDateFrom;

	private String mTempDateTo;

	private View mFooterView;

	private long mLastFeedbackId = 0;

	private final int LOADER_ID_GET_FEEDBACK_STATUS = 1;
	private final int LOADER_ID_GET_FEEDBACK_TEAMS = 2;
	private final int LOADER_ID_GET_FEEDBACK_TYPE = 3;
	private final int LOADER_ID_GET_FEEDBACK_CATEGORY = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.setScreenShotOff(this);
		setContentView(R.layout.activty_feedback_tracker1);

		setUpView();
	}

	private void setUpView() {
		// view in serach filer view

		RadioGroup radioGroupFMSTrackerPendingWithSelection = (RadioGroup) this
				.findViewById(R.id.radioGroupFMSTrackerPendingWithSelection);
		radioGroupFMSTrackerPendingWithSelection
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radioButtonFMSTrackerPendingWithMe:
							mPendingWithOption = 1;
							break;
						case R.id.radioButtonFMSTrackerPendingWithOther:
							mPendingWithOption = 2;
							break;
						default:
							break;
						}

					}
				});

		mRadioButtonPendingWithMe = (RadioButton) this
				.findViewById(R.id.radioButtonFMSTrackerPendingWithMe);

		// view in Main Section

		textViewFMSTrackerSelectedStatus = (TextView) this
				.findViewById(R.id.textViewFMSTrackerSelectedStatus);
		mScrollTextViewFMSTrackerPendingCount = (TextView) this
				.findViewById(R.id.textViewFMSTrackerPendingCount);

		ListView listViewFMSTrackerFeedbackList = (ListView) this
				.findViewById(R.id.listViewFMSTrackerFeedbackList);
		listViewFMSTrackerFeedbackList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						FeedbackTrackerDto1 modal = (FeedbackTrackerDto1) parent
								.getItemAtPosition(position);
						Intent intent = new Intent(view.getContext(),
								FeedbackDetailActivity.class);
						intent.putExtra("FeedbackID", modal.getFeedbackID());
						startActivityForResult(intent,
								FEEDBACK_DETAIL_REQUEST_CODE);
						// startActivity(intent);

					}
				});
		mFooterView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.feedback_tracker_footer_layout, null, false);

		listViewFMSTrackerFeedbackList.addFooterView(mFooterView);
		mFooterView.setVisibility(View.INVISIBLE);

		mFooterView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mHasLoadMore) {
					if (mTempDateFrom != null && mTempDateTo != null) {

						getFeedbackList(mTempFeedbackTeamIDs,
								mTempFeedbackCatIDs, mTempFeedbackTypeIDs,
								mTempStatusID, mTempDateFrom, mTempDateTo);
					}

				} else {

					Helper.showCustomToast(getApplicationContext(),
							R.string.no_more_records, Toast.LENGTH_SHORT);

					mFooterView.setVisibility(View.GONE);
				}
			}
		});

		mFeedbackTrackerAdapter = new FeedbackTrackerAdapter1(this);
		listViewFMSTrackerFeedbackList.setAdapter(mFeedbackTrackerAdapter);
		listViewFMSTrackerFeedbackList.requestFocus();

		linerLayoutFMSTrackerSearchViewSection = (LinearLayout) this
				.findViewById(R.id.linerLayoutFMSTrackerSearchViewSection);

		linerLayoutFMSTrackerSearchResultViewSection = (RelativeLayout) this
				.findViewById(R.id.linerLayoutFMSTrackerSearchResultViewSection);

		slideDownAnim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_down);
		slideUpAnim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_up);
		slideUpAnim.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//
				linerLayoutFMSTrackerSearchViewSection.setVisibility(View.GONE);
				linerLayoutFMSTrackerSearchViewSection.setOnTouchListener(null);
				linerLayoutFMSTrackerSearchResultViewSection.bringToFront();
				linerLayoutFMSTrackerSearchViewSection.clearAnimation();

				/*
				 * linerLayoutFMSTrackerSearchViewSection.clearAnimation();
				 * linerLayoutFMSTrackerSearchViewSection
				 * .setVisibility(View.GONE);
				 */

			}
		});
		slideDownAnim.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				linerLayoutFMSTrackerSearchViewSection
						.setVisibility(View.VISIBLE);
				linerLayoutFMSTrackerSearchViewSection.bringToFront();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		linerLayoutFMSTrackerSearchViewSection.startAnimation(slideUpAnim);

		mLinerLayoutFMSTrackerFeedbackActionPanel = (LinearLayout) this
				.findViewById(R.id.linerLayoutFMSTrackerFeedbackActionPanel);

		ImageButton ib_back = (ImageButton) findViewById(R.id.ib_up_sdActionBar);

		// click listener for back button
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				FMSFeedbackTracker1.this.finish();

			}
		});

		setUpDialogView();

	}

	private void setUpDialogView() {

		editTextFMSTrackerTeamSelection = (EditText) this
				.findViewById(R.id.editTextFMSTrackerTeamSelection);
		editTextFMSTrackerTeamSelection.setOnTouchListener(this);

		editTextFMSTrackerCategorySelection = (EditText) this
				.findViewById(R.id.editTextFMSTrackerCategorySelection);
		editTextFMSTrackerCategorySelection.setOnTouchListener(this);
		editTextFMSTrackerCategoryTypeSelection = (EditText) this
				.findViewById(R.id.editTextFMSTrackerCategoryTypeSelection);
		editTextFMSTrackerCategoryTypeSelection.setOnTouchListener(this);
		editTextFMSTrackerFromDate = (EditText) this
				.findViewById(R.id.editTextFMSTrackerFromDate);
		editTextFMSTrackerFromDate.setOnTouchListener(this);
		editTextFMSTrackerToDate = (EditText) this
				.findViewById(R.id.editTextFMSTrackerToDate);
		editTextFMSTrackerToDate.setOnTouchListener(this);

		editTextFMSTrackerStatusSelection = (EditText) this
				.findViewById(R.id.spinnerFMSTrackerStatusSelection);
		editTextFMSTrackerStatusSelection.setOnTouchListener(this);
		// mStatusAdapter = new StausAdapter(this);
		/*
		 * spinnerFMSTrackerStatusSelection.setAdapter(mStatusAdapter);
		 * 
		 * spinnerFMSTrackerStatusSelection .setOnItemSelectedListener(new
		 * AdapterView.OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> parent, View
		 * view, int position, long id) { String selectedStaus = (String) parent
		 * .getItemAtPosition(position);
		 * textViewFMSTrackerSelectedStatus.setText(selectedStaus);
		 * mSelectedStatus = (int) id;
		 * 
		 * }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> parent) {
		 * 
		 * } });
		 */

		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy",
				Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();

		editTextFMSTrackerFromDate.setTag(d);
		editTextFMSTrackerFromDate.setText(dateFormat.format(d));
		editTextFMSTrackerToDate.setTag(d);
		editTextFMSTrackerToDate.setText(dateFormat.format(d));

		mSelectedTeamIds = new ArrayList<Integer>();
		mSelectedCategoryIds = new ArrayList<Integer>();
		mSelectedCategoryTypeIds = new ArrayList<Integer>();

		mSelectedStatusIds = new ArrayList<Integer>();

		mListFeedbackCategory = new ArrayList<FeedbackCategoryMasterModel>();
		mListFeedbackType = new ArrayList<FeedbackTypeMasterModel>();

		// getTeamsFromDatabase(new GetTeamHandler(this));

		getSupportLoaderManager().initLoader(LOADER_ID_GET_FEEDBACK_TEAMS,
				null, this);

		// getStatusFromDatabase(new GetStatusHandler(this));
		getSupportLoaderManager().initLoader(LOADER_ID_GET_FEEDBACK_STATUS,
				null, this);

		loadInitDataFromServer();

	}

	private void loadInitDataFromServer() {

		try {

			String DateFrom = editTextFMSTrackerFromDate.getText().toString();
			String DateTo = editTextFMSTrackerToDate.getText().toString();
			mSelectedStatusIds.add(0);
			int[] tempStatusId = { 0 }; // this is default
			getFeedbackList(null, null, null, tempStatusId, DateFrom, DateTo);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void getFeedbackList(int[] FeedbackTeamIDs, int[] FeedbackCatIDs,
			int[] FeedbackTypeIDs, int[] StatusIDs, String DateFrom,
			String DateTo) {

		this.mTempFeedbackTeamIDs = FeedbackTeamIDs;
		this.mTempFeedbackCatIDs = FeedbackCatIDs;
		this.mTempFeedbackTypeIDs = FeedbackTypeIDs;

		this.mTempStatusID = StatusIDs;
		this.mTempDateFrom = DateFrom;
		this.mTempDateTo = DateTo;

		try {
			JSONObject params = new JSONObject();
			String ids;
			if (FeedbackTeamIDs != null) {
				ids = Arrays.toString(FeedbackTeamIDs);
				ids = ids.substring(1, ids.length() - 1);
				params.put("FeedbackTeamIDs", ids);
			} else {
				params.put("FeedbackTeamIDs", JSONObject.NULL);

			}
			if (FeedbackCatIDs != null) {
				ids = Arrays.toString(FeedbackCatIDs);
				ids = ids.substring(1, ids.length() - 1);
				params.put("FeedbackCatIDs", ids);
			} else {
				params.put("FeedbackCatIDs", JSONObject.NULL);
			}
			if (FeedbackTypeIDs != null) {
				ids = Arrays.toString(FeedbackTypeIDs);
				ids = ids.substring(1, ids.length() - 1);
				params.put("FeedbackTypeIDs", ids);
			} else {
				params.put("FeedbackTypeIDs", JSONObject.NULL);
			}

			if (StatusIDs != null) {

				ids = Arrays.toString(StatusIDs);
				ids = ids.substring(1, ids.length() - 1);

				// remove status is
				if (StatusIDs.length > 1) {
					if (ids.contains("0,")) {
						ids = "0";
					}
				}

				params.put("StatusIDs", ids);
			} else {
				params.put("StatusIDs", JSONObject.NULL);
			}

			params.put("LastFeedbackID", mLastFeedbackId);
			params.put("Rowcounter", MAX_LOAD_ROW_COUNT);
			params.put("PendingWithType", mPendingWithOption);

			params.put("DateFrom", DateFrom);
			params.put("DateTo", DateTo);
			JSONObject arguments = new JSONObject();
			arguments.put("searchFeedBacks", params);
			arguments.put("storeID", 0);
			arguments
					.put("userID", Helper.getStringValuefromPrefs(
							FMSFeedbackTracker1.this,
							SharedPreferencesKey.PREF_USERID));

	/*		PostDataToNetwork task = new PostDataToNetwork(this,getString(R.string.getting_),
					new GetDataCallBack() {

						@Override
						public void processResponse(Object result) {
							if (result != null) {

								try {
									JSONObject jsonObject = new JSONObject(
											result.toString());
									processFMSListResponse(jsonObject);
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

						}
					});
			task.setConfig(getString(R.string.url), "SearchFeedbacks");

			task.execute(arguments);*/
			
			
			VolleyPostDataToNetwork pdn=new VolleyPostDataToNetwork(this, getString(R.string.getting), new VolleyGetDataCallBack() {
				
				@Override
				public void processResponse(Object result) {
					if (result != null) {

						try {
							JSONObject jsonObject = new JSONObject(
									result.toString());
							processFMSListResponse(jsonObject);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					
				}
				
				@Override
				public void onError(VolleyError error) {
					
					
				}
			});
			
			pdn.setRequestData(arguments);
			pdn.setConfig(getString(R.string.url),WebMethod.SEARCH_FEEDBACKS);
			pdn.callWebService();

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@SuppressLint("NewApi")
	protected Dialog getDialog(final EditText mTextView, final Date minDate,
			final Date maxDate) {

		DatePickerDialog dialog = null;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view,
								int selectedYear, int selectedMonth,
								int selectedDate) {
							mTextView.setText(selectedDate + "/"
									+ selectedMonth + 1 + "/" + selectedYear);
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));

			try {
				dialog.getDatePicker().setCalendarViewShown(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (maxDate != null) {
				dialog.getDatePicker().setMaxDate(maxDate.getTime());
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
							mTextView.setText(selectedDate + "/"
									+ selectedMonth + 1 + "/" + selectedYear);
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH)) {
				@Override
				public void onDateChanged(DatePicker view, int year, int month,
						int day) {
					if (year <= maxDate.getYear()
							&& month + 1 <= maxDate.getMonth()
							&& day <= maxDate.getDay()) {
						if (minDate != null) {
							if (year >= minDate.getYear()
									&& month + 1 >= minDate.getMonth()
									&& day >= minDate.getDay()) {

								view.updateDate(year, month, day);
							} else {

								Helper.showCustomToast(getApplicationContext(),
										R.string.min_date_error,
										Toast.LENGTH_SHORT);

							}
						} else {
							view.updateDate(year, month, day);
						}

					} else {
						Helper.showCustomToast(getApplicationContext(),
								R.string.min_date_error, Toast.LENGTH_SHORT);
					}

				}
			};
		}
		return dialog;

	}

	protected void processFMSListResponse(JSONObject jsonObject) {
		try {
			boolean isSuccess = jsonObject.getBoolean("IsSuccess");

			if (isSuccess) {

				JSONObject rootJson = jsonObject.getJSONObject("SingleResult");

				mHasLoadMore = rootJson.getBoolean("HasMoreRows");
				if (mHasLoadMore) {
					mFooterView.setVisibility(View.VISIBLE);
				}

				setUpFormationgString(rootJson.getString("StatusCountStr"));

				JSONArray jsonItemArray = rootJson
						.getJSONArray("FeedbacksearchList");
				int count = jsonItemArray.length();

				setActionVisiblity(count);

				ArrayList<FeedbackTrackerDto1> modals = new ArrayList<FeedbackTrackerDto1>();

				if (count == 0) {

					Helper.showCustomToast(getApplicationContext(),
							R.string.no_feedback_found, Toast.LENGTH_LONG);

				}

				for (int i = 0; i < count; i++) {

					JSONObject jItem = jsonItemArray.getJSONObject(i);
					FeedbackTrackerDto1 modal = new FeedbackTrackerDto1();
					modal.setCreatedBy(jItem.getString("CreatedBy"));
					modal.setCreatedOn(jItem.getString("CreatedOn"));
					modal.setCurrentStatusID(jItem.getInt("CurrentStatusID"));

					modal.setFeedbackCatID(jItem.getInt("FeedbackCatID"));
					modal.setFeedbackCategoryName(jItem
							.getString("FeedbackCategoryName"));
					modal.setFeedbackID(jItem.getLong("FeedbackID"));
					modal.setFeedbackTeamID(jItem.getInt("FeedbackTeamID"));
					modal.setTeamName(jItem.getString("TeamName"));
					modal.setFeedbackTypeID(jItem.getInt("FeedbackTypeID"));
					modal.setFeedbackTypeName(jItem
							.getString("FeedbackTypeName"));
					modal.setLastUpdatedOn(jItem.getString("LastUpdatedOn"));
					modal.setPendingSince(jItem.getInt("PendingSince"));
					modal.setRemarks(jItem.getString("Remarks"));
					modal.setSpocID(jItem.getInt("SpocID"));

					modal.setUserID(jItem.getInt("UserID"));
					modals.add(modal);

				}

				try {
					if (count > 0) {
						mLastFeedbackId = modals.get(count - 1).getFeedbackID();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mFeedbackTrackerAdapter.addDataSet(modals);
			} else {
				Helper.showToast(getApplicationContext(),
						"Application error.Please contact administrator",
						Toast.LENGTH_LONG);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

	}

	private void setUpFormationgString(String statusCountString) {

		try {
			if (statusCountString != null) {

				mScrollTextViewFMSTrackerPendingCount
						.setText(statusCountString);
				mScrollTextViewFMSTrackerPendingCount.setSelected(true);

			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

	}

	private void setActionVisiblity(int count) {

		if (mSelectedTeamIds != null) {
			if (mSelectedTeamIds.size() == 1) {
				if (mSelectedCategoryIds != null) {
					if (mSelectedCategoryIds.size() == 1) {
						if (mSelectedCategoryTypeIds != null) {
							if (mSelectedCategoryTypeIds.size() == 1) {
								if (mRadioButtonPendingWithMe.isChecked()) {
									if (mSelectedStatusIds != null) {
										if (!mSelectedStatusIds.isEmpty()) {
											if (mSelectedStatusIds.size() == 1) {
												if (!mSelectedStatusIds
														.contains(0)) {
													if (!mSelectedStatusIds
															.contains(MSSStatus.CLOSE_WITH_AGREE)) {
														if (!mSelectedStatusIds
																.contains(MSSStatus.CLOSE_WITH_DISAGREE)) {
															if (!mSelectedStatusIds
																	.contains(MSSStatus.CLOSED)) {
																if (count > 0) {
																	mFeedbackTrackerAdapter
																			.setActionVisiblity(View.VISIBLE);
																	setUpActionBar(View.VISIBLE);
																} else {
																	mFeedbackTrackerAdapter
																			.setActionVisiblity(View.GONE);
																	setUpActionBar(View.GONE);
																}
															} else {
																mFeedbackTrackerAdapter
																		.setActionVisiblity(View.GONE);
																setUpActionBar(View.GONE);
															}

														} else {
															mFeedbackTrackerAdapter
																	.setActionVisiblity(View.GONE);
															setUpActionBar(View.GONE);
														}
													} else {
														mFeedbackTrackerAdapter
																.setActionVisiblity(View.GONE);
														setUpActionBar(View.GONE);
													}
												} else {
													mFeedbackTrackerAdapter
															.setActionVisiblity(View.GONE);
													setUpActionBar(View.GONE);
												}
											} else {
												mFeedbackTrackerAdapter
														.setActionVisiblity(View.GONE);
												setUpActionBar(View.GONE);
											}
										} else {
											mFeedbackTrackerAdapter
													.setActionVisiblity(View.GONE);
											setUpActionBar(View.GONE);
										}
									} else {
										mFeedbackTrackerAdapter
												.setActionVisiblity(View.GONE);
										setUpActionBar(View.GONE);
									}

								} else {
									mFeedbackTrackerAdapter
											.setActionVisiblity(View.GONE);
									setUpActionBar(View.GONE);
								}
							} else {
								mFeedbackTrackerAdapter
										.setActionVisiblity(View.GONE);
								setUpActionBar(View.GONE);
							}
						} else {
							mFeedbackTrackerAdapter
									.setActionVisiblity(View.GONE);
							setUpActionBar(View.GONE);
						}
					} else {
						mFeedbackTrackerAdapter.setActionVisiblity(View.GONE);
						setUpActionBar(View.GONE);
					}
				} else {
					mFeedbackTrackerAdapter.setActionVisiblity(View.GONE);
					setUpActionBar(View.GONE);
				}
			} else {
				mFeedbackTrackerAdapter.setActionVisiblity(View.GONE);
				setUpActionBar(View.GONE);
			}
		} else {
			mFeedbackTrackerAdapter.setActionVisiblity(View.GONE);
			setUpActionBar(View.GONE);
		}
	}

	private void setUpActionBar(int visiblity) {

		mLinerLayoutFMSTrackerFeedbackActionPanel.setVisibility(visiblity);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		p.setMargins(0, 2, 0, 0);
		mLinerLayoutFMSTrackerFeedbackActionPanel.removeAllViews();
		Button actionOne = new Button(this);
		actionOne.setTextColor(Color.WHITE);
		actionOne.setTextSize(12);
		actionOne.setPadding(6, 6, 6, 6);
		actionOne.setBackgroundResource(R.drawable.buttonblue);
		Button actionTwo = new Button(this);
		actionTwo.setTextColor(Color.WHITE);
		actionTwo.setTextSize(12);
		actionTwo.setBackgroundResource(R.drawable.buttonblue);
		actionTwo.setPadding(6, 6, 6, 6);

		switch (mSelectedStatusIds.get(0)) {

		// Pending for ETR 1
		case 1:
			actionOne.setText("ETR");
			p.weight = 0.5f;
			actionOne.setLayoutParams(p);
			actionOne.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "ETR",
							Toast.LENGTH_SHORT).show();

					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();

					if (!selectedItem.isEmpty()) {
						showETRDialog(selectedItem, MSSStatus.PENDING_FOR_RFC);
					} else {
						Toast.makeText(getApplicationContext(),
								"Please select at least one feedback",
								Toast.LENGTH_SHORT).show();
					}

				}
			});
			actionTwo.setText("RFC");
			p.weight = 0.5f;
			actionTwo.setLayoutParams(p);
			actionTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * Toast.makeText(getApplicationContext(), "RFC",
					 * Toast.LENGTH_SHORT).show();
					 */
					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();
					if (!selectedItem.isEmpty()) {
						showRFCDialog(selectedItem,
								MSSStatus.PENDING_FOR_CLOSURE);
					} else {
						Toast.makeText(getApplicationContext(),
								"Please select at least one feedback",
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionOne);
			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionTwo);

			break;
		case 2:
			actionTwo.setText("RFC");
			p.weight = 1.0f;
			actionTwo.setLayoutParams(p);
			actionTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * Toast.makeText(getApplicationContext(), "RFC",
					 * Toast.LENGTH_SHORT).show();
					 */
					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();
					if (!selectedItem.isEmpty()) {
						showRFCDialog(selectedItem,
								MSSStatus.PENDING_FOR_CLOSURE);
					}

				}
			});
			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionTwo);
			break;
		case 3:

			actionOne.setText("Reopen");
			p.weight = 0.5f;
			actionOne.setLayoutParams(p);
			actionOne.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "Reopen",
							Toast.LENGTH_SHORT).show();
					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();
					if (!selectedItem.isEmpty()) {
						showReopenDialog(selectedItem,
								MSSStatus.PENDING_FOR_ETR_2);
					}

				}
			});
			actionTwo.setText("Close");
			p.weight = 0.5f;
			actionTwo.setLayoutParams(p);
			actionTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "Close",
							Toast.LENGTH_SHORT).show();

					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();
					showRFCDialog(selectedItem, 4);

				}
			});

			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionOne);
			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionTwo);

			break;
		case 4:

			break;
		case 5:
			actionOne.setText("ETR");
			p.weight = 0.5f;
			actionOne.setLayoutParams(p);
			actionOne.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "ETR2",
							Toast.LENGTH_SHORT).show();
					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();

					if (!selectedItem.isEmpty()) {
						showETRDialog(selectedItem, MSSStatus.PENDING_FOR_RFC_2);
					}

				}
			});
			actionTwo.setText("RFC");
			p.weight = 0.5f;
			actionTwo.setLayoutParams(p);
			actionTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "RFC2",
							Toast.LENGTH_SHORT).show();
					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();

					if (!selectedItem.isEmpty()) {
						showRFCDialog(selectedItem,
								MSSStatus.PENDING_FOR_CLOSURE_2);
					}

				}
			});

			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionOne);
			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionTwo);

			break;
		case 6:
			actionTwo.setText("RFC");
			p.weight = 1.0f;
			actionTwo.setLayoutParams(p);
			actionTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "RFC2",
							Toast.LENGTH_SHORT).show();
					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();
					if (!selectedItem.isEmpty()) {
						showRFCDialog(selectedItem,
								MSSStatus.PENDING_FOR_CLOSURE_2);
					}

				}
			});
			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionTwo);

			break;
		case 7:
			actionOne.setText("Close with Agree");
			p.weight = 0.5f;
			actionOne.setLayoutParams(p);
			actionOne.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "Close with Agree",
							Toast.LENGTH_SHORT).show();

					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();
					if (!selectedItem.isEmpty()) {
						showRFCDialog(selectedItem, MSSStatus.CLOSE_WITH_AGREE);
					}

				}
			});
			actionTwo.setText("Close with Disagree");
			p.weight = 0.5f;
			actionTwo.setLayoutParams(p);
			actionTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							"Close with DisAgree", Toast.LENGTH_SHORT).show();
					ArrayList<FeedbackTrackerDto1> selectedItem = mFeedbackTrackerAdapter
							.getSelectedItem();
					if (!selectedItem.isEmpty()) {
						showRFCDialog(selectedItem,
								MSSStatus.CLOSE_WITH_DISAGREE);
					}

				}
			});

			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionOne);
			mLinerLayoutFMSTrackerFeedbackActionPanel.addView(actionTwo);
			break;
		case 8:

			break;
		case 9:

			break;

		default:
			break;
		}
		/**
		 * Pending for ETR 1 Pending for RFC 2 Pending For Closure 3 Closed 4
		 * Pending for ETR 2 5 Pending for RFC 2 6 Pending For Closure2 7 Closed
		 * with Aggree 8 Closed with Disagree 9 View All 0
		 */

	}

	protected void showRFCDialog(
			final ArrayList<FeedbackTrackerDto1> selectedItem,
			final int futureStatusId) {

		LayoutInflater inflater = FMSFeedbackTracker1.this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_fms_action, null);
		final EditText etdialogDate = (EditText) dialogView
				.findViewById(R.id.dialog_date);
		etdialogDate.setVisibility(View.GONE);

		final EditText etdialogRemark = (EditText) dialogView
				.findViewById(R.id.dialog_remark);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Action");
		builder.setView(dialogView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {

						try {
							JSONArray jsonArray = new JSONArray();
							for (FeedbackTrackerDto1 feedbackTrackerDto : selectedItem) {
								JSONObject arrayItemJson = new JSONObject();

								if (!isValidInputForETRDialog()) {
									return;
								}

								arrayItemJson.put("ETRDate", "");
								arrayItemJson.put("FeedbackID",
										feedbackTrackerDto.getFeedbackID());
								arrayItemJson
										.put("NewStatusID", futureStatusId);
								arrayItemJson.put("Remarks", etdialogRemark
										.getText().toString());
								arrayItemJson.put("UserIdPendingWith",
										feedbackTrackerDto.getUserID());
								jsonArray.put(arrayItemJson);
							}

							updateService(jsonArray);

						} catch (JSONException e) {
							e.printStackTrace();
						}

						dialog.dismiss();
					}

					private boolean isValidInputForETRDialog() {

						if (TextUtils.isEmpty(etdialogRemark.getText()
								.toString())) {
							Helper.showToast(getApplicationContext(),
									"Please enter remarks", Toast.LENGTH_LONG);
							return false;
						}

						return true;
					}

				})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		builder.create().show();

	}

	@SuppressLint("NewApi")
	protected void showDialog3(View v1, final Date minDate) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		final EditText v = (EditText) v1;
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

							v.setText(dateFormat.format(date));
							v.setTag(date);

							if (v.getId() == R.id.editTextFMSTrackerFromDate) {
								editTextFMSTrackerToDate.setTag(null);
								editTextFMSTrackerToDate.setText("");
							}

						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));

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
						@SuppressWarnings("deprecation")
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

									v.setText(dateFormat.format(date));
									v.setTag(date);

									if (v.getId() == R.id.editTextFMSTrackerFromDate) {
										editTextFMSTrackerToDate.setTag(null);
										editTextFMSTrackerToDate.setText("");
									}

								} else {
									Toast.makeText(getApplicationContext(),
											"Min Date Error",
											Toast.LENGTH_SHORT).show();
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

								v.setText(dateFormat.format(date));
								v.setTag(date);

								if (v.getId() == R.id.editTextFMSTrackerFromDate) {
									editTextFMSTrackerToDate.setTag(null);
									editTextFMSTrackerToDate.setText("");
								}

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

							Toast.makeText(getApplicationContext(),
									"Min Date Error", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						view.updateDate(year, month, date);
					}

				}
			};
		}

		dialog.show();

	}

	public void onTeamClick(View view) {
		mFeedbackTrackerAdapter.sortByTeam();
	}

	public void onCategoryClick(View view) {
		mFeedbackTrackerAdapter.sortByCategoryName();
	}

	public void onStatusClick(View view) {
		mFeedbackTrackerAdapter.sortByStatus();
	}

	public void onUpdatedOnClick(View view) {
		mFeedbackTrackerAdapter.sortByUpdatedOnDate();
	}

	protected void showReopenDialog(
			final ArrayList<FeedbackTrackerDto1> selectedItem,
			final int futureStatusId) {
		LayoutInflater inflater = FMSFeedbackTracker1.this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_fms_action, null);
		final EditText etdialogDate = (EditText) dialogView
				.findViewById(R.id.dialog_date);
		etdialogDate.setVisibility(View.GONE);
		final EditText etdialogRemark = (EditText) dialogView
				.findViewById(R.id.dialog_remark);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Action");
		builder.setView(dialogView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {

						try {
							JSONArray jsonArray = new JSONArray();
							for (FeedbackTrackerDto1 feedbackTrackerDto : selectedItem) {
								JSONObject arrayItemJson = new JSONObject();
								if (!isValidInputForETRDialog()) {

									return;
								}
								arrayItemJson.put("ETRDate", "");
								arrayItemJson.put("FeedbackID",
										feedbackTrackerDto.getFeedbackID());
								arrayItemJson
										.put("NewStatusID", futureStatusId);
								arrayItemJson.put("Remarks", etdialogRemark
										.getText().toString());
								arrayItemJson.put("UserIdPendingWith",
										feedbackTrackerDto.getSpocID());
								jsonArray.put(arrayItemJson);
							}

							updateService(jsonArray);

						} catch (JSONException e) {

							e.printStackTrace();
						}

						dialog.dismiss();
					}

					private boolean isValidInputForETRDialog() {

						if (TextUtils.isEmpty(etdialogRemark.getText()
								.toString())) {
							Helper.showToast(getApplicationContext(),
									"Please enter remarks", Toast.LENGTH_LONG);
							return false;
						}

						return true;
					}
				})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		builder.create().show();

	}

	protected void showETRDialog(
			final ArrayList<FeedbackTrackerDto1> selectedItem,
			final int futureStatusId) {
		LayoutInflater inflater = FMSFeedbackTracker1.this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_fms_action, null);
		final EditText etdialogDate = (EditText) dialogView
				.findViewById(R.id.dialog_date);
		etdialogDate.setKeyListener(null);
		etdialogDate.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {

					// showDatePickerDialog(etdialogDate);

					showDialog3(v, Calendar.getInstance().getTime());
				}

				return false;
			}
		});

		final EditText etdialogRemark = (EditText) dialogView
				.findViewById(R.id.dialog_remark);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Action");
		builder.setView(dialogView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {

						try {
							JSONArray jsonArray = new JSONArray();
							for (FeedbackTrackerDto1 feedbackTrackerDto : selectedItem) {
								JSONObject arrayItemJson = new JSONObject();

								try {

									if (!isValidInputForETRDialog()) {

										return;
									}

									arrayItemJson.put("ETRDate", etdialogDate
											.getText().toString());

								} catch (Exception e) {
									arrayItemJson.put("ETRDate", "");
								}
								arrayItemJson.put("FeedbackID",
										feedbackTrackerDto.getFeedbackID());
								arrayItemJson
										.put("NewStatusID", futureStatusId);
								arrayItemJson.put("Remarks", etdialogRemark
										.getText().toString());
								arrayItemJson.put("UserIdPendingWith",
										feedbackTrackerDto.getSpocID());
								jsonArray.put(arrayItemJson);
							}

							updateService(jsonArray);

						} catch (JSONException e) {
							e.printStackTrace();
						}

						dialog.dismiss();
					}

					private boolean isValidInputForETRDialog() {

						if (etdialogDate.getVisibility() == View.VISIBLE) {
							if (TextUtils.isEmpty(etdialogDate.getText()
									.toString())) {

								Helper.showToast(getApplicationContext(),
										"Please select date", Toast.LENGTH_LONG);
								return false;

							}
						}
						if (TextUtils.isEmpty(etdialogRemark.getText()
								.toString())) {
							Helper.showToast(getApplicationContext(),
									"Please enter remarks", Toast.LENGTH_LONG);
							return false;
						}

						return true;
					}
				})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		builder.create().show();

	}

	private void updateService(JSONArray jsonArrayFeedback) {

		try {
			JSONObject parmsJsonObject = new JSONObject();
			parmsJsonObject.put("Feedback", jsonArrayFeedback);
			parmsJsonObject.put("roleID", Helper.getIntValueFromPrefs(this,
					SharedPreferencesKey.PREF_ROLEID));
			parmsJsonObject.put("userID", Helper.getStringValuefromPrefs(this,
					SharedPreferencesKey.PREF_USERID));
/*
			PostDataToNetwork dataToNetwork = new PostDataToNetwork(this,
					"Updating...", new GetDataCallBack() {

						@Override
						public void processResponse(Object result) {
							if (result != null) {

								try {
									JSONObject jsonObject = new JSONObject(
											result.toString());
									boolean isSuccess = jsonObject
											.getBoolean("IsSuccess");

									if (isSuccess) {
										Toast.makeText(getApplicationContext(),
												"Updated successfully",
												Toast.LENGTH_SHORT).show();

									} else {
										Toast.makeText(getApplicationContext(),
												"Error: Action unsuccessful",
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

								FMSFeedbackTracker1.this.finish();
							}

						}
					});
			dataToNetwork.setConfig(getResources().getString(R.string.url),
					"UpdateFeedbacks");
			dataToNetwork.execute(parmsJsonObject);*/
			
			
			VolleyPostDataToNetwork pdn=new VolleyPostDataToNetwork(this, getString(R.string.updating), new VolleyGetDataCallBack() {
				
				@Override
				public void processResponse(Object result) {
					if (result != null) {

						try {
							JSONObject jsonObject = new JSONObject(
									result.toString());
							boolean isSuccess = jsonObject
									.getBoolean("IsSuccess");

							if (isSuccess) {
								Toast.makeText(getApplicationContext(),
										"Updated successfully",
										Toast.LENGTH_SHORT).show();

							} else {
								Toast.makeText(getApplicationContext(),
										"Error: Action unsuccessful",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						FMSFeedbackTracker1.this.finish();
					}

					
				}
				
				@Override
				public void onError(VolleyError error) {
					// TODO Auto-generated method stub
					
				}
			});
			
			pdn.setRequestData(parmsJsonObject);
			pdn.setConfig(getResources().getString(R.string.url), WebMethod.UPDATE_FEEDBACKS);
			pdn.callWebService();
			
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

	}

	private void resetList() {
		mFeedbackTrackerAdapter.clearDataSet();
		mLastFeedbackId = 0;
	}

	/*
	 * private void getStatusFromDatabase(final Handler handler) {
	 * 
	 * Thread mStatusThread = new Thread() {
	 * 
	 * @Override public void run() {
	 * 
	 * try { mTeamThread.join(); } catch (InterruptedException e1) {
	 * e1.printStackTrace(); } catch (Exception e) { }
	 * 
	 * try {
	 * 
	 * Cursor cursor = getContentResolver().query(
	 * ProviderContract.URI_FEEDBACK_STATUS, null, null, null, null);
	 * 
	 * 
	 * mStausList = DatabaseHelper.getConnection(
	 * getApplicationContext()).getFMSStatus();
	 * 
	 * 
	 * mStausList = DatabaseUtilMethods .getFeedbackStatusFromCursor(cursor);
	 * 
	 * Message msg = handler.obtainMessage(); Bundle bundle = new Bundle(); if
	 * (!mStausList.isEmpty()) { bundle.putBoolean("data_available", true); }
	 * else { bundle.putBoolean("data_available", false);
	 * 
	 * }
	 * 
	 * msg.setData(bundle); handler.sendMessage(msg); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * } };
	 * 
	 * mStatusThread.start();
	 * 
	 * }
	 */
	/*
	 * private static class GetStatusHandler extends Handler {
	 * 
	 * WeakReference<FMSFeedbackTracker1> fmsActivity;
	 * 
	 * public GetStatusHandler(FMSFeedbackTracker1 fms) {
	 * 
	 * fmsActivity = new WeakReference<FMSFeedbackTracker1>(fms); }
	 * 
	 * @Override public void handleMessage(Message msg) { FMSFeedbackTracker1
	 * fmsActivityInstance = fmsActivity.get();
	 * 
	 * boolean data_available = msg.getData().getBoolean("data_available");
	 * 
	 * if (data_available) {
	 * 
	 * if (fmsActivityInstance.mStausList != null) {
	 * 
	 * 
	 * fmsActivityInstance.mStatusAdapter
	 * .addStatus(fmsActivityInstance.mStausMap);
	 * 
	 * 
	 * fmsActivityInstance.mFeedbackTrackerAdapter
	 * .addStatusMap(fmsActivityInstance.mStausList);
	 * 
	 * fmsActivityInstance.editTextFMSTrackerStatusSelection
	 * .setText("View All"); fmsActivityInstance.mSelectedStatusIds.add(0);
	 * fmsActivityInstance.mStausList.get(0).setSelected(true);
	 * 
	 * }
	 * 
	 * } }; }
	 */

	class StausAdapter extends BaseAdapter {

		private HashMap<Integer, String> mData;
		private Integer[] mKeys;

		public StausAdapter(Context context) {
			mData = new HashMap<Integer, String>();

		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		public void addStatus(HashMap<Integer, String> data) {
			this.mData = data;
			mKeys = mData.keySet().toArray(new Integer[data.size()]);
			notifyDataSetChanged();
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			int key = mKeys[position];
			String statusName = getItem(key).toString();
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(
					android.R.layout.simple_dropdown_item_1line, parent, false);
			TextView text = (TextView) mySpinner
					.findViewById(android.R.id.text1);
			text.setText(statusName);
			return mySpinner;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(mKeys[position]);
		}

		@Override
		public long getItemId(int position) {
			return mKeys[position];
		}
	}

	/*
	 * private void getTeamsFromDatabase(final Handler handler) {
	 * 
	 * mTeamThread = new Thread() {
	 * 
	 * @Override public void run() {
	 * 
	 * try { mListTeam = DatabaseHelper.getConnection(
	 * getApplicationContext()).getTeams(); Message msg =
	 * handler.obtainMessage(); Bundle bundle = new Bundle(); if
	 * (!mListTeam.isEmpty()) { bundle.putBoolean("data_available", true);
	 * 
	 * } else { bundle.putBoolean("data_available", false); }
	 * 
	 * msg.setData(bundle); handler.sendMessage(msg); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * } };
	 * 
	 * mTeamThread.start();
	 * 
	 * }
	 */

	public void onSearchButtonClick(View v) {

		try {

			if (mFooterView.getVisibility() == View.VISIBLE) {
				mFooterView.setVisibility(View.INVISIBLE);
			}

			mLinerLayoutFMSTrackerFeedbackActionPanel.setVisibility(View.GONE);

			linerLayoutFMSTrackerSearchViewSection.startAnimation(slideUpAnim);
			isSearchVisible = false;

			resetList();

			String DateTo = editTextFMSTrackerToDate.getText().toString();
			String DateFrom = editTextFMSTrackerFromDate.getText().toString();

			int[] a = convertIntegers(mSelectedTeamIds);
			int[] b = convertIntegers(mSelectedCategoryIds);
			int[] c = convertIntegers(mSelectedCategoryTypeIds);
			int[] d = convertIntegers(mSelectedStatusIds);

			if (DateTo.length() > 0 && DateFrom.length() > 0) {
				getFeedbackList(a, b, c, d, DateFrom, DateTo);
			} else {
				Toast.makeText(getApplicationContext(),
						"Please enter From and To date", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	/*
	 * private static class GetTeamHandler extends Handler {
	 * 
	 * WeakReference<FMSFeedbackTracker1> fmsActivity;
	 * 
	 * public GetTeamHandler(FMSFeedbackTracker1 fms) {
	 * 
	 * fmsActivity = new WeakReference<FMSFeedbackTracker1>(fms); }
	 * 
	 * @Override public void handleMessage(Message msg) { FMSFeedbackTracker1
	 * fmsActivityInstance = fmsActivity.get();
	 * 
	 * boolean data_available = msg.getData().getBoolean("data_available");
	 * 
	 * if (data_available) {
	 * 
	 * if (fmsActivityInstance.mListTeam != null) { }
	 * 
	 * }
	 * 
	 * }; }
	 */

	/*
	 * private static class GetFeedbackCategoryHandler extends Handler {
	 * 
	 * WeakReference<FMSFeedbackTracker1> fmsActivity;
	 * 
	 * public GetFeedbackCategoryHandler(FMSFeedbackTracker1 fms) {
	 * 
	 * fmsActivity = new WeakReference<FMSFeedbackTracker1>(fms); }
	 * 
	 * @Override public void handleMessage(Message msg) { final
	 * FMSFeedbackTracker1 fmsActivityInstance = fmsActivity.get();
	 * 
	 * boolean data_available = msg.getData().getBoolean("data_available");
	 * 
	 * if (data_available) {
	 * 
	 * }
	 * 
	 * }; }
	 */
	public void onFilterButtonClick(View v) {

		if (isSearchVisible) {
			linerLayoutFMSTrackerSearchViewSection.startAnimation(slideUpAnim);

			isSearchVisible = false;
		} else {
			linerLayoutFMSTrackerSearchViewSection
					.startAnimation(slideDownAnim);

			isSearchVisible = true;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FEEDBACK_DETAIL_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				try {

					resetList();

					String DateTo = editTextFMSTrackerToDate.getText()
							.toString();
					String DateFrom = editTextFMSTrackerFromDate.getText()
							.toString();

					int[] selectedTeam = convertIntegers(mSelectedTeamIds);
					int[] selectedCategory = convertIntegers(mSelectedCategoryIds);
					int[] selectedType = convertIntegers(mSelectedCategoryTypeIds);
					int[] selectedStatus = convertIntegers(mSelectedStatusIds);
					if (DateTo.length() > 0 && DateFrom.length() > 0) {
						getFeedbackList(selectedTeam, selectedCategory,
								selectedType, selectedStatus, DateFrom, DateTo);
					} else {
						Toast.makeText(getApplicationContext(),
								"Please enter From and To date",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public boolean onTouch(final View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {

			switch (v.getId()) {
			case R.id.editTextFMSTrackerTeamSelection:

				if (mListTeam != null) {
					int teamCount = mListTeam.size();
					final String[] teamNameArray = new String[teamCount];
					final boolean[] _selectionsTeamName = new boolean[teamCount];
					for (int i = 0; i < teamCount; i++) {
						TeamMasterModel dataModal = mListTeam.get(i);
						teamNameArray[i] = dataModal.getTeamName();
						_selectionsTeamName[i] = dataModal.isSelected();
					}
					AlertDialog.Builder al = new AlertDialog.Builder(
							v.getContext())
							.setTitle("Team Name")
							.setMultiChoiceItems(
									teamNameArray,
									_selectionsTeamName,
									new DialogInterface.OnMultiChoiceClickListener() {
										public void onClick(
												DialogInterface dialog,
												int clicked, boolean selected) {
											_selectionsTeamName[clicked] = selected;
											mListTeam.get(clicked).setSelected(
													selected);
										}
									})
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int clicked) {
											dialog.dismiss();

											StringBuilder builderTeamName = new StringBuilder();

											mSelectedTeamIds.clear();
											editTextFMSTrackerTeamSelection
													.setText("");
											mSelectedCategoryIds.clear();
											editTextFMSTrackerCategorySelection
													.setText("");

											mSelectedCategoryTypeIds.clear();
											editTextFMSTrackerCategoryTypeSelection
													.setText("");

											for (TeamMasterModel team : mListTeam) {
												if (team.isSelected()) {
													builderTeamName.append(team
															.getTeamName()
															+ ",");

													mSelectedTeamIds.add(team
															.getTeamID());

												}
											}

											if (builderTeamName.length() > 0) {
												((EditText) v)
														.setText(builderTeamName
																.toString()
																.substring(
																		0,
																		builderTeamName
																				.toString()
																				.length() - 1));

												if (!mSelectedTeamIds.isEmpty()) {
													int count = mSelectedTeamIds
															.size();
													String teamIds[] = new String[count];
													for (int i = 0; i < count; ++i) {
														teamIds[i] = Integer
																.toString(mSelectedTeamIds
																		.get(i));
													}

													/*
													 * getFeedbackCatagoryFromDatabase
													 * ( teamIds, new
													 * GetFeedbackCategoryHandler
													 * (
													 * FMSFeedbackTracker1.this
													 * ));
													 */

													Bundle bundle = new Bundle();
													bundle.putStringArray(
															"teamIDs", teamIds);

													if (getSupportLoaderManager()
															.getLoader(
																	LOADER_ID_GET_FEEDBACK_CATEGORY) == null) {
														getSupportLoaderManager()
																.initLoader(
																		LOADER_ID_GET_FEEDBACK_CATEGORY,
																		bundle,
																		FMSFeedbackTracker1.this);

													} else {
														getSupportLoaderManager()
																.restartLoader(
																		LOADER_ID_GET_FEEDBACK_CATEGORY,
																		bundle,
																		FMSFeedbackTracker1.this);

													}

												}

											} else {
												((EditText) v).setText("");
											}
										}
									});
					al.create().show();
				}
				break;
			case R.id.editTextFMSTrackerCategorySelection:

				if (mListFeedbackCategory != null
						&& mListFeedbackCategory.size() > 0) {

					final int catCount = mListFeedbackCategory.size();
					final String[] catNameArray = new String[catCount];
					final boolean[] _selectionsCat = new boolean[catCount];

					for (int i = 0; i < catCount; i++) {
						FeedbackCategoryMasterModel dataModal = mListFeedbackCategory
								.get(i);
						catNameArray[i] = dataModal.getFeedbackCatName();
						_selectionsCat[i] = dataModal.isSelected();
					}

					AlertDialog.Builder listDialogCatagory = new AlertDialog.Builder(
							this)
							.setTitle("Catagery Name")
							.setMultiChoiceItems(
									catNameArray,
									_selectionsCat,
									new DialogInterface.OnMultiChoiceClickListener() {
										public void onClick(
												DialogInterface dialog,
												int clicked, boolean selected) {
											_selectionsCat[clicked] = selected;
											mListFeedbackCategory.get(clicked)
													.setSelected(selected);
										}
									})
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int clicked) {
											dialog.dismiss();

											StringBuilder builderFeedbackCatagory = new StringBuilder();

											mSelectedCategoryIds.clear();
											editTextFMSTrackerCategorySelection
													.setText("");
											mSelectedCategoryTypeIds.clear();
											editTextFMSTrackerCategoryTypeSelection
													.setText("");

											for (int i = 0; i < catCount; i++) {
												FeedbackCategoryMasterModel category = mListFeedbackCategory
														.get(i);
												if (category.isSelected()) {
													builderFeedbackCatagory.append(category
															.getFeedbackCatName()
															+ ",");
													mSelectedCategoryIds.add(category
															.getFeedbackCatID());
												}
											}
											if (builderFeedbackCatagory
													.length() > 0) {
												editTextFMSTrackerCategorySelection
														.setText(builderFeedbackCatagory
																.toString()
																.substring(
																		0,
																		builderFeedbackCatagory
																				.toString()
																				.length() - 1));

												if (!mSelectedCategoryIds
														.isEmpty()) {
													int count = mSelectedCategoryIds
															.size();

													String catId[] = new String[count];
													for (int i = 0; i < count; ++i) {
														catId[i] = Integer
																.toString(mSelectedCategoryIds
																		.get(i));
													}

													/*
													 * getFeedbackTypeFromDatabase(
													 * catId, new
													 * GetFeedbackTypeHandler(
													 * FMSFeedbackTracker1
													 * .this));
													 */
													Bundle bundle = new Bundle();
													bundle.putStringArray(
															"catIDs", catId);

													if (getSupportLoaderManager()
															.getLoader(
																	LOADER_ID_GET_FEEDBACK_TYPE) == null) {
														getSupportLoaderManager()
																.initLoader(
																		LOADER_ID_GET_FEEDBACK_TYPE,
																		bundle,
																		FMSFeedbackTracker1.this);

													} else {
														getSupportLoaderManager()
																.restartLoader(
																		LOADER_ID_GET_FEEDBACK_TYPE,
																		bundle,
																		FMSFeedbackTracker1.this);

													}
												}

											} else {
												editTextFMSTrackerCategorySelection
														.setText("");
											}
										}
									});
					listDialogCatagory.create().show();

				}

				break;
			case R.id.editTextFMSTrackerCategoryTypeSelection:

				if (mListFeedbackType != null) {

					if (!mListFeedbackType.isEmpty()) {
						int count = mListFeedbackType.size();
						final String[] catNameArray = new String[count];
						final boolean[] _selectionsCat = new boolean[count];

						for (int i = 0; i < count; i++) {
							FeedbackTypeMasterModel dataModal = mListFeedbackType
									.get(i);
							catNameArray[i] = dataModal.getFeedbackTypeName();
							_selectionsCat[i] = dataModal.isSelected();
						}

						AlertDialog.Builder listDialogCatagory = new AlertDialog.Builder(
								this)
								.setTitle("Catagery Type")
								.setMultiChoiceItems(
										catNameArray,
										_selectionsCat,
										new DialogInterface.OnMultiChoiceClickListener() {
											public void onClick(
													DialogInterface dialog,
													int clicked,
													boolean selected) {
												_selectionsCat[clicked] = selected;
												mListFeedbackType.get(clicked)
														.setSelected(selected);
											}
										})
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int clicked) {
												dialog.dismiss();

												StringBuilder builderFeedbackCatType = new StringBuilder();
												mSelectedCategoryTypeIds
														.clear();
												editTextFMSTrackerCategoryTypeSelection
														.setText("");

												for (FeedbackTypeMasterModel type : mListFeedbackType) {
													if (type.isSelected()) {
														builderFeedbackCatType.append(type
																.getFeedbackTypeName()
																+ ",");
														mSelectedCategoryTypeIds.add(type
																.getFeedbackTypeID());
													}

												}
												if (builderFeedbackCatType
														.length() > 0) {
													editTextFMSTrackerCategoryTypeSelection
															.setText(builderFeedbackCatType
																	.toString()
																	.substring(
																			0,
																			builderFeedbackCatType
																					.toString()
																					.length() - 1));
												} else {
													editTextFMSTrackerCategoryTypeSelection
															.setText("");
												}
											}
										});
						listDialogCatagory.create().show();
					}

				}

				break;
			case R.id.editTextFMSTrackerFromDate:

				showDialog2(v, null, Calendar.getInstance().getTime());

				break;
			case R.id.editTextFMSTrackerToDate:

				showDialog2(v, (Date) editTextFMSTrackerFromDate.getTag(),
						Calendar.getInstance().getTime());
				break;

			case R.id.spinnerFMSTrackerStatusSelection:

				if (mStausList != null) {

					if (!mStausList.isEmpty()) {

						int count = mStausList.size();
						final String[] statusNameArray = new String[count];
						final boolean[] _selectionsStatus = new boolean[count];

						for (int i = 0; i < count; i++) {
							FMSStatusDataModal dataModal = mStausList.get(i);
							statusNameArray[i] = dataModal.getStatusName();
							_selectionsStatus[i] = dataModal.isSelected();
						}

						AlertDialog.Builder al = new AlertDialog.Builder(
								v.getContext())
								.setTitle("Action")
								.setMultiChoiceItems(
										statusNameArray,
										_selectionsStatus,
										new DialogInterface.OnMultiChoiceClickListener() {
											public void onClick(
													DialogInterface dialog,
													int clicked,
													boolean selected) {
												_selectionsStatus[clicked] = selected;
												mStausList.get(clicked)
														.setSelected(selected);
											}
										})

								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int clicked) {
												dialog.dismiss();
												mSelectedStatusIds.clear();
												StringBuilder builderFeedbackStatus = new StringBuilder();

												for (FMSStatusDataModal status : mStausList) {
													if (status.isSelected()) {
														builderFeedbackStatus.append(status
																.getStatusName()
																+ ",");
														mSelectedStatusIds.add(status
																.getStausId());
													}

												}
												if (builderFeedbackStatus
														.length() > 0) {
													editTextFMSTrackerStatusSelection
															.setText(builderFeedbackStatus
																	.toString()
																	.substring(
																			0,
																			builderFeedbackStatus
																					.toString()
																					.length() - 1));
												} else {
													editTextFMSTrackerStatusSelection
															.setText("View All");
													mSelectedStatusIds.add(0);
													mStausList.get(0)
															.setSelected(true);

												}
											}
										});
						al.create().show();

					}

				}

				break;

			default:
				break;
			}
		}
		return false;
	}

	/*
	 * private static class GetFeedbackTypeHandler extends Handler {
	 * 
	 * WeakReference<FMSFeedbackTracker1> fmsActivity;
	 * 
	 * public GetFeedbackTypeHandler(FMSFeedbackTracker1 fms) {
	 * 
	 * fmsActivity = new WeakReference<FMSFeedbackTracker1>(fms); }
	 * 
	 * @Override public void handleMessage(Message msg) { final
	 * FMSFeedbackTracker1 fmsActivityInstance = fmsActivity.get();
	 * 
	 * boolean data_available = msg.getData().getBoolean("data_available");
	 * 
	 * if (data_available) {
	 * 
	 * }
	 * 
	 * }; }
	 */

	/*
	 * private void getFeedbackTypeFromDatabase(final String[] catId, final
	 * Handler handler) {
	 * 
	 * Thread thread = new Thread() {
	 * 
	 * @Override public void run() {
	 * 
	 * try { if (mListFeedbackType != null) { mListFeedbackType.clear();
	 * mListFeedbackType.addAll(DatabaseHelper.getConnection(
	 * getApplicationContext()) .getFeedbackTypeByFeedbackCatID(catId)); Message
	 * msg = handler.obtainMessage(); Bundle bundle = new Bundle();
	 * bundle.putBoolean("data_available", true);
	 * 
	 * msg.setData(bundle); handler.sendMessage(msg); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * } };
	 * 
	 * thread.start();
	 * 
	 * }
	 */
	/*
	 * private void getFeedbackCatagoryFromDatabase(final String[] teamID, final
	 * Handler handler) {
	 * 
	 * Thread thread = new Thread() {
	 * 
	 * @Override public void run() {
	 * 
	 * try {
	 * 
	 * if (mListFeedbackCategory != null) { mListFeedbackCategory.clear();
	 * mListFeedbackCategory.addAll(DatabaseHelper
	 * .getConnection(getApplicationContext())
	 * .getFeedbackCategoryByTeamID(teamID)); Message msg =
	 * handler.obtainMessage(); Bundle bundle = new Bundle(); if
	 * (!mListFeedbackCategory.isEmpty()) { bundle.putBoolean("data_available",
	 * true);
	 * 
	 * } else { bundle.putBoolean("data_available", false); }
	 * 
	 * msg.setData(bundle); handler.sendMessage(msg); } else {
	 * mListFeedbackCategory = new ArrayList<FeedbackCategoryMasterModel>(); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * } };
	 * 
	 * thread.start();
	 * 
	 * }
	 */

	@SuppressLint("NewApi")
	protected void showDialog2(View v1, final Date minDate, final Date maxDate) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		final EditText v = (EditText) v1;
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

							v.setText(dateFormat.format(date));
							v.setTag(date);

							if (v.getId() == R.id.editTextFMSTrackerFromDate) {
								editTextFMSTrackerToDate.setTag(null);
								editTextFMSTrackerToDate.setText("");
							}

						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));

			// (picker is a DatePicker)
			try {
				dialog.getDatePicker().setCalendarViewShown(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (maxDate != null) {
				dialog.getDatePicker().setMaxDate(maxDate.getTime());
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

							Calendar cal1 = Calendar.getInstance();
							cal1.setTime(maxDate);

							System.out.println("" + selectedYear + "="
									+ cal1.get(Calendar.YEAR));
							System.out.println("" + selectedMonth + "="
									+ cal1.get(Calendar.MONTH));
							System.out.println("" + selectedDate + "="
									+ cal1.get(Calendar.DATE));

							if (selectedYear <= cal1.get(Calendar.YEAR)
									&& selectedMonth <= cal1
											.get(Calendar.MONTH)
									&& selectedDate <= cal1.get(Calendar.DATE)) {
								if (minDate != null) {
									Calendar cal2 = Calendar.getInstance();
									cal2.setTime(minDate);

									System.out.println("Min " + selectedYear
											+ "=" + cal2.get(Calendar.YEAR));
									System.out.println("Min " + selectedMonth
											+ "=" + cal2.get(Calendar.MONTH));
									System.out.println("Min " + selectedDate
											+ "=" + cal2.get(Calendar.DATE));

									if (selectedYear >= cal2.get(Calendar.YEAR)
											&& selectedMonth >= cal2
													.get(Calendar.MONTH)
											&& selectedDate >= cal2
													.get(Calendar.DATE)) {

										Calendar cal = Calendar.getInstance();
										cal.set(Calendar.YEAR, selectedYear);
										cal.set(Calendar.MONTH, selectedMonth);
										cal.set(Calendar.DAY_OF_MONTH,
												selectedDate);
										cal.set(Calendar.HOUR_OF_DAY, 0);
										cal.set(Calendar.MINUTE, 0);
										cal.set(Calendar.SECOND, 0);
										cal.set(Calendar.MILLISECOND, 0);
										Date date = cal.getTime();

										DateFormat dateFormat = new SimpleDateFormat(
												"dd/MMM/yyyy", Locale.ENGLISH);

										v.setText(dateFormat.format(date));
										v.setTag(date);

										if (v.getId() == R.id.editTextFMSTrackerFromDate) {
											editTextFMSTrackerToDate
													.setTag(null);
											editTextFMSTrackerToDate
													.setText("");
										}

									} else {
										Toast.makeText(getApplicationContext(),
												"Min Date Error",
												Toast.LENGTH_SHORT).show();
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

									v.setText(dateFormat.format(date));
									v.setTag(date);

									if (v.getId() == R.id.editTextFMSTrackerFromDate) {
										editTextFMSTrackerToDate.setTag(null);
										editTextFMSTrackerToDate.setText("");
									}

								}

							} else {
								Toast.makeText(getApplicationContext(),
										"Max Date Error", Toast.LENGTH_SHORT)
										.show();
							}

						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
							.get(Calendar.DAY_OF_MONTH)) {
				@Override
				public void onDateChanged(DatePicker view, int year, int month,
						int date) {

					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(maxDate);

					System.out.println("" + year + "="
							+ cal1.get(Calendar.YEAR));
					System.out.println("" + month + "="
							+ cal1.get(Calendar.MONTH));
					System.out.println("" + date + "="
							+ cal1.get(Calendar.DATE));

					if (year <= cal1.get(Calendar.YEAR)
							&& month <= cal1.get(Calendar.MONTH)
							&& date <= cal1.get(Calendar.DATE)) {
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

								Toast.makeText(getApplicationContext(),
										"Min Date Error", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							view.updateDate(year, month, date);
						}

					} else {

						Toast.makeText(getApplicationContext(),
								"Max Date Error", Toast.LENGTH_SHORT).show();
					}
				}
			};
		}

		dialog.show();

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		Loader loader = null;
		switch (id) {
		case LOADER_ID_GET_FEEDBACK_STATUS:

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_FEEDBACK_STATUS, null, null, null,
					null);
			break;

		case LOADER_ID_GET_FEEDBACK_TEAMS:

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_FEEDBACK_TEAMS, null, null, null, null);
			break;

		case LOADER_ID_GET_FEEDBACK_TYPE:

			String[] catID = bundle.getStringArray("catIDs");
			String selection = FeedbackTypeMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID
					+ " IN ("
					+ DatabaseUtilMethods.makePlaceholders(catID.length) + ")";

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_FEEDBACK_TYPE, null, selection, catID,
					null);

			break;
		case LOADER_ID_GET_FEEDBACK_CATEGORY:

			String[] teamIDs = bundle.getStringArray("teamIDs");

			String catSelection = FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID
					+ " IN ("
					+ DatabaseUtilMethods.makePlaceholders(teamIDs.length)
					+ ")";

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_FEEDBACK_CATEGORY, null, catSelection,
					teamIDs, null);

			break;
		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		switch (loader.getId()) {
		case LOADER_ID_GET_FEEDBACK_STATUS:

			if (cursor != null && cursor.moveToFirst()) {
				mStausList = DatabaseUtilMethods
						.getFeedbackStatusFromCursor(cursor);
				mFeedbackTrackerAdapter.addStatusMap(mStausList);

				editTextFMSTrackerStatusSelection.setText("View All");
				mSelectedStatusIds.add(0);
				mStausList.get(0).setSelected(true);

			}
			break;
		case LOADER_ID_GET_FEEDBACK_TEAMS:

			if (cursor != null && cursor.moveToFirst()) {
				mListTeam = DatabaseUtilMethods.getFMSTeamsFromCursor(cursor);

			}
			break;

		case LOADER_ID_GET_FEEDBACK_TYPE:
			
			mListFeedbackType.clear();
			
			if (cursor != null && cursor.moveToFirst()) {

			

				mListFeedbackType.addAll(DatabaseUtilMethods
						.getFMSTypeFromCursor(cursor));

			}
			break;

		case LOADER_ID_GET_FEEDBACK_CATEGORY:

			if (mListFeedbackCategory != null) {
				mListFeedbackCategory.clear();

				if (cursor != null && cursor.moveToFirst()) {
					List<FeedbackCategoryMasterModel> listCat = DatabaseUtilMethods
							.getFMSCategoriesFromCursor(cursor);

					mListFeedbackCategory.addAll(listCat);
				}
			} else {
				mListFeedbackCategory = new ArrayList<FeedbackCategoryMasterModel>();
			}

			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}
