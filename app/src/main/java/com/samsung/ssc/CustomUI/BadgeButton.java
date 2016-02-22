package com.samsung.ssc.CustomUI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samsung.ssc.R;

public class BadgeButton extends FrameLayout {
	/**
	 * The {@link TextView} containing the text in the badge.
	 */
	private TextView badgeText;

	private RelativeLayout badgeView;

	/**
	 * The {@link Button} inside the {@link BadgeButton}.
	 */
	private TextView btnClickThrough;

	@SuppressWarnings("deprecation")
	public BadgeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.drawable.badge_button, this);

		// initialize layout components
		badgeText = (TextView) findViewById(R.id.badgebtn_txt);
		badgeView = (RelativeLayout) findViewById(R.id.badgebtn_rl);
		btnClickThrough = (TextView) findViewById(R.id.badgebtn_btn);

		int count = attrs.getAttributeCount();

		for (int i = 0; i != count; i++) {
			String attrName = attrs.getAttributeName(i);
			int attrValue = attrs.getAttributeResourceValue(i, 0);

			// search for android:text
			if (attrName.equals("text")) {
				if (attrValue == 0) // string literal
					btnClickThrough.setText(attrs.getAttributeValue(i));
				else
					// string id, contained in R.string
					btnClickThrough.setText(context.getResources().getString(
							attrValue));
			}
			// search for android:textColor
			else if (attrName.equals("textColor")) {
				if (attrValue == 0) // color literal
					btnClickThrough.setTextColor(attrs.getAttributeIntValue(i,
							0));
				else
					// color id, contained in R.color
					btnClickThrough.setTextColor(context.getResources()
							.getColor(attrValue));
			}
			// search for android:textSize
			else if (attrName.equals("textSize")) {
				if (attrValue == 0) // dimension literal
					btnClickThrough.setTextSize(extractFloat(attrs
							.getAttributeValue(i)));
				else
					// dimension id, contained in R.dimen
					btnClickThrough.setTextSize(context.getResources()
							.getDimension(attrValue));
			}
			// search for android:gravity
			else if (attrName.equals("gravity")) {
				// gravity value
				btnClickThrough.setGravity(attrs.getAttributeIntValue(i, 0));
			}
			// search for android:background
			else if (attrName.equals("background")) {
				// use drawable id
				this.setBackgroundResource(0);
				btnClickThrough.setBackgroundDrawable(context.getResources()
						.getDrawable(attrValue));
			}
		}
	}

	 
	private float extractFloat(String attributeValue) {
		attributeValue = attributeValue.replaceAll("[a-zA-Z]", "");
		return Float.parseFloat(attributeValue);
	}
 
	@Override
	public void setOnClickListener(OnClickListener listener) {
		btnClickThrough.setOnClickListener(listener);
	}

	 
	public void setBadgeText(String badgeTextString) {
		badgeText.setText(badgeTextString);
	}

	 
	public void setBadgeDrawable(Drawable badgeDrawable) {
		((ImageView) findViewById(R.id.badgebtn_img))
				.setImageDrawable(badgeDrawable);
	}

	 
	public String getBadgeText() {
		return badgeText.getText().toString();
	}

	 
	public void hideBadge() {
		badgeView.setVisibility(View.GONE);
	}

	 
	public void showBadge() {
		badgeView.setVisibility(View.VISIBLE);
	}
	
	public void setBtnClickText(String btnText){
		btnClickThrough.setText(btnText);
	}
}
