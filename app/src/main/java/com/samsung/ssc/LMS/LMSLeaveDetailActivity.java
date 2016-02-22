package com.samsung.ssc.LMS;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.dto.LMSLeaveDataModal;
import com.samsung.ssc.dto.LMSLeaveDateModal;
import com.samsung.ssc.dto.LMSLeaveStatusLogModal;
import com.samsung.ssc.util.Helper;

import java.util.ArrayList;
import java.util.Date;

public class LMSLeaveDetailActivity extends BaseActivity {

    private LinearLayout mDateContainerView;
    private TextView etRemark;
    private TextView etSubject;
    private LinearLayout mStatusLogContainerView;
    private LMSLeaveDataModal modal;
    private LayoutInflater mInflater;
    private TextView etCreatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmsleave_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Leave Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setUpView();

        getValueFromBundle();
    }

    private void getValueFromBundle() {
        Bundle bundle = getIntent().getExtras();
        modal = (LMSLeaveDataModal) bundle.getParcelable(LMSListActivity.KEY_LEAVE_DATA);
        etSubject.setText(modal.LeaveSubject);
        etRemark.setText(modal.Remarks);
        Date mDate = new Date();
        mDate.setTime(modal.CreatedDate);
        etCreatedDate.setText(Helper.getDateStringFromDate(mDate, "dd-MMM-yyyy"));


        setUpDateView();
        setUPStatusLogView();

    }

    private void setUpView() {


        this.mInflater = LayoutInflater.from(this);
        mDateContainerView = (LinearLayout) this.findViewById(R.id.layoutDateViewContainer);
        mDateContainerView.removeAllViews();

        mStatusLogContainerView = (LinearLayout) this.findViewById(R.id.layoutStatusLog);
        mStatusLogContainerView.removeAllViews();

        etSubject = (TextView) this.findViewById(R.id.textViewLeaveTitle);
        etCreatedDate = (TextView) this.findViewById(R.id.textViewLeaveCreatedDate);
        etRemark = (TextView) this.findViewById(R.id.textViewRemark);

    }

    private void setUPStatusLogView() {


        ArrayList<LMSLeaveStatusLogModal> statusLogModals = modal.leaveStatusLogModals;

        mStatusLogContainerView.removeAllViews();

        for (LMSLeaveStatusLogModal status : statusLogModals) {

            View rowView = mInflater.inflate(R.layout.lms_status_log_row_view, null);

            TextView tvCreateBy = (TextView) rowView.findViewById(R.id.textViewStatusLogCreatedBy);
            TextView tvDate = (TextView) rowView.findViewById(R.id.textViewStatusLogDate);
            TextView tvRemark = (TextView) rowView.findViewById(R.id.textViewStatusLogRemark);
            tvCreateBy.setText(status.CreatedByUserName);

            switch (status.CurrentStatus){
                case LMSConstants.LEAVE_STATUS_PENDING:
                    tvCreateBy.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_pending, 0, 0, 0);
                    break;
                case LMSConstants.LEAVE_STATUS_APPROVE:
                    tvCreateBy.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_approved, 0, 0, 0);
                    break;
                case LMSConstants.LEAVE_STATUS_REJECT:
                    tvCreateBy.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_reject, 0, 0, 0);
                    break;
                case LMSConstants.LEAVE_STATUS_NOTICATION:
                    tvCreateBy.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_notify, 0, 0, 0);
                    break;
            }
            Date date = new Date();
            date.setTime(status.CreatedDate);
            tvDate.setText(Helper.getDateStringFromDate(date, "dd-MMM-yyyy hh:mm a"));
            tvRemark.setText(status.Remarks);

            mStatusLogContainerView.addView(rowView);

        }
    }

    private void setUpDateView() {
        try {
            mDateContainerView.removeAllViews();
            ArrayList<LMSLeaveDateModal> dates = modal.leaveDateModals;

            for (LMSLeaveDateModal dateModal :
                    dates) {
                View rowView = mInflater.inflate(R.layout.lms_date_row_view, null);
                Date dateObject = new Date();
                dateObject.setTime(dateModal.LeaveDate);
                String dateText = Helper.getDateStringFromDate(dateObject, "EEE   dd MMM yyyy");
                TextView tvDate = (TextView) rowView.findViewById(R.id.textViewLeaveDate);
                tvDate.setText(dateText);

                CheckBox cbIsHalfDayLeave = (CheckBox) rowView.findViewById(R.id.checkBoxLeaveIsHalfDay);
                cbIsHalfDayLeave.setVisibility(View.INVISIBLE);
                mDateContainerView.addView(rowView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_lmsleave_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      /*  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
