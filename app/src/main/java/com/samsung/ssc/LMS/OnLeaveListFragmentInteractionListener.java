package com.samsung.ssc.LMS;

import android.support.v4.app.Fragment;

import com.samsung.ssc.dto.LMSLeaveDataModal;

/**
 * Created by d.ashish on 28-01-2016.
 */
public interface OnLeaveListFragmentInteractionListener {

        public void onFragmentInteraction(int tabID,LMSLeaveDataModal LMSLeaveDataModal);
        public void onFragmentRefresh();
}
