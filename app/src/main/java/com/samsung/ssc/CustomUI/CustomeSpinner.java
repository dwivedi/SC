package com.samsung.ssc.CustomUI;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Spinner;

/**
 * Class gives custom spinner after extends Spinner class.
 * @author ngupta
 */
public class CustomeSpinner extends Spinner {

	public CustomeSpinner(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomeSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomeSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void ignoreOldSelectionByReflection() {
		try {
			Class<?> c = this.getClass().getSuperclass().getSuperclass()
					.getSuperclass();
			Field reqField = c.getDeclaredField("mOldSelectedPosition");
			reqField.setAccessible(true);
			reqField.setInt(this, -1);
		} catch (Exception e) {
			Log.d("Exception Private", "ex", e);
		}
	}

	@Override
	public void setSelection(int position, boolean animate) {
		boolean sameSelected = position == getSelectedItemPosition();
		ignoreOldSelectionByReflection();
		super.setSelection(position, animate);
		if (sameSelected) {
			// Spinner does not call the OnItemSelectedListener if the same item
			// is selected, so do it manually now
			getOnItemSelectedListener().onItemSelected(this, getSelectedView(),
					position, getSelectedItemId());
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsSpinner#setSelection(int)
	 */
	@Override
	public void setSelection(int position) {
		ignoreOldSelectionByReflection();
		super.setSelection(position);
	}
}