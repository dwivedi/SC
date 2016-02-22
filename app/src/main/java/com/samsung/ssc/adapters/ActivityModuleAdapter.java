package com.samsung.ssc.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.sync.SyncUtils;

public class ActivityModuleAdapter extends BaseAdapter {

	private List<Module> dataset;
	private LayoutInflater inflater;
	private Context context;

	public ActivityModuleAdapter(Context context) {
		this.context = context;
		this.dataset = new ArrayList<Module>();
		this.inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return dataset.size();
	}

	@Override
	public Object getItem(int position) {
		return dataset.get(position);
	}

	public void addModules(List<Module> dataset) {
		try {
			this.dataset.clear();
			this.dataset.addAll(dataset);
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addModule(Module module) {
		try {
			this.dataset.add(module);
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.dashboardview, null);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Module moduleDisplayDto = dataset.get(position);

		String icon_name = moduleDisplayDto.getIconName();

		if (icon_name != null && !icon_name.isEmpty()) {

			String iconSplitpng = icon_name.split("\\.")[0];
			int resourceID = context.getResources().getIdentifier(iconSplitpng,
					"drawable", context.getPackageName());

			if (moduleDisplayDto.isIsMandatory()) {
				if (moduleDisplayDto.isActivityIDAvailable()) {

					if (moduleDisplayDto.getSyncStatus() == SyncUtils.SYNC_STATUS_SUBMIT) {

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							holder.icon
									.setBackground(context
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_green_border));
						} else {
							holder.icon
									.setBackgroundDrawable(context
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_green_border));

						}

					} else if (moduleDisplayDto.getSyncStatus() == SyncUtils.SYNC_STATUS_SAVED) {

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							holder.icon
									.setBackground(context
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_yellow_border));
						} else {

							holder.icon
									.setBackgroundDrawable(context
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_yellow_border));
						}

					}

				} else {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						holder.icon
								.setBackground(context
										.getResources()
										.getDrawable(
												R.drawable.grid_item_selector_red_border));
					} else {
						holder.icon
								.setBackgroundDrawable(context
										.getResources()
										.getDrawable(
												R.drawable.grid_item_selector_red_border));
					}

				}

			}

			if (resourceID != 0) {
				holder.icon.setImageResource(resourceID);
			} else {
				holder.icon.setImageResource(R.drawable.refresh); // Default
																	// icon
			}
		} else {
			holder.icon.setImageResource(R.drawable.refresh); // Default icon
		}
		holder.text.setText(dataset.get(position).getName());

		// convertView.startAnimation(animation);

		return convertView;
	}

	class ViewHolder {
		public ViewHolder(View convertView) {

			this.text = (TextView) convertView
					.findViewById(R.id.grid_item_label);
			this.icon = (ImageButton) convertView
					.findViewById(R.id.grid_item_image);

			this.icon.setFocusable(false);
			this.icon.setFocusableInTouchMode(false);
			this.icon.setClickable(false);
			this.icon.setEnabled(false);

		}

		TextView text;
		ImageButton icon;
	}
}