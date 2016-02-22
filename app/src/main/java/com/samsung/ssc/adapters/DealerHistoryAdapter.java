package com.samsung.ssc.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 





import com.samsung.ssc.R;
import com.samsung.ssc.dto.DealerHistoryDataDto;

public class DealerHistoryAdapter extends BaseAdapter {

	private Object inflater;
	private ArrayList<DealerHistoryDataDto> list;
	private HashMap<Integer, String> hashmap;

	public DealerHistoryAdapter(Context context, ArrayList<DealerHistoryDataDto> list) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.list = list;

		hashmap = new HashMap<Integer, String>();
		/*hashmap.put(0, "Request Intitiated");
		hashmap.put(1, "InProgress");
		hashmap.put(2, "Completed");
		hashmap.put(3, "Rejected");
		hashmap.put(4, "Cancelled");
		hashmap.put(5, "Arbitrary");
		hashmap.put(102, "Error");*/
		
		hashmap.put(2, "Request received from Service");
		hashmap.put(3, "Request received on Ax and Sent for My Single Approval");
		hashmap.put(4, "Error,  See Error Message for details");
		hashmap.put(5, "Approved from My Single approval");
		hashmap.put(6, "Rejected from My Single approval");
		hashmap.put(7, "Partner/Dealer Created");
		
		
		/*
		 *  
2	Request received from Service 	   
3	Request received on Ax and Sent for My Single Approval	   
4	Error,  See Error Message for details	   
5	Approved from My Single approval	   
6	Rejected from My Single approval	   
7	Partner/Dealer Created	 

		 */
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public DealerHistoryDataDto getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final DealerHistoryDataDto data = getItem(position);

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = ((LayoutInflater) inflater).inflate(R.layout.dealer_history_items, null);
			holder.firmname = (TextView) convertView.findViewById(R.id.firmname);
			holder.citytown = (TextView) convertView.findViewById(R.id.citytown);
			holder.ownername = (TextView) convertView.findViewById(R.id.ownername);
			holder.ownermobile = (TextView) convertView.findViewById(R.id.ownermobile);
			holder.approvalstatus = (TextView) convertView.findViewById(R.id.approvalstatus);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		
		holder.firmname.setText(Html.fromHtml(data.getNameoffirm()).toString());
		holder.citytown.setText(data.getCitytown());
		holder.ownername.setText(Html.fromHtml(data.getOwnerName()).toString());
		holder.ownermobile.setText(data.getOwnerMob());

		try {
			holder.approvalstatus.setText(hashmap.get(data.getApprovalstatus()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	private static class ViewHolder {
		private TextView firmname, citytown, ownername, ownermobile, approvalstatus;
	}

}
