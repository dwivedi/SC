package com.samsung.ssc.activitymodule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.samsung.ssc.FullScreenImageActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog.OnSDAlertDialogClickListener;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.constants.WebConfig.WebParams;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.dto.StoreBasicModel;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.io.PostDataToNetworkImage;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.io.VolleyPostDataToNetworkImage;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.CommonUtil;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.ImageLoader;

public class OutletProfileFragmentBasic1 extends Fragment implements
		OnClickListener {

	private OutletProfile mParentActivity;
	private View _rootView;
	private static final int CAMERA_PIC_OUTLET_PROFILE = 2399;
	private ViewHolder mHolder;

	private File mOutletProfileImagePath;
	private String profileImage;

	private Bitmap profileImageBitmap;
	private Bitmap profilePic;
	private StoreBasicModel mStoreBasicData;
	private boolean mIsCancelButtonNeeded = false;

	private final int MENU_OPTION_SAVE = 1;
	private final int MENU_OPTION_EDIT = 0;
	private boolean isEdit = false;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (_rootView == null) {
			_rootView = inflater.inflate(
					R.layout.outlet_profile_fragment_basic, container, false);

			setUpBasicView();

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		return _rootView;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// get the instance of the parent activity
		mParentActivity = (OutletProfile) getActivity();

	}

	@SuppressWarnings("deprecation")
	private void setUpBasicView() {

		Bundle bundle = getActivity().getIntent().getExtras();

		mStoreBasicData = bundle.getParcelable(IntentKey.KEY_STORE_BASIC);

		mIsCancelButtonNeeded = bundle
				.getBoolean(IntentKey.KEY_CANCEL_BUTTON_NEEDED);

		mHolder = new ViewHolder(_rootView);

	

		if (mStoreBasicData != null) {
			
			  showGeneralData();
			
			if (mStoreBasicData.getChannelType().equalsIgnoreCase("RR") || mStoreBasicData.getChannelType().equalsIgnoreCase("MR")) {
				
				//mHolder.etContactPerson.setVisibility(View.GONE);
				//mHolder.etContactNumber.setVisibility(View.GONE);
				//mHolder.etTinNumber.setVisibility(View.GONE);
				
				mHolder.llContactPerson.setVisibility(View.GONE);
				mHolder.llContactMobile.setVisibility(View.GONE);
				mHolder.llTinNumber.setVisibility(View.GONE);
				 
			}else if (mStoreBasicData.getChannelType().equalsIgnoreCase("RS") || mStoreBasicData.getChannelType().equalsIgnoreCase("BS") ) {
				
				//mHolder.etStoreManger.setVisibility(View.GONE);
				//mHolder.etSMMobile.setVisibility(View.GONE);
				//mHolder.etSMEmail.setVisibility(View.GONE);
				//mHolder.etAlternateEmailId.setVisibility(View.GONE);
				
				mHolder.llStoreManger.setVisibility(View.GONE);
				mHolder.llSMMobile.setVisibility(View.GONE);
				mHolder.llSMEmail.setVisibility(View.GONE);
				mHolder.llAlternateEmailID.setVisibility(View.GONE);
				
			}
			

		}

		// mHolder.etContactNumber.setOnTouchListener(new OnTouchListener() {

		mHolder.ibAddtoContact.setOnClickListener(this);

		mHolder.tvContactNumber.setText(Html.fromHtml("<u>" + "Contact Mobile"+ "</u>"));

		mHolder.tvContactNumber.setClickable(true);
		mHolder.tvContactNumber.setOnClickListener(this);

		mHolder.btCamera.setOnClickListener(this);
		mHolder.rl_enlargeImage.setOnClickListener(this);
		mHolder.btnUpdateProfile.setOnClickListener(this);
		mHolder.btnNext.setOnClickListener(this);

		if (mIsCancelButtonNeeded) {
			mHolder.btnNext.setText(R.string.cancel);
		}

		setHasOptionsMenu(true);

	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		if (isEdit) {
			menu.add(Menu.NONE, MENU_OPTION_SAVE, Menu.NONE, "SAVE").setIcon(android.R.drawable.ic_menu_save)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		} else {

			menu.add(Menu.NONE, MENU_OPTION_EDIT, Menu.NONE, "Edit").setIcon(android.R.drawable.ic_menu_edit)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
			case MENU_OPTION_EDIT:
				setEditable();
				isEdit = true;

				break;
			case MENU_OPTION_SAVE:
				onUpdateProfileClick();
				isEdit = false;
				break;
			default:
				break;
		}

		getActivity().supportInvalidateOptionsMenu();
		return true;
	}




	private void showGeneralData() {
		
		mHolder.etStoreCode.setText(mStoreBasicData.getStoreCode());
		mHolder.etStoreName.setText(mStoreBasicData.getStoreName());
		String cPersion = mStoreBasicData.getContactPerson();
		if (cPersion != null && !cPersion.equalsIgnoreCase("null")) {
			mHolder.etContactPerson.setText(cPersion);
		}

		mHolder.etContactNumber.setText(mStoreBasicData.getMobileNo());
		mHolder.etContactEmailId.setText(mStoreBasicData.getEmailID());
		mHolder.etStoreAddress.setText(mStoreBasicData.getStoreAddress());
		
		
		/*
		 *  New Added feild
		 */
		
		mHolder.etCity.setText(mStoreBasicData.getCityName());
		
		mHolder.etLandLineNumber.setText(mStoreBasicData.getLandlineNumber());
		mHolder.etSPCName.setText(mStoreBasicData.getSPCName());
		mHolder.etSPCCategory.setText(mStoreBasicData.getSPCCategory());
		mHolder.etStoreManger.setText(mStoreBasicData.getStoreMangerName());
		mHolder.etSMMobile.setText(mStoreBasicData.getSMMobile());
		mHolder.etSMEmail.setText(mStoreBasicData.getSMEmailID());
		mHolder.etAlternateEmailId.setText(mStoreBasicData.getAlternateEmailID());
		mHolder.etTinNumber.setText(mStoreBasicData.getTinNumber());
		mHolder.etPinCode.setText(mStoreBasicData.getPinCode());
		
		
		
		
		profileImage = mStoreBasicData.getPictureFileName();

		if (profileImage != null && !profileImage.equalsIgnoreCase("null")) {

			// remove default background
			mHolder.ivOutletImage.setBackgroundDrawable(null);

			ImageLoader imgLoader = ImageLoader.getInstance(mParentActivity);
			imgLoader.displayImageWithTag(profileImage,
					mHolder.ivOutletImage, R.drawable.camera);

		}
	}

	/**
	 * Show the save contact dialog
	 */
	private void saveContactDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set dialog message
		alertDialogBuilder
				.setMessage("Do you want to save contact?")
				.setCancelable(false)

				.setPositiveButton("Save",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (profileImageBitmap != null) {
									CommonUtil.saveContact(getActivity(),
											mHolder.etContactNumber.getText()
													.toString(),
											mHolder.etContactPerson.getText()
													.toString(),
											mHolder.etContactEmailId.getText()
													.toString(),
											mHolder.etStoreName.getText()
													.toString(),
											profileImageBitmap);
								} else if (profilePic != null) {
									CommonUtil.saveContact(getActivity(),
											mHolder.etContactNumber.getText()
													.toString(),
											mHolder.etContactPerson.getText()
													.toString(),
											mHolder.etContactEmailId.getText()
													.toString(),
											mHolder.etStoreName.getText()
													.toString(), profilePic);
								} else if (profileImage != null) {
									Bitmap bitmap = drawableToBitmap(mHolder.ivOutletImage
											.getDrawable());
									CommonUtil.saveContact(getActivity(),
											mHolder.etContactNumber.getText()
													.toString(),
											mHolder.etContactPerson.getText()
													.toString(),
											mHolder.etContactEmailId.getText()
													.toString(),
											mHolder.etStoreName.getText()
													.toString(), bitmap);
								} else {
									CommonUtil.saveContact(getActivity(),
											mHolder.etContactNumber.getText()
													.toString(),
											mHolder.etContactPerson.getText()
													.toString(),
											mHolder.etContactEmailId.getText()
													.toString(),
											mHolder.etStoreName.getText()
													.toString(), null);
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setCancelable(true);
		// show it
		alertDialog.show();
	}

	/**
	 * It holds the reference of the UI items used in the screen.
	 * 
	 * @author sabyasachi.b
	 * 
	 */
	private class ViewHolder {

		TextView tvContactNumber;
		EditText etContactPerson;
		EditText etContactNumber;
		EditText etContactEmailId;
		EditText etStoreAddress;
		EditText etStoreName;
		EditText etStoreCode;
		Button btnUpdateProfile;
		Button btnNext;
		Button btCamera;

		RelativeLayout rl_enlargeImage;

		ImageView ivOutletImage;
		ImageButton ibAddtoContact;
		
		EditText etLandLineNumber;
		EditText etSPCName;
		EditText etSPCCategory;
		EditText etStoreManger;
		EditText etSMMobile;
		EditText etSMEmail;
		EditText etAlternateEmailId;
		EditText etTinNumber;
		EditText etPinCode;
		EditText etCity;
		
		LinearLayout llContactPerson;
		LinearLayout llContactMobile;
		LinearLayout llTinNumber;
		LinearLayout llStoreManger;
		LinearLayout llSMMobile;
		LinearLayout llSMEmail;
		LinearLayout llAlternateEmailID;
		

		public ViewHolder(View rootView) {

			etContactPerson = (EditText) rootView
					.findViewById(R.id.et_ContactPerson_outletProfileFragmentBasic);
			etContactNumber = (EditText) rootView
					.findViewById(R.id.et_ContactMobile_outletProfileFragmentBasic);
			etContactEmailId = (EditText) rootView
					.findViewById(R.id.et_ContactEmail_outletProfileFragmentBasic);
			etStoreAddress = (EditText) rootView
					.findViewById(R.id.et_StoreAddress_outletProfileFragmentBasic);

			etStoreName = (EditText) rootView
					.findViewById(R.id.et_dealerName_outletProfileFragmentBasic);
			etStoreCode = (EditText) rootView
					.findViewById(R.id.et_dealerCode_outletProfileFragmentBasic);
			btnNext = (Button) rootView
					.findViewById(R.id.btnNext_outletProfileFragmentBasic);
			btnUpdateProfile = (Button) rootView
					.findViewById(R.id.btnUpdate_outletProfileFragmentBasic);

			btCamera = (Button) rootView
					.findViewById(R.id.bt_camera_outletProfileFragmentBasic);

			rl_enlargeImage = (RelativeLayout) rootView
					.findViewById(R.id.rl_enlarge_outletProfileFragmentBasic);

			ivOutletImage = (ImageView) rootView
					.findViewById(R.id.iv_outletimage_outletProfileFragmentBasic);

			tvContactNumber = (TextView) rootView
					.findViewById(R.id.tv_ContactMobile_outletProfileFragmentBasic);

			ibAddtoContact = (ImageButton) rootView
					.findViewById(R.id.ib_add_to_contact_outletProfileFragmentBasic);
			
			
			/* @ankit 
			 *  New Feild Added 
			 */
			
			etLandLineNumber = (EditText)rootView.findViewById(R.id.et_Landline_Number_outletProfileFragmentBasic);
			etSPCName = (EditText)rootView.findViewById(R.id.et_SPC_Name_outletProfileFragmentBasic);
			etSPCCategory = (EditText)rootView.findViewById(R.id.et_SPC_Category_outletProfileFragmentBasic);
			etStoreManger = (EditText)rootView.findViewById(R.id.et_Store_Manager_outletProfileFragmentBasic);
			etSMMobile = (EditText)rootView.findViewById(R.id.et_SM_Mobile_outletProfileFragmentBasic);
			etSMEmail = (EditText)rootView.findViewById(R.id.et_SM_Email_ID_outletProfileFragmentBasic);
			etAlternateEmailId = (EditText)rootView.findViewById(R.id.et_Alternate_Email_Id_outletProfileFragmentBasic);
			etTinNumber = (EditText)rootView.findViewById(R.id.et_Tin_Number_outletProfileFragmentBasic);
			etPinCode = (EditText)rootView.findViewById(R.id.et_Pin_Code_outletProfileFragmentBasic);
			
			 etCity = (EditText)rootView.findViewById(R.id.et_city_outletProfileFragmentBasic);
			
			
			llContactPerson = (LinearLayout)rootView.findViewById(R.id.ll_ContactPerson_outletProfileFragmentBasic);
			llContactMobile = (LinearLayout)rootView.findViewById(R.id.ll_ContactMobile_outletProfileFragmentBasic);
			llTinNumber = (LinearLayout)rootView.findViewById(R.id.ll_Tin_Number_outletProfileFragmentBasic);
			llStoreManger = (LinearLayout)rootView.findViewById(R.id.ll_Store_Manager_outletProfileFragmentBasic);
			llSMMobile = (LinearLayout)rootView.findViewById(R.id.ll_SM_Mobile_outletProfileFragmentBasic);
			llSMEmail = (LinearLayout)rootView.findViewById(R.id.ll_SM_Email_ID_outletProfileFragmentBasic);
			
			llAlternateEmailID = (LinearLayout)rootView.findViewById(R.id.ll_Alternate_Email_Id_outletProfileFragmentBasic);
			
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_camera_outletProfileFragmentBasic:

			onCamaraClick();

			break;

		case R.id.rl_enlarge_outletProfileFragmentBasic:

			makeEnlargement();

			break;

		case R.id.btnUpdate_outletProfileFragmentBasic:

			if (mHolder.etContactPerson.isEnabled()) {
				onUpdateProfileClick();
			} else {
				setEditable();
			}

			break;

		case R.id.btnNext_outletProfileFragmentBasic:

			onNextClick(v);

			break;

		case R.id.tv_ContactMobile_outletProfileFragmentBasic:

			if (mHolder.etContactNumber.getText().toString().equals("")) {

				Helper.showCustomToast(mParentActivity,
						R.string.no_contact_number, Toast.LENGTH_LONG);

			} else {
				CommonUtil.openDialer(getActivity(), mHolder.etContactNumber
						.getText().toString());
			}
			break;

		case R.id.ib_add_to_contact_outletProfileFragmentBasic:

			if (!(mHolder.etContactNumber.getText().toString().equals(""))) {

				if (!CommonUtil.isContactExists(getActivity(),
						mHolder.etContactNumber.getText().toString())) {

					saveContactDialog();

				} else {
					Toast.makeText(getActivity(),
							R.string.contact_already_exist, Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(getActivity(), R.string.no_contact_number,
						Toast.LENGTH_LONG).show();
			}

			break;

		}

	}

	/**
	 * Make the edit texts editable and un editable.
	 */
	private void setEditable() {

		if (!mHolder.etContactPerson.isEnabled()) {
			mHolder.etContactPerson.setEnabled(true);
			mHolder.etContactNumber.setEnabled(true);
			mHolder.etContactEmailId.setEnabled(true);
			// mHolder.etStoreAddress.setEnabled(true);
			
			mHolder.etLandLineNumber.setEnabled(true);
			mHolder.etSPCName.setEnabled(true);
			mHolder.etSPCCategory.setEnabled(true);
			mHolder.etStoreManger.setEnabled(true);
			mHolder.etSMMobile.setEnabled(true);
			mHolder.etSMEmail.setEnabled(true);
			
			mHolder.btnUpdateProfile.setText(R.string.update);

		} else {
			mHolder.etContactPerson.setEnabled(false);
			mHolder.etContactNumber.setEnabled(false);
			mHolder.etContactEmailId.setEnabled(false);
			// mHolder.etStoreAddress.setEnabled(false);
			
			mHolder.etLandLineNumber.setEnabled(false);
			mHolder.etSPCName.setEnabled(false);
			mHolder.etSPCCategory.setEnabled(false);
			mHolder.etStoreManger.setEnabled(false);
			mHolder.etSMMobile.setEnabled(false);
			mHolder.etSMEmail.setEnabled(false);
			
			
			mHolder.btnUpdateProfile.setText(R.string.edit);

		}

	}

	/**
	 * Called when when next button is clicked
	 * 
	 * @param v
	 */
	private void onNextClick(View v) {

		if (mIsCancelButtonNeeded && mParentActivity != null) {

			mParentActivity.finish();
			return;
		}

		if (Helper.getBoolValueFromPrefs(getActivity(),
				SharedPreferencesKey.PREF_IS_GEO_TAG_MANDATORY)) {

			Intent intent = new Intent(getActivity(), Geotag.class);
			intent.putExtra(IntentKey.KEY_STORE_BASIC, mStoreBasicData);
			startActivity(intent);

			getActivity().finish();

		} else {

			// Update is coverge in basic table
			DatabaseHelper.getConnection(mParentActivity).updateStoreCoverge(
					mStoreBasicData.getStoreID());

			Intent intent = new Intent(getActivity(),
					ActivityDashboardChild.class);

			intent.putExtra(IntentKey.KEY_STORE_BASIC, mStoreBasicData);
			intent.putExtra(IntentKey.KEY_MODULE_ID, 4);
			startActivity(intent);
			getActivity().finish();

		}

	}

	/**
	 * This method generates survey response id.
	 */

	/**
	 * Called when the camera button get clicked
	 * 
	 */
	private void onCamaraClick() {
		try {

			if (!mHolder.etContactPerson.isEnabled()) {

				Helper.showCustomToast(mParentActivity,
						R.string.plase_click_on_edit, Toast.LENGTH_LONG);

				return;
			}

			String fileName = "outlet_profile.jpg";
			File folder = new File(FileDirectory.DIRECTORY_GEOTAG_IMAGE);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			mOutletProfileImagePath = new File(folder, fileName);
			if (!mOutletProfileImagePath.exists()) {
				mOutletProfileImagePath.createNewFile();
			}

			Uri mImageUri = Uri.fromFile(mOutletProfileImagePath);

			Intent cameraActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraActivity.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageUri);
			startActivityForResult(cameraActivity, CAMERA_PIC_OUTLET_PROFILE);

		} catch (IOException e) {

			Toast.makeText(mParentActivity, R.string.error15, Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * Called when the update button get clicked
	 */
	private void onUpdateProfileClick() {

		Boolean isProfileUpdated = (Boolean) mHolder.ivOutletImage
				.getTag(R.string.is_store_prifle_image_updated);

		if (validation()) {
			if (Helper.isOnline(mParentActivity)) {
				if (isProfileUpdated != null) {
					if (isProfileUpdated.booleanValue()) {

						Bitmap profileImageBitmap = (Bitmap) mHolder.ivOutletImage
								.getTag();

						updateProfileImage(profileImageBitmap);

					}
				}

				else {
					updateStoreProfile("");
				}

			} else {

				Helper.showAlertDialog(
						mParentActivity,
						SSCAlertDialog.ERROR_TYPE,
						mParentActivity.getStringFromResource(R.string.error1),
						mParentActivity.getStringFromResource(R.string.error2),
						mParentActivity.getStringFromResource(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();
								// mParentActivity.finish();

							}
						}, false, null, null);

			}
		}

		isEdit = false;
		getActivity().supportInvalidateOptionsMenu();

	}

	/**
	 * Update the profile image in server
	 * 
	 * @param profileImageBitmap
	 */

	private void updateProfileImage(Bitmap profileImageBitmap) {

		new AsyncTask<Bitmap, Void, byte[]>() {

			private ProgressDialog progressDialog;

			protected void onPreExecute() {
				try {
					progressDialog = new ProgressDialog(getActivity());
					progressDialog.setProgress(0);
					progressDialog.setMax(100);
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setMessage("Processing Image...");
					progressDialog.setCancelable(false);
					progressDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected byte[] doInBackground(Bitmap... params) {

				byte[] compressedImage = Helper.getByteArray(params[0]);
				return compressedImage;
			}

			protected void onPostExecute(byte[] compressedImage) {

				try {
					progressDialog.cancel();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				VolleyPostDataToNetworkImage vollyPostImage = new VolleyPostDataToNetworkImage(
						mParentActivity,
						mParentActivity.getString(R.string.loadingmessage),
						new VolleyGetDataCallBack() {

							@Override
							public void processResponse(Object result) {
								if (result != null) {

									ResponseDto obj = new FetchingdataParser(
											getActivity())
											.getResponseResult(result
													.toString());
									if (obj.isSuccess()) {
										if (obj.getSingleResult().toString() != null) {
											String storePrifleImage = obj
													.getSingleResult()
													.toString();

											updateStoreProfile(storePrifleImage);

										} else {

											Helper.showCustomToast(
													mParentActivity,
													R.string.unable_upload_profile_image,
													Toast.LENGTH_LONG);

										}
									} else {
										Helper.showCustomToast(mParentActivity,
												R.string.error_occoured,
												Toast.LENGTH_LONG);

									}
								} else {
									Helper.showCustomToast(mParentActivity,
											R.string.networkserverbusy,
											Toast.LENGTH_LONG);
								}
							}

							@Override
							public void onError(VolleyError error) {
								Helper.showAlertDialog(
										mParentActivity,
										SSCAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										getString(R.string.server_not_responding),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {

												sdAlertDialog.dismiss();
												getActivity().finish();
											}
										}, false, null, null);

							}
						});

				vollyPostImage.setRequestData(compressedImage);
				vollyPostImage.setConfig(
						getResources().getString(R.string.url),
						"UploadStoreImage");
				vollyPostImage.callWebService();

			}

		}.execute(profileImageBitmap);

	}

	/**
	 * Update store profile
	 * 
	 * @param storePrifleImage
	 *            name of the store profile image file which is uploaded in the
	 *            server
	 */

	private void updateStoreProfile(final String storePrifleImage) {

		VolleyPostDataToNetwork vpdn = new VolleyPostDataToNetwork(
				mParentActivity,
				mParentActivity.getString(R.string.loadingmessage),
				new VolleyGetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						if (result != null) {
							try {
								JSONObject jObject = new JSONObject(result
										.toString());

								if (jObject.getBoolean("IsSuccess")) {

									Thread thread = new Thread(new Runnable() {

										@Override
										public void run() {

											try {

												updateStoreBasicInfo(storePrifleImage);

											} catch (Exception e) {

												e.printStackTrace();
											}
										}

									});

									thread.start();

									Helper.showAlertDialog(mParentActivity,
											SSCAlertDialog.SUCCESS_TYPE,
											getString(R.string.sucess),
											jObject.getString("Message"),
											getString(R.string.ok),
											new OnSDAlertDialogClickListener() {

												@Override
												public void onClick(
														SSCAlertDialog sdAlertDialog) {

													setEditable();
													sdAlertDialog.dismiss();

												}
											}, false, null, null);

								} else {

									Helper.showAlertDialog(
											mParentActivity,
											SSCAlertDialog.ERROR_TYPE,
											getString(R.string.error3),
											getString(R.string.server_not_responding),
											getString(R.string.ok),
											new SSCAlertDialog.OnSDAlertDialogClickListener() {

												@Override
												public void onClick(
														SSCAlertDialog sdAlertDialog) {

													sdAlertDialog.dismiss();
													getActivity().finish();
												}
											}, false, null, null);
								}
							} catch (Exception e) {

								Helper.showAlertDialog(
										mParentActivity,
										SSCAlertDialog.ERROR_TYPE,
										getString(R.string.error3),
										getString(R.string.server_not_responding),
										getString(R.string.ok),
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {

												sdAlertDialog.dismiss();
												getActivity().finish();
											}
										}, false, null, null);
							}

						} else {

							Helper.showCustomToast(mParentActivity,
									R.string.networkserverbusy,
									Toast.LENGTH_LONG);

						}

					}

					@Override
					public void onError(VolleyError error) {

						Helper.showAlertDialog(
								mParentActivity,
								SSCAlertDialog.ERROR_TYPE,
								getString(R.string.error3),
								getString(R.string.server_not_responding),
								getString(R.string.ok),
								new SSCAlertDialog.OnSDAlertDialogClickListener() {

									@Override
									public void onClick(
											SSCAlertDialog sdAlertDialog) {

										sdAlertDialog.dismiss();
										getActivity().finish();
									}
								}, false, null, null);
					}
				});

		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Long.valueOf(Helper
					.getStringValuefromPrefs(getActivity(),
							SharedPreferencesKey.PREF_USERID)));
			
			jsonobj.put(WebConfig.WebParams.COMPANY_ID, Helper
					.getStringValuefromPrefs(getActivity(),
							SharedPreferencesKey.PREF_COMPANYID));
			
			jsonobj.put(WebConfig.WebParams.ROLE_ID, Helper
					.getIntValueFromPrefs(getActivity(),
							SharedPreferencesKey.PREF_ROLEID));

			String contactPerson = mHolder.etContactPerson.getText().toString();
			String mobileNumber = mHolder.etContactNumber.getText().toString();
			String emailID = mHolder.etContactEmailId.getText().toString();
			String storeAddress = mHolder.etStoreAddress.getText().toString();
			
			
			String landLineNumber = mHolder.etLandLineNumber.getText().toString();
			String spcName = mHolder.etSPCName.getText().toString();
			String spcCategory = mHolder.etSPCCategory.getText().toString();
			String storeManger = mHolder.etStoreManger.getText().toString();
			String smMobile = mHolder.etSMMobile.getText().toString();
			String smEmail = mHolder.etSMEmail.getText().toString();

			mStoreBasicData.setContactPerson(contactPerson);
			mStoreBasicData.setMobileNo(mobileNumber);
			mStoreBasicData.setEmailID(emailID);
			mStoreBasicData.setStoreAddress(storeAddress);
			
			mStoreBasicData.setLandlineNumber(landLineNumber);
			mStoreBasicData.setSPCName(spcName);
			mStoreBasicData.setSPCCategory(spcCategory);
			mStoreBasicData.setStoreMangerName(storeManger);
			mStoreBasicData.setSMMobile(smMobile);
			mStoreBasicData.setSMEmailID(emailID);
			
			JSONObject jsonobjNew = new JSONObject();
			
			
			
			jsonobjNew.put(WebConfig.WebParams.PICTURE_FILE_NAME, storePrifleImage);
			jsonobjNew.put(WebConfig.WebParams. STORE_ID, mStoreBasicData.getStoreID());
			jsonobjNew.put(WebConfig.WebParams.CONTACT_PERSON, contactPerson);
			jsonobjNew.put(WebConfig.WebParams.MOBILE_NO, mobileNumber);
			jsonobjNew.put(WebConfig.WebParams.EMAIL_ID, emailID);
			jsonobjNew.put(WebConfig.WebParams.STORE_ADDRESS, storeAddress);
			
			/*
			 * New added field
			 */
			jsonobjNew.put(WebConfig.WebParams.LAND_LINE_NUMBER, landLineNumber);
			jsonobjNew.put(WebConfig.WebParams.SPC_NAME, spcName);
			jsonobjNew.put(WebConfig.WebParams.SPC_CATEGORY, spcCategory);
			jsonobjNew.put(WebConfig.WebParams.STORE_MANAGER_NAME, storeManger);
			jsonobjNew.put(WebConfig.WebParams.SM_MOBILE, smMobile);
			jsonobjNew.put(WebConfig.WebParams.SM_EMAILID, smEmail);
			
			jsonobj.put(WebConfig.WebParams.STORE_PROFILE, jsonobjNew);
			
			
			

		} catch (NumberFormatException e) {
			Helper.printStackTrace(e);
		} catch (JSONException e) {
			Helper.printStackTrace(e);
		}
		vpdn.setConfig(getString(R.string.url),WebConfig.WebMethod.UPDATE_STORE_PROFILE);
		vpdn.setRequestData(jsonobj);
		vpdn.callWebService();
	}

	private void updateStoreBasicInfo(final String storePrifleImage) {
		mStoreBasicData.setPictureFileName(storePrifleImage);

		long storeID = mStoreBasicData.getStoreID();

		String whereClause = StoreBasicColulmns.KEY_BEAT_STORE_ID + "= ? ";
		String[] whereArgs = new String[] { String.valueOf(storeID) };

		ContentValues contentValues = new ContentValues();

		contentValues.put(StoreBasicColulmns.KEY_BEAT_CONTACT_PERSION,
				mStoreBasicData.getContactPerson());

		contentValues.put(StoreBasicColulmns.KEY_BEAT_EMAIL_ID,
				mStoreBasicData.getEmailID());
		contentValues.put(StoreBasicColulmns.KEY_STORE_ADDRESS,
				mStoreBasicData.getStoreAddress());

		contentValues.put(StoreBasicColulmns.KEY_BEAT_MOBILE_NUMBER,
				mStoreBasicData.getMobileNo());

		if (!mStoreBasicData.getPictureFileName().equalsIgnoreCase("")) {

			String imagePath = 
					getString(R.string.url_file_processor_servicebase)
					+ "id="
					+ mStoreBasicData.getPictureFileName()
					+ "&type=Store";

			contentValues.put(StoreBasicColulmns.KEY_BEAT_PICTURE_FILE_NAME, imagePath);
		}
		
		contentValues.put(StoreBasicColulmns.KEY_LAND_LINE_NUMBER,
				mStoreBasicData.getLandlineNumber());
		
		contentValues.put(StoreBasicColulmns.KEY_SPC_NAME,
				mStoreBasicData.getSPCName());
		
		contentValues.put(StoreBasicColulmns.KEY_SPC_CATEGORY,
				mStoreBasicData.getSPCCategory());
		
		contentValues.put(StoreBasicColulmns.KEY_STORE_MANAGER_NAME,
				mStoreBasicData.getStoreMangerName());
		
		contentValues.put(StoreBasicColulmns.KEY_SM_MOBILE,
				mStoreBasicData.getSMMobile());

		contentValues.put(StoreBasicColulmns.KEY_SM_EMAIL_ID,
				mStoreBasicData.getSMEmailID());
		

		/*
		 * DatabaseHelper .getConnection( mParentActivity)
		 * .updateStoresDetailData( mStoreBasicData);
		 */

		mParentActivity.getContentResolver().update(
				ProviderContract.URI_STORES_BASICS, contentValues, whereClause,
				whereArgs);


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == Activity.RESULT_OK) {

				switch (requestCode) {

				case CAMERA_PIC_OUTLET_PROFILE:

					try {

						profileImageBitmap = Helper
								.creatBitmapFormImagepath(mOutletProfileImagePath
										.getAbsolutePath());
						if (profileImageBitmap != null) {
							mHolder.ivOutletImage
									.setImageBitmap(profileImageBitmap);
							mHolder.ivOutletImage.setTag(profileImageBitmap);
							mHolder.ivOutletImage.setTag(
									R.string.is_store_prifle_image_updated,
									true);

						}

					} catch (Exception e) {

						Helper.showCustomToast(mParentActivity,
								R.string.error15, Toast.LENGTH_LONG);

					}

					break;

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show enlarged image of the user profile.
	 */
	private void makeEnlargement() {

		profilePic = (Bitmap) mHolder.ivOutletImage.getTag();

		if (profilePic != null) {
			Intent intent = new Intent(mParentActivity,
					FullScreenImageActivity.class);

			// Convert to byte array
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			profilePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			intent.putExtra("image", byteArray);

			startActivity(intent);
			mParentActivity.overridePendingTransition(R.anim.from_middle,
					R.anim.to_middle);

		} else {

			Helper.showCustomToast(mParentActivity, R.string.no_profile_image,
					Toast.LENGTH_LONG);

		}

	}

	private Bitmap drawableToBitmap(Drawable drawable) {

		try {
			if (drawable instanceof BitmapDrawable) {
				return ((BitmapDrawable) drawable).getBitmap();
			}

			int width = drawable.getIntrinsicWidth();
			width = width > 0 ? width : 1;
			int height = drawable.getIntrinsicHeight();
			height = height > 0 ? height : 1;

			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);

			return bitmap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean validation() {
		boolean flag = true;
		
		
		if(!mStoreBasicData.getChannelType().equalsIgnoreCase("RR") || !mStoreBasicData.getChannelType().equalsIgnoreCase("MR")){
			
			if (mHolder.etContactPerson.getText().toString().trim().length() == 0) {
				mHolder.etContactPerson.setError(mParentActivity
						.getStringFromResource(R.string.enter_Contact_Person_msg));
				mHolder.etContactPerson.requestFocus();
				flag = false;
			}
			if (mHolder.etContactNumber.getText().toString().trim().length() < 10) {
				mHolder.etContactNumber.setError(mParentActivity
						.getStringFromResource(R.string.enter_mobile_msg));
				mHolder.etContactNumber.requestFocus();
				flag = false;
			}
		}
		
		if (!mStoreBasicData.getChannelType().equalsIgnoreCase("RS") || !mStoreBasicData.getChannelType().equalsIgnoreCase("BS") ) {
			
			if (mHolder.etSMMobile.getText().toString().trim().length() < 10) {
				mHolder.etContactPerson.setError(mParentActivity
						.getStringFromResource(R.string.enter_mobile_msg));
				mHolder.etContactPerson.requestFocus();
				flag = false;
			}	
			
			
			if (mHolder.etSMEmail.getText().toString().trim().length() > 0) {
				if (!Helper.checkEmail(mHolder.etSMEmail.getText()
						.toString().trim())) {
					mHolder.etSMEmail.setError(mParentActivity
							.getStringFromResource(R.string.invalidemail));
					mHolder.etSMEmail.requestFocus();
					flag = false;
			}
		}
			
			if (mHolder.etStoreManger.getText().toString().trim().length() == 0) {
				mHolder.etStoreManger.setError(mParentActivity
						.getStringFromResource(R.string.enter_store_manager_name));
				mHolder.etStoreManger.requestFocus();
				flag = false;
			}	
			
	}
		
		if (mHolder.etContactEmailId.getText().toString().trim().length() == 0) {
			mHolder.etContactEmailId.setError(mParentActivity
					.getStringFromResource(R.string.enter_email_msg));
			mHolder.etContactEmailId.requestFocus();
			flag = false;
		}
		if (mHolder.etContactEmailId.getText().toString().trim().length() > 0) {
			if (!Helper.checkEmail(mHolder.etContactEmailId.getText()
					.toString().trim())) {
				mHolder.etContactEmailId.setError(mParentActivity
						.getStringFromResource(R.string.invalidemail));
				mHolder.etContactEmailId.requestFocus();
				flag = false;
			}
		}
		
		if (mHolder.etLandLineNumber.getText().toString().trim().length() < 10) {
			mHolder.etLandLineNumber.setError(mParentActivity
					.getStringFromResource(R.string.enter_landline_number));
			mHolder.etLandLineNumber.requestFocus();
			flag = false;
		}	
		return flag;
	}

}