package com.samsung.ssc.EMS;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.EMSExpenseDetail;
import com.samsung.ssc.util.Helper;

import java.util.ArrayList;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseItemViewHolder> {
	
	private DatabaseHelper mDatabaseHelper;
    private ArrayList<EMSExpenseDetail> mExpenseItemDTOList;
    private Context mContext;
    private boolean mbIsExpenseListing;
   // private int miTabID;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
     class ExpenseItemViewHolder extends RecyclerView.ViewHolder
     {
        // each data item is just a string in this case
        public TextView mExpenseTypeTextview;
        public TextView mPendingWithTextview;
        public TextView mSubmittedDateTextview;
        public TextView mExpenseStatusTextview;
        
        public View mExpenseItemView;
        
        public ExpenseItemViewHolder(View expenseItemView) {
            super(expenseItemView);
            mExpenseItemView = expenseItemView;
            mExpenseTypeTextview = (TextView) expenseItemView.findViewById(R.id.expense_type_textview);
            mPendingWithTextview = (TextView) expenseItemView.findViewById(R.id.pending_with_textview);
            mSubmittedDateTextview = (TextView) expenseItemView.findViewById(R.id.submitted_date_textview);
            mExpenseStatusTextview = (TextView) expenseItemView.findViewById(R.id.expense_approval_status_textview);
            
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExpenseListAdapter(Context context, boolean inbIsExpenseListing) { //, int initabID
    	mContext = context;
        this.mExpenseItemDTOList = new ArrayList<EMSExpenseDetail>();     
        mDatabaseHelper =  DatabaseHelper.getConnection(mContext);
        mbIsExpenseListing = inbIsExpenseListing;
       // miTabID = initabID;
    }//

    
    
    
    public void addItems(EMSExpenseDetail item){
    	 this.mExpenseItemDTOList.add(item);
    	 
    	 notifyDataSetChanged();
    }
    
    public void addItems(ArrayList<EMSExpenseDetail> item){
    	 this.mExpenseItemDTOList.addAll(item);
    	 
    	 notifyDataSetChanged();
    }
    public void clear(){
   	 
    this.mExpenseItemDTOList.clear();
   	 
   	 notifyDataSetChanged();
   }
    // Create new views (invoked by the layout manager)
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
   
        View expenseItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expense_list_item, parent, false);
   
        ExpenseItemViewHolder expenseItemViewHolder = new ExpenseItemViewHolder(expenseItemView);
        
        return expenseItemViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ExpenseItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
    	final EMSExpenseDetail expenseDetail =  mExpenseItemDTOList.get(position);
    	
    	
    	holder.mExpenseTypeTextview.setText(mDatabaseHelper.getExpenseTypeValue(expenseDetail.miEMSExpenseTypeMasterID));

        if(expenseDetail.mPendingWith == null || TextUtils.isEmpty(expenseDetail.mPendingWith) || expenseDetail.mPendingWith.equalsIgnoreCase("null"))
              holder.mPendingWithTextview.setText("");
        else
              holder.mPendingWithTextview.setText(expenseDetail.mPendingWith);


        holder.mSubmittedDateTextview.setText(Helper.longDateToUIDateString(expenseDetail.mlSubmittedDate));
        
        if(expenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_SUBMITTED)
            holder.mExpenseStatusTextview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.expense_status_submitted_color)); 
        
        else if(expenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_PENDING)
               holder.mExpenseStatusTextview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.expense_status_pending_color));   
        
        else if(expenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVED)
            holder.mExpenseStatusTextview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.expense_status_approved_color));
        
        else if(expenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_REJECT)
            holder.mExpenseStatusTextview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.expense_status_rejected_color));
        
        else if(expenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_CANCELLED)
            holder.mExpenseStatusTextview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.expense_status_cancelled_color));
        
      /*  else if(expenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_EXTERNAL_SYSTEM)
            holder.mExpenseStatusTextview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.expense_status_external_system_color));
     */
        else if(expenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVAL_NOT_REQUIRED)
            holder.mExpenseStatusTextview.setBackgroundColor(ContextCompat.getColor(mContext, R.color.expense_status_approval_not_required_color));
    	
    	
        holder.mExpenseItemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(mbIsExpenseListing)
				  ((ExpenseListingActivity)mContext).launchUpdateExpenseClick(expenseDetail);
				else
				 ((SearchExpenseListingActivity)mContext).launchUpdateExpenseClick(expenseDetail);
					
				
			}
		});
        
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        
    	if(mExpenseItemDTOList == null || mExpenseItemDTOList.size() == 0)
    		return 0;
    	else
    	    return mExpenseItemDTOList.size();
    }
}
