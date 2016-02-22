package com.samsung.ssc.activitymodule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.activitymodule.CounterShareDisplayShareActivity.MyAdapter.ViewHolderGroup;
import com.samsung.ssc.activitymodule.RaceProductAuditCartActivity.GetProductAuditHandler;
import com.samsung.ssc.database.DatabaseUtilMethods;
import com.samsung.ssc.database.DatabaseConstants.RacePOSMDataResponseMasterColumns;
import com.samsung.ssc.dto.RaceProductAddToCartDTO;
import com.samsung.ssc.provider.ProviderContract;
import com.samsung.ssc.util.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.DeletedContacts;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RaceAVCartFragment extends Fragment {

	private View _rootView;
	private TextView mTextViewNoItem;
	private ExpandableListView mParentExpandableListView,
			mRowExpandableListView;

	private RaceProductAuditCartActivity1   parentActivty;

	private HashMap<String, HashMap<Integer, ArrayList<RaceProductAddToCartDTO>>> category_map;

	private ParentLevelAdapter parentAdpater;

	private String[] categotyArr;
	private LayoutInflater inflater;

	private boolean[] parentExpListViewGroupState;

	HashMap<Integer, ExpandableListView> mapInnerExpanddabelListViewPerCategoryCategoryWise = new HashMap<Integer, ExpandableListView>();
	HashMap<Integer, boolean[]> mapRowExpandableStateHashMap;
	
	private boolean firstTime=true;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		parentActivty = (RaceProductAuditCartActivity1) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (_rootView == null) {
			_rootView = inflater.inflate(R.layout.product_audit_av_cart,
					container, false);

		} else {
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		}

	
		
		return _rootView;

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		setUpView();	
	
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}
	
	
	
	static class GetProductAuditHandler extends Handler {

		WeakReference<RaceAVCartFragment> avCartFragment;

		public GetProductAuditHandler(RaceAVCartFragment fms) {

			avCartFragment = new WeakReference<RaceAVCartFragment>(fms);
		}

		@Override
		public void handleMessage(Message msg) {

			boolean data_available = msg.getData().getBoolean("data_available");

			if (data_available) {

				final RaceAVCartFragment fragmentReference = avCartFragment
						.get();

				if (fragmentReference != null) {
					if (fragmentReference.category_map == null) {
						fragmentReference.mTextViewNoItem
								.setVisibility(View.VISIBLE);
					} else if (fragmentReference.category_map != null
							&& fragmentReference.category_map.isEmpty()) {
						fragmentReference.mTextViewNoItem
								.setVisibility(View.VISIBLE);
					} else {
						fragmentReference.mTextViewNoItem
						.setVisibility(View.GONE);  //SDCE-4377
						try {
							fragmentReference.categotyArr = fragmentReference.category_map
									.keySet()
									.toArray(
											new String[fragmentReference.category_map
													.size()]);

							Arrays.sort(fragmentReference.categotyArr);

							fragmentReference.parentAdpater = fragmentReference.new ParentLevelAdapter();

							fragmentReference.mParentExpandableListView
									.setAdapter(fragmentReference.parentAdpater);

							/*
							 * activityRefrence.productCartAdapter
							 * .addItems(activityRefrence.cartItemList);
							 */
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			}

		};

	}

	
	
	private void setUpView() {
		
		mTextViewNoItem = (TextView) _rootView
				.findViewById(R.id.tv_no_item_product_audit_cart);

		mParentExpandableListView = (ExpandableListView) _rootView
				.findViewById(R.id.el_parentLevel);

		this.inflater = LayoutInflater.from(parentActivty);

		if (parentActivty.mActivityID != -1) {
			getProductAuditFromDatabase(new GetProductAuditHandler(this));
		}

	}

	
	
	private void getProductAuditFromDatabase(final Handler handler) {
		Thread mThread = new Thread() {
			@Override
			public void run() {

				try {

					/*
					 * cartItemList = DatabaseHelper.getConnection(
					 * getApplicationContext())
					 * .getProductAuditAddToCartItemDetail(mActivityID);
					 */

					Cursor cursor = parentActivty
							.getContentResolver()
							.query(ProviderContract.URI_RACE_PRODUCT_AUDIT_CART_AV_PRODUCTS,
									null,
									null,
									new String[] { String
											.valueOf(parentActivty.mActivityID) },
									null);

					if (cursor != null) {

						category_map = DatabaseUtilMethods
								.getProductAuditCartForAVFromCursor(cursor);
						cursor.close();
					}

					Message msg = handler.obtainMessage();
					Bundle bundle = new Bundle();

					bundle.putBoolean("data_available", true);

					msg.setData(bundle);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		mThread.start();

	}

	class ParentLevelAdapter extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int arg0, int arg1) {
			return category_map.get(categotyArr[arg0]);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			mRowExpandableListView = new CustExpListview(parentActivty);

			ExpandableListAdapter expandableAdapter = new RowExpandableListAdapter(
					groupPosition);

			mRowExpandableListView.setAdapter(expandableAdapter);

			mapInnerExpanddabelListViewPerCategoryCategoryWise.put(
					groupPosition, mRowExpandableListView);

			return mRowExpandableListView;

		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// return category_map.get(categotyArr[groupPosition]).size();
			// FIX : return 1 so that getChildView called only once
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			try {
				return category_map.size();  //SDCE-4357
			} catch (Exception e) {
				return 0;
			}
			
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
			restoreUIState();

		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			try {
				ViewHolderGroup holderGroup;

				if (convertView == null) {

					convertView = inflater.inflate(
							R.layout.expandable_group_layout_race, null);
					holderGroup = new ViewHolderGroup(convertView);
					convertView.setTag(holderGroup);

				} else {
					holderGroup = (ViewHolderGroup) convertView.getTag();
				}

				String category = categotyArr[groupPosition];
				int rowSize = category_map.get(category).size();

				holderGroup.tvGroupTiitle.setText(category);
				holderGroup.tvRowCount.setText(rowSize
						+ (rowSize > 1 ? " Rows" : " Row"));

				int noOfProducts = getNoOfProducts(category);
				holderGroup.tvProductCount.setText(noOfProducts
						+ (noOfProducts > 1 ? " Products" : " Product"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			return convertView;
		}

		class ViewHolderGroup {

			TextView tvGroupTiitle;
			TextView tvRowCount;
			TextView tvProductCount;

			public ViewHolderGroup(View convertView) {

				this.tvGroupTiitle = (TextView) convertView
						.findViewById(R.id.tvGroupName);
				this.tvRowCount = (TextView) convertView
						.findViewById(R.id.tvRowCount);

				this.tvProductCount = (TextView) convertView
						.findViewById(R.id.tvProductCount);
			}

		}

		@Override
		public boolean hasStableIds() {
			
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			
			return true;
		}

	}

	private int getNoOfProducts(String category) {
		int value = 0;
		HashMap<Integer, ArrayList<RaceProductAddToCartDTO>> rowMap = category_map
				.get(category);
		for (Map.Entry<Integer, ArrayList<RaceProductAddToCartDTO>> entry : rowMap
				.entrySet()) {
			value = value + entry.getValue().size();
		}
		return value;

	}

	public class CustExpListview extends ExpandableListView {

		public CustExpListview(Context context) {
			super(context);
		}

		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(parentActivty
					.getResources().getDisplayMetrics().widthPixels,
					MeasureSpec.AT_MOST);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(7000,
					MeasureSpec.AT_MOST);   // SDCE-4377
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

	}

	public class RowExpandableListAdapter extends BaseExpandableListAdapter {

		int parentGroupPosition;
		Integer[] rowArr;

		public RowExpandableListAdapter(int groupPosition) {
			this.parentGroupPosition = groupPosition;
			int rowMapSize = category_map.get(categotyArr[parentGroupPosition])
					.size();
			rowArr = category_map.get(categotyArr[parentGroupPosition])
					.keySet().toArray(new Integer[rowMapSize]);
			Arrays.sort(rowArr);

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {

			ViewHolderProduct holderGroup;

			if (convertView == null) {

				convertView = inflater.inflate(
						R.layout.race_product_add_cart_list_item, null);
				holderGroup = new ViewHolderProduct(convertView);
				convertView.setTag(holderGroup);

			} else {
				holderGroup = (ViewHolderProduct) convertView.getTag();
			}

			final RaceProductAddToCartDTO product = category_map
					.get(categotyArr[parentGroupPosition])
					.get(rowArr[groupPosition]).get(childPosition);

			holderGroup.tvProductName.setText(product.productName);

			holderGroup.ivCross.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					onDeleteClick(product.stockAuditID,
							categotyArr[parentGroupPosition],
							rowArr[groupPosition], childPosition);

				}
			});

			convertView.setClickable(true);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(parentActivty,
							RaceProductAuditCartItemUpdateActivity.class);
					intent.putExtra("STOCK_AUDIT_ID", product.stockAuditID);
					intent.putExtra("PRODUCT_NAME", product.productName);
					startActivity(intent);
				
					
				}
			});

			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return category_map.get(categotyArr[parentGroupPosition])
					.get(rowArr[groupPosition]).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			return category_map.get(categotyArr[parentGroupPosition]).size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			/*
			 * ViewHolderGroup holderGroup;
			 * 
			 * if (convertView == null) {
			 * 
			 * convertView = inflater.inflate( R.layout.expandable_group_layout,
			 * null); holderGroup = new ViewHolderGroup(convertView);
			 * convertView.setTag(holderGroup);
			 * 
			 * } else { holderGroup = (ViewHolderGroup) convertView.getTag(); }
			 * 
			 * ((TextView) convertView).setText("Row "+rowArr[groupPosition]);
			 * 
			 * return convertView;
			 */

			ViewHolderGroup holderGroup;

			if (convertView == null) {

				convertView = inflater.inflate(
						R.layout.expandable_row_layout_race, null);
				holderGroup = new ViewHolderGroup(convertView);
				convertView.setTag(holderGroup);

			} else {
				holderGroup = (ViewHolderGroup) convertView.getTag();
			}
			
			  String rowcount = rowArr[groupPosition].toString();
			  if (rowcount.equalsIgnoreCase("-1")) {
				  holderGroup.tvGroupTiitle.setText("Row ");
				  
			} else {
				holderGroup.tvGroupTiitle.setText("Row "+rowcount);
			}
			

			int productSize = category_map
					.get(categotyArr[parentGroupPosition])
					.get(rowArr[groupPosition]).size();

			holderGroup.tvProductCount.setText(productSize
					+ (productSize > 1 ? " Products" : " Product"));

			return convertView;

		}

		class ViewHolderGroup {

			TextView tvGroupTiitle;

			TextView tvProductCount;

			public ViewHolderGroup(View convertView) {

				this.tvGroupTiitle = (TextView) convertView
						.findViewById(R.id.tvGroupNameRowLayout);
				this.tvProductCount = (TextView) convertView
						.findViewById(R.id.tvProductCountRowLayout);

			}

		}

		class ViewHolderProduct {

			TextView tvProductName;

			ImageView ivCross;

			public ViewHolderProduct(View convertView) {

				this.tvProductName = (TextView) convertView
						.findViewById(R.id.tvListItemRaceProductName);
				this.ivCross = (ImageView) convertView
						.findViewById(R.id.ivListItenRaceProductCrossBtn);

			}

		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	public void onDeleteClick(final int stockAudit, final String categoryName,
			final int rowID, final int productPosition) {
		Helper.showAlertDialog(
				parentActivty,
				SSCAlertDialog.WARNING_TYPE,
				parentActivty
						.getString(R.string.sync_permission_dialog_button_two),
				parentActivty
						.getString(R.string.are_you_sure_you_want_to_delete),
				parentActivty.getString(R.string.yes),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {

						/*
						 * DatabaseHelper.getConnection(context)
						 * .deleteProductAuditResponse(stockAudit);
						 * DatabaseHelper.getConnection(context)
						 * .deleteProductAuditPosmResponse( stockAudit);
						 */

						parentActivty
								.getContentResolver()
								.delete(ProviderContract.URI_RACE_PRODUCT_AUDIT_RESPONSE,
										RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID
												+ " = ?",
										new String[] { stockAudit + "" });

						parentActivty
								.getContentResolver()
								.delete(ProviderContract.URI_RACE_POSM_RESPONSE,
										RacePOSMDataResponseMasterColumns.KEY_STOCK_AUDIT_ID
												+ " = ?",
										new String[] { stockAudit + "" });

						category_map.get(categoryName).get(rowID)
								.remove(productPosition);

						if (parentAdpater != null) {
							saveUIState();
							parentAdpater.notifyDataSetChanged();

						}

						if (category_map.isEmpty()) {
							mTextViewNoItem.setVisibility(View.VISIBLE);

							/*
							 * DatabaseHelper .getConnection(context)
							 * .deleteActvityDataMaster( mActivityID,
							 * DatabaseHelper .getConnection( context)
							 * .getWritableDatabase());
							 */

							parentActivty
									.getContentResolver()
									.delete(ProviderContract.URI_ACTIVITY_DATA_RESPONSE,
											RacePOSMDataResponseMasterColumns.KEY_ACTIVITY_DATA_MASTER_ACTIVITY_ID
													+ "=?",
											new String[] { String
													.valueOf(parentActivty.mActivityID) });

						}

						sdAlertDialog.dismiss();
					}
				}, true, parentActivty.getString(R.string.no),
				new SSCAlertDialog.OnSDAlertDialogClickListener() {

					@Override
					public void onClick(SSCAlertDialog sdAlertDialog) {
						sdAlertDialog.dismiss();
					}
				});
	}

	private void saveUIState() {

		int numberOfCategoryGroups = parentAdpater.getGroupCount();

		parentExpListViewGroupState = new boolean[numberOfCategoryGroups];

		mapRowExpandableStateHashMap = new HashMap<Integer, boolean[]>();

		for (int i = 0; i < numberOfCategoryGroups; i++) {
			parentExpListViewGroupState[i] = mParentExpandableListView
					.isGroupExpanded(i);

		/*	// state for each row
			ExpandableListView expandableListView = mapInnerExpanddabelListViewPerCategoryCategoryWise
					.get(i);

			// returns null if parent group is not expanded as child expandable
			// view added dynamically on onexpand
			if (expandableListView != null) {
				ExpandableListAdapter adapter = expandableListView
						.getExpandableListAdapter();

				int noOfRowsPerCategory = adapter.getGroupCount();
				boolean[] arrRowStateForEachCategory = new boolean[noOfRowsPerCategory];

				for (int j = 0; j < noOfRowsPerCategory; j++) {

					boolean isExpanded = expandableListView.isGroupExpanded(j);
					arrRowStateForEachCategory[j] = isExpanded;

				}

				mapRowExpandableStateHashMap.put(i, arrRowStateForEachCategory);

			}
*/
		}

	}

	private void restoreUIState() {

		for (int i = 0; i < parentExpListViewGroupState.length; i++) {

			if (parentExpListViewGroupState[i] == true) {
				mParentExpandableListView.expandGroup(i);
			}

		}

	/*	// this is called after parent group expanded as dynamic child row
		// expandble created on parent group expand
		// After group expanded we expand the child expandable listview which
		// are added dyamically
		for (int i = 0; i < parentExpListViewGroupState.length; i++) {

			if (parentExpListViewGroupState[i] == true) {
				// get inner expandable view for a category
				ExpandableListView innerExpandableListView = mapInnerExpanddabelListViewPerCategoryCategoryWise
						.get(i);

				// returns null if parent group is not expanded as child
				// expandable view added dynamically on onexpand
				if (innerExpandableListView != null) {
					// get saved sates for all group inside the inner expandable
					// view
					boolean[] rowStates = mapRowExpandableStateHashMap.get(i);

					// restore the states of groups of inner expandable view
					for (int j = 0; j < rowStates.length; j++) {
						if (rowStates[j] == true) {
							innerExpandableListView.expandGroup(j);
						}

					}
				}

			}

		}
*/
	}

}
