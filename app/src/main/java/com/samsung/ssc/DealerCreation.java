package com.samsung.ssc;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.samsung.ssc.CustomUI.CustomeSpinner;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.CityDto;
import com.samsung.ssc.dto.DealerCreationMultiPartUpload;
import com.samsung.ssc.dto.DealerCreationfinalRequest;
import com.samsung.ssc.dto.DistrictDto;
import com.samsung.ssc.dto.OfficeLocationsDto;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.dto.ResponseTypeDto;
import com.samsung.ssc.dto.SubmitDealerCreationDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.MyLocation;
import com.samsung.ssc.util.Constants.HTTPResponseCode;
import com.samsung.ssc.util.Constants.ImageSize;
import com.samsung.ssc.util.MyLocation.LocationResult;
import com.samsung.ssc.util.ScalingUtilities.ScalingLogic;

public class DealerCreation extends BaseActivity {
	private CustomeSpinner district_spinner, city_spinner, pin_spinner,
			parentdealer_spinner, dealer_spinner, reasonofappointmentspinner,
			firm_type_spinner, dayoff_spinner, promoterrequired_spinner,
			consumerfinanceavailable_spinner, parent_company_spinner;
	private EditText ownerdob_edittext, firm_name_edittext,
			street_name_edittext, firm_email_edittext, owner_name_edittext,
			ownermob_edittext, contactperson_name_edittext,
			contactperson_mob_edittext, totalcountersize_edittext,
			tin_name_edittext, pan_edittext;

	private Button submitbtn, cancelbtn;
	private ImageView btnview1, btnview2, btnview3, btnview4, btnview5;

