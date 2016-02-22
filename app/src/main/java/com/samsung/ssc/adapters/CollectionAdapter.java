package com.samsung.ssc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.activitymodule.AddCollectionDialog;
import com.samsung.ssc.activitymodule.Collections;
import com.samsung.ssc.dto.CollectionDataDto;
import com.samsung.ssc.util.CollectionArrayList;
import com.samsung.ssc.util.Helper;

 
public class CollectionAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private CollectionArrayList dataArray;
	Context mContext;

	public CollectionAdapter(Context context, CollectionArrayList dataArray) {

		this.dataArray = dataArray;
		inflater = LayoutInflater.from(context);
		mContext = context;

	}

	@Override
	public int getCount() {
		return dataArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}//

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = new ViewHolder();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.collection_items, null);
			
			holder.srNo = (TextView) convertView.findViewById(R.id.srNo);
			
			holder.amount = (TextView) convertView
					.findViewById(R.id.collectionAmount);
			
			holder.modeOfpayment = (TextView) convertView
					.findViewById(R.id.collectionMode);
			
			
			holder.transDate = (TextView) convertView
					.findViewById(R.id.transDate);
			
			holder.transId = (TextView) convertView
					.findViewById(R.id.transId);
			
			
//			holder.description = (TextView) convertView
//					.findViewById(R.id.collectionDesc);
			
			holder.delete = (Button) convertView
					.findViewById(R.id.collectionDelete);
			holder.edit = (Button) convertView
					.findViewById(R.id.collectionEdit);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.srNo.setText("" + (position + 1) + ".");
		holder.amount.setText(convertView.getContext().getString(R.string.Rs)+""+((CollectionDataDto) dataArray.get(position)).getAmount());
		holder.modeOfpayment.setText(((CollectionDataDto) dataArray.get(position)).getMode());
		holder.transId.setText(((CollectionDataDto) dataArray.get(position)).getTransId());
		//holder.transDate.setText(((CollectionDataDto) dataArray.get(position)).getTransDate().toString());
		holder.transDate.setText(((CollectionDataDto) dataArray.get(position)).getPaymentDate().toString());
//		holder.description.setText(dataArray.get(position).getDescription());
		
		
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Helper.showAlertDialog(mContext, SSCAlertDialog.WARNING_TYPE, "Confirm!", "Sure want to delete?", mContext.getString(R.string.ok), new SSCAlertDialog.OnSDAlertDialogClickListener() {
					
					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
						dataArray.remove(position);
					 
						((Collections) mContext).refreshList();
					}
				}, true, mContext.getString(R.string.cancel), new SSCAlertDialog.OnSDAlertDialogClickListener() {
					
					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});


			}
			
			
			
			
		});
		holder.edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle myBundle=new Bundle();
				String amount = ((CollectionDataDto) dataArray.get(position)).getAmount();
				myBundle.putString("amount",amount);
				myBundle.putString("paymentMode",((CollectionDataDto) dataArray.get(position)).getMode());
				myBundle.putString("transId",((CollectionDataDto) dataArray.get(position)).getTransId());
				myBundle.putString("transDate",((CollectionDataDto) dataArray.get(position)).getPaymentDate().toString());
				myBundle.putString("description",((CollectionDataDto) dataArray.get(position)).getDescription().toString());
		//		myBundle.putString("displayDate",((CollectionDataDto) dataArray.get(position)).getPaymentDate());
				myBundle.putString("paymentModeId",((CollectionDataDto) dataArray.get(position)).getModeId());
				myBundle.putInt("position", position);
				Intent i = new Intent(mContext, AddCollectionDialog.class);
				i.putExtras(myBundle);
				((Activity) mContext).startActivityForResult(i, 1001);
				
			}
		});
		
		return convertView;

	}

	/**
	 * Static class just holds the items of list view.
	 * 
	 * @author vasingh
	 * 
	 */
	static class ViewHolder {
		TextView srNo, amount, modeOfpayment, transId, transDate, description;
		Button delete,edit;

	}

}
