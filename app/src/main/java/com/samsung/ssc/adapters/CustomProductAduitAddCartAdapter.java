package com.samsung.ssc.adapters;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCProgressDialog;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMDataResponseMasterColumns;
import com.samsung.ssc.dto.RaceProductAddToCartDTO;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

public class CustomProductAduitAddCartAdapter extends BaseAdapter {

	private ArrayList<RaceProductAddToCartDTO> raceProductAddToCartList;
	private LayoutInflater inflater;
	private Context context;
	TextView tvNoItem;

	private ProgressDialog progressDialog;
	private long mActivityID;

	protected void setProgresDialogProperties(String message) {

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			/*
			 * progressDialog = new ProgressDialog(context,
			 * ProgressDialog.THEME_HOLO_LIGHT);
			 */

			progressDialog = SSCProgressDialog.ctor(context);

		} else {
			progressDialog = new ProgressDialog(context);
			progressDialog.setProgress(0);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(message);
			progressDialog.setCancelable(false);
		}

	}

	public CustomProductAduitAddCartAdapter(Context context, TextView tvNoItem,
			long mActivityID) {

		this.raceProductAddToCartList = new ArrayList<RaceProductAddToCartDTO>();
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.tvNoItem = tvNoItem;
		this.mActivityID = mActivityID;
	}

	@Override
	public int getCount() {
		return raceProductAddToCartList.size();
	}

	@Override
	public Object getItem(int position) {
		return raceProductAddToCartList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	public void addItems(ArrayList<RaceProductAddToCartDTO> raceProductData) {

		raceProductAddToCartList.clear();

		if (raceProductData != null) {
			raceProductAddToCartList.addAll(raceProductData);
		}
		notifyDataSetChanged();

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			vi = inflater.inflate(R.layout.race_product_add_cart_list_item,
					null);
			holder = new ViewHolder();
			holder.txtProductName = (TextView) vi
					.findViewById(R.id.tvListItemRaceProductName);
			holder.ivCrossButton = (ImageView) vi
					.findViewById(R.id.ivListItenRaceProductCrossBtn);

			vi.setTag(holder);

		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.txtProductName.setText(raceProductAddToCartList.get(position)
				.getProductName());
		holder.ivCrossButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Helper.showAlertDialog(
						context,
						SSCAlertDialog.WARNING_TYPE,
						context.getString(R.string.sync_permission_dialog_button_two),
						context.getString(R.string.are_you_sure_you_want_to_delete),
						context.getString(R.string.yes),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {

								int stockAudit = raceProductAddToCartList.get(
										position).getStockAuditid();

								/*
								 * DatabaseHelper.getConnection(context)
								 * .deleteProductAuditResponse(stockAudit);
								 * DatabaseHelper.getConnection(context)
								 * .deleteProductAuditPosmResponse( stockAudit);
								 */

								context.getContentResolver()
										.delete(ProviderContract.URI_RACE_PRODUCT_AUDIT_RESPONSE,
												RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID
														+ " = ?",
												new String[] { stockAudit + "" });

								context.getContentResolver()
										.delete(ProviderContract.URI_RACE_POSM_RESPONSE,
												RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID
														+ " = ?",
												new String[] { stockAudit + "" });

								raceProductAddToCartList.remove(position);
								notifyDataSetChanged();

								if (raceProductAddToCartList.isEmpty()) {
									tvNoItem.setVisibility(View.VISIBLE);

									/*
									 * DatabaseHelper .getConnection(context)
									 * .deleteActvityDataMaster( mActivityID,
									 * DatabaseHelper .getConnection( context)
									 * .getWritableDatabase());
									 */

									context.getContentResolver()
											.delete(ProviderContract.URI_ACTIVITY_DATA_RESPONSE,
													RacePOSMDataResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
															+ "=?",
													new String[] { String
															.valueOf(mActivityID) });

								}

								sdAlertDialog.dismiss();
							}
						},
						true,
						context.getString(R.string.no),
						new SSCAlertDialog.OnSDAlertDialogClickListener() {

							@Override
							public void onClick(
									SSCAlertDialog sdAlertDialog) {
								sdAlertDialog.dismiss();
							}
						});

			}
		});

		return vi;

	}

	class ViewHolder {
		TextView txtProductName;
		ImageView ivCrossButton;
	}
}
