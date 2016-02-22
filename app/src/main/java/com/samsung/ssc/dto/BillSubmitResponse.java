package com.samsung.ssc.dto;

import java.util.ArrayList;

public class BillSubmitResponse {

	public long miEMSBillDetailIDClient;
	public long miEMSBillDetailIDServer;
	
	public ArrayList<BillDocumentSubmitResponse> mBillDocumentSubmitResponseList;
}
