package com.samsung.ssc.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.samsung.ssc.constants.DownloadMasterCodes;
import com.samsung.ssc.database.DatabaseConstants;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.CollectionResponseTableColumns;
import com.samsung.ssc.database.DatabaseConstants.CompetitorColumns;
import com.samsung.ssc.database.DatabaseConstants.CounterShareDisplayShareResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.DownloadDataModuleMappingTableColums;
import com.samsung.ssc.database.DatabaseConstants.EOLOrderBookingResponseMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.EOLSchemeDetailsMasterColoums;
import com.samsung.ssc.database.DatabaseConstants.OrderResponseMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.PaymentModeMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.ProductMasterTableColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackCategoryMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackStatusMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.FeedbackTypeMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.TeamMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.QuestionAnswerResponseColumns.UserFeedbackMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceBrandProductMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceFixtureMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMDataResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMProductMappingMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RacePosmMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.RaceProductAuditResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.StoreBasicColulmns;
import com.samsung.ssc.database.DatabaseConstants.StoreGeoTagResponseMasterColumns;
import com.samsung.ssc.database.DatabaseConstants.StorePerformaceColumns;
import com.samsung.ssc.database.DatabaseConstants.SurveyQuestionColumns;
import com.samsung.ssc.database.DatabaseConstants.SurveyQuestionOptionsColumns;
import com.samsung.ssc.database.DatabaseConstants.TableNames;
import com.samsung.ssc.database.DatabaseConstants.UserModuleTableColumns;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SSCContentProvider extends ContentProvider {

    // helper constants for use with the UriMatcher
    private static final int USER_PROFILE = 1;
    private static final int INSERT_SURVEY_QUESTIONS = 2;
    private static final int INSERT_SURVEY_QUESTION_OPTIONS = 3;
    private static final int GET_SURVEY_QUESTIONS = 4;
    private static final int DELETE_SURVEY_QUESTION_ANSWER_RESPONSE = 5;
    private static final int INSERT_SURVEY_QUESTION_ANSWER_RESPONSE = 6;
    private static final int STORES_BASICS = 7;
    private static final int GET_OTHER_STORES_CITIES = 8;
    private static final int GET_OTHER_STORES_BASICS_BY_CITY = 9;
    private static final int DELETE_OTHER_STORE = 10;
    private static final int STORE_PERFORMANCE = 11;
    private static final int COUNTER_SHARE_DISPLAY_SHARE_RESPONSE = 12;
    private static final int COMP_PRODUCT_GROUP = 13;
    private static final int PRODUCT_CATAGORY_P1 = 14;
    private static final int PRODUCT_CATAGORY_P2 = 15;
    private static final int STOCK_ESCALATION_ORDER_RESPONSE_URI = 16;
    private static final int STOCK_ESCALATION_SKU = 17;
    private static final int STOCK_ESCALATION_ORDER_RESPONSE = 18;
    private static final int DELETE_STOCK_ESCALATION_RESPONSE = 33;
    private static final int USER_MODULES = 19;
    private static final int STORE_COUNT = 20;
    // for Feedback tables
    private static final int FEEDBACK_STATUS = 21;
    private static final int FEEDBACK_TEAMS = 22;
    private static final int FEEDBACK_CATEGORY = 23;
    private static final int FEEDBACK_TYPE = 24;
    private static final int USER_FEEDBACK_STORE_WISE = 25;
    private static final int USER_FEEDBACK_ENTRY_CHECK = 26;
    private static final int USER_FEEDBACK_ALL = 27;
    private static final int PAYMENT_MODES = 28;
    private static final int COLLECTION_RESPONSE = 29;
    private static final int PLANOGRAM_RESPONSE = 30;
    private static final int PLANOGRAM_PRODUCT_RESPONSE = 31;
    private static final int PLANOGRAM_COMPITITOR_RESPONSE = 32;
    private static final int DOWNLOAD_DATA = 34;
    private static final int STORE_GEO_TAG_RESPONSE = 35;

    private static final int SURVEY_QUESTION_ANSWER_RESPONSE = 36;
    private static final int ACTIVITY_DATA_RESPONSE = 37;

    private static final int RACE_BRAND_CATEGORY_MAPPING = 38;

    private static final int RACE_BRAND = 39;
    private static final int RACE_FIXTURE = 40;
    private static final int RACE_POSM = 41;
    private static final int RACE_PRODUCT_CATEGORY = 42;
    private static final int RACE_POSM_PRODUCT_MAPPING = 43;
    private static final int RACE_BRAND_PRODUCT = 44;
    private static final int RACE_PROUDCT_AUDIT_RESPONSE = 45;
    private static final int RACE_POSM_RESPONSE = 46;

    private static final int RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID = 47;
    private static final int RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS = 48;

    private static final int EOL_SCHEME_RESPONSE = 49;

    private static final int EOL_SCHEME_DETAILS_RESPONSE = 50;
    // private static final int GET_USER_PROFILE=2;

    private static final int RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC = 51;

    private static final int RACE_POSM_RESPONSE_WITH_ID = 52;

    private static final int PRODUCTS = 53;

    private static final int DOWNLOAD_DATA_SINGLE_SERVICE = 54;

    private static final int COMPETITORS = 55;

    private static final int PALNOGRAM_CLASS = 56;

    private static final int PLANOGRAM_PRODUCT = 57;

    private static final int COMPETITOR_PRODUCT_GROUP_MAPPING = 58;

    private static final int EOL_SCHEME_DETAIL = 59;

    private static final int EOL_SCHEME_HEADER = 60;

    private static final int EOL_ORDER_BOOKING_RESPONSE_MASTER = 61;

    private static final int RACE_PRODUCT_AUDIT_CART_HA_PRODUCTS = 62;

    private static final int STORE_MODULE_MASTER = 63;

    private static final int MODULE_DATA_DOWNLOAD_STATUS = 64;

    private static final int STORE_PERFORMANCE_SECTION_1 = 65;

    private static final int STORE_PERFORMANCE_SECTION_2 = 66;

    private static final int STORE_PERFORMANCE_SECTION_3 = 67;

    private static final int STORE_PERFORMANCE_SECTION_4 = 68;

    private static final int MOM_DETAIL_MASTER = 69;

    private static final int MOM_ATTENDEES_MASTER = 69;

    private static final int LMS_LEAVE_LIST = 70;
    private static final int LMS_LEAVE_DATE_LIST = 71;
    private static final int LMS_LEAVE_STATUS_LOG = 72;

    private static final int LMS_LEAVE_TYPE = 73;

    private static final int LMS_LEAVE_CONFIGURATION = 74;

    // Expense module
    private static final int EXPENSE_TYPE_MASTER = 75;
    private static final int EMS_EXPENSE_DETAIL = 76;
    private static final int EMS_BILL_DETAIL = 77;
    private static final int EMS_BILL_DOCUMENT_DETAIL = 78;


    private static final UriMatcher URI_MATCHER;

    private DatabaseHelper databaseHelper;

    // prepare the UriMatcher
    static {

        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_USER_PROFILE, USER_PROFILE);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_INSERT_SURVET_QUESTIONS,
                INSERT_SURVEY_QUESTIONS);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_INSERT_SURVET_QUESTION_OPTIONS,
                INSERT_SURVEY_QUESTION_OPTIONS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_GET_SURVEY_QUESTIONS,
                GET_SURVEY_QUESTIONS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_DELETE_SURVREY_QUESTION_ANSWER_RESPONSE,
                DELETE_SURVEY_QUESTION_ANSWER_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_INSERT_SURVEY_QUESTION_ANSWER_RESPONSE,
                INSERT_SURVEY_QUESTION_ANSWER_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_SURVEY_QUESTION_ANSWER_RESPONSE,
                SURVEY_QUESTION_ANSWER_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORES_BASICS, STORES_BASICS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_DELETE_OTHER_STORE, DELETE_OTHER_STORE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_PERFORAMCE, STORE_PERFORMANCE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE,
                COUNTER_SHARE_DISPLAY_SHARE_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_COMP_PRODUCT_GROUP, COMP_PRODUCT_GROUP);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_COUNT, STORE_COUNT);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STOCK_ESCALATION_PRODUCT_CATAGORY_P1,
                PRODUCT_CATAGORY_P1);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STOCK_ESCALATION_PRODUCT_CATAGORY_P2,
                PRODUCT_CATAGORY_P2);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STOCK_ESCALATION_ORDER_RESPONSE_URI,
                STOCK_ESCALATION_ORDER_RESPONSE_URI);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STOCK_ESCALATION_SKU,
                STOCK_ESCALATION_SKU);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STOCK_ESCALATION_RESPONSE,
                STOCK_ESCALATION_ORDER_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_DELETE_STOCK_ESCALATION_RESPONSE,
                DELETE_STOCK_ESCALATION_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_USER_MODULE, USER_MODULES);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_PLANOGRAM_RESPONSE, PLANOGRAM_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_PLANOGRAM_PRODUCT_RESPONSE,
                PLANOGRAM_PRODUCT_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_PLANOGRAM_COMPETITORS_RESPONSE,
                PLANOGRAM_COMPITITOR_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_FEEDBACK_STATUS, FEEDBACK_STATUS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_FEEDBACK_TEAM, FEEDBACK_TEAMS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_FEEDBACK_CATEGORY, FEEDBACK_CATEGORY);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_FEEDBACK_TYPE, FEEDBACK_TYPE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_USER_FEEDBACK_STORE_WISE,
                USER_FEEDBACK_STORE_WISE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_USER_FEEDBACK_ENTRY_CHECK,
                USER_FEEDBACK_ENTRY_CHECK);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_USER_FEEDBACK_ALL, USER_FEEDBACK_ALL);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_PAYMENT_MODES, PAYMENT_MODES);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_COLLECTION_RESPONSE, COLLECTION_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_GET_OTHER_STORES_BASICS_BY_CITY,
                GET_OTHER_STORES_BASICS_BY_CITY);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_PERFORAMCE, STORE_PERFORMANCE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_GET_OTHER_STORES_CITIES,
                GET_OTHER_STORES_CITIES);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_DOWNLOAD_DATA, DOWNLOAD_DATA);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_GEO_TAG_RESPONSE,
                STORE_GEO_TAG_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_ACTIVITY_DATA_RESPONSE,
                ACTIVITY_DATA_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_BRAND_CATEGORY_MAPPING,
                RACE_BRAND_CATEGORY_MAPPING);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_BRAND, RACE_BRAND);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_FIXTURE, RACE_FIXTURE);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_POSM, RACE_POSM);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_PRODUCT_CATEGORY,
                RACE_PRODUCT_CATEGORY);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_POSM_PRODUCT_MAPPING,
                RACE_POSM_PRODUCT_MAPPING);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_BRAND_PRODUCT, RACE_BRAND_PRODUCT);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_PRODUCT_AUDIT_RESPONSE,
                RACE_PROUDCT_AUDIT_RESPONSE);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_POSM_RESPONSE, RACE_POSM_RESPONSE);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_POSM_RESPONSE_WITH_ID + "/*",
                RACE_POSM_RESPONSE_WITH_ID);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID,
                RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS,
                RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_PRODUCT_AUDIT_CART_HA_PRODUCTS,
                RACE_PRODUCT_AUDIT_CART_HA_PRODUCTS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC,
                RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_EOL_SCHEME_RESPONSE, EOL_SCHEME_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_EOL_SCHEME_DETAILS_RESPONSE + "/*",
                EOL_SCHEME_DETAILS_RESPONSE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_PRODUCTS, PRODUCTS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_DOWNLOAD_DATA_SINGLE_SERVICE,
                DOWNLOAD_DATA_SINGLE_SERVICE);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_COMPETITORS, COMPETITORS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_PLANOGRAM_CLASS, PALNOGRAM_CLASS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_PLANOGRAM_PRODUCT, PLANOGRAM_PRODUCT);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_COMPETITOR_PRODUCT_GRUOP_MAPPING,
                COMPETITOR_PRODUCT_GROUP_MAPPING);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_EOL_SCHEME_HEADER, EOL_SCHEME_HEADER);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_EOL_SCHEME_DETAIL, EOL_SCHEME_DETAIL);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_EOL_ORDER_BOOKING_RESPONSE_MASTER,
                EOL_ORDER_BOOKING_RESPONSE_MASTER);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_MODULE_MASTER, STORE_MODULE_MASTER);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_MODULE_DATA_DOWNLOAD_STATUS,
                MODULE_DATA_DOWNLOAD_STATUS);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_PERFORAMCE_SECTION_1,
                STORE_PERFORMANCE_SECTION_1);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_PERFORAMCE_SECTION_2,
                STORE_PERFORMANCE_SECTION_2);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_PERFORAMCE_SECTION_3,
                STORE_PERFORMANCE_SECTION_3);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_STORE_PERFORAMCE_SECTION_4,
                STORE_PERFORMANCE_SECTION_4);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_MOM_DETAILS_MASTER,
                MOM_DETAIL_MASTER);

        URI_MATCHER.addURI(ProviderContract.AUTHORITY,
                ProviderContract.PATH_MOM_ATTENDEES_MASTER,
                MOM_ATTENDEES_MASTER);


        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_LMS_LIST, LMS_LEAVE_LIST);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_LMS_DATE_LIST, LMS_LEAVE_DATE_LIST );
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_LMS_STATUS_LOG, LMS_LEAVE_STATUS_LOG);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_LMS_LEAVE_TYPE, LMS_LEAVE_TYPE);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_LMS_LEAVE_CONFIGURATION, LMS_LEAVE_CONFIGURATION);

        // Expense module
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_EXPENSE_TYPE_MASTER, EXPENSE_TYPE_MASTER);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_EMS_EXPENSE_DETAIL, EMS_EXPENSE_DETAIL);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_EMS_BILL_DETAIL, EMS_BILL_DETAIL);
        URI_MATCHER.addURI(ProviderContract.AUTHORITY, ProviderContract.PATH_EMS_BILL_DOCUMENT_DETAIL, EMS_BILL_DOCUMENT_DETAIL);



    }

    @Override
    public boolean onCreate() {
        databaseHelper = DatabaseHelper.getConnection(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {

            case RACE_PRODUCT_AUDIT_RESPONSE_FOR_SYNC:
                cursor = db.rawQuery(getProductAuditResponseForSync(),
                        selectionArgs);
                break;
            case RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS:
                cursor = db
                        .rawQuery(getProductAuditCartAVProducts(), selectionArgs);
                break;

            case RACE_PRODUCT_AUDIT_CART_HA_PRODUCTS:
                cursor = db
                        .rawQuery(getProductAuditCartHAProducts(), selectionArgs);
                break;
            case RACE_POSM_RESPONSE_WITH_ID:

                String mAuditID = uri.getLastPathSegment();

                cursor = db.rawQuery(getPosmResponseByProductID(mAuditID),
                        selectionArgs);
                break;
            case RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID:
                cursor = db.rawQuery(getAuditResponseQueryByID(), selectionArgs);
                break;
            case RACE_POSM_PRODUCT_MAPPING:
                cursor = db.rawQuery(getPosmProductMappingQuery(), selectionArgs);
                break;
            case RACE_PROUDCT_AUDIT_RESPONSE:
                builder.setTables(DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case RACE_FIXTURE:
                builder.setTables(DatabaseConstants.TableNames.TABLE_RACE_FIXTURE_MASTER);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case RACE_BRAND:
                builder.setTables(DatabaseConstants.TableNames.TABLE_RACE_BRAND_MASTER);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case RACE_BRAND_PRODUCT:
                builder.setTables(TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ACTIVITY_DATA_RESPONSE:
                String query1 = getActivityDataModuleQuerry();
                cursor = db.rawQuery(query1, selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case EOL_SCHEME_RESPONSE:

                try {
                    builder.setTables(DatabaseConstants.TableNames.TABLE_EOL_SCHEME_HEADER_MASTER);

                    cursor = builder.query(db, projection, selection,
                            selectionArgs, null, null, sortOrder);
                    cursor.setNotificationUri(getContext().getContentResolver(),
                            uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case EOL_SCHEME_DETAILS_RESPONSE:

                String mActivityID = uri.getLastPathSegment();
                builder.setTables(TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER
                        + " LEFT OUTER JOIN "
                        + TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                        + " ON "
                        + DatabaseConstants.TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER
                        + "."
                        + EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID
                        + "="
                        + TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                        + "."
                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_SCHEME_ID
                        + " AND "
                        + DatabaseConstants.TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER
                        + "."
                        + EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE
                        + "="
                        + TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                        + "."
                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE
                        + " AND "
                        + TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                        + "."
                        + EOLOrderBookingResponseMasterColoums.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                        + " = " + mActivityID);

                Map<String, String> columnMap = new HashMap<String, String>();

                columnMap.put(TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER + "."
                                + EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID,
                        TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER + "."
                                + EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID
                                + " AS "
                                + EOLSchemeDetailsMasterColoums.KEY_EOL_SCHEME_ID);

                columnMap
                        .put(TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER
                                        + "."
                                        + EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE,
                                TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER
                                        + "."
                                        + EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE
                                        + " AS "
                                        + EOLSchemeDetailsMasterColoums.KEY_EOL_DETAILS_BASIC_MODEL_CODE);

                columnMap
                        .put(TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                                        + "."
                                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY,
                                TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                                        + "."
                                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY
                                        + " AS "
                                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ORDER_QUATITY);
                columnMap
                        .put(TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                                        + "."
                                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT,
                                TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER
                                        + "."
                                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT
                                        + " AS "
                                        + EOLOrderBookingResponseMasterColoums.KEY_EOL_DETAILS_ACTUAL_SUPPORT);

                builder.setProjectionMap(columnMap);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;
            case SURVEY_QUESTION_ANSWER_RESPONSE:

                cursor = db.rawQuery(getSurveyQuestionAnswerResponseQuery(),
                        selectionArgs);
                break;

            case USER_PROFILE:

                builder.setTables(DatabaseConstants.TableNames.TABLE_USER_PROFILE);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            case STORES_BASICS:

                try {
                    builder.setTables(DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER);

                    cursor = builder.query(db, projection, selection,
                            selectionArgs, null, null, sortOrder);
                    cursor.setNotificationUri(getContext().getContentResolver(),
                            uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GET_OTHER_STORES_CITIES:

                try {

                    String query = getOtherStoresDistinctCitiesQuesry();

                    cursor = db.rawQuery(query, selectionArgs);
                /*
				 * cursor.setNotificationUri(getContext().getContentResolver( ),
				 * uri);
				 */
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GET_OTHER_STORES_BASICS_BY_CITY:
                try {
                    builder.setTables(DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER);

                    cursor = builder.query(db, projection, selection,
                            selectionArgs, null, null, sortOrder);
				/*
				 * cursor.setNotificationUri(getContext().getContentResolver( ),
				 * uri);
				 */
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GET_SURVEY_QUESTIONS:
                // String[] args = {String.valueOf(uri.getLastPathSegment())};

                String query = getSurveyQuestionQuery();

                cursor = db.rawQuery(query, selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case STORE_PERFORMANCE:

			/*
			 * builder.setTables(DatabaseConstants.TableNames.
			 * TABLE_STORE_PERFORMANCE_MASTER);
			 * 
			 * cursor = builder.query(db, projection, selection, selectionArgs,
			 * null, null, sortOrder);
			 * 
			 * cursor.setNotificationUri(getContext().getContentResolver(),
			 * uri);
			 */

                cursor = db.rawQuery(getIsStorePerformanceEmptyQuery(),
                        selectionArgs);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            case COUNTER_SHARE_DISPLAY_SHARE_RESPONSE:

                cursor = db.rawQuery(getCounterShareDisplayShareResposeQuery(),
                        selectionArgs);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            case COMP_PRODUCT_GROUP:

                builder.setTables(DatabaseConstants.TableNames.TABLE_COMPETETION_PRODUCT_GROUP);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            case USER_MODULES:

                try {

                    cursor = db.rawQuery(getUserModuleQuerry(), selectionArgs);
                    cursor.setNotificationUri(getContext().getContentResolver(),
                            uri);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case STORE_COUNT:

                cursor = db.rawQuery(getStoreCountQuerry(), selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case PRODUCT_CATAGORY_P1:

                cursor = db.rawQuery(getProductCatagoryP1(), selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case PRODUCT_CATAGORY_P2:

                cursor = db.rawQuery(getProductCatagoryP2(), selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case STOCK_ESCALATION_ORDER_RESPONSE_URI:

                cursor = db.rawQuery(getOrderResponse(), selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case STOCK_ESCALATION_SKU:

                cursor = db.rawQuery(getSKUProductListQuery(), selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case FEEDBACK_TEAMS:

                builder.setTables(DatabaseConstants.TableNames.TABLE_FMS_TEAM_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case FEEDBACK_STATUS:

                builder.setTables(DatabaseConstants.TableNames.TABLE_FEEDBACK_STATUS_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case FEEDBACK_CATEGORY:

                builder.setTables(DatabaseConstants.TableNames.TABLE_FEEDBACK_CATEGORY_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case FEEDBACK_TYPE:

                builder.setTables(DatabaseConstants.TableNames.TABLE_FEEDBACK_TYPE_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case USER_FEEDBACK_STORE_WISE:

                cursor = db.rawQuery(getUserFeedbackQuery(), selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            case USER_FEEDBACK_ENTRY_CHECK:

                builder.setTables(DatabaseConstants.TableNames.TABLE_USER_FEEDBACK_MASTER);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case USER_FEEDBACK_ALL:

                builder.setTables(DatabaseConstants.TableNames.TABLE_USER_FEEDBACK_MASTER);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case PAYMENT_MODES:
                builder.setTables(DatabaseConstants.TableNames.TABLE_PAYMENT_MODE_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case COLLECTION_RESPONSE:

                cursor = db.rawQuery(getCollectionResponseQuery(), selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            case DOWNLOAD_DATA:

                builder.setTables(DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case PLANOGRAM_RESPONSE:

                builder.setTables(DatabaseConstants.TableNames.TABLE_PLANOGRAM_RESPONSE_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case PLANOGRAM_COMPITITOR_RESPONSE:

                builder.setTables(DatabaseConstants.TableNames.TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case PLANOGRAM_PRODUCT_RESPONSE:

                builder.setTables(DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case STORE_GEO_TAG_RESPONSE:

                cursor = db.rawQuery(getGeoTagResponseQuery(), selectionArgs);

                break;

            case MODULE_DATA_DOWNLOAD_STATUS:
                cursor = db.rawQuery(getModuleDataDownloadStatus(), selectionArgs);
                break;

            case DOWNLOAD_DATA_SINGLE_SERVICE:

                builder.setTables(DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case STORE_PERFORMANCE_SECTION_1:

                cursor = db.rawQuery(getQueryStorePerformanceSection1(),
                        selectionArgs);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case STORE_PERFORMANCE_SECTION_2:

                cursor = db.rawQuery(getQueryStorePerformanceSection2(),
                        selectionArgs);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;


            case STORE_PERFORMANCE_SECTION_3:

                cursor = db.rawQuery(getQueryStorePerformanceSection3(),
                        selectionArgs);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case STORE_PERFORMANCE_SECTION_4:

                cursor = db.rawQuery(getQueryStorePerformanceSection4(),
                        selectionArgs);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;


            case LMS_LEAVE_LIST:




                break;

            case EXPENSE_TYPE_MASTER:
                builder.setTables(DatabaseConstants.TableNames.TABLE_EXPENSE_TYPE_MASTER);

                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

		/* Cursor cursor =databaseHelper.getUserProfileCursor(); */
        return cursor;
    }



    private String getModuleDataDownloadStatus() {

        return "SELECT ddmm."
                + DownloadDataModuleMappingTableColums.KEY_MODULE_CODE
                + ",ddmm."
                + DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE
                + ",ddm."
                + DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DOWNLOAD_STATUS
                + " from "
                + TableNames.TABLE_DOWNLOAD_DATA_MODULE_MAPPING_MASTER
                + " ddmm left join "
                + TableNames.TABLE_DOWNLOAD_DATA_MASTER
                + " ddm where ddm."
                + DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE
                + "=ddmm."
                + DownloadDataModuleMappingTableColums.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE
                + " order by "
                + DownloadDataModuleMappingTableColums.KEY_MODULE_CODE;
    }

    private String getProductAuditResponseForSync() {
        return "SELECT stock."
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_TOPPER
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_BRAND_ID
                + ",stock."
                + RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG
                + ",posm."
                + RacePOSMDataResponseMasterColumns.KEY_POSM_ID
                + ",posm."
                + RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE
                + " from "
                + TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER
                + " stock LEFT JOIN "
                + TableNames.TABLE_RACE_POSM_DATA_RESPONSE_MASTER
                + " posm ON stock."
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + "=posm."
                + RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + " where stock."
                + RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + "=? ORDER BY " + "stock."
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + " ASC";

    }

    private String getProductAuditCart() {
        return "SELECT " + "rr."
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + ","
                + "rr."
                + RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER
                + ","
                + "rr."
                + RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER
                + ","
                + "rm."
                + RaceBrandProductMasterColumns.KEY_NAME
                + ","
                + "rfm."
                + RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME
                + " from "
                + TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER
                + " rr INNER JOIN "
                + TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER
                + " rm on  rr."
                + RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID
                + " = rm."
                + RaceBrandProductMasterColumns.KEY_PRODUCT_ID
                + " Inner Join "
                + TableNames.TABLE_RACE_FIXTURE_MASTER
                + " rfm on rr."
                + RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID
                + "=rfm."
                + RaceFixtureMasterColumns.KEY_FIXTURE_ID
                + " WHERE "
                + RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " =? " + " ORDER BY "
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + " DESC";
    }

    private String getProductAuditCartAVProducts() {
        return "SELECT rr."
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + ",rr."
                + RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER
                + ",rr."
                + RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER
                + ",rm."
                + RaceBrandProductMasterColumns.KEY_NAME
                + ",rfm."
                + RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME
                + ",rbm."
                + RaceBrandMasterColumns.KEY_BRAND_NAME
                + ",rm."
                + RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP
                + " from "
                + TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER
                + " rr INNER JOIN "
                + TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER
                + " rm on  rr."
                + RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID
                + "= rm."
                + RaceBrandProductMasterColumns.KEY_PRODUCT_ID
                + " Inner Join "
                + TableNames.TABLE_RACE_FIXTURE_MASTER
                + " rfm on rr."
                + RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID
                + "=rfm."
                + RaceFixtureMasterColumns.KEY_FIXTURE_ID
                + " left Join "
                + TableNames.TABLE_RACE_BRAND_MASTER
                + " rbm on rbm."
                + RaceBrandMasterColumns.KEY_BRAND_ID
                + "=rr."
                + RaceProductAuditResponseMasterColumns.KEY_BRAND_ID
                + " WHERE "
                + RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " = ? AND "
                + RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER
                + "!=-1 ORDER BY rfm."
                + RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME + " ASC";
    }

    private String getProductAuditCartHAProducts() {
        return "SELECT rr."
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + ",rr."
                + RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER
                + ",rr."
                + RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER
                + ",rm."
                + RaceBrandProductMasterColumns.KEY_NAME
                + ",rfm."
                + RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME
                + ",rbm."
                + RaceBrandMasterColumns.KEY_BRAND_NAME
                + ",rm."
                + RaceBrandProductMasterColumns.KEY_PRODUCT_GROUP
                + " from "
                + TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER
                + " rr INNER JOIN "
                + TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER
                + " rm on  rr."
                + RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID
                + "= rm."
                + RaceBrandProductMasterColumns.KEY_PRODUCT_ID
                + " Inner Join "
                + TableNames.TABLE_RACE_FIXTURE_MASTER
                + " rfm on rr."
                + RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID
                + "=rfm."
                + RaceFixtureMasterColumns.KEY_FIXTURE_ID
                + " left Join "
                + TableNames.TABLE_RACE_BRAND_MASTER
                + " rbm on rbm."
                + RaceBrandMasterColumns.KEY_BRAND_ID
                + "=rr."
                + RaceProductAuditResponseMasterColumns.KEY_BRAND_ID
                + " WHERE "
                + RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " = ? AND "
                + RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER
                + "=-1 ORDER BY rbm." + RaceBrandMasterColumns.KEY_BRAND_NAME
                + " ASC";
    }

    private String getPosmResponseByProductID(String mAuditID) {
        String query = "SELECT PM." + RacePosmMasterColumns.KEY_POSM_ID
                + ",PM." + RacePosmMasterColumns.KEY_POSM_NAME + ",PPRM."
                + RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + ",PPRM." + RacePOSMDataResponseMasterColumns.KEY_POSM_TYPE
                + " FROM " + TableNames.TABLE_RACE_POSM_MASTER + " PM "
                + "INNER JOIN "
                + TableNames.TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER
                + " PPMM ON PPMM."

                + RacePOSMProductMappingMasterColumns.KEY_POSM_ID + "= PM."
                + RacePosmMasterColumns.KEY_POSM_ID + " LEFT JOIN "
                + TableNames.TABLE_RACE_POSM_DATA_RESPONSE_MASTER + " PPRM "
                + "ON (PPRM." + RacePOSMDataResponseMasterColumns.KEY_POSM_ID
                + "= PM." + RacePosmMasterColumns.KEY_POSM_ID + " AND PPRM."
                + RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID + "="
                + mAuditID + ")" + "WHERE PPMM."
                + RacePOSMProductMappingMasterColumns.KEY_PRODUCT_ID + " =?";
        return query;

        //
    }

    private String getAuditResponseQueryByID() {
        String query = "SELECT PRM."
                + RaceProductAuditResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + ",PRM."
                + RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID
                + ",PRM."
                + RaceProductAuditResponseMasterColumns.KEY_PRODUCT_ID
                + ",PRM."
                + RaceProductAuditResponseMasterColumns.KEY_WALL_NUMBER
                + ",PRM."
                + RaceProductAuditResponseMasterColumns.KEY_ROW_NUMBER
                + ",PRM." + RaceProductAuditResponseMasterColumns.KEY_TOPPER
                + ",PRM."
                + RaceProductAuditResponseMasterColumns.KEY_SWITCHED_ON
                + ",PRM." + RaceProductAuditResponseMasterColumns.KEY_BRAND_ID
                + ",PRM." + RaceProductAuditResponseMasterColumns.KEY_PRICE_TAG
                + ",RFM." + RaceFixtureMasterColumns.KEY_FIXTURE_CATEGORY_NAME
                + ",RFM." + RaceFixtureMasterColumns.KEY_FIXTURE_SUB_CATEGORY
                + " FROM "
                + TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER
                + " PRM INNER JOIN " + TableNames.TABLE_RACE_FIXTURE_MASTER
                + " RFM ON RFM." + RaceFixtureMasterColumns.KEY_FIXTURE_ID
                + " = PRM."
                + RaceProductAuditResponseMasterColumns.KEY_FIXTURE_ID
                + " where PRM."
                + RaceProductAuditResponseMasterColumns.KEY_STOCK_AUDIT_ID
                + " =?";
        return query;
    }

    private String getPosmProductMappingQuery() {

        String selectQuery = "SELECT RPM."
                + RacePOSMProductMappingMasterColumns.KEY_POSM_ID + ",RPM."
                + RacePosmMasterColumns.KEY_POSM_NAME + " FROM "
                + TableNames.TABLE_RACE_POSM_MASTER + " RPM INNER JOIN "
                + TableNames.TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER
                + " RPPM ON  RPM." + RacePosmMasterColumns.KEY_POSM_ID
                + " = RPPM." + RacePOSMProductMappingMasterColumns.KEY_POSM_ID
                + " WHERE RPPM."
                + RacePOSMProductMappingMasterColumns.KEY_PRODUCT_ID
                + "=? ORDER BY " + RacePosmMasterColumns.KEY_POSM_NAME;
        return selectQuery;
    }

    private String getSurveyQuestionAnswerResponseQuery() {
        String query = "SELECT ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID
                + ",qar."
                + QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_ID
                + ",qar."
                + QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_USER_RESPONSE
                + " ,qar."
                + QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_SUB_ID
                + ", qar."
                + QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_TYPE_ID
                + ", sqm."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID
                + " FROM "
                + TableNames.TABLE_QUESTION_ANSWER_RESPONSE_MASTER
                + " qar INNER JOIN "
                + TableNames.TABLE_ACTIVITY_DATA_MASTER
                + " ad on qar."
                + QuestionAnswerResponseColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + "=ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " LEFT JOIN "
                + TableNames.TABLE_SURVEY_QUESTION_MASTERS
                + " sqm on qar."
                + QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_ID
                + "  = sqm."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID
                + " WHERE ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + "=?";
        return query;
    }

    private String getCollectionResponseQuery() {

        String query = "SELECT crm."
                + CollectionResponseTableColumns.KEY_USER_ROLE_ID
                + ",crm."
                + CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID
                + ",crm."
                + CollectionResponseTableColumns.KEY_COLLECTION_TRANSACTION_ID
                + ", crm."
                + CollectionResponseTableColumns.KEY_COLLECTION_COMMENTS
                + ", crm."
                + CollectionResponseTableColumns.KEY_COLLECTION_AMOUNT
                + ", crm."
                + CollectionResponseTableColumns.KEY_PAYMENT_MODE_ID
                + ", crm."
                + CollectionResponseTableColumns.KEY_BEAT_STORE_ID
                + ", ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID
                + ", crm."
                + CollectionResponseTableColumns.KEY_USER_ID
                + ", crm."
                + CollectionResponseTableColumns.KEY_COLLECTION_PAYMENT_DATE
                + ", pmm."
                + PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_NAME
                + " FROM "
                + TableNames.TABLE_COLLECTION_RESPONSE_MASTER
                + " crm  INNER JOIN "
                + TableNames.TABLE_PAYMENT_MODE_MASTER
                + " pmm  on pmm."
                + PaymentModeMasterTableColumns.KEY_PAYMENT_MODE_ID
                + " = crm."
                + CollectionResponseTableColumns.KEY_PAYMENT_MODE_ID
                + " INNER JOIN "
                + TableNames.TABLE_ACTIVITY_DATA_MASTER
                + " ad on crm."
                + CollectionResponseTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " = ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " WHERE ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " = ?";

        return query;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = 0;
        switch (URI_MATCHER.match(uri)) {

            case RACE_POSM_RESPONSE:
                id = db.insert(
                        DatabaseConstants.TableNames.TABLE_RACE_POSM_DATA_RESPONSE_MASTER,
                        null, values);
                break;

            case RACE_PROUDCT_AUDIT_RESPONSE:
                id = db.insert(
                        DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER,
                        null, values);
                break;
            case ACTIVITY_DATA_RESPONSE:
                id = db.insert(
                        DatabaseConstants.TableNames.TABLE_ACTIVITY_DATA_MASTER,
                        null, values);
                break;

            case USER_PROFILE:
                id = db.insert(DatabaseConstants.TableNames.TABLE_USER_PROFILE,
                        null, values);
                break;

            case PLANOGRAM_RESPONSE:
                id = db.insert(
                        DatabaseConstants.TableNames.TABLE_PLANOGRAM_RESPONSE_MASTER,
                        null, values);
                break;

            case STORE_PERFORMANCE:

                id = db.insert(
                        DatabaseConstants.TableNames.TABLE_STORE_PERFORMANCE_MASTER,
                        null, values);

                break;

            case USER_FEEDBACK_STORE_WISE:

                id = db.insert(
                        DatabaseConstants.TableNames.TABLE_USER_FEEDBACK_MASTER,
                        null, values);

                break;

            case STORE_GEO_TAG_RESPONSE:

                id = db.insert(
                        DatabaseConstants.TableNames.TABLE_STORE_GEO_TAG_RESPONSE,
                        null, values);

                break;

            case MOM_DETAIL_MASTER:
                id = db.insert(DatabaseConstants.TableNames.TABLE_MOM_DETAILS_MASTER, null, values);
                break;

            case EMS_EXPENSE_DETAIL:

                id = db.insert(DatabaseConstants.TableNames.TABLE_EMS_EXPENSE_DETAIL, null, values);

                break;

            case EMS_BILL_DETAIL:
                id = db.insert(DatabaseConstants.TableNames.TABLE_EMS_BILL_DETAIL, null, values);
                break;



        }

        return getUriForId(id, uri);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numInserted = 0;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();

        switch (URI_MATCHER.match(uri)) {

            case EOL_ORDER_BOOKING_RESPONSE_MASTER:

                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_EOL_ORDER_BOOKING_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case EOL_SCHEME_HEADER:
                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_EOL_SCHEME_HEADER_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case EOL_SCHEME_DETAIL:
                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_EOL_SCHEME_DETAILS_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case COMPETITOR_PRODUCT_GROUP_MAPPING:
                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_COMPETITOR_PRODUCT_GROUP_MAPPING,
                                DatabaseConstants.CompetitorProductGroupMappingTableColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case PLANOGRAM_PRODUCT:
                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }
                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_MASTER,
                                DatabaseConstants.PlanogramProductMasterTableColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case PALNOGRAM_CLASS:

                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_PLANOGRAM_CLASS_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_PLANOGRAM_CLASS_MASTER,
                                DatabaseConstants.PlanogramClassMasterTableColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case COMP_PRODUCT_GROUP:
                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_COMPETETION_PRODUCT_GROUP,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_COMPETETION_PRODUCT_GROUP,
                                DatabaseConstants.CompetetionProductGroupColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case COMPETITORS:
                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_COMPETITOR_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_COMPETITOR_MASTER,
                                CompetitorColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case PAYMENT_MODES:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_PAYMENT_MODE_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_PAYMENT_MODE_MASTER,
                                PaymentModeMasterTableColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case PRODUCTS:
                try {
                    for (ContentValues cv : values) {
                        long newID = db.insertWithOnConflict(
                                DatabaseConstants.TableNames.TABLE_PRODUCT_MASTER,
                                null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_PRODUCT_MASTER,
                                ProductMasterTableColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case RACE_POSM_RESPONSE:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_RACE_POSM_DATA_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case RACE_BRAND_CATEGORY_MAPPING:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_RACE_BRAND_CATEGORY_MAPPING_MASTER,
                                DatabaseConstants.RaceBrandCategoryMappingMasterColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case RACE_BRAND:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_RACE_BRAND_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_RACE_BRAND_MASTER,
                                DatabaseConstants.RaceBrandMasterColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case RACE_FIXTURE:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_RACE_FIXTURE_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_RACE_FIXTURE_MASTER,
                                DatabaseConstants.RaceFixtureMasterColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case RACE_POSM:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_RACE_POSM_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }
                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_RACE_POSM_MASTER,
                                DatabaseConstants.RacePosmMasterColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case RACE_PRODUCT_CATEGORY:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_CATEGORY_MASTER,
                                DatabaseConstants.RaceBrandProductCategoryMasterColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case RACE_POSM_PRODUCT_MAPPING:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_POSM_MAPPING_MASTER,
                                DatabaseConstants.RacePOSMProductMappingMasterColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case RACE_BRAND_PRODUCT:
                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                        System.out.println(newID);
                    }

                    try {
					/*
					 * db.delete(
					 * DatabaseConstants.TableNames.TABLE_RACE_BRAND_PRODUCT_MASTER
					 * , DatabaseConstants.RaceBrandProductMasterColumns.
					 * KEY_IS_DELETED + "= ?", new String[] { "1" });
					 */
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case STORES_BASICS:

                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER,
                                StoreBasicColulmns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;

                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e);
                } finally {
                    db.endTransaction();
                }

                break;

            case INSERT_SURVEY_QUESTIONS:

                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_MASTERS,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_MASTERS,
                                SurveyQuestionColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }

                break;

            case INSERT_SURVEY_QUESTION_OPTIONS:

                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_OPTION_MASTERS,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }
                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_SURVEY_QUESTION_OPTION_MASTERS,
                                SurveyQuestionOptionsColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }

                break;

            case INSERT_SURVEY_QUESTION_ANSWER_RESPONSE:

                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_QUESTION_ANSWER_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }

                break;
            case USER_MODULES:

                try {
                    for (ContentValues cv : values) {
                        try {
                            long newID = db
                                    .insertWithOnConflict(
                                            DatabaseConstants.TableNames.TABLE_USER_MODULE_MASTER,
                                            null, cv,
                                            SQLiteDatabase.CONFLICT_REPLACE);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_USER_MODULE_MASTER,
                                UserModuleTableColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }

                    db.setTransactionSuccessful();
                    numInserted = values.length;
                } finally {
                    db.endTransaction();

                }

                break;

            case STOCK_ESCALATION_ORDER_RESPONSE:

                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_ORDER_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }

                break;

            case COLLECTION_RESPONSE:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_COLLECTION_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case PLANOGRAM_PRODUCT_RESPONSE:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case PLANOGRAM_COMPITITOR_RESPONSE:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            case COUNTER_SHARE_DISPLAY_SHARE_RESPONSE:
                try {
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;

            case FEEDBACK_STATUS:

                try {
                    // db.beginTransaction();
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_FEEDBACK_STATUS_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_FEEDBACK_STATUS_MASTER,
                                FeedbackStatusMasterColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    numInserted = values.length;
                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }

                } finally {
                    db.endTransaction();
                }

                break;

            case FEEDBACK_TEAMS:

                try {
                    // db.beginTransaction();
                    for (ContentValues cv : values) {
                        long newID = db.insertWithOnConflict(
                                DatabaseConstants.TableNames.TABLE_FMS_TEAM_MASTER,
                                null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_FMS_TEAM_MASTER,
                                TeamMasterColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }

                break;

            case FEEDBACK_CATEGORY:

                try {
                    // db.beginTransaction();
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_FEEDBACK_CATEGORY_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_FEEDBACK_CATEGORY_MASTER,
                                FeedbackCategoryMasterColumns.KEY_IS_DELETED
                                        + "= ?", new String[]{"1"});
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;
                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }

                break;

            case FEEDBACK_TYPE:

                try {
                    // db.beginTransaction();
                    for (ContentValues cv : values) {
                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_FEEDBACK_TYPE_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_FEEDBACK_TYPE_MASTER,
                                FeedbackTypeMasterColumns.KEY_IS_DELETED + "= ?",
                                new String[]{"1"});
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;
                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }

                break;

            case DOWNLOAD_DATA:

                try {
                    db.beginTransaction();
                    for (ContentValues cv : values) {
                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;
                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }

                break;

            case STORE_PERFORMANCE:

                try {

                    for (ContentValues cv : values) {

                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_STORE_PERFORMANCE_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }

                break;

            case MOM_ATTENDEES_MASTER:

                try {

                    for (ContentValues cv : values) {

                        long newID = db
                                .insert(DatabaseConstants.TableNames.TABLE_MOM_ATTENDEES_MASTER,
                                        null, cv);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }


                break;

            case LMS_LEAVE_LIST:

                try {

                    for (ContentValues cv : values) {

                        try {
                            long newID = db
                                    .insertWithOnConflict(TableNames.TABLE_LMS_LEAVE_MASTER,
                                            null, cv,SQLiteDatabase.CONFLICT_REPLACE);
                            if (newID <= 0) {
                                /*throw new SQLException("Failed to insert row into "
                                        + uri);*/
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.print(e);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }


                break;

            case LMS_LEAVE_DATE_LIST:

                try {

                    for (ContentValues cv : values) {

                        try {
                            long newID = db
                                    .insertWithOnConflict(TableNames.TABLE_LMS_LEAVE_DATE_MASTER,
                                            null, cv,SQLiteDatabase.CONFLICT_REPLACE);
                            if (newID <= 0) {
                                /*throw new SQLException("Failed to insert row into "
                                        + uri);*/
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.print(e);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }


                break;
            case LMS_LEAVE_STATUS_LOG:

                try {

                    for (ContentValues cv : values) {

                        try {
                            long newID = db
                                    .insertWithOnConflict(TableNames.TABLE_LMS_STATUS_LOG,
                                            null, cv,SQLiteDatabase.CONFLICT_REPLACE);
                            if (newID <= 0) {
                               /* throw new SQLException("Failed to insert row into "
                                        + uri);*/
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.print(e);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }


                break;


          case LMS_LEAVE_TYPE:

                try {

                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(TableNames.TABLE_LMS_LEAVE_TYPE_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }


                break;
            case LMS_LEAVE_CONFIGURATION:

                try {

                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(TableNames.TABLE_LMS_LEAVE_TYPE_CONFIGURATION,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                        if (newID <= 0) {
                            throw new SQLException("Failed to insert row into "
                                    + uri);
                        }
                    }
                    db.setTransactionSuccessful();

                    numInserted = values.length;

                    if (values.length > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } finally {
                    db.endTransaction();
                }


                break;

            case EXPENSE_TYPE_MASTER:

                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_EXPENSE_TYPE_MASTER,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_EXPENSE_TYPE_MASTER,
                                DatabaseConstants.ExpenseTypeMasterTableColumns.KEY_EXPENSE_TYPE_MASTER_IS_DELETED
                                        + "= ?", new String[] { "1" });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }

                break;

            case EMS_BILL_DOCUMENT_DETAIL:

                try {
                    for (ContentValues cv : values) {

                        long newID = db
                                .insertWithOnConflict(
                                        DatabaseConstants.TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL,
                                        null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                    }

                    try {
                        db.delete(
                                DatabaseConstants.TableNames.TABLE_EMS_BILL_DOCUMENT_DETAIL,
                                DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_IS_DELETED
                                        + "= ?"  + " AND "
                                        + DatabaseConstants.EMSBillDocumentDetailTableColumns.KEY_EMS_BILL_DOCUMENT_DETAIL_ID_SERVER + " = ?", new String[] { "1","0" });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }

                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported URI for insertion: " + uri);

        }

        return numInserted;
    }

    @Override
    public ContentProviderResult[] applyBatch(
            ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {

        return super.applyBatch(operations);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deleteCount = -1;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {

            case STORE_MODULE_MASTER:
                try {
                    deleteCount = db.delete(
                            DatabaseConstants.TableNames.TABLE_STORE_MODULE_MASTER,
                            selection, selectionArgs);
                } catch (Exception e) {
                }
                break;

            case RACE_PROUDCT_AUDIT_RESPONSE:
                try {
                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER,
                                    selection, selectionArgs);
                } catch (Exception e) {
                }
                break;
            case RACE_POSM_RESPONSE:
                try {
                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_RACE_POSM_DATA_RESPONSE_MASTER,
                                    selection, selectionArgs);
                } catch (Exception e) {
                }
                break;
            case ACTIVITY_DATA_RESPONSE:
                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_ACTIVITY_DATA_MASTER,
                                    selection, selectionArgs);

                } catch (Exception e) {
                    // TODO: handle exception
                }

                break;

            case DELETE_SURVEY_QUESTION_ANSWER_RESPONSE:

                try {
                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_QUESTION_ANSWER_RESPONSE_MASTER,
                                    selection, selectionArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case DELETE_OTHER_STORE:

                try {

                    deleteCount = db.delete(
                            DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER,
                            selection, selectionArgs);

                    System.out.println(deleteCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case DELETE_STOCK_ESCALATION_RESPONSE:

                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_ORDER_RESPONSE_MASTER,
                                    selection, selectionArgs);

                    System.out.println(deleteCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case COLLECTION_RESPONSE:

                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_COLLECTION_RESPONSE_MASTER,
                                    selection, selectionArgs);

                    System.out.println(deleteCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PLANOGRAM_RESPONSE:
                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_PLANOGRAM_RESPONSE_MASTER,
                                    selection, selectionArgs);

                    System.out.println(deleteCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PLANOGRAM_PRODUCT_RESPONSE:
                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_PLANOGRAM_PRODUCT_RESPONSE_MASTER,
                                    selection, selectionArgs);

                    System.out.println(deleteCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PLANOGRAM_COMPITITOR_RESPONSE:
                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_PLANOGRAM_COMPITIOR_RESPONSE_MASTER,
                                    selection, selectionArgs);

                    System.out.println(deleteCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case COUNTER_SHARE_DISPLAY_SHARE_RESPONSE:
                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER,
                                    selection, selectionArgs);

                    System.out.println(deleteCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case USER_FEEDBACK_STORE_WISE:

                try {

                    deleteCount = db
                            .delete(DatabaseConstants.TableNames.TABLE_USER_FEEDBACK_MASTER,
                                    selection, selectionArgs);

                    // Notify to loader/content resolver
                    getContext().getContentResolver().notifyChange(uri, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported URI for insertion: " + uri);
        }

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int updateCount = -1;

        if (URI_MATCHER.match(uri) != USER_PROFILE) {

        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {

            case STORE_MODULE_MASTER:
                try {
                    updateCount = db.update(
                            DatabaseConstants.TableNames.TABLE_STORE_MODULE_MASTER,
                            values, selection, selectionArgs);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                break;

            case DOWNLOAD_DATA_SINGLE_SERVICE:
                try {
                    updateCount = db
                            .update(DatabaseConstants.TableNames.TABLE_DOWNLOAD_DATA_MASTER,
                                    values, selection, selectionArgs);

                    getContext().getContentResolver().notifyChange(uri, null);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                break;

            case ACTIVITY_DATA_RESPONSE:

                try {
                    updateCount = db
                            .update(DatabaseConstants.TableNames.TABLE_ACTIVITY_DATA_MASTER,
                                    values, selection, selectionArgs);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case RACE_PRODUCT_AUDIT_RESPONSE_BY_AUDIT_ID:
                try {
                    updateCount = db
                            .update(DatabaseConstants.TableNames.TABLE_RACE_PRODUCT_AUDIT_RESPONSE_MASTER,
                                    values, selection, selectionArgs);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                break;

            case USER_PROFILE:

                try {
                    updateCount = db.update(
                            DatabaseConstants.TableNames.TABLE_USER_PROFILE,
                            values, selection, selectionArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case STORES_BASICS:

                updateCount = db.update(
                        DatabaseConstants.TableNames.TABLE_STORE_BASIC_MASTER,
                        values, selection, selectionArgs);
                break;

            case DOWNLOAD_DATA:

                db.execSQL(getUpdateDownloadDataQuery(selection));

                break;

            case STORE_GEO_TAG_RESPONSE:

                updateCount = db.update(
                        DatabaseConstants.TableNames.TABLE_STORE_GEO_TAG_RESPONSE,
                        values, selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI for insertion: " + uri);

        }

        return updateCount;

    }

    private Uri getUriForId(long id, Uri uri) {

        if (id > 0) {

            getContext().getContentResolver().notifyChange(uri, null);

            Uri itemUri = ContentUris.withAppendedId(uri, id);
            {
                // notify all listeners of changes and return itemUri:
                // getContext().getContentResolver().notifyChange(itemUri,
                // null);

                return itemUri;
            }

        }
        // s.th. went wrong:
        throw new SQLException("Problem while inserting into uri: " + uri);
    }

    private String getUpdateDownloadDataQuery(String downloadDataCodeString) {

        ArrayList<String> arr_module_data_code = new ArrayList<String>();

        String downloadDataCodeArray[] = downloadDataCodeString.split(",");

        for (String dataCode : downloadDataCodeArray)
            arr_module_data_code.add("'" + dataCode + "'");

        if (arr_module_data_code.contains("'"
                + DownloadMasterCodes.PLANOGRAM_PRODUCT + "'"))
            arr_module_data_code.add("'" + DownloadMasterCodes.PLANOGRAM_CLASS
                    + "'");

        if (arr_module_data_code.contains("'"
                + DownloadMasterCodes.RACE_POSM_MASTER + "'")) {
            arr_module_data_code.add("'"
                    + DownloadMasterCodes.RACE_FIXTURE_MASTER + "'");
            arr_module_data_code.add("'"
                    + DownloadMasterCodes.RACE_PRODUCT_CATEGORY + "'");
            arr_module_data_code.add("'"
                    + DownloadMasterCodes.RACE_POSM_PRODUCT_MAPPING + "'");
            arr_module_data_code.add("'"
                    + DownloadMasterCodes.RACE_BRAND_MASTER + "'");
            arr_module_data_code.add("'"
                    + DownloadMasterCodes.RACE_BRAND_CATEGORY_MAPPING + "'");

            arr_module_data_code.add("'" + DownloadMasterCodes.RACE_PRODUCT
                    + "'");
        }

        if (arr_module_data_code.contains("'"
                + DownloadMasterCodes.MSS_CATEGORY + "'")) {
            arr_module_data_code.add("'" + DownloadMasterCodes.MSS_TEAM + "'");
            arr_module_data_code.add("'" + DownloadMasterCodes.MSS_TYPE + "'");
            arr_module_data_code
                    .add("'" + DownloadMasterCodes.MSS_STATUS + "'");
        }

        String module_data_codes = TextUtils.join(",", arr_module_data_code);

        String query = "UPDATE "
                + TableNames.TABLE_DOWNLOAD_DATA_MASTER
                + " SET "
                + DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_IS_DOWNLOAD_NEEDED
                + " = " + 1 + " WHERE "
                + DownloadDataMasterColumns.KEY_DOWNLOAD_DATA_MASTER_DATA_CODE
                + " IN(" + module_data_codes + ")";

        return query;

    }

    private String getSurveyQuestionQuery() {
        String query = "SELECT sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_MODULE_CODE
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_MODULE_ID
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_TYPE_ID
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_TEXT_LENGTH
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_IMAGE
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATET_MAX_TIMES
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TEXT
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_REPEATER_TYPE_ID
                + ", "
                + "sq."
                + SurveyQuestionColumns.KEY_IS_MENDATORY
                + ", "
                + "so."
                + SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_VALUE
                + ", "
                + "so."
                + SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE
                + " AS SequenceOption, "
                + "IFNULL(so."
                + SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID
                + ",0) AS "
                + SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_ID
                + ", "
                + "so."
                + SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_OPTION_IS_AFFIRMATIVE
                + ", "
                + "qarm."
                + QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_USER_RESPONSE
                + " "
                + "FROM "
                + TableNames.TABLE_SURVEY_QUESTION_MASTERS
                + " sq "
                + "LEFT JOIN "
                + TableNames.TABLE_SURVEY_QUESTION_OPTION_MASTERS
                + " so "
                + "ON "
                + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID
                + " = so."
                + SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_ID
                + " "
                + "LEFT JOIN "
                + ""
                + TableNames.TABLE_QUESTION_ANSWER_RESPONSE_MASTER
                + " qarm "
                + "ON qarm."
                + QuestionAnswerResponseColumns.KEY_SURVEY_QUESTION_ID
                + " = sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID
                + "  AND qarm."
                + QuestionAnswerResponseColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " = " + "? " + " WHERE " + "sq."
                + SurveyQuestionColumns.KEY_MODULE_ID + "=" + "?" + " "
                + "AND " + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_DEPENDENT_OPTION_ID
                + " = " + "?" + " " + "ORDER BY " + "sq."
                + SurveyQuestionColumns.KEY_SURVEY_QUESTION_SEQUENCE + " ,"
                + "so."
                + SurveyQuestionOptionsColumns.KEY_SURVEY_QUESTION_SEQUENCE
                + "," + "sq." + SurveyQuestionColumns.KEY_SURVEY_QUESTION_ID
                + "";
        return query;
    }

    private String getCounterShareDisplayShareResposeQuery() {
        String selectQuery = "SELECT csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_USER_ROLE_ID
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_ID
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_PRODUCT_GROUP_ID
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_SURVEY_QUESTION_USER_RESPONSE
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_BEAT_STORE_ID
                + ",ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_USER_ID
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_COMETITOR_ID
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID
                + ",csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID
                + " FROM "
                + TableNames.TABLE_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_MASTER
                + " csds INNER JOIN "
                + TableNames.TABLE_ACTIVITY_DATA_MASTER
                + " ad on ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " = csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + "  WHERE ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + "= ?  AND csds."
                + CounterShareDisplayShareResponseMasterColumns.KEY_COUNTER_SHARE_DISPLAY_SHARE_RESPONSE_COMPETITION_TYPE
                + " = ? ";

        return selectQuery;
    }

    private String getUserModuleQuerry() {
        String query = "SELECT UM."
                + UserModuleTableColumns.KEY_MODULE_ICON_NAME
                + ",UM."
                + UserModuleTableColumns.KEY_IS_MENDATORY
                + ",UM."
                + UserModuleTableColumns.KEY_MODULE_IS_QUESTION_MODULE
                + ",UM."
                + UserModuleTableColumns.KEY_MODULE_IS_STORE_WISE
                + ",UM."
                + UserModuleTableColumns.KEY_MODULE_CODE
                + ",UM."
                + UserModuleTableColumns.KEY_MODULE_ID
                + ",UM."
                + UserModuleTableColumns.KEY_NAME
                + ",UM."
                + UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID
                + ",UM."
                + UserModuleTableColumns.KEY_MODULE_SEQUENCE
                + ",ADM."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + ",ADM."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS
                + " FROM "
                + TableNames.TABLE_USER_MODULE_MASTER
                + " UM LEFT JOIN "
                + TableNames.TABLE_ACTIVITY_DATA_MASTER
                + " ADM ON UM."
                + UserModuleTableColumns.KEY_MODULE_CODE
                + "=ADM."
                + ActivityDataMasterColumns.KEY_MODULE_CODE
                + " AND ADM."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS
                + "!=" + SyncUtils.SYNC_STATUS_SYNC_COMPLETED + " WHERE UM."
                + UserModuleTableColumns.KEY_MODULE_PARENT_MODULE_ID + " = "
                + "? " + " ORDER BY " + "UM."
                + UserModuleTableColumns.KEY_MODULE_SEQUENCE + " ASC " + ",UM."
                + UserModuleTableColumns.KEY_NAME + " ASC ;";
        return query;
    }

    private String getStoreCountQuerry() {

        String query = "SELECT COUNT( DISTINCT "
                + ActivityDataMasterColumns.KEY_BEAT_STORE_ID
                + ") FROM "
                + TableNames.TABLE_ACTIVITY_DATA_MASTER
                + " WHERE "
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS
                + " = " + SyncUtils.SYNC_STATUS_SUBMIT;

        return query;
    }

    private String getProductCatagoryP1() {
        String selectQuery = "SELECT DISTINCT "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_NAME
                + ", "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID
                + ", "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE
                + " FROM " + TableNames.TABLE_PRODUCT_MASTER;

        return selectQuery;
    }

    private String getProductCatagoryP2() {

        String selectQuery = "SELECT DISTINCT "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_CODE
                + ", "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_NAME
                + ", "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID
                + " FROM "
                + TableNames.TABLE_PRODUCT_MASTER
                + " WHERE "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_CODE
                + "= " + " ? ";

        return selectQuery;
    }

    private String getOrderResponse() {

        String selectQuery = "SELECT orm."
                + OrderResponseMasterTableColumns.KEY_USER_ROLE_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_ORDER_MASTER_QUANTITY
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDERNO
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_BEAT_STORE_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID
                + ",ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SURVEY_RESPONSE_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_USER_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_COVERAGE_ID
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_ORDER_MASTER_PRICE
                + ",orm."
                + OrderResponseMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE
                + " FROM "
                + TableNames.TABLE_ORDER_RESPONSE_MASTER
                + " orm INNER JOIN "
                + TableNames.TABLE_ACTIVITY_DATA_MASTER
                + " ad ON orm."
                + OrderResponseMasterTableColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " = ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " WHERE ad."
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + "= "
                + " ? "
                + " AND orm."
                + OrderResponseMasterTableColumns.KEY_ORDER_MASTER_ORDER_BOOKING_TYPE
                + "=" + "  ?  ";

        return selectQuery;
    }

    private String getSKUProductListQuery() {

        String selectQuery = "SELECT "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_ID
                + ", "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE
                + ", "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_DEALER_PRICE
                + ","
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_CAEGORY_ID
                + ","
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_TYPE_ID
                + ","
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_ID
                + " FROM "
                + TableNames.TABLE_PRODUCT_MASTER
                + " WHERE "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_PRODUCT_GROUP_CODE
                + "=" + " ? " + " ORDER BY "
                + ProductMasterTableColumns.KEY_PRODUCT_MASTER_SKU_CODE;

        return selectQuery;
    }

    private String getOtherStoresDistinctCitiesQuesry() {
        String query = "SELECT DISTINCT " + StoreBasicColulmns.KEY_BEAT_CITY
                + " FROM " + TableNames.TABLE_STORE_BASIC_MASTER + " WHERE "
                + StoreBasicColulmns.KEY_BEAT_TYPE + "='"
                + Constants.STORE_TYPE_OTHER + "'";
        return query;
    }

    private String getUserFeedbackQuery() {
        String selectQuery = "select fcm."
                + FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_NAME
                + ",ftm."
                + FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_NAME
                + ",tm."
                + TeamMasterColumns.KEY_TEAM_MASTER_NAME
                + ",ufm."
                + UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_REMARK
                + ",ufm."
                + UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID
                + ",ufm."
                + UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_IMAGE_PATH
                + " ,ufm."
                + UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_STORE_ID
                + ",ufm."
                + UserFeedbackMasterColumns._ID
                + " from  "
                + TableNames.TABLE_USER_FEEDBACK_MASTER
                + " ufm"
                + " inner join "
                + TableNames.TABLE_FEEDBACK_CATEGORY_MASTER
                + " fcm  on ufm."
                + UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID
                + " = fcm."
                + FeedbackCategoryMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_ID
                + " "
                + " inner join "
                + TableNames.TABLE_FEEDBACK_TYPE_MASTER
                + " ftm on ufm."
                + UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_TYPE_ID
                + "=ftm."
                + FeedbackTypeMasterColumns.KEY_FEEDBACK_TYPE_MASTER_ID
                + " "
                + " inner join "
                + TableNames.TABLE_FMS_TEAM_MASTER
                + " tm on ufm."
                + UserFeedbackMasterColumns.KEY_FEEDBACK_CATEGORY_MASTER_TEAM_ID
                + " = tm." + TeamMasterColumns.KEY_TEAM_MASTER_ID + " "
                + " WHERE ufm."
                + UserFeedbackMasterColumns.KEY_USER_FEEDBACK_MASTER_STORE_ID
                + "=?;";
        return selectQuery;

    }

    private String getGeoTagResponseQuery() {
        // TODO Auto-generated method stub
        String selectQuery = "SELECT sgm."
                + StoreGeoTagResponseMasterColumns.KEY_BEAT_STORE_ID
                + ",sgm."
                + StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LATITUDE
                + ", sgm."
                + StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_LONGITUDE
                + ", sgm."
                + StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_IMAGE
                + ", sgm."
                + StoreGeoTagResponseMasterColumns.KEY_STORE_GEO_TAG_GEO_USER_OPTION
                + ", sbm."
                + StoreBasicColulmns.KEY_BEAT_STORE_NAME
                + " FROM "
                + TableNames.TABLE_STORE_GEO_TAG_RESPONSE
                + " sgm"
                + " INNER JOIN "
                + TableNames.TABLE_STORE_BASIC_MASTER
                + " sbm ON  sgm."
                + StoreBasicColulmns.KEY_BEAT_STORE_ID
                + " =  sbm."
                + StoreBasicColulmns.KEY_BEAT_STORE_ID
                + " WHERE sgm."
                + StoreGeoTagResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + "= ?";

        return selectQuery;
    }

    private String getActivityDataModuleQuerry() {
        // TODO Auto-generated method stub

        String selectQuery = "SELECT "
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
                + " FROM "
                + DatabaseConstants.TableNames.TABLE_ACTIVITY_DATA_MASTER
                + " WHERE "
                + ActivityDataMasterColumns.KEY_BEAT_STORE_ID
                + " = ?"
                + " AND "
                + ActivityDataMasterColumns.KEY_MODULE_CODE
                + " = ?"
                + " AND "
                + ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_SYNC_STATUS
                + " <> " + SyncUtils.SYNC_STATUS_SYNC_COMPLETED;

        return selectQuery;

    }

    public String getIsStorePerformanceEmptyQuery() {
        String query = "SELECT  EXISTS (SELECT  1 FROM "
                + TableNames.TABLE_STORE_PERFORMANCE_MASTER + "  WHERE "
                + StorePerformaceColumns.KEY_BEAT_STORE_ID + "=?  limit 1)";
        return query;
    }

    public static String getQueryStorePerformanceSection1() {
        String query = "select ProductType,transactiontype, sum(MTDUnit) as MTDUnit ,sum(MTDvalue) as MTDvalue,"
                + "sum(LMTDUnit) as LMTDUnit ,sum(LMTDvalue) as LMTDvalue from "
                + "("
                + "select  ProductType,TransactionType,Volume as MTDUnit,value_k_ as MTDvalue ,0 as LMTDUnit,0 as LMTDvalue"
                + " from StorePerformanceMaster"
                + " where StoreID=? and producttype in('AC','AV','HA') and transactiontype in('Sell In','Sell Out') and salestype='MTD'"
                + "union"
                + " select  producttype,transactiontype,0 as MTDUnit,0 as MTDvalue ,Volume as LMTDUnit,value_k_ as LMTDvalue"
                + " from StorePerformanceMaster "
                + " where StoreID=? and producttype in('AC','AV','HA') and transactiontype in('Sell In','Sell Out') and salestype='LMTD'"
                + ")  group by producttype,transactiontype";

        return query;

    }

    public static String getQueryStorePerformanceSection2() {
        String query = "select Case TransactionType when 'Sell In' Then 'Total Sell In' "
                + "When 'Sell Out' Then 'Total Sell Out'"
                + " Else TransactionType END as TransactionType, sum(MTDUnit) as MTDUnit ,sum(MTDvalue) as MTDvalue,sum(LMTDUnit) as LMTDUnit ,sum(LMTDvalue) as LMTDvalue"
                + " from "
                + "(select  TransactionType,Volume as MTDUnit,Value_K_ as MTDvalue ,0 as LMTDUnit,0 as LMTDvalue from StorePerformanceMaster"
                + " where storeID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell In','Sell Out') and SalesType='MTD'"
                + " union"
                + " select  TransactionType,0 as MTDUnit,0 as MTDvalue ,Volume as LMTDUnit,Value_K_ as LMTDvalue from StorePerformanceMaster"
                + " where StoreID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell In','Sell Out') and SalesType='LMTD'"

                + " union"
                + " select  TransactionType,0 as MTDUnit,0 as MTDvalue ,Volume as LMTDUnit,Value_K_ as LMTDvalue from StorePerformanceMaster"
                + " where StoreID=? and TransactionType in('SPC Target','Store Ach%','Store Target','SPC Ach%') and SalesType='LMTD'"
                + " UNION"
                + " select  TransactionType,Volume as MTDUnit,Value_K_ as MTDvalue ,0 as LMTDUnit,0 as LMTDvalue from StorePerformanceMaster"
                + " where StoreID=? and TransactionType in('SPC Target','Store Ach%','Store Target','SPC Ach%') and SalesType='MTD' "
                + ") "
                + " group by TransactionType "
                + " ORDER by TransactionType DESC";

        return query;

    }

    public static String getQueryStorePerformanceSection4() {
        String query = " select ProductGroup , sum(MTDUnit) as MTDUnit ,sum(MTDvalue) as MTDvalue,sum(LMTDUnit) as LMTDUnit ,sum(LMTDvalue) as LMTDvalue"
                + " from"
                + "(select  ProductGroup ,Volume as MTDUnit,Value_K_ as MTDvalue ,0 as LMTDUnit,0 as LMTDvalue from StorePerformanceMaster"
                + " where storeID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell Out') and SalesType='MTD'"
                + " union"
                + " select  ProductGroup,0 as MTDUnit,0 as MTDvalue ,Volume as LMTDUnit,Value_K_ as LMTDvalue from StorePerformanceMaster"
                + " where StoreID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell Out') and SalesType='LMTD'"
                + " UNION"
                + " select  ' Total' as ProductGroup,Volume as MTDUnit,Value_K_ as MTDvalue ,0 as LMTDUnit,0 as LMTDvalue from StorePerformanceMaster"
                + " where storeID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell Out') and SalesType='MTD'"
                + " union"
                + " select  ' Total' as ProductGroup,0 as MTDUnit,0 as MTDvalue ,Volume as LMTDUnit,Value_k_ as LMTDvalue from StorePerformanceMaster"
                + " where storeID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell Out') and SalesType='LMTD'"
                + ")"
                + " group by ProductGroup"
                + " ORDER by ProductGroup DESC";

        return query;

    }

    public static String getQueryStorePerformanceSection3() {
        String query = " select ProductGroup , sum(MTDUnit) as MTDUnit ,sum(MTDvalue) as MTDvalue,sum(LMTDUnit) as LMTDUnit ,sum(LMTDvalue) as LMTDvalue"
                + " from"
                + "(select  ProductGroup ,Volume as MTDUnit,Value_K_ as MTDvalue ,0 as LMTDUnit,0 as LMTDvalue from StorePerformanceMaster"
                + " where storeID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell In') and SalesType='MTD'"
                + " union"
                + " select  ProductGroup,0 as MTDUnit,0 as MTDvalue ,Volume as LMTDUnit,Value_K_ as LMTDvalue from StorePerformanceMaster"
                + " where StoreID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell In') and SalesType='LMTD'"
                + " UNION"
                + " select  ' Total' as ProductGroup,Volume as MTDUnit,Value_K_ as MTDvalue ,0 as LMTDUnit,0 as LMTDvalue from StorePerformanceMaster"
                + " where storeID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell In') and SalesType='MTD'"
                + " union"
                + " select  ' Total' as ProductGroup,0 as MTDUnit,0 as MTDvalue ,Volume as LMTDUnit,Value_k_ as LMTDvalue from StorePerformanceMaster"
                + " where storeID=? and ProductType in('AC','AV','HA') and TransactionType in('Sell In') and SalesType='LMTD'"
                + ")"
                + " group by ProductGroup"
                + " ORDER by ProductGroup DESC";

        return query;

    }
}
