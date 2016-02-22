package com.samsung.ssc.LMS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.LMSLeaveTypeModal;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class LMSCreateLeaveActivity extends BaseActivity {

    private static final int REQUEST_PICK_DATE = 1;
    protected static final String SELECTED_DATE = "SelectedDate";
    protected static final String RESERVED_DATE = "ReservedDate";
    protected static final String SELECTED_LEAVE_TYPE = "SelectedLeaveType";
    private MaterialSpinner mSpinnerLeaveType;
    private ArrayList<Date> mSelectedDates;
    private ArrayList<Date> mReservedDates;
    private LinearLayout mDateContainerView;
    private TextInputLayout etRemark;
    private TextInputLayout etSubject;
    private CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmscreate_leave);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Apply Leave");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_DATE) {
            switch (resultCode) {

                case RESULT_OK:
                    ArrayList<Date> dates = (ArrayList<Date>) data.getExtras().get(LeaveCalenderActivity.SELETED_DATE);
                    mSelectedDates.clear();
                    mSelectedDates.addAll(dates);

                    mDateContainerView.removeAllViews();

                    for (Date date :
                            dates) {
                        View rowView = LayoutInflater.from(this).inflate(R.layout.lms_date_row_view, null);

                        String dateText = Helper.getDateStringFromDate(date, "dd MMM yyyy");
                        TextView tvDate = (TextView) rowView.findViewById(R.id.textViewLeaveDate);
                        tvDate.setText(dateText);

                        CheckBox cbIsHalfDayLeave = (CheckBox) rowView.findViewById(R.id.checkBoxLeaveIsHalfDay);

                        cbIsHalfDayLeave.setButtonDrawable(R.drawable.checkbox_lms_day_type_selector);
                        mDateContainerView.addView(rowView);

                    }
                    break;
            }
        }
    }


    private void setUpView() {


        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        mSelectedDates = new ArrayList<Date>();
        mReservedDates = new ArrayList<Date>();
        mSpinnerLeaveType = (MaterialSpinner) this.findViewById(R.id.spinnerLeaveType);
        mSpinnerLeaveType.setAdapter(new LeaveTypeAdapter(this));

        mSpinnerLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Object object = adapterView.getItemAtPosition(i);
                if (object instanceof LMSLeaveTypeModal) {
                    LMSLeaveTypeModal leaveTypeModal = (LMSLeaveTypeModal) adapterView.getItemAtPosition(i);
                    if (leaveTypeModal.LeaveBalance < 1) {
                        mSpinnerLeaveType.setError("You have no enough leave balance for " + leaveTypeModal.LeaveType);
                    } else {
                        mSpinnerLeaveType.setError(null);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDateContainerView = (LinearLayout) this.findViewById(R.id.layoutDateViewContainer);
        mDateContainerView.removeAllViews();

        etRemark = (TextInputLayout) this.findViewById(R.id.etLMSRemark);
        etRemark.getEditText().addTextChangedListener(new InputFieldTextListener(etRemark));
        etSubject = (TextInputLayout) this.findViewById(R.id.etLMSSubject);
        etSubject.getEditText().addTextChangedListener(new InputFieldTextListener(etSubject));




        mReservedDates = DatabaseHelper.getConnection(this).getReservedDate(Integer.parseInt(Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)));
    }

    public void onCalendarClick(View view) {

        if (mSpinnerLeaveType.getSelectedItemPosition() == 0 || mSpinnerLeaveType.getError()!=null) {
            mSpinnerLeaveType.setError("Please select leave type first");
         } else {
            LMSLeaveTypeModal typeModal = (LMSLeaveTypeModal) mSpinnerLeaveType.getSelectedItem();
            if(typeModal.LeaveBalance>1) {
                mSpinnerLeaveType.setError(null);
                Intent intent = new Intent(this, LeaveCalenderActivity.class);
                intent.putExtra(SELECTED_DATE, mSelectedDates);
                intent.putExtra(RESERVED_DATE, mReservedDates);
                intent.putExtra(SELECTED_LEAVE_TYPE, typeModal);
                startActivityForResult(intent, REQUEST_PICK_DATE);
            }else{
                Snackbar snackbar = Snackbar
                        .make(mCoordinatorLayout, "No leave balance for "+typeModal.LeaveType, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.DKGRAY);
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }

    }

    class LeaveTypeAdapter extends BaseAdapter {
        private final List<LMSLeaveTypeModal> items;
        private final LayoutInflater inflater;

        public LeaveTypeAdapter(Context context) {
            this.items = geLeaveType();
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = inflater.inflate(android.R.layout.simple_list_item_activated_1, null);

            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setBackgroundColor(Color.WHITE);
            tv.setPadding(5, 5, 5, 0);
            tv.setTextColor(Color.BLACK);
            tv.setText(items.get(i).LeaveTypeCode);

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu
                                                   menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lmscreate_leave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit) {

            if (isInputValid()) {
                onCreateLeaveClick();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isInputValid() {

        if (TextUtils.isEmpty(etSubject.getEditText().getText())){
            etSubject.setErrorEnabled(true);
            etSubject.setError("Field can't be blank");
            return false;
        }else if(mSpinnerLeaveType.getSelectedItemPosition() == 0){
            etSubject.setErrorEnabled(false);
             mSpinnerLeaveType.setError("Select leave type");
            return false;
        }else if(mDateContainerView.getChildCount()<1){
            mSpinnerLeaveType.setError(null);
            Animation anim = AnimationUtils.loadAnimation(this,
                    R.anim.shake);
            ((View)mDateContainerView.getParent()).startAnimation(anim);
            return false;
        }
        else if(TextUtils.isEmpty(etRemark.getEditText().getText())){
            etRemark.setErrorEnabled(true);
            etRemark.setError("Field can't be blank");
            return false;
        }else{
            etRemark.setErrorEnabled(false);

            return true;
        }
    }

    private void onCreateLeaveClick() {

        try {

            JSONObject leaveJSONObject = new JSONObject();
            leaveJSONObject.put("LMSLeaveTypeMasterID",( (LMSLeaveTypeModal) mSpinnerLeaveType.getSelectedItem()).LMSLeaveTypeMasterID);
            leaveJSONObject.put("LeaveSubject",etSubject.getEditText().getText());
            leaveJSONObject.put("Remarks",etRemark.getEditText().getText());
            leaveJSONObject.put("AppliedDate",Helper.getDateStringFromDate(new Date(), "dd-MMM-yyyy"));
            leaveJSONObject.put("CurrentStatus",LMSConstants.TAB_SUBMITTED);


            JSONArray jsonArrayDates = new JSONArray();
            int count = mDateContainerView.getChildCount();
            for (int i = 0;i<count;i++){
                JSONObject jsonObject = new JSONObject();
                View rowView = mDateContainerView.getChildAt(i);
                jsonObject.put("CreatedBy", Integer.parseInt(Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)));
                jsonObject.put("IsHalfDay",((CheckBox) rowView.findViewById(R.id.checkBoxLeaveIsHalfDay)).isChecked());
                jsonObject.put("LeaveDate", ((TextView) rowView.findViewById(R.id.textViewLeaveDate)).getText());
                jsonObject.put("CurrentStatus", LMSConstants.TAB_SUBMITTED);
                jsonArrayDates.put(jsonObject);
            }



            leaveJSONObject.put("LMSLeaveDetails",jsonArrayDates);
            leaveJSONObject.put("NumberOfLeave", count);


            JSONArray leavesArray = new JSONArray();
            leavesArray.put(leaveJSONObject);


            JSONObject requestObject = new JSONObject();
            requestObject.put(WebConfig.WebParams.USER_ID, Integer.parseInt(Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)));
            requestObject.put(WebConfig.WebParams.ROLE_ID, Helper.getIntValueFromPrefs(this, SharedPreferencesKey.PREF_ROLEID));
            requestObject.put("ApproverRemarks", "");
            requestObject.put("LMSLeaveMasterData",leavesArray);


            PostDataToNetwork network = new PostDataToNetwork(this, "", new GetDataCallBack() {
                @Override
                public void processResponse(Object result) {
                    if (result!=null){

                        FetchingdataParser parser  = new FetchingdataParser(LMSCreateLeaveActivity.this);
                        final ResponseDto response = parser.getResponseResult(result.toString());

                            Helper.showAlertDialog(LMSCreateLeaveActivity.this, SSCAlertDialog.SUCCESS_TYPE,(response.isSuccess()?"Leave applied":"Server not responding")+"Leave Applied","","OK", new SSCAlertDialog.OnSDAlertDialogClickListener() {
                                @Override
                                public void onClick(SSCAlertDialog sdAlertDialog) {
                                    sdAlertDialog.dismiss();
                                    if (response.isSuccess()) {
                                        Intent intent = getIntent();
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }
                                }
                            },false,null,null);


                    }
                }
            });
            network.setConfig(getString(R.string.url), WebConfig.WebMethod.SUMBIT_LEAVE_REQUEST);

            network.execute(requestObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public List<LMSLeaveTypeModal> geLeaveType() {

        List<LMSLeaveTypeModal> data  = DatabaseHelper.getConnection(this).getLeaveTypeData();

        return data;
    }


    class InputFieldTextListener implements TextWatcher {
        TextInputLayout textInput;

        public InputFieldTextListener(TextInputLayout inputLayout) {
            this.textInput = inputLayout;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (textInput.getId()) {
                case R.id.etLMSRemark:
                    if (charSequence.length() < 20) {
                        textInput.setError("At least 20 character should be on title");
                        textInput.setErrorEnabled(true);
                    } else {
                        textInput.setErrorEnabled(false);
                    }

                    break;

                case R.id.etLMSSubject:

                    if (charSequence.length() < 10) {
                        textInput.setError("At least 10 character should be on Subject");
                        textInput.setErrorEnabled(true);
                    } else {
                        textInput.setErrorEnabled(false);
                    }

                    break;

                default:
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
