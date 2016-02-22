package com.samsung.ssc.LMS;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.samsung.ssc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LeaveListFilterActivity extends Activity {

    private static final int DATE_DIALOG_ID_START = 1;
    private static final int DATE_DIALOG_ID_END = 2;

    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private TextInputLayout fromDateEtxt;
    private SimpleDateFormat dateFormatter;
    private TextInputLayout toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
     private long toDateTime = 0;
    private long fromDateTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list_filter);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
    }

    private void findViewsById() {
        fromDateEtxt = (TextInputLayout) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.getEditText().setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (TextInputLayout) findViewById(R.id.etxt_todate);
        toDateEtxt.getEditText().setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                fromDateTime = newDate.getTimeInMillis();
                fromDateEtxt.getEditText(). setText(dateFormatter.format(newDate.getTime()));
                toDateEtxt.getEditText().setText("");

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                toDateTime = newDate.getTimeInMillis();
                if (fromDateTime<toDateTime){
                    toDateEtxt.getEditText().setText(dateFormatter.format(newDate.getTimeInMillis()));
                }else{
                    toDateEtxt.setErrorEnabled(true);
                    toDateEtxt.setError("To date show be grater then from date");
                }



            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }





    public void onFromDateClick(View v){
        fromDateEtxt.setErrorEnabled(false);
        fromDatePickerDialog.show();
    }

    public void onToDateClick(View v){


        if (!TextUtils.isEmpty(fromDateEtxt.getEditText().getText().toString())) {
            toDateEtxt.setErrorEnabled(false);
            toDatePickerDialog.show();
        } else {
            fromDateEtxt.setErrorEnabled(true);
            fromDateEtxt.setError("Select From date");
        }
    }


    public void onFilterClick(View view){

        if (!TextUtils.isEmpty(toDateEtxt.getEditText().getText().toString()) && !TextUtils.isEmpty(fromDateEtxt.getEditText().getText().toString())) {
            Intent intent=getIntent();
            intent.putExtra("FROM_DATE", fromDateTime);
            intent.putExtra("FROM_DATE_VALUE", fromDateEtxt.getEditText().getText().toString());
            intent.putExtra("TO_DATE",toDateTime);
            intent.putExtra("TO_DATE_VALUE",toDateEtxt.getEditText().getText().toString());
            setResult(RESULT_OK,intent);
            finish();
        } else {
            Toast.makeText(this,"Please select date first",Toast.LENGTH_LONG).show();
        }
    }
}