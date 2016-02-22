package com.samsung.ssc;

import java.util.ArrayList;

import com.samsung.ssc.dto.EOLSchemeDetailDTO;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EOLSchemeProductFragment extends Fragment {

	EOLNotificationShow mParentActivity;

	Button mBtViewMore, mBtExit;

	LayoutInflater mInflater;

	ListView mLvSchemeDetail;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		mInflater = inflater;

		mParentActivity = (EOLNotificationShow) getActivity();

		View view = inflater.inflate(R.layout.eol_scheme_product_fragment,
				container, false);

		return setUpView(view);

	}

	private View setUpView(View view) {

		if (view == null) {
			return null;
		}

		mBtViewMore = (Button) view.findViewById(R.id.viewMore_eolNotification);
		mBtExit = (Button) view.findViewById(R.id.exit_eolNotification);
		mLvSchemeDetail = (ListView) view
				.findViewById(R.id.lv_product_eolNotification);

		if (mParentActivity.fromGCM) {
			mBtViewMore.setVisibility(View.VISIBLE);

		} else {
			mBtViewMore.setVisibility(View.GONE);

		}

		if (mParentActivity.mEolSchemeDto != null) {

			if (mParentActivity.mEolSchemeDto.getScheemDetails().size() > 0)
				mLvSchemeDetail.setAdapter(new SchemeDetailAdapter(
						mParentActivity.mEolSchemeDto.getScheemDetails()));
		}

		return view;

	}


	private class SchemeDetailAdapter extends BaseAdapter {

		private ArrayList<EOLSchemeDetailDTO> al_scheme_detail;

		public SchemeDetailAdapter(ArrayList<EOLSchemeDetailDTO> al) {
			this.al_scheme_detail = al;

		}

		@Override
		public int getCount() {

			return al_scheme_detail.size();
		}

		@Override
		public EOLSchemeDetailDTO getItem(int position) {

			return al_scheme_detail.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.eol_scheme_listview_item, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			EOLSchemeDetailDTO item = al_scheme_detail.get(position);
			holder.tv_product.setText(item.getBasicModelCode());
			holder.tv_max_support.setText(item.getQuatity() + "");
			holder.tv_support_per_unit
					.setText(item.getSupport() + "");

			return convertView;
		}

		class ViewHolder {

			TextView tv_product, tv_max_support, tv_support_per_unit;

			public ViewHolder(View convertView) {
				tv_product = (TextView) convertView
						.findViewById(R.id.tv_product_name_eolSchemeDetailListviewItem);
				tv_max_support = (TextView) convertView
						.findViewById(R.id.tv_max_support_eolSchemeDetailListviewItem);
				tv_support_per_unit = (TextView) convertView
						.findViewById(R.id.tv_support_per_unit_eolSchemeDetailListviewItem);
			}

		}

	}

}
