package com.samsung.ssc.activitymodule;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;

public class DealerSelectionActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

	
	private AutoCompleteTextView acCitys;
	private AutoCompleteTextView acStores;
	protected int mStoreID = 0;
	private final int LOADER_OTHER_CITY_LIST_ID = 1;
	private final int LOADER_GET_OTHER_STROES_BY_CITY = 2;
	private final int LOADER_STORE_ID_BY_STORE_CODE = 3;
	private String mSearchedCityName = "";
 	

 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.setScreenShotOff(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dealer_selection);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		Button btnSubmit = (Button) findViewById(R.id.submit_others);
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mStoreID != 0) {
					Intent intent = getIntent();
					intent.putExtra(IntentKey.DEALER_ID, mStoreID);
					setResult(Activity.RESULT_OK, intent);
					finish();
				} else {
					Helper.showCustomToast(getApplicationContext(),
							R.string.please_select_dealer, Toast.LENGTH_LONG);

				}

			}
		});
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			this.setFinishOnTouchOutside(false);
		}

		setUpView();

	}

	private void setUpView() {
		
		getSupportLoaderManager().initLoader(LOADER_OTHER_CITY_LIST_ID, null, DealerSelectionActivity.this);
		
		acCitys = (AutoCompleteTextView) findViewById(R.id.city_others);
		
		//List<String> cityList = DatabaseHelper.getConnection(getApplicationContext()).getCityList();
		//setCityAdapter(cityList);
		
		acCitys.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String cityName = acCitys.getText().toString().trim();
				
			/*	List<String> dealerList = DatabaseHelper.getConnection(
						getApplicationContext()).getStoreNameByCity(
						cityName);*/
				 mSearchedCityName = cityName;
				if (getSupportLoaderManager().getLoader(LOADER_GET_OTHER_STROES_BY_CITY) == null) {
					getSupportLoaderManager().initLoader(LOADER_GET_OTHER_STROES_BY_CITY, null, DealerSelectionActivity.this);
				} else {
					getSupportLoaderManager().restartLoader(LOADER_GET_OTHER_STROES_BY_CITY, null, DealerSelectionActivity.this);
				}
				
 				

				//setStoreNameAdapter(dealerList);
				acStores.setText("");

			}
		});

		acStores = (AutoCompleteTextView) findViewById(R.id.dealer_others);
		
	/*	List<String> storeList = DatabaseHelper.getConnection(getApplicationContext()).getStoreNameByCity("");
		setStoreNameAdapter(storeList);*/
		
		getSupportLoaderManager().initLoader(LOADER_GET_OTHER_STROES_BY_CITY, null, DealerSelectionActivity.this);
		
		acStores.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				try {
					String dealerNameCode = acStores.getText().toString().trim();
					
					/*mStoreID = DatabaseHelper.getConnection(
							getApplicationContext())
							.getStoreIDByStoreCode(dealerNameCode);*/
					
					 /*JSONObject jObj = DatabaseHelper.getConnection(
							getApplicationContext())
							.getStoreIDByStoreCode(dealerNameCode);*/
					 
					 Bundle bundle = new Bundle();
					 bundle.putString("DealerNameCode", dealerNameCode);
					 
						if (getSupportLoaderManager().getLoader(LOADER_STORE_ID_BY_STORE_CODE) == null) {
							getSupportLoaderManager().initLoader(LOADER_STORE_ID_BY_STORE_CODE, bundle, DealerSelectionActivity.this);
						} else {
							getSupportLoaderManager().restartLoader(LOADER_STORE_ID_BY_STORE_CODE, bundle, DealerSelectionActivity.this);
						}
					 
					 
 					 
					 /*mStoreID  = jObj.getInt("StoreID");
					 String cityName = jObj.getString("CityName");*/

				/*	String cityName = DatabaseHelper.getConnection(
							getApplicationContext()).getCityByStoreID(
							mStoreID);*/
		/*			
					if (!TextUtils.isEmpty(cityName)) {
						acCitys.setText(cityName);
						
						getSupportLoaderManager().initLoader(LOADER_GET_OTHER_STROES_BY_CITY, null, DealerSelectionActivity.this);
						mSearchedCityName =cityName;
						
						List<String> storeList = DatabaseHelper.getConnection(
								getApplicationContext())
								.getStoreNameByCity(cityName);
						setStoreNameAdapter(storeList);
					}*/

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}


	private void setCityAdapter(List<String> arrayEmement) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				DealerSelectionActivity.this,
				android.R.layout.select_dialog_item, arrayEmement);

		acCitys.setThreshold(1);
		acCitys.setAdapter(adapter);
	}


	private void setStoreNameAdapter(List<String> arrayEmement) {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				DealerSelectionActivity.this, R.layout.dropdown_view,
				R.id.textDropdown, arrayEmement);
		acStores.setThreshold(1);
		acStores.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		Intent intent = getIntent();
		intent.putExtra("DEALER_ID", mStoreID);
		setResult(Activity.RESULT_CANCELED, intent);
		finish();

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle data) {
		// TODO Auto-generated method stub
		CursorLoader cursorLoader = null;
		if (loaderId == LOADER_OTHER_CITY_LIST_ID) {
			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_OTHER_STORES_CITIES, null, null, null,
					null);
		} else if (loaderId == LOADER_GET_OTHER_STROES_BY_CITY) {

			if (mSearchedCityName.equalsIgnoreCase("")) {
				String selectionForcity = StoreBasicColulmns.KEY_BEAT_TYPE
						+ "=?";

				String selectionArgsForCity[] = { Constants.STORE_TYPE_OTHER };

				cursorLoader = new CursorLoader(getApplicationContext(),
						ProviderContract.URI_GET_OTHER_STORES_BASICS_BY_CITY,
						null, selectionForcity, selectionArgsForCity, null);
			} else {
				String selectionForcity = StoreBasicColulmns.KEY_BEAT_TYPE
						+ "=? AND " + StoreBasicColulmns.KEY_BEAT_CITY + "=?";

				String selectionArgsForCity[] = { Constants.STORE_TYPE_OTHER,
						mSearchedCityName };

				cursorLoader = new CursorLoader(getApplicationContext(),
						ProviderContract.URI_GET_OTHER_STORES_BASICS_BY_CITY,
						null, selectionForcity, selectionArgsForCity, null);
			}
		}else if (loaderId == LOADER_STORE_ID_BY_STORE_CODE) {
			
			String dealerCode = data.getString("DealerNameCode");
			String dealerCode1 = dealerCode.substring(dealerCode.indexOf("[") + 1,
					dealerCode.indexOf("]"));
			
			String selectionForcity = StoreBasicColulmns.KEY_BEAT_TYPE +" =? AND "+StoreBasicColulmns.KEY_BEAT_STORE_CODE + "=?";
			String selectionArgsForCity[] = {Constants.STORE_TYPE_OTHER,dealerCode1};
			
			cursorLoader = new CursorLoader(getApplicationContext(),
					ProviderContract.URI_GET_OTHER_STORES_BASICS_BY_CITY,
					null, selectionForcity, selectionArgsForCity, null);
			
		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		
		int loaderId = loader.getId();
		
		if (loaderId == LOADER_OTHER_CITY_LIST_ID) {
			
			if (cursor != null && cursor.getCount() >0) {
				
			   List<String> cityList = DatabaseUtilMethods.getOtherStoreCitiesfromCursor(cursor);
			   setCityAdapter(cityList);
			}
			
		}else if (loaderId == LOADER_GET_OTHER_STROES_BY_CITY) {
			
			if (cursor != null && cursor.getCount() >0) {
				
				List<String> storeList = DatabaseUtilMethods.getStoresFromCity(cursor);
				setStoreNameAdapter(storeList);

			} 
		} else if (loaderId == LOADER_STORE_ID_BY_STORE_CODE) {
			
			
			if (cursor != null && cursor.getCount() >0) {
				
				  JSONObject jObj = DatabaseUtilMethods.getStoreIDByStoreCode(cursor);
				  String cityName = null;
				try {
					  mStoreID  = jObj.getInt("StoreID");
					  cityName = jObj.getString("CityName");
					  
				} catch (Exception e) {
					// TODO: handle exception
				}
				  
					if (!TextUtils.isEmpty(cityName)) {
						acCitys.setText(cityName);
						
						getSupportLoaderManager().initLoader(LOADER_GET_OTHER_STROES_BY_CITY, null, DealerSelectionActivity.this);
						mSearchedCityName =cityName;  
					}
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
		
	}
}
