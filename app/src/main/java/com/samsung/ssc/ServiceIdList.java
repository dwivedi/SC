package com.samsung.ssc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.samsung.ssc.activitymodule.FeedbackDetailActivity;

public class ServiceIdList extends Activity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_id_list);
		listView = (ListView) findViewById(R.id.listView);
		final String[] idsArray = getIntent()
				.getStringArrayExtra("notification_list");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, idsArray);

		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String serviceId = idsArray[position].split(",")[1].trim();
				
				Intent intent = new Intent(ServiceIdList.this,FeedbackDetailActivity.class);
				intent.putExtra("FeedbackID", Long.valueOf(serviceId));
				startActivity(intent);
				ServiceIdList.this.finish();
			}
		});
	}
}
