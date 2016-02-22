package com.samsung.ssc;

import java.util.LinkedHashMap;

import com.samsung.ssc.dto.EOLSchemeDTO;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EOLSchemeDetailFragment extends Fragment {

	EOLNotificationShow mParentActivity;

	Button mBtViewMore, mBtExit;

	LinearLayout mSchemeHeaderContainer;

	LayoutInflater mInflater;

	LinearLayout scheme_detail_item;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		mInflater = inflater;

		mParentActivity = (EOLNotificationShow) getActivity();

		View view = inflater.inflate(R.layout.eol_scheme_detail_fragment,
				container, false);

		return setUpView(view);

	}

	private View setUpView(View view) {

		if (view == null) {
			return null;
		}

		mBtViewMore = (Button) view.findViewById(R.id.viewMore_eolNotification);
		mBtExit = (Button) view.findViewById(R.id.exit_eolNotification);
		mSchemeHeaderContainer = (LinearLayout) view
				.findViewById(R.id.ll_schemeDetailContainer_eolNotification);
		if (mParentActivity.fromGCM) {
			mBtViewMore.setVisibility(View.VISIBLE);

		} else {
			mBtViewMore.setVisibility(View.GONE);

		}
		if (mParentActivity.mEolSchemeDto != null) {
			showScheme(mParentActivity.mEolSchemeDto);
		}
		return view;

	}

	private void showScheme(EOLSchemeDTO EOLScheme) {

		LinkedHashMap<String, String> map = EOLScheme.getValueMap();

		for (String key : map.keySet()) {
			String value = map.get(key);
			if (!TextUtils.isEmpty(value)) {

				scheme_detail_item = (LinearLayout) mInflater.inflate(
						R.layout.eol_scheme_detail_list_item, null);

				TextView tv_name = (TextView) scheme_detail_item
						.findViewById(R.id.tv1_scheme_detail_name_eolSchemeDetailListviewItem);
				TextView tv_value = (TextView) scheme_detail_item
						.findViewById(R.id.tv1_scheme_detail_value_eolSchemeDetailListviewItem);

				tv_name.setText(key);
				tv_value.setText(value);

				mSchemeHeaderContainer.addView(scheme_detail_item);
			}
		}
	}

}
