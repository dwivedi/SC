package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.StoreBasicModel;

public class OutletWorkingAdapter1 extends ArrayAdapter<StoreBasicModel>{
	private final List<StoreBasicModel> todaysStores;
	private LayoutInflater inflater;
 	public OutletWorkingAdapter1(Context context, int textViewResourceId,List<StoreBasicModel> todaysStores) {
		super(context,R.layout.beat_today, todaysStores);
		this.todaysStores=todaysStores;
		this.inflater = (LayoutInflater)LayoutInflater.from(context);
 	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.beat_today, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (todaysStores != null) {
			StoreBasicModel todaysStore = todaysStores.get(position);
			if (todaysStore != null) {
				holder.tvAccountId.setText(todaysStore.getStoreCode());
				holder.tvAccountName.setText(todaysStore.getStoreName());
				if (todaysStore.isCoverage()) {
					holder.tvAccountName.setTextColor(Color.GREEN);
				} else {
					holder.tvAccountName.setTextColor(Color.RED);
				}
			}
		}
		return convertView;
	}
	
	class ViewHolder{
		public ViewHolder(View convertView) {
			tvAccountName = (TextView) convertView.findViewById(R.id.tvaccountName);
			tvAccountId = (TextView) convertView.findViewById(R.id.tvaccid);
		}

		TextView tvAccountName,tvAccountId;
	}

}
