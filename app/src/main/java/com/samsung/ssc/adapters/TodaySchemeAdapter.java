package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.TodaySchemesModel;

public class TodaySchemeAdapter extends ArrayAdapter<TodaySchemesModel> {

	private LayoutInflater inflater;
	List<TodaySchemesModel> listTodaySchemes;

	public TodaySchemeAdapter(Context context,
			List<TodaySchemesModel> todaySchemes) {
		super(context, R.layout.listview_item_schemes, todaySchemes);

		this.inflater = (LayoutInflater) LayoutInflater.from(context);
		this.listTodaySchemes = todaySchemes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item_schemes,
					parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (listTodaySchemes != null) {

			TodaySchemesModel singleTodayScheme = listTodaySchemes
					.get(position);
			if (singleTodayScheme != null) {

				holder.tvSchemeTitle.setText(singleTodayScheme.title);

			}

		}
		return convertView;
	}

	class ViewHolder {
		public ViewHolder(View convertView) {
			tvSchemeTitle = (TextView) convertView
					.findViewById(R.id.listTextView);

		}

		TextView tvSchemeTitle;

	}
}
