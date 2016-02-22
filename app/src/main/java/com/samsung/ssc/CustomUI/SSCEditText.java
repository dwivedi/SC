package com.samsung.ssc.CustomUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.samsung.ssc.R;


public class SSCEditText extends EditText {
	


	public SSCEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	public SSCEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
		
	}
	
	public SSCEditText(Context context) {
		super(context);
		init(null);
	}
	
	private void init(AttributeSet attrs) {
		if (attrs!=null) {
			 TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SSCEditText);
			 int fontType = a.getInt(R.styleable.SSCEditText_edittextFontType,0);
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
