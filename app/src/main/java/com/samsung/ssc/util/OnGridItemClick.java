package com.samsung.ssc.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.samsung.ssc.BeatApproval;
import com.samsung.ssc.BeatCreation;
import com.samsung.ssc.BeatsDetail;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.DealerCreation;
import com.samsung.ssc.DealerHistory;
import com.samsung.ssc.EMS.ExpenseListingActivity;
import com.samsung.ssc.KPIListActivity1;
import com.samsung.ssc.LMS.LMSListActivity;
import com.samsung.ssc.LoginActivity;
import com.samsung.ssc.MOM.MOMListActivity;
import com.samsung.ssc.MainMenuActivityListGrid;
import com.samsung.ssc.R;
import com.samsung.ssc.StoreWorking;
import com.samsung.ssc.UserProfile;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.activitymodule.ActivityDashboardChild;
import com.samsung.ssc.activitymodule.ActivityDashboardChildNonStoreWise;
import com.samsung.ssc.activitymodule.BookOrder;
import com.samsung.ssc.activitymodule.Collections;
import com.samsung.ssc.activitymodule.CounterShareDisplayShareActivity;
import com.samsung.ssc.activitymodule.EOLOrderActivity;
import com.samsung.ssc.activitymodule.FMSCreateFeedbackActivity;
import com.samsung.ssc.activitymodule.FMSFeedbackTracker1;
import com.samsung.ssc.activitymodule.OutletProfile2;
import com.samsung.ssc.activitymodule.PalnogramActivity1;
import com.samsung.ssc.activitymodule.QuestionnaireActivity;
import com.samsung.ssc.activitymodule.RaceProductAuditActivity;
import com.samsung.ssc.activitymodule.SODetailActivity;
import com.samsung.ssc.activitymodule.SchemeAvailable;
import com.samsung.ssc.activitymodule.SellOutProjection;
import com.samsung.ssc.activitymodule.StockEscalation;
import com.samsung.ssc.activitymodule.TodaySchemesActivity;
import com.samsung.ssc.adapters.SeparatedListAdapter;
import com.samsung.ssc.adapters.SeparatedListItemAdapter;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.ModuleCode;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.ActivityDataMasterModel;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.reports.DashboardReports;
import com.samsung.ssc.sync.SyncMaster;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class OnGridItemClick implements OnItemClickListener {

    private Activity context;
    int total_items_checked = 0;
    boolean selected_items[] = null;
    private Animation mAnim = null;
    private Bundle mBundle;

    public OnGridItemClick(final Activity context) {
        this.context = context;
    }

    public OnGridItemClick(final Activity context, Bundle bundle) {
        this.context = context;
        this.mBundle = bundle;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view,
                            int position, long id) {

        getAnimation();
        view.startAnimation(mAnim);

        try {
            Module module = (Module) adapterView
                    .getItemAtPosition(position);
            if (module != null) {
                onGridItemClick(module);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAnimation() {

        if (mAnim == null)
            mAnim = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
    }

    private void onGridItemClick(Module module) {

        try {
            if (module.isQuestionType()) {

                Intent intent = new Intent(context, QuestionnaireActivity.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in,R.anim.left_out);

            } else {
                try {
                    if (module.getModuleCode() == ModuleCode.MENU_MARKETWORKING) {

                        onActivityItemClick(module);

                    } else {

                        if (DatabaseHelper.getConnection(context)
                                .isSubModuleExists(module.getModuleID())) {

                            Intent intent;

                            if (module.isStoreWise()) {

                                intent = new Intent(context,
                                        ActivityDashboardChild.class);

                                if (mBundle != null) {
                                    intent.putExtra(
                                            IntentKey.KEY_STORE_BASIC,
                                            mBundle.getParcelable(IntentKey.KEY_STORE_BASIC));
                                }

                            } else {

                                intent = new Intent(
                                        context,
                                        ActivityDashboardChildNonStoreWise.class);
                                intent.putExtra(IntentKey.MOUDLE_POJO, module);

                            }

                            intent.putExtra(IntentKey.KEY_MODULE_ID,
                                    module.getModuleID());
                            context.startActivity(intent);
                            //context.overridePendingTransition(R.anim.right_in,R.anim.left_out);

                        } else {
                            onActivityItemClick(module);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityItemClick(Module module) {

        Intent intent = new Intent();
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        int moduleCodeSelected;
        try {
            moduleCodeSelected = module.getModuleCode();

        } catch (NumberFormatException e) {
            e.printStackTrace();
            moduleCodeSelected = -1;
        }

        switch (moduleCodeSelected) {

            case ModuleCode.MENU_EMS_MODULE:

                intent.setClass(context, ExpenseListingActivity.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_LMS_MODULE:

                intent.setClass(context, LMSListActivity.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;


            case ModuleCode.MENU_MOM_MODULE:

                intent.setClass(context, MOMListActivity.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;


            case ModuleCode.MENU_PRODUCT_AUDIT:

                intent.setClass(context, RaceProductAuditActivity.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_MARKETWORKING:
                intent.setClass(context, StoreWorking.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_REPORTS:
                intent.setClass(context, DashboardReports.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_USERPROFILE:

                intent.setClass(context, UserProfile.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_PALNOGRAM:
                if (Helper.getBoolValueFromPrefs(context,
                        SharedPreferencesKey.PREF_STORE_IS_PLANOGRAM)) {
                    //intent.setClass(context, PalnogramActivity.class);
                    intent.setClass(context, PalnogramActivity1.class);
                    intent.putExtra(IntentKey.MOUDLE_POJO, module);
                    context.startActivity(intent);
                    //context.overridePendingTransition(R.anim.right_in,R.anim.left_out);

                } else {

                    Helper.showCustomToast(context,
                            R.string.planogram_module_is_not_for_selected_store,
                            Toast.LENGTH_LONG);
                }

                break;
            case ModuleCode.MENU_BEATAPPROVAL:

                intent.setClass(context, BeatApproval.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_BEATCREATION:
                intent.setClass(context, BeatCreation.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_BEATSUMMARY:
                intent.setClass(context, BeatsDetail.class);
                intent.putExtra("beattype", "beatsummary");

                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_REFRESHMATER:

                // refreshMaster1();

                Helper.showAlertDialog(
                        context,
                        SSCAlertDialog.WARNING_TYPE,
                        context.getString(R.string.error3),
                        context.getString(R.string.refresh_message),
                        context.getString(R.string.ok),
                        new SSCAlertDialog.OnSDAlertDialogClickListener() {

                            @Override
                            public void onClick(SSCAlertDialog sdAlertDialog) {
                                sdAlertDialog.dismiss();
                                Editor editor = Helper.getEditor(context);
                                editor.clear().commit();
                                DatabaseHelper.getConnection(context)
                                        .deleteMasters();

                                logOut();

                            }
                        }, true, context.getString(R.string.cancel),
                        new SSCAlertDialog.OnSDAlertDialogClickListener() {

                            @Override
                            public void onClick(SSCAlertDialog sdAlertDialog) {
                                sdAlertDialog.dismiss();
                            }
                        });

                break;


            case ModuleCode.MENU_COLLECTION:
                intent.setClass(context, Collections.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_STOREOUTLETPROFILE:
                intent.setClass(context, OutletProfile2.class);
                intent.putExtra(IntentKey.KEY_CANCEL_BUTTON_NEEDED, true);

                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_STOCK_ESCALATION:

                intent.setClass(context, StockEscalation.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_ORDER_BOOKING:

                intent.setClass(context, BookOrder.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_SELL_OUT_PROJECTION:

                intent.setClass(context, SellOutProjection.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_OD_EOL:

                intent.setClass(context, EOLOrderActivity.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                intent.putExtra(IntentKey.STORE_ID, -1);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_SCHEMEAVAILABLE:
                intent.setClass(context, SchemeAvailable.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                Toast.makeText(context, context.getString(R.string.comingsoon),
                        Toast.LENGTH_LONG).show();

                break;
            case ModuleCode.MENU_SYNCING:
                if (isOnline()) {

                    Map<String, ArrayList<String>> pandingMoudleList = DatabaseHelper
                            .getConnection(context)
                            .getPendingMendantoryModuleList();

                    if (pandingMoudleList.size() == 0) {

                        final OnOkListener okListener = new OnOkListener() {

                            @Override
                            public void onOKPressed() {

                                context.finish();

                            }
                        };

                        final SyncMaster master = new SyncMaster(context);
                        SyncPreparationCompeteCallback callback = new SyncPreparationCompeteCallback() {

                            @Override
                            public void onComplete(
                                    ArrayList<ActivityDataMasterModel> data) {

                                if (data.size() > 0) {

                                    if (!Helper.isOnline(context)) {

                                        Toast.makeText(context,
                                                R.string.not_online,
                                                Toast.LENGTH_LONG).show();

                                        return;
                                    }


                                    if (context instanceof ActivityDashboardChild) {
                                        ((ActivityDashboardChild) context).isPopupVisible(true);
                                    }

                                    master.sync(data, false, okListener);

                                } else {
                                    Toast.makeText(context, R.string.Nodata,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        };
                        DatabaseHelper
                                .getConnection(context)
                                .updateStoreAssessment(
                                        Long.parseLong(Helper
                                                .getStringValuefromPrefs(
                                                        context,
                                                        SharedPreferencesKey.PREF_STOREID)));
                        master.getSyncData(callback);
                    } else {
                        SeparatedListAdapter adapter = new SeparatedListAdapter(
                                context);
                        Set<String> keySet = pandingMoudleList.keySet();
                        for (String key : keySet) {
                            pandingMoudleList.get(key);
                            SeparatedListItemAdapter itemAdapter = new SeparatedListItemAdapter(
                                    context, 0, pandingMoudleList.get(key));
                            adapter.addSection(key, itemAdapter);
                        }

                        SSCAlertDialog alertDialog = new SSCAlertDialog(
                                context, SSCAlertDialog.LIST_TYPE)
                                .setTitleText("Pending Modules")
                                .setListAdapter(adapter)
                                .setEnableConfirmButton(true)
                                .setConfirmText("OK")
                                .setConfirmClickListener(
                                        new SSCAlertDialog.OnSDAlertDialogClickListener() {

                                            @Override
                                            public void onClick(
                                                    SSCAlertDialog sdAlertDialog) {

                                                sdAlertDialog.dismiss();

                                            }

                                        }).showCancelButton(false)
                                .setCancelText(null).setCancelClickListener(null);
                        alertDialog.setCancelable(false);

                        alertDialog.show();
                    }

                } else {

                    Helper.showAlertDialog(
                            context,
                            SSCAlertDialog.ERROR_TYPE,
                            context.getString(R.string.error1),
                            context.getString(R.string.error2),
                            context.getString(R.string.ok),
                            new SSCAlertDialog.OnSDAlertDialogClickListener() {

                                @Override
                                public void onClick(
                                        SSCAlertDialog sdAlertDialog) {

                                    sdAlertDialog.dismiss();

                                }
                            }, false, null, null);

                }
                break;
            case ModuleCode.MENU_SYNCING_HOME:

                if (isOnline()) {

                    Map<String, ArrayList<String>> pandingMoudleList = DatabaseHelper
                            .getConnection(context)
                            .getPendingMendantoryModuleList();

                    if (pandingMoudleList.size() == 0) {
                        final OnOkListener okListener1 = new OnOkListener() {

                            @Override
                            public void onOKPressed() {
                                Activity mActivity = (Activity) context;
                                if (mActivity instanceof MainMenuActivityListGrid) {
                                    MainMenuActivityListGrid activity = (MainMenuActivityListGrid) mActivity;
                                    activity.refreshPendingSyncCount();

                                }
                            }
                        };
                        final SyncMaster master1 = new SyncMaster(context);
                        SyncPreparationCompeteCallback callback1 = new SyncPreparationCompeteCallback() {

                            @Override
                            public void onComplete(
                                    ArrayList<ActivityDataMasterModel> data) {

                                if (data.size() > 0) {

                                    if (!Helper.isOnline(context)) {

                                        Helper.showCustomToast(context,
                                                R.string.not_online,
                                                Toast.LENGTH_LONG);

                                        return;
                                    }

                                    master1.sync(data, false, okListener1);

                                } else {

                                    Helper.showCustomToast(context,
                                            R.string.Nodata, Toast.LENGTH_LONG);
                                }
                            }
                        };
                        master1.getSyncData(callback1);
                    } else {
                        SeparatedListAdapter adapter = new SeparatedListAdapter(
                                context);
                        Set<String> keySet = pandingMoudleList.keySet();
                        for (String key : keySet) {
                            pandingMoudleList.get(key);
                            SeparatedListItemAdapter itemAdapter = new SeparatedListItemAdapter(
                                    context, 0, pandingMoudleList.get(key));
                            adapter.addSection(key, itemAdapter);
                        }

                        SSCAlertDialog alertDialog = new SSCAlertDialog(
                                context, SSCAlertDialog.LIST_TYPE)
                                .setTitleText("Pending Modules")
                                .setListAdapter(adapter)
                                .setEnableConfirmButton(true)
                                .setConfirmText("OK")
                                .setConfirmClickListener(
                                        new SSCAlertDialog.OnSDAlertDialogClickListener() {

                                            @Override
                                            public void onClick(
                                                    SSCAlertDialog sdAlertDialog) {

                                                sdAlertDialog.dismiss();

                                            }

                                        }).showCancelButton(false)
                                .setCancelText(null).setCancelClickListener(null);
                        alertDialog.setCancelable(false);

                        alertDialog.show();
                    }

                } else {

                    Helper.showAlertDialog(
                            context,
                            SSCAlertDialog.ERROR_TYPE,
                            context.getString(R.string.error1),
                            context.getString(R.string.error2),
                            context.getString(R.string.ok),
                            new SSCAlertDialog.OnSDAlertDialogClickListener() {

                                @Override
                                public void onClick(
                                        SSCAlertDialog sdAlertDialog) {

                                    sdAlertDialog.dismiss();

                                }
                            }, false, null, null);

                }
                break;
            case ModuleCode.MENU_DISPLAYSHARE:
                if (Helper
                        .getBoolValueFromPrefs(
                                context,
                                SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE)) {
                    intent.setClass(context, CounterShareDisplayShareActivity.class);
                    intent.putExtra(IntentKey.MOUDLE_POJO, module);
                    context.startActivity(intent);
                    //context.overridePendingTransition(R.anim.right_in,
                    //		R.anim.left_out);

                } else {

                    Helper.showCustomToast(
                            context,
                            R.string.display_share_module_is_not_for_selected_store,
                            Toast.LENGTH_LONG);

                }
                break;

            case ModuleCode.MENU_COUNTERSHARE:
                if (Helper
                        .getBoolValueFromPrefs(
                                context,
                                SharedPreferencesKey.PREF_STORE_IS_DISPLAY_SHARE_COUNTER_SHARE)) {
                    intent.setClass(context, CounterShareDisplayShareActivity.class);
                    intent.putExtra(IntentKey.MOUDLE_POJO, module);
                    context.startActivity(intent);
                    //context.overridePendingTransition(R.anim.right_in,
                    //	R.anim.left_out);

                } else {

                    Helper.showToast(
                            context,
                            R.string.counter_share_module_is_not_for_selected_store,
                            Toast.LENGTH_LONG);

                }

                break;

            case ModuleCode.MENU_FMS_TRACKER_NON_STORE_WISE:
                intent.setClass(context, FMSFeedbackTracker1.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_FMS_TRACKER_STORE_WISE:
                intent.setClass(context, FMSFeedbackTracker1.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_FMS_CREATE_FMS_NON_STORE_WISE:
                intent.setClass(context, FMSCreateFeedbackActivity.class);
                intent.putExtra(IntentKey.STORE_ID, -1);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_FMS_CREATE_FMS_STORE_WISE:
                intent.setClass(context, FMSCreateFeedbackActivity.class);

                String storeId1 = Helper.getStringValuefromPrefs(context,
                        SharedPreferencesKey.PREF_STOREID);
                intent.putExtra(IntentKey.STORE_ID, Integer.parseInt(storeId1));
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_MDM_DELEAR_CREATION:

                intent.setClass(context, DealerCreation.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_MDM_HISTORY:

                intent.setClass(context, DealerHistory.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_KAS:

                intent.setClass(context, KPIListActivity1.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_SO_DETAIL_STORE_WISE:
                intent.setClass(context, SODetailActivity.class);

                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case ModuleCode.MENU_SO_DETAIL_NON_STORE_WISE:
                intent.setClass(context, SODetailActivity.class);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

            case ModuleCode.MENU_SCHEMES:
                intent.setClass(context, TodaySchemesActivity.class);
                intent.putExtra(IntentKey.MOUDLE_POJO, module);
                context.startActivity(intent);
                //context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            default:

                Helper.showCustomToast(context, R.string.no_activity_found,
                        Toast.LENGTH_LONG);
                break;
        }

    }

    private boolean isOnline() {
        boolean flag = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            flag = cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        return flag;
    }

    private void logOut() {

        Helper.clearPreferencesData(context);

        Helper.cancelSync(context);

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("logout", true);
        context.finish();
        context.startActivity(intent);

    }

    public class DialogButtonClickHandler implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int clicked) {
            switch (clicked) {
                case DialogInterface.BUTTON_POSITIVE:

                    break;
            }
        }
    }


}
