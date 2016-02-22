package com.samsung.ssc.CustomUI;

import com.samsung.ssc.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


public class SSCButton extends Button {
	


	public SSCButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	public SSCButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
		
	}
	
	public SSCButton(Context context) {
		super(context);
		init(null);
	}
	
	private void init(AttributeSet attrs) {
		if (attrs!=null) {
			 TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SSCButton);
			 int fontType = a.getInt(R.styleable.SSCButton_buttonFontType,0);
			 String fontName = null;
			 switch (fontType) {
			case 1:
				fontName = "Roboto-Light.ttf";
				break;
			case 2:
				fontName = "Roboto-Medium.ttf";
				break;

			default:
				break;
			}
			 if (fontName!=null) {
				 Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
				 setTypeface(myTypeface);
			 }
			 a.recycle();
		}
	}
	
}
