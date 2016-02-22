package com.samsung.ssc.activitymodule;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.dto.PaymentModes;
import com.samsung.ssc.util.Helper;

/**
 * 
 * @author Ankit Saxena To add entries for collection activities..
 * 
 */

public class AddCollectionDialog extends BaseActivity {

	private Spinner mSpinnerPaymentMode;
	private String[] mPaymentModes;
	private String[] mPaymentModesId;
	private EditText mEtAmount, mEtTransationID, mEtTransationDate,
			mEtDescription;
	private Bundle mBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setTitle(R.string.add_collection);
			setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
		}*/
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_collection_entries1);
		
		mBundle = getIntent().getExtras();
		setUpView();

	}
	 
	/**
	 * 
	 */
	private void setUpView() {

		mEtAmount = (EditText) findViewById(R.id.enterAmount);
		mEtTransationID = (EditText) findViewById(R.id.transId);
		mEtTransationDate = (EditText) findViewById(R.id.transDate);
		mEtTransationDate.setFocusable(false);
		mEtTransationDate.setKeyListener(null);
		mEtTransationDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openCalenderDialog();
			}
		});
		mEtDescription = (EditText) findViewById(R.id.description);
		mSpinnerPaymentMode = (Spinner) findViewById(R.id.paymentModes);

		setUpPaymentSpinner();
	}

	private void setUpPaymentSpinner() {
		int paymentModeCount = Collections.mPaymentModeList.size();

		if (paymentModeCount > 0) {
			mPaymentModes = new String[paymentModeCount];
			mPaymentModesId = new String[paymentModeCount];

			for (int i = 0; i < paymentModeCount; i++) {

				PaymentModes modal = Collections.mPaymentModeList.get(i);
				mPaymentModes[i] = modal.getModeName();
				mPaymentModesId[i] = modal.getPaymentModeID();
			}

			ArrayAdapter<String> paymentModeAdapter = new ArrayAdapter<String>(
					AddCollectionDialog.this,
					android.R.layout.simple_spinner_item, mPaymentModes);
			paymentModeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpinnerPaymentMode.setAdapter(paymentModeAdapter);

			mSpinnerPaymentMode
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							if (position == 0) {
								mEtTransationID.setVisibility(View.GONE);
								// transDate.setVisibility(View.GONE);
								mEtTransationDate.setHint(R.string.date);
								mEtTransationID.setText("");
							}

							if (position == 1) {
								mEtTransationID.setVisibility(View.VISIBLE);
								mEtTransationDate.setVisibility(View.VISIBLE);

								mEtTransationID.setHint(R.string.chequeNo);
								mEtTransationDate.setHint(R.string.chequeDate);
							} else if (position == 2) {
								mEtTransationID.setVisibility(View.VISIBLE);
								mEtTransationDate.setVisibility(View.VISIBLE);
								mEtTransationID.setHint(R.string.ddNo);
								mEtTransationDate.setHint(R.string.ddDate);
							}

							else if (position == 3 || position == 4) {
								mEtTransationID.setVisibility(View.VISIBLE);
								mEtTransationDate.setVisibility(View.VISIBLE);
								mEtTransationID.setHint(R.string.transactionId);
								mEtTransationDate
										.setHint(R.string.transactionDate);
							}

						}

						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

			if (mBundle != null) {// set edited value into spinner
				int index = -1;
				for (int i = 0; (i < mPaymentModes.length) && (index == -1); i++) {
					if (mPaymentModes[i].equals(mBundle.get("paymentMode"))) {
						index = i;
					}
				}
				mSpinnerPaymentMode.setSelection(index);

			} else {
				mSpinnerPaymentMode.setSelection(0);
			}
			mSpinnerPaymentMode.setPrompt(getString(R.string.paymentMode));

			if (mBundle != null) {
				mEtAmount.setText(mBundle.getString("amount"));
				mEtTransationID.setText(mBundle.getString("transId"));
				mEtTransationDate.setText(mBundle.getString("transDate"));
				mEtDescription.setText(mBundle.getString("description"));
			}

		} else {

		
			
			
			Helper.showCustomToast(getApplicationContext(), R.string.download_authorization_not_assign_to_you, Toast.LENGTH_LONG);
			

			finish();
			return;
		}

	}

	public void onAddCollectionDataClick(View v) {
		try {
			if (validation()) {
				Bundle data = new Bundle();

				if (mBundle != null) {
					int position = mBundle.getInt("position");
					data.putString("editmode", "editmode");
					data.putInt("editposition", position);
				}
				data.putString("paymentMode", mPaymentModes[mSpinnerPaymentMode
						.getSelectedItemPosition()]);
				data.putString("paymentModeId",
						mPaymentModesId[mSpinnerPaymentMode
								.getSelectedItemPosition()]);
				data.putString("amount", mEtAmount.getText().toString().trim());
				data.putString("transId", mEtTransationID.getText().toString()
						.trim());
				data.putString("transDate", mEtTransationDate.getText()
						.toString().trim());
				data.putString("description", mEtDescription.getText()
						.toString().trim());

				Intent i = new Intent(this, Collections.class);
				i.putExtras(data);
				setResult(RESULT_OK, i);
				finish();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onCancelCollectionDataClick(View v) {
		finish();

	}

	public void onTransactionDatePickerClick(View v) {
		openCalenderDialog();

	}

	private void openCalenderDialog() {

		Calendar calendar = Calendar.getInstance();
		DatePickerDialog datePicker = new DatePickerDialog(
				AddCollectionDialog.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month,
							int date) {

						try {

							String dispalyDate = (month + 1) + " " + date + " "
									+ year;

							mEtTransationDate.setText(dispalyDate);

							mEtDescription.requestFocus();

						} catch (Exception e) {
							Helper.printStackTrace(e);
						}
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE));
		datePicker.show();

	}

	public boolean validation() {

		boolean flag = true;

		if (mSpinnerPaymentMode.getSelectedItemPosition() == 0) {

			if (mEtAmount.length() == 0) {
				mEtAmount.setError(getStringFromResource(R.string.amount));
				flag = false;
			}

			if (mEtTransationDate.length() == 0) {
				mEtTransationDate
						.setError(getStringFromResource(R.string.chequeDate));
				flag = false;
			}

			return flag;
		}

		if (mEtAmount.length() == 0) {
			mEtAmount.setError(getStringFromResource(R.string.amount));
			flag = false;
		}

		if (mEtTransationID.length() == 0) {
			if (mSpinnerPaymentMode.getSelectedItemPosition() == 1) {
				mEtTransationID
						.setError(getStringFromResource(R.string.chequeNo));
				flag = false;
			}
			if (mSpinnerPaymentMode.getSelectedItemPosition() == 2) {
				mEtTransationID.setError(getStringFromResource(R.string.ddNo));
				flag = false;
			}
			if (mSpinnerPaymentMode.getSelectedItemPosition() == 3) {
				mEtTransationID
						.setError(getStringFromResource(R.string.transactionId));
				flag = false;
			}

		}

		if (mEtTransationDate.length() == 0) {
			if (mSpinnerPaymentMode.getSelectedItemPosition() == 1) {
				mEtTransationDate
						.setError(getStringFromResource(R.string.chequeDate));
				flag = false;
			}
			if (mSpinnerPaymentMode.getSelectedItemPosition() == 2) {
				mEtTransationDate
						.setError(getStringFromResource(R.string.ddDate));
				flag = false;
			}
			if (mSpinnerPaymentMode.getSelectedItemPosition() == 3) {
				mEtTransationDate
						.setError(getStringFromResource(R.string.transactionDate));
				flag = false;
			}

		}

		return flag;
	}

}