	private int REQUEST_CODE = 1001;
	private int viewParentid;
	private String imagepath;
	private View cameraView;
	private View editview;
	private double latitude, longitude;
	private MyLocation myloc;
	protected boolean isfirsttime;
	private LinearLayout parentdealerLayout;
	private ArrayList<OfficeLocationsDto> srdData;
	private ArrayList<DistrictDto> districtList;
	private ArrayList<CityDto> cityList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dealer_creation);
		getCurrentLocation();
		initViews();
		if (isOnline()) {
			getSrd();
			getDistrict();
		} else {

			Helper.showAlertDialog(DealerCreation.this,
					SSCAlertDialog.ERROR_TYPE,
					getString(R.string.error1), getString(R.string.error2),
					getString(R.string.ok),
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();

						}
					}, false, null, null);

			district_spinner.setEnabled(false);
			parent_company_spinner.setEnabled(false);
		}

	}

	private void initViews() {

		district_spinner = (CustomeSpinner) findViewById(R.id.district_spinner);
		parent_company_spinner = (CustomeSpinner) findViewById(R.id.parent_company_spinner);
		parentdealerLayout = (LinearLayout) findViewById(R.id.parentdealerLayout);
		dealer_spinner = (CustomeSpinner) findViewById(R.id.dealer_spinner);
		parentdealer_spinner = (CustomeSpinner) findViewById(R.id.parentdealer_spinner);
		reasonofappointmentspinner = (CustomeSpinner) findViewById(R.id.reasonofappointmentspinner);
		firm_type_spinner = (CustomeSpinner) findViewById(R.id.firm_type_spinner);
		dayoff_spinner = (CustomeSpinner) findViewById(R.id.dayoff_spinner);
		promoterrequired_spinner = (CustomeSpinner) findViewById(R.id.promoterrequired_spinner);
		consumerfinanceavailable_spinner = (CustomeSpinner) findViewById(R.id.consumerfinanceavailable_spinner);
		firm_name_edittext = (EditText) findViewById(R.id.firm_name_edittext);
		street_name_edittext = (EditText) findViewById(R.id.street_name_edittext);
		firm_email_edittext = (EditText) findViewById(R.id.firm_email_edittext);
		tin_name_edittext = (EditText) findViewById(R.id.tin_name_edittext);
		pan_edittext = (EditText) findViewById(R.id.pan_edittext);
		owner_name_edittext = (EditText) findViewById(R.id.owner_name_edittext);
		ownermob_edittext = (EditText) findViewById(R.id.ownermob_edittext);
		contactperson_name_edittext = (EditText) findViewById(R.id.contactperson_name_edittext);
		contactperson_mob_edittext = (EditText) findViewById(R.id.contactperson_mob_edittext);
		totalcountersize_edittext = (EditText) findViewById(R.id.totalcountersize_edittext);
		ownerdob_edittext = (EditText) findViewById(R.id.ownerdob_edittext);
		ImageButton ownerdob_imagebutton = (ImageButton) findViewById(R.id.ownerdob_imagebutton);
		submitbtn = (Button) findViewById(R.id.btnSubmit);
		cancelbtn = (Button) findViewById(R.id.btnCancel);
		btnview1 = (ImageView) findViewById(R.id.btnview1);
		btnview2 = (ImageView) findViewById(R.id.btnview2);
		btnview3 = (ImageView) findViewById(R.id.btnview3);
		btnview4 = (ImageView) findViewById(R.id.btnview4);
		btnview5 = (ImageView) findViewById(R.id.btnview5);
		ownerdob_imagebutton.setOnClickListener(this);
		submitbtn.setOnClickListener(this);
		cancelbtn.setOnClickListener(this);
		btnview1.setOnClickListener(this);
		btnview2.setOnClickListener(this);
		btnview3.setOnClickListener(this);
		btnview4.setOnClickListener(this);
		btnview5.setOnClickListener(this);
		pin_spinner = (CustomeSpinner) findViewById(R.id.pincode_spinner);
		city_spinner = (CustomeSpinner) findViewById(R.id.city_spinner);
		city_spinner.setEnabled(false);
		pin_spinner.setEnabled(false);
		btnview1.setEnabled(false);
		btnview2.setEnabled(false);
		btnview3.setEnabled(false);
		btnview4.setEnabled(false);
		btnview5.setEnabled(false);
	}

	@Override
	public void onClick(View v) {

		super.onClick(v);
		switch (v.getId()) {
		case R.id.ownerdob_imagebutton:
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					ownerdob_edittext.setText(formatDate(year, monthOfYear,
							dayOfMonth));

				}
			};
			DatePickerDialog datePickerDialog = new DatePickerDialog(this,
					datePickerListener, year, month, day);
			datePickerDialog.show();

			break;

		case R.id.btnSubmit:

			onSubmitClick(v);

			break;

		case R.id.btnCancel:
			finish();

			break;

		case R.id.btnview1:
		case R.id.btnview2:
		case R.id.btnview3:
		case R.id.btnview4:
		case R.id.btnview5:

			showpopup(v);

			break;

		default:
			break;
		}
	}

	public void onSpanShotClick(View view) {

		openCamera(view);

	}

	private void showpopup(View v) {

		String storeimagepath = (String) v.getTag();

		File imgFile = new File(storeimagepath);
		if (storeimagepath != null) {
			Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			LayoutInflater inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.popupdealercreation,
					(ViewGroup) findViewById(R.id.mainLayout));
			ImageView imageView = (ImageView) layout
					.findViewById(R.id.imageViewpopup);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					300, 300);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			imageView.setScaleType(ScaleType.FIT_XY);
			final PopupWindow popup = new PopupWindow(this);
			popup.setWidth(300);
			popup.setHeight(300);
			popup.setContentView(layout);
			popup.setBackgroundDrawable(new BitmapDrawable());
			popup.setOutsideTouchable(true);
			popup.setFocusable(true);
			imageView.setImageBitmap(bitmap);
			popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

		}

	}

	private void openCamera(View v) {

		String imagename = ((TextView) ((LinearLayout) v.getParent()
				.getParent()).getChildAt(0)).getText().toString();
		String imagePath = FileDirectory.DIRECTORY_DEALER_CREATION + "/"
				+ imagename;
		File dealercreationImageFile = new File(imagePath);
		if (!dealercreationImageFile.exists()) {
			try {
				dealercreationImageFile.createNewFile();
			} catch (IOException e) {
				Helper.printStackTrace(e);
			}
		} else {
		}
		Uri uri = Uri.fromFile(dealercreationImageFile);

		Intent cameraActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraActivity.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
		viewParentid = ((View) ((RelativeLayout) v.getParent()).getChildAt(0))
				.getId();
		cameraView = v;
		editview = ((View) ((RelativeLayout) v.getParent()).getChildAt(2));
		imagepath = imagePath;
		startActivityForResult(cameraActivity, REQUEST_CODE);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void getDistrict() {

		PostDataToNetwork pdn = new PostDataToNetwork(DealerCreation.this,
				getResources().getString(R.string.loadingmessage),
				new GetDataCallBack() {

					

					@Override
					public void processResponse(Object result) {

						try {
							if (result != null) {

								try {

									ResponseDto obj = new FetchingdataParser(
											DealerCreation.this)
											.getResponseResult(result
													.toString());

									if (obj.isSuccess()) {
										
										String resultString = obj.getResult();
										JSONArray jArray  = new JSONArray(resultString);
										districtList = 	new ArrayList<DistrictDto>();

										for (int i = 0; i < jArray.length(); i++) {
											
											DistrictDto district = new DistrictDto();
											
											JSONObject jItemObject = jArray
													.getJSONObject(i);
											district.setDistrictName(jItemObject.getString("DistrictName"));
											district.setDistrictCode(jItemObject.getString("DistrictCode"));
											districtList.add(district);
										}

										if (districtList != null
												&& districtList.size() > 0) {
											
											ArrayList<String> districtNameList = new ArrayList<String>();
											
											for (int i = 0; i < districtList.size(); i++) {
												
												districtNameList.add(districtList.get(i).getDistrictName());
												
											}
											
											districtNameList.add(0, "Select");

											// district_spinner.setPrompt("Select District");
											ArrayAdapter<String> adapter = new ArrayAdapter<String>(
													getBaseContext(),
													android.R.layout.simple_spinner_item,
													districtNameList);
											adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
											district_spinner
													.setAdapter(adapter);
											district_spinner
													.setOnItemSelectedListener(new OnItemSelectedListener() {

														@Override
														public void onItemSelected(
																AdapterView<?> parent,
																View view,
																int position,
																long id) {
															if (position > 0) {

																//String distictName = (String) parent.getItemAtPosition(position);
																String distictCode = districtList.get(position-1).getDistrictCode();

																if (isOnline()) {
																	getCity(distictCode);
																} else {

																	Helper.showAlertDialog(
																			DealerCreation.this,
																			SSCAlertDialog.ERROR_TYPE,
																			getString(R.string.error1),
																			getString(R.string.error2),
																			getString(R.string.ok),
																			new SSCAlertDialog.OnSDAlertDialogClickListener() {

																				@Override
																				public void onClick(
																						SSCAlertDialog sdAlertDialog) {
																					sdAlertDialog
																							.dismiss();

																				}
																			},
																			false,
																			null,
																			null);
																}

															}
														}

														@Override
														public void onNothingSelected(
																AdapterView<?> parent) {
														 
														}
													});

										} else {
											Helper.showErrorAlertDialog(
													DealerCreation.this,
													getResources().getString(
															R.string.error3),
													getResources()
															.getString(
																	R.string.district_not_available),
													new OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.dismiss();
															district_spinner
																	.setEnabled(false);

														}
													});

										}

									} else {

										Helper.showAlertDialog(
												getApplicationContext(),
												SSCAlertDialog.ERROR_TYPE,
												"Error",
												"Please contact your admin",
												"OK",
												new SSCAlertDialog.OnSDAlertDialogClickListener() {

													@Override
													public void onClick(
															SSCAlertDialog sdAlertDialog) {
														// TODO Auto-generated
														// method stub
														sdAlertDialog.dismiss();

													}
												}, false, null, null);

									}
								} catch (JSONException e) {
									// TODO: handle exception
								}  catch (NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e2) {
									// TODO: handle exception
								}

							} else {
								Toast.makeText(
										DealerCreation.this,
										getResources().getString(
												R.string.networkserverbusy),
										Toast.LENGTH_LONG).show();
							}
						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(DealerCreation.this,
							SharedPreferencesKey.PREF_USERID));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		pdn.setConfig(getString(R.string.url),
				WebConfig.WebMethod.GET_MDM_DISTRICT);

		pdn.execute(jsonobj);

	}

	private void getCity(final String district) {
		// TODO Auto-generated method stub

		PostDataToNetwork pdn = new PostDataToNetwork(DealerCreation.this,
				getResources().getString(R.string.loadingmessage),
				new GetDataCallBack() {

					

					@Override
					public void processResponse(Object result) {

						if (result != null) {

							try {
								ResponseDto obj = new FetchingdataParser(
										DealerCreation.this)
										.getResponseResult(result
												.toString());

								if (obj.isSuccess()) {
									JSONArray jArray = new JSONArray(obj.getResult());
									
									cityList =  new ArrayList<CityDto>();
									
									
									for (int i = 0; i < jArray.length(); i++) {
										
										CityDto cityDtoObj = new CityDto();
										JSONObject jItemObject = jArray.getJSONObject(i);
										cityDtoObj.setCityName(jItemObject.getString("CityName"));
										cityDtoObj.setCityCode(jItemObject.getString("CityCode"));
										cityList.add(cityDtoObj);
										//cityList.add(jArray.getString(i));
									} 

									if (cityList != null && cityList.size() > 0) {
										
										final ArrayList<String> citesName = new ArrayList<String>();
										
										for (int i = 0; i < cityList.size(); i++) {
											citesName.add(cityList.get(i).getCityName());
										}
										citesName.add(0, "Select");

										// district_spinner.setPrompt("Select District");
										ArrayAdapter<String> adapter = new ArrayAdapter<String>(
												getBaseContext(),
												android.R.layout.simple_spinner_item,
												citesName);
										adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

										city_spinner.setAdapter(adapter);
										city_spinner.setEnabled(true);

									 

									} else {
										Helper.showErrorAlertDialog(
												DealerCreation.this,
												getResources().getString(
														R.string.error3),
												getResources().getString(
														R.string.datanotavailable),
												new OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();

													}
												});

									}

								} else {

									Helper.showErrorAlertDialog(
											DealerCreation.this,
											getString(R.string.error3),
											getString(R.string.please_contact_admin));
								}
							} catch (NotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO: handle exception
							}

						} else {
							Toast.makeText(
									DealerCreation.this,
									getResources().getString(
											R.string.networkserverbusy),
									Toast.LENGTH_LONG).show();
						}
					}
				});

		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj.put(WebConfig.WebParams.DISTRICT, district);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pdn.setConfig(getString(R.string.url), WebConfig.WebMethod.GET_MDM_CITY);

		pdn.execute(jsonobj);

	}

