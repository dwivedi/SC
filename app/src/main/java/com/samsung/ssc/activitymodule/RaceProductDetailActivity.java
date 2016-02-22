package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceFixtureMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMDataResponseMasterColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.RaceFixtureDataModal;
import com.samsung.ssc.dto.RacePOSMMasterDTO;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

public class RaceProductDetailActivity extends BaseActivity {

	private EditText spDisplayArea;
	private EditText spSubDisplayArea;
	private EditText spWallNumber;
	private EditText spRowNumber;
	private EditText spBrand;
	private EditText spProductSticker;
	private ArrayList<RacePOSMMasterDTO> racePOSMProductData;

	private CheckBox cbPriceTag;
	private CheckBox cbRaceTopper;
	private CheckBox cbOnTag;
	private int productId;
	private TextView tvRaceProducct;
	private int fixtureID, brandID;
	private long mActivityID = -1;
	private ActivityDataMasterModel mActivityData;
	private RaceFixtureDataModal mSelectedraceFixtureDataIntent;
	private RaceFixtureDataModal mSelectedraceFixtureDataTemp = null;

	private View mllWallContainer, mllRowContainter, mllBrandContainter;

	private ArrayList<RaceFixtureDataModal> mRaceFixtureData;

	private Map<Integer, String> mRaceBrandNameData;

	private ContentResolver contentResolver;

	@Override
	public void init() {
		super.init();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.product_audit_detail);

		contentResolver = getContentResolver();

		setUpView();

