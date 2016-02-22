package com.samsung.ssc.EMS;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.EMSExpenseType;

import java.util.ArrayList;

public class ExpenseTypeDialogListAdapter extends RecyclerView.Adapter<ExpenseTypeDialogListAdapter.ExpenseItemViewHolder> {

    private Dialog mExpenseTypesDialog;
    private ArrayList<EMSExpenseType> mExpenseTypeItemDTOList;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ExpenseItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mExpenseTypeNameTextview;

        public ExpenseItemViewHolder(View expenseItemView) {
            super(expenseItemView);

            mExpenseTypeNameTextview = (TextView) expenseItemView.findViewById(R.id.expense_type_name_textview);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExpenseTypeDialogListAdapter(Context context, Dialog expenseTypesDialog, ArrayList<EMSExpenseType> expenseItemDTOList) {
        mContext = context;
        mExpenseTypesDialog = expenseTypesDialog;
        mExpenseTypeItemDTOList = expenseItemDTOList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View expenseTypeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expense_type_item, parent, false);

        ExpenseItemViewHolder expenseItemViewHolder = new ExpenseItemViewHolder(expenseTypeItemView);

        return expenseItemViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ExpenseItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final EMSExpenseType expenseItemDTO = mExpenseTypeItemDTOList.get(position);
        holder.mExpenseTypeNameTextview.setText(expenseItemDTO.mName);
        holder.mExpenseTypeNameTextview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((NewExpenseActivity) mContext).setExpenseType(expenseItemDTO);
                mExpenseTypesDialog.dismiss();

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (mExpenseTypeItemDTOList == null || mExpenseTypeItemDTOList.size() == 0)
            return 0;
        else
            return mExpenseTypeItemDTOList.size();
    }
}
