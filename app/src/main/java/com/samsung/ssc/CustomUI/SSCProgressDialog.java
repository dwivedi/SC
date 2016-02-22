package com.samsung.ssc.CustomUI;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.samsung.ssc.R;
 

public class SSCProgressDialog extends ProgressDialog {
	  private AnimationDrawable animation;

	public static ProgressDialog ctor(Context context) {
		
		SSCProgressDialog dialog = new SSCProgressDialog(context,R.style.CustomDialogTheme);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}

	  public SSCProgressDialog(Context context) {
	    super(context);
	  }

	  public SSCProgressDialog(Context context, int theme) {
	    super(context, theme);
	  }

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.view_custom_progress_dialog);

	    ImageView la = (ImageView) findViewById(R.id.animationPrgressImage);
	    la.setBackgroundResource(R.drawable.custom_progress_dialog_animation);
	 //   la.setImageDrawable(getContext().getResources().getDrawable(R.drawable.custom_progress_dialog_animation));
	    animation = (AnimationDrawable) la.getBackground();
	  }

	  @Override
	  public void show() {
	    super.show();
	    try {
			animation.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	  @Override
	  public void dismiss() {
	    super.dismiss();
	    try {
			animation.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	}
