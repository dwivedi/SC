package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.adapters.CustomProductAduitAdapter;
import com.samsung.ssc.adapters.NothingSelectionSpinnerAdapter;
import com.samsung.ssc.adapters.RaceCustomSpinnerProductGroupAdapter;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandProductMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceFixtureMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceProductAuditResponseMasterColumns;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.RaceFixtureDataModal;
import com.samsung.ssc.dto.RaceProductDataModal;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

public class RaceProductAuditActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private CustomProductAduitAdapter productAuditListAdapter;

	private TextView tvAuditCount;
	private ActivityDataMasterModel mActivityData;
	private long mActivityID = -1;
	private Spinner mSp_product_group, mSp_product_category;
	private RaceFixtureDataModal mSelectedraceFixtureData = null;
	private Map<Integer, String> raceBrandNameData;

	private RaceFixtureDataModal mSelectedraceFixtureDataTemp;
	EditText mEtProductSearch;
	ArrayList<RaceFixtureDataModal>  raceFixtureData;
	public static int REQUEST_CODE = 1;
	private ContentResolver contentResolver;
	private final int LOADER_ID_GET_ACTIVITY_ID = 1;
	private final int LOADER_ID_GET_AUDIT_COUNT = 2;

	private boolean isProductCategorySelected = false;
	private String selectedProductGroup = null;
	private ListView lvRaceProductAudit = null;

	private ArrayList<RaceProductDataModal> raceProductDataByProductCategory;

	@Override
	public void init() {
		super.init();

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.product_audit);

		contentResolver = getContentResolver();

		getBundleValue();
		setUpView();

	}

	private void getBundleValue() {

		Intent intent = getIntent();

		Module module = (Module) intent
				.getParcelableExtra("MOUDLE_POJO");

		mActivityData = Helper.getActivityDataMasterModel(
				getApplicationContext(), module);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mActivityData != null) {

			/*
			 * mActivityID =
			 * DatabaseHelper.getConnection(getApplicationContext())
			 * .getActivityIdIfExist(mActivityData);
			 */

			Loader loader = getSupportLoaderManager().getLoader(
					LOADER_ID_GET_ACTIVITY_ID);
			if (loader == null) {
				getSupportLoaderManager().initLoader(LOADER_ID_GET_ACTIVITY_ID,
						null, this);
			} else {
				getSupportLoaderManager().restartLoader(
						LOADER_ID_GET_ACTIVITY_ID, null, this);
			}

		}

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	private void setUpView() {

		tvAuditCount = (TextView) findViewById(R.id.tvProductAuditCount);

		mSp_product_group = (Spinner) findViewById(R.id.spinnerProductGroupProductAudit);
		mSp_product_group.setEnabled(false);
		mSp_product_category = (Spinner) findViewById(R.id.spinnerProductCategoryProductAudit);
		mSp_product_category.setEnabled(false);
		lvRaceProductAudit = (ListView) findViewById(R.id.lv_race_productaudit);

		mEtProductSearch = (EditText) findViewById(R.id.et_product_audit_search);

		productAuditListAdapter = new CustomProductAduitAdapter(
				RaceProductAuditActivity.this, this);
		lvRaceProductAudit.setAdapter(productAuditListAdapter);

		lvRaceProductAudit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				onListItemClick(parent, position);

			}

		});

		mEtProductSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				String searchText = mEtProductSearch.getText().toString();

				try {

					// product list is based on product category
					if (isProductCategorySelected) {

						// if user deleted search text than reinsert category
						// products if user search products from product list
						// category wise
						if (searchText.equalsIgnoreCase("")
								&& raceProductDataByProductCategory != null) {
							productAuditListAdapter
									.addItems(raceProductDataByProductCategory);
							productAuditListAdapter.notifyDataSetChanged();
						} else {
							productAuditListAdapter.filter(searchText);
						}

					} else {

						// product list is based on product group
						if (!searchText.equalsIgnoreCase("")) {
							getProductByProductGroupAndSerachText(searchText);
						} else {
							productAuditListAdapter.clearAll();
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		showFixtureDialog();
	}

	public void getProductByProductGroupAndSerachText(String searchText) {

		String projection[] = { RaceBrandProductMasterColumns.KEY_BRAND_ID,
				RaceBrandProductMasterColumns.KEY_NAME,
				RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY,
				RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP,
				RaceBrandProductMasterColumns.KEY_PRODUCT_ID,
				RaceBrandProductMasterColumns.KEY_PRODUCT_TYPE };

		Cursor cursor = contentResolver.query(
				ProviderContract.URI_RACE_BRAND_PRODUCT, projection,
				RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP + "=? AND "
						+ RaceBrandProductMasterColumns.KEY_NAME + " LIKE ?",
				new String[] { selectedProductGroup, "%" +searchText + "%" }, null);   // SDCE-4397

		if (cursor == null) {
			return;
		}

		ArrayList<RaceProductDataModal> raceProductDataByProductGroup = DatabaseUtilMethods
				.getRaceProductListFromCursor(cursor);

		cursor.close();

		productAuditListAdapter.addItems(raceProductDataByProductGroup);
		productAuditListAdapter.notifyDataSetChanged();

	}

	private void onListItemClick(AdapterView<?> parent, int position) {
		RaceProductDataModal responseHeader = (RaceProductDataModal) parent
				.getItemAtPosition(position);

		Intent intent = new Intent(RaceProductAuditActivity.this,
				RaceProductDetailActivity.class);
		intent.putExtra(Constants.KEY_RACE_FIXTURE, mSelectedraceFixtureData);
		intent.putExtra("PRODUCTID", responseHeader.getProductID());
		intent.putExtra("HEADERNAME", responseHeader.getProductName());

		intent.putExtra("ACTIVITY_ID", mActivityID);
		intent.putExtra("ACTIVITY_DATA", mActivityData);

		/*
		 * i.putExtra("DISPLAYAREA", displayArea); i.putExtra("SUBDISPLAYAREA",
		 * subDisplayArea); i.putExtra("WALLNUMBER", wallNumber);
		 * i.putExtra("ROWNUMBER", rowNumber); i.putExtra("BRANDNAME",
		 * brandName); i.putExtra("PRODUCTID", responseHeader.getProductID());
		 * i.putExtra("HEADERNAME", responseHeader.getProductName());
		 * i.putExtra("FIXTUREID", fixtureID);
		 */

		startActivityForResult(intent, REQUEST_CODE);
	}

	public void onFixtureSelectionClick(View v) {

		showFixtureDialog();

	}

	private void showFixtureDialog() {

		// final RaceFixtureDataModal selectedSubCategoryModal ;

		/*
		 * final ArrayList<String> raceCategoryData = DatabaseHelper
		 * .getConnection(getApplicationContext()).getCategoryData();
		 */

		Cursor cursor = contentResolver.query(
				ProviderContract.URI_RACE_FIXTURE, new String[] { "Distinct "
						+ RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME },
				null, null, null);

		if (cursor == null) {
			return;
		}

		final ArrayList<String> raceCategoryData = DatabaseUtilMethods
				.getRaceCategoryListFromCursor(cursor);

		cursor.close();

		AlertDialog.Builder dialogBuilder;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			dialogBuilder = new AlertDialog.Builder(
					RaceProductAuditActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			dialogBuilder = new AlertDialog.Builder(
					RaceProductAuditActivity.this);
		}

		// inflate and adjust layout
		LayoutInflater inflater = (LayoutInflater) RaceProductAuditActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogViewContainer = inflater.inflate(
				R.layout.race_product_audit_fixture_popup, null);

		dialogBuilder.setView(dialogViewContainer);

		final AlertDialog parentFixtureAlertDialog = dialogBuilder.create();
		parentFixtureAlertDialog.show();

		final EditText spDisplayArea = (EditText) dialogViewContainer
				.findViewById(R.id.etDisplayArea);

		final EditText spSubDisplayArea = (EditText) dialogViewContainer
				.findViewById(R.id.etSubDisplayArea);

		spSubDisplayArea.setEnabled(false);

		final EditText spWallNumber = (EditText) dialogViewContainer
				.findViewById(R.id.etWallNumber);

		final EditText spRowNumber = (EditText) dialogViewContainer
				.findViewById(R.id.etRowNumber);

		final EditText spBrand = (EditText) dialogViewContainer
				.findViewById(R.id.etBrand);

		final LinearLayout llBrandContainer = (LinearLayout) dialogViewContainer
				.findViewById(R.id.ll_SpinnerBrandContainer);

		final LinearLayout llWallContainer = (LinearLayout) dialogViewContainer
				.findViewById(R.id.ll_SpinnerWallContainer);

		final LinearLayout llRowContainer = (LinearLayout) dialogViewContainer
				.findViewById(R.id.ll_SpinnerRowContainer);

		if (mSelectedraceFixtureData != null) {

			spDisplayArea.setText(mSelectedraceFixtureData.getCategory());
			spSubDisplayArea.setText(mSelectedraceFixtureData.getSubCategory());

			spSubDisplayArea.setEnabled(true);

			spSubDisplayArea.setTag(mSelectedraceFixtureData.getFixtureID());

			int noOfWall = mSelectedraceFixtureData.getWallNumber();
			int noOfRow = mSelectedraceFixtureData.getRowNumber();

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

			spBrand.setText(mSelectedraceFixtureData.getBrandName());

			llBrandContainer.setVisibility(mSelectedraceFixtureData
					.isCompetitorAvailable() ? View.VISIBLE : View.GONE);
			llRowContainer.setVisibility(mSelectedraceFixtureData
					.isRowAvailable() ? View.VISIBLE : View.GONE);

			llWallContainer.setVisibility(mSelectedraceFixtureData
					.isWallAvailable() ? View.VISIBLE : View.GONE);
		}

		Button btnRaceFixtureOk = (Button) dialogViewContainer
				.findViewById(R.id.btnRaceFixtureOk);
		Button btnRaceFixtureCancel = (Button) dialogViewContainer
				.findViewById(R.id.btnRaceFixtureCancel);

		spDisplayArea.setOnClickListener(new View.OnClickListener() {

			private AlertDialog alert;

			@Override
			public void onClick(View v) {

				final AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this);
				}

				builder.setTitle(R.string.display_area);
				int selectedIndex = -1;

				final String[] categoryArr = new String[raceCategoryData.size()];
				raceCategoryData.toArray(categoryArr);

				String selectedValue = spDisplayArea.getText().toString();

				if (!selectedValue.equalsIgnoreCase("")) {

					selectedIndex = raceCategoryData.indexOf(selectedValue);
				}

				builder.setSingleChoiceItems(categoryArr, selectedIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String selectedItem = categoryArr[which]
										.toString();
								spDisplayArea.setText(selectedItem);

								/*
								 * raceFixtureData =
								 * DatabaseHelper.getConnection(
								 * getApplicationContext())
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

								raceFixtureData = DatabaseUtilMethods
										.getRaceFixtureDataModelListFromCursor(cursor);
								cursor.close();

								/*
								 * String selectQuery = "SELECT " +
								 * RaceFixtureMasterColumns.KEY_FIXTURE_ID + ","
								 * + KEY_FIXTURE_SUB_CATEGORY + "," +
								 * KEY_FIXTURE_PRODUCT_GROUPS + "," +
								 * KEY_FIXTURE_IS_COMPETITOR_AVAILBALE + "," +
								 * KEY_FIXTURE_IS_ROW_AVAILABLE + "," +
								 * KEY_FIXTURE_IS_WALL_AVAILABLE + " FROM " +
								 * TABLE_RACE_FIXTURE_MASTER + " WHERE " +
								 * KEY_FIXTURE_CATEGORY_NAME + " = '" +
								 * categoryName + "'";
								 */

								spSubDisplayArea.setText("");
								spSubDisplayArea.setEnabled(true);

								llBrandContainer.setVisibility(View.GONE);
								llRowContainer.setVisibility(View.GONE);
								llWallContainer.setVisibility(View.GONE);

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

				if (raceFixtureData == null) {
					return;
				}

				int count = raceFixtureData.size();
				final String[] fixtureSubCategoryArray = new String[count];

				for (int i = 0; i < count; i++) {
					fixtureSubCategoryArray[i] = raceFixtureData.get(i)
							.getSubCategory();
				}

				AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this);
				}
				builder.setTitle(R.string.display_sub_area);
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

								mSelectedraceFixtureDataTemp = raceFixtureData
										.get(which);

								// if previously used fixture data available
								if (mSelectedraceFixtureData != null) {

									mSelectedraceFixtureDataTemp
											.setRowNumber(mSelectedraceFixtureData
													.getRowNumber());
									mSelectedraceFixtureDataTemp
											.setWallNumber(mSelectedraceFixtureData
													.getWallNumber());
									mSelectedraceFixtureDataTemp
											.setBrandID(mSelectedraceFixtureData
													.getBrandID());
									mSelectedraceFixtureDataTemp
											.setBrandName(mSelectedraceFixtureData
													.getBrandName());

								}

								if (mSelectedraceFixtureDataTemp != null) {

									spSubDisplayArea
											.setTag(mSelectedraceFixtureDataTemp
													.getFixtureID());

									if (mSelectedraceFixtureDataTemp
											.isCompetitorAvailable()
											&& llBrandContainer.getVisibility() == View.GONE) {
										llBrandContainer
												.setVisibility(View.VISIBLE);

										if (raceBrandNameData == null) {

											/*
											 * raceBrandNameData =
											 * DatabaseHelper .getConnection(
											 * getApplicationContext())
											 * .getBrandData();
											 */

											String projection[] = {
													RaceBrandMasterColumns.KEY_BRAND_NAME,
													RaceBrandMasterColumns.KEY_BRAND_ID };

											Cursor cursor = contentResolver
													.query(ProviderContract.URI_RACE_BRAND,
															projection, null,
															null, null);

											if (cursor == null) {
												return;
											}

											raceBrandNameData = DatabaseUtilMethods
													.getRaceBrandFromCursor(cursor);
											cursor.close();

										}

									} else if (!mSelectedraceFixtureDataTemp
											.isCompetitorAvailable()
											&& llBrandContainer.getVisibility() == View.VISIBLE) {
										llBrandContainer
												.setVisibility(View.GONE);
									}

									if (mSelectedraceFixtureDataTemp
											.isRowAvailable()
											&& llRowContainer.getVisibility() == View.GONE) {
										llRowContainer
												.setVisibility(View.VISIBLE);
									} else if (!mSelectedraceFixtureDataTemp
											.isRowAvailable()
											&& llRowContainer.getVisibility() == View.VISIBLE) {
										llRowContainer.setVisibility(View.GONE);
									}
									if (mSelectedraceFixtureDataTemp
											.isWallAvailable()
											&& llWallContainer.getVisibility() == View.GONE) {
										llWallContainer
												.setVisibility(View.VISIBLE);
									} else if (!mSelectedraceFixtureDataTemp
											.isWallAvailable()
											&& llWallContainer.getVisibility() == View.VISIBLE) {
										llWallContainer
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

				AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this);
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

								mSelectedraceFixtureDataTemp
										.setWallNumber(Integer
												.parseInt(selectedItem));
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

				AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this);
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

								mSelectedraceFixtureDataTemp
										.setRowNumber(Integer
												.parseInt(selectedItem));

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

				if (raceBrandNameData == null) {
					return;
				}

				AlertDialog.Builder builder;

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builder = new AlertDialog.Builder(
							RaceProductAuditActivity.this);
				}

				builder.setTitle(R.string.brand);
				int selectedIndex = -1;

				final String[] brandNameArr = raceBrandNameData.values()
						.toArray(new String[raceBrandNameData.size()]);

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
								spBrand.setText(selectedItem);

								mSelectedraceFixtureDataTemp
										.setBrandName(selectedItem);
								Object key = Helper.getKeyFromValue(
										raceBrandNameData, selectedItem);
								if (key != null) {
									mSelectedraceFixtureDataTemp
											.setBrandID((Integer) key);
								}

								alert.dismiss();
							}
						});

				alert = builder.create();
				alert.show();
			}
		});

		btnRaceFixtureOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (spDisplayArea.getText().toString().equals("")) {
					Helper.showCustomToast(getApplicationContext(),
							R.string.race_warninng_please_select_display_area,
							Toast.LENGTH_LONG);
					return;
				} else if (spSubDisplayArea.getText().toString().equals("")) {
					Helper.showCustomToast(
							getApplicationContext(),
							R.string.race_warninng_please_select_sub_display_area,
							Toast.LENGTH_LONG);
					return;
				} else if (llBrandContainer.getVisibility() == View.VISIBLE
						&& spBrand.getText().toString().equals("")) {
					Helper.showCustomToast(getApplicationContext(),
							R.string.race_warninng_please_select_brand,
							Toast.LENGTH_LONG);
					return;
				} else if (llWallContainer.getVisibility() == View.VISIBLE
						&& spWallNumber.getText().toString().equals("")) {
					Helper.showCustomToast(getApplicationContext(),
							R.string.race_warninng_please_select_wall,
							Toast.LENGTH_LONG);
					return;
				}

				else if (llRowContainer.getVisibility() == View.VISIBLE
						&& spRowNumber.getText().toString().equals("")) {
					Helper.showCustomToast(getApplicationContext(),
							R.string.race_warninng_please_select_row,
							Toast.LENGTH_LONG);
					return;
				}

				mSelectedraceFixtureData = mSelectedraceFixtureDataTemp;

				mSelectedraceFixtureData.setCategory(spDisplayArea.getText()
						.toString());

				String productGroup = mSelectedraceFixtureData
						.getProductGroup();
				if (productGroup != null) {

					List<String> items = Arrays.asList(productGroup
							.split("\\s*,\\s*"));

					if (items.size() > 0) {

						/*
						 * if (mEtProductSearch.getVisibility() == View.VISIBLE)
						 * { mEtProductSearch.setVisibility(View.GONE); }
						 */
						if (!mSp_product_group.isEnabled()) {
							mSp_product_group.setEnabled(true);
						}

						if (mSp_product_category.isEnabled()) {
							mSp_product_category.setSelection(0);
							mSp_product_category.setEnabled(false);
						}
						if (productAuditListAdapter != null) {
							productAuditListAdapter.addItems(null);
						}

						final RaceCustomSpinnerProductGroupAdapter productAdapter = new RaceCustomSpinnerProductGroupAdapter(
								getApplicationContext(), items);
						mSp_product_group
								.setAdapter(new NothingSelectionSpinnerAdapter(
										productAdapter,
										R.layout.spinner_product_group_nothing_selected,
										RaceProductAuditActivity.this));

						mSp_product_group
								.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> parent, View view,
											int position, long id) {

										selectedProductGroup = (String) parent
												.getItemAtPosition(position);

										/*
										 * List<String> productCategoryList =
										 * DatabaseHelper .getConnection(
										 * getApplicationContext())
										 * .getRaceProductCategoryByProductGroup
										 * ( selectedProductGroup);
										 */

										if (selectedProductGroup == null) {
											return;
										}

										if (!mEtProductSearch.isFocusable()) {
											mEtProductSearch
													.setFocusableInTouchMode(true);
											mEtProductSearch.setFocusable(true);
										}

										mEtProductSearch.setText("");

										isProductCategorySelected = false;

										String projection[] = { "Distinct "
												+ RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY };
										Cursor cursor = contentResolver
												.query(ProviderContract.URI_RACE_BRAND_PRODUCT,
														projection,
														RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP
																+ "=?",
														new String[] { selectedProductGroup },
														null);

										if (cursor == null) {
											return;
										}

										List<String> productCategoryList = DatabaseUtilMethods
												.getRaceProductCategoryListFromCursor(cursor);

										cursor.close();

										RaceCustomSpinnerProductGroupAdapter productCategoryAdapter = new RaceCustomSpinnerProductGroupAdapter(
												getApplicationContext(),
												productCategoryList);
										mSp_product_category
												.setAdapter(new NothingSelectionSpinnerAdapter(
														productCategoryAdapter,
														R.layout.spinner_product_category_nothing_selected,
														RaceProductAuditActivity.this));
										if (!productCategoryList.isEmpty()) {
											mSp_product_category
													.setEnabled(true);
										}

										productAuditListAdapter.addItems(null);

										/*
										 * if (mEtProductSearch.getVisibility()
										 * == View.VISIBLE) { mEtProductSearch
										 * .setVisibility(View.GONE); }
										 */
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> parent) {

									}
								});

						mSp_product_category
								.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> parent, View view,
											int position, long id) {

										String selectedProductCategory = (String) parent
												.getItemAtPosition(position);

										/*
										 * ArrayList<RaceProductDataModal>
										 * raceProductData = DatabaseHelper
										 * .getConnection(
										 * getApplicationContext())
										 * .getRaceProductNameData(
										 * selectedProductCategory);
										 */

										/*
										 * KEY_BRAND_ID + "," + KEY_NAME + "," +
										 * KEY_PRODUCT_CATEGORY + "," +
										 * KEY_PRODUCT_GROUP + "," +
										 * KEY_PRODUCT_ID + "," +
										 * KEY_PRODUCT_TYPE
										 */

										if (selectedProductCategory == null) {
											return;
										}

										isProductCategorySelected = true;

										String projection[] = {
												RaceBrandProductMasterColumns.KEY_BRAND_ID,
												RaceBrandProductMasterColumns.KEY_NAME,
												RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY,
												RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP,
												RaceBrandProductMasterColumns.KEY_PRODUCT_ID,
												RaceBrandProductMasterColumns.KEY_PRODUCT_TYPE };

										Cursor cursor = contentResolver
												.query(ProviderContract.URI_RACE_BRAND_PRODUCT,
														projection,
														RaceBrandProductMasterColumns.KEY_PRODUCT_CATEGORY
																+ "=? AND "+RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP+"=?",
														new String[] { selectedProductCategory,selectedProductGroup },
														null);

										if (cursor == null) {
											return;
										}

										raceProductDataByProductCategory = DatabaseUtilMethods
												.getRaceProductListFromCursor(cursor);

										cursor.close();

										if (raceProductDataByProductCategory != null
												&& !raceProductDataByProductCategory
														.isEmpty()) {

											productAuditListAdapter
													.addItems(raceProductDataByProductCategory);
											productAuditListAdapter
													.notifyDataSetChanged();

											/*
											 * mEtProductSearch
											 * .setVisibility(View.VISIBLE);
											 */
											mEtProductSearch.setText("");

											// to scroll up
											lvRaceProductAudit.setSelection(0);

										}/*
										 * else { mEtProductSearch
										 * .setVisibility(View.GONE); }
										 */

									}

									@Override
									public void onNothingSelected(
											AdapterView<?> parent) {

									}
								});

					}

				}

				parentFixtureAlertDialog.dismiss();
			}
		});

		btnRaceFixtureCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				parentFixtureAlertDialog.dismiss();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				RaceFixtureDataModal mSelectedraceFixtureDataIntent = data
						.getExtras().getParcelable(Constants.KEY_RACE_FIXTURE);

				mSelectedraceFixtureData = mSelectedraceFixtureDataIntent;

			}
		}

	}

	public void onCartClick(View view) {

		Intent intent = new Intent(RaceProductAuditActivity.this,
				RaceProductAuditCartActivity1.class);
		intent.putExtra("ACTIVITY_ID", mActivityID);
		intent.putExtra("ACTIVITY_DATA", mActivityData);
		startActivity(intent);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {

		Loader loader = null;

		switch (id) {
		case LOADER_ID_GET_ACTIVITY_ID:

			String[] arg = { String.valueOf(mActivityData.getStoreID()),
					String.valueOf(mActivityData.getModuleCode()) };

			loader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_ACTIVITY_DATA_RESPONSE, null, null,
					arg, null);

			break;
		case LOADER_ID_GET_AUDIT_COUNT:

			/*
			 * String countQuery = "SELECT  COUNT(*) FROM " +
			 * TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER + " WHERE " +
			 * KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID + "=" + mActivity;
			 */

			String projection[] = { " COUNT(*)" };

			loader = new CursorLoader(
					getApplicationContext(),
					ProviderContract.URI_RACE_PRODUCT_AUDIT_RESPONSE,
					projection,
					RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
							+ "=?",
					new String[] { String.valueOf(mActivityID) }, null);

			break;
		default:
			break;
		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int auditCount;
		switch (loader.getId()) {
		case LOADER_ID_GET_ACTIVITY_ID:

			if (cursor != null && cursor.getCount() > 0) {

				mActivityID = DatabaseUtilMethods.getActivityID(cursor);

			} else { // reset mActivityID in case previously assigned values
						// still in the variable

				/**
				 * unit testing case: When we delete the last item in the cart
				 * screen we also delete the activity data from activity data
				 * master table but when we come back to this activity old
				 * 'mActivityID' still persist in the code. So we reset the
				 * value when cursor does not return any value form activity
				 * data master table.
				 */

				mActivityID = -1;
			}

			if (mActivityID != -1) {
				/*
				 * auditCount = DatabaseHelper.getConnection(
				 * getApplicationContext()).getProductAuditCount( mActivityID);
				 */

				Loader audit_count_loader = getSupportLoaderManager()
						.getLoader(LOADER_ID_GET_AUDIT_COUNT);
				if (audit_count_loader == null) {
					getSupportLoaderManager().initLoader(
							LOADER_ID_GET_AUDIT_COUNT, null,
							RaceProductAuditActivity.this);
				} else {
					getSupportLoaderManager().restartLoader(
							LOADER_ID_GET_AUDIT_COUNT, null,
							RaceProductAuditActivity.this);
				}

			} else {
				auditCount = 0;
				tvAuditCount.setText(String.valueOf(auditCount));
			}

			break;
		case LOADER_ID_GET_AUDIT_COUNT:

			if (cursor != null && cursor.moveToFirst()) {

				auditCount = cursor.getInt(0);
				tvAuditCount.setText(String.valueOf(auditCount));

			}

			break;
		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}
