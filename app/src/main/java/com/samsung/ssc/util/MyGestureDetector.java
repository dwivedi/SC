package com.samsung.ssc.util;

import com.samsung.ssc.R;

import android.app.Activity;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class MyGestureDetector extends SimpleOnGestureListener {
	private Activity context;
	public MyGestureDetector(Activity context) {

		this.context = context;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		 
 		try {
			float slope = (e1.getY() - e2.getY()) / (e1.getX() - e2.getX());
			float angle = (float) Math.atan(slope);
			float angleInDegree = (float) Math.toDegrees(angle);
			// left to right
			if (e1.getX() - e2.getX() > 2000 && Math.abs(velocityX) > 2000) {
				if ((angleInDegree < 45 && angleInDegree > -45)) {
					

				}
				// right to left fling
			} else if (e2.getX() - e1.getX() > 300 && Math.abs(velocityX) > 800) {
				if ((angleInDegree < 45 && angleInDegree > -45)) {
					context.finish();
					context.overridePendingTransition(R.anim.left_in, R.anim.right_out);
				}
			}
			return true;
		} catch (Exception e) {
			// nothing
		}
		return false;
	}
}