package com.samsung.ssc.EMS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.samsung.ssc.R;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.EMSExpenseDetail;

import java.util.ArrayList;

public class SearchExpenseListingActivity extends Activity { 
	
	private EditText mSearchExpenseEditText;
	private ExpenseListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_expense_listing);

		setUpView();

	}
	
	 @Override
	    public void onResume() {
	    	super.onResume();
	    	
	    	if(!TextUtils.isEmpty(mSearchExpenseEditText.getText().toString()))
	    	getSerachExpensesDataFromDatabase(mSearchExpenseEditText.getText().toString());
	    }
	
	private void setUpView() {
		
		mSearchExpenseEditText = (EditText) findViewById(R.id.search_expense_editetxt);
		
		RecyclerView mExpenseItemListRecyclerview = (RecyclerView) findViewById(R.id.serach_expense_list_recycler_view);
		mExpenseItemListRecyclerview.setHasFixedSize(true);
		// use a linear layout manager
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mExpenseItemListRecyclerview.setLayoutManager(mLayoutManager);

		mAdapter = new ExpenseListAdapter(this,false);
		mExpenseItemListRecyclerview.setAdapter(mAdapter);
		 
		
		
	}
	
	private void searchExpenseByExpenseType()
	{
		if(TextUtils.isEmpty(mSearchExpenseEditText.getText().toString()))
		 {
			mSearchExpenseEditText.setError("Expense Type is empty.");
		 }
		else
		{
			getSerachExpensesDataFromDatabase(mSearchExpenseEditText.getText().toString());
		}	
	}

	 public void onSearchExpenseClick(View view)
	 {
		 searchExpenseByExpenseType(); 
	 }
	 
	 
	 private void getSerachExpensesDataFromDatabase(String searchExpenseString) {
			
	    	ArrayList<EMSExpenseDetail> emsExpenseDetailArrayList = DatabaseHelper.getConnection(this).getSearchExpenseData(searchExpenseString);
	    	
			mAdapter.clear();
			
			if(emsExpenseDetailArrayList != null && emsExpenseDetailArrayList.size() > 0)
			     mAdapter.addItems(emsExpenseDetailArrayList);
			
		}	
	 
	 
	 public void launchUpdateExpenseClick(EMSExpenseDetail inEMSExpenseDetail){

			Intent newExpenseIntent = new Intent(this, NewExpenseActivity.class);
			newExpenseIntent.putExtra("EMSExpenseDetail", inEMSExpenseDetail);
			startActivity(newExpenseIntent);

		}

	public void onBackBtnClick(View view)
	{
		finish();
	}
}
