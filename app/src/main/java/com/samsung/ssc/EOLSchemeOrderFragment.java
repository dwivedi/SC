package com.samsung.ssc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.samsung.ssc.dto.EOLSchemeOrderDTO;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EOLSchemeOrderFragment extends Fragment {

	EOLNotificationShow mParentActivity;

	Button mBtViewMore, mBtExit;

	LayoutInflater mInflater;

	ListView mLvSchemeDetail;

	private View _rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		mInflater = inflater;

		mParentActivity = (EOLNotificationShow) getActivity();

		if (_rootView == null) {
			_rootView = inflater.inflate(
					R.layout.eol_scheme_order_product_fragment, container,
					false);
			setUpView(_rootView);
		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		return _rootView;

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
				try {
					mLvSchemeDetail.setAdapter(new SchemeOrderProductAdapter(
							mParentActivity.mEolSchemeDto.getSchemeOrders()));
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
		}

		return view;

	}

	private class SchemeOrderProductAdapter extends BaseAdapter {

		private ArrayList<EOLSchemeOrderDTO> al_scheme_order;

		public SchemeOrderProductAdapter(ArrayList<EOLSchemeOrderDTO> al)
				throws CloneNotSupportedException {
			this.al_scheme_order = al;

			Collections
					.sort(al_scheme_order, new EOLSchemeOrderDTOComparator());

			int size = al_scheme_order.size();
			ArrayList<EOLSchemeOrderDTO> temp_al_scheme_order = new ArrayList<EOLSchemeOrderDTO>();
			int tempStoreID = 0;
			for (int i = 0; i < size; i++) {
				EOLSchemeOrderDTO data = al_scheme_order.get(i);
				if (tempStoreID != data.getStoreID()) {

					EOLSchemeOrderDTO tempData = (EOLSchemeOrderDTO) data
							.clone();
					tempData.setHeaderType(true);
					temp_al_scheme_order.add(tempData);
					tempStoreID = tempData.getStoreID();
				}

				data.setHeaderType(false);
				temp_al_scheme_order.add(data);

			}

			al_scheme_order.clear();
			al_scheme_order = temp_al_scheme_order;

		}

		@Override
		public int getItemViewType(int position) {

			return al_scheme_order.get(position).isHeaderType() ? 1 : 0;

		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getCount() {

			return al_scheme_order.size();
		}

		@Override
		public EOLSchemeOrderDTO getItem(int position) {

			return al_scheme_order.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolderHeader holderHeader;
			ViewHolderItem holderItem;

			EOLSchemeOrderDTO item = al_scheme_order.get(position);

			if (1 == getItemViewType(position)) {

				try {
					if (convertView == null) {

						convertView = mInflater
								.inflate(
										R.layout.eol_scheme_order_product_listview_header,
										parent, false);
						holderHeader = new ViewHolderHeader(convertView);
						convertView.setTag(holderHeader);

					} else {
						holderHeader = (ViewHolderHeader) convertView.getTag();
					}

					holderHeader.tv_header.setText(item.getStoreName());
				} catch (Exception e) {

					e.printStackTrace();
				}

			} else {

				try {
					if (convertView == null) {

						convertView = mInflater
								.inflate(
										R.layout.eol_scheme_order_product_listview_item,
										parent, false);
						holderItem = new ViewHolderItem(convertView);
						convertView.setTag(holderItem);

					} else {
						holderItem = (ViewHolderItem) convertView.getTag();
					}

					holderItem.tv_product.setText(item.getBasicModelCode());
					holderItem.tv_qty.setText(String.valueOf(item
							.getOrderQunatity()));
					holderItem.tv_actual_support.setText(String.valueOf(item
							.getActualSupport()));
				} catch (Exception e) {

					e.printStackTrace();
				}

			}

			return convertView;

		}

		class ViewHolderItem {

			TextView tv_product, tv_qty, tv_actual_support;

			public ViewHolderItem(View convertView) {

				tv_product = (TextView) convertView
						.findViewById(R.id.tv_product_name_eolSchemeOrderProductListviewItem);
				tv_qty = (TextView) convertView
						.findViewById(R.id.tv_qty_eolSchemeOrderProductListviewItem);

				tv_actual_support = (TextView) convertView
						.findViewById(R.id.tv_actual_support_eolSchemeOrderProductListviewItem);

			}

		}

		class ViewHolderHeader {

			TextView tv_header;

			public ViewHolderHeader(View convertView) {

				tv_header = (TextView) convertView
						.findViewById(R.id.tv_header_separator_eolSchemeOrderProductListviewItem);

			}

		}

	}

	class EOLSchemeOrderDTOComparator implements Comparator<EOLSchemeOrderDTO> {
		@Override
		public int compare(EOLSchemeOrderDTO o1, EOLSchemeOrderDTO o2) {
			if (o1.getStoreID() > o2.getStoreID()) {
				return -1;
			} else if (o1.getStoreID() < o2.getStoreID()) {
				return 1;
			}
			return 0;
		}
	}

}
