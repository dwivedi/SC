package com.samsung.ssc.MOM;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.MOMDetailModal;
import com.samsung.ssc.util.Helper;
 
public class MOMDetailsActivity extends BaseActivity {

	private EditText etDate;
	private TextInputLayout etLocation;
	private TextInputLayout etTitle;
	private TextInputLayout etActionItem;
	private TextInputLayout etDiscription;
	private LinearLayout llAttendeesContainer;
	private int year;
	private int month;
	private int day;
	private MOMDetailModal modal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mom_details);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
		toolbar.setTitle("MOM Details");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_mom_details,menu);

		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.menu_delete_mom:
				onDeleteMOM(null);
				break;
			case  R.id.menu_update_mom:
				onUpdateMOM(null);
				break;

			default:
				break;

		}

		return super.onOptionsItemSelected(item);
	}

	private void setUpView() {

		modal = (MOMDetailModal) getIntent().getExtras().getParcelable(
				"MOM_MODAL");

		etDate = (EditText) this.findViewById(R.id.etMOMDate);
		etDate.setText(modal.momDate);
		etLocation = (TextInputLayout) this.findViewById(R.id.etMOMLocation);
		etLocation.getEditText().setText(modal.momLocaton);
		etTitle = (TextInputLayout) this.findViewById(R.id.etMOMTile);
		etTitle.getEditText().setText(modal.momTitle);
		etTitle.getEditText().addTextChangedListener(new MOMInputFieldTextListuner(etTitle));
		etActionItem = (TextInputLayout) this.findViewById(R.id.etMOMActionItem);
		etActionItem.getEditText().setText(modal.momActionItem);
		etActionItem.getEditText().addTextChangedListener(new MOMInputFieldTextListuner(etActionItem));
		etDiscription = (TextInputLayout) this.findViewById(R.id.etMOMDiscription);
		etDiscription.getEditText().setText(modal.momDiscription);
		etDiscription.getEditText().addTextChangedListener(new MOMInputFieldTextListuner(etDiscription));
		llAttendeesContainer = (LinearLayout) this
				.findViewById(R.id.llMOMAttendeesContainer);
		ArrayList<String> attendees = modal.momAttendees;
		for (String string : attendees) {
			EditText editComment = new EditText(this);
			editComment.setText(string);
			editComment.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
			editComment.setTextColor(ContextCompat.getColor(this, R.color.text_color));
			llAttendeesContainer.addView(editComment);
		}

		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

	}

	private void showDate(Date dateFromDatePicket) {
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String s = df.format(dateFromDatePicket);
		etDate.setText(s);

	}

	private java.util.Date getDateFromDatePicket(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime();
	}

	public void onDataClick(View view) {
		DatePickerDialog dialog = new DatePickerDialog(this, myDateListener,
				year, month, day);
		dialog.getDatePicker().setMaxDate(new Date().getTime());
		dialog.show();

	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int arg1, int arg2,
				int arg3) {
			showDate(getDateFromDatePicket(datePicker));
		}
	};

	public void onAddAttendeesClick(View view) {

		EditText lastView = (EditText) llAttendeesContainer
				.getChildAt(llAttendeesContainer.getChildCount() - 1);
		if (!TextUtils.isEmpty(lastView.getText())) {
			Helper.setKeyBoardShow(MOMDetailsActivity.this, false);

			final EditText editComment = new EditText(this);
			int maxLength = 30;
			InputFilter[] filterArray = new InputFilter[1];
			filterArray[0] = new InputFilter.LengthFilter(maxLength);
			editComment.setFilters(filterArray);
			editComment.getBackground().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
			editComment.setTextColor(ContextCompat.getColor(view.getContext(), R.color.text_color));
			llAttendeesContainer.addView(editComment);
		} else {
			lastView.setError("Feild can't blank");
		}
	}

	class MOMInputFieldTextListuner implements TextWatcher {
		TextInputLayout textInput;
		public MOMInputFieldTextListuner(TextInputLayout inputLayout){
			this.textInput = inputLayout;
		}


		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			switch (textInput.getId()) {
				case R.id.etMOMTile:
					if (charSequence.length()<3){
						textInput.setError("At least 3 character should be on title");
						textInput.setErrorEnabled(true);
					}else{
						textInput.setErrorEnabled(false);
					}

					break;

				case R.id.etMOMActionItem:

					if (charSequence.length()<3){
						textInput.setError("At least 3 character should be on Action Item");
						textInput.setErrorEnabled(true);
					}else{
						textInput.setErrorEnabled(false);
					}

					break;
				case R.id.etMOMDiscription:

					if (charSequence.length()<20){
						textInput.setError("At least 20 character should be on title");
						textInput.setErrorEnabled(true);
					}else{
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

	public void onUpdateMOM(View view) {
		if (isInputValid()) {

			JSONObject jObject;
			try {
				int countAttendees = llAttendeesContainer.getChildCount();
				JSONArray jArray = new JSONArray();
				for (int i = 0; i < countAttendees; i++) {
					View childView = llAttendeesContainer.getChildAt(i);
					if (childView instanceof EditText) {
						String text = ((EditText) childView).getText()
								.toString();
						if (!TextUtils.isEmpty(text)) {
							JSONObject o = new JSONObject();
							o.put("AttendeeName", text);
							jArray.put(o);
						}
					}
				}

				jObject = new JSONObject();
				jObject.put("MOMId", modal.momID);
				jObject.put("Attendees", jArray);
				jObject.put("MOMDate", etDate.getText().toString());
				try {
					long time = new SimpleDateFormat("dd-MMM-yyyy").parse(etDate.getText().toString()).getTime();
					jObject.put("MOMDateValue", time);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				jObject.put("Location", etLocation.getEditText().getText().toString());
				jObject.put("Description", etDiscription.getEditText().getText().toString());
				jObject.put("MOMTitle", etTitle.getEditText().getText().toString());
				jObject.put("ActionItem", etActionItem.getEditText().getText().toString());
				jObject.put("MOMIdServer", modal.momServerID);
				jObject.put("MOMIsDeleted", modal.momIsDeleted);
				jObject.put("MOMIsUpdated", true);

				jObject.put("UserID", Long.parseLong(Helper
						.getStringValuefromPrefs(this,
								SharedPreferencesKey.PREF_USERID)));

				// updateMOMService(jObject);

				DatabaseHelper.getConnection(getApplicationContext())
						.updateMOMData(jObject);
				setResult(Activity.RESULT_OK);
				finish();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	private boolean isInputValid() {

		if (TextUtils.isEmpty(etTitle.getEditText().getText().toString())) {
			etTitle.setError("Field can't blank");
			return false;
		}  else if (etTitle.getEditText().getText().toString().length() < 3) {
			etTitle.setError("At least 3 character should be on title");

			return false;
		}else if (TextUtils.isEmpty(etActionItem.getEditText().getText().toString())) {
			etActionItem.setError("Field can't blank");
			return false;
		}else if (etActionItem.getEditText().getText().toString().length() < 3) {
			etActionItem
					.setError("At least 3 character should be on meeting agenda");
			return false;
		} else if (TextUtils.isEmpty(etDiscription.getEditText().getText().toString())) {
			etDiscription.setError("Field can't blank");
			return false;
		} else if (etDiscription.getEditText().getText().toString().length() < 20) {
			etDiscription
					.setError("At least 20 character should be on description");
			return false;
		}  else {
			boolean flag = false;
			int countAttendees = llAttendeesContainer.getChildCount();
			for (int i = 0; i < countAttendees; i++) {
				View childView = llAttendeesContainer.getChildAt(i);
				if (childView instanceof EditText) {
					int lenght = ((EditText) childView).getText().toString()
							.length();
					if (lenght > 0) {
						flag = true;
						break;
					}

				}
			}
			if (!flag) {
				Toast.makeText(getApplicationContext(),
						"Please add at least one attendee", Toast.LENGTH_SHORT)
						.show();
			}
			return flag;
		}

	}

	public void onDeleteMOM(View view) {

		if (isInputValid()) {
			Helper.showAlertDialog(MOMDetailsActivity.this,
					SSCAlertDialog.WARNING_TYPE, "Delete",
					"Are you sure delete this MOM", "OK",
					new SSCAlertDialog.OnSDAlertDialogClickListener() {

						@Override
						public void onClick(SSCAlertDialog sdAlertDialog) {
							sdAlertDialog.dismiss();
							DatabaseHelper.getConnection(
									getApplicationContext()).deleteMOMData(
									new int[]{modal.momID});
							setResult(Activity.RESULT_OK);
							finish();

						}
					}, false, null, null);

		}

	}

}
