package com.samsung.ssc.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.StoreBasicModel;

public class OutletWorkingOtherStoreAdapter extends
		ArrayAdapter<StoreBasicModel> {
	private List<StoreBasicModel> mLsOtherStoresBasic;
	private List<StoreBasicModel> mLsOtherStoresBasictemp;
	private LayoutInflater inflater;
	private Context context;

	private String filter_characters = "";

	public OutletWorkingOtherStoreAdapter(Context context,
			List<StoreBasicModel> todaysBeatList) {
		super(context, R.layout.other_store_list_view_item, todaysBeatList);

		this.mLsOtherStoresBasictemp = new ArrayList<StoreBasicModel>();

		// this.mLsOtherStoresBasic=new ArrayList<StoreBasicModel>();
		mLsOtherStoresBasic = todaysBeatList;
		mLsOtherStoresBasictemp.addAll(todaysBeatList);

		// mLsOtherStoresBasictemp.addAll(todaysBeatList);

		this.inflater = (LayoutInflater) LayoutInflater.from(context);
		this.context = context;

	}

	@Override
	public int getCount() {

		return mLsOtherStoresBasic.size();
	}

	public void addSotres(List<StoreBasicModel> storeList) {
		mLsOtherStoresBasic.clear();
		mLsOtherStoresBasictemp.clear();

		mLsOtherStoresBasic = storeList;
		mLsOtherStoresBasictemp.addAll(storeList);

		notifyDataSetChanged();
	}

	@Override
	public StoreBasicModel getItem(int position) {

		return mLsOtherStoresBasic.get(position);
	}

	/*
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) {
	 * 
	 * ViewHolder holder;
	 * 
	 * if (convertView == null) { convertView =
	 * inflater.inflate(R.layout.other_store_list_view_item, parent, false);
	 * holder = new ViewHolder(convertView); convertView.setTag(holder); } else
	 * { holder = (ViewHolder) convertView.getTag(); } if (mLsOtherStoresBasic
	 * != null) { try { StoreBasicModel todayStoreBasic = mLsOtherStoresBasic
	 * .get(position); if (todayStoreBasic != null) {
	 * 
	 * holder.tvStoreCode.setText(todayStoreBasic.getStoreCode());
	 * 
	 * int startPos = todayStoreBasic.getStoreName() .toLowerCase(Locale.US)
	 * .indexOf(filter_characters.toLowerCase(Locale.US)); int endPos = startPos
	 * + filter_characters.length();
	 * 
	 * if (startPos != -1 && !filter_characters.equalsIgnoreCase("")) {
	 * Spannable spannable = new SpannableString(
	 * todayStoreBasic.getStoreName()); ColorStateList blueColor = new
	 * ColorStateList( new int[][] { new int[] {} }, new int[] {
	 * context.getResources().getColor( R.color.search_text_high_light_color)
	 * }); TextAppearanceSpan highlightSpan = new TextAppearanceSpan( null,
	 * Typeface.BOLD, -1, blueColor, null);
	 * 
	 * spannable.setSpan(highlightSpan, startPos, endPos,
	 * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	 * holder.tvStoreName.setText(spannable); } else {
	 * holder.tvStoreName.setText(todayStoreBasic .getStoreName()); }
	 * 
	 * } } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } return convertView; }
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.other_store_list_view_item,
					parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mLsOtherStoresBasic != null) {
			try {
				StoreBasicModel todayStoreBasic = mLsOtherStoresBasic
						.get(position);
				if (todayStoreBasic != null) {

					holder.tvStoreCode.setText(todayStoreBasic.getStoreCode());

					double distance = todayStoreBasic.getStoreDistance();

					if (distance == 1000) {
						holder.tvDistance.setText("Distance: NA");
					} else {
						DecimalFormat df = new DecimalFormat("#.##");
						distance = Double.valueOf(df.format(distance));
						holder.tvDistance.setText("Distance: " + distance
								+ " KM");
					}

					int startPos = todayStoreBasic.getStoreName()
							.toLowerCase(Locale.US)
							.indexOf(filter_characters.toLowerCase(Locale.US));
					int endPos = startPos + filter_characters.length();

					holder.tvStoreCode.setTextColor(todayStoreBasic
							.getStoreColor());

					if (todayStoreBasic.isCoverage()) {

						holder.tvStoreCode.setTextColor(todayStoreBasic
								.getStoreColor());
						holder.tvStoreName.setTextColor(todayStoreBasic
								.getStoreColor());

						holder.tvStoreName.setPaintFlags(holder.tvStoreName
								.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					} else {
						holder.tvStoreName.setTextColor(todayStoreBasic
								.getStoreColor());
						holder.tvStoreCode.setTextColor(todayStoreBasic
								.getStoreColor());

						holder.tvStoreName.setPaintFlags(holder.tvStoreName
								.getPaintFlags()
								& (~Paint.STRIKE_THRU_TEXT_FLAG));

					}
					if (startPos != -1
							&& !filter_characters.equalsIgnoreCase("")) {
						Spannable spannable = new SpannableString(
								todayStoreBasic.getStoreName());
						ColorStateList blueColor = new ColorStateList(
								new int[][] { new int[] {} },
								new int[] { context.getResources().getColor(
										R.color.search_text_high_light_color) });
						TextAppearanceSpan highlightSpan = new TextAppearanceSpan(
								null, Typeface.BOLD, -1, blueColor, null);

						spannable.setSpan(highlightSpan, startPos, endPos,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						holder.tvStoreName.setText(spannable);
					} else {
						holder.tvStoreName.setText(todayStoreBasic
								.getStoreName());
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return convertView;
	}

	class ViewHolder {
		public ViewHolder(View convertView) {
			tvStoreName = (TextView) convertView
					.findViewById(R.id.tv_name_otherStoreListItem);
			tvStoreCode = (TextView) convertView
					.findViewById(R.id.tv_code_otherStoreListItem);
			tvDistance = (TextView) convertView
					.findViewById(R.id.tv_distance_otherStoreListItem);

		}

		TextView tvStoreName, tvStoreCode, tvDistance;

	}

	public void filter(String charText) {
		try {
			this.filter_characters = charText;
			charText = charText.toLowerCase(Locale.getDefault());
			mLsOtherStoresBasic.clear();
			if (charText.length() == 0) {
				mLsOtherStoresBasic.addAll(mLsOtherStoresBasictemp);
			} else {
				for (StoreBasicModel bsm : mLsOtherStoresBasictemp) {
					if (bsm.getStoreName().toLowerCase(Locale.getDefault())
							.contains(charText)) {
						mLsOtherStoresBasic.add(bsm);
					}
				}
			}
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
