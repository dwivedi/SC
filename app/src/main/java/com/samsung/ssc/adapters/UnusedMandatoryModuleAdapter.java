package com.samsung.ssc.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.Module;

public class UnusedMandatoryModuleAdapter extends BaseAdapter {

	ArrayList<Module> mData;
	Context mContext;
	LayoutInflater mInflater;

	public UnusedMandatoryModuleAdapter(Context context) {
		mData = new ArrayList<Module>();
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			convertView = mInflater.inflate(
					R.layout.unused_mandatory_module_layout, null);
		}
		TextView tvTitle = (TextView) convertView
				.findViewById(R.id.tv_module_name_unUsedMandatoryModule);

		Module module = mData.get(position);

		tvTitle.setText(module.getName());
		return convertView;

	}

	public void addItems(List<Module> list) {
		mData.clear();
		mData.addAll(list);
	/*	for (Module module : list) {

			if (module.isIsMandatory()) {
				if (!module.isActivityIDAvailable()) {
					mData.add(module);
				} else if (module.getSyncStatus() == SyncUtils.SYNC_STATUS_SAVED
						|| module.getSyncStatus() == SyncUtils.SYNC_STATUS_ERROR
						|| module.getSyncStatus() == SyncUtils.SYNC_STATUS_SYNC_COMPLETED) {
					mData.add(module);
				}
			}

		}
*/
		notifyDataSetChanged();
	}
	
	
	public ArrayList<Module>  getDataSet() {
	 
		
	  return mData;

	}

}