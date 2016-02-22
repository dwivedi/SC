package com.samsung.ssc.EMS;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.EMSBillDocumentDetail;

import java.util.ArrayList;

public class BillDocumentListAdapter extends RecyclerView.Adapter<BillDocumentListAdapter.ExpenseItemViewHolder> {
    
	private DialogFragment mExpenseBillDocumentListFragment;
	private ArrayList<EMSBillDocumentDetail> mExpenseBillDocumentList;
	private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
     class ExpenseItemViewHolder extends RecyclerView.ViewHolder
     {
        // each data item is just a string in this case
        public TextView mExpenseTypeNameTextview;
        public ImageView mCheckBillDocumentImageview;
           
        public ExpenseItemViewHolder(View expenseItemView) {
            super(expenseItemView);
           
            mExpenseTypeNameTextview = (TextView) expenseItemView.findViewById(R.id.bill_document_name_textview);
            mCheckBillDocumentImageview = (ImageView) expenseItemView.findViewById(R.id.check_bill_document_imageview);    
            
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BillDocumentListAdapter(Context context, DialogFragment expenseBillDocumentListFragment, ArrayList<EMSBillDocumentDetail> expenseItemDTOList) {
    	mContext = context;
    	
    	if(expenseItemDTOList != null && expenseItemDTOList.size() > 0)
    	{
    		mExpenseBillDocumentList = new ArrayList<EMSBillDocumentDetail>();
    		for(int i = 0 ; i < expenseItemDTOList.size();i++ )
    		{
    			EMSBillDocumentDetail emsBillDocumentDetail = expenseItemDTOList.get(i);
    			
    			if(emsBillDocumentDetail.mIsDeleted == false)
    		      mExpenseBillDocumentList.add(emsBillDocumentDetail);
    		}
    	}
    	
    	mExpenseBillDocumentListFragment = expenseBillDocumentListFragment;
        
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
   
        View expenseTypeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bill_document_view, parent, false);
   
        ExpenseItemViewHolder expenseItemViewHolder = new ExpenseItemViewHolder(expenseTypeItemView);
        
        return expenseItemViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ExpenseItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
    	final EMSBillDocumentDetail expenseItemDTO =  mExpenseBillDocumentList.get(position);
        holder.mExpenseTypeNameTextview.setText(expenseItemDTO.mDocumentName);
        holder.mCheckBillDocumentImageview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				 ((ExpenseBillDocumentListFragment)mExpenseBillDocumentListFragment).displayBillDocumentDialog(expenseItemDTO);
				// mExpenseBillDocumentListFragment.dismiss();
				
			}
		});
        
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        
    	if(mExpenseBillDocumentList == null || mExpenseBillDocumentList.size() == 0)
    		return 0;
    	else
    	    return mExpenseBillDocumentList.size();
    }
}

