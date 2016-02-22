package com.samsung.ssc.LMS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;
import com.samsung.ssc.R;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.constants.SharedPreferencesKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.LMSLeaveDataModal;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.RecyclerItemClickListener;

import java.util.Calendar;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnLeaveListFragmentInteractionListener}
 * interface.
 */
public class LMSLeaveListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_DATE_FILETER = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final int LOADER_ID_LMS_LIST = 1;

    private OnLeaveListFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private RecyclerView mRecyclerView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private LMSLeaveListAdapter mAdapter;
    private View _rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TagCloudLinkView mTagCloudContainer;

    int mTabID =  LMSConstants.TAB_SUBMITTED;

    // TODO: Rename and change types of parameters
    public static LMSLeaveListFragment newInstance(String param1, String param2) {
        LMSLeaveListFragment fragment = new LMSLeaveListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LMSLeaveListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTabID = getArguments().getInt(IntentKey.KEY_PAGE_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new LMSLeaveListAdapter(getActivity(),mTabID);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (_rootView == null) {
            // Inflate the layout for this fragment
            _rootView = inflater.inflate(R.layout.fragment_leave_list, container, false);
            // Find and setup subviews
            setHasOptionsMenu(true);
            setUpView();
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove _rootView from the existing parent view group
            // (it will be added back).
         //   ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        }
        return _rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_lms_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_filter) {

            Intent intent = new Intent(getActivity(),LeaveListFilterActivity.class);
            startActivityForResult(intent, REQUEST_CODE_DATE_FILETER);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQUEST_CODE_DATE_FILETER:

                if (resultCode == Activity.RESULT_OK){

                    long fromDate = data.getLongExtra("FROM_DATE",0);
                    long toDate = data.getLongExtra("TO_DATE",0);
                    String fromDateValue = data.getStringExtra("FROM_DATE_VALUE");
                    String toDateValue = data.getStringExtra("TO_DATE_VALUE");

                    mTagCloudContainer.removeAllViews();
                    mTagCloudContainer.add(new Tag(1,fromDateValue+ "-"+toDateValue));
                    mTagCloudContainer.drawTags();


                    getLeaveData(fromDate,toDate);


                }

                break;
            default:
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpView() {

        mTagCloudContainer = (TagCloudLinkView) _rootView.findViewById(R.id.tagCloudContainer);

        /*mTagCloudContainer.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener() {
            @Override
            public void onTagSelected(Tag tag, int i) {
                // write something
                Toast.makeText(LMSLeaveListFragment.this.getActivity(), tag.getText() + " selected", Toast.LENGTH_SHORT).show();
            }
        });
*/
         mTagCloudContainer.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                // write something
                Toast.makeText(LMSLeaveListFragment.this.getActivity(), tag.getText() + " deleted", Toast.LENGTH_SHORT).show();

                getLeaveData();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout)_rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) _rootView.findViewById(R.id.recyclerViewLeaveList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        LMSLeaveDataModal obj = mAdapter.getItemAtPosition(position);
                        mListener.onFragmentInteraction(mTabID, obj);
                    }
                })
        );

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTagCloudContainer.removeAllViews();
                mListener.onFragmentRefresh();
            }
        });
     }

     void getLeaveData() {
      //List<LMSLeaveDataModal> lmsListData = DatabaseHelper.getConnection(getActivity()).getLeaveListData(REQUEST_TYPE);
         if (mTagCloudContainer.getTags().size()== 0) {
             Calendar cal = Calendar.getInstance();
             cal.add(Calendar.MONTH, -1);

             int userID = Integer.parseInt(Helper.getStringValuefromPrefs(getActivity().getApplicationContext(), SharedPreferencesKey.PREF_USERID));
             List<LMSLeaveDataModal> lmsListData = DatabaseHelper.getConnection(getActivity()).getLeaveListData(mTabID, userID, cal.getTimeInMillis());
             mAdapter.addItems(lmsListData);

             setRefreshing(false);
         } else {
             getLeaveData();

         }
     }



    void getLeaveData(long startDate,long endDate) {
        //List<LMSLeaveDataModal> lmsListData = DatabaseHelper.getConnection(getActivity()).getLeaveListData(REQUEST_TYPE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        int userID = Integer.parseInt(Helper.getStringValuefromPrefs(getActivity().getApplicationContext(), SharedPreferencesKey.PREF_USERID));
        List<LMSLeaveDataModal> lmsListData = DatabaseHelper.getConnection(getActivity()).getLeaveListData(mTabID, userID, startDate,endDate);
        mAdapter.addItems(lmsListData);

        setRefreshing(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLeaveListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    void setRefreshing(boolean refreshing){
        if (mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    public void notifyDataSetChange() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        int userID = Integer.parseInt(Helper.getStringValuefromPrefs(getActivity().getApplicationContext(), SharedPreferencesKey.PREF_USERID));

        List<LMSLeaveDataModal> lmsListData = DatabaseHelper.getConnection(getActivity()).getLeaveListData(mTabID, userID, cal.getTimeInMillis());
        mAdapter.addItems(lmsListData);

        mAdapter.notifyDataSetChanged();
    }
}
