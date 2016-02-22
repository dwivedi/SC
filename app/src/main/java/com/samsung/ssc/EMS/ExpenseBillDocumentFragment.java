package com.samsung.ssc.EMS;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.EMSBillDocumentDetail;
import com.samsung.ssc.util.ImageLoader;
import com.samsung.ssc.util.ScalingUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class ExpenseBillDocumentFragment extends DialogFragment {
	
	 ImageView mBillDocumentImageview;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_expense_bill_document, container,false);
		mBillDocumentImageview = (ImageView) rootView.findViewById(R.id.bill_document_imageview);
		
		Bundle bundle = getArguments();
		
		if (bundle != null && bundle.getSerializable("bill_document") != null) {
			
			EMSBillDocumentDetail emsBillDocumentDetail = (EMSBillDocumentDetail) bundle.getSerializable("bill_document");
		
			if(emsBillDocumentDetail != null)
			{
				if(emsBillDocumentDetail.mDocumentFilePath != null && !TextUtils.isEmpty(emsBillDocumentDetail.mDocumentFilePath))
				    loadBillDocumentFromDevice(emsBillDocumentDetail.mDocumentFilePath);
				else
					loadBillDocumentFromURL(emsBillDocumentDetail.mDocumentName);
			}
			
		}

		return rootView;	
	
	}
	
	void loadBillDocumentFromDevice(String documentPath)
	{
		/*BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(documentPath, options);*/

		try
		{
			mBillDocumentImageview.setImageBitmap(loadPrescaledBitmap(documentPath));
		}
		catch(Exception e)
		{

		}
	}

	private Bitmap loadPrescaledBitmap(String path) {

		Bitmap scaledBitmap = null;
		Bitmap unscaledBitmap = null;

		DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
		int desireWidth = (int)(metrics.widthPixels * metrics.density);
		int desireHeight = (int)(metrics.heightPixels* metrics.density);

		try {
			// Part 1: Decode image
			unscaledBitmap = ScalingUtilities.decodeFile(path, desireWidth,
					desireHeight, ScalingUtilities.ScalingLogic.FIT);

			if (!(unscaledBitmap.getWidth() <= desireWidth && unscaledBitmap
					.getHeight() <= desireHeight)) {
				// Part 2: Scale image
				scaledBitmap = ScalingUtilities.createScaledBitmap(
						unscaledBitmap, desireWidth, desireHeight,
						ScalingUtilities.ScalingLogic.FIT);

			} else {

				return compressBitmapImage(path, unscaledBitmap);
			}

		} catch (Throwable e) {
		}

		return compressBitmapImage(path, scaledBitmap);
	}


	private Bitmap compressBitmapImage(String path,
									   Bitmap scaledOrUnscaledBitmap) {
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			scaledOrUnscaledBitmap
					.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return scaledOrUnscaledBitmap;
	}

	void loadBillDocumentFromURL(String documentName)
	{
/*
        http://107.110.101.188/SalesCatalystService_Test/contents/expensefiles/10179_116_AEWJELAGCP217201652943PM1.jpeg

        http://107.110.101.188/SaleCatalystService_Dev/Contents/ExpenseFiles/7_153_34E7NQTJK2121201630549PM1.jpeg

*/



        String emsDocumentURL = getActivity().getResources().getString(R.string.expense_image_base_url)+ documentName;
		 ImageLoader.getInstance(getActivity()).displayImage(emsDocumentURL, mBillDocumentImageview);

	}


	@Override
	public void onResume() {
		super.onResume();

		  ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
		  params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		  params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		  getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	  Dialog dialog = super.onCreateDialog(savedInstanceState);
	  
	  // request a window without the title
	  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

	  return dialog;
	}


	
}
