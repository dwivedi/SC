package com.samsung.ssc.CustomUI;

/**
 * This class used to show the new desing alert dialog using bounce effect.
 * @author d.ashish
 */

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.io.GetDataCallBack;

public class SSCAlertDialog extends Dialog implements
		View.OnClickListener {
	private View mDialogView;
	private AnimationSet mModalInAnim;
	private AnimationSet mModalOutAnim;
	private Animation mOverlayOutAnim;
	private Animation mErrorInAnim;
	private AnimationSet mErrorXInAnim;
	private AnimationSet mSuccessLayoutAnimSet;
	private Animation mSuccessBowAnim;
	private TextView mTitleTextView;
	private TextView mContentTextView;
	private String mTitleText;
	private String mContentText;
	private boolean mShowCancel;
	private boolean mShowContent;
	private String mCancelText;
	private String mConfirmText;
	private int mAlertType;
	private FrameLayout mErrorFrame;
	// private FrameLayout mListFrame;
	private ListView mListView;
	private LinearLayout mLayoutFrame;
	
	private FrameLayout mSuccessFrame;
	private SuccessTickView mSuccessTick;
	private ImageView mErrorX;
	private View mSuccessLeftMask;
	private View mSuccessRightMask;
	private Button mConfirmButton;
	private Button mCancelButton;
	private FrameLayout mWarningFrame;
	private OnSDAlertDialogClickListener mCancelClickListener;
	private OnSDAlertDialogClickListener mConfirmClickListener;
	private boolean mCloseFromCancel;
	private BaseAdapter mAdapter;
	private boolean isEnableConformationButton = true;
	private boolean mShowList;
	OnItemClickListener mListener;
	private View mLayoutFrameView;
	/**
	 * Alert dialog view type with not animation
	 */
	public static final int NORMAL_TYPE = 0;
	/**
	 * Alert dialog view type with Error Animation
	 */
	public static final int ERROR_TYPE = 1;
	/**
	 * Alert dialog view type with Successful animation
	 */
	public static final int SUCCESS_TYPE = 2;
	/**
	 * Alert dialog view type with warning animation
	 */
	public static final int WARNING_TYPE = 3;
	
	public static final int LIST_TYPE = 5;
	
	public static final int LEYOUT_FRAME_TYPE = 6;

	public static interface OnSDAlertDialogClickListener {
		public void onClick(SSCAlertDialog sdAlertDialog);
	}

	/**
	 * Defult constructor to make a alert dialog with Normal Type
	 * @param context Activity refrece
	 */
	public SSCAlertDialog(Context context) {
		this(context, NORMAL_TYPE);
	}

	/**
	 *  constructor to make a alert dialog with pacified Type
	 * @param context Activity refrece
	 * @param alertType pacified alert type
	 */
	public SSCAlertDialog(Context context, int alertType) {
		super(context, R.style.smart_dost_alert_dialog);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		mAlertType = alertType;
		mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(),
				R.anim.error_frame_in);
		mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.error_x_in);
		// 2.3.x system don't support alpha-animation on layer-list drawable
		// remove it from animation set
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			List<Animation> childAnims = mErrorXInAnim.getAnimations();
			int idx = 0;
			for (; idx < childAnims.size(); idx++) {
				if (childAnims.get(idx) instanceof AlphaAnimation) {
					break;
				}
			}
			if (idx < childAnims.size()) {
				childAnims.remove(idx);
			}
		}
		mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(),
				R.anim.success_bow_roate);
		mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader
				.loadAnimation(getContext(), R.anim.success_mask_layout);
		mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_in);
		mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_out);
		mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mDialogView.setVisibility(View.GONE);
				mDialogView.post(new Runnable() {
					@Override
					public void run() {
						if (mCloseFromCancel) {
							SSCAlertDialog.super.cancel();
						} else {
							SSCAlertDialog.super.dismiss();
						}
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		// dialog overlay fade out
		mOverlayOutAnim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				WindowManager.LayoutParams wlp = getWindow().getAttributes();
				wlp.alpha = 1 - interpolatedTime;
				getWindow().setAttributes(wlp);
			}
		};
		mOverlayOutAnim.setDuration(120);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smart_dost_alert_dialog);

		mDialogView = getWindow().getDecorView().findViewById(
				android.R.id.content);
		mTitleTextView = (TextView) findViewById(R.id.title_text);
		mContentTextView = (TextView) findViewById(R.id.content_text);
		mErrorFrame = (FrameLayout) findViewById(R.id.error_frame);
		mErrorX = (ImageView) mErrorFrame.findViewById(R.id.error_x);
		/* mListFrame = (FrameLayout)findViewById(R.id.list_frame); */
		mListView = (ListView) findViewById(R.id.sd_list_dialog);
		mLayoutFrame = (LinearLayout) findViewById(R.id.sd_layout_frame_dialog);

		mSuccessFrame = (FrameLayout) findViewById(R.id.success_frame);
		mSuccessTick = (SuccessTickView) mSuccessFrame
				.findViewById(R.id.success_tick);
		mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
		mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
		mWarningFrame = (FrameLayout) findViewById(R.id.warning_frame);
		mConfirmButton = (Button) findViewById(R.id.confirm_button);
		mCancelButton = (Button) findViewById(R.id.cancel_button);
		mConfirmButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);

		setTitleText(mTitleText);
		if (mAlertType == LIST_TYPE) {
			setListAdapter(mAdapter);
			mLayoutFrame.setVisibility(View.GONE);
		}
		if (mAlertType == LEYOUT_FRAME_TYPE) {
			setLayoutFrameView(mLayoutFrameView);
			mListView.setVisibility(View.GONE);

		}
		setListItemClickListener(mListener);
		
		setEnableConfirmButton(isEnableConformationButton);
		setContentText(mContentText);
		setCancelText(mCancelText);
		setConfirmText(mConfirmText);
		changeAlertType(mAlertType, true);

	}

	private void restore() {
		mErrorFrame.setVisibility(View.GONE);
		mSuccessFrame.setVisibility(View.GONE);
		mWarningFrame.setVisibility(View.GONE);
		mConfirmButton.setVisibility(View.VISIBLE);

		mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
		mErrorFrame.clearAnimation();
		mErrorX.clearAnimation();
		mSuccessTick.clearAnimation();
		mSuccessLeftMask.clearAnimation();
		mSuccessRightMask.clearAnimation();
	}

	private void playAnimation() {
		if (mAlertType == ERROR_TYPE) {
			mErrorFrame.startAnimation(mErrorInAnim);
			mErrorX.startAnimation(mErrorXInAnim);
		} else if (mAlertType == SUCCESS_TYPE) {
			mSuccessTick.startTickAnim();
			mSuccessRightMask.startAnimation(mSuccessBowAnim);
		}
	}

	private void changeAlertType(int alertType, boolean fromCreate) {
		mAlertType = alertType;
		// call after created views
		if (mDialogView != null) {
			if (!fromCreate) {
				// restore all of views state before switching alert type
				restore();
			}
			switch (mAlertType) {
			case ERROR_TYPE:
				mErrorFrame.setVisibility(View.VISIBLE);
				break;
			case SUCCESS_TYPE:
				mSuccessFrame.setVisibility(View.VISIBLE);
				// initial rotate layout of success mask
				mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet
						.getAnimations().get(0));
				mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet
						.getAnimations().get(1));
				break;
			case WARNING_TYPE:
				mConfirmButton
						.setBackgroundResource(R.drawable.red_button_background);
				mWarningFrame.setVisibility(View.VISIBLE);
				break;

			case LIST_TYPE:
				mListView.setVisibility(View.VISIBLE);
				break;

			case LEYOUT_FRAME_TYPE:
				mLayoutFrame.setVisibility(View.VISIBLE);
				break;
				

			}
			if (!fromCreate) {
				playAnimation();
			}
		}
	}

	public int getAlerType() {
		return mAlertType;
	}

	public void changeAlertType(int alertType) {
		changeAlertType(alertType, false);
	}

	public String getTitleText() {
		return mTitleText;
	}

	public SSCAlertDialog setTitleText(String text) {
		mTitleText = text;
		if (mTitleTextView != null && mTitleText != null) {
			mTitleTextView.setText(mTitleText);
		}
		return this;
	}

	public SSCAlertDialog setListAdapter(BaseAdapter adapter) {
		mAdapter = adapter;
		if (mListView != null) {
			showContentList(true);
			mListView.setAdapter(mAdapter);
		}
		return this;
	}

	
	
	public SSCAlertDialog setListItemClickListener(OnItemClickListener listener)
	{
		mListener=listener;
		if(mListView!=null)
		{
			mListView.setOnItemClickListener(listener);
		}
		
		return this;
	}
	
	public SSCAlertDialog setLayoutFrameView(View view) {
		mLayoutFrameView = view;
		
		if (mLayoutFrame!= null) {
 			mLayoutFrame.removeAllViews();
			mLayoutFrame.addView(mLayoutFrameView);
		}
		return this;
	}

	public String getContentText() {
		return mContentText;
	}

	public SSCAlertDialog setContentText(String text) {
		mContentText = text;
		if (mContentTextView != null && mContentText != null) {
			showContentText(true);
			mContentTextView.setText(mContentText);
		}
		return this;
	}

	
	
	public boolean isShowCancelButton() {
		return mShowCancel;
	}

	public SSCAlertDialog showCancelButton(boolean isShow) {
		mShowCancel = isShow;
		if (mCancelButton != null) {
			mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public boolean isShowContentText() {
		return mShowContent;
	}

	public SSCAlertDialog showContentText(boolean isShow) {
		mShowContent = isShow;
		if (mContentTextView != null) {
			mContentTextView.setVisibility(mShowContent ? View.VISIBLE
					: View.GONE);
			mListView.setVisibility(mShowContent ? View.GONE : View.VISIBLE);
		}
		return this;
	}

	public SSCAlertDialog showContentList(boolean isShow) {
		mShowList = isShow;
		if (mListView != null) {
			mListView.setVisibility(mShowList ? View.VISIBLE : View.GONE);
			mContentTextView
					.setVisibility(mShowList ? View.GONE : View.VISIBLE);
		}
		return this;
	}

	public String getCancelText() {
		return mCancelText;
	}

	public SSCAlertDialog setCancelText(String text) {
		mCancelText = text;
		if (mCancelButton != null && mCancelText != null) {
			showCancelButton(true);
			mCancelButton.setText(mCancelText);
		}
		return this;
	}

	public String getConfirmText() {
		return mConfirmText;
	}

	public SSCAlertDialog setConfirmText(String text) {
		mConfirmText = text;
		if (mConfirmButton != null && mConfirmText != null) {
			mConfirmButton.setText(mConfirmText);
		}
		return this;
	}

	public SSCAlertDialog setEnableConfirmButton(boolean isEnable) {
		this.isEnableConformationButton = isEnable;
		if (mConfirmButton != null && mConfirmText != null) {
			mConfirmButton.setEnabled(isEnableConformationButton);
		}
		return this;
	}

	public ListView getListView() {

		if (getAlerType() == LIST_TYPE) {

			return mListView;
		}

		return null;
	}
	
	public LinearLayout getLayoutFrameView() {

		if (getAlerType() == LEYOUT_FRAME_TYPE) {

			return mLayoutFrame;
		}

		return null;
	}

	public SSCAlertDialog setCancelClickListener(
			OnSDAlertDialogClickListener listener) {
		mCancelClickListener = listener;
		return this;
	}

	public SSCAlertDialog setNotCancelable()
	{
		this.setCancelable(false);
		return this;
	}
	
	public SSCAlertDialog setConfirmClickListener(
			OnSDAlertDialogClickListener listener) {
		mConfirmClickListener = listener;
		return this;
	}

	protected void onStart() {
		mDialogView.startAnimation(mModalInAnim);
		playAnimation();
	}

	/**
	 * The real Dialog.cancel() will be invoked async-ly after the animation
	 * finishes.
	 */
	@Override
	public void cancel() {
		dismissWithAnimation(true);
	}

	/**
	 * The real Dialog.dismiss() will be invoked async-ly after the animation
	 * finishes.
	 */
	public void dismissWithAnimation() {
		dismissWithAnimation(false);
	}

	private void dismissWithAnimation(boolean fromCancel) {
		mCloseFromCancel = fromCancel;
		mConfirmButton.startAnimation(mOverlayOutAnim);
		mDialogView.startAnimation(mModalOutAnim);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancel_button) {
			if (mCancelClickListener != null) {
				mCancelClickListener.onClick(SSCAlertDialog.this);
			} else {
				dismissWithAnimation();
			}
		} else if (v.getId() == R.id.confirm_button) {
			if (mConfirmClickListener != null) {
				mConfirmClickListener.onClick(SSCAlertDialog.this);
			} else {
				dismissWithAnimation();
			}
		}
	}

}