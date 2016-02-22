package com.samsung.ssc.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.ssc.R;


	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private final List<String> menus;
		 private List<Integer> imageids;
		 private List<Integer> gridviewids;
		public ImageAdapter(Context context, List<String> textvalue,List<Integer> imageids,List<Integer>gridviewids) {
			this.context = context;
			this.menus = textvalue;
			this.imageids=imageids;
			this.gridviewids=gridviewids;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
	 
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View gridView;
	 
			if (convertView == null) {
	 
				gridView = new View(context);
	 
				// get layout from mobile.xml
				gridView = inflater.inflate(R.layout.dashboardview, null);
	 
			} else {
				gridView = (View) convertView;
			}
			gridView.setId(gridviewids.get(position));
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(menus.get(position));
 
			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);
			imageView.setImageResource(imageids.get(position));
	 
			return gridView;
		}
	 
		@Override
		public int getCount() {
			return menus.size();
		}
	 
		@Override
		public Object getItem(int position) {
			return null;
		}
	 
		@Override
		public long getItemId(int position) {
			return 0;
		}
	 
	}


