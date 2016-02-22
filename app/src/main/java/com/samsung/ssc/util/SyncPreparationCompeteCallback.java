package com.samsung.ssc.util;

import java.util.ArrayList;

import com.samsung.ssc.dto.ActivityDataMasterModel;

public interface SyncPreparationCompeteCallback {
	public void onComplete(ArrayList<ActivityDataMasterModel> data);
}
