package com.samsung.ssc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseConstants;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.UserProfileTableColumns;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.io.PostDataToNetworkImage;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.io.VolleyPostDataToNetworkImage;
import com.samsung.ssc.io.VolleySingleton;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.CommonUtil;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.ImageLoader;

public class UserProfile extends BaseActivity implements View.OnClickListener,
		LoaderCallbacks<Cursor> {

	private EditText etFirstName, etLastName, etEmailID, etAltEmailID,
			etAltMobileNumber, etMobileNumber, etAddress, etPinNumber;
	private static final int CAMERA_PIC_USER_PROFILE = 2399;
	private ImageView ivProfilePic;
	private Button btCamera;
	private RelativeLayout rl_enlargeImage1;
	private File mUserProfileImagePath;
	private Bitmap userPhoto;
	private Button btnUpdate;
	private Button btnCancel;
	private TextView tvMobile1;
	private TextView tvAltMobile;

	private ContentResolver mContentResolver;
	private final int LOADER_ID = 1;

	// private UserProfileContentObserver mContentObserver;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.userprofile1);
		mContentResolver = getApplicationContext().getContentResolver();
		// mContentObserver=new UserProfileContentObserver(new Handler());

		setUpView();
	}

	/**
	 * 
	 */
	private void setUpView() {

		etFirstName = (EditText) findViewById(R.id.empFirstName);
		etLastName = (EditText) findViewById(R.id.empLastName);
		etEmailID = (EditText) findViewById(R.id.empMail1);
		etAltEmailID = (EditText) findViewById(R.id.empEmail2);
		etMobileNumber = (EditText) findViewById(R.id.empMobileCalling);
		etAltMobileNumber = (EditText) findViewById(R.id.empMobileSd);
		etAddress = (EditText) findViewById(R.id.empAddress);
		etPinNumber = (EditText) findViewById(R.id.pincode);

		btCamera = (Button) findViewById(R.id.bt_camera_userProfileFragmentBasic);
		ivProfilePic = (ImageView) findViewById(R.id.iv_outletimage_userProfileFragmentBasic);
		rl_enlargeImage1 = (RelativeLayout) findViewById(R.id.rl_enlarge_userProfile1);

		btnUpdate = (Button) findViewById(R.id.updateBtn);
		btnCancel = (Button) findViewById(R.id.cancelBtn);

		tvMobile1 = (TextView) findViewById(R.id.tvUserProfileMobileNo);
		tvMobile1.setText(Html.fromHtml("<u>" + " Mobile" + "</u>"));
		tvAltMobile = (TextView) findViewById(R.id.tvAltUserMobileNumber);
		tvAltMobile.setText(Html.fromHtml("<u>" + " Alt Mobile" + "</u>"));

		btnUpdate.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		btCamera.setOnClickListener(this);
		rl_enlargeImage1.setOnClickListener(this);
		tvMobile1.setOnClickListener(this);
		tvAltMobile.setOnClickListener(this);

		getSupportLoaderManager().initLoader(LOADER_ID, null, this);

		// getUserProfile();

	}

	private void getUserProfile() {

		Cursor cursor = mContentResolver.query(
				ProviderContract.USER_PROFILE_URI, null, null, null, null);

		if (cursor != null && cursor.getCount() != 0) {

			JSONObject jsonObject = DatabaseUtilMethods
					.getUserProfileFromCursor(cursor);
			cursor.close();
			if (jsonObject != null) {
				fillView(jsonObject);
			}
		} else {

			if (isOnline()) {
				// initProfile();

				initProfilebyVolly();

			} else {
				Helper.showErrorAlertDialog(UserProfile.this,
						getStringFromResource(R.string.error1),
						getStringFromResource(R.string.error2),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface d, int arg1) {
								d.dismiss();
								finish();
							}
						});

			}
		}

	}

	@Override
	public boolean validation() {

		boolean flag = true;

		if (etFirstName.getText().toString().trim().length() == 0) {
			etFirstName
					.setError(getStringFromResource(R.string.enter_name_msg));
			flag = false;
		}

		if (etLastName.getText().toString().trim().length() == 0) {
			etLastName
					.setError(getStringFromResource(R.string.enter_last_name_msg));
			flag = false;
		}

		if (etEmailID.getText().toString().trim().length() == 0) {
			etEmailID.setError(getStringFromResource(R.string.enter_email_msg));
			flag = false;
		}
		if (etEmailID.length() > 0) {
			if (!Helper.checkEmail(etEmailID.getText().toString().trim())) {
				etEmailID
						.setError(getStringFromResource(R.string.invalidemail));
				flag = false;
			}
		}

		if (etMobileNumber.length() < 10) {
			etMobileNumber
					.setError(getStringFromResource(R.string.enter_mobile_msg));
			flag = false;
		}

		if (etAddress.getText().toString().trim().length() == 0) {
			etAddress
					.setError(getStringFromResource(R.string.enter_address_msg));
			flag = false;
		}

		if (etPinNumber.getText().toString().trim().length() < 6) {
			etPinNumber
					.setError(getStringFromResource(R.string.enter_pincode_msg));
			flag = false;
		}

		return flag;
	}

	/*private void initProfile() {
		PostDataToNetwork pdn = new PostDataToNetwork(UserProfile.this,
				getStringFromResource(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						if (result != null) {

							try {

								JSONObject jsonObject = new JSONObject(result
										.toString());
								if (jsonObject.getBoolean("IsSuccess")) {

									JSONObject itemJsonObject = jsonObject
											.getJSONObject("SingleResult");

									ContentValues contentValues = getContetnValue(itemJsonObject);
									mContentResolver.insert(
											ProviderContract.USER_PROFILE_URI,
											contentValues);

								} else {

								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							Helper.showCustomToast(UserProfile.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}
					}

				});
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(UserProfile.this,
							SharedPreferencesKey.PREF_USERID)));

			printLog("result_userprofile_parameter", jsonobj.toString());
		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getStringFromResource(R.string.url), "DisplayUserProfile");

		pdn.execute(jsonobj);

	}*/

	private void initProfilebyVolly() {


		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(UserProfile.this,
							SharedPreferencesKey.PREF_USERID)));

			printLog("result_userprofile_parameter", jsonobj.toString());

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

		
		
		VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
				UserProfile.this,
				getStringFromResource(R.string.loadingmessage),
				new VolleyGetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						if (result != null) {

							try {

								JSONObject jsonObject=new JSONObject(result.toString());
								
								if (jsonObject.getBoolean("IsSuccess")) {

									JSONObject itemJsonObject = jsonObject
											.getJSONObject("SingleResult");

									ContentValues contentValues = getContetnValue(itemJsonObject);
									mContentResolver.insert(
											ProviderContract.USER_PROFILE_URI,
											contentValues);

								} else {

								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {

							Helper.showCustomToast(UserProfile.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}

						
						
						
						
						
						

					}
					
					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						
					}
					
				});

		
		pdn.setConfig(getStringFromResource(R.string.url),
				"DisplayUserProfile");
		pdn.setRequestData(jsonobj);
		pdn.callWebService();
		
		
		

	}

	private ContentValues getContetnValue(JSONObject jsonObject) {

		ContentValues contentValues = new ContentValues();

		try {
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_ADDRESS,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_ADDRESS));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_ALTERNATE_EMAIL_ID,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_ALTERNATE_EMAIL_ID));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.EMAIL_ID,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.EMAIL_ID));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_EMPLOYEE_CODE,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_EMPLOYEE_CODE));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_LAST_NAME,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_LAST_NAME));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_IS_OFFLINE_PROFILE,
							jsonObject
									.getBoolean(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_IS_OFFLINE_PROFILE) ? 1
									: 0);
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_IS_ROAMING_PRIFLE,
							jsonObject
									.getBoolean(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_IS_ROAMING_PRIFLE) ? 1
									: 0);
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_CALLING,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_CALLING));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_SD,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_SD));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_PIN_CODE,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_PIN_CODE));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_PICTURE_FILE_NAME,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_PICTURE_FILE_NAME));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_USER_CODE,
							jsonObject
									.getString(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_USER_CODE));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_ID,
							jsonObject
									.getInt(DatabaseConstants.UserProfileTableColumns.KEY_USER_ID));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_ROLE_ID,
							jsonObject
									.getInt(DatabaseConstants.UserProfileTableColumns.KEY_USER_ROLE_ID));
			contentValues
					.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_USER_ROLE_ID_ACTUAL,
							jsonObject
									.getInt(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_USER_ROLE_ID_ACTUAL));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return contentValues;
	}

	/**
	 * @param result
	 */
	private void fillView(JSONObject jsonObject) {

		try {
			if (!Helper.isNullOrEmpty(jsonObject.getString("FirstName"))) {
				etFirstName.setText(jsonObject.getString("FirstName"));
			}
			if (!Helper.isNullOrEmpty(jsonObject.getString("LastName"))) {
				etLastName.setText(jsonObject.getString("LastName"));
			}

			if (!Helper.isNullOrEmpty(jsonObject.getString("EmailID"))) {
				etEmailID.setText(jsonObject.getString("EmailID"));
			}
			if (!Helper.isNullOrEmpty(jsonObject.getString("AlternateEmailID"))) {
				etAltEmailID.setText(jsonObject.getString("AlternateEmailID"));
			}

			if (!Helper.isNullOrEmpty(jsonObject.getString("Mobile_Calling"))) {
				etMobileNumber.setText(jsonObject.getString("Mobile_Calling"));

			}
			if (!Helper.isNullOrEmpty(jsonObject.getString("Mobile_SD"))) {
				etAltMobileNumber.setText(jsonObject.getString("Mobile_SD"));

			}

			if (!Helper.isNullOrEmpty(jsonObject.getString("Address"))) {
				etAddress.setText(jsonObject.getString("Address"));

			}
			if (!Helper.isNullOrEmpty(jsonObject.getString("Pincode"))) {
				etPinNumber.setText(jsonObject.getString("Pincode"));

			}
			if (!Helper.isNullOrEmpty(jsonObject
					.getString("ProfilePictureFileName"))) {
				ImageLoader imgLoader = ImageLoader
						.getInstance(UserProfile.this);
				imgLoader.displayImageWithTag(
						jsonObject.getString("ProfilePictureFileName"),
						ivProfilePic, R.drawable.img_user);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
	}

	private void fillView(Cursor cursor) {
		try {
			cursor.moveToFirst();

			String address = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_ADDRESS));

			String altEmailID = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_ALTERNATE_EMAIL_ID));

			String emailID = cursor.getString(cursor
					.getColumnIndex(UserProfileTableColumns.EMAIL_ID));

			String empCode = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_EMPLOYEE_CODE));

			String firstName = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME));

			String lastName = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_LAST_NAME));

			boolean isOfflineProfile = cursor
					.getInt(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_IS_OFFLINE_PROFILE)) == 1 ? true
					: false;

			boolean isRoamingProfile = cursor
					.getInt(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_IS_ROAMING_PRIFLE)) == 1 ? true
					: false;

			String altMobileNumber = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_SD));

			String mobileNumber = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_CALLING));

			String userRoleIDActual = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_USER_ROLE_ID_ACTUAL));

			String pictureFileName = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_PICTURE_FILE_NAME));

			String pinCode = cursor
					.getString(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_PIN_CODE));

			int userCode = cursor
					.getInt(cursor
							.getColumnIndex(UserProfileTableColumns.KEY_USER_PROFILE_USER_CODE));

			int userID = cursor.getInt(cursor
					.getColumnIndex(UserProfileTableColumns.KEY_USER_ID));
			int userRoleID = cursor.getInt(cursor
					.getColumnIndex(UserProfileTableColumns.KEY_USER_ROLE_ID));

			if (!Helper.isNullOrEmpty(firstName)) {
				etFirstName.setText(firstName);
			}
			if (!Helper.isNullOrEmpty(lastName)) {
				etLastName.setText(lastName);
			}

			if (!Helper.isNullOrEmpty(emailID)) {
				etEmailID.setText(emailID);
			}
			if (!Helper.isNullOrEmpty(altEmailID)) {
				etAltEmailID.setText(altEmailID);
			}

			if (!Helper.isNullOrEmpty(mobileNumber)) {
				etMobileNumber.setText(mobileNumber);

			}
			if (!Helper.isNullOrEmpty(altMobileNumber)) {
				etAltMobileNumber.setText(altMobileNumber);

			}

			if (!Helper.isNullOrEmpty(address)) {
				etAddress.setText(address);

			}
			if (!Helper.isNullOrEmpty(pinCode)) {
				etPinNumber.setText(pinCode);

			}
			if (!Helper.isNullOrEmpty(pictureFileName)) {
				ImageLoader imgLoader = ImageLoader
						.getInstance(UserProfile.this);
				imgLoader.displayImageWithTag(pictureFileName, ivProfilePic,
						R.drawable.img_user);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateProfile(final String picturefilename) {

		PostDataToNetwork pdn = new PostDataToNetwork(UserProfile.this,
				getStringFromResource(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						if (result != null) {
							try {

								JSONObject jsonObject = new JSONObject(result
										.toString());
								boolean isSuccess = jsonObject
										.getBoolean("IsSuccess");
								if (isSuccess) {

									Helper.showCustomToast(UserProfile.this,
											jsonObject.getString("Message"),
											Toast.LENGTH_LONG);

									updateLocalDatabase(picturefilename);

									finish();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {
							Toast.makeText(
									UserProfile.this,
									getStringFromResource(R.string.networkserverbusy),
									Toast.LENGTH_LONG).show();
						}

					}

				});

		JSONObject userProfileObj = new JSONObject();

		try {

			JSONObject jsonobj = new JSONObject();
			jsonobj.put(WebConfig.WebParams.USER_ID_CAPS, Long.valueOf(Helper
					.getStringValuefromPrefs(UserProfile.this,
							SharedPreferencesKey.PREF_USERID)));
			jsonobj.put(WebConfig.WebParams.FIRST_NAME, etFirstName.getText()
					.toString().trim());
			jsonobj.put(WebConfig.WebParams.LAST_NAME, etLastName.getText()
					.toString().trim());
			jsonobj.put(WebConfig.WebParams.MOBILE_CALLING, etMobileNumber
					.getText().toString().trim());
			jsonobj.put(WebConfig.WebParams.Mobile_SD, etAltMobileNumber
					.getText().toString().trim());
			jsonobj.put(WebConfig.WebParams.EMAIL_ID, etEmailID.getText()
					.toString().trim());
			jsonobj.put(WebConfig.WebParams.ALTERNATE_EMAIL_ID, etAltEmailID
					.getText().toString().trim());
			jsonobj.put(WebConfig.WebParams.ADDRESS, etAddress.getText()
					.toString().trim());
			jsonobj.put(WebConfig.WebParams.PINCODE, etPinNumber.getText());
			if (!picturefilename.equals("")) {
				jsonobj.put(WebConfig.WebParams.PROFILE_PICTURE_FILE_NAME,
						picturefilename);
			}
			userProfileObj.put(WebConfig.WebParams.USER_PROFILE, jsonobj);

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getStringFromResource(R.string.url),
				WebConfig.WebMethod.UPDATE_USER_PROFILE);
		pdn.execute(userProfileObj);

	}

	private void updateProfileVolley(final String picturefilename) {

		VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
				UserProfile.this,
				getStringFromResource(R.string.loadingmessage),
				new VolleyGetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						if (result != null) {
							try {

								JSONObject jsonObject = new JSONObject(result
										.toString());
								boolean isSuccess = jsonObject
										.getBoolean("IsSuccess");
								if (isSuccess) {

									Helper.showCustomToast(UserProfile.this,
											jsonObject.getString("Message"),
											Toast.LENGTH_LONG);

									updateLocalDatabase(picturefilename);

									finish();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {
							Toast.makeText(
									UserProfile.this,
									getStringFromResource(R.string.networkserverbusy),
									Toast.LENGTH_LONG).show();
						}

					}

					@Override
					public void onError(VolleyError error) {
					
						
					}

				});

		JSONObject userProfileObj = new JSONObject();

		try {

			JSONObject jsonobj = new JSONObject();
			jsonobj.put("UserID", Long.valueOf(Helper.getStringValuefromPrefs(
					UserProfile.this, SharedPreferencesKey.PREF_USERID)));
			jsonobj.put("FirstName", etFirstName.getText().toString().trim());
			jsonobj.put("LastName", etLastName.getText().toString().trim());
			jsonobj.put("Mobile_Calling", etMobileNumber.getText().toString()
					.trim());
			jsonobj.put("Mobile_SD", etAltMobileNumber.getText().toString()
					.trim());
			jsonobj.put("EmailID", etEmailID.getText().toString().trim());
			jsonobj.put("AlternateEmailID", etAltEmailID.getText().toString()
					.trim());
			jsonobj.put("Address", etAddress.getText().toString().trim());
			jsonobj.put("Pincode", etPinNumber.getText());
			if (!picturefilename.equals("")) {
				jsonobj.put("ProfilePictureFileName", picturefilename);
			}
			userProfileObj.put("userProfile", jsonobj);

			printLog("UpdateUserProfile", userProfileObj.toString());

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getStringFromResource(R.string.url), "UpdateUserProfile");
		pdn.setRequestData(userProfileObj);
		pdn.callWebService();

	}

	protected void updateLocalDatabase(String pictureFileName) {
		try {

			ContentValues contentValues = new ContentValues();

			try {
				contentValues
						.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_ADDRESS,
								etAddress.getText().toString().trim());
				contentValues
						.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_ALTERNATE_EMAIL_ID,
								etAltEmailID.getText().toString().trim());
				contentValues.put(
						DatabaseConstants.UserProfileTableColumns.EMAIL_ID,
						etEmailID.getText().toString().trim());

				contentValues
						.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_FIRST_NAME,
								etFirstName.getText().toString().trim());
				contentValues
						.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_LAST_NAME,
								etLastName.getText().toString().trim());

				contentValues
						.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_CALLING,
								etMobileNumber.getText().toString().trim());
				contentValues
						.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_MOBILE_SD,
								etAltMobileNumber.getText().toString().trim());
				contentValues
						.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_PIN_CODE,
								etPinNumber.getText().toString());

				if (!pictureFileName.equalsIgnoreCase("")) {
					String imagePath = getString(R.string.url_file_processor_servicebase)
							+ "id=" + pictureFileName + "&type=User";

					contentValues.put(DatabaseConstants.UserProfileTableColumns.KEY_USER_PROFILE_PICTURE_FILE_NAME,
							imagePath);
				}

				String userID = Helper.getStringValuefromPrefs(
						UserProfile.this, SharedPreferencesKey.PREF_USERID);

				mContentResolver.update(ProviderContract.USER_PROFILE_URI,
						contentValues,
						DatabaseConstants.UserProfileTableColumns.KEY_USER_ID
								+ "=?", new String[] { userID });

			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == Activity.RESULT_OK) {

				switch (requestCode) {

				case CAMERA_PIC_USER_PROFILE:

					try {
						userPhoto = Helper
								.creatBitmapFormImagepath(mUserProfileImagePath
										.getAbsolutePath());
						if (userPhoto != null) {
							ivProfilePic.setImageBitmap(userPhoto);
							ivProfilePic.setTag(userPhoto);

						}

					} catch (Exception e) {
						Toast.makeText(UserProfile.this, R.string.error15,
								Toast.LENGTH_LONG).show();
					}

					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void senduserImage() {

		PostDataToNetworkImage pdn = new PostDataToNetworkImage(
				UserProfile.this,
				UserProfile.this.getString(R.string.loadingmessage),
				new GetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {
							Helper.printLog("result  image", result + "");
							ResponseDto obj = new FetchingdataParser(
									UserProfile.this).getResponseResult(result
									.toString());
							if (obj.isSuccess()) {
								if (obj.getSingleResult().toString() != null) {

									updateProfile(obj.getSingleResult()
											.toString());

								} else {
									Helper.showErrorAlertDialog(
											UserProfile.this,
											getResources().getString(
													R.string.error3), obj
													.getMessage(),
											new OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// method stub
													dialog.dismiss();
													finish();

												}
											});

								}

							} else {

								Helper.showAlertDialog(getApplicationContext(),
										SSCAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										obj.getMessage(), null, null, false,
										null, null);

							}

						} else {

							Helper.showCustomToast(UserProfile.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}
					}
				});
		JSONObject jobj = new JSONObject();

		try {

			jobj.put(WebConfig.WebParams.IMAGE, Helper.getByteArray(userPhoto));

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getResources().getString(R.string.url), "UploadUserImage");
		// pdn.setConfig("http://192.168.11.199/SmartDostService/SmartDost.svc/",
		// "UploadStoreImage");
		pdn.execute(Helper.getByteArray(userPhoto));

	}

	private void sendUserImageVolley() {

		VolleyPostDataToNetworkImage pdn = new VolleyPostDataToNetworkImage(
				UserProfile.this,
				UserProfile.this.getString(R.string.loadingmessage),
				new VolleyGetDataCallBack() {
					@Override
					public void processResponse(Object result) {
						if (result != null) {

							// JSONObject block start
							if (result instanceof JSONObject) {

								Helper.printLog("result  image", result + "");
								ResponseDto obj = new FetchingdataParser(
										UserProfile.this)
										.getResponseResult(result.toString());
								if (obj.isSuccess()) {
									if (obj.getSingleResult().toString() != null) {

										updateProfileVolley(obj
												.getSingleResult().toString());
									} else {
										Helper.showErrorAlertDialog(
												UserProfile.this,
												getResources().getString(
														R.string.error3),
												obj.getMessage(),
												new OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// method stub
														dialog.dismiss();
														finish();

													}
												});

									}

								} else {

									Helper.showAlertDialog(
											getApplicationContext(),
											SSCAlertDialog.ERROR_TYPE,
											getString(R.string.error3),
											obj.getMessage(), null, null,
											false, null, null);

								}

							}
							// JSONObject block end

							// VolleyError block starts
							else if (result instanceof VolleyError) {

								Helper.showCustomToast(UserProfile.this,
										R.string.networkserverbusy,
										Toast.LENGTH_LONG);

							}
							// VolleyError block ends

						} else {

							Helper.showCustomToast(UserProfile.this,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}
					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						
					}
				});
		JSONObject jobj = new JSONObject();

		try {

			jobj.put(WebConfig.WebParams.IMAGE, Helper.getByteArray(userPhoto));

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}
		pdn.setConfig(getResources().getString(R.string.url), "UploadUserImage");
		// pdn.setConfig("http://192.168.11.199/SmartDostService/SmartDost.svc/",
		// "UploadStoreImage");
		pdn.setRequestData(Helper.getByteArray(userPhoto));
		pdn.callWebService();

	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_camera_userProfileFragmentBasic:
			onCamaraClick();
			break;

		case R.id.rl_enlarge_userProfile1:
			makeEnlargement();
			break;

		case R.id.updateBtn:

			if (etFirstName.isEnabled()) {
				onUpdateUserProfileClick();

			} else {
				setEditable();
			}

			break;

		case R.id.cancelBtn:
			UserProfile.this.finish();
			break;

		case R.id.tvUserProfileMobileNo:

			if (etMobileNumber.getText().toString().equals("")) {
				Toast.makeText(UserProfile.this, "No contact number",
						Toast.LENGTH_LONG).show();
			} else {
				CommonUtil.openDialer(UserProfile.this, etMobileNumber
						.getText().toString());
			}
			break;

		case R.id.tvAltUserMobileNumber:

			if (etAltMobileNumber.getText().toString().equals("")) {
				Toast.makeText(UserProfile.this, "No contact number",
						Toast.LENGTH_LONG).show();
			} else {
				CommonUtil.openDialer(UserProfile.this, etAltMobileNumber
						.getText().toString());
			}
			break;
		}

	}

	private void onUpdateUserProfileClick() {

		if (isOnline()) {
			if (validation()) {
				if (userPhoto != null) {
					sendUserImageVolley();
				} else {
					updateProfile("");
				}
			}
		} else {

			Helper.showAlertDialog(getApplicationContext(),
					SSCAlertDialog.ERROR_TYPE,
					getString(R.string.error1), getString(R.string.error2),
					null, null, false, null, null);

		}

	}

	private void setEditable() {

		if (!etFirstName.isEnabled()) {

			etFirstName.setEnabled(true);
			etLastName.setEnabled(true);
			etEmailID.setEnabled(true);
			etAltEmailID.setEnabled(true);
			etMobileNumber.setEnabled(true);
			etAltMobileNumber.setEnabled(true);
			etAddress.setEnabled(true);
			etPinNumber.setEnabled(true);

			btnUpdate.setText("Update");

		} else {

			etFirstName.setEnabled(false);
			etLastName.setEnabled(false);
			etEmailID.setEnabled(false);
			etAltEmailID.setEnabled(false);
			etMobileNumber.setEnabled(false);
			etAltMobileNumber.setEnabled(false);
			etAddress.setEnabled(false);
			etPinNumber.setEnabled(false);

			btnUpdate.setText("Edit");
		}
	}

	private void makeEnlargement() {

		Bitmap profilePic = (Bitmap) ivProfilePic.getTag();

		if (profilePic != null) {
			Intent intent = new Intent(UserProfile.this,
					FullScreenImageActivity.class);

			// Convert to byte array
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			profilePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			intent.putExtra("image", byteArray);

			startActivity(intent);
			UserProfile.this.overridePendingTransition(R.anim.from_middle,
					R.anim.to_middle);

		} else {
			Toast.makeText(UserProfile.this, "No profile Image",
					Toast.LENGTH_LONG).show();

		}

	}

	private void onCamaraClick() {

		if (!etFirstName.isEnabled()) {
			Toast.makeText(UserProfile.this, R.string.plase_click_on_edit,
					Toast.LENGTH_LONG).show();
			return;
		}

		try {
			String fileName = "user_profile.jpg";
			File folder = new File(FileDirectory.DIRECTORY_GEOTAG_IMAGE);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			mUserProfileImagePath = new File(folder, fileName);
			if (!mUserProfileImagePath.exists()) {
				mUserProfileImagePath.createNewFile();
			}

			Uri mImageUri = Uri.fromFile(mUserProfileImagePath);

			Intent cameraActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraActivity.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageUri);
			startActivityForResult(cameraActivity, CAMERA_PIC_USER_PROFILE);

		} catch (IOException e) {

			Toast.makeText(UserProfile.this, R.string.error15,
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		return new CursorLoader(getApplicationContext(),
				ProviderContract.USER_PROFILE_URI, null, null, null, null);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		if (data != null && data.getCount() > 0) {

			/*JSONObject jsonObject = DatabaseUtilMethods
					.getUserProfileFromCursor(data);
			if (jsonObject != null) {
				fillView(jsonObject);
			}*/
			
			fillView(data);
		} else {
			if (isOnline()) {
				// initProfile();

				initProfilebyVolly();

			} else {
				Helper.showErrorAlertDialog(UserProfile.this,
						getStringFromResource(R.string.error1),
						getStringFromResource(R.string.error2),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface d, int arg1) {
								d.dismiss();
								finish();
							}
						});

			}
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

}
