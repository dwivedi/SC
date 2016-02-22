package com.samsung.ssc.MOM;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;

import android.support.v7.widget.Toolbar;


import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.constants.WebConfig;
import com.samsung.ssc.constants.WebConfig.WebMethod;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.MOMDetailModal;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.dto.ResponseDto;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.io.GetDataCallBack;
import com.samsung.ssc.io.PostDataToNetwork;
import com.samsung.ssc.util.Helper;

public class MOMListActivity extends BaseActivity {


    private Module mModule;
    private MOMSearchListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mom_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("MOM");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getBundleValue();

        setUpView();

    }

    private void getBundleValue() {
        Intent intent = getIntent();

        mModule = (Module) intent
                .getParcelableExtra(IntentKey.MOUDLE_POJO);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_mom_list, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sync:
                onSyncButtonClick();
                break;
            case R.id.menu_search_mom:
                onSearchButtonClick();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSearchButtonClick() {

        Intent intent = new Intent(MOMListActivity.this,
                MOMSearchActivity.class);
        startActivity(intent);

    }

    private void onSyncButtonClick() {

        try {

            // first get data of Delete.

            callWebServiceForDeleteMOMService();

            // third create all mom

        } catch (Exception exception) {

        }

    }

    protected void callWebServiceForMOMCreation() {

        JSONArray josnArray = DatabaseHelper.getConnection(
                getApplicationContext()).getMOMData(getApplicationContext());

        if (josnArray.length() > 0) {
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put(WebConfig.WebParams.USER_ID_CAPS, Long
                        .valueOf(Helper.getStringValuefromPrefs(
                                MOMListActivity.this,
                                SharedPreferencesKey.PREF_USERID)));
                jsonObject.put("MOMList", josnArray);

                PostDataToNetwork dataToNetwork = new PostDataToNetwork(
                        MOMListActivity.this, "Sync new MOM",
                        new GetDataCallBack() {

                            @Override
                            public void processResponse(Object result) {
                                if (result != null) {

                                    ResponseDto response = new FetchingdataParser(
                                            MOMListActivity.this)
                                            .getResponseResult(result
                                                    .toString());

                                    if (response.isSuccess()) {

                                        try {
                                            JSONArray resultJSONArray = new JSONArray(
                                                    response.getResult());

                                            int count = resultJSONArray
                                                    .length();
                                            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
                                            for (int i = 0; i < count; i++) {

                                                JSONObject jsonObject = resultJSONArray
                                                        .getJSONObject(i);
                                                int momID = jsonObject
                                                        .getInt("MOMIdClient");

                                                int momServerID = jsonObject
                                                        .getInt("MOMIdServer");
                                                map.put(momID, momServerID);

                                            }

                                            DatabaseHelper.getConnection(
                                                    getApplicationContext())
                                                    .updateMOMServerKey(map);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }

                                callWebServiceForGetALLServerMOMs();
                            }
                        });
                dataToNetwork.setConfig(getString(R.string.url),
                        WebMethod.SUBMIT_MOM_LIST);
                dataToNetwork.execute(jsonObject);

            } catch (JSONException e) {

            }
        } else {
            callWebServiceForGetALLServerMOMs();
        }

    }

    private void callWebServiceForGetALLServerMOMs() {

        try {
            PostDataToNetwork dataToNetwork = new PostDataToNetwork(
                    MOMListActivity.this, "Sync getting server mom",
                    new GetDataCallBack() {

                        @Override
                        public void processResponse(Object result) {
                            if (result != null) {

                                ResponseDto response = new FetchingdataParser(
                                        MOMListActivity.this)
                                        .getResponseResult(result.toString());
                                if (response.isSuccess()) {

                                    try {

                                        JSONArray jArray = new JSONArray(
                                                response.getResult());
                                        int count = jArray.length();
                                        if (count != 0) {
                                            ArrayList<MOMDetailModal> modals = new ArrayList<MOMDetailModal>();
                                            for (int i = 0; i < count; i++) {
                                                JSONObject jObject = jArray
                                                        .getJSONObject(i);
                                                MOMDetailModal modal = new MOMDetailModal();
                                                modal.momServerID = jObject
                                                        .getInt("MOMId");
                                                modal.momActionItem = jObject
                                                        .getString("ActionItem");
                                                modal.momDiscription = jObject
                                                        .getString("Description");
                                                modal.momDate = jObject
                                                        .getString("MomDateStr");
                                                try {
                                                    long time = new SimpleDateFormat(
                                                            "dd-MMM-yyyy")
                                                            .parse(jObject
                                                                    .getString("MomDateStr"))
                                                            .getTime();
                                                    modal.momDateValue = time;
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                modal.momLocaton = jObject
                                                        .getString("Location");
                                                modal.momTitle = jObject
                                                        .getString("MOMTitle");
                                                modal.momIsDeleted = jObject
                                                        .getBoolean("IsDeleted");
                                                modal.momIsUpdated = false;
                                                JSONArray jArrayAttendees = jObject
                                                        .getJSONArray("MOMAttendees");

                                                ArrayList<String> attendeesArray = new ArrayList<String>();
                                                for (int j = 0; j < jArrayAttendees
                                                        .length(); j++) {

                                                    attendeesArray
                                                            .add(jArrayAttendees
                                                                    .getJSONObject(
                                                                            j)
                                                                    .getString(
                                                                            "AttendeeName"));

                                                }
                                                modal.momAttendees = attendeesArray;

                                                modals.add(modal);
                                            }

                                            DatabaseHelper
                                                    .getConnection(
                                                            getApplicationContext())
                                                    .insertMOMServerData(modals);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                            getMOMDataFromDatabase();

                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserID", Long.valueOf(Helper
                    .getStringValuefromPrefs(MOMListActivity.this,
                            SharedPreferencesKey.PREF_USERID)));
            dataToNetwork.setConfig(getString(R.string.url),
                    WebMethod.GET_USER_MOM_DATA);
            dataToNetwork.execute(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callWebServiceForDeleteMOMService() {

        try {

            final HashMap<Integer, Integer> momServerIDs = DatabaseHelper
                    .getConnection(getApplicationContext()).getAllDeletedMOM();

            if (momServerIDs.size() > 0) {
                JSONArray jArrrr = new JSONArray();
                Set<Integer> keySetMOMID = momServerIDs.keySet();
                for (Integer momID : keySetMOMID) {
                    Integer momServerID = momServerIDs.get(momID);
                    jArrrr.put(momServerID);
                }

                JSONObject jObject = new JSONObject();
                jObject.put("momid", jArrrr);
                jObject.put("UserID", Long.parseLong(Helper
                        .getStringValuefromPrefs(this,
                                SharedPreferencesKey.PREF_USERID)));
                PostDataToNetwork dataToNetwork = new PostDataToNetwork(this,
                        "Sync Deleted", new GetDataCallBack() {

                    @Override
                    public void processResponse(Object result) {
                        if (result != null) {
                            try {
                                ResponseDto jsonObject = new FetchingdataParser(
                                        MOMListActivity.this)
                                        .getResponseResult(result
                                                .toString());

                                if (jsonObject.isSuccess()) {

                                    DatabaseHelper.getConnection(
                                            getApplicationContext())
                                            .deleteMOMDataPhicaly(
                                                    momServerIDs);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        callWebServiceForMOMCreation();
                    }
                });
                dataToNetwork.setConfig(getString(R.string.url),
                        "DeleteMOMData");
                dataToNetwork.execute(jObject);
            } else {
                callWebServiceForMOMCreation();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setUpView() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutMOMList);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSyncButtonClick();
            }
        });
        final ListView listView = (ListView) findViewById(R.id.list_view_mom);

         mAdapter = new MOMSearchListAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = listView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                mAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = mAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        int[] momIds = new int[selected.size()];
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                MOMDetailModal selecteditem = (MOMDetailModal) mAdapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                mAdapter.remove(selecteditem);
                                momIds[i] = selecteditem.momID;

                            }
                        }

                        DatabaseHelper.getConnection(MOMListActivity.this)
                                .deleteMOMData(momIds);
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.mom_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int postion, long l) {
                MOMDetailModal modal = (MOMDetailModal) adapterView
                        .getItemAtPosition(postion);

                Intent i = new Intent();
                i.setClass(view.getContext(), MOMDetailsActivity.class);
                i.putExtra("MOM_MODAL", modal);
                startActivity(i);

            }
        });



    }

    public void onAddMOMClick(View view) {
        Intent i = new Intent();
        i.setClass(this, MOMCreateActiviity.class);
        i.putExtra(IntentKey.MOUDLE_POJO, mModule);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMOMDataFromDatabase();

    }

    private void getMOMDataFromDatabase() {
        ArrayList<MOMDetailModal> momData = DatabaseHelper.getConnection(
                getApplicationContext()).getMOMData();

        mAdapter.dataSet.clear();
        mAdapter.addItem(momData);
    }
}
