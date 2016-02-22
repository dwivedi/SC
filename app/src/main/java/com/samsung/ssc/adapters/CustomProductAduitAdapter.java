package com.samsung.ssc.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.activitymodule.RaceProductAuditActivity;
import com.samsung.ssc.activitymodule.RaceProductAuditCartActivity1;
import com.samsung.ssc.dto.RaceProductDataModal;

public class CustomProductAduitAdapter extends BaseAdapter {

	private ArrayList<RaceProductDataModal> raceProductList;
	private LayoutInflater inflater;
	private String filter_characters = "";
	private ArrayList<RaceProductDataModal> raceProductListtemp;
	private Context mContext;
	private RaceProductAuditActivity mActivity;

	public CustomProductAduitAdapter(Context context,RaceProductAuditActivity activity) {

		this.raceProductList = new ArrayList<RaceProductDataModal>();
		this.raceProductListtemp = new ArrayList<RaceProductDataModal>();
		inflater = LayoutInflater.from(context);
		mContext = context;
		mActivity=activity;
	}

	@Override
	public int getCount() {
		return raceProductList.size();
	}

	@Override
	public RaceProductDataModal getItem(int position) {
		return raceProductList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	public void addItems(ArrayList<RaceProductDataModal> raceProductData) {

		raceProductList.clear();
		raceProductListtemp.clear();

		if (raceProductData != null) {
			raceProductList.addAll(raceProductData);
			raceProductListtemp.addAll(raceProductData);
			filter_characters="";
		}
		notifyDataSetChanged();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			vi = inflater.inflate(R.layout.race_product_list_item, null);
			holder = new ViewHolder();
			holder.txtProductName = (TextView) vi
					.findViewById(R.id.tvListItemRaceProductName);
			// holder.ivCrossButton = (ImageView)
			// vi.findViewById(R.id.ivListItenRaceProductCrossBtn);

			vi.setTag(holder);

		} else {
			holder = (ViewHolder) vi.getTag();
		}

		if (raceProductList != null) {
			RaceProductDataModal raceProduct = raceProductList.get(position);

			if (raceProduct != null) {

				holder.txtProductName.setText(raceProduct.getProductName());

				int startPos = raceProduct.getProductName()
						.toLowerCase(Locale.US)
						.indexOf(filter_characters.toLowerCase(Locale.US));
				int endPos = startPos + filter_characters.length();

				if (startPos != -1 && !filter_characters.equalsIgnoreCase("")) {
					Spannable spannable = new SpannableString(
							raceProduct.getProductName());
					ColorStateList blueColor = new ColorStateList(
							new int[][] { new int[] {} },
							new int[] { mContext.getResources().getColor(
									R.color.search_text_high_light_color) });
					TextAppearanceSpan highlightSpan = new TextAppearanceSpan(
							null, Typeface.BOLD, -1, blueColor, null);

					spannable.setSpan(highlightSpan, startPos, endPos,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					holder.txtProductName.setText(spannable);
				} else {
					holder.txtProductName.setText(raceProduct.getProductName());
				}

			}
		}

		return vi;

	}

	class ViewHolder {
		TextView txtProductName;
		// ImageView ivCrossButton;
	}

	public void filter(String charText) {
		this.filter_characters = charText;
		charText = charText.toLowerCase(Locale.getDefault());
		raceProductList.clear();
		if (charText.length() == 0) {
			raceProductList.addAll(raceProductListtemp);
		} else {
			for (RaceProductDataModal bsd : raceProductListtemp) {
				if (bsd.getProductName().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					raceProductList.add(bsd);
				}
				
				
				/*if (bsd.getProductName().toLowerCase(Locale.getDefault())
						.startsWith(charText)) {
					raceProductList.add(bsd);
				}*/
			}
			
			// if no match found in the current search
			if(raceProductList.isEmpty())
			{
				// reset filter character
				filter_characters="";
				mActivity.getProductByProductGroupAndSerachText(charText);
			}
		}
		
		notifyDataSetChanged();
	}

	public void clearAll() {
		raceProductList.clear();
		notifyDataSetChanged();
	}

}
