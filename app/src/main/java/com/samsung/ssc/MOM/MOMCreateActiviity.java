
package com.samsung.ssc.MOM;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseConstants.MOMAttendeesMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.MOMDetailsMasterColumns;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

public class MOMCreateActiviity extends BaseActivity {

    private TextInputLayout etLocation;
    private TextInputLayout etTitle;
    private EditText etDate;
    private TextInputLayout etActionItem;
    private TextInputLayout etDiscription;
    private int year;
    private int month;
    private int day;
    private LinearLayout llAttendeesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mom_creation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Create MOM");
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

    private void setUpView() {

        etDate = (EditText) this.findViewById(R.id.etMOMDate);
        etLocation = (TextInputLayout) this.findViewById(R.id.etMOMLocation);

        etTitle = (TextInputLayout) this.findViewById(R.id.etMOMTile);
        etTitle.getEditText().addTextChangedListener(new MOMInputFieldTextListuner(etTitle));
        etActionItem = (TextInputLayout) this.findViewById(R.id.etMOMActionItem);
        etActionItem.getEditText().addTextChangedListener(new MOMInputFieldTextListuner(etActionItem));
        etDiscription = (TextInputLayout) this.findViewById(R.id.etMOMDiscription);
        etDiscription.getEditText().addTextChangedListener(new MOMInputFieldTextListuner(etDiscription));
        llAttendeesContainer = (LinearLayout) this
                .findViewById(R.id.llMOMAttendeesContainer);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(calendar.getTime());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_mom_create, menu);

        return true;
    }

    class MOMInputFieldTextListuner implements TextWatcher {
        TextInputLayout textInput;

        public MOMInputFieldTextListuner(TextInputLayout inputLayout) {
            this.textInput = inputLayout;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (textInput.getId()) {
                case R.id.etMOMTile:
                    if (charSequence.length() < 3) {
                        textInput.setError("At least 3 character should be on title");
                        textInput.setErrorEnabled(true);
                    } else {
                        textInput.setErrorEnabled(false);
                    }

                    break;

                case R.id.etMOMActionItem:

                    if (charSequence.length() < 3) {
                        textInput.setError("At least 3 character should be on Action Item");
                        textInput.setErrorEnabled(true);
                    } else {
                        textInput.setErrorEnabled(false);
                    }

                    break;
                case R.id.etMOMDiscription:

                    if (charSequence.length() < 20) {
                        textInput.setError("At least 20 character should be on title");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_mom:
                onCreateMOM(null);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDataClick(View view) {

        DatePickerDialog dialog = new DatePickerDialog(this, myDateListener,
                year, month, day);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    public void onAddAttendeesClick(View view) {

        EditText lastView = (EditText) llAttendeesContainer
                .getChildAt(llAttendeesContainer.getChildCount() - 1);
        if (!TextUtils.isEmpty(lastView.getText())) {

            final EditText editComment = new EditText(this);
            int maxLength = 30;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            editComment.setFilters(FilterArray);
            editComment.requestFocus();
            editComment.setTextColor(ContextCompat.getColor(view.getContext(), R.color.text_color));
            editComment.getBackground().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editComment, InputMethodManager.SHOW_IMPLICIT);
            llAttendeesContainer.addView(editComment);
        } else {
            lastView.setError("Feild can't blank");
        }
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int arg1, int arg2,
                              int arg3) {

            getDateFromDatePicket(datePicker);

            showDate(getDateFromDatePicket(datePicker));
        }

    };

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

    public void onCreateMOM(View view) {

        try {
            if (isInputValue()) {

                try {
                    int countAttendees = llAttendeesContainer.getChildCount();
                    JSONArray jArrayAttendeesList = new JSONArray();
                    for (int i = 0; i < countAttendees; i++) {
                        View childView = llAttendeesContainer.getChildAt(i);
                        if (childView instanceof EditText) {
                            String text = ((EditText) childView).getText()
                                    .toString();
                            if (!TextUtils.isEmpty(text)) {
                                jArrayAttendeesList.put(text);
                            }
                        }
                    }

                    JSONObject jObject = new JSONObject();
                    jObject.put("Attendees", jArrayAttendeesList);
                    jObject.put("MOMDate", etDate.getText().toString());
                    try {
                        long time = new SimpleDateFormat("dd-MMM-yyyy").parse(
                                etDate.getText().toString()).getTime();
                        jObject.put("MOMDateValue", time);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    jObject.put("Location", etLocation.getEditText().getText().toString());
                    jObject.put("Description", etDiscription.getEditText().getText()
                            .toString());
                    jObject.put("MOMTitle", etTitle.getEditText().getText().toString());
                    jObject.put("ActionItem", etActionItem.getEditText().getText().toString());
                    jObject.put("UserID", Long.parseLong(Helper
                            .getStringValuefromPrefs(this,
                                    SharedPreferencesKey.PREF_USERID)));

                    ContentValues values = new ContentValues();
                    values.put(MOMDetailsMasterColumns.KEY_MOM_TITLE,
                            jObject.getString("MOMTitle"));
                    values.put(MOMDetailsMasterColumns.KEY_MOM_DATE_TEXT,
                            jObject.getString("MOMDate"));
                    values.put(MOMDetailsMasterColumns.KEY_MOM_DATE_VALUE,
                            jObject.getLong("MOMDateValue"));
                    values.put(MOMDetailsMasterColumns.KEY_MOM_DISCRIPETION,
                            jObject.getString("Description"));
                    values.put(MOMDetailsMasterColumns.KEY_MOM_ACTION_ITEM,
                            jObject.getString("ActionItem"));
                    values.put(MOMDetailsMasterColumns.KEY_MOM_LOCATION,
                            jObject.getString("Location"));
                    values.put(MOMDetailsMasterColumns.KEY_MOM_SERVER_ID, -1);
                    values.put(MOMDetailsMasterColumns.KEY_MOM_IS_DELETED, 0);
                    values.put(MOMDetailsMasterColumns.KEY_MOM_IS_UPDATED, 0);

                    Uri uri = getContentResolver().insert(
                            ProviderContract.URI_MOM_DETAILS, values);

                    long momID = ContentUris.parseId(uri);

                    JSONArray jArrayAttendees = jObject
                            .getJSONArray("Attendees");
                    int count = jArrayAttendees.length();
                    ContentValues[] attendeesListValues = new ContentValues[count];
                    for (int i = 0; i < count; i++) {
                        ContentValues v = new ContentValues();
                        v.put(MOMAttendeesMasterColumns.KEY_MOM_ID, momID);
                        v.put(MOMAttendeesMasterColumns.KEY_MOM_ATTENDEES_NAME,
                                jArrayAttendees.getString(i));
                        attendeesListValues[i] = v;
                    }

                    getContentResolver().bulkInsert(
                            ProviderContract.URI_MOM_ATTENDEES,
                            attendeesListValues);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private boolean isInputValue() {

        if (TextUtils.isEmpty(etTitle.getEditText().getText().toString())) {
            etTitle.setError("Field can't blank");

            return false;
        } else if (etTitle.getEditText().getText().toString().length() < 3) {
            etTitle.setError("At least 3 character should be on title");

            return false;
        } else if (TextUtils.isEmpty(etActionItem.getEditText().getText().toString())) {
            etActionItem.setError("Field can't blank");

            return false;
        } else if (etActionItem.getEditText().getText().toString().length() < 3) {
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
        } else {
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

}
