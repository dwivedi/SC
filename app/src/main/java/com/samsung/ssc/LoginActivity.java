package com.samsung.ssc;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonParseException;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.dto.LoginResponse;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.io.VolleyGetDataCallBack;
import com.samsung.ssc.io.VolleyPostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.sync.SyncStatusNotificationModel;
import com.samsung.ssc.util.Constants;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.PasswordValidator;
import com.samsung.ssc.util.updateApp.UpdateApp;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    protected static final String TAG = "Smartdost";
    private EditText etPinText;
    private String mDeviceId;
    private double mLongitude = 0, mLatitude = 0;

    SyncStatusNotificationModel syscStatusModel;
    private LoginResponse mLoginResponse;
    private ImageView ivShowPassword;

    // The authority for the sync adapter's content provider

    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES
            * SECONDS_PER_MINUTE;

    protected boolean isPasswordShoing = false;
    private Button loginButton;
    private TextView tvForgotPin, tvCallUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setScreenShotOff(this);

        //setContentView(R.layout.activity_activity1);
        setContentView(R.layout.activity_activity1_ssc);

        setUpView();

        Helper.saveBoolValueInPrefs(getApplicationContext(),
                Constants.IS_SESSION_LOGOUT, false);

    }

    private void setUpView() {

        loginButton = (Button) this
                .findViewById(R.id.btnLoginSubmit);
        // loginButton.setProgress(0);
        // progressGenerator.setDefultValue(loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onLoginButtonClick(v);
                disableWidgetWhileLogin();

                // loginButton.setProgress(10);
            }
        });

        // RELEASE_CHECK hard coded IMEI should not be present
        mDeviceId = ((TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

        if (mDeviceId == null) {
            return;
        }

        mDeviceId = "355506060000007";  // 8
        //	mDeviceId = "355506060001963";  // 1964
        //	mDeviceId = "355506060000442";  // 443
        //	mDeviceId = "355506060000596";  // 597
        // mDeviceId = "355506060001300";  // 1301
        //	mDeviceId = "355506060001370";  // 1371


        etPinText = (EditText) this.findViewById(R.id.etLoginPin);

        etPinText
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {

                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                                || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            onLoginButtonClick(v);
                        }
                        return false;
                    }
                });

        etPinText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (ivShowPassword.getVisibility() == View.GONE) {
                    ivShowPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivShowPassword = (ImageView) findViewById(R.id.ivShowPassword);

        tvForgotPin = (TextView) this.findViewById(R.id.tvLoginForgetPassword);

        tvCallUs = (TextView) this.findViewById(R.id.tvCallUs);

        if (!Helper.getBoolValueFromPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_INSTALL_FIRST_TIME)) {

            SpannableString content = new SpannableString(
                    getText(R.string.registerPin));

	/*		content.setSpan(new UnderlineSpan(), 0,
                    getText(R.string.registerPin).length(), 0);*/

            tvForgotPin.setText(content);

        } else {

            SpannableString content = new SpannableString(
                    getText(R.string.forgetpin));

			/*content.setSpan(new UnderlineSpan(), 0, getText(R.string.forgetpin)
					.length(), 0);*/

            tvForgotPin.setText(content);
        }

        String version = Helper.getAppVersion(this);
        TextView mVersionName = (TextView) this
                .findViewById(R.id.tvLoginVersion);
        mVersionName.setText("Version " + version);
    }

    public void onLoginButtonClick(final View v) {

        hideKeyboard();

        cancelSync();

        Helper.changeSyncFlag(false);

        try {
            if (isOnline(v.getContext())) {
                if (etPinText.getText().toString().trim().length() != 0) {
                    JSONObject arguments = null;
                    try {

                        arguments = new JSONObject();
                        JSONObject params = new JSONObject();

                        params.put(WebConfig.WebParams.APK_VERSION,
                                Helper.getAppVersion(LoginActivity.this));
                        params.put(WebConfig.WebParams.PASSWORD, etPinText
                                .getText().toString());
                        params.put(WebConfig.WebParams.IMEI, mDeviceId);
                        params.put(WebConfig.WebParams.LATTITUDE, mLatitude);
                        params.put(WebConfig.WebParams.LONGITUDE, mLongitude);

                        String modelName = Helper.getDeviceModelName();
                        String ipAddress = Helper.getLocalIpAddress();

                        if (Helper.isDeviceRooted()) {

                            if (modelName != null) {
                                modelName = modelName + "_Rooted";
                            } else {
                                modelName = "_Rooted";
                            }
                        }
                        params.put(WebConfig.WebParams.MODEL_NAME,
                                modelName != null ? modelName : "");

                        params.put(WebConfig.WebParams.IP_ADDRESS,
                                ipAddress != null ? ipAddress : "");

                        params.put(WebConfig.WebParams.BROWSER_NAME, "");

                        params.put(WebConfig.WebParams.REQUIRE_ANNOUNCEMENT,
                                Helper.getAnnouncement(getApplicationContext())
                                        .equals("") ? true : false);

                        arguments.put(WebConfig.WebParams.SIDTO, params);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    PostDataToNetwork pdn = new PostDataToNetwork(
                            v.getContext(), getString(R.string.loadingmessage),
                            new GetDataCallBack() {

                                @Override
                                public void processResponse(Object result) {

                                    if (result != null) {
                                        parseResponse(v, result);

                                    } else {

                                        enableWidgetsOnLoginFailed();

                                    }
                                }
                            });
                    pdn.setConfig(getString(R.string.url),
                            WebConfig.WebMethod.LOGIN);
                    pdn.execute(arguments);

                } else {

                    ivShowPassword.setVisibility(View.GONE);

                    Animation anim = AnimationUtils.loadAnimation(this,
                            R.anim.shake);

                    anim.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            enableWidgetsOnLoginFailed();
                            etPinText
                                    .setError(getString(R.string.please_enter_password));

                            // loginButton.setProgress(0);

                        }
                    });
                    etPinText.startAnimation(anim);

                }
            } else {

                enableWidgetsOnLoginFailed();
                // loginButton.setProgress(0);

                Helper.showAlertDialog(
                        this,
                        SSCAlertDialog.ERROR_TYPE,
                        getString(R.string.error1),
                        getString(R.string.error2),
                        getString(R.string.ok),
                        new SSCAlertDialog.OnSDAlertDialogClickListener() {

                            @Override
                            public void onClick(
                                    SSCAlertDialog sdAlertDialog) {
                                sdAlertDialog.dismiss();
                                etPinText.setText("");
                                enableWidgetsOnLoginFailed();
                                // loginButton.setProgress(0);

                            }
                        }, false, null, null);

            }
        } catch (Exception e) {
            e.printStackTrace();
            enableWidgetsOnLoginFailed();
        }

    }

    protected void showUpdateAppDialog(final LoginResponse mLoginResponse) {


        try {
            Helper.showConfirmationDialog1(this, getString(R.string.error10),
                    getString(R.string.apk_update_msg), getString(R.string.ok),
                    getString(R.string.no),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                Intent intent = new Intent(LoginActivity.this,
                                        UpdateApp.class);
                                intent.putExtra("url",
                                        mLoginResponse.getApkURL());
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                Helper.printStackTrace(e);
                            }
                        }
                    }, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showAnnouncementData(mLoginResponse);
                            dialog.dismiss();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAnnouncementData(final LoginResponse mLoginResponse) {
        Helper.saveLongValueInPref(getApplicationContext(),
                SharedPreferencesKey.CURRENT_INTERACTION_TIME,
                System.currentTimeMillis());


        Intent intent = new Intent(this, Announcement.class);
        Bundle b = new Bundle();
        b.putSerializable(IntentKey.LOGIN_DATA, mLoginResponse);
        intent.putExtras(b);
        startActivity(intent);

        // loginButton.setProgress(0);

        enableWidgetsOnLoginFailed();

    }

    private void parseResponse(final View v, Object result) {
        try {

            if (result != null) {
                JSONObject rootJsonObject = new JSONObject(result.toString());
                boolean isSuccess = rootJsonObject.getBoolean("IsSuccess");
                if (isSuccess) {
                    // loginButton.setProgress(100);
                    JSONObject responseJson = rootJsonObject
                            .getJSONObject("SingleResult");

                    // fill value to data modal
                    mLoginResponse = parserLogingDataModal(responseJson);
                    Helper.ragisterGCM(LoginActivity.this);
                    saveDataPreferences(mLoginResponse);

                    if (mLoginResponse.isApkUpdated()) {

                        startSync();

                        showAnnouncementData(mLoginResponse);

                    } else {
                        showUpdateAppDialog(mLoginResponse);
                    }

                } else {

                    enableWidgetsOnLoginFailed();
                    // loginButton.setProgress(0);
                    String errorMessage = rootJsonObject.getString("Message");
                    Helper.showAlertDialog(
                            this,
                            SSCAlertDialog.ERROR_TYPE,
                            getString(R.string.error3),
                            errorMessage,
                            getString(R.string.ok),
                            new SSCAlertDialog.OnSDAlertDialogClickListener() {

                                @Override
                                public void onClick(
                                        SSCAlertDialog sdAlertDialog) {
                                    sdAlertDialog.dismiss();
                                    etPinText.setText("");
                                }
                            }, false, null, null);
                }
            }

        } catch (JsonParseException e) {
            Toast.makeText(getApplicationContext(),
                    R.string.server_data_not_in_json, Toast.LENGTH_SHORT)
                    .show();
            // loginButton.setProgress(-1);
            enableWidgetsOnLoginFailed();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LoginResponse parserLogingDataModal(JSONObject responseJson)
            throws JSONException {

        mLoginResponse = new LoginResponse();

        mLoginResponse.setApiKey(responseJson.isNull("APIKey") ? ""
                : responseJson.getString("APIKey"));
        mLoginResponse.setApiToken(responseJson.isNull("APIToken") ? ""
                : responseJson.getString("APIToken"));
        mLoginResponse.setApkURL(responseJson.isNull("APKURL") ? ""
                : responseJson.getString("APKURL"));
        mLoginResponse.setAnnouncementMessage(responseJson
                .isNull("AnnouncementMsg") ? "" : responseJson
                .getString("AnnouncementMsg"));
        mLoginResponse
                .setAttendanceType(responseJson.isNull("AttendanceType") ? ""
                        : responseJson.getString("AttendanceType"));
        mLoginResponse.setCompanyId(responseJson.getString("CompanyID"));
        mLoginResponse.setEmployeeCode(responseJson.isNull("EmplCode") ? ""
                : responseJson.getString("EmplCode"));
        mLoginResponse.setAttendanceMarked(responseJson
                .getBoolean("HasAttendance"));

        try {
            mLoginResponse.setAttendanceMandate(responseJson
                    .getBoolean("IsAttendanceMandate"));
        } catch (Exception e2) {
            mLoginResponse.setAttendanceMandate(true);
        }

        try {
            mLoginResponse.setGeoFencingApplicable(responseJson
                    .getBoolean("IsGeoFencingApplicable"));
        } catch (Exception e1) {
            mLoginResponse.setGeoFencingApplicable(false);
        }

        mLoginResponse.setGeoPhotoMandate(responseJson
                .getBoolean("IsGeoPhotoMandate"));
        mLoginResponse.setGeoTagMandate(responseJson
                .getBoolean("IsGeoTagMandate"));

        mLoginResponse.setRoleId(responseJson.getInt("RoleID"));

        mLoginResponse.setUserId(responseJson.getString("UserID"));

        mLoginResponse.setApkUpdated(responseJson.getBoolean("ApkUpdated"));
        mLoginResponse.setHasNewAnnouncment(responseJson
                .isNull("HasNewAnnouncment") ? false : responseJson
                .getBoolean("HasNewAnnouncment"));
        mLoginResponse
                .setOfflineAccess(responseJson.isNull("IsOfflineAccess") ? false
                        : responseJson.getBoolean("IsOfflineAccess"));
        mLoginResponse
                .setRaceProfile(responseJson.isNull("IsRaceProfile") ? false
                        : responseJson.getBoolean("IsRaceProfile"));
        mLoginResponse.setShowPerformanceTab(responseJson
                .isNull("ShowPerformanceTab") ? false : responseJson
                .getBoolean("ShowPerformanceTab"));
        mLoginResponse.setStoreProfileVisible(responseJson
                .getBoolean("IsStoreProfileVisible"));
        try {
            String downloadableModuleCodes = responseJson
                    .isNull("DownloadDataMasterCodesString") ? ""
                    : responseJson.getString("DownloadDataMasterCodesString");

            if (!downloadableModuleCodes.equalsIgnoreCase("")) {

                getContentResolver().update(ProviderContract.URI_DOWNLOAD_DATA,
                        null, downloadableModuleCodes, null);

            } else {
                String downloadableCodes = "A,B,C,D,E,F,H,I,J,K,L,M";

                getContentResolver().update(ProviderContract.URI_DOWNLOAD_DATA,
                        null, downloadableCodes, null);

            }
        } catch (Exception e) {

            String downloadableCodes = "A,B,C,D,E,F,H,I,J,K,L,M";

            getContentResolver().update(ProviderContract.URI_DOWNLOAD_DATA,
                    null, downloadableCodes, null);

        }

        mLoginResponse.setUserRoleID(responseJson.getInt("UserRoleID"));
        mLoginResponse.setFirstName(responseJson.isNull("FirstName") ? ""
                : responseJson.getString("FirstName"));
        mLoginResponse.setLastName(responseJson.isNull("LastName") ? ""
                : responseJson.getString("LastName"));
        mLoginResponse.setRoamingProfile(responseJson
                .getBoolean("IsRoamingProfile"));
        mLoginResponse.setMobileCalling(responseJson
                .getString("Mobile_Calling"));

        return mLoginResponse;
    }

    private void saveDataPreferences(LoginResponse mLoginResponse) {

        Helper.saveStringValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_EMPID,
                mLoginResponse.getEmployeeCode());
        Helper.saveStringValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_APIKEY, mLoginResponse.getApiKey());
        Helper.saveStringValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_APITOKEN,
                mLoginResponse.getApiToken());
        Helper.saveStringValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_COMPANYID,
                mLoginResponse.getCompanyId());
        Helper.saveStringValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_USERID, mLoginResponse.getUserId());
        Helper.saveIntgerValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_ROLEID, mLoginResponse.getRoleId());
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_INSTALL_FIRST_TIME, true);
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_GEO_TAG_MANDATORY,
                mLoginResponse.isGeoTagMandate());
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_GEO_PHOTO_MANDATORY,
                mLoginResponse.isGeoPhotoMandate());
        Helper.saveStringValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_ROLEIDSTOREWISE,
                String.valueOf(mLoginResponse.getUserRoleID()));
        Helper.saveStringValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_ROAMING_PROFILE,
                Boolean.toString(mLoginResponse.isRoamingProfile()));
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_STORE_PROFILE_VISIBLE,
                mLoginResponse.isStoreProfileVisible());
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_SHOW_PERFRMANCE_TAB,
                mLoginResponse.isShowPerformanceTab());
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_RACE_PROFILE,
                mLoginResponse.isRaceProfile());
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_OFFLINE_ACCESS,
                mLoginResponse.isOfflineAccess());
        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_APK_UPDATED,
                mLoginResponse.isApkUpdated());

        Helper.saveBoolValueInPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_IS_GEO_FENCING_APPLICABLE,
                mLoginResponse.isGeoFencingApplicable());

    }

    public void onForgetLinkClick(View v) {


        final Dialog mDialog;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            //	mDialog = new Dialog(v.getContext(), AlertDialog.THEME_HOLO_LIGHT);
            mDialog = new Dialog(v.getContext());
        } else {
            mDialog = new Dialog(v.getContext());
        }

        final PasswordValidator passValidator = new PasswordValidator();

        if (!Helper.getBoolValueFromPrefs(getApplicationContext(),
                SharedPreferencesKey.PREF_INSTALL_FIRST_TIME)) {
            mDialog.setTitle(getText(R.string.registerPin));
        } else {
            mDialog.setTitle(getText(R.string.reset_pin));
        }

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(R.layout.forgetpassword);

        TextView tvPasswordPloicy = (TextView) mDialog
                .findViewById(R.id.textViewPasswordPolicy);
        String udata = "Password Policy";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        tvPasswordPloicy.setText(content);

        ((Button) mDialog.findViewById(R.id.submit))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText etConfirmationId = (EditText) mDialog
                                .findViewById(R.id.confirmationid);
                        EditText etPin = (EditText) mDialog
                                .findViewById(R.id.newpassword);
                        EditText etRePin = (EditText) mDialog
                                .findViewById(R.id.reenterpassword);

                        if (isValidInput(etConfirmationId, etPin, etRePin)) {
                            String mConfirmationId = etConfirmationId.getText()
                                    .toString();
                            String mPinText = etPin.getText().toString();
                            forgotPINTask(mDialog, mConfirmationId, mPinText);
                        }
                    }

                    private boolean isValidInput(EditText... editTexts) {

                        boolean isValid = true;

                        for (EditText editText : editTexts) {
                            if (editText.getText().toString().length() < 1) {
                                isValid = false;
                                editText.setError(getString(R.string.fields_can_not_blank));
                            }
                        }
                        if (!isValid) {
                            return false;
                        }

                        int et_one_length = editTexts[1].getText().toString()
                                .length();
                        int et_two_length = editTexts[2].getText().toString()
                                .length();

                        if ((et_one_length < 10 && et_two_length < 10)) {

                            editTexts[1]
                                    .setError(getString(R.string.invalidpin_length));
                            editTexts[2]
                                    .setError(getString(R.string.invalidpin_length));
                            isValid = false;
                        } else if (!passValidator.validateLowerCase(editTexts[1]
                                .getText().toString())) {
                            editTexts[1]
                                    .setError(getString(R.string.invalidpin_lowecase));

                            isValid = false;
                        } else if (!passValidator.validateDigit(editTexts[1]
                                .getText().toString())) {
                            editTexts[1]
                                    .setError(getString(R.string.invalidpin_digit));

                            isValid = false;
                        } else if (!passValidator.validateUpperCase(editTexts[1]
                                .getText().toString())) {
                            editTexts[1]
                                    .setError(getString(R.string.invalidpin_uppercase));

                            isValid = false;
                        } else if (passValidator.validateIdentical(editTexts[1]
                                .getText().toString())) {
                            editTexts[1]
                                    .setError(getString(R.string.invalid_identical));

                            isValid = false;
                        } else if (!editTexts[1].getText().toString()
                                .equals(editTexts[2].getText().toString())) {
                            editTexts[2]
                                    .setError(getString(R.string.pinnotmatch));
                            isValid = false;
                        }

                        return isValid;
                    }

                });
        ((Button) mDialog.findViewById(R.id.cancel))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        mDialog.dismiss();
                    }
                });
        mDialog.show();

    }

    protected boolean isOnline(Context mContext) {

        boolean flag = false;
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            flag = cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        return flag;

    }

    private void forgotPINTask(final Dialog mDialog, String mConfirmationId,
                               String mPinText) {
        try {

            JSONObject jsonobj = new JSONObject();
            try {

                String mDeviceId1 = ((TelephonyManager) this
                        .getSystemService(Context.TELEPHONY_SERVICE))
                        .getDeviceId();

                jsonobj.put(WebConfig.WebParams.IMEI, mDeviceId1);

                jsonobj.put(WebConfig.WebParams.CONFIRMATION_ID,
                        mConfirmationId.toString());

                jsonobj.put(WebConfig.WebParams.NEW_PASSWORD, mPinText);

            } catch (JSONException e) {
                Helper.printStackTrace(e);
            }

            VolleyPostDataToNetwork pdn = new VolleyPostDataToNetwork(
                    LoginActivity.this, getString(R.string.loadingmessage),
                    new VolleyGetDataCallBack() {

                        @Override
                        public void processResponse(Object result) {

                            if (result != null) {

                                try {
                                    JSONObject jsonObject = new JSONObject(
                                            result.toString());

                                    if (jsonObject.getBoolean("IsSuccess")) {
                                        mDialog.dismiss();

                                        Helper.showAlertDialog(
                                                LoginActivity.this,
                                                SSCAlertDialog.SUCCESS_TYPE,
                                                getString(R.string.sucess),
                                                jsonObject.getString("Message"),
                                                getString(R.string.ok),
                                                new SSCAlertDialog.OnSDAlertDialogClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            SSCAlertDialog sdAlertDialog) {
                                                        sdAlertDialog.dismiss();

                                                    }
                                                }, false, null, null);

                                    } else {

                                        Helper.showAlertDialog(
                                                LoginActivity.this,
                                                SSCAlertDialog.ERROR_TYPE,
                                                getString(R.string.error3),
                                                jsonObject.getString("Message"),
                                                getString(R.string.ok),
                                                new SSCAlertDialog.OnSDAlertDialogClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            SSCAlertDialog sdAlertDialog) {
                                                        sdAlertDialog.dismiss();

                                                    }
                                                }, false, null, null);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                }

                            } else {

                                Toast.makeText(LoginActivity.this,
                                        getString(R.string.networkserverbusy),
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });

            pdn.setConfig(getString(R.string.url),
                    WebConfig.WebMethod.FORGET_PASSWORD);
            pdn.setRequestData(jsonobj);
            pdn.callWebService();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onShowHidePasswordClick(View view) {
        if (isPasswordShoing) {
            etPinText
                    .setTransformationMethod(new PasswordTransformationMethod());
            isPasswordShoing = false;
            ((ImageView) (this.findViewById(R.id.ivShowPassword)))
                    .setImageResource(R.drawable.ic_close_eye);
        } else {
            etPinText.setTransformationMethod(null);
            isPasswordShoing = true;
            ((ImageView) (this.findViewById(R.id.ivShowPassword)))
                    .setImageResource(R.drawable.ic_open_eye);

        }

        etPinText.setSelection(etPinText.getText().length());
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void disableWidgetWhileLogin() {
        etPinText.setFocusable(false);
        tvForgotPin.setClickable(false);
        tvCallUs.setClickable(false);
        loginButton.setClickable(false);

    }

    private void enableWidgetsOnLoginFailed() {
        if (!etPinText.isFocusable()) {
            etPinText.setFocusableInTouchMode(true);
            etPinText.setFocusable(true);
        }
        if (!tvForgotPin.isClickable()) {
            tvForgotPin.setClickable(true);
        }
        if (!tvCallUs.isClickable()) {
            tvCallUs.setClickable(true);
        }
        if (!loginButton.isClickable()) {
            loginButton.setClickable(true);
        }

    }

    public void onExportDBClick(View view) {

        try {
            Helper.backupDatabase(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Unable to export DB",
                    Toast.LENGTH_LONG).show();
        }
    }


    private void cancelSync() {

        ContentResolver.cancelSync(Helper.getAccount(getApplicationContext()),
                ProviderContract.AUTHORITY);
		/*
		 * if (Helper.isSyncActive(mAccount, AUTHORITY)) {
		 * 
		 * 
		 * Log.e("Cancel Sync", "Cancel Sync Calleddd"); }
		 */

    }

    private void startSync() {

        Bundle bundle = new Bundle();

        bundle.putString(SharedPreferencesKey.PREF_APIKEY, Helper
                .getStringValuefromPrefs(getApplicationContext(),
                        SharedPreferencesKey.PREF_APIKEY));
        bundle.putString(SharedPreferencesKey.PREF_APITOKEN, Helper
                .getStringValuefromPrefs(getApplicationContext()
                                .getApplicationContext(),
                        SharedPreferencesKey.PREF_APITOKEN));

        bundle.putString(SharedPreferencesKey.PREF_USERID, Helper
                .getStringValuefromPrefs(getApplicationContext(),
                        SharedPreferencesKey.PREF_USERID));

        bundle.putInt(SharedPreferencesKey.PREF_ROLEID, Helper
                .getIntValueFromPrefs(getApplicationContext(),
                        SharedPreferencesKey.PREF_ROLEID));

        bundle.putString(SharedPreferencesKey.PREF_COMPANYID, Helper
                .getStringValuefromPrefs(getApplicationContext(),
                        SharedPreferencesKey.PREF_COMPANYID));

        bundle.putString(SharedPreferencesKey.PREF_ROLEIDSTOREWISE, Helper
                .getStringValuefromPrefs(getApplicationContext(),
                        SharedPreferencesKey.PREF_ROLEIDSTOREWISE));

		/*
		 * if (ContentResolver.isSyncPending(mAccount, AUTHORITY)) {
		 * ContentResolver.cancelSync(mAccount, AUTHORITY); }
		 */

        // for periodic sync

        try {

            ContentResolver.setSyncAutomatically(
                    Helper.getAccount(getApplicationContext()),
                    ProviderContract.AUTHORITY, true);
            ContentResolver.addPeriodicSync(
                    Helper.getAccount(getApplicationContext()),
                    ProviderContract.AUTHORITY, bundle,
                    Constants.SYNC_FREQUENCY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // bundle values for request sync
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(Helper.getAccount(getApplicationContext()),
                ProviderContract.AUTHORITY, bundle);

		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		 * SyncRequest.Builder b = (new SyncRequest.Builder()).syncPeriodic(60,
		 * 50); b.setSyncAdapter(mAccount, AUTHORITY); b.setExtras(bundle);
		 * ContentResolver.requestSync(b.build()); } else {
		 * ContentResolver.addPeriodicSync(mAccount, AUTHORITY, new Bundle(),
		 * 60); }
		 */
    }


    public void onClearData(View view) {
        ((ActivityManager) view.getContext().getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();

    }
}
