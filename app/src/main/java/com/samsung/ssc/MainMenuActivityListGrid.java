/**
 *
 */
package com.samsung.ssc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.adapters.ActivityModuleAdapterListViewGridView;
import com.samsung.ssc.constants.ModuleCode;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.OnGridItemClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author d.ashish
 */
public class MainMenuActivityListGrid extends BaseActivity implements
        LoaderCallbacks<Cursor> {


    private GridView gridView;
    private TextView tvLoading;
    private LinearLayout containerLayout;
    private ListView listView;
     private TextView tvPendingCount;
    private Menu menu;

    private boolean isGrid = false;

    private final int LOADER_USER_MODULE_ID = 1;
    private final int LOADER_GET_STORE_COUNT_ID = 2;
    private final int LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE = 3;

    protected ActivityModuleAdapterListViewGridView adapter;



    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        setContentView(R.layout.dashboardmenu_list_grid);

        Toolbar toolBar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setIcon(R.drawable.ic_app_logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setUpView();

    }





    private void setUpView() {

        containerLayout = (LinearLayout) this.findViewById(R.id.listGridCantainer);
        tvPendingCount = (TextView)this. findViewById(R.id.textViewPendingCount);
        gridView = new GridView(getApplicationContext());
        LinearLayout.LayoutParams layoutParms = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(layoutParms);
        gridView.setGravity(Gravity.CENTER);
        gridView.setNumColumns(GridView.AUTO_FIT);
        float scalefactor = getResources().getDisplayMetrics().density * 100;
        int number = getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / (float) scalefactor);
        gridView.setNumColumns(columns);
        gridView.setOnItemClickListener(new OnGridItemClick(
                MainMenuActivityListGrid.this));

        listView = new ListView(getApplicationContext());
        listView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setDivider(new ColorDrawable(this.getResources().getColor(
                R.color.listselectdivider)));
        listView.setDividerHeight(2);

        adapter = new ActivityModuleAdapterListViewGridView(
                MainMenuActivityListGrid.this);
        gridView.setAdapter(adapter);
        adapter.setSelectionListorGrid(!isGrid);
        containerLayout.addView(gridView);

        tvLoading = (TextView) findViewById(R.id.tvLoadingMessage);

        gridView.setEmptyView(tvLoading);
        listView.setEmptyView(tvLoading);


        final ImageButton gridViewListViewSwitcher = (ImageButton) findViewById(R.id.buttonGridViewListView);
        gridViewListViewSwitcher.setImageLevel(0);
        gridViewListViewSwitcher.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isGrid) {
                    gridViewListViewSwitcher.setImageLevel(0);
                    containerLayout.removeView(listView);
                    containerLayout.addView(gridView);
                    gridView.setAdapter(adapter);
                    adapter.setSelectionListorGrid(isGrid);
                    gridView.setOnItemClickListener(new OnGridItemClick(
                            MainMenuActivityListGrid.this));
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_action_list));
                    isGrid = false;

                } else {
                    gridViewListViewSwitcher.setImageLevel(1);
                    containerLayout.removeView(gridView);
                    containerLayout.addView(listView);
                    listView.setAdapter(adapter);
                    adapter.setSelectionListorGrid(isGrid);
                    listView.setOnItemClickListener(new OnGridItemClick(
                            MainMenuActivityListGrid.this));
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_action_grid));
                    isGrid = true;
                }
            }
        });

        int check = Helper.getIntValueFromPrefs(MainMenuActivityListGrid.this,
                SharedPreferencesKey.PREF_MARK_ATTENDANCE);
        if (check == 0) {
            drawOfflineModule();
        } else {
            getSupportLoaderManager().initLoader(LOADER_USER_MODULE_ID, null,
                    this);

            getSupportLoaderManager().initLoader(
                    LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE, null, this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home_logout:
                onLogoutClick();
                break;
            case R.id.menu_home_module_view:

                if (isGrid) {
                    containerLayout.removeView(listView);
                    containerLayout.addView(gridView);
                    gridView.setAdapter(adapter);
                    adapter.setSelectionListorGrid(isGrid);
                    gridView.setOnItemClickListener(new OnGridItemClick(
                            MainMenuActivityListGrid.this));
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_action_list));
                    isGrid = false;

                } else {
                    containerLayout.removeView(gridView);
                    containerLayout.addView(listView);
                    listView.setAdapter(adapter);
                    adapter.setSelectionListorGrid(isGrid);
                    listView.setOnItemClickListener(new OnGridItemClick(
                            MainMenuActivityListGrid.this));

                   // menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_action_grid_format));
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_action_grid));

                    isGrid = true;
                    break;
                }

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getSupportLoaderManager().getLoader(LOADER_GET_STORE_COUNT_ID) == null) {
            getSupportLoaderManager().initLoader(LOADER_GET_STORE_COUNT_ID,
                    null, this);
        } else {
            getSupportLoaderManager().restartLoader(LOADER_GET_STORE_COUNT_ID,
                    null, this);
        }

    }

    private void drawOfflineModule() {

        adapter.clear();
        Module offlineModule = new Module();
        offlineModule.setIconName("report.png");
        offlineModule.setName("Reports");
        offlineModule.setModuleCode(ModuleCode.MENU_REPORTS);
        offlineModule.setModuleID(7);
        adapter.addModule(offlineModule);
        offlineModule = new Module();
        offlineModule.setIconName("user_profile.png");
        offlineModule.setName("User Profile");
        offlineModule.setModuleCode(ModuleCode.MENU_USERPROFILE);
        offlineModule.setModuleID(8);
        adapter.addModule(offlineModule);

        gridView.setOnItemClickListener(new OnGridItemClick(
                MainMenuActivityListGrid.this));
    }

    @Override
    public void onBackPressed() {

        onLogoutClick();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle arg1) {

        CursorLoader cursorLoader = null;
        if (loaderId == LOADER_USER_MODULE_ID) {
            String[] whereClause = {"0"};
            cursorLoader = new CursorLoader(getApplicationContext(),
                    ProviderContract.USER_MODULES_URI, null, null, whereClause,
                    null);

        } else if (loaderId == LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE) {

            cursorLoader = new CursorLoader(getApplicationContext(),
                    ProviderContract.URI_DOWNLOAD_DATA_SINGLE_SERVICE, null,
                    null, null, null);

        } else if (loaderId == LOADER_GET_STORE_COUNT_ID) {

            cursorLoader = new CursorLoader(getApplicationContext(),
                    ProviderContract.STORE_COUNT_WITH_SUBMIT_DATA_URI, null,
                    null, null, null);

        }
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case LOADER_USER_MODULE_ID:

                if (data != null) {

                    List<Module> userModules = DatabaseUtilMethods
                            .getUserModuleFromCursor(data);

                    adapter.addModules(userModules);

                }

                break;

            case LOADER_DOWNLOAD_STATUS_SINGLE_SERVICE:


                Cursor cursor = getContentResolver().query(
                        ProviderContract.URI_MODULE_DATA_DOWNLOAD_STATUS, null,
                        null, null, null);

                adapter.setNewDownloadStatusMap(DatabaseUtilMethods
                        .getDownloadStatusMapFromCursor(cursor));


                break;

            case LOADER_GET_STORE_COUNT_ID:
                if (data != null && data.getCount() > 0) {

                    getStoreCount(data);
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }

    public void getStoreCount(Cursor data) {

        int no_of_stores = DatabaseUtilMethods
                .getNumberOfStoresWithSubmittedData(data);

        if (no_of_stores > 0) {

            if (tvPendingCount.getVisibility() == View.GONE) {
                tvPendingCount.setVisibility(View.VISIBLE);
            }

            String storeORstores = no_of_stores == 1 ? " item" : " items";
            String hasORhave = no_of_stores == 1 ? " has" : " have";

            String message = no_of_stores + storeORstores + hasORhave
                    + getString(R.string.pending_unsynced_data);

            tvPendingCount.setText(message);

        } else {
            if (tvPendingCount.getVisibility() == View.VISIBLE) {
                tvPendingCount.setVisibility(View.GONE);
            }
        }
    }

    public void refreshPendingSyncCount() {
        Loader loader = getSupportLoaderManager().getLoader(
                LOADER_GET_STORE_COUNT_ID);

        if (loader != null) {
            getSupportLoaderManager().restartLoader(LOADER_GET_STORE_COUNT_ID,
                    null, this);
        }

    }

    private void onLogoutClick() {

        Helper.showAlertDialog(MainMenuActivityListGrid.this,
                SSCAlertDialog.WARNING_TYPE, getString(R.string.error10),
                getString(R.string.error12), getString(R.string.yes),
                new SSCAlertDialog.OnSDAlertDialogClickListener() {

                    @Override
                    public void onClick(SSCAlertDialog sdAlertDialog) {
                        sdAlertDialog.dismiss();
                        Helper.clearPreferencesData(MainMenuActivityListGrid.this);

                        //getContentResolver().cancelSync(Helper.getAccount(MainMenuActivityListGrid.this), ProviderContract.AUTHORITY);
                        Helper.cancelSync(MainMenuActivityListGrid.this);

                        Intent intent = new Intent(MainMenuActivityListGrid.this,
                                LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("logout", true);
                        finish();
                        startActivity(intent);
                    }
                }, true, getString(R.string.no),
                new SSCAlertDialog.OnSDAlertDialogClickListener() {

                    @Override
                    public void onClick(SSCAlertDialog sdAlertDialog) {
                        sdAlertDialog.dismiss();
                    }
                });

    }
}
