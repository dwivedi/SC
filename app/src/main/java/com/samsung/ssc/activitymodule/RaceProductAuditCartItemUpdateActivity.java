package com.samsung.ssc.activitymodule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceFixtureMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMDataResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceProductAuditResponseMasterColumns;
import com.samsung.ssc.dto.RaceFixtureDataModal;
import com.samsung.ssc.dto.RacePOSMMasterDTO;
import com.samsung.ssc.dto.RacePoductAuditResponseDTO;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

public class RaceProductAuditCartItemUpdateActivity extends BaseActivity {

	private EditText spDisplayArea;
	private EditText spSubDisplayArea;
	private EditText spWallNumber;
	private EditText spRowNumber;
	private EditText spBrand;

	private EditText spProductSticker;

	private CheckBox cbPriceTag;
	private CheckBox cbRaceTopper;
	private CheckBox cbOnTag;

	private TextView tvRaceProducct;
	private int mFixtureID, brandID;

	private RaceFixtureDataModal mSelectedraceFixtureDataTemp = null;

	private RacePoductAuditResponseDTO mResponseFromDataBase;

	private View mllWallContainer, mllRowContainter, mllBrandContainter;

	private Map<Integer, String> mRaceBrandNameData;

	private ProgressDialog progressDialog;

	private ArrayList<RaceFixtureDataModal> mRaceFixtureData;

	private int stockAuditID;

	private ContentResolver contentResolver;

