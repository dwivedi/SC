package com.samsung.ssc.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.KPIData;

public class KPIListAdapter1 extends BaseAdapter {

	private ArrayList<KPIData> mDataSet;
	private LayoutInflater inflater;
 
	public KPIListAdapter1(Context context) {
		super();
		this.mDataSet = new ArrayList<KPIData>();
		this.inflater = LayoutInflater.from(context);

 	}

	@Override
	public int getCount() {
		return mDataSet.size();
	}

	@Override
	public KPIData getItem(int position) {
		return mDataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.kpi_list_item2, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		KPIData item = mDataSet.get(position);
		
		holder.tv_module_name.setText(item
				.getNotificationTypeDescription());
		
		holder.tv_unread.setText(String.valueOf(item.getUnreadCount()));
		holder.tv_total.setText(String.valueOf(item.getTotalCount()));
		

		return convertView;
	}
	
	class ViewHolder{

	
		TextView tv_module_name,tv_unread,tv_total;
		
		
		 
		public ViewHolder(View convertView) {
			
			 
			tv_module_name=(TextView)convertView.findViewById(R.id.tv_module_name_kpiListItem);
			tv_unread=(TextView)convertView.findViewById(R.id.tv_unread_kpiListItem);
			tv_total=(TextView)convertView.findViewById(R.id.tv_total_kpiListItem);
			
			
		}
		
	}

	public void addItem(ArrayList<KPIData> kpiDatas) {
		
		this.mDataSet.clear();
		this.mDataSet.addAll(kpiDatas);
		notifyDataSetChanged();
		
	}
}
