package com.samsung.ssc.activitymodule;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;

public class SchemeDetails extends BaseActivity{
	private TextView schemeStartDate,schemeExpiryDate,description;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		setContentView(R.layout.schemedetails1);
		setCurrentContext(SchemeDetails.this);
 		description=(TextView)findViewById(R.id.description);
		schemeStartDate=(TextView)findViewById(R.id.schemeStartDate);
		schemeExpiryDate=(TextView)findViewById(R.id.schemeExpiryDate);
		Bundle bundle= getIntent().getExtras();
		if(bundle!=null){
			String startDate=bundle.getString("StartDate");
			String endDate=bundle.getString("ExpiryDate");
		    schemeStartDate.setText(changeToDate(startDate));
		    schemeExpiryDate.setText(changeToDate(endDate));   
		    description.setText(bundle.getString("Description"));
		
		}
		
		
		
	}
	private String changeToDate(String date){
		String sdate=date.substring(6, 19);
		long time=Long.parseLong(sdate);
		Date date1 = new Date(time);
		 SimpleDateFormat format = new SimpleDateFormat("d-MMM-yyyy");
		return format.format(date1);
		
	}
	
}
