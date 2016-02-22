package com.samsung.ssc.adapters;

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.activitymodule.SellOutProjection;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseConstants.ActivityDataMasterColumns;
import com.samsung.ssc.dto.SKUProductList;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.SellOutProjectionArrayList;

public class SellOutProjectionAddAdapter extends BaseAdapter {
	private final Context context;
	private AlertDialog helpDialog;
	private long mActivtyID;

	private SellOutProjectionArrayList sellOutProjectionData = SellOutProjectionArrayList
			.getInstance();

	private DecimalFormat decimalFormat = new DecimalFormat("0.00");

	public SellOutProjectionAddAdapter(Context pContext, long activityID) {
		context = pContext;
		mActivtyID = activityID;
	}

	@Override
	public int getCount() {
		return sellOutProjectionData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	} 

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.bookorderadditem, parent,
				false);
		TextView sNo = (TextView) rowView.findViewById(R.id.sr);
		TextView product = (TextView) rowView.findViewById(R.id.product);
		TextView qty = (TextView) rowView.findViewById(R.id.quantity);
		TextView value = (TextView) rowView.findViewById(R.id.value);
		Button delete = (Button) rowView.findViewById(R.id.close);
		Button edit = (Button) rowView.findViewById(R.id.edit);

		sNo.setText(String.valueOf(position + 1));
		product.setText(((SKUProductList) sellOutProjectionData.get(position))
				.getSKUCode().toString());
		qty.setText(((SKUProductList) sellOutProjectionData.get(position))
				.getQuantity().toString());
		value.setText(((SKUProductList) sellOutProjectionData.get(position))
				.getDealerPrice().toString());

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (sellOutProjectionData.size() == 1 && mActivtyID != -1) {

					Helper.showAlertDialog(
							context,
							SSCAlertDialog.WARNING_TYPE,
							context.getString(R.string.confirmationmessage),
							context.getString(R.string.delete_all_confirmation),
							context.getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();

								/*	boolean result = DatabaseHelper
											.getConnection(context)
											.deleteDataFromOrderResponseAndActivityDataMasterTable(
													mActivtyID);*/
									
									
									
									String whereClause = ActivityDataMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
											+ "=?";
									String[] whereArgs = new String[] { String.valueOf(mActivtyID) };
									
									int rowEffectedInOrderTable = context.getContentResolver().delete(ProviderContract.DELETE_STOCK_ESCALATION_ORDER_RESPONSE, whereClause, whereArgs);
									int rowEffectedInActivtyDataTable = context.getContentResolver().delete(ProviderContract.URI_ACTIVITY_DATA_RESPONSE, whereClause, whereArgs);
									
									 
									if (rowEffectedInOrderTable != 0
											&& rowEffectedInActivtyDataTable != 0) {
										sellOutProjectionData.removeAll();

										notifyDataSetChanged();

										if (sellOutProjectionData.size() == 0) {
											parent.setVisibility(View.GONE);
										}

										SellOutProjection sellOutProjection = (SellOutProjection) context;
										sellOutProjection.refreshTotal();
									}

									else {

										Helper.showCustomToast(context,
												R.string.delete_all_fail,
												Toast.LENGTH_LONG);

									}

								}
							},
							true,
							context.getString(R.string.cancel),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();
								}
							});

				}

				else {

					Helper.showAlertDialog(
							context,
							SSCAlertDialog.WARNING_TYPE,
							context.getString(R.string.confirmationmessage),
							context.getString(R.string.delete_msg),
							context.getString(R.string.ok),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();

									sellOutProjectionData.remove(position);

									notifyDataSetChanged();

									if (sellOutProjectionData.size() == 0) {
										parent.setVisibility(View.GONE);
									}

									SellOutProjection selloutProjection = (SellOutProjection) context;
									selloutProjection.refreshTotal();

								}
							},
							true,
							context.getString(R.string.cancel),
							new SSCAlertDialog.OnSDAlertDialogClickListener() {

								@Override
								public void onClick(
										SSCAlertDialog sdAlertDialog) {
									sdAlertDialog.dismiss();
								}
							});
				}
			}
		});

		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showPopUp(position);

			}
		});

		return rowView;
	}

	public SellOutProjectionArrayList getItems() {

		return sellOutProjectionData;
	}

	private void showPopUp(final int position) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View PopupLayout = inflater.inflate(R.layout.bookordereditdialog,
				null);
		TextView sNO = (TextView) PopupLayout.findViewById(R.id.sredit);
		TextView product = (TextView) PopupLayout
				.findViewById(R.id.productedit);
		final EditText qty = (EditText) PopupLayout.findViewById(R.id.qtyedit);
		TextView value = (TextView) PopupLayout.findViewById(R.id.valueedit);
		Button okButton = (Button) PopupLayout.findViewById(R.id.editok);
		Button cancelButton = (Button) PopupLayout
				.findViewById(R.id.editcancel);
		sNO.setText(String.valueOf(1));
		product.setText(((SKUProductList) sellOutProjectionData.get(position))
				.getSKUCode().toString());
		qty.setText(((SKUProductList) sellOutProjectionData.get(position))
				.getQuantity().toString());
		Double dpValue = Double.valueOf(((SKUProductList) sellOutProjectionData
				.get(position)).getDealerPrice().toString());
		Double dValueqty = Double
				.valueOf(((SKUProductList) sellOutProjectionData.get(position))
						.getQuantity().toString());
		final Double dealerPrice = dpValue / dValueqty;

		// value.setText(String.valueOf(dealerPrice));
		value.setText(decimalFormat.format(dealerPrice));
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);

		helpBuilder.setView(PopupLayout);
		helpDialog = helpBuilder.create();
		helpDialog.setCancelable(false);
		helpDialog.setInverseBackgroundForced(true);
		helpDialog.show();
		// helpDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
		// WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (qty.getText().toString().trim().equals("")) {

					Helper.showCustomToast(context, R.string.qtyempty,
							Toast.LENGTH_LONG);

					return;
				}

				else if (qty.getText().toString().trim().equals("0")) {
					Helper.showCustomToast(context,
							R.string.please_enter_value_other_then_zero,
							Toast.LENGTH_LONG);
					return;
				}

				((SKUProductList) sellOutProjectionData.get(position))
						.setQuantity(qty.getText().toString().trim());

				int qty = Integer
						.valueOf(((SKUProductList) sellOutProjectionData
								.get(position)).getQuantity().toString());

				double price = Double.valueOf(dealerPrice);
				double finalPrice = (qty * price);

				// String result = String.valueOf(finalPrice);
				String result = decimalFormat.format(finalPrice);

				((SKUProductList) sellOutProjectionData.get(position))
						.setDealerPrice(result);
				notifyDataSetChanged();

				SellOutProjection order = (SellOutProjection) context;
				order.refreshTotal();

				helpDialog.dismiss();

			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				helpDialog.dismiss();
			}
		});

	}

}
