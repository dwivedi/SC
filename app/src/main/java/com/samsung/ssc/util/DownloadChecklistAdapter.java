package com.samsung.ssc.util;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.samsung.ssc.R;

public class DownloadChecklistAdapter extends BaseAdapter implements
		CompoundButton.OnCheckedChangeListener {

	HashMap<Integer, Boolean> mHmCheckStates;

	Context context;
	CharSequence items[];

	private LayoutInflater inflter;

	public DownloadChecklistAdapter(Context context, CharSequence items[]) {
		this.inflter = LayoutInflater.from(context);
		this.context = context;
		this.items = items;
		mHmCheckStates = new HashMap<Integer, Boolean>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {

			convertView = this.inflter.inflate( R.layout.download_item_layout, parent, false);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtTitle.setText(items[position]);

		// holder.chkSelect.setChecked(true);
		holder.chkSelect.setTag(position);

		if (mHmCheckStates.get(position) != null) {
			holder.chkSelect.setChecked(mHmCheckStates.get(position));
		}

		holder.chkSelect.setOnCheckedChangeListener(this);
		return convertView;

	}

	public boolean isChecked(int position) {
		return mHmCheckStates.get(position);
	}

	public void setChecked(int position, boolean isChecked) {
		mHmCheckStates.put(position, isChecked);

	}

	public void toggle(int position) {
		setChecked(position, !isChecked(position));

	}

	public void setAllChecked() {
		for (int i = 0; i < items.length; i++) {

			mHmCheckStates.put(i, true);

		}
		setAllDownloadServiceFalse();
		notifyDataSetChanged();

	}

	private void setAllUnChecked() {
		for (int i = 0; i < items.length; i++) {

			mHmCheckStates.put(i, false);
		}
		setAllDownloadServiceTrue();
		notifyDataSetChanged();

	}

	/**
	 * This method is created because listview last items can not created
	 * without the user scrolling down So last times checkboxes did not called
	 * until then.
	 */
	private void setAllDownloadServiceTrue() {

		// set all service to true
		Helper.saveBoolValueInPrefs(context, "A", true);
		Helper.saveBoolValueInPrefs(context, "B", true);
		Helper.saveBoolValueInPrefs(context, "C", true);
		Helper.saveBoolValueInPrefs(context, "D", true);
		Helper.saveBoolValueInPrefs(context, "E", true);
		Helper.saveBoolValueInPrefs(context, "F", true);
		Helper.saveBoolValueInPrefs(context, "G", true);
		Helper.saveBoolValueInPrefs(context, "H", true);
		Helper.saveBoolValueInPrefs(context, "I", true);
		Helper.saveBoolValueInPrefs(context, "J", true);
		Helper.saveBoolValueInPrefs(context, "K", true);

		// mark the check status for the Module to false

		mHmCheckStates.put(9, false);
	}

	/**
	 * This method is created because listview last items can not created
	 * without the user scrolling down So last times checkboxes did not called
	 * until then.
	 */
	private void setAllDownloadServiceFalse() {

		// set all service to true
		Helper.saveBoolValueInPrefs(context, "A", false);
		Helper.saveBoolValueInPrefs(context, "B", false);
		Helper.saveBoolValueInPrefs(context, "C", false);
		Helper.saveBoolValueInPrefs(context, "D", false);
		Helper.saveBoolValueInPrefs(context, "E", false);
		Helper.saveBoolValueInPrefs(context, "F", false);
		Helper.saveBoolValueInPrefs(context, "G", false);
		Helper.saveBoolValueInPrefs(context, "H", false);
		Helper.saveBoolValueInPrefs(context, "I", false);
		Helper.saveBoolValueInPrefs(context, "J", false);
		Helper.saveBoolValueInPrefs(context, "K", false);

		// mark the check status for the Module to true
		// to mark it as downloadable again
		mHmCheckStates.put(9, true);
	}

	/**
	 * Check if any of the download modules is checked by the user
	 * 
	 * @return true if checked false otherwise
	 */
	public boolean getIfAnyItemIsChecked() {

		try {
			Set<Integer> keys = mHmCheckStates.keySet();

			if (keys.isEmpty()) {
				return false;
			}
			// if only 'Select all' item is checked and rest are unchecked
			// individually

			Boolean zero = mHmCheckStates.get(0);
			Boolean one = mHmCheckStates.get(1);
			Boolean two = mHmCheckStates.get(2);
			Boolean three = mHmCheckStates.get(3);
			Boolean four = mHmCheckStates.get(4);
			Boolean five = mHmCheckStates.get(5);
			Boolean six = mHmCheckStates.get(6);
			Boolean seven = mHmCheckStates.get(7);
			Boolean eight = mHmCheckStates.get(8);
			Boolean nine = mHmCheckStates.get(9);

			if ((zero != null ? zero == true : false)
					&& (one != null ? one == false : true)
					&& (two != null ? two == false : true)
					&& (three != null ? three == false : true)
					&& (four != null ? four == false : true)
					&& (five != null ? five == false : true)
					&& (six != null ? six == false : true)
					&& (seven != null ? seven == false : true)
					&& (eight != null ? eight == false : true)
					&& (nine != null ? nine == false : true)) {
				return false;
			}

			for (int i : keys) {
				if (mHmCheckStates.get(i) == true) {
					return true;
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		int position = (Integer) buttonView.getTag();
		switch (position) {

		case 0: // Select All

			if (isChecked) {

				// check to stop recursive calling
				if (mHmCheckStates.get(position) != null
						&& mHmCheckStates.get(position) == false) {
					mHmCheckStates
							.put((Integer) buttonView.getTag(), isChecked);

					setAllChecked();

				} else if (mHmCheckStates.get(position) == null) {

					mHmCheckStates
							.put((Integer) buttonView.getTag(), isChecked);

					setAllChecked();

				}

			} else {

				// check to stop recursive calling
				if (mHmCheckStates.get(position) != null
						&& mHmCheckStates.get(position) == true) {
					mHmCheckStates
							.put((Integer) buttonView.getTag(), isChecked);

					setAllUnChecked();

				} else if (mHmCheckStates.get(position) == null) {

					mHmCheckStates
							.put((Integer) buttonView.getTag(), isChecked);

					setAllUnChecked();

				}

			}

			break;

		case 1: // Download product

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "A", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "A", true);

			}

			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);

			break;

		case 2: // Download Payment Modes

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "B", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "B", true);

			}

			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);
			break;

		case 3: // Download Competitors
				// Download Competition Product Group

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "C", false);

				Helper.saveBoolValueInPrefs(context, "D", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "C", true);
				Helper.saveBoolValueInPrefs(context, "D", true);

			}
			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);

			break;

		case 4:// Download Other Beat

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "E", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "E", true);

			}
			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);
			break;

		case 5:// Download Questions

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "F", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "F", true);

			}
			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);
			break;

		case 6:// Download Planogram

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "G", false);
				Helper.saveBoolValueInPrefs(context, "H", false);
				Helper.saveBoolValueInPrefs(context, "I", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "G", true);
				Helper.saveBoolValueInPrefs(context, "H", true);
				Helper.saveBoolValueInPrefs(context, "I", true);

			}

			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);
			break;

		case 7:// Download Feedback Data

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "J", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "J", true);

			}
			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);
			break;

		case 8:// Download EOL Data

			if (isChecked) {
				Helper.saveBoolValueInPrefs(context, "K", false);

			} else {
				Helper.saveBoolValueInPrefs(context, "K", true);

			}
			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);
			break;

		case 9:// Download Modules

			mHmCheckStates.put((Integer) buttonView.getTag(), isChecked);
			break;

		default:

			break;
		}

	}

	/**
	 * Checks if the user modules to be downloaded.User module is not downloaded
	 * via service so we don't save the download status in preference
	 * 
	 * @return true if user module is checked otherwise false
	 */
	public boolean checkIfUserModuleNeedTobedownloaded() {

		if (mHmCheckStates.get(9) != null && mHmCheckStates.get(9) == true) {

			return true;

		}

		return false;
	}

	static class ViewHolder {

		public ViewHolder(View convertView) {
			// TODO Auto-generated constructor stub
			txtTitle = (TextView) convertView
					.findViewById(R.id.tv_name_download_item_layout);
			chkSelect = (CheckBox) convertView
					.findViewById(R.id.cb_download_item_layout);
		}

		TextView txtTitle;
		CheckBox chkSelect;

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
