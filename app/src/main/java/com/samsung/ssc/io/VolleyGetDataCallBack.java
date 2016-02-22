package com.samsung.ssc.io;

import com.android.volley.VolleyError;

public interface VolleyGetDataCallBack {

	void processResponse(Object result);
	
	void onError(VolleyError error);

}

