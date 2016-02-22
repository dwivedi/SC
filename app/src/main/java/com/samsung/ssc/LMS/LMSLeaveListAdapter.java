package com.samsung.ssc.LMS;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.LMSLeaveDataModal;
import com.samsung.ssc.util.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by d.ashish on 13-01-2016.
 */
public class LMSLeaveListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {




    private final LayoutInflater inflater;
    private final int viewType;
    private List<LMSLeaveDataModal> items;

    public LMSLeaveListAdapter(Context context, int viewType) {
        this.inflater = LayoutInflater.from(context);
        items = new ArrayList<LMSLeaveDataModal>();
        this.viewType = viewType;

    }


    @Override
    public int getItemViewType(int position) {
        return this.viewType;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == LMSConstants.TAB_SUBMITTED) {
            View itemView = this.inflater.
                    inflate(R.layout.row_view_leave_request,
                            parent,
                            false);
            return new ViewHolderSubmitted(itemView);
        } else
        if (viewType == LMSConstants.TAB_PENDING) {
            View itemView = this.inflater.
                    inflate(R.layout.row_view_leave_pending,
                            parent,
                            false);
            return new ViewHolderPending(itemView);
        } else
        if (viewType == LMSConstants.TAB_NOTIFICATION) {
            View itemView = this.inflater.
                    inflate(R.layout.row_view_leave_pending,
                            parent,
                            false);
            return new ViewHolderNotification(itemView);
        } else
        if (viewType == LMSConstants.TAB_ACTED) {
            View itemView = this.inflater.
                    inflate(R.layout.row_view_leave_pending,
                            parent,
                            false);
            return new ViewHolderActed(itemView);
        }
        else
            return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LMSLeaveDataModal modal = items.get(position);
        switch (holder.getItemViewType()){
            case LMSConstants.TAB_SUBMITTED:

                ViewHolderSubmitted holderRequest = (ViewHolderSubmitted) holder;
                holderRequest .tvLeaveTitle.setText(modal.LeaveSubject);
                holderRequest .tvLeaveType.setText(" "+modal.LeaveTypeCode+" ");
                holderRequest .tvActionBy.setText(modal.PendingWithUserName);
                holderRequest .tvNumberOfDays.setText(String.valueOf(modal.NumberOfLeave) + "\n Days");
                Date date = new Date();
                date.setTime(modal.CreatedDate);
                holderRequest.tvLeaveAppliedDate.setText(Helper.getDateStringFromDate(date,"dd-MMM"));
                switch (modal.CurrentStatus) {
                    case LMSConstants.LEAVE_STATUS_APPROVE:
                         holderRequest.tvActionBy.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_approved, 0, 0, 0);
                        break;
                    case LMSConstants.LEAVE_STATUS_PENDING:
                         holderRequest.tvActionBy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pending, 0, 0, 0);
                        break;
                    case LMSConstants.LEAVE_STATUS_REJECT:
                         holderRequest.tvActionBy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reject, 0, 0, 0);
                        break;
                    default:
                         break;
                }
                break;
            case LMSConstants.TAB_PENDING:
                ViewHolderPending holderApprove = (ViewHolderPending) holder;
                holderApprove.tvLeaveCreateByUserName.setText(modal.CreatedByUserName);
                Date date1 = new Date();
                date1.setTime(modal.CreatedDate);
                holderApprove.tvLeaveAppliedDate.setText(Helper.getDateStringFromDate(date1, "dd-MMM"));
                holderApprove .tvNumberOfDays.setText(String.valueOf(modal.NumberOfLeave) + "\n Days");
                holderApprove.tvLeaveSubject.setText(modal.LeaveSubject);
                break;

            case LMSConstants.TAB_NOTIFICATION:
                ViewHolderNotification holderNotification = (ViewHolderNotification) holder;
                holderNotification.tvLeaveCreateByUserName.setText(modal.CreatedByUserName);
                Date dateN = new Date();
                dateN.setTime(modal.CreatedDate);
                holderNotification.tvLeaveAppliedDate.setText(Helper.getDateStringFromDate(dateN, "dd-MMM"));
                holderNotification.tvNumberOfDays.setText(String.valueOf(modal.NumberOfLeave) + "\n Days");
                holderNotification.tvLeaveSubject.setText(modal.LeaveSubject);
                break;

            case LMSConstants.TAB_ACTED:
                ViewHolderActed holderActed = (ViewHolderActed) holder;
                holderActed.tvLeaveCreateByUserName.setText(modal.CreatedByUserName);
                Date dateA = new Date();
                dateA.setTime(modal.CreatedDate);
                holderActed.tvLeaveAppliedDate.setText(Helper.getDateStringFromDate(dateA, "dd-MMM"));
                holderActed.tvNumberOfDays.setText(String.valueOf(modal.NumberOfLeave) + "\n Days");
                holderActed.tvLeaveSubject.setText(modal.LeaveSubject);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItems(List<LMSLeaveDataModal> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public LMSLeaveDataModal getItemAtPosition(int position) {
        return  this.items.get(position);
    }


    public final static class ViewHolderSubmitted extends RecyclerView.ViewHolder {
         TextView tvLeaveTitle, tvActionBy,  tvLeaveType, tvNumberOfDays,tvLeaveAppliedDate;
     //  View viewStatusColor;

        public ViewHolderSubmitted(View view) {
            super(view);
            tvLeaveAppliedDate = (TextView) view.findViewById(R.id.textViewLMSLeaveAppliedDate);
             tvLeaveTitle = (TextView) view.findViewById(R.id.textViewLeaveTitle);
            tvActionBy = (TextView) view.findViewById(R.id.textViewLMSActionUserName);
            tvLeaveType = (TextView) view.findViewById(R.id.textViewLMSLeaveType);
            tvNumberOfDays = (TextView) view.findViewById(R.id.textViewLMSNumberOfDays);
          //  viewStatusColor = view.findViewById(R.id.viewLeaveStatusBar);

        }
    }

    public final static class ViewHolderPending extends RecyclerView.ViewHolder {
        TextView tvLeaveCreateByUserName, tvNumberOfDays, tvLeaveAppliedDate,tvLeaveSubject;

        public ViewHolderPending(View view) {
            super(view);
            tvLeaveSubject = (TextView) view.findViewById(R.id.textViewLMSLeaveSubject);
            tvLeaveCreateByUserName = (TextView) view.findViewById(R.id.textViewLeaveCreatedBy);
            tvNumberOfDays = (TextView) view.findViewById(R.id.textViewLMSNumberOfDays);
            tvLeaveAppliedDate = (TextView) view.findViewById(R.id.textViewLMSLeaveAppliedDate);

        }
    }

    public final static class ViewHolderActed extends RecyclerView.ViewHolder {
        TextView tvLeaveCreateByUserName, tvNumberOfDays, tvLeaveAppliedDate,tvLeaveSubject;

        public ViewHolderActed(View view) {
            super(view);
            tvLeaveSubject = (TextView) view.findViewById(R.id.textViewLMSLeaveSubject);
            tvLeaveCreateByUserName = (TextView) view.findViewById(R.id.textViewLeaveCreatedBy);
            tvNumberOfDays = (TextView) view.findViewById(R.id.textViewLMSNumberOfDays);
            tvLeaveAppliedDate = (TextView) view.findViewById(R.id.textViewLMSLeaveAppliedDate);

        }
    }

    public final static class ViewHolderNotification extends RecyclerView.ViewHolder {
        TextView tvLeaveCreateByUserName, tvNumberOfDays, tvLeaveAppliedDate,tvLeaveSubject;

        public ViewHolderNotification(View view) {
            super(view);
            tvLeaveSubject = (TextView) view.findViewById(R.id.textViewLMSLeaveSubject);
            tvLeaveCreateByUserName = (TextView) view.findViewById(R.id.textViewLeaveCreatedBy);
            tvNumberOfDays = (TextView) view.findViewById(R.id.textViewLMSNumberOfDays);
            tvLeaveAppliedDate = (TextView) view.findViewById(R.id.textViewLMSLeaveAppliedDate);

        }
    }
}




