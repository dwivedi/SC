/**
 * 
 */
package com.samsung.ssc.activitymodule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.EOLOrderBookingResponseMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.EOLSchemeDetailsMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.EOLSchemeHeaderMasterMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.TableNames;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.EOLSchemeDTO;
import com.samsung.ssc.dto.EOLSchemeDetailDTO;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Helper;

/**
 * @author d.ashish
 * 
 */
public class EOLOrderActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	protected ArrayList<EOLSchemeDTO> mEolSchemeList;
	private EditText mSpinnerEOLScheem;
	private SchemeProductDetailsAdapter adapter;
	private ArrayList<EOLSchemeDetailDTO> mSelectedProductList;
	private static final int DEALER_SELECTION_REQUEST = 2;
	protected final int LOADER_EOL_SCHEME_RESPONSE = 1;
	protected final int LOADER_EOL_DETAILS_RESPONSE = 2;

	private ActivityDataMasterModel mActivityData;
	private long mActivityID;
	private int mStoreID;
	private ProgressDialog mProgressDialog;
	private String[] schemeIDs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eol_order1);

		mStoreID = getIntent().getExtras().getInt(IntentKey.STORE_ID);


		if (mStoreID == -1) {
			showStoreSelection();
		} else {
			getBundleValue(mStoreID);
			setUpView();

		}

	}

	public void onCancelButtonClick(View v) {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case DEALER_SELECTION_REQUEST:

				try {
					mStoreID = data.getExtras().getInt(IntentKey.DEALER_ID);

					getBundleValue(mStoreID);

				} catch (NumberFormatException e) {
					Helper.printStackTrace(e);
					mStoreID = 0;
				}

				break;

			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) {
			if (requestCode == DEALER_SELECTION_REQUEST) {
				mStoreID = 0;
				finish();
			}
		}
	}

	private void getBundleValue(int storeID) {

		Intent intent = getIntent();

		Module module = (Module) intent
				.getParcelableExtra(IntentKey.MOUDLE_POJO);

		mActivityData = Helper.getActivityDataMasterModel(
				getApplicationContext(), module);
	
		if (mActivityData != null) {
			
			mActivityData.setStoreID(storeID);
			mActivityData.setSurveyResponseID(0);

			mActivityID = DatabaseHelper.getConnection(getApplicationContext())
					.getActivityIdIfExistForEOL(mActivityData);
			if (mActivityID == -1) {

				PostDataToNetwork dataToNetwork = new PostDataToNetwork(
						EOLOrderActivity.this, "Get Last saved data",
						new GetDataCallBack() {

							@Override
							public void processResponse(Object result) {

								if (result != null) {

									processSchemeResponse(result);

								} else {
									setUpView();
								}

							}

							private void processSchemeResponse(Object result) {
								try {
									JSONObject jsonObject = new JSONObject(
											result.toString());

									if (jsonObject.getBoolean("IsSuccess")) {

										JSONArray rootJsonArray = jsonObject
												.getJSONArray("Result");

										if (rootJsonArray.length() > 0) {

											if (mActivityID == -1) {
												mActivityData
														.setSyncStatus(SyncUtils.SYNC_STATUS_SYNC_COMPLETED);
												mActivityID = DatabaseHelper
														.getConnection(
																getApplicationContext())
														.insertActivtyDataMaster(
																mActivityData);
											} else {
												try {
													DatabaseHelper
															.getConnection(
																	getApplicationContext())
															.updateActivtyDataMaster(
																	mActivityID,
																	SyncUtils.SYNC_STATUS_SYNC_COMPLETED);
													;
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											for (int i = 0; i < rootJsonArray
													.length(); i++) {
												rootJsonArray.getJSONObject(i)
														.put("ActivityID",
																mActivityID);
											}
											boolean flag;
											try {
												
												getContentResolver()
												.delete(ProviderContract.URI_EOL_ORDER_BOOKING_RESPONE,
														EOLOrderBookingResponseMasterColoums.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
																+ " = ?",
														new String[] { String.valueOf(mActivityID) });
					 
												ContentValues[] contantValues = DatabaseUtilMethods.getEOLOrderBookingResponseContantValue(mActivityID,
														rootJsonArray);
												
												getContentResolver()
												.bulkInsert(
														ProviderContract.URI_EOL_ORDER_BOOKING_RESPONE,
														contantValues);
												
												flag = true;
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
												flag = false;
											}
											
											/*boolean flag = DatabaseHelper
													.getConnection(
															getApplicationContext())
													.insertEOLOrderBookingResponse(
															mActivityID,
															rootJsonArray);*/
											if (flag) {
												setUpView();
											}
										} else {
											setUpView();
										}

									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
				dataToNetwork.setConfig(getString(R.string.url),
						WebConfig.WebMethod.LAST_SAVED_EOL_ACTIVITY);

				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put(WebConfig.WebParams.SCHEME_ID, 0);
					jsonObject.put(WebConfig.WebParams.STORE_ID_CAPS, storeID);
					jsonObject.put(WebConfig.WebParams.RETURN_ALL_SCHEMES , true);

					dataToNetwork.execute(jsonObject);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				setUpView();
			}
		}

	}

	/**
	 * initialise resource from xml
	 */
	private void setUpView() {

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {
			Button btn = (Button) findViewById(R.id.btn_submit_eolOrder);
			btn.setText(getString(R.string.proceed));
		}

		mSpinnerEOLScheem = (EditText) this.findViewById(R.id.spinnerEOLScheem);

		ListView lvScheemProductList = (ListView) this
				.findViewById(R.id.listViewEOLScheem);
		lvScheemProductList.setItemsCanFocus(true);

		adapter = new SchemeProductDetailsAdapter(this);
		lvScheemProductList.setAdapter(adapter);

		getScheemDataFromDatabase(new GetScheemHandler(this));

	}

	private void showStoreSelection() {

		Intent intent = new Intent(this, DealerSelectionActivity.class);
		startActivityForResult(intent, DEALER_SELECTION_REQUEST);
	}

	private void getScheemDataFromDatabase(final Handler handler) {

		getSupportLoaderManager().initLoader(LOADER_EOL_SCHEME_RESPONSE, null,
				this);
		 

	}

	static class GetScheemHandler extends Handler {

		WeakReference<EOLOrderActivity> eolActivity;

		public GetScheemHandler(EOLOrderActivity fms) {

			eolActivity = new WeakReference<EOLOrderActivity>(fms);
		}

		@Override
		public void handleMessage(Message msg) {

			boolean data_available = msg.getData().getBoolean("data_available");

			if (data_available) {
				final EOLOrderActivity activityRefrence = eolActivity.get();
				if (activityRefrence!=null) {
					activityRefrence.setDefaultValue();
				
			

				activityRefrence.mSpinnerEOLScheem
						.setOnTouchListener(new View.OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {

								if (event.getAction() == MotionEvent.ACTION_UP) {
									if (activityRefrence.mEolSchemeList.size() > 0) {
										
										activityRefrence.showScheemListDialog();

									}

								}
								return false;
							}
						});
			}
		}
		};
	}

	protected void showScheemListDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Scheme");
		int count = mEolSchemeList.size();
		String[] items = new String[count];
		for (int i = 0; i < count; i++) {
			items[i] = mEolSchemeList.get(i).getSchemeNumber();
		}

		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				EOLSchemeDTO selectedScheme = mEolSchemeList.get(which);

				mSpinnerEOLScheem.setText(selectedScheme.getSchemeNumber());
				mSpinnerEOLScheem.setTag(selectedScheme.getSchemeID());
				ArrayList<EOLSchemeDetailDTO> productList = selectedScheme
						.getScheemDetails();
				adapter.addProductDetails(productList);

			}
		});

		builder.create().show();

	}

	public void setDefaultValue() {

		String schemeNumber = mEolSchemeList.get(0).getSchemeNumber();
		int mSelectedSchemeID = mEolSchemeList.get(0).getSchemeID();
		mSpinnerEOLScheem.setText(schemeNumber);
		mSpinnerEOLScheem.setTag(mSelectedSchemeID);
		mSelectedProductList = mEolSchemeList.get(0).getScheemDetails();
		adapter.addProductDetails(mSelectedProductList);

	}

	class SchemeProductDetailsAdapter extends BaseAdapter {

		ArrayList<EOLSchemeDetailDTO> dataSet;
		private LayoutInflater inflater;

		public SchemeProductDetailsAdapter(Context context) {

			dataSet = new ArrayList<EOLSchemeDetailDTO>();
			inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return dataSet.size();
		}

		@Override
		public Object getItem(int position) {
			return dataSet.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public ArrayList<EOLSchemeDetailDTO> getDataSet() {
			return dataSet;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {

				convertView = inflater
						.inflate(R.layout.eol_listview_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.ref = position;

			final EOLSchemeDetailDTO modal = dataSet.get(position);
			holder.tvBasicModelCode.setText(modal.getBasicModelCode());

			holder.etOrderQuentity.setText(String.valueOf(modal
					.getUserDefineQuentity()));

			holder.etOrderQuentity.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {

					try {
						if (s != null) {
							dataSet.get(holder.ref)
									.setUserDefineQuentity(
											Integer.parseInt(s.toString()
													.equals("") ? "0" : s
													.toString()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

			holder.etActualSupportUnit.setText(String.valueOf(modal
					.getUserDefineSupport()));
			holder.etActualSupportUnit
					.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {

						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {

						}

						@Override
						public void afterTextChanged(Editable s) {

							try {
								if (s != null) {
									dataSet.get(holder.ref)
											.setUserDefineSupport(
													Integer.parseInt(s
															.toString().equals(
																	"") ? "0"
															: s.toString()));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});

			return convertView;
		}

		class ViewHolder {
			public ViewHolder(View view) {

				tvBasicModelCode = (TextView) view
						.findViewById(R.id.tv_model_eolOrder);

				etOrderQuentity = (EditText) view
						.findViewById(R.id.et_qty_eolOrder);
				etActualSupportUnit = (EditText) view
						.findViewById(R.id.et_supportPerUnit_eolOrder);

			}

			TextView tvBasicModelCode;

			EditText etOrderQuentity;
			EditText etActualSupportUnit;

			int ref;

		}

		public void addProductDetails(ArrayList<EOLSchemeDetailDTO> detailDTO) {
			this.dataSet.clear();
			this.dataSet.addAll(detailDTO);

			notifyDataSetChanged();
		}

		public void addProductDetails(EOLSchemeDetailDTO detailDTO) {
			this.dataSet.clear();
			this.dataSet.add(detailDTO);

			notifyDataSetChanged();

		}

	}

	public void onProceedButtonClick(View view) {

		if (Helper.getBoolValueFromPrefs(getApplicationContext(),
				SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS)) {

			DialogInterface.OnClickListener buttonSubmitListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					saveEOLData(SyncUtils.SYNC_STATUS_SUBMIT);

				}
			};

			DialogInterface.OnClickListener buttonSaveListerner = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					saveEOLData(SyncUtils.SYNC_STATUS_SAVED);
				}
			};

			Helper.showThreeButtonConfirmationDialog(this,
					R.string.sync_confirmation_dialog_title,
					R.string.sync_confirmation_dialog_message,
					R.string.sync_confirmation_dialog_button_one,
					R.string.sync_confirmation_dialog_button_two,
					R.string.sync_confirmation_dialog_button_negative,
					buttonSubmitListerner, buttonSaveListerner, null);

		} else {
			saveEOLData(SyncUtils.SYNC_STATUS_SUBMIT);
		}

	}

	protected void saveEOLData(int syncStatus) {
		saveDataToDatabase(syncStatus, new SaveDataHandler(this));

	}

	static class SaveDataHandler extends Handler {

		WeakReference<EOLOrderActivity> activityReference;

		public SaveDataHandler(EOLOrderActivity eolOrderActivity) {
			this.activityReference = new WeakReference<EOLOrderActivity>(
					eolOrderActivity);

		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			EOLOrderActivity activity = activityReference.get();

			if (activity!=null) {
				boolean dbStatus = msg.getData().getBoolean("STATUS");
				if (dbStatus) {

					Helper.showCustomToast(activity,
							R.string.data_saved_successfully,
							Toast.LENGTH_SHORT);

					activity.finish();
				} else {

					Helper.showCustomToast(activity, R.string.invalid_data,
							Toast.LENGTH_SHORT);

				}
				activity.dismissProgressDilaog();
			}
		}

	}

	private void saveDataToDatabase(final int syncStatus, final Handler handler) {

		showProgressDialog();

		Thread thread = new Thread() {
			public void run() {

				try {
					int rootCount = mEolSchemeList.size();
					JSONArray rootJsonArray = new JSONArray();

					for (int i = 0; i < rootCount; i++) {

						EOLSchemeDTO item = mEolSchemeList.get(i);

						ArrayList<EOLSchemeDetailDTO> productDetailsList = item
								.getScheemDetails();
						int productCount = productDetailsList.size();
						if (productCount > 0) {

							for (int j = 0; j < productCount; j++) {
								EOLSchemeDetailDTO itemProduct = productDetailsList
										.get(j);
								JSONObject jsonObject = new JSONObject();
								try {

									if (itemProduct.getUserDefineQuentity() != 0
											&& itemProduct
													.getUserDefineSupport() != 0) {

										// jsonObject.put("ActivityID",
										// mActivityID);
										jsonObject.put("StoreID", mStoreID);
										jsonObject.put("SchemeID",
												itemProduct.getSchemeID());
										jsonObject
												.put("BasicModelCode",
														itemProduct
																.getBasicModelCode());
										jsonObject
												.put("OrderQuantity",
														itemProduct
																.getUserDefineQuentity());
										jsonObject
												.put("ActualSupport",
														itemProduct
																.getUserDefineSupport());
										rootJsonArray.put(jsonObject);
									} else if (itemProduct
											.getUserDefineQuentity() != 0
											|| itemProduct
													.getUserDefineSupport() != 0) {
										Bundle extra = new Bundle();
										extra.putBoolean("STATUS", false);
										Message msg = handler.obtainMessage();
										msg.setData(extra);
										handler.sendMessage(msg);
										return;
									}

								} catch (NumberFormatException e) {
									e.printStackTrace();
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						}

					}

					if (rootJsonArray.length() > 0) {

						if (mActivityID == -1) {
							// ganrate activity id
							mActivityData.setSyncStatus(syncStatus);

							mActivityID = DatabaseHelper.getConnection(
									getApplicationContext())
									.insertActivtyDataMaster(mActivityData);
						} else {
							// update acitivty id with new sync status
							try {
								DatabaseHelper.getConnection(
										getApplicationContext())
										.updateActivtyDataMaster(mActivityID,
												syncStatus);
								;
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						for (int i = 0; i < rootJsonArray.length(); i++) {

							try {
								rootJsonArray.getJSONObject(i).put(
										"ActivityID", mActivityID);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						boolean flag;
						
						try {
							
							getContentResolver()
							.delete(ProviderContract.URI_EOL_ORDER_BOOKING_RESPONE,
									EOLOrderBookingResponseMasterColoums.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
											+ " = ?",
									new String[] { String.valueOf(mActivityID) });
 
							ContentValues[] contantValues = DatabaseUtilMethods.getEOLOrderBookingResponseContantValue(mActivityID,
									rootJsonArray);
							
							getContentResolver()
							.bulkInsert(
									ProviderContract.URI_EOL_ORDER_BOOKING_RESPONE,
									contantValues);
							
							flag = true;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							flag = false;
						}
						
					/*	boolean flag = DatabaseHelper.getConnection(
								getApplicationContext())
								.insertEOLOrderBookingResponse(mActivityID,
										rootJsonArray);
						
						*/
						
						

						Bundle extra = new Bundle();
						extra.putBoolean("STATUS", flag);
						Message msg = handler.obtainMessage();
						msg.setData(extra);
						handler.sendMessage(msg);

					} else {
						Bundle extra = new Bundle();
						extra.putBoolean("STATUS", false);
						Message msg = handler.obtainMessage();
						msg.setData(extra);
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Bundle extra = new Bundle();
					extra.putBoolean("STATUS", false);
					Message msg = handler.obtainMessage();
					msg.setData(extra);
					handler.sendMessage(msg);
				}
			}
		};
		thread.start();

	}

	public void dismissProgressDilaog() {
		if (!mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
	}

	private void showProgressDialog() {
		mProgressDialog = new ProgressDialog(EOLOrderActivity.this);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Process Data");
		mProgressDialog.setCancelable(false);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {

		Loader<Cursor> loader = null;
		switch (loaderID) {
		case LOADER_EOL_SCHEME_RESPONSE:

			loader = new CursorLoader(
					this,
					ProviderContract.URI_EOL_SCHEME_RESPONSE,
					new String[] {
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_ID,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_NUMBER,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_FROM,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_SCHEME_TO,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_FROM,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_ORDER_TO,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_NUMBER,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PUMI_DATE,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_TYPE,
							EOLSchemeHeaderMasterMasterColumns.KEY_EOL_PRODUCT_GROUP,
							EOLSchemeHeaderMasterMasterColumns.KEY_PRODUCT_CATEGORY,
							EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_CREATED_DATE,
							EOLSchemeHeaderMasterMasterColumns.KEY_ACTIVITY_DATA_MASTER_MODIFIED_DATE

					}, null, null, null);

			break;

		case LOADER_EOL_DETAILS_RESPONSE:

			String selection2 = TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER
					+ "." + EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID
					+ " in (";
			for (int i = 0; i < schemeIDs.length; i++) {
				selection2 += "?, ";
			}
			selection2 = selection2.substring(0, selection2.length() - 2) + ")";

			loader = new CursorLoader(this, Uri.withAppendedPath(
					ProviderContract.URI_EOL_SCHEME_DETAILS_RESPONSE_URI,
					String.valueOf(mActivityID)), null, selection2, schemeIDs,
					null);

			break;

		default:
			break;
		}

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loaderId, Cursor cursor) {

		switch (loaderId.getId()) {
		case LOADER_EOL_SCHEME_RESPONSE:
			if (cursor != null && cursor.getCount() > 0) {

				mEolSchemeList = DatabaseUtilMethods
						.getEOLSchemeHeaderResponseFromCursor(cursor);

				int count = mEolSchemeList.size();

				schemeIDs = new String[count];
				for (int i = 0; i < schemeIDs.length; i++) {

					schemeIDs[i] = String.valueOf(mEolSchemeList.get(i)
							.getSchemeID());
				}

				getSupportLoaderManager().initLoader(
						LOADER_EOL_DETAILS_RESPONSE, null, this);

			}
			break;

		case LOADER_EOL_DETAILS_RESPONSE:
			if (cursor != null && cursor.getCount() > 0) {

				ArrayList<EOLSchemeDetailDTO> detailDTOs = DatabaseUtilMethods
						.getEOLSchemeDetailsOrderResponsFromCursor(cursor);
				int size = detailDTOs.size();

				for (int i = 0; i < mEolSchemeList.size(); i++) {
					EOLSchemeDTO item = mEolSchemeList.get(i);
					int schemeID = item.getSchemeID();
					ArrayList<EOLSchemeDetailDTO> list = new ArrayList<EOLSchemeDetailDTO>();
					for (int j = 0; j < size; j++) {

						EOLSchemeDetailDTO itemChild = detailDTOs.get(j);

						int schemeIDChild = itemChild.getSchemeID();

						if (schemeIDChild == schemeID) {

							list.add(itemChild);
						}
					}
					item.setScheemDetails(list);
				}

				setDefaultValue();

				mSpinnerEOLScheem
						.setOnTouchListener(new View.OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {

								if (event.getAction() == MotionEvent.ACTION_UP) {
									if (mEolSchemeList.size() > 0) {
										showScheemListDialog();

									}

								}
								return false;
							}
						});

			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
