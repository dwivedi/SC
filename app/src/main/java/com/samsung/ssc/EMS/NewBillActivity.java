package com.samsung.ssc.EMS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.FileDirectory;
import com.samsung.ssc.dto.EMSBillDetail;
import com.samsung.ssc.dto.EMSBillDocumentDetail;
import com.samsung.ssc.util.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewBillActivity extends BaseActivity implements OnClickListener {
    private final int REQ_CODE_PICK_IMAGE = 100;
    private final int REQUEST_CODE_CAMERA_BILL_DOCUMENT = 101;

    private final int NON_APPROVED_TABS = -1;
    private final int EXPENSE_NO_STATUS = -100;

    private TextView mDateValueTextview;
    private TextView mBillNumberTextview;
    private EditText mDescriptionEditText;
    private EditText mAmountEditText;
    private LinearLayout mDocumentsLayout;

  //  private TextView mBillTitleTextview;
    private ImageView submitBillBtn;
    private PercentRelativeLayout updateBillLayout;

    private ImageView selectDateImageview;
    private TextView uploadDocumentTextview;

    EMSBillDetail mBillDetail;

    Uri mCameraImageUri;

    String mBillDocumentFileName;
    String mBillDocumentFilePath;


    int documentCount = 0;
    int miExpenseStatus = EXPENSE_NO_STATUS;
    boolean mbIsPendingTabExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle(R.string.new_bill);
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
        initializeBillDetail();

    }


    private void setUpView() {
       // mBillTitleTextview = (TextView) findViewById(R.id.bill_title_textview);
        mDateValueTextview = (TextView) findViewById(R.id.date_value_textview);
        mBillNumberTextview = (TextView) findViewById(R.id.bill_number_editext);
        mDescriptionEditText = (EditText) findViewById(R.id.description_edittext);

        mAmountEditText = (EditText) findViewById(R.id.amount_editetxt);
        //	mAmountEditText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,2)});

        mDocumentsLayout = (LinearLayout) findViewById(R.id.documents_layout);

        submitBillBtn = (ImageView) findViewById(R.id.submit_bill_btn);
        updateBillLayout = (PercentRelativeLayout) findViewById(R.id.update_bill_layout);

        selectDateImageview = (ImageView) findViewById(R.id.select_date_imageview);
        uploadDocumentTextview = (TextView) findViewById(R.id.upload_documents_textview);

        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.select_date_imageview).setOnClickListener(this);
        findViewById(R.id.upload_documents_textview).setOnClickListener(this);
        findViewById(R.id.submit_bill_btn).setOnClickListener(this);

        findViewById(R.id.update_bill_btn).setOnClickListener(this);
        findViewById(R.id.delete_bill_btn).setOnClickListener(this);

    }


    private void initializeBillDetail() {
        if (getIntent() != null && getIntent().getExtras() != null) {
         //   mBillTitleTextview.setText(getString(R.string.bill_detail));

            getSupportActionBar().setTitle(getString(R.string.bill_detail));

            Bundle bundle = getIntent().getExtras();

            mBillDetail = (EMSBillDetail) bundle.getSerializable("EMSBillDetail");

            int expenseTabID = bundle.getInt("ExpenseTabId");

            handleUIBasedOnExpenseTab(expenseTabID);

            miExpenseStatus = bundle.getInt("ExpenseStatus");

            if (miExpenseStatus == EMSConstants.EXPENSE_STATUS_CANCELLED || miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVED || miExpenseStatus == EMSConstants.EXPENSE_STATUS_REJECT || miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVAL_NOT_REQUIRED)
                disableUIInteractions();

            if (expenseTabID == EMSConstants.EXPENSE_TAB_PENDING)
                mbIsPendingTabExpense = true;


            mDateValueTextview.setText(Helper.longDateToString(mBillDetail.mBillDate));

            mBillNumberTextview.setText(mBillDetail.mBillNo + "");

            mDescriptionEditText.setText(mBillDetail.mDescription);

			/*DecimalFormat format = new DecimalFormat("####.##");
            String billAmountFormatted = format.format(mBillDetail.mAmount+"");
			mAmountEditText.setText(billAmountFormatted);*/

            mAmountEditText.setText(mBillDetail.mAmount + "");

            if (mBillDetail.mEMSBillDocumentDetailList != null && mBillDetail.mEMSBillDocumentDetailList.size() > 0) {

                for (int i = 0; i < mBillDetail.mEMSBillDocumentDetailList.size(); i++) {
                    EMSBillDocumentDetail emsBillDocumentDetail = mBillDetail.mEMSBillDocumentDetailList.get(i);

                    if (emsBillDocumentDetail.mIsDeleted == false)
                        addBillDocument(emsBillDocumentDetail, expenseTabID);
                }
            }

        } else {

            //mBillTitleTextview.setText(getString(R.string.new_bill));
            getSupportActionBar().setTitle(getString(R.string.new_bill));
            mBillDetail = new EMSBillDetail();

            updateBillLayout.setVisibility(View.GONE);
            submitBillBtn.setVisibility(View.VISIBLE);
        }


    }

	/*public class DecimalDigitsInputFilter implements InputFilter {

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
			//mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
			mPattern=Pattern.compile("^\\d{0,4}(\\.\\d{1,2})?$");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

			Matcher matcher=mPattern.matcher(dest);
			if(!matcher.matches())
				return "";
			return null;
		}

	}*/


    void disableUIInteractions() {
        updateBillLayout.setVisibility(View.GONE);
        submitBillBtn.setVisibility(View.GONE);

        selectDateImageview.setEnabled(false);
        mBillNumberTextview.setEnabled(false);
        mDescriptionEditText.setEnabled(false);
        mAmountEditText.setEnabled(false);
        uploadDocumentTextview.setEnabled(false);
    }

    void handleUIBasedOnExpenseTab(int inExpenseTabId) {

        if (inExpenseTabId == EMSConstants.EXPENSE_TAB_APPROVED || inExpenseTabId == EMSConstants.EXPENSE_TAB_NOTIFICATIONS) {

            disableUIInteractions();
        } else {
            updateBillLayout.setVisibility(View.VISIBLE);
            submitBillBtn.setVisibility(View.GONE);
        }


    }


    void displayExpenseDateSelectionDialog() {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                if (!isGivenDateIsGreaterThanCurrentDate(year, monthOfYear, dayOfMonth) && !isBillMoreThanOneMonthOld(year, monthOfYear, dayOfMonth))

                    mDateValueTextview.setText(formatDate(year, monthOfYear, dayOfMonth));
                else {
                    if (isGivenDateIsGreaterThanCurrentDate(year, monthOfYear, dayOfMonth))
                        showAlertDialog("Please select Bill Date less than Current Date.");
                    else if (isBillMoreThanOneMonthOld(year, monthOfYear, dayOfMonth))
                        showAlertDialog("Bill Date is more than 1 month Old.");

                }

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        datePickerDialog.show();

    }


    private String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(date);
    }


    private void addBillDocument(EMSBillDocumentDetail emsBillDocumentDetail, int expenseTabID) {
        View billDocumentParentView = getLayoutInflater().inflate(R.layout.layout_bill_document_item, null);

        ((TextView) billDocumentParentView.findViewById(R.id.document_name_textview)).setText(emsBillDocumentDetail.mDocumentName);

        billDocumentParentView.setTag(emsBillDocumentDetail);

        billDocumentParentView.findViewById(R.id.delete_document_imageview).setTag(billDocumentParentView);

        billDocumentParentView.findViewById(R.id.delete_document_imageview).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {


                showDocumentDeleteAlertDialog("Delete Document", "Do you want to delete document?", (View) view.getTag());
            }
        });


        if (expenseTabID == EMSConstants.EXPENSE_TAB_APPROVED || expenseTabID == EMSConstants.EXPENSE_TAB_NOTIFICATIONS || miExpenseStatus == EMSConstants.EXPENSE_STATUS_CANCELLED || miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVED || miExpenseStatus == EMSConstants.EXPENSE_STATUS_REJECT || miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVAL_NOT_REQUIRED)
            billDocumentParentView.findViewById(R.id.delete_document_imageview).setEnabled(false);

        billDocumentParentView.findViewById(R.id.document_imageview).setTag(emsBillDocumentDetail);

        billDocumentParentView.findViewById(R.id.document_imageview).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                displayBillDocumentDialog((EMSBillDocumentDetail) view.getTag());
            }
        });


        mDocumentsLayout.addView(billDocumentParentView);
    }

    private void displayBillDocumentDialog(EMSBillDocumentDetail emsBillDocumentDetail) {

        try {

            FragmentTransaction billDocumentFragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment previosbillingAndProductDetailsFragment = getSupportFragmentManager().findFragmentByTag("bill_document_dialog");
            if (previosbillingAndProductDetailsFragment != null) {
                billDocumentFragmentTransaction.remove(previosbillingAndProductDetailsFragment);
            }

            billDocumentFragmentTransaction.addToBackStack(null);

            Bundle billDocumentBundle = new Bundle();
            billDocumentBundle.putSerializable("bill_document", emsBillDocumentDetail);

            ExpenseBillDocumentFragment billDocumentFragment = new ExpenseBillDocumentFragment();
            billDocumentFragment.setArguments(billDocumentBundle);
            billDocumentFragment.show(billDocumentFragmentTransaction, "bill_document_dialog");

        } catch (IllegalStateException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

    }


    String getFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Bill_Document_" + timeStamp + ".jpg";
        return imageFileName;

    }

    void createBillDocument(String filename, String expenseDocumentOutputFilePath) {
        EMSBillDocumentDetail emsBillDocumentDetail = new EMSBillDocumentDetail();

        emsBillDocumentDetail.mDocumentName = filename;


        emsBillDocumentDetail.mDocumentFilePath = expenseDocumentOutputFilePath;
        emsBillDocumentDetail.mIsActive = true;

        //emsBillDocumentDetail.miCreatedBy= Integer.parseInt(Helper.getStringValuefromPrefs(getApplicationContext(),SharedPreferencesKey.PREF_USERID));

        mBillDetail.addDocument(emsBillDocumentDetail);

        documentCount++;

        addBillDocument(emsBillDocumentDetail, NON_APPROVED_TABS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {

            case REQ_CODE_PICK_IMAGE:

                if (resultCode == RESULT_OK) {

                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    String filename = getFileName(); //filePath.substring(filePath.lastIndexOf("/")+1);

                    String expenseDocumentOutputFilePath = writeExpenseDocumentInSDcard(filename, filePath);

                    createBillDocument(filename, expenseDocumentOutputFilePath);
	         
	          /*  EMSBillDocumentDetail emsBillDocumentDetail = new EMSBillDocumentDetail();
	                        
	            emsBillDocumentDetail.mDocumentName = filename;
	            
	            String expenseDocumentOutputFilePath = writeExpenseDocumentInSDcard(filename, filePath);
	            
	            emsBillDocumentDetail.mDocumentFilePath = expenseDocumentOutputFilePath;
	            emsBillDocumentDetail.mIsActive = true;
	            
	            //emsBillDocumentDetail.miCreatedBy= Integer.parseInt(Helper.getStringValuefromPrefs(getApplicationContext(),SharedPreferencesKey.PREF_USERID));
	            
	            mBillDetail.addDocument(emsBillDocumentDetail);
	            
	            documentCount++;
	            
	            addBillDocument(emsBillDocumentDetail);*/

                }

                break;


            case REQUEST_CODE_CAMERA_BILL_DOCUMENT:

                if (resultCode == RESULT_OK) {

                    String filename = mBillDocumentFileName;
                    String expenseDocumentOutputFilePath = mBillDocumentFilePath;

                    createBillDocument(filename, expenseDocumentOutputFilePath);

                    mBillDocumentFileName = "";
                    mBillDocumentFilePath = "";

                }


                break;
        }

    }


    private String writeExpenseDocumentInSDcard(String expenseDocumentFileName, String inputFilePath) {


        //	expenseDocumentFileName = "bill_document.png";

        File expenseBillDocumentFolder = new File(FileDirectory.DIRECTORY_EXPENSE_BILL_DOCUMENTS);

        if (!expenseBillDocumentFolder.exists()) {
            expenseBillDocumentFolder.mkdirs();
        }

        String expenseDocumentOutputFilePath = FileDirectory.DIRECTORY_EXPENSE_BILL_DOCUMENTS + "/" + expenseDocumentFileName;

        File expenseDocumentOutputFile = new File(expenseDocumentOutputFilePath);

        if (!expenseDocumentOutputFile.exists()) {
            try {
                expenseDocumentOutputFile.createNewFile();
            } catch (IOException e) {
                Helper.printStackTrace(e);
            }
        } else {
        }


        File f = new File(inputFilePath);
        InputStream in = null;
        OutputStream out = null;

        try {
			
			
			/*
			fis = new FileInputStream(f);
			fos = new FileOutputStream(expenseDocumentOutputFile);
			while (true) {
				int i = fis.read();
				if (i != -1) {
					fos.write(i);
				} else {
					break;
				}
			}
			
			
		
			fos.flush();*/


            in = new FileInputStream(f);
            out = new FileOutputStream(expenseDocumentOutputFile);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();


            //	Toast.makeText(this, "DB dump OK", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(this, "DB dump ERROR", Toast.LENGTH_LONG).show();

        } finally {
            try {
                if (in != null)
                    in.close();

                if (out != null)
                    out.close();
            } catch (IOException ioe) {
            }
        }

        return expenseDocumentOutputFilePath;
    }

    void showDocumentDeleteAlertDialog(String title, final String message, final View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setMessage(message);
        adb.setIcon(android.R.drawable.ic_dialog_alert);

        adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                EMSBillDocumentDetail emsBillDocumentDetail = (EMSBillDocumentDetail) view.getTag();

                mDocumentsLayout.removeView(view);
                mBillDetail.softDeleteDocument(emsBillDocumentDetail);

                dialog.dismiss();
    	        
    	        	/*mDocumentsLayout.removeViewAt(documentPosition);
    	        	mBillDetail.removeDocument(documentPosition);*/

            }
        });


        adb.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.show();
    }


    private void showAlertDialog(final String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });


        alertDialogBuilder.show();
    }

    // launch camera also dialog
    private void captureBillDocumentFromPhotoGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);
    }

    private void captureBillDocumentByCamera() {

        try {

            mBillDocumentFileName = getFileName();

            File expenseBillDocumentFolder = new File(FileDirectory.DIRECTORY_EXPENSE_BILL_DOCUMENTS);

            mBillDocumentFilePath = FileDirectory.DIRECTORY_EXPENSE_BILL_DOCUMENTS + "/" + mBillDocumentFileName;

            if (!expenseBillDocumentFolder.exists()) {
                expenseBillDocumentFolder.mkdirs();
            }

            File expenseDocumentOutputFile = new File(mBillDocumentFilePath);

            if (!expenseDocumentOutputFile.exists()) {
                expenseDocumentOutputFile.createNewFile();
            }

            Uri mImageUri = Uri.fromFile(expenseDocumentOutputFile);

            Intent cameraActivityIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraActivityIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);


            if (cameraActivityIntent.resolveActivity(getPackageManager()) != null) {

                startActivityForResult(cameraActivityIntent, REQUEST_CODE_CAMERA_BILL_DOCUMENT);
            }

        } catch (IOException e) {

            Helper.showCustomToast(getApplicationContext(),
                    "Unable to capture Bill Document.", Toast.LENGTH_LONG);
        }

    }

    private void showChooseBillDocumentSourceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(getString(R.string.bill_document_pick_msg));
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialogBuilder.setPositiveButton(getString(R.string.bill_document_gallery), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                captureBillDocumentFromPhotoGallery();
                dialog.dismiss();

            }
        });

        alertDialogBuilder.setNegativeButton(getString(R.string.bill_document_camera), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                captureBillDocumentByCamera();
                dialog.dismiss();

            }
        });


        alertDialogBuilder.show();

        //	captureBillDocumentFromPhotoGallery();
    }


    void submitBill(boolean inbISDeleted, boolean inbISActive) {
        if (TextUtils.isEmpty(mDateValueTextview.getText())) {

            showAlertDialog("Please select Date.");
            return;
        }

        if (TextUtils.isEmpty(mBillNumberTextview.getText())) {

            showAlertDialog("Bill Number is empty.");
            return;
        }

        if (TextUtils.isEmpty(mDescriptionEditText.getText())) {
            showAlertDialog("Description is empty.");
            return;
        }

        if (TextUtils.isEmpty(mAmountEditText.getText())) {
            showAlertDialog("Amount is empty.");
            return;
        }

        if (mBillDetail.getDocumentCount() == 0 && !mbIsPendingTabExpense) {

            showAlertDialog("Please provide Bill Document.");

            return;
        }

        mBillDetail.mBillDate = Helper.dateStringToLong(mDateValueTextview.getText().toString());
        mBillDetail.mBillNo = mBillNumberTextview.getText().toString();
        mBillDetail.mDescription = mDescriptionEditText.getText().toString();
        mBillDetail.mAmount = Double.parseDouble(mAmountEditText.getText().toString());
        //mBillDetail.miCreatedBy =  Integer.parseInt(Helper.getStringValuefromPrefs(getApplicationContext(),SharedPreferencesKey.PREF_USERID));

        mBillDetail.mIsActive = inbISActive;
        mBillDetail.mIsDeleted = inbISDeleted;

        if (mBillDetail.mIsDeleted) {
            if (mBillDetail.mEMSBillDocumentDetailList != null && mBillDetail.mEMSBillDocumentDetailList.size() > 0) {

                for (int i = 0; i < mBillDetail.mEMSBillDocumentDetailList.size(); i++) {
                    mBillDetail.mEMSBillDocumentDetailList.get(i).mIsDeleted = true;
                    mBillDetail.mEMSBillDocumentDetailList.get(i).mIsActive = false;


                }
            }

        }

        Intent data = new Intent();
        data.putExtra("BillDetail", mBillDetail);
        setResult(Activity.RESULT_OK, data);
        finish();
    }


    @Override
    public void onClick(View view) {

        int viewID = view.getId();

        switch (viewID) {

            case R.id.back_btn:

                finish();

                break;

            case R.id.select_date_imageview:

                displayExpenseDateSelectionDialog();

                break;

            case R.id.upload_documents_textview:

                showChooseBillDocumentSourceAlertDialog();

                break;

            case R.id.submit_bill_btn:

                submitBill(false, true);

                break;

            case R.id.update_bill_btn:

                submitBill(false, true);

                break;


            case R.id.delete_bill_btn:

                submitBill(true, false);

                break;

            default:
                break;

        }
    }


    private boolean isGivenDateIsGreaterThanCurrentDate(int year, int month, int day) {
        boolean isGreaterDate = true;

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);


        if (year > currentYear)
            return isGreaterDate;

        if (year == currentYear && month > currentMonth)
            return isGreaterDate;

        if ((year == currentYear && month == currentMonth && day > currentDay))
            return isGreaterDate;

        isGreaterDate = false;

        return isGreaterDate;

    }

    boolean isBillMoreThanOneMonthOld(int year, int month, int day) {
        boolean isMoreThanOneMonthOld = false;

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.DAY_OF_MONTH, day);
        selectedDate.set(Calendar.MONTH, month); // 0-11 so 1 less
        selectedDate.set(Calendar.YEAR, year);

        Calendar today = Calendar.getInstance();

        long dayTimeMilliSecondDifference = today.getTimeInMillis() - selectedDate.getTimeInMillis();

        long days = dayTimeMilliSecondDifference / (24 * 60 * 60 * 1000);

        if (days > 30)
            isMoreThanOneMonthOld = true;

        return isMoreThanOneMonthOld;

    }


}
