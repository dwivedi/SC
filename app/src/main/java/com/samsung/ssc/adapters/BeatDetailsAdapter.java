package com.samsung.ssc.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.UserBeatDetailDto;

/**
 * 
 * @author ngupta1
 * 
 */
public class BeatDetailsAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<UserBeatDetailDto> beatList;
	private String selectedSpinnerdata = null;
	private boolean summary;

	public String getSelectedSpinnerdata() {
		return selectedSpinnerdata;
	}

	public void setSelectedSpinnerdata(String selectedSpinnerdata) {
		this.selectedSpinnerdata = selectedSpinnerdata;
	}

	public BeatDetailsAdapter(Context context,
			ArrayList<UserBeatDetailDto> beatList, boolean summary) {

		this.beatList = beatList;
		inflater = LayoutInflater.from(context);
		setSelectedSpinnerdata(selectedSpinnerdata);
		this.summary = summary;

	}

	@Override
	public int getCount() {
		return beatList.size();
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
		final UserBeatDetailDto data = beatList.get(position);

		if (convertView == null) {

			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.beat_details_list_items,
					null);
			holder.storeName = (TextView) convertView
					.findViewById(R.id.storeName);
			holder.beatDate = (TextView) convertView
					.findViewById(R.id.beatDate);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		if (summary) {
			holder.beatDate.setText(data.getDateRange());
			String[] storedetails = data.getStoreName().split(",");
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < storedetails.length; i++) {
				builder.append((i + 1) + ". " + storedetails[i] + "\n\n");
			}
			holder.storeName.setText(builder.toString());

		} else {

			holder.storeName.setText(data.getStoreName());
			String date=getFormateDate(data.getDateRange());
			if(date!=null)
			{
				holder.beatDate.setText(date);	
			}
			

		}

		return convertView;

	}

	static class ViewHolder {
		private TextView storeName, beatDate;
	}

	private String getFormateDate(String orginalDateString) {

		DateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy (EEEEE)",
				Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy (EEE)");
		Date date = null;
		String formattedDate = null;
		try {
			date = originalFormat.parse(orginalDateString);
			formattedDate = targetFormat.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return formattedDate;

	}
}
