package com.samsung.ssc.activitymodule;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.CustomSpinneFeedbackTypeAdapter;
import com.samsung.ssc.adapters.CustomSpinnerFeedbackCategoryAdapter;
import com.samsung.ssc.adapters.CustomSpinnerTeamAdapter;
import com.samsung.ssc.adapters.NothingSelectionSpinnerAdapter;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.constants.WebConfig.WebMethod;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackCategoryMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackTypeMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.UserFeedbackMasterColumns;
import com.samsung.ssc.dto.FeedbackCategoryMasterModel;
import com.samsung.ssc.dto.FeedbackTypeMasterModel;
import com.samsung.ssc.dto.UserFeedback;
import com.samsung.ssc.dto.UserFeedbackValues;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.ImageLoader;

public class FMSCreateFeedbackActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private static final int REQUEST_FILE = 0;
	private static final int REQUEST_CAMERA = 1;
	private static final int DEALER_SELECTION_REQUEST = 2;
	private String mStImagePath, mStRemark, mStTeamName, mStFeedbackCatName,
			mStFeedbackType;

	private ImageView mIvSnapshot, mIvSampleImage;
	private Button mBtSnapshot, mBtEditSnapshot, mBtAdd, mBtSubmit;
	private TextView mTvAddSnapshot, mTvSnapshot;
	private CustomSpinnerFeedbackCategoryAdapter mAdapterFeedbackCategoty;
	private CustomSpinneFeedbackTypeAdapter mAdapterFeedbackType;

	private Spinner mSpinnerTeam, mSpinnerFeedbackCategory,
			mSpinnerFeedbackType;

	private int mSelectedFeedbackCatID = -1, mSelectedFeedbackTypeID = -1,
			mSelectedTeamID = -1;
	private EditText mEtRemark;
	private View mTableHeader;
	private LinearLayout mllTableContainer;
	private LayoutInflater mLayoutInflater;

	private List<FeedbackCategoryMasterModel> mListFeedbackCategory;
	private List<FeedbackTypeMasterModel> mListFeedbackType;
	private List<UserFeedback> mListUserFeedbacks;
	private int storeId;
	protected File mFeedbackImagePath;

	private final int LOADER_ID_GET_TEAMS = 1;
	private final int LOADER_ID_GET_CATEGORY = 2;
	private final int LOADER_ID_GET_TYPE = 3;
	private final int LOADER_ID_GET_USER_FEEDBACK = 4;

	private LoaderManager mLoaderManager;

	private void addSingleFeedbackRow(String teamName, String catName,
			String feedbackTypeName, long rowID) {

		if (rowID == -1) {
			return;
		}

		enableSubmitButton();
		showTableHeader();

		LinearLayout ll_single_row = (LinearLayout) mLayoutInflater.inflate(
				R.layout.fms_table_single_row, null, false);

		TextView tv_team_name = (TextView) ll_single_row
				.findViewById(R.id.tv_team_name_table_row);
		TextView tv_feedback_category = (TextView) ll_single_row
				.findViewById(R.id.tv_feedback_category_table_row);
		TextView tv_feedback_type = (TextView) ll_single_row
				.findViewById(R.id.tv_feedback_type_table_row);

		if (teamName != null) {
			tv_team_name.setText(teamName);
		}
		if (catName != null) {
			tv_feedback_category.setText(catName);
		}
		if (feedbackTypeName != null) {
			tv_feedback_type.setText(feedbackTypeName);
		}

		Button bt_delete_row = (Button) ll_single_row
				.findViewById(R.id.bt_delete_table_row);

		bt_delete_row.setOnClickListener(mTableRowDeleteButtonClickListener);

		bt_delete_row.setTag(R.string.database_row_id, rowID);
		mllTableContainer.addView(ll_single_row, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

	}

	private static class GetUserFeedbacksHandler extends Handler {

		WeakReference<FMSCreateFeedbackActivity> fmsActivity;

		public GetUserFeedbacksHandler(FMSCreateFeedbackActivity fms) {

			fmsActivity = new WeakReference<FMSCreateFeedbackActivity>(fms);
		}

		@Override
		public void handleMessage(Message msg) {
			FMSCreateFeedbackActivity fmsActivityInstance = fmsActivity.get();

			boolean data_available = msg.getData().getBoolean("data_available");

			if (data_available) {

				if (fmsActivityInstance.mListUserFeedbacks != null
						&& fmsActivityInstance.mListUserFeedbacks.size() > 0) {

					fmsActivityInstance.submitUserFeedbacks();

				}

			}

		};
	}

	private AdapterView.OnItemSelectedListener spinnerItemSelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			switch (parent.getId()) {
			case R.id.spinnerTeamFMSCreateFeedback:

				if (position != 0) {

					try {
						mSelectedTeamID = (Integer) view.getTag();

						mSelectedFeedbackCatID = -1;
						mSelectedFeedbackTypeID = -1;
						mEtRemark.setText("");
						mStTeamName = ((TextView) view.findViewById(R.id.text1))
								.getText().toString();
						/*
						 * getFeedbackCatagoryFromDatabase(mSelectedTeamID, new
						 * GetFeedbackCategoryHandler(
						 * FMSCreateFeedbackActivity.this));
						 */

						Loader loader = mLoaderManager
								.getLoader(LOADER_ID_GET_CATEGORY);
						if (loader == null) {
							getSupportLoaderManager().initLoader(
									LOADER_ID_GET_CATEGORY, null,
									FMSCreateFeedbackActivity.this);
						} else {
							getSupportLoaderManager().restartLoader(
									LOADER_ID_GET_CATEGORY, null,
									FMSCreateFeedbackActivity.this);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				break;
			case R.id.spinnerFeedbackCategoryFMSCreateFeedback:

				if (position != 0) {

					try {
						mSelectedFeedbackCatID = (Integer) view.getTag();
						mSelectedFeedbackTypeID = -1;
						mEtRemark.setText("");
						mStFeedbackCatName = ((TextView) view
								.findViewById(R.id.text1)).getText().toString();

						/*
						 * getFeedbackTypeFromDatabase(mSelectedFeedbackCatID,
						 * new GetFeedbackTypeHandler(
						 * FMSCreateFeedbackActivity.this));
						 */

						Loader loader = mLoaderManager
								.getLoader(LOADER_ID_GET_TYPE);
						if (loader == null) {
							getSupportLoaderManager().initLoader(
									LOADER_ID_GET_TYPE, null,
									FMSCreateFeedbackActivity.this);
						} else {
							getSupportLoaderManager().restartLoader(
									LOADER_ID_GET_TYPE, null,
									FMSCreateFeedbackActivity.this);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				break;
			case R.id.spinnerFeedbackTypeFMSCreateFeedback:

				if (position != 0) {
					try {
						mStFeedbackType = ((TextView) view
								.findViewById(R.id.text1)).getText().toString();
						mSelectedFeedbackTypeID = (Integer) view.getTag();
						mEtRemark.setText("");
						String sampleImageName = (String) view
								.getTag(R.string.sample_image_tag);

						setSampleImage(sampleImageName);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			default:
				break;
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private TextWatcher mRemarkTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

			try {
				mStRemark = mEtRemark.getText().toString();
				if (!mStRemark.equals("")
						&& (mSpinnerFeedbackCategory.isEnabled() && mSpinnerFeedbackType
								.isEnabled()) && mSelectedTeamID != -1
						&& mSelectedFeedbackCatID != -1
						&& mSelectedFeedbackTypeID != -1) {

					enableAddButton();

				} else {
					disableAddButton();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	private View.OnClickListener mTableRowDeleteButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(final View v) {

			final long database_row_id = (Long) v
					.getTag(R.string.database_row_id);

			Helper.showAlertDialog(
					FMSCreateFeedbackActivity.this,
					SSCAlertDialog.WARNING_TYPE,
					"Delete",
					"Are you sure you want to delete ?",
					getString(R.string.yes),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();

							deleteRowFromDatabase(database_row_id);
						}
					}, true, getString(R.string.no),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();
						}
					});

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.setScreenShotOff(this);
		setContentView(R.layout.fms_create_feedback_activity1);

		mLoaderManager = getSupportLoaderManager();
		mListFeedbackCategory = new ArrayList<FeedbackCategoryMasterModel>();
		mListFeedbackType = new ArrayList<FeedbackTypeMasterModel>();

		setUpViews();

		// disable feedback and sub-feedback spinner
		setWidgetsVisibility();

	}

	/**
	 * 
	 */
	private void setUpViews() {

		storeId = getIntent().getExtras().getInt(IntentKey.STORE_ID);

		mLayoutInflater = LayoutInflater.from(getApplicationContext());

		// TextView textViewTitle = (TextView)
		// findViewById(R.id.headercentretext);
		// textViewTitle.setText("Create Feedback");
		mBtAdd = (Button) findViewById(R.id.bt_add_fms_create_feedback);
		mBtSubmit = (Button) findViewById(R.id.bt_submit_fms_create_feedback);
		mEtRemark = (EditText) findViewById(R.id.et_remark_fms_create_feedback);
		mTableHeader = findViewById(R.id.table_header_fms_create_feedback);
		mllTableContainer = (LinearLayout) findViewById(R.id.ll_table_container_fms_create_feedback);
		mIvSampleImage = (ImageView) findViewById(R.id.iv_sample_image);

		mEtRemark.addTextChangedListener(mRemarkTextWatcher);
		// Team Spinner
		mSpinnerTeam = (Spinner) findViewById(R.id.spinnerTeamFMSCreateFeedback);
		mSpinnerTeam.setOnItemSelectedListener(spinnerItemSelectListener);

		mLoaderManager.initLoader(LOADER_ID_GET_TEAMS, null, this);

		// getTeamsFromDatabase(new GetTeamHandler(this));

		// Feedback Spinner
		mSpinnerFeedbackCategory = (Spinner) findViewById(R.id.spinnerFeedbackCategoryFMSCreateFeedback);
		mAdapterFeedbackCategoty = new CustomSpinnerFeedbackCategoryAdapter(
				mListFeedbackCategory, getApplicationContext());

		mSpinnerFeedbackCategory.setAdapter(new NothingSelectionSpinnerAdapter(
				mAdapterFeedbackCategoty,
				R.layout.issue_spinner_nothing_selected, this));

		mSpinnerFeedbackCategory
				.setOnItemSelectedListener(spinnerItemSelectListener);

		// SubFeedback Spinner
		mSpinnerFeedbackType = (Spinner) findViewById(R.id.spinnerFeedbackTypeFMSCreateFeedback);

		mAdapterFeedbackType = new CustomSpinneFeedbackTypeAdapter(
				mListFeedbackType, getApplicationContext());

		mSpinnerFeedbackType.setAdapter(new NothingSelectionSpinnerAdapter(
				mAdapterFeedbackType,
				R.layout.sub_issue_spinner_nothing_selected, this));

		mSpinnerFeedbackType
				.setOnItemSelectedListener(spinnerItemSelectListener);

		mIvSnapshot = (ImageView) findViewById(R.id.ivSnapShotFMSCreateFeedback);
		mBtSnapshot = (Button) findViewById(R.id.btnSnapShotFMSCreateFeedback);
		mBtEditSnapshot = (Button) findViewById(R.id.btnEditSnapShotFMSCreateFeedback);
		mTvAddSnapshot = (TextView) findViewById(R.id.tv_add_SnapShotFMSCreateFeedback);
		mTvSnapshot = (TextView) findViewById(R.id.tv_snapshot_SnapShotFMSCreateFeedback);

		if (storeId == -1) {
			showStoreSelection();

		} else {
			mLoaderManager.initLoader(LOADER_ID_GET_USER_FEEDBACK, null, this);

			// new GetExistedFeedbackAsyncTask().execute();
		}

	}

	private void showStoreSelection() {

		Intent intent = new Intent(this, DealerSelectionActivity.class);
		startActivityForResult(intent, DEALER_SELECTION_REQUEST);
	}

	private void setFeedbackCategoryData() {

		mAdapterFeedbackCategoty.notifyDataSetChanged();
		mSpinnerFeedbackCategory.setSelection(0);

		enableFeedbackCategorySpinner();
		disableFeedbackTypeSpinner();
	}

	private void setFeedbackTypeData() {

		mAdapterFeedbackType.notifyDataSetChanged();
		mSpinnerFeedbackType.setSelection(0);
		enableFeedbackTypeSpinner();

	}

	public void onSubmitClick(View view) {

		Helper.showAlertDialog(FMSCreateFeedbackActivity.this,
				SSCAlertDialog.NORMAL_TYPE, "Submit",
				"Do you want to submit ?", getString(R.string.yes),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();

						if (Helper.isOnline(getApplicationContext())) {
							getAllUserFeedback(new GetUserFeedbacksHandler(
									FMSCreateFeedbackActivity.this));

						} else {

							// Offline

							Helper.showCustomToast(getApplicationContext(),
									R.string.data_saved_successfully,
									Toast.LENGTH_LONG);

							FMSCreateFeedbackActivity.this.finish();

						}
					}
				}, true, getString(R.string.no),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});

	}

	public void onAddClick(View view) {

		try {
			if (storeId != -1) {
				/*
				 * insertRowIntoDatabase(createUserFeedbackDto(), new
				 * InsertRowHandler(this));
				 */
				Cursor cursor = null;

				try {
					String selection = UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID
							+ "=? AND "
							+ UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID
							+ "=? AND "
							+ UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_TYPE_ID
							+ "=? AND "
							+ UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_STORE_ID
							+ "=?";
					String[] whereArgs = { mSelectedTeamID + "",
							mSelectedFeedbackCatID + "",
							mSelectedFeedbackTypeID + "", storeId + "" };

					cursor = getContentResolver().query(
							ProviderContract.URI_USER_FEEDBACK_ENTRY_CHECK,
							null, selection, whereArgs, null);

					if (cursor.getCount() > 0) {
						Helper.showCustomToast(this, R.string.feedback_exist,
								Toast.LENGTH_LONG);
					} else {
						insertRowIntoDatabase(createUserFeedbackContentValues());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {

					if (cursor != null) {
						cursor.close();
					}
				}

			} else {

				Helper.showCustomToast(getApplicationContext(),
						R.string.invalid_store_selection, Toast.LENGTH_LONG);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method creates new UserFeedback module based on user inputs
	 * 
	 */

	private UserFeedback createUserFeedbackDto() {

		UserFeedback userFeedback = new UserFeedback();
		userFeedback.setTeamID(mSelectedTeamID);
		userFeedback.setFeedbackCatID(mSelectedFeedbackCatID);
		userFeedback.setStoreId(storeId);

		userFeedback.setFeedbackTypeID(mSelectedFeedbackTypeID);
		userFeedback.setRemark(mStRemark);

		if (mStImagePath != null) {
			userFeedback.setImagePath(mStImagePath);

		}

		return userFeedback;

	}

	private ContentValues createUserFeedbackContentValues() {
		ContentValues contentValues = new ContentValues();

		contentValues.put(
				UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID,
				mSelectedTeamID);
		contentValues.put(
				UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID,
				mSelectedFeedbackCatID);
		contentValues.put(
				UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_TYPE_ID,
				mSelectedFeedbackTypeID);
		contentValues.put(
				UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_REMARK,
				mStRemark);
		contentValues.put(
				UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_STORE_ID,
				storeId);

		if (mStImagePath != null) {
			contentValues
					.put(UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_IMAGE_PATH,
							mStImagePath);

		}

		return contentValues;

	}

	/**
	 * This method delete all the the user added feedback from the local sqlite
	 * database as well remove data form UI
	 */

	public void onDeleteAllClick(View view) {

		Helper.showAlertDialog(FMSCreateFeedbackActivity.this,
				SSCAlertDialog.WARNING_TYPE, "Delete",
				"Are you sure you want to delete?", getString(R.string.yes),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();

						deleteAllRowFromDatabaseByStoreID(

						storeId);
					}
				}, true, getString(R.string.no),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});

	}

	public void onSpanShotClick(View view) {
		selectImageDialog();
	}

	public void onEditSpanShotClick(View view) {
		selectImageDialog();
	}

	public void onIvSnapshotClick(View view) {

		try {
			if (mStImagePath != null) {
				Intent intent = new Intent(this,
						ActivityFullScreenImageView.class);
				intent.putExtra("image_path", mStImagePath);
				intent.putExtra("called_from", "create_feedback");

				startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void selectImageDialog() {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setItems(R.array.camera_options,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						String fileName = System.currentTimeMillis()
								+ "feedback.jpg";
						File folder = new File(
								FileDirectory.DIRECTORY_FEEDBACK_IMAGES);

						if (!folder.exists()) {
							folder.mkdirs();
						}

						mFeedbackImagePath = new File(folder, fileName);
						if (!mFeedbackImagePath.exists()) {
							try {
								mFeedbackImagePath.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						Uri mImageUri = Uri.fromFile(mFeedbackImagePath);

						if (which == 0) {

							Intent cameraActivity = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							cameraActivity.putExtra(
									android.provider.MediaStore.EXTRA_OUTPUT,
									mImageUri);
							startActivityForResult(cameraActivity,
									REQUEST_CAMERA);
							dialog.dismiss();

						} else if (which == 1) {

							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(Intent.createChooser(intent,
									"Select Picture"), REQUEST_FILE);
							dialog.dismiss();
						}
					}
				});

		dialog.create();
		dialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == RESULT_OK) {

				switch (requestCode) {

				case REQUEST_CAMERA:

					try {

						Bitmap rotatedbitmap;

						mStImagePath = mFeedbackImagePath.getAbsolutePath();

						rotatedbitmap = Helper
								.creatBitmapFormImagepath(mFeedbackImagePath
										.getAbsolutePath());
						if (rotatedbitmap == null) {
							rotatedbitmap = BitmapFactory.decodeResource(
									getResources(),
									R.drawable.no_image_placeholder_fms);
						} else {
							setSnapshotBackground(rotatedbitmap);
						}

					} catch (Exception e) {

						Helper.showCustomToast(getApplicationContext(),
								R.string.unable_to_take_picture,
								Toast.LENGTH_LONG);

					}

					break;

				case REQUEST_FILE:

					if (null != data) {
						Uri selectedImage = data.getData();
						String[] filePathColumn = { MediaStore.Images.Media.DATA };

						Cursor cursor = getContentResolver()
								.query(selectedImage, filePathColumn, null,
										null, null);
						cursor.moveToFirst();

						int columnIndex = cursor
								.getColumnIndex(filePathColumn[0]);
						mStImagePath = cursor.getString(columnIndex);
						cursor.close();

						Bitmap bitmap = Helper
								.creatBitmapFormImagepath(mStImagePath);
						if (bitmap != null) {
							setSnapshotBackground(bitmap);
						}

					}

					break;

				case DEALER_SELECTION_REQUEST:

					try {
						storeId = data.getExtras().getInt(IntentKey.DEALER_ID);

					} catch (Exception e) {
						Helper.printStackTrace(e);
						storeId = 0;
					}

					// new GetExistedFeedbackAsyncTask().execute();

					mLoaderManager.initLoader(LOADER_ID_GET_USER_FEEDBACK,
							null, this);

					break;
				}
			} else if (resultCode == RESULT_CANCELED) {
				if (requestCode == DEALER_SELECTION_REQUEST) {
					storeId = 0;
					finish();
				}

			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setSnapshotBackground(Bitmap bm) {
		if (bm != null) {
			mIvSnapshot.setImageBitmap(bm);
			hideSnapShotButtonAndTextView();
			visibleEditSnapShotButton();
			setSnapshotImageViewClickable();
		}

	}

	private void hideSnapShotButtonAndTextView() {

		if (mBtSnapshot.getVisibility() == View.VISIBLE) {
			mBtSnapshot.setVisibility(View.INVISIBLE);
		}
		if (mTvAddSnapshot.getVisibility() == View.VISIBLE) {
			mTvAddSnapshot.setVisibility(View.INVISIBLE);
		}
		if (mTvSnapshot.getVisibility() == View.VISIBLE) {
			mTvSnapshot.setVisibility(View.INVISIBLE);
		}

	}

	private void showSnapShotButtonAndTextView() {
		if (mBtSnapshot.getVisibility() == View.INVISIBLE) {
			mBtSnapshot.setVisibility(View.VISIBLE);
		}
		if (mTvAddSnapshot.getVisibility() == View.INVISIBLE) {
			mTvAddSnapshot.setVisibility(View.VISIBLE);
		}
		if (mTvSnapshot.getVisibility() == View.INVISIBLE) {
			mTvSnapshot.setVisibility(View.VISIBLE);
		}

	}

	private void visibleEditSnapShotButton() {
		if (mBtEditSnapshot.getVisibility() == View.INVISIBLE) {
			mBtEditSnapshot.setVisibility(View.VISIBLE);
		}
	}

	private void hideEditSnapShotButton() {
		if (mBtEditSnapshot.getVisibility() == View.VISIBLE) {
			mBtEditSnapshot.setVisibility(View.INVISIBLE);
		}

	}

	private void setSnapshotImageViewClickable() {
		if (!mIvSnapshot.isClickable()) {
			mIvSnapshot.setClickable(true);
		}

	}

	private void setSnapshotImageViewUnClickable() {
		if (mIvSnapshot.isClickable()) {
			mIvSnapshot.setClickable(false);
		}

	}

	private void setWidgetsVisibility() {

		mSpinnerFeedbackCategory.setEnabled(false);
		mSpinnerFeedbackType.setEnabled(false);
		mBtAdd.setEnabled(false);
		disableSubmitButton();
		hideTableHeader();
	}

	private void hideTableHeader() {
		mTableHeader.setVisibility(View.GONE);
	}

	private void showTableHeader() {
		if (mTableHeader.getVisibility() == View.GONE) {
			mTableHeader.setVisibility(View.VISIBLE);
		}

	}

	private void enableSubmitButton() {
		if (!mBtSubmit.isEnabled()) {
			mBtSubmit.setEnabled(true);
		}

	}

	private void disableSubmitButton() {
		if (mBtSubmit.isEnabled()) {
			mBtSubmit.setEnabled(false);
		}

	}

	private void enableFeedbackCategorySpinner() {
		if (!mSpinnerFeedbackCategory.isEnabled()) {
			mSpinnerFeedbackCategory.setEnabled(true);
		}
	}

	private void enableFeedbackTypeSpinner() {
		if (!mSpinnerFeedbackType.isEnabled()) {
			mSpinnerFeedbackType.setEnabled(true);
		}
	}

	private void enableAddButton() {
		if (!mBtAdd.isEnabled()) {
			mBtAdd.setEnabled(true);
		}

	}

	private void disableAddButton() {
		if (mBtAdd.isEnabled()) {
			mBtAdd.setEnabled(false);
		}

	}

	private void disableFeedbackTypeSpinner() {
		if (mSpinnerFeedbackType.isEnabled()) {
			mSpinnerFeedbackType.setSelection(0);
			mSpinnerFeedbackType.setEnabled(false);
		}
	}

	private void insertRowIntoDatabase(final ContentValues contentValues) {

		Uri uri = getContentResolver().insert(
				ProviderContract.URI_USER_FEEDBACK, contentValues);
		// long rowID = ContentUris.parseId(uri);

		/*
		 * addSingleFeedbackRow(mStTeamName, mStFeedbackCatName,
		 * mStFeedbackType, rowID);
		 */

		enableSubmitButton();
		showTableHeader();

		/**
		 * reset widget for user to accept fresh value for new entry
		 */
		resetWidgets();

		// reset value
		mStImagePath = null;

	}

	private void getAllUserFeedback(final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				Cursor cursor = getContentResolver().query(
						ProviderContract.URI_USER_FEEDBACK_ALL, null, null,
						null, null);

				mListUserFeedbacks = DatabaseUtilMethods
						.getUserFeedbackAllFromCursor(cursor);
				
				
				if(!cursor.isClosed())
				{
					cursor.close();
				}

				Message msg = handler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putBoolean("data_available", true);
				msg.setData(bundle);
				handler.sendMessage(msg);

			}
		};

		thread.start();

	}

	private void deleteRowFromDatabase(final long row_id) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				int no_of_rows_deleted = getContentResolver().delete(
						ProviderContract.URI_USER_FEEDBACK,
						UserFeedbackMasterColumns._ID + "=?",
						new String[] { String.valueOf(row_id) });

			}
		};

		thread.start();

	}

	private void deleteAllRowFromDatabaseByStoreID(final int storeId) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				int no_of_rows_deleted = getContentResolver()
						.delete(ProviderContract.URI_USER_FEEDBACK,
								UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_STORE_ID
										+ "=?",
								new String[] { String.valueOf(storeId) });

			}
		};

		thread.start();

	}

	private void deleteAllRowFromDatabase() {

		Thread thread = new Thread() {
			@Override
			public void run() {

				int no_of_rows_deleted = getContentResolver().delete(
						ProviderContract.URI_USER_FEEDBACK, null, null);

			}
		};

		thread.start();

	}

	/**
	 * this method resets some widgets to defaulto
	 */

	private void resetWidgets() {
		mEtRemark.setText("");

		mIvSnapshot.setImageResource(R.drawable.snapshot_bg);

		disableAddButton();
		showSnapShotButtonAndTextView();
		hideEditSnapShotButton();
		setSnapshotImageViewUnClickable();

	}

	private void submitUserFeedbacks() {

		try {

			if (storeId == -1) {
				return;
			}

			new AsyncTask<Void, Void, JSONObject>() {

				private ProgressDialog progressDialog;

				@Override
				protected JSONObject doInBackground(Void... params) {
					try {

						JSONObject rootObject = new JSONObject();

						try {
							rootObject.put(WebConfig.WebParams.USER_ID, Long
									.valueOf(Helper.getStringValuefromPrefs(
											FMSCreateFeedbackActivity.this,
											SharedPreferencesKey.PREF_USERID)));
						} catch (NumberFormatException e) {
							Helper.printStackTrace(e);

							rootObject.put(WebConfig.WebParams.USER_ID, 0);

						}

						// rootObject.put(Constants.USERID, Long.valueOf(841));

						JSONArray jsonArray = new JSONArray();

						for (UserFeedback userFeedback : mListUserFeedbacks) {

							JSONObject singleFeedbackObject = new JSONObject();

							singleFeedbackObject.put("FeedbackTeamID",
									userFeedback.getTeamID());
							singleFeedbackObject.put("FeedbackCatID",
									userFeedback.getFeedbackCatID());
							singleFeedbackObject.put("FeedbackTypeID",
									userFeedback.getFeedbackTypeID());
							singleFeedbackObject.put("Remarks",
									userFeedback.getRemark());
							singleFeedbackObject.put("storeID",
									userFeedback.getStoreId());

							if (userFeedback.getImagePath() != null) {

								String base64String = null;

								base64String = Helper
										.getBase64StringFormBitmap(Helper
												.creatBitmapFormImagepath(userFeedback
														.getImagePath()));

								if (base64String != null) {
									singleFeedbackObject.put("ImageBytes",
											base64String);
								}

							}

							jsonArray.put(singleFeedbackObject);

						}

						rootObject.put("FeedBacks", jsonArray);

						return rootObject;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				protected void onPreExecute() {

					try {
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
							progressDialog = SSCProgressDialog
									.ctor(FMSCreateFeedbackActivity.this);
						} else {
							progressDialog = new ProgressDialog(
									FMSCreateFeedbackActivity.this);

							progressDialog.setProgress(0);
							progressDialog.setMax(100);
							progressDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setMessage("Loading");
							progressDialog.setCancelable(false);

						}
						progressDialog.show();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				protected void onPostExecute(JSONObject jsonObject) {
					try {
						progressDialog.dismiss();
						if (jsonObject != null) {

							/*
							 * PostDataToNetwork pdn = new PostDataToNetwork(
							 * FMSCreateFeedbackActivity.this, "Loading", new
							 * GetDataCallBack() {
							 * 
							 * @Override public void processResponse( Object
							 * result) {
							 * 
							 * if (result != null) {
							 * 
							 * parseResponse(result); }
							 * 
							 * } });
							 * 
							 * pdn.setConfig(getString(R.string.url),
							 * "SubmitFeedbacks");
							 * 
							 * pdn.execute(jsonObject);
							 */

							VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
									FMSCreateFeedbackActivity.this,
									getString(R.string.loadingmessage),
									new VolleyGetDataCallBack() {

										@Override
										public void processResponse(
												Object result) {
											if (result != null) {

												parseResponse(result);
											}

										}

										@Override
										public void onError(VolleyError error) {
											// TODO Auto-generated method stub

										}
									});

							  pdn.setRequestData(jsonObject);
							  pdn.setConfig(getString(R.string.url),
									 WebMethod.SUBMIT_FEEDBACKS);
							  pdn.callWebService();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}.execute();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseResponse(Object result) {

		try {
			JSONObject jsonObject = new JSONObject(result.toString());
			if (jsonObject.getBoolean("IsSuccess")) {
				// deleteAllRowFromDatabase(new DeleteAllRowHandler(this));
				deleteAllRowFromDatabase();
				Helper.showCustomToast(getApplicationContext(),
						R.string.feedback_submitted_successfully,
						Toast.LENGTH_LONG);

				finish();

			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	private void setSampleImage(final String sampleImageName) {

		if (sampleImageName != null
				&& !sampleImageName.equalsIgnoreCase("null")) {

			 ImageLoader.getInstance(getApplicationContext()).displayImage(
						getResources().getString(R.string.url_file_processor_consolebase)+"id="+sampleImageName+"&type=FMSIcon"
								, mIvSampleImage);
				

			mIvSampleImage.setClickable(true);
			mIvSampleImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(v.getContext(),
							ActivityFullScreenImageView.class);
					intent.putExtra("image_path", sampleImageName);
					intent.putExtra("called_from", "create_feedback_hint_image");
					startActivity(intent);

				}
			});

		} else {

			mIvSampleImage.setClickable(false);
			int stubid = R.drawable.no_image_placeholder_fms;
			mIvSampleImage.setImageResource(stubid);

		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {

		Loader loader = null;
		switch (id) {
		case LOADER_ID_GET_TEAMS:

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_FEEDBACK_TEAMS, null, null, null, null);

			break;

		case LOADER_ID_GET_CATEGORY:

			String selection = FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID
					+ "=?";
			String whereArgs[] = { mSelectedTeamID + "" };

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_FEEDBACK_CATEGORY, null, selection,
					whereArgs, null);

			break;

		case LOADER_ID_GET_TYPE:
			
			String selectionCategory = FeedbackTypeMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID
					+ "=?";
			String whereArgsCategory[] = { mSelectedFeedbackCatID + "" };

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_FEEDBACK_TYPE, null,
					selectionCategory, whereArgsCategory, null);

			break;

		case LOADER_ID_GET_USER_FEEDBACK:

			if (storeId != -1) {

				loader = new CursorLoader(getApplicationContext(),
						ProviderContract.URI_USER_FEEDBACK, null, null,
						new String[] { String.valueOf(storeId) }, null);
			}

			break;

		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		switch (loader.getId()) {
		case LOADER_ID_GET_TEAMS:

			if (cursor != null && cursor.moveToFirst()) {

				List<TeamMasterModel> listTeam = DatabaseUtilMethods
						.getFMSTeamsFromCursor(cursor);
				if (listTeam != null) {
					ArrayAdapter<TeamMasterModel> teamAdapter = new CustomSpinnerTeamAdapter(
							FMSCreateFeedbackActivity.this, listTeam);
					mSpinnerTeam.setAdapter(new NothingSelectionSpinnerAdapter(
							teamAdapter,
							R.layout.team_spinner_nothing_selected,
							FMSCreateFeedbackActivity.this));

				}

			}

			break;

		case LOADER_ID_GET_CATEGORY:

			// clear data if new data availble or not
			mListFeedbackCategory.clear();

			if (cursor != null && cursor.moveToFirst()) {

				List<FeedbackCategoryMasterModel> listCategory = DatabaseUtilMethods
						.getFMSCategoriesFromCursor(cursor);
				if (listCategory != null) {
					mListFeedbackCategory.addAll(listCategory);
					setFeedbackCategoryData();
				}

			}

			break;

		case LOADER_ID_GET_TYPE:

			mListFeedbackType.clear();
			if (cursor != null && cursor.moveToFirst()) {

				List<FeedbackTypeMasterModel> listType = DatabaseUtilMethods
						.getFMSTypeFromCursor(cursor);
				if (listType != null) {
					mListFeedbackType.addAll(listType);
					setFeedbackTypeData();
				}

			}
			break;

		case LOADER_ID_GET_USER_FEEDBACK:
			if (cursor != null && cursor.moveToFirst()) {

				if (mllTableContainer != null
						&& mllTableContainer.getChildCount() > 0) {
					mllTableContainer.removeAllViews();

				}

				List<UserFeedbackValues> userFeedbacks = DatabaseUtilMethods
						.getUserFeedbackStoreWiseFromCursor(cursor);
				if (userFeedbacks != null && !userFeedbacks.isEmpty()) {
					for (UserFeedbackValues userFeedbackValues : userFeedbacks) {

						addSingleFeedbackRow(userFeedbackValues.getTeamName(),
								userFeedbackValues.getFeedbackCatName(),
								userFeedbackValues.getFeedbackTypeName(),
								userFeedbackValues.getRowId());

					}
				}

			} else if (cursor!=null&&cursor.getCount() == 0) {
				disableSubmitButton();
				hideTableHeader();
				resetWidgets();

				if (mllTableContainer != null
						&& mllTableContainer.getChildCount() > 0) {
					mllTableContainer.removeAllViews();
				}

			}

			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}