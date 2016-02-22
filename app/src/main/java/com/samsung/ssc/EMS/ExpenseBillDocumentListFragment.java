package com.samsung.ssc.EMS;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.EMSBillDocumentDetail;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.RecyclerViewLinearLayoutParams;

import java.util.ArrayList;

public class ExpenseBillDocumentListFragment extends DialogFragment {
	
	RecyclerView mExpenseTypeRecyclerview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		 View rootView = inflater.inflate(R.layout.fragment_expense_bill_document_list, container,false);
		
		 setupView(rootView);
		 
		 initializeBillDocumentList();
	
		return rootView;	
	
	}
	
	void setupView(View rootView)
	{
		
		    mExpenseTypeRecyclerview = (RecyclerView) rootView.findViewById(R.id.expense_type_list_recycler_view);
		  
		   //use this setting to improve performance if you know that changes  in content do not change the layout size of the RecyclerView
		    mExpenseTypeRecyclerview.setHasFixedSize(true);

	        // use a linear layout manager
		    RecyclerViewLinearLayoutParams mLayoutManager = new RecyclerViewLinearLayoutParams(getActivity(),RecyclerViewLinearLayoutParams.VERTICAL,false);
		  
		   mExpenseTypeRecyclerview.setLayoutManager(mLayoutManager);
		   
		   rootView.findViewById(R.id.close_bill_document_list_imageview).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				dismiss();
				
			}
		}); 

	}
	
	void initializeBillDocumentList()
	{
		  Bundle bundle = getArguments();
			
			if (bundle != null && bundle.getSerializable("bill_document_list") != null) {
				
				@SuppressWarnings("unchecked")
				ArrayList<EMSBillDocumentDetail> emsBillDocumentDetailList =  (ArrayList<EMSBillDocumentDetail>) bundle.getSerializable("bill_document_list"); 				 
				BillDocumentListAdapter billDocumentListAdapter = new BillDocumentListAdapter(getActivity(),this, emsBillDocumentDetailList );
				mExpenseTypeRecyclerview.setAdapter(billDocumentListAdapter);	
			}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		  ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
		  params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		  params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		  getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	}
	
	public void displayBillDocumentDialog(EMSBillDocumentDetail emsBillDocumentDetail)
	{
		try
		{
		 FragmentTransaction billDocumentFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
	  	   
	  	   Fragment previosbillingAndProductDetailsFragment = getActivity().getSupportFragmentManager().findFragmentByTag("bill_document_dialog");
	  	    if (previosbillingAndProductDetailsFragment != null) {
	  	    	billDocumentFragmentTransaction.remove(previosbillingAndProductDetailsFragment);
	  	    }
	  	    
	  	   billDocumentFragmentTransaction.addToBackStack(null);
		
		   Bundle billDocumentBundle = new Bundle();
	  	   billDocumentBundle.putSerializable("bill_document", emsBillDocumentDetail);
	  	   
	  	   ExpenseBillDocumentFragment billDocumentFragment = new ExpenseBillDocumentFragment();	
	  	   billDocumentFragment.setArguments(billDocumentBundle);
		   billDocumentFragment.show(billDocumentFragmentTransaction, "bill_document_dialog");
		   
	}
	catch(IllegalStateException e)
	{
		Helper.printStackTrace(e);
	}
	catch (Exception e) {
		Helper.printStackTrace(e);
	}
		
	}
  
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	  Dialog dialog = super.onCreateDialog(savedInstanceState);
	  
	  // request a window without the title
	  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

	  return dialog;
	}
	
}