		getDataFromDB();
	}

	public void onAddProductClick(View view) {

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
			productDetailsJSONObject.put("ProductId", productId);
			productDetailsJSONObject.put("FixtureId", fixtureID);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// check whter mActivity is not to -1

		if (mActivityID == -1) {

			mActivityData.setSyncStatus(SyncUtils.SYNC_STATUS_SUBMIT);
			/*
			 * mActivityID = DatabaseHelper.getConnection(this)
			 * .insertActivtyDataMaster(mActivityData);
			 */

			ContentValues contentValues = DatabaseUtilMethods
					.getActivityDataContetnValueArray(mActivityData);
			Uri uri = contentResolver.insert(
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues);
			mActivityID = ContentUris.parseId(uri);

		}

		/*long stockAuditID = DatabaseHelper.getConnection(
				getApplicationContext()).insertRaceProductAudit(mActivityID,
				productDetailsJSONObject);*/

		
		ContentValues cv_auditResponse=DatabaseUtilMethods
		.getContentValueForRaceProductAuditResponseFromJSON(
				mActivityID, productDetailsJSONObject);
		
		if(cv_auditResponse==null)
		{
			return;
		}
		
		Uri uri=contentResolver.insert(
				ProviderContract.URI_RACE_PRODUCT_AUDIT_RESPONSE,
				cv_auditResponse);
		
		
		long stockAuditID  = ContentUris.parseId(uri);

		if (!racePOSMProductData.isEmpty()) {

		/*	DatabaseHelper.getConnection(getApplicationContext())
					.insertRaceProductAuditPOSMResponse(mActivityID,
							stockAuditID, racePOSMProductData);
			*/
			/*
			 * db.delete(TABLE_RACE_POSM_DATA_RESPONSE_MASTER,
			 * KEY_STOCK_AUDIT_ID + " = ?", new String[] { stockAuditID + "" });
			 */

			// delete previous value
			contentResolver.delete(ProviderContract.URI_RACE_POSM_RESPONSE, RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID + " = ?", new String[] { stockAuditID + "" });
			
			ContentValues[] cv =DatabaseUtilMethods.getContentValuesPOSMResponseFromJSON(mActivityID, stockAuditID, racePOSMProductData);
			if(cv.length>0)
			{
				contentResolver.bulkInsert(ProviderContract.URI_RACE_POSM_RESPONSE,cv);
			}
			
			
		}

		Intent intent = getIntent();

		if (mSelectedraceFixtureDataTemp == null) {
			intent.putExtra(Constants.KEY_RACE_FIXTURE,
					mSelectedraceFixtureDataIntent);
		} else {
			intent.putExtra(Constants.KEY_RACE_FIXTURE,
					mSelectedraceFixtureDataTemp);
		}

		setResult(RESULT_OK, intent);
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

		getBundleValue();

		mllWallContainer = findViewById(R.id.ll_wallContainer_productAuditDetail);
		mllRowContainter = findViewById(R.id.ll_rowContainer_productAuditDetail);
		mllBrandContainter = findViewById(R.id.ll_brandContainer_productAuditDetail);

		if (mSelectedraceFixtureDataIntent.isRowAvailable()) {
			mllRowContainter.setVisibility(View.VISIBLE);
		}
		if (mSelectedraceFixtureDataIntent.isWallAvailable()) {
			mllWallContainer.setVisibility(View.VISIBLE);
		}
		if (mSelectedraceFixtureDataIntent.isCompetitorAvailable()) {
			mllBrandContainter.setVisibility(View.VISIBLE);
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
							RaceProductDetailActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductDetailActivity.this);
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
							RaceProductDetailActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductDetailActivity.this);
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

								fixtureID = mRaceFixtureData.get(which)
										.getFixtureID();

								mSelectedraceFixtureDataTemp = mRaceFixtureData
										.get(which);

						

								if (mSelectedraceFixtureDataIntent != null) {

									mSelectedraceFixtureDataTemp
									.setCategory(spDisplayArea.getText()
											.toString());
									
									
									mSelectedraceFixtureDataTemp
											.setRowNumber(mSelectedraceFixtureDataIntent
													.getRowNumber());
									mSelectedraceFixtureDataTemp
											.setWallNumber(mSelectedraceFixtureDataIntent
													.getWallNumber());
									mSelectedraceFixtureDataTemp
											.setBrandID(mSelectedraceFixtureDataIntent
													.getBrandID());
									mSelectedraceFixtureDataTemp
											.setBrandName(mSelectedraceFixtureDataIntent
													.getBrandName());

								}

							

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
							RaceProductDetailActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductDetailActivity.this);
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

								if (mSelectedraceFixtureDataTemp == null) {
									mSelectedraceFixtureDataIntent
											.setWallNumber(Integer
													.parseInt(selectedItem));
								} else {
									mSelectedraceFixtureDataTemp
											.setWallNumber(Integer
													.parseInt(selectedItem));
								}

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
							RaceProductDetailActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductDetailActivity.this);
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

								if (mSelectedraceFixtureDataTemp == null) {
									mSelectedraceFixtureDataIntent
											.setRowNumber(Integer
													.parseInt(selectedItem));
								} else {
									mSelectedraceFixtureDataTemp
											.setRowNumber(Integer
													.parseInt(selectedItem));
								}

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
					return;
				}

				final AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductDetailActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductDetailActivity.this);
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

								if (mSelectedraceFixtureDataTemp == null) {
									mSelectedraceFixtureDataIntent
											.setBrandName(selectedItem);
								} else {
									mSelectedraceFixtureDataTemp
											.setBrandName(selectedItem);
								}

								Object key = Helper.getKeyFromValue(
										mRaceBrandNameData, selectedItem);
								if (key != null) {
									brandID = (Integer) key;

									if (mSelectedraceFixtureDataTemp == null) {
										mSelectedraceFixtureDataIntent
												.setBrandID(brandID);
									} else {
										mSelectedraceFixtureDataTemp
												.setBrandID(brandID);
									}

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

				if (racePOSMProductData.isEmpty()) {
					Helper.showCustomToast(getApplicationContext(),
							R.string.posm_data_is_not_available,
							Toast.LENGTH_LONG);
					return;

				}

				final ArrayList<RacePOSMMasterDTO> tempracePOSMProductData = Helper
						.cloneList(racePOSMProductData);

				int count = racePOSMProductData.size();
				final String[] posmNameArray = new String[count];
				final boolean[] _selectionsSticker = new boolean[count];

				for (int i = 0; i < count; i++) {
					RacePOSMMasterDTO racePOSMData = racePOSMProductData.get(i);
					posmNameArray[i] = racePOSMData.getPOSMName();
					_selectionsSticker[i] = racePOSMData.isStickerSelected();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(
						RaceProductDetailActivity.this);
				builder.setTitle(R.string.select_sticker);
				builder.setMultiChoiceItems(posmNameArray, _selectionsSticker,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int clicked, boolean selected) {

								_selectionsSticker[clicked] = selected;
								racePOSMProductData.get(clicked)
										.setStickerSelected(selected);
							}
						});

				// Set the action buttons
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

								spProductSticker.setText("");

								StringBuilder builderSticker = new StringBuilder();

								for (RacePOSMMasterDTO posm : racePOSMProductData) {
									if (posm.isStickerSelected()) {
										builderSticker.append(posm
												.getPOSMName() + ",");

									}
								}

								spProductSticker.setText(builderSticker
										.toString());

							}
						});

				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								alert.dismiss();

								// racePOSMProductData.clear();
								racePOSMProductData = tempracePOSMProductData;

							}
						});
				alert = builder.create();// AlertDialog dialog; create like this
											// outside onClick
				alert.show();
			}
		});

	}

	private void getBundleValue() {
		Bundle bundle = this.getIntent().getExtras();
		mActivityID = bundle.getLong("ACTIVITY_ID");
		mActivityData = (ActivityDataMasterModel) bundle
				.getSerializable("ACTIVITY_DATA");

		mSelectedraceFixtureDataIntent = bundle
				.getParcelable(Constants.KEY_RACE_FIXTURE);

		spDisplayArea.setText(mSelectedraceFixtureDataIntent.getCategory());
		spSubDisplayArea.setText(mSelectedraceFixtureDataIntent
				.getSubCategory());

		int noOfWall = mSelectedraceFixtureDataIntent.getWallNumber();
		int noOfRow = mSelectedraceFixtureDataIntent.getRowNumber();

		if (noOfWall == -1) {
			spWallNumber.setText("");
		} else {
			spWallNumber.setText(noOfWall + "");
		}

		if (noOfRow == -1) {
			spRowNumber.setText("");
		} else {
			spRowNumber.setText(noOfRow + "");
		}

		spBrand.setText(mSelectedraceFixtureDataIntent.getBrandName());

		productId = bundle.getInt("PRODUCTID");
		tvRaceProducct.setText(bundle.getString("HEADERNAME"));
		fixtureID = mSelectedraceFixtureDataIntent.getFixtureID();
		brandID = mSelectedraceFixtureDataIntent.getBrandID();
	}

	private void getDataFromDB() {

		/*
		 * racePOSMProductData = DatabaseHelper.getConnection(
		 * getApplicationContext()).getPosmByProductID(productId);
		 */

		/*
		 * mRaceBrandNameData = DatabaseHelper .getConnection(
		 * getApplicationContext()) .getBrandData();
		 */

		Cursor posmCursor = contentResolver.query(
				ProviderContract.URI_RACE_POSM_PRODUCT_MAPPING, null, null,
				new String[] { String.valueOf(productId) }, null);

		if (posmCursor != null) {
			racePOSMProductData = DatabaseUtilMethods
					.getPOSMbyProduct(posmCursor);
			posmCursor.close();
		}

		String projection[] = { RaceBrandMasterColumns.KEY_BRAND_NAME,
				RaceBrandMasterColumns.KEY_BRAND_ID };

		Cursor cursor = contentResolver.query(ProviderContract.URI_RACE_BRAND,
				projection, null, null, null);

		if (cursor == null) {
			return;
		}

		mRaceBrandNameData = DatabaseUtilMethods.getRaceBrandFromCursor(cursor);
		cursor.close();

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
