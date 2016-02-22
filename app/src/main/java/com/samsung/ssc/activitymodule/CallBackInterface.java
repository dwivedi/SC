package com.samsung.ssc.activitymodule;

import android.widget.LinearLayout;
import android.widget.RadioButton;

public interface CallBackInterface {

	/***
	 * Method to be called when asynctask has been completed. when AsyncTask is
	 * a seperate class
	 * 
	 * @param string
	 */
	public void asyncTaskCompletedCallBack(String string);

	/***
	 * Method to add Views in to body layout if clicked radio button has matched
	 * the criteria
	 * 
	 * @param bodyLayout
	 * @param radioButton
	 * @return
	 */
	public LinearLayout addViews(LinearLayout bodyLayout,
			RadioButton radioButton);

	/***
	 * Method to be called when any of radio button has been clicked or to
	 * change the state of radio button.
	 * 
	 * @param radioButton
	 */
	public void radioButtonClicked(RadioButton radioButton);
}
