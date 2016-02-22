package com.samsung.ssc.sync.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.samsung.ssc.constants.DownloadMasterCodes;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataMasterColumns;
import com.samsung.ssc.download.DownloadMasterGetTableData;
import com.samsung.ssc.download.DownloadMasterTable;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

	// Global variables
	// Define a variable to contain a content resolver instance
	ContentResolver mContentResolver;

	Context mContext;

	private LinkedHashMap<Character, String> al_download_data_codes;

	public static boolean isSyncStopped;

	// private ArrayList<Character> al_download_data_codes;

	/**
	 * Set up the sync adapter
	 */
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		/*
		 * If your app uses a content resolver, get an instance of it from the
		 * incoming Context
		 */
		mContentResolver = context.getContentResolver();
		mContext = context;
		isSyncStopped = false;
	}

	/**
	 * Set up the sync adapter. This form of the constructor maintains
	 * compatibility with Android 3.0 and later platform versions
	 */
	public SyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		/*
		 * If your app uses a content resolver, get an instance of it from the
		 * incoming Contextk
		 */
		mContentResolver = context.getContentResolver();
		isSyncStopped = false;

	}

	@Override
	public void onSyncCanceled() {
		// TODO Auto-generated method stub
		super.onSyncCanceled();
		Log.e("cancel sync adapter", "cancel  sync called");
		
		isSyncStopped=true;
		
		Helper.changeSyncFlag(isSyncStopped);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {

		/*
		 * AccountManager am = AccountManager.get(mContext); Account account =
		 * am.getAccountsByType(Const.ACCOUNT_TYPE)[0];
		 * if(ContentResolver.isSyncActive(account, authority){
		 * 
		 * }
		 */

		Log.e("sync adapter", "Perform sync called");
		
		isSyncStopped=Helper.getSyncFlag();
		
		if (!isSyncStopped) {
			downloadData(extras);
		}

	}

	private void downloadData(Bundle bundle) {
		try {

			String selection = DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED
					+ "= ? ";

			Cursor cursor = mContext
					.getContentResolver()
					.query(ProviderContract.URI_DOWNLOAD_DATA,
							new String[] {
									DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE,
									DownloadDataMasterColumns.KEY_MODIFIED_DATE_TIME_STAMP },
							selection, new String[] { "1" }, null);

			al_download_data_codes = DatabaseUtilMethods
					.getDownloadableDataCodesFromCursor1(cursor);

			if (al_download_data_codes
					.containsKey(DownloadMasterCodes.USER_MODULES)) {
				LinkedHashMap<Character, String> newmap = (LinkedHashMap<Character, String>) al_download_data_codes
						.clone();
				al_download_data_codes.clear();
				al_download_data_codes.put('M', "");
				al_download_data_codes.putAll(newmap);
			}

			DownloadMasterTable<Context, JSONObject, Entry<Character, String>, Bundle> download = new DownloadMasterGetTableData();

			Set<Entry<Character, String>> entries = al_download_data_codes
					.entrySet();

			for (Entry<Character, String> entry : entries) {

				if (!isSyncStopped) {

					// if EOL data is downloaded than we do not have to look for
					// EOL data in further syncing process
					if (entry.getKey() == DownloadMasterCodes.EOL_SCHEMES) {

						Cursor cursorResult = mContext
								.getContentResolver()
								.query(ProviderContract.URI_DOWNLOAD_DATA,
										new String[] { DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS },
										DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE
												+ "=?",
										new String[] { String
												.valueOf(DownloadMasterCodes.EOL_SCHEMES) },
										null);

						boolean status = DatabaseUtilMethods
								.getDownloadStatusFromCursor(cursorResult);
						if (status == true) {

							continue;
						}

					}

					download.getData(mContext, entry, bundle);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}