package com.samsung.ssc.CustomUI;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samsung.ssc.AttendanceActivity;
import com.samsung.ssc.R;

public class SSCActionBar extends RelativeLayout {

	public SSCActionBar(Context context) {
		super(context);

	}

	public SSCActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);

	}

	public SSCActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(final Context context, AttributeSet attrs) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View view = inflater.inflate(R.layout.sd_action_bar_view, this);

		TextView textView = (TextView) view
				.findViewById(R.id.tv_title_sdActionBar);

		ImageButton btBack = (ImageButton) view
				.findViewById(R.id.ib_up_sdActionBar);

		btBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof Activity) {

					if (context instanceof AttendanceActivity) {
						
						((AttendanceActivity) context).onBackPressed();

						
					} else {

						((Activity) context).finish();
					}

				}

			}
		});

		if (attrs != null) {
			TypedArray actionTypedArray = getContext().obtainStyledAttributes(
					attrs, R.styleable.SSCActionBar);
			String title = actionTypedArray
					.getString(R.styleable.SSCActionBar_actionTitle);

			if (!TextUtils.isEmpty(title)) {
				textView.setText(title);
			}

			actionTypedArray.recycle();
		}

	}

}
