package com.samsung.ssc.LMS;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.LMSLeaveDataModal;
import com.samsung.ssc.dto.LMSLeaveDateModal;
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

public class LMSLeaveActionActivity extends BaseActivity {


    private LMSLeaveDataModal modal;
    private TextView etSubject;
    private LayoutInflater mInflater;
    private TextView etRemark;
    private LinearLayout mDateContainerView;
    private TextView etCreatedDate;
    private TextView tvLeaveAppliedName;
    private TextInputLayout etLMSSubmitRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmsleave_action);



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
        tvLeaveAppliedName.setText(modal.CreatedByUserName);
        etRemark.setText(modal.Remarks);
        Date mDate = new Date();
        mDate.setTime(modal.CreatedDate);
        etCreatedDate.setText(Helper.getDateStringFromDate(mDate, "dd-MMM-yyyy"));

        setUpDateView();

    }

    private void setUpDateView() {
        try {
            mDateContainerView.removeAllViews();
            ArrayList<LMSLeaveDateModal> dates = modal.leaveDateModals;

            for (final LMSLeaveDateModal dateModal :
                    dates) {
                View rowView = mInflater.inflate(R.layout.lms_date_row_view, null);
                Date dateObject = new Date();
                dateObject.setTime(dateModal.LeaveDate);
                String dateText = Helper.getDateStringFromDate(dateObject, "EEE   dd MMM yyyy");
                TextView tvDate = (TextView) rowView.findViewById(R.id.textViewLeaveDate);
                tvDate.setText(dateText);
                if (dateModal.IsHalfDay) {
                    tvDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checkbox_lms_half, 0);
                }

                CheckBox cbIsApproved = (CheckBox) rowView.findViewById(R.id.checkBoxLeaveIsHalfDay);
                cbIsApproved.setChecked(dateModal.CurrentStatus!=LMSConstants.LEAVE_STATUS_REJECT);

                mDateContainerView.addView(rowView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpView() {


        this.mInflater = LayoutInflater.from(this);
        mDateContainerView = (LinearLayout) this.findViewById(R.id.layoutDateViewContainer);
        mDateContainerView.removeAllViews();

        etSubject = (TextView) this.findViewById(R.id.textViewLeaveTitle);
        tvLeaveAppliedName = (TextView) this.findViewById(R.id.textViewLeaveAppliedName);

        etCreatedDate = (TextView) this.findViewById(R.id.textViewLeaveCreatedDate);
        etRemark = (TextView) this.findViewById(R.id.textViewRemark);
        etLMSSubmitRemark = (TextInputLayout)this.findViewById(R.id.etLMSSubmitRemark);

    }

    public void onClickReject(View view){
        leaveAction(LMSConstants.LEAVE_STATUS_REJECT);
    }

    public void onClickApprove(View view){ leaveAction(LMSConstants.LEAVE_STATUS_APPROVE); }


    public void leaveAction(final int leaveStatus){

        try {

            JSONObject leaveJSONObject = new JSONObject();

            leaveJSONObject.put("LMSLeaveTypeMasterID",modal.LMSLeaveTypeMasterID);
            leaveJSONObject.put("LeaveMasterID",modal.LeaveMasterID);
            leaveJSONObject.put("LeaveSubject",modal.LeaveSubject);
            leaveJSONObject.put("Remarks",modal.Remarks); //TODO LEAVE
            leaveJSONObject.put("AppliedDate",Helper.getDateStringFromDate(new Date(), "dd-MMM-yyyy"));
            leaveJSONObject.put("CurrentStatus",leaveStatus);


            JSONArray jsonArrayDates = new JSONArray();
            int count = mDateContainerView.getChildCount();
            for (int i = 0;i<count;i++){
                JSONObject jsonObject = new JSONObject();
                View rowView = mDateContainerView.getChildAt(i);
                jsonObject.put("LMSLeaveDetailID", modal.leaveDateModals.get(i).LMSLeaveDetailID);
                jsonObject.put("CreatedBy", Integer.parseInt(Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)));
                jsonObject.put("IsHalfDay",modal.leaveDateModals.get(i).IsHalfDay);
                jsonObject.put("LeaveDate", ((TextView) rowView.findViewById(R.id.textViewLeaveDate)).getText());
                jsonObject.put("CurrentStatus", ((CheckBox) rowView.findViewById(R.id.checkBoxLeaveIsHalfDay)).isChecked()?LMSConstants.LEAVE_STATUS_APPROVE:LMSConstants.LEAVE_STATUS_REJECT);  //TODO need to be change
                jsonArrayDates.put(jsonObject);
            }
            leaveJSONObject.put("LMSLeaveDetails",jsonArrayDates);
            leaveJSONObject.put("NumberOfLeave", count);


            JSONArray leavesArray = new JSONArray();
            leavesArray.put(leaveJSONObject);


            JSONObject requestObject = new JSONObject();
           requestObject.put(WebConfig.WebParams.USER_ID, Integer.parseInt(Helper.getStringValuefromPrefs(this, SharedPreferencesKey.PREF_USERID)));
            requestObject.put(WebConfig.WebParams.ROLE_ID, Helper.getIntValueFromPrefs(this, SharedPreferencesKey.PREF_ROLEID));
            requestObject.put("ApproverRemarks", TextUtils.isEmpty(etLMSSubmitRemark.getEditText().getText().toString())?"":etLMSSubmitRemark.getEditText().getText().toString());



            requestObject.put("LMSLeaveMasterData",leavesArray);


            PostDataToNetwork network = new PostDataToNetwork(this, "", new GetDataCallBack() {
                @Override
                public void processResponse(Object result) {
                    if (result!=null){

                        FetchingdataParser parser  = new FetchingdataParser(LMSLeaveActionActivity.this);
                        ResponseDto response = parser.getResponseResult(result.toString());

                        if (response.isSuccess()){
                            Helper.showAlertDialog(LMSLeaveActionActivity.this, SSCAlertDialog.SUCCESS_TYPE,"Leave "+(leaveStatus==LMSConstants.LEAVE_STATUS_APPROVE?"Approved":"Rejected"),"","OK", new SSCAlertDialog.OnSDAlertDialogClickListener() {
                                @Override
                                public void onClick(SSCAlertDialog sdAlertDialog) {
                                    sdAlertDialog.dismiss();
                                    Intent intent = getIntent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                 }
                            },false,null,null);

                        }

                    }
                }
            });
            network.setConfig(getString(R.string.url), WebConfig.WebMethod.SUMBIT_LEAVE_REQUEST);

            network.execute(requestObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
