package com.samsung.ssc.EMS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.percent.PercentFrameLayout.LayoutParams;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.database.DatabaseConstants.EMSBillDetailTableColumns;
import com.samsung.ssc.database.DatabaseConstants.EMSBillDocumentDetailTableColumns;
import com.samsung.ssc.database.DatabaseConstants.EMSExpenseDetailTableColumns;
import com.samsung.ssc.database.DatabaseConstants.TableNames;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.dto.EMSBillDetail;
import com.samsung.ssc.dto.EMSBillDocumentDetail;
import com.samsung.ssc.dto.EMSExpenseDetail;
import com.samsung.ssc.dto.EMSExpenseType;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.RecyclerViewLinearLayoutParams;

import java.util.ArrayList;

public class NewExpenseActivity extends BaseActivity implements OnClickListener, LoaderCallbacks<Cursor> {

    private final int NEW_BILL_REQUEST_CODE = 0;
    private final int LOADER_ID_EXPENSE_TYPES = 1;
    private ArrayList<EMSExpenseType> mExpenseTypeList;

   // private TextView mExpenseTitleTextview;


    private TextView mExpenseTypeValueTextview;
    private EMSExpenseType mSelectedExpenseType;

    private EMSExpenseDetail mEMSExpenseDetail;

    private LinearLayout mExpenseItemListLayout;
    private LinearLayout mBillableToLayout;

    private EditText mBillableToEditText;
    private EditText mCommentsEditText;

    private int miSelectedBillableID;

    private PercentRelativeLayout updateExpenseLayout;
    private PercentRelativeLayout approvalExpenseLayout;
    private ImageView submitexpenseBtn;

    boolean mbISUpdateExpense;
    int miBillUpdateRowNumber;

    int miBillDocumentCount;

    private View mUserActionBillayout;

    private TextView addBillTextview;

    private ImageView selectExpenseTypeImageview;
    private RadioButton billableYesRadioButton;
    private RadioButton billableNoRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle(getString(R.string.new_expense));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        getSupportLoaderManager().initLoader(LOADER_ID_EXPENSE_TYPES, null, this);

