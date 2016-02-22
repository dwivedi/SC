package com.samsung.ssc.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.samsung.ssc.util.Helper;

public class ActivityModuleAdapterListViewGridView extends BaseAdapter {

	private List<Module> dataset;
	private LayoutInflater inflater;
	private Context context;
	private boolean type;
	private Map<Integer, Integer> mapDownloadStatus;
	private final int QUESTIONIRE_TYPE_TEMP_MODULECODE = -1;

	public ActivityModuleAdapterListViewGridView(Context context) {
		this.context = context;
		this.dataset = new ArrayList<Module>();
		this.inflater = LayoutInflater.from(context);
	}

	public void setSelectionListorGrid(boolean type) {
		this.type = type;
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
			if (dataset.size() > 0) {
				this.dataset.addAll(dataset);
			}

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

		int downloadStatus = 1;

		if (convertView == null) {
			if (type) {
				convertView = inflater.inflate(R.layout.dashboardview, null);
			} else {
				convertView = inflater
						.inflate(R.layout.listdashboardview, null);
			}

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Module moduleDisplayDto = dataset.get(position);

		if (mapDownloadStatus != null
				&& (mapDownloadStatus.containsKey(moduleDisplayDto
						.getModuleCode()) | moduleDisplayDto.isQuestionType())) {
			if (moduleDisplayDto.isQuestionType()) {
				downloadStatus = mapDownloadStatus
						.get(QUESTIONIRE_TYPE_TEMP_MODULECODE);

			} else {
				downloadStatus = mapDownloadStatus.get(moduleDisplayDto
						.getModuleCode());
			}

		}

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

			} else {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					holder.icon.setBackground(context.getResources()
							.getDrawable(R.drawable.grid_item_selector));
				} else {
					holder.icon.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.grid_item_selector));
				}

			}

			if (resourceID != 0) {
				// holder.icon.setImageResource(resourceID);
				Drawable drawable = context.getResources().getDrawable(
						resourceID);
				holder.icon.setImageDrawable(drawable);

			} else {
				// Default
				Drawable drawable = context.getResources().getDrawable(
						R.drawable.refresh);
				holder.icon.setImageDrawable(drawable); // icon
			}

		} else {
			// holder.icon.setImageResource(R.drawable.refresh); // Default icon
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.refresh);
			holder.icon.setImageDrawable(drawable);
		}
		holder.text.setText(moduleDisplayDto.getName());

		if (downloadStatus == 0) {
			Drawable drawable = holder.icon.getDrawable();
			drawable = Helper.convertDrawableToGrayScale(drawable);
			if (drawable != null) {
				holder.icon.setImageDrawable(drawable);

			}

			holder.icon.setFocusable(true);
			holder.icon.setFocusableInTouchMode(true);
		}

		else {

			holder.icon.setFocusable(false);
			holder.icon.setFocusableInTouchMode(false);
		}
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

	public void clear() {
		this.dataset.clear();

	}

	public void setNewDownloadStatusMap(Map<Integer, Integer> Downloadstatus) {
		mapDownloadStatus = Downloadstatus;
		notifyDataSetChanged();
	}

}
