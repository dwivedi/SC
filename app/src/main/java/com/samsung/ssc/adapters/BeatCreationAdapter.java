package com.samsung.ssc.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.BeatCreationDto;
import com.samsung.ssc.util.Helper;
 

public class BeatCreationAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<BeatCreationDto> storesList;
	private static HashMap<String, ArrayList<String>> map;
	private String selectedSpinnerdata;
	private double month;
	private int year;

	private TextView tvTotalViewCount;

	/**
	 * @return the selectedSpinnerdata
	 */
	public String getSelectedSpinnerdata() {
		return selectedSpinnerdata;
	}

	/**
	 * @param selectedSpinnerdata
	 *            the selectedSpinnerdata to set
	 */
	public void setSelectedSpinnerdata(String selectedSpinnerdata) {
		this.selectedSpinnerdata = selectedSpinnerdata;
	}

	private Context context;

	public BeatCreationAdapter(Context context,
			ArrayList<BeatCreationDto> storeData, int year, double month,
			TextView tvTotalViewCount) {
		this.context = context;
		this.storesList = storeData;
		inflater = LayoutInflater.from(context);
		this.month = month;
		this.year = year;
		this.tvTotalViewCount = tvTotalViewCount;
		if (map == null) {
			map = new HashMap<String, ArrayList<String>>();
		}
	}

	@Override
	public int getCount() {
		return storesList.size();
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
		final BeatCreationDto data = storesList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.beat_list_items, null);
			holder.store = (TextView) convertView.findViewById(R.id.storeName);
			// holder.beatSummay = (TextView)
			// convertView.findViewById(R.id.beatSummary);

			holder.beatDate = (EditText) convertView
					.findViewById(R.id.beatDate);
			holder.checkBeat = (CheckBox) convertView
					.findViewById(R.id.selectBeat);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.checkBeat.setChecked(data.isStatus());

		if (/* !Helper.isNullOrEmpty(data.getStoreName()) */data.getStoreName() != null) {
			holder.store.setText(data.getStoreName() + " ("
					+ data.getStoreCode() + ")");
		}

		// if(!Helper.isNullOrEmpty(data.getBeatSummary())){
		// holder.beatSummay.setText(data.getBeatSummary());
		// }

		if (map.size() > 0) {
			if (map.get(data.getStoreId()) != null) {
				holder.beatDate.setText(map.get(data.getStoreId()).toString()
						.replace("[", "").replace("]", "").replace(", ", ","));

			} else {
				holder.beatDate.setText("");
			}
		} else {
			holder.beatDate.setText("");
		}

		// EditTextWatcher watcher = new EditTextWatcher();
		// holder.beatDate.addTextChangedListener(watcher);
		// watcher.setTarget(position);

		// for (int i = 0; i < getCount(); i++) {
		// try {
		//
		// if (compData.size() != 0) {
		//
		// holder.beatDate.setText("" + compData.get(position).getBrand1());
		//
		// }
		// }
		//
		//
		//
		// catch (Exception e) {
		// Helper.printLog("getView beat creation adapter", "" + e);
		// }
		// }

		holder.checkBeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				data.setStatus(!data.isStatus());

				// ((CheckBox) arg0).setChecked(data.isStatus());

				ArrayList<String> list = new ArrayList<String>();
				String key = data.getStoreId();
				if (data.isStatus()) {
					if (map.get(key) != null) {
						list.addAll(map.get(key));
						if (!list.contains(getSelectedSpinnerdata())) {
							list.add(getSelectedSpinnerdata());
						}

						map.put(key, list);
					} else {
						list.add(getSelectedSpinnerdata());
						map.put(key, list);

					}
					Helper.printLog("map checked", map.toString());
					// i++;
					// totalview.setText("Total : "+i);

				} else {
					if (map.size() > 0) {
						list = (ArrayList<String>) map.get(key);
						if (list.contains(getSelectedSpinnerdata())) {
							list.remove(getSelectedSpinnerdata());
						}
						if (list.isEmpty()) {
							map.remove(key);
						} else {

							map.put(key, list);
						}
						Helper.printLog("map unchecked", map.toString());
					}
					// if(i>0){
					// i--;
					// totalview.setText("Total : "+i);
					// }
				}
				if (map.size() > 0) {
					if (map.get(key) != null) {
						holder.beatDate.setText(map.get(key).toString()
								.replace("[", "").replace("]", "")
								.replace(", ", ","));

					} else {
						holder.beatDate.setText("");
					}
				} else {
					holder.beatDate.setText("");
				}

				if (map.size() > 0) {
					int total = 0;
					for (Entry<String, ArrayList<String>> entry : map
							.entrySet()) {
						ArrayList<String> listdata = entry.getValue();

						if (listdata.contains(selectedSpinnerdata)) {
							total++;
						}
					}
					tvTotalViewCount.setText("Total Outlets: " + total);

				}else{
					tvTotalViewCount.setText("Total Outlets: 0");
				}

			}
		});

		return convertView;

	}

	static class ViewHolder {
		TextView store;// , beatSummay
		EditText beatDate;
		CheckBox checkBeat;
		boolean ischecked;

	}


	public ArrayList<JSONObject> getselecteddata() {
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		DecimalFormat mFormat = new DecimalFormat("00");
		for (Entry<String, ArrayList<String>> entry : map.entrySet()) {

			ArrayList<String> dateslist = entry.getValue();
			for (int i = 0; i < dateslist.size(); i++) {
				JSONObject jsonobject = new JSONObject();
				if (!entry.getKey().equals("")) {
					try {
						jsonobject.put(WebConfig.WebParams.COMPANY_ID_CAPS, Helper
								.getStringValuefromPrefs(context,
										SharedPreferencesKey.PREF_COMPANYID));
						jsonobject.put("StoreID", entry.getKey());
						jsonobject.put("UserID", Helper
								.getStringValuefromPrefs(context,
										SharedPreferencesKey.PREF_USERID));
						jsonobject.put(
								"PlanDate",
								mFormat.format(month)
										+ " "
										+ mFormat.format(Double
												.valueOf(dateslist.get(i)
														.toString())) + " "
										+ year);
						jsonobject.put("IsCoverage", false);
						jsonobject.put("StatusID", "0");
						jsonobject.put("Remarks", "beat created");
					} catch (JSONException e) {
						Helper.printStackTrace(e);
					}
					list.add(jsonobject);
				}
			}

		}
		return list;

	}

	public HashMap<String, ArrayList<String>> getdataMap() {
		return map;
	}

	public HashMap<String, ArrayList<String>> getdataMapinverse() {
		HashMap<String, ArrayList<String>> map2 = new LinkedHashMap<String, ArrayList<String>>();
		for (Entry<String, ArrayList<String>> entry : map.entrySet()) {
			for (String v : entry.getValue()) {
				ArrayList<String> list2 = map2.get(v);
				if (list2 == null)
					map2.put(v, list2 = new ArrayList<String>());
				list2.add(entry.getKey());
			}
		}

		return map2;
	}

	public void clearMap() {
		map.clear();

	}

	public void setTotal(TextView textView) {

		if (map.size() > 0) {
			int total = 0;
			for (Entry<String, ArrayList<String>> entry : map.entrySet()) {
				ArrayList<String> listdata = entry.getValue();

				if (listdata.contains(selectedSpinnerdata)) {
					total++;
				}
			}
			textView.setText("Total Outlets: " + total);

		} else {
			textView.setText("Total Outlets: " + 0);
		}
	}

}