	protected void setProgresDialogProperties(String message) {

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			/*
			 * progressDialog = new ProgressDialog(context,
			 * ProgressDialog.THEME_HOLO_LIGHT);
			 */

			progressDialog = SSCProgressDialog
					.ctor(RaceProductAuditCartItemUpdateActivity.this);

		} else {
			progressDialog = new ProgressDialog(
					RaceProductAuditCartItemUpdateActivity.this);
			progressDialog.setProgress(0);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(message);
			progressDialog.setCancelable(false);
		}

	}

	@Override
	public void init() {
		super.init();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		contentResolver=getContentResolver();
		setContentView(R.layout.product_audit_cart_item_update);

		setUpView();

	}

	public void onUpdateProductClick(View view) {

		if (!validate()) {
			return;
		}

		// Acticity Id
		String wallNumber = spWallNumber.getText().toString();
		String rowNumber = spRowNumber.getText().toString();

		boolean bolTopper = cbRaceTopper.isChecked();
		boolean bolOn = cbOnTag.isChecked();
		boolean bolPriceTag = cbPriceTag.isChecked();

		JSONObject productDetailsJSONObject = new JSONObject();
		try {

			if (mllWallContainer.getVisibility() == View.VISIBLE) {
				productDetailsJSONObject.put("WallNumber",
						Integer.parseInt(wallNumber));
			} else {
				productDetailsJSONObject.put("WallNumber", -1);
			}
			if (mllRowContainter.getVisibility() == View.VISIBLE) {
				productDetailsJSONObject.put("RowNumber",
						Integer.parseInt(rowNumber));
			} else {
				productDetailsJSONObject.put("RowNumber", -1);
			}
			if (mllBrandContainter.getVisibility() == View.VISIBLE) {
				productDetailsJSONObject.put("BrandId", brandID);
			} else {
				productDetailsJSONObject.put("BrandId", -1);
			}

			productDetailsJSONObject.put("BolTopper", bolTopper);
			productDetailsJSONObject.put("BolOn", bolOn);
			productDetailsJSONObject.put("BolPriceTag", bolPriceTag);

			productDetailsJSONObject.put("FixtureId", mFixtureID);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		updateProductAuditData(productDetailsJSONObject,
				new UpdateProductAuditHandler(this));

	}

	static class UpdateProductAuditHandler extends Handler {

		WeakReference<RaceProductAuditCartItemUpdateActivity> updateActivity;

		public UpdateProductAuditHandler(
				RaceProductAuditCartItemUpdateActivity updateActivity) {

			this.updateActivity = new WeakReference<RaceProductAuditCartItemUpdateActivity>(
					updateActivity);
		}

		@Override
		public void handleMessage(Message msg) {

			boolean isSuccess = msg.getData().getBoolean("is_success");
			final RaceProductAuditCartItemUpdateActivity activityRefrence = updateActivity
					.get();
			if (activityRefrence!=null) {
				
			
			if (activityRefrence.progressDialog != null
					&& activityRefrence.progressDialog.isShowing()) {
				activityRefrence.progressDialog.dismiss();
			}

			if (isSuccess) {

				Helper.showAlertDialog(
						activityRefrence,
						SSCAlertDialog.SUCCESS_TYPE,
						activityRefrence.getString(R.string.sucess),
						activityRefrence
								.getString(R.string.updated_successfully),
						activityRefrence.getString(R.string.ok),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {

								sdAlertDialog.dismiss();
								activityRefrence.finish();

							}
						}, false, null, null);

			} else {
				Helper.showAlertDialog(
						activityRefrence,
						SSCAlertDialog.ERROR_TYPE,
						activityRefrence.getString(R.string.error7),
						activityRefrence
								.getString(R.string.request_not_successful),
						activityRefrence.getString(R.string.ok),
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
	};

	private void updateProductAuditData(
			final JSONObject productDetailsJSONObject, final Handler handler) {

		setProgresDialogProperties(getString(R.string.updating));
		progressDialog.show();

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

	/*			boolean isProductUpdateSuccess = DatabaseHelper.getConnection(
						getApplicationContext()).updateRaceProductAudit(
						productDetailsJSONObject, stockAuditID);*/

				String whereClause = RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
						+ "=?";
				String[] whereArgs = new String[] { String
						.valueOf(stockAuditID) };
				
				int no_of_audit_rows_updated = getContentResolver()
						.update(ProviderContract.URI_RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID,
								DatabaseUtilMethods
										.getContentValueUpdateForRaceProductAuditResponseFromJSON(productDetailsJSONObject),
								whereClause, whereArgs);
				
				
				

				/*
				 * String whereClause =
				 * RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID +
				 * "=?"; String[] whereArgs = new String[] { String
				 * .valueOf(stockAuditID) };
				 * 
				 * contentResolver .update(ProviderContract.
				 * URI_RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID,
				 * DatabaseUtilMethods
				 * .getContentValueForRaceProductAuditResponseFromJSON
				 * (stockAuditID, productDetailsJSONObject), whereClause,
				 * whereArgs);
				 */

			/*	boolean isPosmUpdateSuccess = DatabaseHelper.getConnection(
						getApplicationContext())
						.insertRaceProductAuditPOSMResponse(
								mResponseFromDataBase.activittyID,
								stockAuditID, mResponseFromDataBase.al_posm);*/
				
				// delete previous value
				
				int no_of_posm_rows_updated = 0;
				contentResolver.delete(ProviderContract.URI_RACE_POSM_RESPONSE, RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID + " = ?", new String[] { stockAuditID + "" });
				
				ContentValues[] cv =DatabaseUtilMethods.getContentValuesPOSMResponseFromJSON(mResponseFromDataBase.activittyID, stockAuditID, mResponseFromDataBase.al_posm);
				if(cv.length>0)
				{
					no_of_posm_rows_updated=contentResolver.bulkInsert(ProviderContract.URI_RACE_POSM_RESPONSE,cv);
				}
				
				

				Message msg = handler.obtainMessage();
				Bundle bundle = new Bundle();

				if (no_of_audit_rows_updated>0 && no_of_posm_rows_updated!=-1) {
					bundle.putBoolean("is_success", true);
				} else {
					bundle.putBoolean("is_success", false);
				}

				msg.setData(bundle);
				handler.sendMessage(msg);

			}
		});

		thread.start();
	}

	public void onCancelClick(View view) {
		finish();
	}

	private void setUpView() {

		tvRaceProducct = (TextView) findViewById(R.id.tvRaceProduct);

		spDisplayArea = (EditText) findViewById(R.id.etProductDisplayArea);
		spSubDisplayArea = (EditText) findViewById(R.id.etProductSubDisplayArea);

		spWallNumber = (EditText) findViewById(R.id.etProductWallNumber);
		spRowNumber = (EditText) findViewById(R.id.etProductRowNumber);
		spBrand = (EditText) findViewById(R.id.etProductBrand);

		spProductSticker = (EditText) findViewById(R.id.etProductSticker);

		cbRaceTopper = (CheckBox) findViewById(R.id.cbRaceTopper);
		cbOnTag = (CheckBox) findViewById(R.id.cbRaceOn);
		cbPriceTag = (CheckBox) findViewById(R.id.cbRacePriceTag);

		mllWallContainer = findViewById(R.id.ll_wallContainer_productAuditDetail);
		mllRowContainter = findViewById(R.id.ll_rowContainer_productAuditDetail);
		mllBrandContainter = findViewById(R.id.ll_brandContainer_productAuditDetail);

		getBundleValue();

	}

	private void initializeView() {

		if (mResponseFromDataBase.rowNumber != -1) {
			mllRowContainter.setVisibility(View.VISIBLE);
			spRowNumber.setText(mResponseFromDataBase.rowNumber + "");

		}
		if (mResponseFromDataBase.wallNumber != -1) {
			mllWallContainer.setVisibility(View.VISIBLE);
			spWallNumber.setText(mResponseFromDataBase.wallNumber + "");
		}

		this.brandID = mResponseFromDataBase.brandID;
		this.mFixtureID = mResponseFromDataBase.fixtureID;

		if (brandID != -1) {
			mllBrandContainter.setVisibility(View.VISIBLE);
			if (mRaceBrandNameData != null) {
				String brandName = mRaceBrandNameData.get(brandID);
				spBrand.setText(brandName);
			}
		}

		spDisplayArea.setText(mResponseFromDataBase.category);
		spSubDisplayArea.setText(mResponseFromDataBase.subCategory);

		ArrayList<RacePOSMMasterDTO> al_posm_data = mResponseFromDataBase.al_posm;
		int count = al_posm_data.size();

		ArrayList<String> posmName = new ArrayList<String>();
		for (int i = 0; i < count; i++) {

			RacePOSMMasterDTO posm = al_posm_data.get(i);
			if (posm.isStickerSelected()) {
				posmName.add(posm.getPOSMName());
			}

		}
		String posmNames;
		if (!posmName.isEmpty()) {
			posmNames = TextUtils.join(",", posmName);
		} else {
			posmNames = "";
		}
		spProductSticker.setText(posmNames);

		if (mResponseFromDataBase.isPriceTag) {
			cbPriceTag.setChecked(true);
		}
		if (mResponseFromDataBase.isSwithcedOn) {
			cbOnTag.setChecked(true);
		}
		if (mResponseFromDataBase.isTopperAvailble) {
			cbRaceTopper.setChecked(true);
		}

		spDisplayArea.setOnClickListener(new View.OnClickListener() {

			private AlertDialog alert;

			@Override
			public void onClick(View v) {

				/*
				 * ArrayList<String> raceCategoryData = DatabaseHelper
				 * .getConnection(getApplicationContext()) .getCategoryData();
				 */

				Cursor cursor = contentResolver
						.query(ProviderContract.URI_RACE_FIXTURE,
								new String[] { "Distinct "
										+ RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME },
								null, null, null);

				if (cursor == null) {
					return;
				}

				final ArrayList<String> raceCategoryData = DatabaseUtilMethods
						.getRaceCategoryListFromCursor(cursor);
				cursor.close();

				final AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this);
				}

				builder.setTitle(R.string.display_area);
				int selectedIndex = -1;

				final String[] stockArr = new String[raceCategoryData.size()];
				raceCategoryData.toArray(stockArr);

				String selectedValue = spDisplayArea.getText().toString();

				if (!selectedValue.equalsIgnoreCase("")) {

					selectedIndex = raceCategoryData.indexOf(selectedValue);
				}

				builder.setSingleChoiceItems(stockArr, selectedIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String selectedItem = stockArr[which]
										.toString();
								spDisplayArea.setText(selectedItem);
								spSubDisplayArea.setText("");

								/*
								 * mRaceFixtureData = DatabaseHelper
								 * .getConnection(getApplicationContext())
								 * .getSubCategoryData(selectedItem);
								 */

								String projection[] = {
										RaceFixtureMasterColumns.KEY_FIXTURE_ID,
										RaceFixtureMasterColumns.KEY_FIXTURE_SUB_CATEGORY,
										RaceFixtureMasterColumns.KEY_FIXTURE_PRODUCT_GROUPS,
										RaceFixtureMasterColumns.KEY_FIXTURE_IS_COMPETITOR_AVAILBALE,
										RaceFixtureMasterColumns.KEY_FIXTURE_IS_ROW_AVAILABLE,
										RaceFixtureMasterColumns.KEY_FIXTURE_IS_WALL_AVAILABLE

								};

								Cursor cursor = contentResolver
										.query(ProviderContract.URI_RACE_FIXTURE,
												projection,
												RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME
														+ "=?",
												new String[] { selectedItem },
												null);

								if (cursor == null) {
									return;
								}

								mRaceFixtureData = DatabaseUtilMethods
										.getRaceFixtureDataModelListFromCursor(cursor);
								cursor.close();

								mllBrandContainter.setVisibility(View.GONE);
								mllRowContainter.setVisibility(View.GONE);
								mllWallContainer.setVisibility(View.GONE);

								alert.dismiss();
							}
						});

				alert = builder.create();
				alert.show();
			}
		});

		spSubDisplayArea.setOnClickListener(new View.OnClickListener() {

			private AlertDialog alert;

			@Override
			public void onClick(View v) {

				if (mRaceFixtureData == null) {

					/*
					 * mRaceFixtureData = DatabaseHelper.getConnection(
					 * getApplicationContext()).getSubCategoryData(
					 * spDisplayArea.getText().toString());
					 */

					String projection[] = {
							RaceFixtureMasterColumns.KEY_FIXTURE_ID,
							RaceFixtureMasterColumns.KEY_FIXTURE_SUB_CATEGORY,
							RaceFixtureMasterColumns.KEY_FIXTURE_PRODUCT_GROUPS,
							RaceFixtureMasterColumns.KEY_FIXTURE_IS_COMPETITOR_AVAILBALE,
							RaceFixtureMasterColumns.KEY_FIXTURE_IS_ROW_AVAILABLE,
							RaceFixtureMasterColumns.KEY_FIXTURE_IS_WALL_AVAILABLE

					};

					Cursor cursor = contentResolver.query(
							ProviderContract.URI_RACE_FIXTURE, projection,
							RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME
									+ "=?", new String[] { spDisplayArea
									.getText().toString() }, null);

					if (cursor == null) {
						return;
					}

					mRaceFixtureData = DatabaseUtilMethods
							.getRaceFixtureDataModelListFromCursor(cursor);
					cursor.close();

				}

				int count = mRaceFixtureData.size();
				final String[] fixtureSubCategoryArray = new String[count];

				for (int i = 0; i < count; i++) {
					RaceFixtureDataModal fixtureData = mRaceFixtureData.get(i);
					fixtureSubCategoryArray[i] = fixtureData.getSubCategory();
				}

				final AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this);
				}

				builder.setTitle(R.string.sub_display_area);
				int selectedIndex = -1;

				String selectedValue = spSubDisplayArea.getText().toString();

				if (!selectedValue.equals("")) {

					selectedIndex = Arrays.asList(fixtureSubCategoryArray)
							.indexOf(selectedValue);
				}

				builder.setSingleChoiceItems(fixtureSubCategoryArray,
						selectedIndex, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String selectedItem = fixtureSubCategoryArray[which]
										.toString();
								spSubDisplayArea.setText(selectedItem);
								mFixtureID = mRaceFixtureData.get(which)
										.getFixtureID();

								mSelectedraceFixtureDataTemp = mRaceFixtureData
										.get(which);
						

								if (mSelectedraceFixtureDataTemp != null) {
									
									spSubDisplayArea
									.setTag(mSelectedraceFixtureDataTemp
											.getFixtureID());
									
									if (mSelectedraceFixtureDataTemp
											.isCompetitorAvailable()
											&& mllBrandContainter
													.getVisibility() == View.GONE) {
										mllBrandContainter
												.setVisibility(View.VISIBLE);

									} else if (!mSelectedraceFixtureDataTemp
											.isCompetitorAvailable()
											&& mllBrandContainter
													.getVisibility() == View.VISIBLE) {
										mllBrandContainter
												.setVisibility(View.GONE);

									}

									if (mSelectedraceFixtureDataTemp
											.isRowAvailable()
											&& mllRowContainter.getVisibility() == View.GONE) {
										mllRowContainter
												.setVisibility(View.VISIBLE);
									} else if (!mSelectedraceFixtureDataTemp
											.isRowAvailable()
											&& mllRowContainter.getVisibility() == View.VISIBLE) {
										mllRowContainter
												.setVisibility(View.GONE);
									}
									if (mSelectedraceFixtureDataTemp
											.isWallAvailable()
											&& mllWallContainer.getVisibility() == View.GONE) {
										mllWallContainer
												.setVisibility(View.VISIBLE);
									} else if (!mSelectedraceFixtureDataTemp
											.isWallAvailable()
											&& mllWallContainer.getVisibility() == View.VISIBLE) {
										mllWallContainer
												.setVisibility(View.GONE);
									}
								}

								alert.dismiss();
							}
						});

				alert = builder.create();
				alert.show();
			}
		});

		spWallNumber.setOnClickListener(new View.OnClickListener() {

			private AlertDialog alert;

			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this);
				}
				builder.setTitle(R.string.wall_number);
				int selectedIndex = -1;

				final String wallArr[] = { "1", "2", "3", "4", "5" };

				String selectedValue = spWallNumber.getText().toString();
				if (!selectedValue.equals("")) {
					selectedIndex = Integer.parseInt(selectedValue);
					selectedIndex = selectedIndex - 1;
				}

				builder.setSingleChoiceItems(wallArr, selectedIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String selectedItem = wallArr[which].toString();
								spWallNumber.setText(selectedItem);

								alert.dismiss();
							}
						});
				alert = builder.create();
				alert.show();
			}
		});

		spRowNumber.setOnClickListener(new View.OnClickListener() {

			private AlertDialog alert;

			@Override
			public void onClick(View v) {

				final AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this);
				}
				builder.setTitle(R.string.row_number);
				final String rowArr[] = { "1", "2", "3", "4", "5" };

				int selectedIndex = -1;

				String selectedValue = spRowNumber.getText().toString();

				if (!selectedValue.equals("")) {
					selectedIndex = Integer.parseInt(selectedValue);
					selectedIndex = selectedIndex - 1;
				}

				builder.setSingleChoiceItems(rowArr, selectedIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String selectedItem = rowArr[which].toString();
								spRowNumber.setText(selectedItem);

								alert.dismiss();
							}
						});

				alert = builder.create();
				alert.show();
			}
		});

		spBrand.setOnClickListener(new View.OnClickListener() {

			private AlertDialog alert;

			@Override
			public void onClick(View v) {

				if (mRaceBrandNameData == null) {

					/*
					 * mRaceBrandNameData = DatabaseHelper.getConnection(
					 * getApplicationContext()).getBrandData();
					 */

					String projection[] = {
							RaceBrandMasterColumns.KEY_BRAND_NAME,
							RaceBrandMasterColumns.KEY_BRAND_ID };

					Cursor cursor = contentResolver.query(
							ProviderContract.URI_RACE_BRAND, projection, null,
							null, null);

					if (cursor == null) {
						return;
					}

					mRaceBrandNameData = DatabaseUtilMethods
							.getRaceBrandFromCursor(cursor);
					cursor.close();

				}

				final AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditCartItemUpdateActivity.this);
				}
				builder.setTitle(R.string.brand);
				int selectedIndex = -1;

				final String[] brandNameArr = mRaceBrandNameData.values()
						.toArray(new String[mRaceBrandNameData.size()]);

				String selectedValue = spBrand.getText().toString();

				if (!selectedValue.equals("")) {
					selectedIndex = Arrays.asList(brandNameArr).indexOf(
							selectedValue);
				}

				builder.setSingleChoiceItems(brandNameArr, selectedIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								String selectedItem = brandNameArr[which]
										.toString();
								spBrand.setText("");
								spBrand.setText(selectedItem);

								Object key = Helper.getKeyFromValue(
										mRaceBrandNameData, selectedItem);
								if (key != null) {
									brandID = (Integer) key;
								}

								alert.dismiss();
							}
						});

				alert = builder.create();
				alert.show();
			}
		});

		spProductSticker.setOnClickListener(new View.OnClickListener() {

			private AlertDialog alert;

			@Override
			public void onClick(View v) {

				if (mResponseFromDataBase.al_posm.isEmpty()) {
					Helper.showCustomToast(getApplicationContext(),
							R.string.posm_data_is_not_available,
							Toast.LENGTH_LONG);
					return;

				}

				final ArrayList<RacePOSMMasterDTO> posmList = mResponseFromDataBase.al_posm;

				final ArrayList<RacePOSMMasterDTO> tempPosmList = Helper
						.cloneList(mResponseFromDataBase.al_posm);

				int countList = posmList.size();
				final String[] posmNameArray = new String[countList];
				final boolean[] _selectionsSticker = new boolean[countList];

				for (int i = 0; i < countList; i++) {
					RacePOSMMasterDTO racePOSMData = posmList.get(i);
					posmNameArray[i] = racePOSMData.getPOSMName();
					_selectionsSticker[i] = racePOSMData.isStickerSelected();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(
						RaceProductAuditCartItemUpdateActivity.this);
				builder.setTitle(R.string.select_sticker);
				builder.setMultiChoiceItems(posmNameArray, _selectionsSticker,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int position, boolean selected) {

								_selectionsSticker[position] = selected;
								posmList.get(position).setStickerSelected(
										selected);

							}
						});

				// Set the action buttons
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

								ArrayList<String> posmName = new ArrayList<String>();
								for (int i = 0; i < posmList.size(); i++) {
									RacePOSMMasterDTO posm = posmList.get(i);
									posm.setStickerSelected(_selectionsSticker[i]);
									if (posm.isStickerSelected()) {
										posmName.add(posm.getPOSMName());

									}

								}

								if (!posmName.isEmpty()) {
									String posmNames = TextUtils.join(",",
											posmName);
									spProductSticker.setText(posmNames);
								} else {
									spProductSticker.setText("");
								}

								mResponseFromDataBase.al_posm = posmList;

							}
						});

				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								alert.dismiss();

								mResponseFromDataBase.al_posm = tempPosmList;

							}
						});
				alert = builder.create();

				alert.show();
			}
		});

	}

	
	
	
	private void getBundleValue() {

		Bundle bundle = this.getIntent().getExtras();
		stockAuditID = bundle.getInt("STOCK_AUDIT_ID");
		String productName = bundle.getString("PRODUCT_NAME");
		tvRaceProducct.setText(productName);

		setProgresDialogProperties(getString(R.string.loadingmessage));
		progressDialog.show();

		getProductAuditResponseFromDatabase(new GetProductAuditHandler(this),
				stockAuditID);

	}

	
	
	
	static class GetProductAuditHandler extends Handler {

		WeakReference<RaceProductAuditCartItemUpdateActivity> updateActivity;

		public GetProductAuditHandler(
				RaceProductAuditCartItemUpdateActivity updateActivity) {

			this.updateActivity = new WeakReference<RaceProductAuditCartItemUpdateActivity>(
					updateActivity);
		}

		@Override
		public void handleMessage(Message msg) {

			boolean data_available = msg.getData().getBoolean("data_available");

			if (data_available) {

				final RaceProductAuditCartItemUpdateActivity activityRefrence = updateActivity
						.get();

				if (activityRefrence!=null) {
					if (activityRefrence.progressDialog != null
							&& activityRefrence.progressDialog.isShowing()) {
						activityRefrence.progressDialog.dismiss();

						activityRefrence.progressDialog = null;
					}
					activityRefrence.initializeView();
				}

			}

		};

	}

	private void getProductAuditResponseFromDatabase(final Handler handler,
			final int stockAuditID) {
		Thread mThread = new Thread() {
			@Override
			public void run() {

				try {

					/*
					 * mResponseFromDataBase = DatabaseHelper.getConnection(
					 * getApplicationContext())
					 * .getRaceProductAuditResposneByStockAuditID(
					 * stockAuditID);
					 */

					Cursor auditCursor = contentResolver
							.query(ProviderContract.URI_RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID,
									null,
									null,
									new String[] { String.valueOf(stockAuditID) },
									null);

					if (auditCursor == null) {
						Message msg = handler.obtainMessage();
						Bundle bundle = new Bundle();

						bundle.putBoolean("data_available", false);

						msg.setData(bundle);
						handler.sendMessage(msg);
						return;
					}

					mResponseFromDataBase = DatabaseUtilMethods
							.getProductAuditResponseFromCursor(auditCursor);
					auditCursor.close();

					
					Cursor posmResponseCursor = contentResolver
							.query(Uri.withAppendedPath(ProviderContract.URI_RACE_POSM_RESPONSE_WITH_ID, String.valueOf(stockAuditID)),
									null,
									null,
									new String[] { String
											.valueOf(mResponseFromDataBase.productID)
											},
									null);

					if (posmResponseCursor != null) {
						ArrayList<RacePOSMMasterDTO> al_posm = DatabaseUtilMethods
								.getPosmResponseFormCursor(posmResponseCursor,
										stockAuditID);
						mResponseFromDataBase.al_posm = al_posm;
						posmResponseCursor.close();
					}

					/*
					 * mRaceBrandNameData = DatabaseHelper.getConnection(
					 * getApplicationContext()).getBrandData();
					 */

					String projection[] = {
							RaceBrandMasterColumns.KEY_BRAND_NAME,
							RaceBrandMasterColumns.KEY_BRAND_ID };

					Cursor cursor = contentResolver.query(
							ProviderContract.URI_RACE_BRAND, projection, null,
							null, null);

					if (cursor == null) {
						return;
					}

					mRaceBrandNameData = DatabaseUtilMethods
							.getRaceBrandFromCursor(cursor);
					cursor.close();

					Message msg = handler.obtainMessage();
					Bundle bundle = new Bundle();

					bundle.putBoolean("data_available", true);

					msg.setData(bundle);
					handler.sendMessage(msg);
				} catch (Exception e) {
					if (progressDialog != null
							&& progressDialog.isShowing()) {
						progressDialog.dismiss();

						progressDialog = null;
					}
					e.printStackTrace();
				}

			}
		};

		mThread.start();

	}

	private boolean validate() {
		if (spDisplayArea.getText().toString().equals("")) {
			Helper.showCustomToast(getApplicationContext(),
					R.string.race_warninng_please_select_display_area,
					Toast.LENGTH_LONG);
			return false;
		} else if (spSubDisplayArea.getText().toString().equals("")) {
			Helper.showCustomToast(getApplicationContext(),
					R.string.race_warninng_please_select_sub_display_area,
					Toast.LENGTH_LONG);
			return false;
		} else if (mllBrandContainter.getVisibility() == View.VISIBLE
				&& spBrand.getText().toString().equals("")) {
			Helper.showCustomToast(getApplicationContext(),
					R.string.race_warninng_please_select_brand,
					Toast.LENGTH_LONG);
			return false;
		} else if (mllWallContainer.getVisibility() == View.VISIBLE
				&& spWallNumber.getText().toString().equals("")) {
			Helper.showCustomToast(getApplicationContext(),
					R.string.race_warninng_please_select_wall,
					Toast.LENGTH_LONG);
			return false;
		}

		else if (mllRowContainter.getVisibility() == View.VISIBLE
				&& spRowNumber.getText().toString().equals("")) {
			Helper.showCustomToast(getApplicationContext(),
					R.string.race_warninng_please_select_row, Toast.LENGTH_LONG);
			return false;
		}

		return true;

	}
}
