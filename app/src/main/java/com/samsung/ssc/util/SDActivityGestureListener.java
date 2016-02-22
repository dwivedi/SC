package com.samsung.ssc.util;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SDActivityGestureListener extends
		GestureDetector.SimpleOnGestureListener {
	private Activity mActivity;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;

	// handle 'swipe right' action only

	public SDActivityGestureListener(Activity activity) {
		// TODO Auto-generated constructor stub
		this.mActivity = activity;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				// do your code

			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				// left to right flip
				mActivity.finish();
			}

		} catch (Exception e) {
			// nothing
		}
		return false;
 	}
}
