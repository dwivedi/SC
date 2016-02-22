package com.samsung.ssc.activitymodule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.MSSStatus;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.FeedbackDetailModel;
import com.samsung.ssc.dto.FeedbackLogModel;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.ImageLoader;

public class FeedbackDetailActivity extends Activity {

	private static final int TAKE_ACTION_REQUEST_CODE = 0;
	private TextView mTvConcernedTeam, mTvFeebackCategory, mTvFeedbackType;
	private View btTakeAction, llSeparator;
	private LinearLayout logContaner;
	private int mCurrentStatus, mFeedbackUserID, mSpocID;
	private TextView tvFeedbackID;
	// private TextView textviewTitle;
	private TextView tvTimeTaken;
	private long feedbackId;
	private HashMap<Integer, String> feedbackStatusMap;
	private ImageView mIvSnapShot;
	private int RESULT_CODE_FOR_ACTION;

	private static class GetFeedbackDetailHandler extends Handler {

		WeakReference<FeedbackDetailActivity> feedbackDetailActivity;

		public GetFeedbackDetailHandler(FeedbackDetailActivity fda) {

			feedbackDetailActivity = new WeakReference<FeedbackDetailActivity>(
					fda);
		}

		@Override
		public void handleMessage(Message msg) {

			try {
				FeedbackDetailActivity feedbackDetailActivityInstance = feedbackDetailActivity
						.get();

				String values[] = msg.getData().getStringArray("values");

				if (values[0] != null) {
					feedbackDetailActivityInstance.mTvConcernedTeam
							.setText(values[0]);
				}
				if (values[1] != null) {
					feedbackDetailActivityInstance.mTvFeebackCategory
							.setText(values[1]);
				}

				if (values[2] != null) {
					feedbackDetailActivityInstance.mTvFeedbackType
							.setText(values[2]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.setScreenShotOff(this);
		setContentView(R.layout.activty_feedback_detail1);
		setupView();
	}

	private void setupView() {

		feedbackStatusMap = DatabaseHelper.getConnection(this)
				.getFeedbackStatus();

		// TextView textviewTitle = (TextView)
		// findViewById(R.id.headercentretext);
		tvFeedbackID = (TextView) findViewById(R.id.tvFeedbackID_feedbackDetail);
		mTvConcernedTeam = (TextView) findViewById(R.id.tvConcernedTeam_feedbackDetail);
		mTvFeebackCategory = (TextView) findViewById(R.id.tvfeedbackCategory_feedbackDetail);
		mTvFeedbackType = (TextView) findViewById(R.id.tvfeedbackType_feedbackDetail);

		mIvSnapShot = (ImageView) findViewById(R.id.iv_SnapShotActivity_feedbackDetail);

		btTakeAction = findViewById(R.id.btTakeAction_fFeedbackDetail);

		llSeparator = findViewById(R.id.llSeparator_FeedbackDetail);
		logContaner = (LinearLayout) this
				.findViewById(R.id.conteer_LogDetails_feedbackDetail);
		tvTimeTaken = (TextView) findViewById(R.id.tvTimeTaken_feedbackDetail);

		// textviewTitle.setText("Feedback Detail");

		feedbackId = getIntent().getExtras().getLong("FeedbackID");

		try {
			getFeedbackDetail(feedbackId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getFeedbackDetail(long feedbackID) {
		try {
			if (Helper.isOnline(getApplicationContext())) {

				JSONObject arguments = new JSONObject();

				arguments.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
						.getStringValuefromPrefs(FeedbackDetailActivity.this,
								SharedPreferencesKey.PREF_USERID)));
				arguments.put(WebConfig.WebParams.ROLE_ID, Helper
						.getIntValueFromPrefs(FeedbackDetailActivity.this,
								SharedPreferencesKey.PREF_ROLEID));

				arguments.put("FeedbackID", feedbackID);

				PostDataToNetwork pdn = new PostDataToNetwork(
						FeedbackDetailActivity.this, "Loading",
						new GetDataCallBack() {

							@Override
							public void processResponse(Object result) {

								if (result != null) {

									parserFeedbackResult(result.toString());

								}
							}
						});

				pdn.setConfig(getString(R.string.url), "GetFeedbackDetails");
				pdn.execute(arguments);
			} else {

				Helper.showAlertDialog(
						FeedbackDetailActivity.this,
						SSCAlertDialog.ERROR_TYPE,
						getString(R.string.error1),
						getString(R.string.error2),
						getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();
							}
						}, false, null, null);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void parserFeedbackResult(String result) {

		try {
			JSONObject jsonObject = new JSONObject(result);
			boolean isSuccess = jsonObject.getBoolean("IsSuccess");
			if (isSuccess) {
				JSONObject singleResult = jsonObject
						.getJSONObject("SingleResult");
				FeedbackDetailModel feedbackDataModel = new FetchingdataParser(
						getApplicationContext())
						.getFeedbackDetailModel(singleResult);
				if (feedbackDataModel != null) {
					if (!Helper.isNullOrEmpty(feedbackDataModel
							.getFeedbackNumber())) {
						tvFeedbackID.setText(feedbackDataModel
								.getFeedbackNumber());
					}
					tvTimeTaken.setText(feedbackDataModel.getTimeTaken() + "");

					try {
						int totalHours = (int) feedbackDataModel.getTimeTaken();

						int days = totalHours / 24;
						int hours = totalHours % 24;

						String timeTaken = days + " Days ," + hours + " Hours";
						tvTimeTaken.setText(timeTaken);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					setUpFeedbackLogView(feedbackDataModel);
					mCurrentStatus = feedbackDataModel
							.getCurrentFeedbackStatusID();

					getFeedbackDetailFromDatabase(new GetFeedbackDetailHandler(
							this), feedbackDataModel.getTeamID(),
							feedbackDataModel.getFeedbackCatID(),
							feedbackDataModel.getFeedbackTypeID());

					mFeedbackUserID = feedbackDataModel.getFeedbackUserID();
					mSpocID = feedbackDataModel.getSpocID();

					try {
						int userIdLogined = Integer.parseInt(Helper
								.getStringValuefromPrefs(
										getApplicationContext(),
										SharedPreferencesKey.PREF_USERID));

						if (!canShowTakeActionButton(mFeedbackUserID, mSpocID,
								userIdLogined)) {
							removeTakeActionButton();
						}
					} catch (NumberFormatException e) {

					}

					final String imageUrl = feedbackDataModel
							.getFeedbackImageURL();

					if (imageUrl != null && !imageUrl.equalsIgnoreCase("null")) {

						ImageLoader
								.getInstance(getApplicationContext())
								.displayImage(
										getResources()
												.getString(
														R.string.url_file_processor_servicebase)
												+ "id="
												+ imageUrl
												+ "&type=FMS",
										(ImageView) findViewById(R.id.iv_SnapShotActivity_feedbackDetail));

						mIvSnapShot.setClickable(true);

						mIvSnapShot.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent intent = new Intent(
										FeedbackDetailActivity.this,
										ActivityFullScreenImageView.class);
								intent.putExtra("image_path", imageUrl);
								intent.putExtra("called_from",
										"feedback_detail");
								startActivity(intent);

							}
						});

					}

				}

			} else {

				Helper.showCustomToast(getApplicationContext(),
						R.string.message_false, Toast.LENGTH_LONG);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private boolean canShowTakeActionButton(int feedbackUserID, int spocID,
			int loginedUserId) {

		if (feedbackUserID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_CLOSURE) {
			return true;
		} else if (feedbackUserID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_CLOSURE_2) {
			return true;
		} else if (spocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_ETR) {
			return true;
		} else if (spocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_ETR_2) {
			return true;
		} else if (spocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_RFC) {
			return true;
		} else if (spocID == loginedUserId
				&& mCurrentStatus == MSSStatus.PENDING_FOR_RFC_2) {
			return true;
		}

		return false;

	}

	/*
	 * this method shows the UI which displays the feedback log
	 */
	private void setUpFeedbackLogView(FeedbackDetailModel feedbackDataModels) {

		logContaner.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);

		try {
			ArrayList<FeedbackLogModel> logs = feedbackDataModels
					.getFeedbackLogs();

			for (FeedbackLogModel feedbackLogModel : logs) {

				RelativeLayout rowView = (RelativeLayout) inflater.inflate(
						R.layout.feedback_log_deatils_row_view, null);
				final TextView tvHeaderView = (TextView) rowView
						.findViewById(R.id.tv_feedback_log_deatils_header);

				if (!feedbackStatusMap.isEmpty()) {
					String statusName = feedbackStatusMap.get(feedbackLogModel
							.getStatusID());
					if (statusName != null) {
						tvHeaderView.setText(statusName);
					} else {
						tvHeaderView
								.setText("Status Name not found in database");
					}

				} else {
					tvHeaderView.setText("Status Name not found in database");
				}

				final LinearLayout rowLayutConter = (LinearLayout) rowView
						.findViewById(R.id.layout_container_feedback_log_deatils);

				HashMap<String, String> map = feedbackLogModel.getValueMap();

				rowView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (rowLayutConter.getVisibility() == View.GONE) {
							rowLayutConter.setVisibility(View.VISIBLE);
							tvHeaderView
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, R.drawable.collapse, 0);
						} else {
							rowLayutConter.setVisibility(View.GONE);
							tvHeaderView
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, R.drawable.expand, 0);
						}
					}
				});

				for (String key : map.keySet()) {
					LinearLayout layout;
					String value = map.get(key);
					if (!TextUtils.isEmpty(value)) {

						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.MATCH_PARENT);
						params.weight = 1.0f;

						layout = new LinearLayout(rowLayutConter.getContext());
						layout.setWeightSum(2);

						TextView tvTitle = new TextView(layout.getContext());

						tvTitle.setTextColor(Color.BLACK);
						tvTitle.setText(key);
						layout.addView(tvTitle, params);

						TextView tvTitle1 = new TextView(layout.getContext());
						tvTitle1.setGravity(Gravity.CENTER_VERTICAL
								| Gravity.RIGHT);
						tvTitle1.setTextColor(Color.BLACK);
						tvTitle1.setText(value);
						layout.addView(tvTitle1, params);
						rowLayutConter.addView(layout);

					}

				}

				// collapse log detail views initially
				rowLayutConter.setVisibility(View.GONE);
				tvHeaderView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.expand, 0);

				logContaner.addView(rowView);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getFeedbackDetailFromDatabase(final Handler handler,
			final int... ids) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				try {

					Log.e("thread", "thread for type");
					String values[] = DatabaseHelper.getConnection(
							getApplicationContext()).getFeedbackDetail(ids);

					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putStringArray("values", values);

					msg.setData(bundle);
					handler.sendMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		thread.start();

	}

	public void onTakeActionClick(View view) {

		Intent intent = new Intent(this, TakeActionFeedbackDetailActivity.class);

		intent.putExtra("feedback_user_id", mFeedbackUserID);
		intent.putExtra("spoc_id", mSpocID);
		intent.putExtra("current_status", mCurrentStatus);

		intent.putExtra("feedbackID", feedbackId);

		startActivityForResult(intent, TAKE_ACTION_REQUEST_CODE);
		// startActivity(intent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_ACTION_REQUEST_CODE) {
				getFeedbackDetail(feedbackId);
				RESULT_CODE_FOR_ACTION = RESULT_OK;
			}
		}
	}

	public void onCancelClick(View view) {
		setResult(RESULT_CODE_FOR_ACTION, getIntent());
		finish();
	}

	private void removeTakeActionButton() {
		if (btTakeAction.getVisibility() == View.VISIBLE) {
			btTakeAction.setVisibility(View.GONE);
		}
		if (llSeparator.getVisibility() == View.VISIBLE) {
			llSeparator.setVisibility(View.GONE);
		}

	}

	@Override
	public void onBackPressed() {

		setResult(RESULT_CODE_FOR_ACTION, getIntent());
		super.onBackPressed();
	}

}
