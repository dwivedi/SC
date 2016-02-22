package com.samsung.ssc.sync;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.samsung.ssc.R;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.PlanogramProductResponseMasterColumns;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.dto.UploadImageMultipartDataModal;
import com.samsung.ssc.dto.UploadMuliplePartImageRepeaterDataModal;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

public class SyncService extends IntentService {

	boolean isStarted = false;

	public SyncService() {
		super("THis is sunc");
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	protected void onHandleIntent(Intent intent) {

		int syncStatus = SyncUtils.SYNC_STATUS_ERROR;

		Bundle bundle = intent.getExtras();
		long[] activityID = bundle.getLongArray(SyncUtils.ACTIVITY_ID);
		boolean isImageTypeData = bundle
				.getBoolean(SyncUtils.REQUEST_STRING_IS_IMAGE_TYPE);
		String confugrationURL = bundle.getString(SyncUtils.CONFUGRATION_URL);

		// SENDING BROADCOST TO SEND ACTION START EXECUTING SERVICE NAME
		Intent syncStart = new Intent(SyncUtils.SYNC_ACTION_START);
		syncStart.putExtra(SyncUtils.SYNC_SERVICE,
				bundle.getString(SyncUtils.SYNC_SERVICE));
		LocalBroadcastManager.getInstance(this).sendBroadcast(syncStart);

		if (isImageTypeData) {

			ArrayList<UploadImageMultipartDataModal> requestString = bundle
					.getParcelableArrayList(SyncUtils.REQUEST_STRING);

			boolean isUploaded = uploadFile(confugrationURL, requestString);

			if (isUploaded) {
				// delete files and folder
				//deleteStoreImageFolder();
				
				File folder = new File(
						FileDirectory.DIRECTORY_SURVEY_QUESTION_IMAGES);
				deleteStoreImagesWithFolder(folder);
				
				syncStatus = SyncUtils.SYNC_STATUS_SYNC_COMPLETED;
 			} else {
				syncStatus = SyncUtils.SYNC_STATUS_ERROR;
			}

		} else {

			try {
				String requestString = bundle
						.getString(SyncUtils.REQUEST_STRING);
				String responseString = postMethod(confugrationURL,
						requestString);

				if (responseString != null) {
					ResponseDto response = new FetchingdataParser(this)
							.getResponseResult(responseString);
					if (response.isSuccess()) {
						syncStatus = SyncUtils.SYNC_STATUS_SYNC_COMPLETED;
					} else {
						syncStatus = SyncUtils.SYNC_STATUS_ERROR;
					}
				} else {
					syncStatus = SyncUtils.SYNC_STATUS_ERROR;
				}

			} catch (Exception e) {
				syncStatus = SyncUtils.SYNC_STATUS_ERROR;
			}

		}

		/*DatabaseHelper.getConnection(this).updateActivtyDataMaster(activityID,
				syncStatus);*/

		updateActivityDataMaster(activityID,syncStatus);
		
		Intent intent1 = new Intent(SyncUtils.SYNC_ACTION_UPDATE);
		intent1.putExtra(SyncUtils.SYNC_SERVICE,
				bundle.getString(SyncUtils.SYNC_SERVICE));
		intent1.putExtra(SyncUtils.SYNC_STATUS, syncStatus);

		LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
	}

	private void deleteStoreImageFolder() {
		try {
			File folder = new File(
					FileDirectory.DIRECTORY_SURVEY_QUESTION_IMAGES);
			if (folder.isDirectory()) {
				String[] children = folder.list();
				for (int i = 0; i < children.length; i++) {
					new File(folder, children[i]).delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void deleteStoreImagesWithFolder(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteStoreImagesWithFolder(child);

		fileOrDirectory.delete();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Intent intent1 = new Intent(SyncUtils.SYNC_ACTION_COMPLETED);
		intent1.putExtra(SyncUtils.SYNC_SERVICE, "All service completed");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
	}

	private String postMethod(String conUrl, String requestString)
			throws Exception {
		String result = null;
		InputStream is = null;

		HttpClient httpclient = new DefaultHttpClient();
		URI u = new URI(conUrl);
		HttpPost httppost = new HttpPost(u);
		httppost.setHeader(WebConfig.WebParams.APIKEY,
				Helper.getStringValuefromPrefs(this,
						SharedPreferencesKey.PREF_APIKEY));
		httppost.setHeader(WebConfig.WebParams.APITOKEN, Helper
				.getStringValuefromPrefs(this,
						SharedPreferencesKey.PREF_APITOKEN));

		if (!Helper.getStringValuefromPrefs(this,
				SharedPreferencesKey.PREF_USERID).equals("")) {

			httppost.setHeader(WebConfig.WebParams.USER_ID, Helper
					.getStringValuefromPrefs(this,
							SharedPreferencesKey.PREF_USERID));

		} else {
			httppost.setHeader(WebConfig.WebParams.USER_ID, "0");
		}
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept",
				"application/json, text/javascript, */*;q=0.01");
		httppost.setEntity(new ByteArrayEntity(requestString.getBytes("UTF8")));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			is = entity.getContent();
		}

		result = getStringfromInputStream(is);
		if (entity != null) {
			entity.consumeContent();
		}
		httpclient.getConnectionManager().shutdown();
		return result;

	}

	/**
	 * This method get string from InputStream
	 * 
	 * @param is
	 * @return data in string format
	 */

	private String getStringfromInputStream(InputStream is) {

		String result = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Helper.printStackTrace(e);
			Helper.printLog("Loading Runnable Error converting result DM",
					e.toString());
		}
		return result;

	}

	private boolean uploadFile(String upLoadServerUrl,
			ArrayList<UploadImageMultipartDataModal> imageUploadData) {

		int count = imageUploadData.size();

		
		for (int i = 0; i < count; i++) {

			UploadImageMultipartDataModal itemModal = imageUploadData.get(i);
			int type = itemModal.getType();
			boolean isSuccesse;
			if (type == 2) {
				isSuccesse = multipartImageUploadRepeater(
						this.getString(R.string.url)
								+ "UploadRepeatImageStream", itemModal);
			} else {
				isSuccesse = multipartImageUpload(upLoadServerUrl, itemModal);
			}
			if (!isSuccesse) {
				return false;
			}
		}
		
		return true;
	}

	private boolean multipartImageUploadRepeater(String upLoadServerUrl,
			UploadImageMultipartDataModal imageData) {

		InputStream fileInputStream = null;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			conn = getServerConn(upLoadServerUrl, boundary);
			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"StoreID\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(imageData.getStoreID()));
			dos.writeBytes(lineEnd);

			/*
			 * Latitude node
			 */
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"Latitude\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(imageData.getLatitude()));
			dos.writeBytes(lineEnd);

			/*
			 * Longitude node
			 */
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"Longitude\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(imageData.getLongitude()));
			dos.writeBytes(lineEnd);

			/*
			 * UserOption node
			 */
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"UserOption\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(imageData.getUserOption()));
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"surveyresponseid\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(imageData.getSurveyResponseId()));
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"userid\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(imageData.getUserId()));
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"surveyquestionid\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(String.valueOf(imageData.getSurveyQuestionId()));
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"type\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes("" + imageData.getType());
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"RepeatResponse\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes("" + imageData.getUserResponse());
			dos.writeBytes(lineEnd);

			List<UploadMuliplePartImageRepeaterDataModal> imageList = imageData
					.getArrList();
			int count = imageList.size();
			for (int i = 0; i < count; i++) {
				UploadMuliplePartImageRepeaterDataModal item = imageList.get(i);
				String filepath = item.getImagePath();
				File sourceFile = new File(filepath);

				String sourcefileName = sourceFile.toString();
				if (!sourcefileName.equals("")) {

					if (sourceFile.exists()) {

						fileInputStream = getStream(sourceFile);

						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
								+ sourcefileName + "\"" + lineEnd);
						dos.writeBytes(lineEnd);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						buffer = new byte[bufferSize];
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
						while (bytesRead > 0) {

							dos.write(buffer, 0, bufferSize);
							bytesAvailable = fileInputStream.available();
							bufferSize = Math
									.min(bytesAvailable, maxBufferSize);
							bytesRead = fileInputStream.read(buffer, 0,
									bufferSize);

						}
						dos.writeBytes(lineEnd);

						closeInputStream(fileInputStream);
					}
				}
			}

			dos.writeBytes(twoHyphens + boundary + twoHyphens + twoHyphens
					+ lineEnd);

			int serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			if (!serverResponseMessage.equalsIgnoreCase("OK")) {

				return false;

			} else {
				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);
			}
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	private boolean multipartImageUpload(String upLoadServerUrl,
			UploadImageMultipartDataModal imageData) {
		InputStream fileInputStream = null;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {

			String filepath = imageData.getUserResponse();
			File sourceFile;
			String sourcefileName;
			if (filepath.equals("")) {
				sourceFile = new File(filepath);
				sourcefileName = "";
			} else {
				sourceFile = new File(filepath);
				sourcefileName = sourceFile.toString();
			}

			if (sourcefileName != null) {
				if (filepath.equals("") || sourceFile.exists()) {

					try {
						conn = getServerConn(upLoadServerUrl, boundary);
						dos = new DataOutputStream(conn.getOutputStream());

						/*
						 * Store ID node
						 */
						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"StoreID\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(imageData.getStoreID()));
						dos.writeBytes(lineEnd);

						/*
						 * Latitude node
						 */
						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"Latitude\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(imageData.getLatitude()));
						dos.writeBytes(lineEnd);

						/*
						 * Longitude node
						 */
						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"Longitude\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(imageData.getLongitude()));
						dos.writeBytes(lineEnd);

						/*
						 * UserOption node
						 */
						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"UserOption\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(imageData.getUserOption()));
						dos.writeBytes(lineEnd);

						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"surveyresponseid\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(imageData
								.getSurveyResponseId()));
						dos.writeBytes(lineEnd);

						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"userid\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(imageData.getUserId()));
						dos.writeBytes(lineEnd);

						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"surveyquestionid\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes(String.valueOf(imageData
								.getSurveyQuestionId()));
						dos.writeBytes(lineEnd);

						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"type\""
								+ lineEnd);
						dos.writeBytes(lineEnd);
						dos.writeBytes("1");
						dos.writeBytes(lineEnd);

						if (!sourcefileName.equals("")) {

							fileInputStream = getStream(sourceFile);

							dos.writeBytes(twoHyphens + boundary + twoHyphens
									+ lineEnd);
							dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
									+ sourcefileName + "\"" + lineEnd);
							dos.writeBytes(lineEnd);
							bytesAvailable = fileInputStream.available();
							bufferSize = Math
									.min(bytesAvailable, maxBufferSize);
							buffer = new byte[bufferSize];
							bytesRead = fileInputStream.read(buffer, 0,
									bufferSize);
							while (bytesRead > 0) {

								dos.write(buffer, 0, bufferSize);
								bytesAvailable = fileInputStream.available();
								bufferSize = Math.min(bytesAvailable,
										maxBufferSize);
								bytesRead = fileInputStream.read(buffer, 0,
										bufferSize);

							}
							dos.writeBytes(lineEnd);
						}
						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ twoHyphens + lineEnd);

						int serverResponseCode = conn.getResponseCode();
						String serverResponseMessage = conn
								.getResponseMessage();

						if (!serverResponseMessage.equalsIgnoreCase("OK")) {

							return false;

						} else {
							Log.i("uploadFile", "HTTP Response is : "
									+ serverResponseMessage + ": "
									+ serverResponseCode);
						}
						dos.flush();
					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}
				} else {
					System.out.println("--File not exist---" + sourcefileName);
				}

			} else {
				System.out.println("No Record Found");
			}
			closeInputStream(fileInputStream);
			if(dos!=null)
			{
				dos.flush();
				dos.close();	
			}
			
		} catch (MalformedURLException ex) {
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			return false;

		} catch (Exception e) {
			Log.e("Upload file to server Exception",
					"Exception : " + e.getMessage(), e);
			return false;
		}

		return true;
	}

	private void closeInputStream(final InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception exc) {
			}
		}
	}

	/**
	 * Get server connection data to server
	 * 
	 * @param upLoadServerUrl
	 * @param boundary
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */

	private HttpURLConnection getServerConn(String upLoadServerUrl,
			String boundary) throws MalformedURLException, IOException,
			ProtocolException {
		HttpURLConnection conn;
		// open a URL connection to the server
		URL url = new URL(upLoadServerUrl);
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

	private void updateActivityDataMaster(long[] activityID,
			int syncStatus)
	{
		
		String selection = ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
				+ " in (";
		for (int i = 0; i < activityID.length; i++) {
			selection += "?, ";
		}
		selection = selection.substring(0, selection.length() - 2) + ")";
		
		String[] selectionArgs = new String[activityID.length];

		for(int i = 0; i < activityID.length; i++){
			selectionArgs[i] = String.valueOf(activityID[i]);
		}
		
		ContentValues contentValues=new ContentValues();
		contentValues.put(ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS, syncStatus);
		
		getContentResolver().update(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, contentValues, selection, selectionArgs);
		
		if (syncStatus!=SyncUtils.SYNC_STATUS_ERROR) {
			DatabaseHelper.getConnection(getApplicationContext())
					.deleteCompletedDataFromStoreModuleMaster();
		}
		
	}
	
}