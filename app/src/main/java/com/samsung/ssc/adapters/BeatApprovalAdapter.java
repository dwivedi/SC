package com.samsung.ssc.adapters;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.EmployeeListDto;

/**
 * 
 * @author ngupta1
 * 
 */
public class BeatApprovalAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private HashMap<Integer, EmployeeListDto> employeeMap;


	public BeatApprovalAdapter(Context context,
			HashMap<Integer, EmployeeListDto> employeeList) {
		
		this.employeeMap = employeeList;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return employeeMap.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}//

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.beat_approval_list_items,
					null);
			holder.store = (TextView) convertView.findViewById(R.id.storeName);
			holder.checkBeat = (CheckBox) convertView
					.findViewById(R.id.selectBeat);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.store.setText(employeeMap.get(position).getFirstName() + " " + employeeMap.get(position).getLastName());
		

		holder.checkBeat
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean checked) {
						

						if (checked) {
							
							if(employeeMap.get(position).getUserId() != null){
								employeeMap.get(position).setUserId(employeeMap.get(position).getUserId());
							}
							
							if(employeeMap.get(position).getFirstName() != null){
								employeeMap.get(position).setFirstName(employeeMap.get(position).getFirstName());
							}
							
							if(employeeMap.get(position).getLastName() != null){
								employeeMap.get(position).setLastName(employeeMap.get(position).getLastName());
							}
							
							if(employeeMap.get(position).getCoverageType() != null){
								employeeMap.get(position).setCoverageType(employeeMap.get(position).getCoverageType());
							}
							
							if(employeeMap.get(position).getMarketOffDays() != null){
								employeeMap.get(position).setMarketOffDays(employeeMap.get(position).getMarketOffDays());
							}
							
							if(employeeMap.get(position).getStatusID() != null){
								employeeMap.get(position).setStatusID(employeeMap.get(position).getStatusID());
							}
							
							

						} else {
							
							
							if(employeeMap.get(position).getUserId() != null){
								employeeMap.get(position).setUserId(employeeMap.get(position).getUserId());
							}
							
							if(employeeMap.get(position).getFirstName() != null){
								employeeMap.get(position).setFirstName(employeeMap.get(position).getFirstName());
							}
							
							if(employeeMap.get(position).getLastName() != null){
								employeeMap.get(position).setLastName(employeeMap.get(position).getLastName());
							}
							
							if(employeeMap.get(position).getCoverageType() != null){
								employeeMap.get(position).setCoverageType(employeeMap.get(position).getCoverageType());
							}
							
							if(employeeMap.get(position).getMarketOffDays() != null){
								employeeMap.get(position).setMarketOffDays(employeeMap.get(position).getMarketOffDays());
							}
							
							if(employeeMap.get(position).getStatusID() != null){
								employeeMap.get(position).setStatusID(employeeMap.get(position).getStatusID());
							}
							
							
						}
						
						employeeMap.get(position).setStatus(checked);
						employeeMap.put(position, employeeMap.get(position));
						

					}
				});

		return convertView;

	}

	static class ViewHolder {
		TextView store;
		CheckBox checkBeat;
		boolean ischecked;

	}

//	public ArrayList<JSONObject> getselecteddata() {
//		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
//
//		for (Entry<String, ArrayList<String>> entry : map.entrySet()) {
//
//			JSONObject jsonobject = new JSONObject();
//			try {
//				jsonobject.put(Constants.COMPANYID, Helper
//						.getStringValuefromPrefs(context,
//								Constants.PREF_COMPANYID));
//				jsonobject.put("DealerID", entry.getKey());
//				jsonobject.put("UserID", Helper.getStringValuefromPrefs(
//						context, Constants.PREF_USERID));
//				// jsonobject.put("CoverageDate",entry.getValue().toString());
//				jsonobject.put("CoverageDate", "21-03-2013");
//				jsonobject.put("IsCoverage", false);
//				jsonobject.put("StatusID", false);
//				jsonobject.put("Remarks", "beat created");
//			} catch (JSONException e) {
//
//				e.printStackTrace();
//			}
//
//			list.add(jsonobject);
//
//		}
//		return list;
//
//	}

}
