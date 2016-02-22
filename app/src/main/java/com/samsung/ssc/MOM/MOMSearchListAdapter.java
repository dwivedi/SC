package com.samsung.ssc.MOM;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.MOMDetailModal;

public class MOMSearchListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public ArrayList<MOMDetailModal> dataSet;
	private SparseBooleanArray mSelectedItemsIds;

	public MOMSearchListAdapter(Context context) {
		this.mSelectedItemsIds = new SparseBooleanArray();
		this.inflater = LayoutInflater.from(context);
		this.dataSet = new ArrayList<MOMDetailModal>();
	}

	@Override
	public int getCount() {
		return dataSet.size();
	}

	public void toggleSelection(int position) {
		selectView(position, !mSelectedItemsIds.get(position));
	}

	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);
		notifyDataSetChanged();
	}
	public void remove(MOMDetailModal object) {
		dataSet.remove(object);
		notifyDataSetChanged();
	}

	public int getSelectedCount() {
		return mSelectedItemsIds.size();
	}

	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}

	@Override
	public Object getItem(int position) {
		return dataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected void addItem(MOMDetailModal object) {
		this.dataSet.add(object);
		notifyDataSetChanged();

	}

	public void addItem(ArrayList<MOMDetailModal> objects) {
		this.dataSet.clear();
		this.dataSet.addAll(objects);
		notifyDataSetChanged();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_view_mom_search_result,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		convertView.setBackgroundColor(Color.WHITE);
		
		if (mSelectedItemsIds.get(position)) {
			convertView.setBackgroundColor(Color.CYAN);
		}
		
		MOMDetailModal dateModal = dataSet.get(position);

		holder.tvMOMTitle.setText(dateModal.momTitle);
		holder.tvMOMDate.setText(dateModal.momDate);

		return convertView;
	}

	class ViewHolder {
		public ViewHolder(View convertView) {
			this.tvMOMTitle = (TextView) convertView
					.findViewById(R.id.tvMOMRowTitle);
			this.tvMOMDate = (TextView) convertView
					.findViewById(R.id.tvMOMRowDate);

		}

		TextView tvMOMTitle, tvMOMDate;
	}

}
