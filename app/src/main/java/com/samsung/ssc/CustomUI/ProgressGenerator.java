package com.samsung.ssc.CustomUI;

import android.os.Handler;
import java.util.Random;

public class ProgressGenerator {

	public interface OnCompleteListener {

		public void onComplete();
	}

	private OnCompleteListener mListener;
	private int mProgress;
	private boolean isCompleted = false;

	public ProgressGenerator(OnCompleteListener listener) {
		mListener = listener;
	}

	public void start(final ProcessButton button) {

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				if (!isCompleted) {
					mProgress += 10;
					button.setProgress(mProgress);
					if (mProgress < 100) {
						handler.postDelayed(this, generateDelay());
					} else {
						mProgress = 10;
						button.setProgress(mProgress);
						// mListener.onComplete();
						handler.postDelayed(this, generateDelay());
					}
				}

			}
		}, 10);
	}

	public void completed(final ProcessButton button) {
		
		mProgress = 100;
		button.setProgress(mProgress);
		isCompleted = true;
		
	}

	public void setDefultValue(final ProcessButton button) {
		
		isCompleted = true;
		mProgress = 0;
		button.setProgress(mProgress);
	}
	
	 public void setInitialVal(final ProcessButton button) {
		 	
		 	isCompleted = false;
	 		mProgress = 0;
			button.setProgress(mProgress);
		}

	private Random random = new Random();

	private int generateDelay() {
		return random.nextInt(500);
	}
}