        setUpView();
        initializeExpenseDetail();
    }

    private void setUpView() {
       // mExpenseTitleTextview = (TextView) findViewById(R.id.expense_title_textview);

        mExpenseTypeValueTextview = (TextView) findViewById(R.id.expense_value_textview);
        mExpenseItemListLayout = (LinearLayout) findViewById(R.id.expense_item_list_layout);
        mBillableToLayout = (LinearLayout) findViewById(R.id.billable_to_layout);
        mBillableToEditText = (EditText) findViewById(R.id.billable_to_edit_text);
        mCommentsEditText = (EditText) findViewById(R.id.comments_edit_text);
        submitexpenseBtn = (ImageView) findViewById(R.id.submit_expense_btn);

        updateExpenseLayout = (PercentRelativeLayout) findViewById(R.id.update_expense_layout);
        approvalExpenseLayout = (PercentRelativeLayout) findViewById(R.id.approval_expense_layout);


        selectExpenseTypeImageview = (ImageView) findViewById(R.id.select_expense_type_imageview);
        addBillTextview = (TextView) findViewById(R.id.add_bill_textview);
        billableYesRadioButton = (RadioButton) findViewById(R.id.billable_yes_radio_button);
        billableNoRadioButton = (RadioButton) findViewById(R.id.billable_no_radio_button);

        RadioGroup billableRadioGroup = (RadioGroup) findViewById(R.id.billable_radioGroup);
        billableRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.billable_yes_radio_button) {
                    miSelectedBillableID = R.id.billable_yes_radio_button;
                    mBillableToLayout.setVisibility(View.VISIBLE);
                } else {
                    miSelectedBillableID = R.id.billable_no_radio_button;
                    mBillableToEditText.setText("");
                    mBillableToLayout.setVisibility(View.GONE);
                }

            }
        });

         findViewById(R.id.add_bill_textview).setOnClickListener(this);
        findViewById(R.id.select_expense_type_imageview).setOnClickListener(this);

        findViewById(R.id.submit_expense_btn).setOnClickListener(this);
        findViewById(R.id.update_expense_btn).setOnClickListener(this);
        findViewById(R.id.cancel_expense_btn).setOnClickListener(this);

        findViewById(R.id.approve_expense_btn).setOnClickListener(this);
        findViewById(R.id.reject_expense_btn).setOnClickListener(this);

    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    private void initializeExpenseDetail() {
        if (getIntent() != null && getIntent().getExtras() != null) {

            //mExpenseTitleTextview.setText(getString(R.string.expense_detail));
            getSupportActionBar().setTitle(getString(R.string.expense_detail));


            mEMSExpenseDetail = (EMSExpenseDetail) getIntent().getExtras().getSerializable("EMSExpenseDetail");

            handleUIBasedOnExpenseTab();

            if (mEMSExpenseDetail.miEMSExpenseDetailIDServer != 0)
                selectExpenseTypeImageview.setEnabled(false);

            if (mEMSExpenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_CANCELLED || mEMSExpenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVED || mEMSExpenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_REJECT || mEMSExpenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_APPROVAL_NOT_REQUIRED)
                disableUIInteractions();

            submitexpenseBtn.setVisibility(View.GONE);


            mExpenseTypeValueTextview.setText(DatabaseHelper.getConnection(getApplicationContext()).getExpenseTypeValue(mEMSExpenseDetail.miEMSExpenseTypeMasterID));

            if (mEMSExpenseDetail.mBillableTo != null && !TextUtils.isEmpty(mEMSExpenseDetail.mBillableTo)) {
                mBillableToEditText.setText(mEMSExpenseDetail.mBillableTo);

                ((RadioButton) findViewById(R.id.billable_yes_radio_button)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.billable_no_radio_button)).setChecked(true);
            }

            mCommentsEditText.setText(mEMSExpenseDetail.mComment);

            if (mEMSExpenseDetail.mEMSBillDetailList != null && mEMSExpenseDetail.mEMSBillDetailList.size() > 0) {

                for (int i = 0; i < mEMSExpenseDetail.mEMSBillDetailList.size(); i++) {
                    EMSBillDetail emsBillDetail = mEMSExpenseDetail.mEMSBillDetailList.get(i);

                    if (emsBillDetail.mIsDeleted == false)
                        addBill(emsBillDetail, i);
                }
            }

        } else {
            //mExpenseTitleTextview.setText(getString(R.string.new_expense));
            getSupportActionBar().setTitle(getString(R.string.new_expense));

            mEMSExpenseDetail = new EMSExpenseDetail();
            updateExpenseLayout.setVisibility(View.GONE);
            approvalExpenseLayout.setVisibility(View.GONE);
            submitexpenseBtn.setVisibility(View.VISIBLE);
        }


    }


    void disableUIInteractions() {
        approvalExpenseLayout.setVisibility(View.GONE);
        updateExpenseLayout.setVisibility(View.GONE);

        selectExpenseTypeImageview.setEnabled(false);
        addBillTextview.setEnabled(false);
        billableYesRadioButton.setEnabled(false);
        billableNoRadioButton.setEnabled(false);
        mCommentsEditText.setEnabled(false);
        mBillableToEditText.setEnabled(false);
    }

    void handleUIBasedOnExpenseTab() {
        if (mEMSExpenseDetail.miEMSExpenseDetailITabID == EMSConstants.EXPENSE_TAB_PENDING) {
            approvalExpenseLayout.setVisibility(View.VISIBLE);
            updateExpenseLayout.setVisibility(View.GONE);
        } else if (mEMSExpenseDetail.miEMSExpenseDetailITabID == EMSConstants.EXPENSE_TAB_APPROVED || mEMSExpenseDetail.miEMSExpenseDetailITabID == EMSConstants.EXPENSE_TAB_NOTIFICATIONS) {

            disableUIInteractions();
        } else {
            approvalExpenseLayout.setVisibility(View.GONE);
            updateExpenseLayout.setVisibility(View.VISIBLE);
        }


    }

    private void displayExpenseTypesDialog() {
        if (mExpenseTypeList != null && mExpenseTypeList.size() > 0) {

            Dialog expenseTypesDialog = new Dialog(this);
            expenseTypesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = expenseTypesDialog.getWindow();
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            View expenseTypesParentView = getLayoutInflater().inflate(R.layout.layout_expense_dialog, null);

            RecyclerView expenseTypeRecyclerview = (RecyclerView) expenseTypesParentView.findViewById(R.id.expense_type_list_recycler_view);

            //use this setting to improve performance if you know that changes  in content do not change the layout size of the RecyclerView
            expenseTypeRecyclerview.setHasFixedSize(true);

            // use a linear layout manager
            RecyclerViewLinearLayoutParams mLayoutManager = new RecyclerViewLinearLayoutParams(this, RecyclerViewLinearLayoutParams.VERTICAL, false);

            expenseTypeRecyclerview.setLayoutManager(mLayoutManager);

            ExpenseTypeDialogListAdapter clad = new ExpenseTypeDialogListAdapter(this, expenseTypesDialog, mExpenseTypeList);
            expenseTypeRecyclerview.setAdapter(clad);
            expenseTypesDialog.setContentView(expenseTypesParentView);
            expenseTypesDialog.show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {

        Loader<Cursor> loader = null;

        switch (loaderID) {

            case LOADER_ID_EXPENSE_TYPES:

                loader = new CursorLoader(this, ProviderContract.URI_EXPENSE_TYPE_MASTER, null, null, null, null);

                break;

            default:
                break;
        }


        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        switch (loader.getId()) {
            case LOADER_ID_EXPENSE_TYPES:

                if (cursor != null && cursor.moveToFirst()) {

                    mExpenseTypeList = DatabaseUtilMethods.getExpenseTypesFromCursor(cursor);

                }

                break;


            default:
                break;
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub

    }

    public void setExpenseType(EMSExpenseType expenseTypeDTO) {

        mEMSExpenseDetail.miEMSExpenseTypeMasterID = expenseTypeDTO.miExpenseTypeMasterId;
        mExpenseTypeValueTextview.setText(expenseTypeDTO.mName);
    }


    private void showAlertDialog(final Activity activityContext, final String message) {
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


    private void submitExpense() {
        try {

            // mEMSExpenseDetail.miEMSExpenseTypeMasterID = mSelectedExpenseType.miCode;

            if (miSelectedBillableID == R.id.billable_yes_radio_button) {
                mEMSExpenseDetail.mbBillable = true;
                mEMSExpenseDetail.mBillableTo = mBillableToEditText.getText().toString();
            } else {
                mEMSExpenseDetail.mBillableTo = "";
                mEMSExpenseDetail.mbBillable = false;
            }

            mEMSExpenseDetail.mComment = mCommentsEditText.getText().toString();

            ContentValues emsExpenseDetailValues = new ContentValues();

            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_TYPE_MASTER_ID, mEMSExpenseDetail.miEMSExpenseTypeMasterID);

            if (mEMSExpenseDetail.mbBillable == true)
                emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE, 1);
            else
                emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE, 0);


            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE_TO, mEMSExpenseDetail.mBillableTo);
            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_COMMENT, mEMSExpenseDetail.mComment);
            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_STATUS, EMSConstants.EXPENSE_STATUS_SUBMITTED);
            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER, 0);


            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_DELETED, 0);
            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_EXPENSE_UPDATED, 0);
            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_ACTIVE, 1);

            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE, System.currentTimeMillis());

            emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID, EMSConstants.EXPENSE_TAB_SUBMITTED);

            Uri uri = getContentResolver().insert(ProviderContract.URI_EMS_EXPENSE_DETAIL, emsExpenseDetailValues);

            long emsExpenseDetailID = ContentUris.parseId(uri);

            if (mEMSExpenseDetail.mEMSBillDetailList != null && mEMSExpenseDetail.mEMSBillDetailList.size() > 0) {

                int emsbillDetailcount = mEMSExpenseDetail.mEMSBillDetailList.size();

                for (int i = 0; i < emsbillDetailcount; i++) {

                    EMSBillDetail emsBillDetail = mEMSExpenseDetail.mEMSBillDetailList.get(i);

                    ContentValues emsBillDetailValues = new ContentValues();

                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER, 0);
                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID, emsExpenseDetailID);
                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_BILL_DATE, emsBillDetail.mBillDate);
                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_BILL_NO, emsBillDetail.mBillNo);
                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_DESCRIPTION, emsBillDetail.mDescription);
                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_AMOUNT, emsBillDetail.mAmount);
                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_IS_ACTIVE, 1);
                    emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_IS_DELETED, 0);


                    Uri billURI = getContentResolver().insert(ProviderContract.URI_EMS_BILL_DETAIL, emsBillDetailValues);

                    long emsBillDetailID = ContentUris.parseId(billURI);


                    if (emsBillDetail.mEMSBillDocumentDetailList != null && emsBillDetail.mEMSBillDocumentDetailList.size() > 0) {
                        int emBillDocumentDetailcount = emsBillDetail.mEMSBillDocumentDetailList.size();

                        ContentValues[] emsBillDocumentListValues = new ContentValues[emBillDocumentDetailcount];

                        for (int j = 0; j < emBillDocumentDetailcount; j++) {
                            EMSBillDocumentDetail emsBillDocumentDetail = emsBillDetail.mEMSBillDocumentDetailList.get(j);

                            ContentValues emsBillDocumentValues = new ContentValues();

                            emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER, 0);

                            emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID, emsBillDetailID);
                            emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_NAME, emsBillDocumentDetail.mDocumentName);

                            emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_PATH, emsBillDocumentDetail.mDocumentFilePath);

                            emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_IS_ACTIVE, 1);
                            emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_IS_DELETED, 0);
                            emsBillDocumentListValues[j] = emsBillDocumentValues;
                        }

                        getContentResolver().bulkInsert(
                                ProviderContract.URI_EMS_BILL_DOCUMENT_DETAIL,
                                emsBillDocumentListValues);
                    }

                }

            }


        } catch (Exception e) {

        } finally {

            finish();
        }

    }


    private void updateExpense() {

        SQLiteDatabase db = null;

        try {

            if (miSelectedBillableID == R.id.billable_yes_radio_button) {
                mEMSExpenseDetail.mbBillable = true;
                mEMSExpenseDetail.mBillableTo = mBillableToEditText.getText().toString();
            } else {
                mEMSExpenseDetail.mBillableTo = "";
                mEMSExpenseDetail.mbBillable = false;
            }

            mEMSExpenseDetail.mComment = mCommentsEditText.getText().toString();


            db = DatabaseHelper.getConnection(getApplicationContext()).getWritableDatabase();

            synchronized (db) {

                db.delete(TableNames.TABLE_EMS_EXPENSE_DETAIL, EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + " = ?", new String[]{String.valueOf(mEMSExpenseDetail.miEMSExpenseDetailID)});

                ArrayList<Integer> billDetailsIDList = DatabaseHelper.getConnection(getApplicationContext()).getAllBillDetailsID(mEMSExpenseDetail.miEMSExpenseDetailID);

                db.delete(TableNames.TABLE_EMS_BILL_DETAIL, EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID + " = ?", new String[]{String.valueOf(mEMSExpenseDetail.miEMSExpenseDetailID)});

                for (int i = 0; i < billDetailsIDList.size(); i++) {
                    int billDetailsID = billDetailsIDList.get(i);
                    db.delete(TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL, EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID + " = ?", new String[]{String.valueOf(billDetailsID)});
                }

                if (mEMSExpenseDetail.miExpenseStatus == EMSConstants.EXPENSE_STATUS_CANCELLED && mEMSExpenseDetail.miEMSExpenseDetailIDServer == 0) {
                    // Do nothing
                } else {

                    ContentValues emsExpenseDetailValues = new ContentValues();

                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID, mEMSExpenseDetail.miEMSExpenseDetailID);
                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_TYPE_MASTER_ID, mEMSExpenseDetail.miEMSExpenseTypeMasterID);

                    if (mEMSExpenseDetail.mbBillable == true)
                        emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE, 1);
                    else
                        emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE, 0);


                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_BILLABLE_TO, mEMSExpenseDetail.mBillableTo);
                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_COMMENT, mEMSExpenseDetail.mComment);
                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_STATUS, mEMSExpenseDetail.miExpenseStatus);


                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMSEXPENSE_DETAIL_ID_SERVER, mEMSExpenseDetail.miEMSExpenseDetailIDServer);
                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_DELETED, mEMSExpenseDetail.mbIsDeleted);
                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_EXPENSE_UPDATED, 1);
                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_IS_ACTIVE, mEMSExpenseDetail.mbIsActive);

                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_SUBMITTED_DATE, mEMSExpenseDetail.mlSubmittedDate);

                    emsExpenseDetailValues.put(EMSExpenseDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_TAB_ID, mEMSExpenseDetail.miEMSExpenseDetailITabID);


                    long emsExpenseDetailID = db.insert(TableNames.TABLE_EMS_EXPENSE_DETAIL, null,
                            emsExpenseDetailValues);


                    if (mEMSExpenseDetail.mEMSBillDetailList != null && mEMSExpenseDetail.mEMSBillDetailList.size() > 0) {

                        int emsbillDetailcount = mEMSExpenseDetail.mEMSBillDetailList.size();

                        for (int i = 0; i < emsbillDetailcount; i++) {

                            EMSBillDetail emsBillDetail = mEMSExpenseDetail.mEMSBillDetailList.get(i);

                            if (emsBillDetail.mIsDeleted == true && emsBillDetail.mEMSBillDetailIDServer == 0) {
                                continue;
                            }

                            ContentValues emsBillDetailValues = new ContentValues();
                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_BILL_DETAIL_ID_SERVER, emsBillDetail.mEMSBillDetailIDServer);


                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID, emsExpenseDetailID);

                            //	emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_EMS_EXPENSE_DETAIL_ID, mEMSExpenseDetail.miEMSExpenseDetailID);

                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_BILL_DATE, emsBillDetail.mBillDate);
                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_BILL_NO, emsBillDetail.mBillNo);
                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_DESCRIPTION, emsBillDetail.mDescription);
                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_AMOUNT, emsBillDetail.mAmount);
                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_IS_ACTIVE, emsBillDetail.mIsActive);
                            emsBillDetailValues.put(EMSBillDetailTableColumns.KEY_IS_DELETED, emsBillDetail.mIsDeleted);


                            Uri billURI = getContentResolver().insert(ProviderContract.URI_EMS_BILL_DETAIL, emsBillDetailValues);

                            long emsBillDetailID = ContentUris.parseId(billURI);


                            if (emsBillDetail.mEMSBillDocumentDetailList != null && emsBillDetail.mEMSBillDocumentDetailList.size() > 0) {
                                int emBillDocumentDetailcount = emsBillDetail.mEMSBillDocumentDetailList.size();

                                int emsNonDeletedBillDocumentDetailcount = 0;

                                for (int s = 0; s < emsBillDetail.mEMSBillDocumentDetailList.size(); s++) {
                                    EMSBillDocumentDetail emsBillDocumentDetail = emsBillDetail.mEMSBillDocumentDetailList.get(s);
                                    if (emsBillDocumentDetail.mIsDeleted == true && emsBillDocumentDetail.mEMSBillDocumentDetailIDServer == 0)
                                        continue;
                                    emsNonDeletedBillDocumentDetailcount++;
                                }

                                int count = 0;

                                ContentValues[] emsBillDocumentListValues = new ContentValues[emsNonDeletedBillDocumentDetailcount];

                                for (int j = 0; j < emBillDocumentDetailcount; j++) {
                                    EMSBillDocumentDetail emsBillDocumentDetail = emsBillDetail.mEMSBillDocumentDetailList.get(j);

                                    if (emsBillDocumentDetail.mIsDeleted == true && emsBillDocumentDetail.mEMSBillDocumentDetailIDServer == 0)
                                        continue;

                                    ContentValues emsBillDocumentValues = new ContentValues();

                                    emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER, emsBillDocumentDetail.mEMSBillDocumentDetailIDServer);

                                    emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DETAIL_ID, emsBillDetailID);
                                    emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_NAME, emsBillDocumentDetail.mDocumentName);

                                    emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_DOCUMENT_PATH, emsBillDocumentDetail.mDocumentFilePath);

                                    emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_IS_ACTIVE, emsBillDocumentDetail.mIsActive);
                                    emsBillDocumentValues.put(EMSBillDocumentDetailTableColumns.KEY_IS_DELETED, emsBillDocumentDetail.mIsDeleted);

                                    emsBillDocumentListValues[count] = emsBillDocumentValues;

                                    count++;
                                }

                                getContentResolver().bulkInsert(
                                        ProviderContract.URI_EMS_BILL_DOCUMENT_DETAIL,
                                        emsBillDocumentListValues);
                            }

                        }

                    }

                }


            }
        } catch (SQLException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        } finally {

            if (db != null && db.isOpen()) {
                db.close();
            }

            finish();
        }

    }


    private boolean isFieldsValidated() {
        if (TextUtils.isEmpty(mExpenseTypeValueTextview.getText())) {

            showAlertDialog(this, "Please select Expense Type.");
            return false;
        }

        //	if (mEMSExpenseDetail.mEMSBillDetailList == null || mEMSExpenseDetail.mEMSBillDetailList .size() == 0) {

        if (mEMSExpenseDetail.getBillCount() == 0) {
            showAlertDialog(this, "Please Add a Bill");
            return false;
        }

        if (miSelectedBillableID == R.id.billable_yes_radio_button && TextUtils.isEmpty(mBillableToEditText.getText())) {
            mCommentsEditText.setError("Billable To is empty.");
            //showAlertDialog(this, "Billable To is empty.");
            return false;
        }

        if (TextUtils.isEmpty(mCommentsEditText.getText())) {
            mCommentsEditText.setError("Comments is empty.");
            //showAlertDialog(this, "Comments is empty.");
            return false;
        }

        return true;
    }


    public void launchNewBillActivity() {
        mbISUpdateExpense = false;
        Intent newBillIntent = new Intent(this, NewBillActivity.class);
        startActivityForResult(newBillIntent, NEW_BILL_REQUEST_CODE);
    }


    public void updateBillActivity(EMSBillDetail emsBillDetail) {
        mbISUpdateExpense = true;
        Intent newBillIntent = new Intent(this, NewBillActivity.class);
        newBillIntent.putExtra("EMSBillDetail", emsBillDetail);
        newBillIntent.putExtra("ExpenseTabId", mEMSExpenseDetail.miEMSExpenseDetailITabID);
        newBillIntent.putExtra("ExpenseStatus", mEMSExpenseDetail.miExpenseStatus);
        startActivityForResult(newBillIntent, NEW_BILL_REQUEST_CODE);
    }


    void updateBillLayout(EMSBillDetail emsBillDetail) {
        if (emsBillDetail.mIsDeleted == true) ///  emsBillDetail.mEMSBillDetailIDServer == 0
        {
            mExpenseItemListLayout.removeView(mUserActionBillayout);
        } else {
            ((TextView) mUserActionBillayout.findViewById(R.id.bill_no_textview)).setText(emsBillDetail.mBillNo);
            ((TextView) mUserActionBillayout.findViewById(R.id.bill_description_textview)).setText(emsBillDetail.mDescription);
            ((TextView) mUserActionBillayout.findViewById(R.id.bill_amount_textview)).setText(emsBillDetail.mAmount + "");
            ((TextView) mUserActionBillayout.findViewById(R.id.date_textview)).setText(Helper.longDateToUIDateString(emsBillDetail.mBillDate));

            mUserActionBillayout.findViewById(R.id.bill_documents_layout).setTag(emsBillDetail);
            mUserActionBillayout.setTag(emsBillDetail);
        }
    }

    void addBill(EMSBillDetail emsBillDetail, final int positionInBillList) {

        final View billDocumentParentView = getLayoutInflater().inflate(R.layout.layout_bill_list_item, null);
        ((TextView) billDocumentParentView.findViewById(R.id.bill_no_textview)).setText(emsBillDetail.mBillNo);
        ((TextView) billDocumentParentView.findViewById(R.id.bill_description_textview)).setText(emsBillDetail.mDescription);
        ((TextView) billDocumentParentView.findViewById(R.id.bill_amount_textview)).setText(emsBillDetail.mAmount + "");
        ((TextView) billDocumentParentView.findViewById(R.id.date_textview)).setText(Helper.longDateToUIDateString(emsBillDetail.mBillDate));

        billDocumentParentView.setTag(emsBillDetail);

        billDocumentParentView.setId(miBillDocumentCount);


        billDocumentParentView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mUserActionBillayout = arg0;

                int viewID = mUserActionBillayout.getId();

                //	int billPosition = 0;

				/*for(int i = 0; i < mExpenseItemListLayout.getChildCount(); ++i) {
                    int currentViewId = mExpenseItemListLayout.getChildAt(i).getId();

					if(currentViewId == viewID) {
						billPosition = i;
					}
				}*/


                EMSBillDetail emsBillDetail = (EMSBillDetail) billDocumentParentView.getTag();

                emsBillDetail.miBillPositionInParentLayout = positionInBillList;

                updateBillActivity(emsBillDetail);

            }
        });

        billDocumentParentView.findViewById(R.id.bill_documents_layout).setTag(emsBillDetail);

        billDocumentParentView.findViewById(R.id.bill_documents_layout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                EMSBillDetail emsBillDetail = (EMSBillDetail) view.getTag();
                displayBillDocumentListDialog(emsBillDetail);

            }
        });


        miBillDocumentCount++;

        mExpenseItemListLayout.addView(billDocumentParentView);
    }


    private void displayBillDocumentListDialog(EMSBillDetail emsBillDetail) {

        try {
            FragmentTransaction spsFragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment previosbillingAndProductDetailsFragment = getSupportFragmentManager().findFragmentByTag("bill_document_list_dialog");
            if (previosbillingAndProductDetailsFragment != null) {
                spsFragmentTransaction.remove(previosbillingAndProductDetailsFragment);
            }

            spsFragmentTransaction.addToBackStack(null);

            Bundle brandShopListBundle = new Bundle();
            brandShopListBundle.putSerializable("bill_document_list", emsBillDetail.mEMSBillDocumentDetailList);

            ExpenseBillDocumentListFragment brandShopListFragment = new ExpenseBillDocumentListFragment();
            brandShopListFragment.setArguments(brandShopListBundle);
            brandShopListFragment.show(spsFragmentTransaction, "bill_document_list_dialog");
        } catch (IllegalStateException e) {
            Helper.printStackTrace(e);
        } catch (Exception e) {
            Helper.printStackTrace(e);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        switch (requestCode) {

            case NEW_BILL_REQUEST_CODE:

                if (resultCode == RESULT_OK) {

                    EMSBillDetail emsBillDetail = (EMSBillDetail) dataIntent.getSerializableExtra("BillDetail");

                    if (mbISUpdateExpense == false) {

                        mEMSExpenseDetail.addBill(emsBillDetail);

                        int count = 0;

                        if (mEMSExpenseDetail.mEMSBillDetailList != null && mEMSExpenseDetail.mEMSBillDetailList.size() > 0) {
                            count = mEMSExpenseDetail.mEMSBillDetailList.size() - 1;
                        }

                        addBill(emsBillDetail, count);

                    } else {
                        mEMSExpenseDetail.updateBill(emsBillDetail);
                        updateBillLayout(emsBillDetail);
                    }

                }
        }

    }


    @Override
    public void onClick(View view) {

        int viewID = view.getId();

        switch (viewID) {

            case R.id.back_btn:

                finish();

                break;

            case R.id.add_bill_textview:

                launchNewBillActivity();

                break;

            case R.id.select_expense_type_imageview:

                displayExpenseTypesDialog();

                break;


            case R.id.submit_expense_btn:

                if (isFieldsValidated())
                    submitExpense();

                break;

            case R.id.update_expense_btn:

                if (isFieldsValidated())
                    updateExpenseDialog();

                break;

            case R.id.cancel_expense_btn:

                if (isFieldsValidated())
                    cancelExpense();

                break;

            case R.id.approve_expense_btn:

                if (isFieldsValidated())
                    approveOrRejectExpense("Do you want to Approve Expense?", true);

                break;


            case R.id.reject_expense_btn:

                if (isFieldsValidated())
                    approveOrRejectExpense("Do you want to Reject Expense?", false);

                break;


            default:
                break;

        }
    }


    public void updateExpenseDialog() {

        Helper.showAlertDialog(this,
                SSCAlertDialog.WARNING_TYPE, "Update Expense",
                "Do you want to update this Expense?", "OK",
                new SSCAlertDialog.OnSDAlertDialogClickListener() {

                    @Override
                    public void onClick(SSCAlertDialog sdAlertDialog) {
                        sdAlertDialog.dismiss();

                        updateExpense();


                    }
                }, false, null, null);

    }


    private void approveOrRejectExpense(String msg, final boolean isApproved) {

        Helper.showAlertDialog(this,
                SSCAlertDialog.WARNING_TYPE, "Expense Approval",
                msg, "OK",
                new SSCAlertDialog.OnSDAlertDialogClickListener() {

                    @Override
                    public void onClick(SSCAlertDialog sdAlertDialog) {
                        sdAlertDialog.dismiss();
						
						/*DatabaseHelper.getConnection(
								getApplicationContext()).cancelExpensesData(
								new long[] { mEMSExpenseDetail.miEMSExpenseDetailID });
						*/

                        if (isApproved)
                            mEMSExpenseDetail.miExpenseStatus = EMSConstants.EXPENSE_STATUS_APPROVED;

                        else
                            mEMSExpenseDetail.miExpenseStatus = EMSConstants.EXPENSE_STATUS_REJECT;

                        mEMSExpenseDetail.miEMSExpenseDetailITabID = EMSConstants.EXPENSE_TAB_APPROVED;

                        updateExpense();
                        //finish();

                    }
                }, false, null, null);

    }

    public void cancelExpense() {

        Helper.showAlertDialog(this,
                SSCAlertDialog.WARNING_TYPE, "Cancel Expense",
                "Do you want to cancel this Expense?", "OK",
                new SSCAlertDialog.OnSDAlertDialogClickListener() {

                    @Override
                    public void onClick(SSCAlertDialog sdAlertDialog) {
                        sdAlertDialog.dismiss();
							
							/*DatabaseHelper.getConnection(
									getApplicationContext()).cancelExpensesData(
									new long[] { mEMSExpenseDetail.miEMSExpenseDetailID });
							*/
                        mEMSExpenseDetail.miExpenseStatus = EMSConstants.EXPENSE_STATUS_CANCELLED;
                        updateExpense();
                        //finish();

                    }
                }, false, null, null);

    }

}
