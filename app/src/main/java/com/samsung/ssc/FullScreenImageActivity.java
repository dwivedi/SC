package com.samsung.ssc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class FullScreenImageActivity extends BaseActivity {
	private Bitmap bmp;

	@Override
	public void init() {
		super.init();
		setContentView(R.layout.activity_fullscreen);

		byte[] byteArray = getIntent().getByteArrayExtra("image");
		bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		ImageView imageView = (ImageView) findViewById(R.id.imageview);

		imageView.setImageBitmap(bmp);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
				overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
			}
		});
		
	}

	@Override
	protected void onPause() {
		if (bmp != null)
			bmp.recycle();
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (bmp != null)
			bmp.recycle();
		super.onStop();
	}
}