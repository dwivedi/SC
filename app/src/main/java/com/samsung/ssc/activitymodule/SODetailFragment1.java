package com.samsung.ssc.activitymodule;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class SODetailFragment1 extends Fragment implements OnClickListener {

	private View _rootView;
	//private EditText mEtSoNumber;
	private LinearLayout mLLContainer;

	private TextView tvMSCCode;
	private TextView tvBPName;
	private TextView tvBranchName;
	private TextView tvcloseDate;

	private TextView tvClaimDate;
	private TextView tvServiceOrderNumber;
	private TextView tvdefectDesc;
	private TextView tvGCICResponseDesc;

	private TextView tvGCICStatus;
	private TextView tvProduct;
	private TextView tvReceivedDate;
	private TextView tvRejectionReason;

	private TextView tvRejectioNRemark;
	private TextView tvRepairDesc;
	private TextView tvRlsStatus;
	private TextView tvSawDateTime;

	private TextView tvSawNumber;
	private TextView tvSawStatus;
	private TextView tvSerialNumberIMEI;
	private TextView tvRLSDTGMRSStatus;
	
	private TabHost mTabHost;
 	private ViewPager mViewPager;
	//private Button mSubmitButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		//String mRLSResponce = getArguments().getString("RLSResponse");
		
	/*	if (mRLSResponce !=null) {
			parseResponse(mRLSResponce);
		}else {
			
		}*/
		
		
		if (_rootView == null) {
			_rootView = inflater.inflate(
					R.layout.fragment_so_detail1, container, false);

			setUpBasicView();

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

		return _rootView;

	}

	private void setUpBasicView() {
		
		//mEtSoNumber = (EditText) _rootView.findViewById(R.id.et_soNumberSoDetail);
		//mSubmitButton = (Button)_rootView.findViewById(R.id.btnLoginSubmit);
		
		mLLContainer = (LinearLayout) _rootView.findViewById(R.id.ll_containerSoDetail);

		tvMSCCode = (TextView) _rootView.findViewById(R.id.tv_ascCodeRODetail);
		tvBPName = (TextView) _rootView.findViewById(R.id.tv_branchCodeRODetail);
		tvBranchName = (TextView) _rootView.findViewById(R.id.tv_branchNameRODetail);
		tvcloseDate = (TextView) _rootView.findViewById(R.id.tv_closeDateRODetail);

		tvClaimDate = (TextView) _rootView.findViewById(R.id.tv_claimDateRODetail);
		tvServiceOrderNumber = (TextView) _rootView.findViewById(R.id.tv_claimNumberRODetail);
		tvdefectDesc = (TextView) _rootView.findViewById(R.id.tv_defectDescRODetail);
		tvGCICResponseDesc = (TextView) _rootView.findViewById(R.id.tv_gcicReasonDescRODetail);

		tvGCICStatus = (TextView) _rootView.findViewById(R.id.tv_gcicStatusRODetail);
		tvProduct = (TextView) _rootView.findViewById(R.id.tv_productRODetail);
		tvReceivedDate = (TextView) _rootView.findViewById(R.id.tv_receivedDateRODetail);
		tvRejectionReason = (TextView) _rootView.findViewById(R.id.tv_rejectionReasonRODetail);

		tvRejectioNRemark = (TextView) _rootView.findViewById(R.id.tv_rejectionRemarkRODetail);
		tvRepairDesc = (TextView) _rootView.findViewById(R.id.tv_repairDescRODetail);
		tvRlsStatus = (TextView) _rootView.findViewById(R.id.tv_rlsStatusRODetail);
		tvSawDateTime = (TextView) _rootView.findViewById(R.id.tv_sawDateTimeRODetail);

		tvSawNumber = (TextView) _rootView.findViewById(R.id.tv_sawNumberRODetail);
		tvSawStatus = (TextView) _rootView.findViewById(R.id.tv_sawStatusRODetail);
		tvSerialNumberIMEI = (TextView) _rootView.findViewById(R.id.tv_serialNumberIMEIRODetail);
		tvRLSDTGMRSStatus = (TextView) _rootView.findViewById(R.id.tv_statusIDRODetail);
		
		
		
	/*	mSubmitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmitButtonClick();
				
			}
		});*/
		
		
		
	}
	
	

	
	 

 


	private void parseResponse(Object response) {
		try {
			JSONObject jsonObject = new JSONObject(response.toString());
			boolean isSuccess = jsonObject.getBoolean("IsSuccess");
			if (isSuccess) {

				JSONObject singleResult = jsonObject.isNull("SingleResult") ? null
						: jsonObject.getJSONObject("SingleResult");
				if (singleResult != null) {
					showData(singleResult);
				} else {
					
					clearLastSyncData();
					aletBoxRLSData();

				}

			} else {
				clearLastSyncData();
				aletBoxRLSData();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void aletBoxRLSData() {
		Helper.showAlertDialog(
				getActivity(),
				SSCAlertDialog.WARNING_TYPE,
				getActivity().getString(R.string.error3),
				getActivity()
						.getString(R.string.no_data_availble_rls),
						getActivity().getString(R.string.ok),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(
							SSCAlertDialog sdAlertDialog) {
						clearLastSyncData();
						sdAlertDialog.dismiss();

					}
				}, false, null, null);
	}

	private void clearLastSyncData() {
		
		tvMSCCode.setText("");
		tvBPName.setText("");
		tvBranchName.setText("");
		tvcloseDate.setText("");

		tvClaimDate.setText("");
		tvServiceOrderNumber.setText("");
		tvdefectDesc.setText("");
		tvGCICResponseDesc.setText("");

		tvGCICStatus.setText("");
		tvProduct.setText("");
		tvReceivedDate.setText("");
		tvRejectionReason.setText("");

		tvRejectioNRemark.setText("");
		tvRepairDesc.setText("");
		tvRlsStatus.setText("");
		tvSawDateTime.setText("");

		tvSawNumber.setText("");
		tvSawStatus.setText("");
		tvSerialNumberIMEI.setText("");
		tvRLSDTGMRSStatus.setText("");
	}

	private void showData(JSONObject jObject) {

		try {

			String stAsCode = jObject.isNull("AscCode") ? "" : jObject
					.getString("AscCode");
			String stBPName = jObject.isNull("BPNAME") ? "" : jObject
					.getString("BPNAME");
			String stBranchName = jObject.isNull("BranchName") ? "" : jObject
					.getString("BranchName");
			String stCloseDate = jObject.isNull("CLOSE_DATE") ? "" : jObject
					.getString("CLOSE_DATE");

			String stClaimDate = jObject.isNull("ClaimDate") ? "" : jObject
					.getString("ClaimDate");
			String stClaimNumber = jObject.isNull("ClaimNo") ? "" : jObject
					.getString("ClaimNo");
			String stDefectDesc = jObject.isNull("DEFECT_DESC") ? "" : jObject
					.getString("DEFECT_DESC");
			String stGCICReasoDesc = jObject.isNull("GCICReasonDescription") ? ""
					: jObject.getString("GCICReasonDescription");

			String stGCICStatus = jObject.isNull("GcicStatus") ? "" : jObject
					.getString("GcicStatus");
			String stProduct = jObject.isNull("Product") ? "" : jObject
					.getString("Product");
			String stReceivedDate = jObject.isNull("RECEIVED_DT") ? ""
					: jObject.getString("RECEIVED_DT");
			String stReejctionReason = jObject.isNull("RejectReason") ? ""
					: jObject.getString("RejectReason");

			String stRejectionRemarks = jObject.isNull("RejectRemarks") ? ""
					: jObject.getString("RejectRemarks");
			String stRepairDesc = jObject.isNull("RepairDesc") ? "" : jObject
					.getString("RepairDesc");
			String stRlsStatus = jObject.isNull("RlsStatus") ? "" : jObject
					.getString("RlsStatus");
			String stSawDateTime = jObject.isNull("SawDateTime") ? "" : jObject
					.getString("SawDateTime");

			String stSawNo = jObject.isNull("SawNo") ? "" : jObject
					.getString("SawNo");
			String stSawStatus = jObject.isNull("SawStatus") ? "" : jObject
					.getString("SawStatus");
			String stSerialNoIMEI = jObject.isNull("SerialNoIMEI") ? ""
					: jObject.getString("SerialNoIMEI");
			String stStatusID = jObject.isNull("StatusID") ? "" : jObject
					.getString("StatusID");

			tvMSCCode.setText(stAsCode);
			tvBPName.setText(stBPName);
			tvBranchName.setText(stBranchName);
			tvcloseDate.setText(stCloseDate);

			tvClaimDate.setText(stClaimDate);
			tvServiceOrderNumber.setText(stClaimNumber);
			tvdefectDesc.setText(stDefectDesc);
			tvGCICResponseDesc.setText(stGCICReasoDesc);

			tvGCICStatus.setText(stGCICStatus);
			tvProduct.setText(stProduct);
			tvReceivedDate.setText(stReceivedDate);
			tvRejectionReason.setText(stReejctionReason);

			tvRejectioNRemark.setText(stRejectionRemarks);
			tvRepairDesc.setText(stRepairDesc);
			tvRlsStatus.setText(stRlsStatus);
			tvSawDateTime.setText(stSawDateTime);

			tvSawNumber.setText(stSawNo);
			tvSawStatus.setText(stSawStatus);
			tvSerialNumberIMEI.setText(stSerialNoIMEI);
			tvRLSDTGMRSStatus.setText(stStatusID);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// get the instance of the parent activity

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void setRLSResponce(Object response) {
		
		parseResponse(response);
	}

}