//	private void getpin(String city, String district) {
//		// TODO Auto-generated method stub
//
//		PostDataToNetwork pdn = new PostDataToNetwork(DealerCreation.this,
//				getResources().getString(R.string.loadingmessage),
//				new GetDataCallBack() {
//
//					@Override
//					public void processResponse(Object result) {
//
//						if (result != null) {
//
//							Gson gson = new Gson();
//
//							Type type = new TypeToken<ResponseTypeDto>() {
//							}.getType();
//							ResponseTypeDto<?> data = gson.fromJson(
//									result.toString(), type);
//
//							if (data.isSuccess()) {
//								final ArrayList<String> pinlist = (ArrayList<String>) data
//										.getResult();
//
//								if (pinlist != null && pinlist.size() > 0) {
//									pinlist.add(0, "Select");
//
//									// district_spinner.setPrompt("Select District");
//									ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//											getBaseContext(),
//											android.R.layout.simple_spinner_item,
//											pinlist);
//									adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//									pin_spinner.setAdapter(adapter);
//									pin_spinner.setEnabled(true);
//
//								} else {
//									Helper.showErrorAlertDialog(
//											DealerCreation.this,
//											getResources().getString(
//													R.string.error3),
//											getResources().getString(
//													R.string.datanotavailable),
//											new OnClickListener() {
//
//												@Override
//												public void onClick(
//														DialogInterface dialog,
//														int which) {
//													dialog.dismiss();
//
//												}
//											});
//
//								}
//
//							} else {
//
//								Helper.showErrorAlertDialog(
//										DealerCreation.this,
//										getString(R.string.error3),
//										getString(R.string.please_contact_admin));
//							}
//
//						} else {
//							Toast.makeText(
//									DealerCreation.this,
//									getResources().getString(
//											R.string.networkserverbusy),
//									Toast.LENGTH_LONG).show();
//						}
//					}
//				});
//
//		JSONObject jsonobj = new JSONObject();
//		try {
//			jsonobj.put(WebConfig.WebParams.DISTRICT, district);
//			jsonobj.put(WebConfig.WebParams.CITY, city);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		pdn.setConfig(getString(R.string.url),
//				WebConfig.WebMethod.GET_MDM_PINCODE);
//
//		pdn.execute(jsonobj);
//
//	}

	private void onSubmitClick(View v) {

		// prepareImagesendArraylist(11);

		if (checkValidation()) {

			if (isOnline()) {
				SubmitDealerCreationDto inputdata = prepareRequest();
				checkDuplicateData(inputdata);
			} else {

				Helper.showAlertDialog(
						v.getContext(),
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
		}
	}

	private void checkDuplicateData(final SubmitDealerCreationDto inputdata) {
		PostDataToNetwork pdn = new PostDataToNetwork(DealerCreation.this,
				getResources().getString(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						if (result != null) {

 
							ResponseDto obj = new FetchingdataParser(
									DealerCreation.this)
									.getResponseResult(result.toString());

							if (obj.isSuccess()) {

								boolean resultdata = Boolean.parseBoolean(obj
										.getSingleResult());
								if (resultdata) {
									uploadDataServer(inputdata);

								} else {
									Helper.showErrorAlertDialog(
											DealerCreation.this,
											getString(R.string.error3),
											getString(R.string.duplicate_msg_dealer_creation));
								}

							} else {

								Helper.showErrorAlertDialog(
										DealerCreation.this,
										getString(R.string.error3),
										getString(R.string.please_contact_admin));
							}

						} else {
							Toast.makeText(
									DealerCreation.this,
									getResources().getString(
											R.string.networkserverbusy),
									Toast.LENGTH_LONG).show();
						}
					}
				});

		JSONObject jsonobj = new JSONObject();
		EditText firm_mobno_edittext = (EditText) findViewById(R.id.firm_mobno_edittext);

		try {
			jsonobj.put(WebConfig.WebParams.FIRM_MOBILE, firm_mobno_edittext
					.getText().toString().trim());
			jsonobj.put(WebConfig.WebParams.FIRM_EMAIL, firm_email_edittext
					.getText().toString().trim());
			jsonobj.put(WebConfig.WebParams.PAN, pan_edittext.getText()
					.toString().trim());
			jsonobj.put(WebConfig.WebParams.TIN, tin_name_edittext.getText()
					.toString().trim());
			if (dealer_spinner.getSelectedItemPosition() == 1) {
				jsonobj.put(WebConfig.WebParams.IS_CHILD, false);
			} else {
				jsonobj.put(WebConfig.WebParams.IS_CHILD, true);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pdn.setConfig(getString(R.string.url),
				WebConfig.WebMethod.GET_MDM_DUPLICATE_CHECK);

		pdn.execute(jsonobj);

	}

	private void getSrd() {

		PostDataToNetwork pdn = new PostDataToNetwork(DealerCreation.this,
				getStringFromResource(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						if (result != null) {

							try {

								ResponseDto obj = new FetchingdataParser(
										DealerCreation.this)
										.getResponseResult(result.toString());

								String resultSting = obj.getResult();

								JSONArray jArray = new JSONArray(resultSting);
								srdData = new ArrayList<OfficeLocationsDto>();

								int count = jArray.length();
								for (int i = 0; i < count; i++) {

									JSONObject item = jArray.getJSONObject(i);

									OfficeLocationsDto modal = new OfficeLocationsDto();
									modal.setDistributorName(item
											.getString("DistributorName"));
									modal.setDistributorCode(item
											.getString("DistributorCode"));
									srdData.add(modal);
								}

								if (obj.isSuccess()) {

									if (srdData != null && srdData.size() > 0) {

										ArrayList<String> srdList = new ArrayList<String>();
										for (int i = 0; i < srdData.size(); i++) {
											srdList.add(srdData.get(i)
													.getDistributorName());
										}
										srdList.add(0, "Select");

										ArrayAdapter<String> adapter = new ArrayAdapter<String>(
												getBaseContext(),
												android.R.layout.simple_spinner_item,
												srdList);
										adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

										parent_company_spinner
												.setAdapter(adapter);

									} else {
										Helper.showErrorAlertDialog(
												DealerCreation.this,
												getResources().getString(
														R.string.error3),
												getResources()
														.getString(
																R.string.parent_company_not_available),
												new OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
														parent_company_spinner
																.setEnabled(false);

													}
												});

									}

								} else {

									Helper.showErrorAlertDialog(
											DealerCreation.this,
											getString(R.string.error3),
											getString(R.string.please_contact_admin));
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {
							Toast.makeText(
									DealerCreation.this,
									getResources().getString(
											R.string.networkserverbusy),
									Toast.LENGTH_LONG).show();
						}
					}
				});
		try {
			JSONObject jsonobj = new JSONObject();

			jsonobj.put(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID));
			jsonobj.put(WebConfig.WebParams.ROLE_ID_CAPS, Helper
					.getIntValueFromPrefs(this,
							SharedPreferencesKey.PREF_ROLEID));

			pdn.setConfig(getString(R.string.url),
					WebConfig.WebMethod.GET_PARENT_DISTRIBUTER);

			pdn.execute(jsonobj);

		} catch (Exception e) {
			Helper.printStackTrace(e);
		}

	}

	private SubmitDealerCreationDto prepareRequest() {

		SubmitDealerCreationDto creation = new SubmitDealerCreationDto();
		creation.setReasonofappointment(reasonofappointmentspinner
				.getSelectedItemPosition());
		creation.setTypeofFirm(firm_type_spinner.getSelectedItemPosition());
		creation.setNameoffirm(firm_name_edittext.getText().toString().trim());

		try {
			// creation.setDistrict(district_spinner.getSelectedItem().toString());
			creation.setDistrict(districtList.get(district_spinner.getSelectedItemPosition() -1).getDistrictCode());
			//creation.setCitytown(city_spinner.getSelectedItem().toString());
			creation.setCitytown(cityList.get(city_spinner.getSelectedItemPosition() -1 ).getCityCode());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();

			Helper.showErrorAlertDialog(
					DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.district_not_available_please_contact_admin));
		}
		// creation.setPinCode(pin_spinner.getSelectedItem().toString());
		creation.setStreetName(street_name_edittext.getText().toString().trim());
		EditText landlineno_edittext = (EditText) findViewById(R.id.landlineno_edittext);

		if (landlineno_edittext.getText() != null)
			creation.setLandlineNo(landlineno_edittext.getText().toString()
					.trim());

		EditText firm_mobno_edittext = (EditText) findViewById(R.id.firm_mobno_edittext);
		if (firm_mobno_edittext.getText() != null)
			creation.setFirmMob(firm_mobno_edittext.getText().toString().trim());
		creation.setFirmEmail(firm_email_edittext.getText().toString().trim());
		creation.setTypeFfDealer(dealer_spinner.getSelectedItem().toString());
		try {
			creation.setParentCompany(srdData.get(
					parent_company_spinner.getSelectedItemPosition() - 1)
					.getDistributorCode());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			creation.setParentDealerCode(parentdealer_spinner.getSelectedItem()
					.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pan_edittext.getText() != null)
			creation.setPan(pan_edittext.getText().toString().trim());
		if (tin_name_edittext.getText() != null)
			creation.setTin(tin_name_edittext.getText().toString().trim());
		creation.setOwnerName(owner_name_edittext.getText().toString().trim());
		creation.setOwnerMob(ownermob_edittext.getText().toString().trim());
		creation.setContactPersonName(contactperson_name_edittext.getText()
				.toString().trim());
		creation.setContactPersonMob(contactperson_mob_edittext.getText()
				.toString().trim());
		creation.setDayOff(dayoff_spinner.getSelectedItemPosition());
		creation.setTotalcountersize(Integer.parseInt(totalcountersize_edittext
				.getText().toString().trim()));
		EditText storesize_edittext = (EditText) findViewById(R.id.storesize_edittext);
		if (storesize_edittext.getText() != null)
			creation.setStoreCode(storesize_edittext.getText().toString()
					.trim());

		EditText prmcode_edittext = (EditText) findViewById(R.id.prmcode_edittext);
		if (prmcode_edittext.getText() != null)
			creation.setPrmCode(prmcode_edittext.getText().toString().trim());

		creation.setPromoterrequired(promoterrequired_spinner.getSelectedItem()
				.toString());
		creation.setConsumerFinanceAvailable(consumerfinanceavailable_spinner
				.getSelectedItem().toString());
		creation.setOwnerDOB(ownerdob_edittext.getText().toString().trim());
		creation.setLattitude(latitude);
		creation.setLongitude(longitude);
		return creation;

	}

	private void uploadDataServer(SubmitDealerCreationDto inputdata) {

		PostDataToNetwork pdn = new PostDataToNetwork(DealerCreation.this,
				getResources().getString(R.string.loadingmessage),
				new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {

						if (result != null) {
							
							
							ResponseDto obj = new FetchingdataParser(
									DealerCreation.this)
									.getResponseResult(result.toString());


							if (obj.isSuccess()) {
								int dealerCreationID = Integer.parseInt(obj
										.getSingleResult());

								if (dealerCreationID > 0) {

									prepareImagesendArraylist(dealerCreationID);
								}

							} else {

								Helper.showErrorAlertDialog(
										DealerCreation.this,
										getString(R.string.error3),
										getString(R.string.please_contact_admin));
							}

						} else {
							Toast.makeText(
									DealerCreation.this,
									getResources().getString(
											R.string.networkserverbusy),
									Toast.LENGTH_LONG).show();
						}
					}
				});

		JSONObject jsonobj = null;

		DealerCreationfinalRequest creationfinalRequest = new DealerCreationfinalRequest();
		creationfinalRequest.setEmplcode(Helper.getStringValuefromPrefs(this,
				SharedPreferencesKey.PREF_EMPID));
		creationfinalRequest.setCreationDto(inputdata);

		try {
			Gson gson = new Gson();
			String jsonRequest = gson.toJson(creationfinalRequest);
			jsonobj = new JSONObject(jsonRequest);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pdn.setConfig(getString(R.string.url),
				WebConfig.WebMethod.SUBMIT_MDM_DEALER_CREATION);
		pdn.execute(jsonobj);

	}

	protected void prepareImagesendArraylist(int dealerCreationID) {

		final ArrayList<DealerCreationMultiPartUpload> dealerCreationMultiPartUploads = new ArrayList<DealerCreationMultiPartUpload>();
		if (btnview1.getTag() != null) {

			DealerCreationMultiPartUpload creationMultiPartUpload = new DealerCreationMultiPartUpload();
			String imagepath1 = (String) btnview1.getTag();
			// int lastindex = imagepath1.lastIndexOf("/");
			// String imagename = imagepath1.substring(lastindex + 1,
			// imagepath1.length());
			// String finalimagename = imagename.replace(".png", "");
			creationMultiPartUpload.setColumnName("Tinphoto");
			creationMultiPartUpload.setImagePath(imagepath1);
			creationMultiPartUpload.setUserId(Integer.parseInt(Helper
					.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID)));
			creationMultiPartUpload.setDealerCreationId(dealerCreationID);
			dealerCreationMultiPartUploads.add(creationMultiPartUpload);
		}

		if (btnview2.getTag() != null) {

			DealerCreationMultiPartUpload creationMultiPartUpload = new DealerCreationMultiPartUpload();
			String imagepath1 = (String) btnview2.getTag();
			// int lastindex = imagepath1.lastIndexOf("/");
			// String imagename = imagepath1.substring(lastindex + 1,
			// imagepath1.length());
			// String finalimagename = imagename.replace(".png", "");
			creationMultiPartUpload.setColumnName("PanPhoto");
			creationMultiPartUpload.setImagePath(imagepath1);
			creationMultiPartUpload.setUserId(Integer.parseInt(Helper
					.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID)));
			creationMultiPartUpload.setDealerCreationId(dealerCreationID);
			dealerCreationMultiPartUploads.add(creationMultiPartUpload);
		}

		if (btnview3.getTag() != null) {

			DealerCreationMultiPartUpload creationMultiPartUpload = new DealerCreationMultiPartUpload();
			String imagepath1 = (String) btnview3.getTag();
			// int lastindex = imagepath1.lastIndexOf("/");
			// String imagename = imagepath1.substring(lastindex + 1,
			// imagepath1.length());
			// String finalimagename = imagename.replace(".png", "");
			creationMultiPartUpload.setColumnName("GSBPhoto");
			creationMultiPartUpload.setImagePath(imagepath1);
			creationMultiPartUpload.setUserId(Integer.parseInt(Helper
					.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID)));
			creationMultiPartUpload.setDealerCreationId(dealerCreationID);
			dealerCreationMultiPartUploads.add(creationMultiPartUpload);
		}

		if (btnview4.getTag() != null) {

			DealerCreationMultiPartUpload creationMultiPartUpload = new DealerCreationMultiPartUpload();
			String imagepath1 = (String) btnview4.getTag();
			// int lastindex = imagepath1.lastIndexOf("/");
			// String imagename = imagepath1.substring(lastindex + 1,
			// imagepath1.length());
			// String finalimagename = imagename.replace(".png", "");
			creationMultiPartUpload.setColumnName("OwnerPhoto");
			creationMultiPartUpload.setImagePath(imagepath1);
			creationMultiPartUpload.setUserId(Integer.parseInt(Helper
					.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID)));
			creationMultiPartUpload.setDealerCreationId(dealerCreationID);
			dealerCreationMultiPartUploads.add(creationMultiPartUpload);
		}

		if (btnview5.getTag() != null) {

			DealerCreationMultiPartUpload creationMultiPartUpload = new DealerCreationMultiPartUpload();
			String imagepath1 = (String) btnview5.getTag();
			// int lastindex = imagepath1.lastIndexOf("/");
			// String imagename = imagepath1.substring(lastindex + 1,
			// imagepath1.length());
			// String finalimagename = imagename.replace(".png", "");
			creationMultiPartUpload.setColumnName("ContactPersonPhoto");
			creationMultiPartUpload.setImagePath(imagepath1);
			creationMultiPartUpload.setUserId(Integer.parseInt(Helper
					.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID)));
			creationMultiPartUpload.setDealerCreationId(dealerCreationID);
			dealerCreationMultiPartUploads.add(creationMultiPartUpload);
		}

		DealerCreationImageSync creationImageSync = new DealerCreationImageSync(
				this, dealerCreationMultiPartUploads, new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						int statuscode = (Integer) result;

						if (statuscode == HTTPResponseCode.RESPONSE_CODE_OK) {
							for (DealerCreationMultiPartUpload imageData : dealerCreationMultiPartUploads) {
								Helper.deleteFiles(imageData.getImagePath());
								Helper.showAlertDialog(
										DealerCreation.this,
										SSCAlertDialog.SUCCESS_TYPE,
										"Success",
										"data submitted successfully",
										"OK",
										new SSCAlertDialog.OnSDAlertDialogClickListener() {

											@Override
											public void onClick(
													SSCAlertDialog sdAlertDialog) {
												// TODO Auto-generated method
												// stub
												sdAlertDialog.dismiss();
												finish();
											}
										}, false, null, null);

							}
						} else {
							Toast.makeText(DealerCreation.this,
									"Multipart Image Error", 1000).show();

						}

					}
				});
		creationImageSync.execute();

	}

	private int sendImagestoServer(
			String upLoadServerUrl,
			ArrayList<DealerCreationMultiPartUpload> dealerCreationimageUploadData) {

		InputStream fileInputStream = null;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		int uploadStatus = 300;// Fail

		try {
			int count = dealerCreationimageUploadData.size();

			conn = getServerConn(upLoadServerUrl, boundary);
			dos = new DataOutputStream(conn.getOutputStream());
			// //////////////////
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"APIKey\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(Helper.getStringValuefromPrefs(this,
					SharedPreferencesKey.PREF_APIKEY)));
			dos.writeBytes(lineEnd);
			// //////////////////
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"APIToken\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(Helper.getStringValuefromPrefs(this,
					SharedPreferencesKey.PREF_APITOKEN)));
			dos.writeBytes(lineEnd);
			// //////////////////

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"userid\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(dealerCreationimageUploadData.get(0)
					.getUserId()));
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"dealerCreationID\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(dealerCreationimageUploadData.get(0)
					.getDealerCreationId()));
			dos.writeBytes(lineEnd);

			// //////////////////

			for (int i = 0; i < count; i++) {

				DealerCreationMultiPartUpload imageData = dealerCreationimageUploadData
						.get(i);
				String filepath = imageData.getImagePath();
				File sourceFile = new File(filepath);
				String sourcefileName = sourceFile.toString();

				if (!TextUtils.isEmpty(sourcefileName)) {
					if (sourceFile.exists()) {
						try {

							fileInputStream = getStream(sourceFile);
							dos.writeBytes(twoHyphens + boundary + twoHyphens
									+ lineEnd);

							dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
									+ imageData.getColumnName()
									+ "\""
									+ lineEnd);
							dos.writeBytes(lineEnd);

							/* create a buffer of maximum size */
							bytesAvailable = fileInputStream.available();

							bufferSize = Math
									.min(bytesAvailable, maxBufferSize);
							buffer = new byte[bufferSize];

							/* read file and write it into buffer. */
							bytesRead = fileInputStream.read(buffer, 0,
									bufferSize);

							while (bytesRead > 0) {
								/*
								 * for notification on notification window. cal
								 * the byte upload. uploadedBytes =
								 * uploadedBytes+ bytesRead;
								 */
								dos.write(buffer, 0, bufferSize);
								bytesAvailable = fileInputStream.available();
								bufferSize = Math.min(bytesAvailable,
										maxBufferSize);
								bytesRead = fileInputStream.read(buffer, 0,
										bufferSize);

								// Broadcast the Byte of data.
								// broadcastProgress(uploadedBytes, totalBytes);
							}
							// send multipart form data necesssary after file
							// data...
							dos.writeBytes(lineEnd);

						} catch (MalformedURLException ex) {
							uploadStatus = 307;
							return uploadStatus;
						} catch (ProtocolException e) {
							uploadStatus = 306;
							return uploadStatus;
						} catch (IOException e) {
							uploadStatus = 305;
							return uploadStatus;
						} catch (Exception e) {
							uploadStatus = 304;
							return uploadStatus;
						}
					} else {
						uploadStatus = 303;
						System.out.println("--File not exist on SD Card---"
								+ sourcefileName);
						return uploadStatus;
					}
				} else {
					uploadStatus = 302;
					System.out.println("--File Name is empty ---");
					return uploadStatus;
				}
			}
			dos.writeBytes(twoHyphens + boundary + twoHyphens + twoHyphens
					+ lineEnd);
			// Responses from the server message
			int serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			// if (!serverResponseMessage.equalsIgnoreCase("OK")) {
			if (serverResponseCode == HTTPResponseCode.RESPONSE_CODE_OK
					|| serverResponseCode == HTTPResponseCode.RESPONSE_CODE_CREATED
					|| serverResponseCode == HTTPResponseCode.RESPONSE_CODE_ACCEPTED) {
				uploadStatus = HTTPResponseCode.RESPONSE_CODE_OK;
			} else {
				uploadStatus = 308;
				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				return uploadStatus;
			}
			// End of for loop
		} catch (Exception e) {
			uploadStatus = 301;
			return uploadStatus;

		}

		finally {
			try {
				if (fileInputStream != null) {
					closeInputStream(fileInputStream);
				}
				if (dos != null) {
					dos.flush();
					dos.close();
				}
			} catch (Exception e2) {
				Helper.printStackTrace(e2);
			}
		}

		return uploadStatus;

	}

	// private boolean checkValidation() {
	//
	// if (reasonofappointmentspinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_reason_of_appontment));
	// return false;
	// }
	//
	// if (firm_type_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_firm_type));
	// return false;
	// }
	//
	// if (TextUtils.isEmpty(firm_name_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_firm_name));
	// return false;
	// }
	//
	// if (district_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_district));
	// return false;
	// }
	//
	// if (city_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_city));
	// return false;
	// }
	//
	// /*
	// * if (pin_spinner.getSelectedItemPosition() == 0) {
	// * Helper.showErrorAlertDialog(DealerCreation.this,
	// * getString(R.string.error3), getString(R.string.please_select_pin));
	// * return false; }
	// */
	//
	// if (parent_company_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_parent_company));
	// return false;
	// }
	// if (TextUtils.isEmpty(street_name_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_street_name));
	// return false;
	// }
	//
	// EditText firm_mobno_edittext = (EditText)
	// findViewById(R.id.firm_mobno_edittext);
	//
	// if (TextUtils.isEmpty(firm_mobno_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_firm_mobile));
	// return false;
	// }
	// if (TextUtils.isEmpty(firm_email_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_firm_email));
	// return false;
	// }
	// if (dealer_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_dealer_type));
	// return false;
	// }
	//
	// // if (parentdealerLayout.getVisibility() == View.VISIBLE) {
	// // if (parentdealer_spinner.getSelectedItemPosition() == 0) {
	// // Helper.showErrorAlertDialog(DealerCreation.this,
	// // getString(R.string.error3),
	// // getString(R.string.please_select_parent_dealer_code));
	// // return false;
	// // }
	// // }
	// if (TextUtils.isEmpty(pan_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_pan));
	// return false;
	// }
	//
	// if (TextUtils.isEmpty(tin_name_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_tin));
	// return false;
	// }
	//
	// if (TextUtils.isEmpty(owner_name_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_owner_name));
	// return false;
	// }
	// if (TextUtils.isEmpty(ownermob_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_owner_mob));
	// return false;
	// }
	// if (TextUtils.isEmpty(contactperson_name_edittext.getText().toString()))
	// {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_contact_person_name));
	// return false;
	// }
	// if (TextUtils.isEmpty(contactperson_mob_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_contact_person_mob));
	// return false;
	// }
	// if (dayoff_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_day_off));
	// return false;
	// }
	// if (TextUtils.isEmpty(totalcountersize_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_counter_size));
	// return false;
	// }
	// if (!TextUtils.isEmpty(tin_name_edittext.getText().toString())) {
	// if (btnview1.getTag() == null) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_take_tin_photo));
	// return false;
	// }
	// }
	//
	// if (!TextUtils.isEmpty(pan_edittext.getText().toString())) {
	// if (btnview2.getTag() == null) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_take_pan_photo));
	// return false;
	// }
	// }
	//
	// if (btnview3.getTag() == null) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_take_gsb_photo));
	// return false;
	// }
	//
	// if (promoterrequired_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_promoter_required));
	// return false;
	// }
	//
	// if (consumerfinanceavailable_spinner.getSelectedItemPosition() == 0) {
	// Helper.showErrorAlertDialog(
	// DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_select_consumer_finance_available));
	// return false;
	// }
	//
	// if (TextUtils.isEmpty(ownerdob_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// getString(R.string.please_fill_owner_dob));
	// return false;
	// }
	//
	// if (!isValidEmail(firm_email_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Please enter valid Firm Email address");
	// return false;
	// }
	// if (firm_mobno_edittext.getText().toString().length() < 10) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Minimum length of Firm Mobile number is 10");
	// return false;
	// }
	// if (ownermob_edittext.getText().toString().length() < 10) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Minimum length of Owner Mobile number is 10");
	// return false;
	// }
	// if (contactperson_mob_edittext.getText().toString().length() < 10) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Minimum length of Contact Person Mobile number is 10");
	// return false;
	// }
	//
	// EditText landlineno_edittext = (EditText)
	// findViewById(R.id.landlineno_edittext);
	// if (landlineno_edittext.getText().toString().length() < 10) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Minimum length of Landline number is 10");
	// return false;
	// }
	//
	// return true;
	//
	// }

	private boolean checkValidation() {

		if (reasonofappointmentspinner.getSelectedItemPosition() == 0) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_select_reason_of_appontment));
			// hideKeyboard();
			// reasonofappointmentspinner.setFocusable(true);
			// reasonofappointmentspinner.setFocusableInTouchMode(true);
			// reasonofappointmentspinner.requestFocus();
			return false;
		}

		if (firm_type_spinner.getSelectedItemPosition() == 0) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_select_firm_type));
			return false;
		}

		if (TextUtils.isEmpty(firm_name_edittext.getText().toString().trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_firm_name));
			firm_name_edittext.requestFocus();
			return false;
		}

		try {
			if (district_spinner.getSelectedItemPosition() == 0
					|| district_spinner.getSelectedItemPosition() == -1) {
				Helper.showErrorAlertDialog(DealerCreation.this,
						getString(R.string.error3),
						getString(R.string.please_select_district));
				return false;
			}

			if (city_spinner.getSelectedItemPosition() == 0
					|| city_spinner.getSelectedItemPosition() == -1) {
				Helper.showErrorAlertDialog(DealerCreation.this,
						getString(R.string.error3),
						getString(R.string.please_select_city));
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Helper.showErrorAlertDialog(
					DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.district_not_available_please_contact_admin));
			return false;
		}

		// if (pin_spinner.getSelectedItemPosition() == 0) {
		// Helper.showErrorAlertDialog(DealerCreation.this,
		// getString(R.string.error3),
		// getString(R.string.please_select_pin));
		// return false;
		// }

		if (TextUtils.isEmpty(street_name_edittext.getText().toString().trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_street_name));
			street_name_edittext.requestFocus();
			return false;
		}

		EditText landlineno_edittext = (EditText) findViewById(R.id.landlineno_edittext);
		if (landlineno_edittext.getText().toString().length() < 8
				&& !TextUtils.isEmpty(landlineno_edittext.getText().toString()
						.trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					"Please enter Valid Landline No.");
			landlineno_edittext.requestFocus();
			return false;
		}

		EditText firm_mobno_edittext = (EditText) findViewById(R.id.firm_mobno_edittext);

		if (TextUtils.isEmpty(firm_mobno_edittext.getText().toString().trim())
				|| firm_mobno_edittext.getText().toString().length() < 10) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					"Please enter Valid firm Mobile No.");
			firm_mobno_edittext.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(firm_email_edittext.getText().toString().trim())
				|| !isValidEmail(firm_email_edittext.getText().toString())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					"Please enter Valid firm Email.");
			firm_email_edittext.requestFocus();
			return false;
		}

		try {
			if (parent_company_spinner.getSelectedItemPosition() == 0
					|| parent_company_spinner.getSelectedItemPosition() == -1) {
				Helper.showErrorAlertDialog(DealerCreation.this,
						getString(R.string.error3),
						getString(R.string.please_select_parent_company));
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Helper.showErrorAlertDialog(
					DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.parentcompany_not_available_please_contact_admin));
			return false;
		}
		if (dealer_spinner.getSelectedItemPosition() == 0) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_select_dealer_type));
			return false;
		}

		// if (parentdealerLayout.getVisibility() == View.VISIBLE) {
		// if (parentdealer_spinner.getSelectedItemPosition() == 0) {
		// Helper.showErrorAlertDialog(DealerCreation.this,
		// getString(R.string.error3),
		// getString(R.string.please_select_parent_dealer_code));
		// return false;
		// }
		// }
		if (TextUtils.isEmpty(pan_edittext.getText().toString().trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_pan));
			pan_edittext.requestFocus();
			return false;
		}

		if (TextUtils.isEmpty(tin_name_edittext.getText().toString().trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_tin));
			tin_name_edittext.requestFocus();
			return false;
		}

		if (TextUtils.isEmpty(owner_name_edittext.getText().toString().trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_owner_name));
			owner_name_edittext.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(ownermob_edittext.getText().toString().trim())
				|| ownermob_edittext.getText().toString().length() < 10) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					"Please enter Valid Owner Mobile No.");
			ownermob_edittext.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(contactperson_name_edittext.getText().toString()
				.trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_contact_person_name));
			contactperson_name_edittext.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(contactperson_mob_edittext.getText().toString()
				.trim())
				|| contactperson_mob_edittext.getText().toString().length() < 10) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					"Please enter Valid Contact Person Mobile No.");
			contactperson_mob_edittext.requestFocus();
			return false;
		}
		if (dayoff_spinner.getSelectedItemPosition() == 0) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_select_day_off));
			return false;
		}
		if (TextUtils.isEmpty(totalcountersize_edittext.getText().toString()
				.trim())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_counter_size));
			totalcountersize_edittext.requestFocus();
			return false;
		}
		if (!TextUtils.isEmpty(tin_name_edittext.getText().toString().trim())) {
			if (btnview1.getTag() == null) {
				Helper.showErrorAlertDialog(DealerCreation.this,
						getString(R.string.error3),
						getString(R.string.please_take_tin_photo));
				return false;
			}
		}

		if (!TextUtils.isEmpty(pan_edittext.getText().toString().trim())) {
			if (btnview2.getTag() == null) {
				Helper.showErrorAlertDialog(DealerCreation.this,
						getString(R.string.error3),
						getString(R.string.please_take_pan_photo));
				return false;
			}
		}

		if (btnview3.getTag() == null) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_take_gsb_photo));
			return false;
		}

		if (promoterrequired_spinner.getSelectedItemPosition() == 0) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_select_promoter_required));
			return false;
		}

		if (consumerfinanceavailable_spinner.getSelectedItemPosition() == 0) {
			Helper.showErrorAlertDialog(
					DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_select_consumer_finance_available));
			return false;
		}

		if (TextUtils.isEmpty(ownerdob_edittext.getText().toString())) {
			Helper.showErrorAlertDialog(DealerCreation.this,
					getString(R.string.error3),
					getString(R.string.please_fill_owner_dob));
			ownerdob_edittext.requestFocus();
			return false;
		}
		return true;
	}

	// if (!isValidEmail(firm_email_edittext.getText().toString())) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Please enter valid Firm Email address");
	// return false;
	// }
	// if (firm_mobno_edittext.getText().toString().length() < 10) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Minimum length of Firm Mobile number is 10");
	// return false;
	// }
	// if (ownermob_edittext.getText().toString().length() < 10) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Minimum length of Owner Mobile number is 10");
	// return false;
	// }
	// if (contactperson_mob_edittext.getText().toString().length() < 10) {
	// Helper.showErrorAlertDialog(DealerCreation.this,
	// getString(R.string.error3),
	// "Minimum length of Contact Person Mobile number is 10");
	// return false;
	// }

	public final static boolean isValidEmail(CharSequence target) {

		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == REQUEST_CODE && arg1 == RESULT_OK) {
			handleCameraRequest();
		}
	}

	private HttpURLConnection getServerConn(String upLoadServerUrl,
			String boundary) throws MalformedURLException, IOException,
			ProtocolException {
		HttpURLConnection conn;
		// open a URL connection to the server
		URL url = new URL(getString(R.string.url) + upLoadServerUrl);
		// Open a HTTP connection to the URL
		conn = (HttpURLConnection) url.openConnection();

		conn.setDoInput(true); // Allow Inputs
		conn.setDoOutput(true); // Allow Outputs
		conn.setUseCaches(false); // Don't use a Cached Copy
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);

		return conn;
	}

	private final InputStream getStream(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	private void closeInputStream(final InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception exc) {
			}
		}
	}

	private void handleCameraRequest() {
		ImageView imageView = (ImageView) findViewById(viewParentid);
		Bitmap optimizedBitmap = loadOptimizedBitmap(imagepath,
				ImageSize.QUESTIONNAIRE_IMAGE_WIDTH,
				ImageSize.QUESTIONNAIRE_IMAGE_HEIGHT);
		if (optimizedBitmap != null) {
			imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(
					optimizedBitmap,
					ImageSize.QUESTIONNAIRE_IMAGE_WIDTH_THUMBNAIL,
					ImageSize.QUESTIONNAIRE_IMAGE_HEIGHT_THUMBNAIL));
			imageView.setTag(imagepath);
			cameraView.setVisibility(View.GONE);
			editview.setVisibility(View.VISIBLE);
			imageView.setEnabled(true);

		} else {
			imageView.setTag(null);
		}
	};

	public class DealerCreationImageSync extends AsyncTask<Void, Void, Void> {

		private Context context;
		private GetDataCallBack callBack;
		private ArrayList<DealerCreationMultiPartUpload> dealerCreationMultiPartUploads;
		private ProgressDialog progressDialog;

		private int statuscode;

		private void setProgresDialogProperties(Context context) {

			try {
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					/*
					 * progressDialog = new ProgressDialog(context,
					 * ProgressDialog.THEME_HOLO_LIGHT);
					 */

					progressDialog = SSCProgressDialog.ctor(context);

				} else {
					progressDialog = new ProgressDialog(context);
					progressDialog.setProgress(0);
					progressDialog.setMax(100);
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setMessage("Uploading Images");
					progressDialog.setCancelable(false);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public DealerCreationImageSync(
				Context context,
				ArrayList<DealerCreationMultiPartUpload> dealerCreationMultiPartUploads,
				GetDataCallBack callBack) {
			this.context = context;
			this.callBack = callBack;
			this.dealerCreationMultiPartUploads = dealerCreationMultiPartUploads;
			setProgresDialogProperties(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			statuscode = sendImagestoServer(
					WebConfig.WebMethod.UPLOAD_MDM_DEALER_CREATIONIMAGE,
					dealerCreationMultiPartUploads);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				progressDialog.dismiss();
			} catch (Exception e) {
				Helper.printStackTrace(e);
			}
			if (callBack != null) {
				callBack.processResponse(statuscode);
			}
		}

	}

	private void getCurrentLocation() {

		myloc = new MyLocation();

		LocationResult locationRes = new LocationResult() {

			@Override
			public void gotLocation(Location location) {

				if (location != null) {
					if (!isfirsttime) {
						isfirsttime = true;
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				}

			}
		};
		myloc.getLocation(DealerCreation.this, locationRes);
	}

	public static Bitmap loadOptimizedBitmap(String path, int desireWidth,
			int desireHeight) {
		Bitmap scaledBitmap = null;
		try {
			// Part 1: Decode image
			Bitmap rountAboutScaledBitmap = decodeFile(path, desireWidth,
					desireHeight, ScalingLogic.FIT);

			// if (!(unscaledBitmap.getWidth() <= desireWidth &&
			// unscaledBitmap.getHeight() <= desireHeight)) {
			if (rountAboutScaledBitmap.getWidth() > desireWidth
					&& rountAboutScaledBitmap.getHeight() > desireHeight) {
				// Part 2: Scale Down the image
				scaledBitmap = createScaledBitmap(rountAboutScaledBitmap,
						desireWidth, desireHeight, ScalingLogic.FIT);
			} else {
				return compressBitmapImage(path, rountAboutScaledBitmap);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return compressBitmapImage(path, scaledBitmap);
	}

	public static Bitmap compressBitmapImage(String path,
			Bitmap scaledOrUnscaledBitmap) {
		try {
			File f = new File(path);
			// String strMyImagePath = f.getAbsolutePath();
			FileOutputStream fos = new FileOutputStream(f);
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

	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {

		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),
				dstRect.height(), Config.RGB_565);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(
				Paint.FILTER_BITMAP_FLAG));

		return scaledBitmap;
	}

	public static Rect calculateSrcRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.CROP) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				final int srcRectWidth = (int) (srcHeight * dstAspect);
				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
						srcHeight);
			} else {
				final int srcRectHeight = (int) (srcWidth / dstAspect);
				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
				return new Rect(0, scrRectTop, srcWidth, scrRectTop
						+ srcRectHeight);
			}
		} else {
			return new Rect(0, 0, srcWidth, srcHeight);
		}
	}

	public static Rect calculateDstRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			} else {
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		} else {
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}

	public static Bitmap decodeFile(String path, int dstWidth, int dstHeight,
			ScalingLogic scalingLogic) {

		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;

		options.inSampleSize = calculateSampleSize(options.outWidth,
				options.outHeight, dstWidth, dstHeight, scalingLogic);

		Bitmap rountAboutScaledBitmap = null;
		try {
			Bitmap rountAboutScaledBitmapTemp = BitmapFactory.decodeFile(path,
					options);
			rountAboutScaledBitmap = rotateImageAccordingToEXIF(
					rountAboutScaledBitmapTemp, path);
			// unscaledBitmap.recycle();

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			Helper.printStackTrace(e);
			;
		}
		return rountAboutScaledBitmap;
	}

	public static Bitmap rotateImageAccordingToEXIF(Bitmap bm, String image_path) {
		ExifInterface ei = null;
		try {
			ei = new ExifInterface(image_path);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				bm = rotateBitmap(bm, 90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				bm = rotateBitmap(bm, 180);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static int calculateSampleSize(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {

		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcWidth / dstWidth;
			} else {
				return srcHeight / dstHeight;
			}
		} else {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcHeight / dstHeight;
			} else {
				return srcWidth / dstWidth;
			}
		}
	}

	public static Bitmap rotateBitmap(Bitmap source, float angle) {
		try {
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			Bitmap b = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), matrix, true);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String formatDate(int year, int month, int day) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		;
		cal.set(year, month, day);
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

		return sdf.format(date);
	}
}
