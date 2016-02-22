package com.samsung.ssc.activitymodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.samsung.ssc.BaseActivity;
import com.samsung.ssc.R;
import com.samsung.ssc.CustomUI.SSCAlertDialog;
import com.samsung.ssc.constants.IntentKey;
import com.samsung.ssc.database.DatabaseHelper;
import com.samsung.ssc.dto.Module;
import com.samsung.ssc.io.FetchingdataParser;
import com.samsung.ssc.sync.SyncUtils;
import com.samsung.ssc.util.OnGridItemClick;

public class ActivityDashboardChildNonStoreWise extends BaseActivity {

	private ArrayList<Module> modulesList;
	private ModuleGridAdapter adapter;
	private Stack<Integer> stackGridItems;
	SSCAlertDialog mDownloadListDialog = null;

	private int subParentID = -1;

	private TextView tv_title;
	private HashMap<Integer, String> name_map = new HashMap<Integer, String>();
	private Bundle mBundle;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.activitydashboard1);

		Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			onBackPressed();
			}
		});



		tv_title = (TextView) findViewById(R.id.tv_title_sdActionBar);

	}

	private void setUpView() {

		stackGridItems = new Stack<Integer>();
		GridView moduleGridView = (GridView) findViewById(R.id.gridmenu);
		super.setParentLayout(moduleGridView);

		adapter = new ModuleGridAdapter();
		moduleGridView.setAdapter(adapter);

		int rootParentModuleID = getIntent().getExtras().getInt(
				IntentKey.KEY_MODULE_ID);
		Module module = (Module)getIntent().getExtras().getParcelable(IntentKey.MOUDLE_POJO);
		getSupportActionBar().setTitle(module.getName());
		modulesList = DatabaseHelper.getConnection(getApplicationContext())
				.getUserModuleByParentId(rootParentModuleID);

		adapter.addAll(modulesList);

		moduleGridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							View convertView, int position, long id) {

						Module modulesDataModal = (Module) parent
								.getItemAtPosition(position);

						onGridItemClick(modulesDataModal);

					}
				});

		ImageButton ib_up = (ImageButton) findViewById(R.id.ib_up_sdActionBar);
		ib_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onBackPressed();

			}
		});

		if (name_map.containsKey(rootParentModuleID)) {

			tv_title.setText(name_map.get(rootParentModuleID));
		} else {
			name_map.put(rootParentModuleID, "Activity");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (stackGridItems != null && stackGridItems.size() > 0) {

			retrivePreviousModules();

		} else {
			setUpView();
		}

	}

	protected void onGridItemClick(Module modulesDataModal) {

		if (modulesDataModal.isQuestionType()) {

			subParentID = -1;

			int parentId = modulesDataModal.getParentModuleID();

			if (!stackGridItems.contains(parentId)) {
				stackGridItems.add(parentId);

			}

			Intent intent = new Intent(ActivityDashboardChildNonStoreWise.this,
					QuestionnaireActivity.class);
			intent.putExtra(IntentKey.MOUDLE_POJO, modulesDataModal);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);

		} else {

			subParentID = modulesDataModal.getModuleID();
			int parentID = modulesDataModal.getParentModuleID();
			ArrayList<Module> subChildModule = DatabaseHelper
					.getConnection(this).getUserModuleByParentId(subParentID);

			if (subChildModule.size() > 0) {

				if (!stackGridItems.contains(parentID)) {
					stackGridItems.push(parentID);

					String module_name = modulesDataModal.getName();
					tv_title.setText(module_name);
					getSupportActionBar().setTitle(module_name);
					name_map.put(subParentID, module_name);
				}
				restoreGridAdapter(subChildModule);

			} else {
				subParentID = -1;
				int parentId = modulesDataModal.getParentModuleID();
				if (!stackGridItems.contains(parentId)) {
					stackGridItems.push(parentId);
				}
				new OnGridItemClick(ActivityDashboardChildNonStoreWise.this,
						mBundle).onActivityItemClick(modulesDataModal);
			}
		}
	}

	@Override
	public void onBackPressed() {

		if (stackGridItems.size() > 0) {
			retrivePreviousModules();

		} else {

			super.onBackPressed();
		}

	}

	private void retrivePreviousModules() {
		subParentID = stackGridItems.pop();

		if (name_map.containsKey(subParentID)) {
			String moduleTitle = name_map.get(subParentID);
			tv_title.setText(moduleTitle);
			getSupportActionBar().setTitle(moduleTitle);
		}

		ArrayList<Module> subChildModule = DatabaseHelper
				.getConnection(this).getUserModuleByParentId(subParentID);
		adapter.clean();
		adapter.addAll(subChildModule);

	}

	private void restoreGridAdapter(ArrayList<Module> subChildModule) {
		adapter.clean();
		adapter.addAll(subChildModule);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (stackGridItems.size() != 0 && subParentID != -1
				&& !stackGridItems.contains(subParentID)) {
			stackGridItems.push(subParentID);
		}
	}

	private class ModuleGridAdapter extends BaseAdapter {
		private ArrayList<Module> dataset;
		private LayoutInflater inflater;

		ModuleGridAdapter() {

			dataset = new ArrayList<Module>();
			this.inflater = LayoutInflater.from(getApplicationContext());

		}

		@Override
		public int getCount() {
			return dataset.size();
		}

		@Override
		public Object getItem(int index) {
			return dataset.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.dashboardview, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Module moduleDisplayDto = dataset.get(position);

			String icon_name = moduleDisplayDto.getIconName();
			if (icon_name != null && !icon_name.isEmpty()) {
				String iconSplitpng = icon_name.split("\\.")[0];

				int resourceID = getResources().getIdentifier(iconSplitpng,
						"drawable", getPackageName());

				if (moduleDisplayDto.isIsMandatory()) {
					if (moduleDisplayDto.isActivityIDAvailable()) {

						if (moduleDisplayDto.getSyncStatus() == SyncUtils.SYNC_STATUS_SUBMIT) {

							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
								holder.icon
										.setBackground(ActivityDashboardChildNonStoreWise.this
												.getResources()
												.getDrawable(
														R.drawable.grid_item_selector_green_border));
							} else {
								holder.icon
										.setBackgroundDrawable(ActivityDashboardChildNonStoreWise.this
												.getResources()
												.getDrawable(
														R.drawable.grid_item_selector_green_border));
							}

						} else if (moduleDisplayDto.getSyncStatus() == SyncUtils.SYNC_STATUS_SAVED) {

							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
								holder.icon
										.setBackground(ActivityDashboardChildNonStoreWise.this
												.getResources()
												.getDrawable(
														R.drawable.grid_item_selector_yellow_border));
							} else {
								holder.icon
										.setBackgroundDrawable(ActivityDashboardChildNonStoreWise.this
												.getResources()
												.getDrawable(
														R.drawable.grid_item_selector_yellow_border));
							}

						}

					} else {

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							holder.icon
									.setBackground(ActivityDashboardChildNonStoreWise.this
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_red_border));
						} else {
							holder.icon
									.setBackgroundDrawable(ActivityDashboardChildNonStoreWise.this
											.getResources()
											.getDrawable(
													R.drawable.grid_item_selector_red_border));
						}

					}

				} else {

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						holder.icon
								.setBackground(ActivityDashboardChildNonStoreWise.this
										.getResources().getDrawable(
												R.drawable.grid_item_selector));
					} else {
						holder.icon
								.setBackgroundDrawable(ActivityDashboardChildNonStoreWise.this
										.getResources().getDrawable(
												R.drawable.grid_item_selector));
					}

				}

				if (resourceID != 0) {

					holder.icon.setImageResource(resourceID);
				} else {
					holder.icon.setImageResource(R.drawable.refresh); // Default
																		// icon
				}
			} else {
				holder.icon.setImageResource(R.drawable.refresh); // Default
																	// icon
			}

			holder.text.setText(moduleDisplayDto.getName());
			// convertView.startAnimation(animation);

			return convertView;
		}

		private class ViewHolder {
			ViewHolder(View convertView) {
				this.text = (TextView) convertView
						.findViewById(R.id.grid_item_label);
				this.icon = (ImageButton) convertView
						.findViewById(R.id.grid_item_image);

				this.icon.setFocusable(false);
				this.icon.setFocusableInTouchMode(false);
				this.icon.setClickable(false);
				this.icon.setEnabled(false);
			}

			TextView text;
			ImageButton icon;
		}

		void addAll(List<Module> moduleDataModals) {

			this.dataset.addAll(moduleDataModals);
			notifyDataSetChanged();
		}

		void clean() {
			this.dataset.clear();

		}

		@SuppressWarnings({ "unchecked", "unused" })
		public ArrayList<Module> getAll() {
			return (ArrayList<Module>) this.dataset.clone();
		}

	}

	protected void parseFileResponse(Object result) {
		new FetchingdataParser(this).getDisplayModules(result.toString());

	}

}
