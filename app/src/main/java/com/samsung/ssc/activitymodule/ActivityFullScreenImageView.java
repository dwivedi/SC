 package com.samsung.ssc.activitymodule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.samsung.ssc.R;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.ImageLoader;

public class ActivityFullScreenImageView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Helper.setScreenShotOff(this);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activty_fullscreen_imageview);
		ImageView imageView = (ImageView) findViewById(R.id.ivFullScreenImageView);
		Intent intent = getIntent();
		String image_path = intent.getExtras().getString("image_path");
		String called_from = intent.getExtras().getString("called_from");

		if (called_from != null
				&& called_from.equalsIgnoreCase("feedback_detail")) {
			if (image_path != null) {
			/*	ImageLoader.getInstance(getApplicationContext()).displayImage(
						getResources().getString(R.string.url_image)
								+ "Contents/FMS/" + image_path, imageView);*/
				
				ImageLoader.getInstance(getApplicationContext())
				.displayImage( getResources().getString(R.string.url_file_processor_servicebase)+"id="+image_path+"&type=FMS",
						imageView);

			
			}
		}

		else if (called_from != null
				&& called_from.equalsIgnoreCase("create_feedback")) {
			if (image_path != null) {
				Bitmap rotatedbitmap = null;

				rotatedbitmap = Helper.creatBitmapFormImagepath(image_path);

				if (rotatedbitmap != null) {
					imageView.setImageBitmap(rotatedbitmap);
				}
			}
		} else if (called_from != null
				&& called_from.equalsIgnoreCase("create_feedback_hint_image")) {

		

			/*ImageLoader.getInstance(getApplicationContext()).displayImage(
					getResources().getString(R.string.url_image_hint)+""
							+ image_path, imageView);
			*/
			ImageLoader.getInstance(getApplicationContext()).displayImage(
					getResources().getString(R.string.url_file_processor_consolebase)+"id="+image_path+"&type=FMSIcon"
							, imageView);
		
			
			

		}

	}
}