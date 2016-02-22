package com.samsung.ssc.MOM;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;


import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.R;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.MOMDetailModal;
import com.samsung.ssc.util.Helper;

public class MOMSearchActivity extends BaseActivity {
	
	private EditText etMOMSearchText;
	private MOMSearchListAdapter adapter;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mom_search);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
		toolbar.setTitle("Search MOM");
		setSupportActionBar(toolbar);

		SearchView searchView = (SearchView)toolbar.findViewById(R.id.searchViewMOM);
		searchView.setIconifiedByDefault(true);
		searchView.setFocusable(true);
		searchView.setIconified(false);
		searchView.requestFocusFromTouch();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {

				onGetMOMData(query);


				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		setUpView();
		
	}

	private void setUpView() {

		etMOMSearchText = (EditText)this.findViewById(R.id.etMOMSearchInput);
		ListView lvMOMSearchList = (ListView)this.findViewById(R.id.lvMOMSearch);
		adapter = new MOMSearchListAdapter(this);
		lvMOMSearchList.setAdapter(adapter);
		
		lvMOMSearchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


				MOMDetailModal modal = (MOMDetailModal)parent.getItemAtPosition(position);
				Intent i = new Intent(MOMSearchActivity.this,MOMDetailsActivity.class);
				i.putExtra("MOM_MODAL", modal);
				startActivityForResult(i, 0);
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode , int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0) {
			switch (resultCode) {
			case Activity.RESULT_OK:
				
				onSearchMOMClick(null);

				break;

			default:
				break;
			}
		}
	}
	
	public void onSearchMOMClick(View view) {
		
		Helper.setKeyBoardShow(MOMSearchActivity.this, false);

		onGetMOMData(etMOMSearchText.getText().toString().trim());
	}

	private void onGetMOMData(String momText) {
		if (isInputValid(momText)) {

			ArrayList<MOMDetailModal> modals = DatabaseHelper.getConnection(getApplicationContext()).getMOMDataSearch(momText);

			if (modals.size()>0) {
				adapter.addItem(modals);
			}else{
				Helper.showAlertDialog(MOMSearchActivity.this, SSCAlertDialog.NORMAL_TYPE, "MOM not find", "MOM is not find in Application. You should sync your MOM", "OK", new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {

						sdAlertDialog.dismiss();
					}
				}, false, null, null);

			}
		}
	}


	private boolean isInputValid(String queryText) {

		if (TextUtils.isEmpty(queryText)) {
			//etMOMSearchText.setError("Feild can't blank");
			return false;
		}
		return true;
	}
}
