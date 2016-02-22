package com.samsung.ssc.LMS;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.LMSLeaveTypeModal;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeaveCalenderActivity extends AppCompatActivity {

    public static final String SELETED_DATE = "SelectedDate";
    private CalendarPickerView mCalendar;
    private ArrayList<Date> mSelectedDate;
    private ArrayList<Date> mReservedDate;
    private LMSLeaveTypeModal mLeaveType;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        mSelectedDate = new ArrayList<Date>();
        mReservedDate = new ArrayList<Date>();


        getBundleValues();

        setUpView();

    }

    private void setUpView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Date");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        setUpCalenderView();
    }

    private void setUpCalenderView() {

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        mCalendar = (CalendarPickerView) findViewById(R.id.calendar_view);

        mCalendar.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            Calendar cal = Calendar.getInstance();

            @Override
            public boolean isDateSelectable(Date date) {
                boolean isSelecteable = true;
                cal.setTime(date);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                //disable if weekend
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    isSelecteable = false;
                }

                if (mReservedDate.contains(date)) {
                    isSelecteable = false;
                }


                return isSelecteable;
            }
        });


        mCalendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE) //
                .withSelectedDates(mSelectedDate);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calender, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit) {

            onSubmitDate();

        }

        return true;
    }

    private void onSubmitDate() {

        if (isValidate()) {
            mSelectedDate.clear();

            mSelectedDate.addAll(mCalendar.getSelectedDates());

            Intent intent = getIntent();
            intent.putExtra(SELETED_DATE, mSelectedDate);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean isValidate() {

        if (mLeaveType.ConsecutiveLeaves < mCalendar.getSelectedDates().size()) {

            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, mLeaveType.LeaveType + " allow only " + mLeaveType.ConsecutiveLeaves + " Consecutive Leaves", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        } else if (mLeaveType.LeaveBalance < mCalendar.getSelectedDates().size()) {
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, "You have only " + mLeaveType.LeaveBalance + " " + mLeaveType.LeaveType + " available", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }

        return true;

    }

    public void getBundleValues() {

        Bundle bundle = getIntent().getExtras();
        ArrayList<Date> selectedDates = (ArrayList<Date>) bundle.get(LMSCreateLeaveActivity.SELECTED_DATE);
        ArrayList<Date> reservedDates = (ArrayList<Date>) bundle.get(LMSCreateLeaveActivity.RESERVED_DATE);
        mLeaveType = (LMSLeaveTypeModal) bundle.getParcelable(LMSCreateLeaveActivity.SELECTED_LEAVE_TYPE);
        mSelectedDate.clear();
        mSelectedDate.addAll(selectedDates);
        mReservedDate.clear();
        mReservedDate.addAll(reservedDates);

    }
}
