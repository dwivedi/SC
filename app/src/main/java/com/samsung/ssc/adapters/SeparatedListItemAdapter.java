package com.samsung.ssc.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;

public class SeparatedListItemAdapter extends ArrayAdapter<String> {
	public Context context;
	public ArrayList<String> groupItems;
	private LayoutInflater inflater;

	public SeparatedListItemAdapter(Context context, int resource,
			ArrayList<String> groupItems) {
		super(context, resource, groupItems);
		this.context = context;
		this.groupItems = groupItems;
		 inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (groupItems != null)
			return groupItems.size();

		return 0;
	}

	@Override
	public String getItem(int position) {
		return groupItems.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View myConvertView = null;
		try {
			final String itemText = groupItems.get(position);
			myConvertView = convertView;

			if (myConvertView == null) {
				
				myConvertView = inflater.inflate(
						R.layout.separated_list_item_view, null);
			}
			TextView listItem = (TextView) myConvertView
					.findViewById(R.id.separated_list_item);

			listItem.setText(itemText);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return myConvertView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
