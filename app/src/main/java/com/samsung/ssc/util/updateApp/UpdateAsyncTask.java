package com.samsung.ssc.util.updateApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.samsung.ssc.util.Helper;


public class UpdateAsyncTask extends AsyncTask<Context, Integer, Void>{

	Context context;
	ProgressDialog progressDialog;
	CallBackUpdateApp callBack;
	String downloadLink;
	
	public UpdateAsyncTask(Context context ,CallBackUpdateApp callBack ,String url) {
		this.context=context;
		this.callBack=callBack;
		this.downloadLink=url;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
    	progressDialog = new ProgressDialog(context);
		progressDialog.setProgress(0);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("Downloading.....");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(Context... params) {
		  try {
	        	//set the download URL, a url that points to a file on the internet
	            //this is the file to be downloaded
	            URL url = new URL(downloadLink);

	            //create the new connection
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

	            //and connect!
	            urlConnection.connect();

	            //set the path where we want to save the file
	            //in this case, going to save it on the root directory of the
	            //sd card.
	            File SDCardRoot = Environment.getExternalStorageDirectory();
	            //create a new file, specifying the path, and the filename
	            //which we want to save the file as.
	            File file = new File(SDCardRoot,"File.apk");

	            //this will be used to write the downloaded data into the file we created
	            FileOutputStream fileOutput = new FileOutputStream(file);

	            //this will be used in reading the data from the internet
	            InputStream inputStream = urlConnection.getInputStream();

	            //this is the total size of the file
	            int totalSize = urlConnection.getContentLength();
	            progressDialog.setMax(totalSize);
	            
	            //variable to store total downloaded bytes
	            int downloadedSize = 0;

	            //create a buffer...
	            byte[] buffer = new byte[1024];
	            int bufferLength = 0; //used to store a temporary size of the buffer

	            //now, read through the input buffer and write the contents to the file
	            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                    //add the data in the buffer to the file in the file output stream (the file on the sd card
	                    fileOutput.write(buffer, 0, bufferLength);
	                    //add up the size so we know how much is downloaded
	                    downloadedSize += bufferLength;
	                    //this is where you would do something to report the prgress, like this maybe
	                    publishProgress(downloadedSize);
	            }
	            //close the output stream when done
	            fileOutput.close();
	    } catch (MalformedURLException e) {
	            Helper.printStackTrace(e);
	    } catch (IOException e) {
	            Helper.printStackTrace(e);
	    }
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		progressDialog.setProgress(values[0]);
	}	
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		callBack.processResponse();
		try {
			progressDialog.dismiss();
		} catch (Exception e) {
		}
	}
}	
