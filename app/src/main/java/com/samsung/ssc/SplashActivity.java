package com.samsung.ssc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.util.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SplashActivity extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 1000;

	// Constants
	// The authority for the sync adapter's content provider
	/*public static final String AUTHORITY = "com.samsung.smartdost.provider";
	
	// An account type, in the form of a domain name
	public static final String ACCOUNT_TYPE = "smartdost.com";
	// The account name
	public static final String ACCOUNT = "dummyaccount";
	// Instance fields
	Account mAccount;

	// Sync interval constants
	public static final long SECONDS_PER_MINUTE = 60L;
	public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
	public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES
			* SECONDS_PER_MINUTE;
	private static AccountManager accountManager;
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// boolean restoreDatabase = restoreDB();
		// Toast.makeText(getApplicationContext(), "DB store "+restoreDatabase,
		// Toast.LENGTH_SHORT).show();

		Helper.setScreenShotOff(this);
		//setContentView(R.layout.splash);
		setContentView(R.layout.activity_splash_ssc);

		/*
		 * DatabaseHelper.getConnection(getApplicationContext())
		 * .getReadableDatabase();
		 * 
		 * new Handler().postDelayed(new Runnable() { public void run() {
		 * 
		 * Intent intent = new Intent(); intent.setClass(SplashActivity.this,
		 * LoginActivity1.class);
		 * 
		 * SplashActivity.this.startActivity(intent);
		 * SplashActivity.this.finish();
		 * 
		 * // transition from splash to main menu
		 * overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		 * 
		 * } }, SPLASH_TIME_OUT);
		 */

		if (Helper.isDeviceRooted()) {

			showDeviceRootedAlert();

		} else {

			DatabaseHelper.getConnection(getApplicationContext())
					.getReadableDatabase();

			new Handler().postDelayed(new Runnable() {
				public void run() {

					Intent intent = new Intent();
					intent.setClass(SplashActivity.this, LoginActivity.class);

					SplashActivity.this.startActivity(intent);
					SplashActivity.this.finish();

					// transition from splash to main menu
					overridePendingTransition(R.anim.fadein, R.anim.fadeout);

				}
			}, SPLASH_TIME_OUT);

		}
		TextView ver = (TextView) findViewById(R.id.version);
		ver.setText("Version " + Helper.getAppVersion(getApplicationContext()));

	/*	accountManager = (AccountManager) this
				.getSystemService(ACCOUNT_SERVICE);
		Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

		if (accounts.length == 0 || accounts[0] == null) {
			mAccount = CreateSyncAccount(this);
		} else {
			mAccount = accounts[0];
		}
*/
		//ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
		//ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 30);

		
		
		
		// Pass the settings flags by inserting them in a bundle

		/*Bundle settingsBundle = new Bundle();
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

		ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);*/

	}

	private void showDeviceRootedAlert() {
		Helper.showNotCancelableAlertDialog(
				SplashActivity.this,
				SSCAlertDialog.WARNING_TYPE,
				"Alert!",
				"Sorry! Your device is rooted. SmartDost app can not run on rooted device.",
				"OK", new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {

						sdAlertDialog.dismiss();

						SplashActivity.this.finish();

					}
				}, false, null, null);
	}

	private boolean restoreDB() {

		File f = new File(
				"/data/data/com.samsung.smartdost.ui/databases/SMARTDOST_CE_DATABASE");
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(f);
			fos = new FileOutputStream(FileDirectory.DIRECTORY_DATABASE);
			while (true) {
				int i = fis.read();
				if (i != -1) {
					fos.write(i);
				} else {
					break;
				}
			}
			fos.flush();
			Toast.makeText(this, "DB dump OK", Toast.LENGTH_LONG).show();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "DB dump ERROR", Toast.LENGTH_LONG).show();
			return false;
		} finally {
			try {
				fos.close();
				fis.close();
			} catch (IOException ioe) {
			}
		}
	}

	/**
	 * Create a new dummy account for the sync adapter
	 * 
	 * @param context
	 *            The application context
	 */
	/*public static Account CreateSyncAccount(Context context) {
		// Create the account type and default account
		Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
		// Get an instance of the Android account manager

		
		 * Add the account and account type, no password or user data If
		 * successful, return the Account object, otherwise report an error.
		 
		if (accountManager.addAccountExplicitly(newAccount, null, null)) {
			
			 * If you don't set android:syncable="true" in in your <provider>
			 * element in the manifest, then call context.setIsSyncable(account,
			 * AUTHORITY, 1) here.
			 
			System.out.println("Done");

		} else {
			
			 * The account exists or some other error occurred. Log this, report
			 * it, or handle it internally.
			 
			System.out.println("Error");
		}

		return newAccount;
	}
*/
}
