package com.samsung.ssc;



/***
 * beat management
 * @author ngupta1
 *
 */
public class BeatManagement extends BaseActivity{
 	@Override
	public void init() {/*
		super.init();
		
		setContentView(R.layout.activitydashboard);
		setCurrentContext(BeatManagement.this);
		setCentretext(getStringFromResource(R.string.beatmanagement));
		menuhome=new ArrayList<String>();
		grididhome=new ArrayList<Integer>();
		gridView = (GridView) findViewById(R.id.gridmenu);
		
			if(isOnline()){
				drawUIitems();
				
			}
			else{
				Helper.showErrorAlertDialog(BeatManagement.this, getStringFromResource(R.string.error1), getStringFromResource(R.string.error2),new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface d, int arg1) {
						d.dismiss();
						finish();
					}
				});

			}
		 
		
		
		*/}
	
	@Override
	public boolean validation() {
		return super.validation();
	}
	

	
	
/*	private void drawUIitems(){

		FileReader fr = new FileReader(BeatManagement.this,
				Constants.USERMODULESFILE, new GetDataCallBack() {

					@Override
					public void processResponse(Object result) {
						ArrayList<Module> list = new FetchingdataParser(
								BeatManagement.this)
								.getDisplayModules(result.toString());
						

						ArrayList<Integer> imageidhome = new ArrayList<Integer>();
						if (list.size() > 0) {
							for (Module listdata : list) {

								if (listdata.isIsMandatory()) {
									if (listdata.getModuleCode() == (Constants.MENU_BEATAPPROVAL)) {
										menuhome.add(listdata.getName());
										imageidhome.add(R.drawable.beatapproval);
										grididhome.add(Integer.valueOf(listdata
												.getModuleCode()));

									} else if (listdata.getModuleCode() == (Constants.MENU_BEATCREATION)) {
										menuhome.add(listdata.getName());
										imageidhome.add(R.drawable.beatcreation);
										grididhome.add(Integer.valueOf(listdata
												.getModuleCode()));

									}
								}

							}

							gridView.setAdapter(new ImageAdapter(
									BeatManagement.this, menuhome,imageidhome, grididhome));
							gridView.setOnItemClickListener(new OnItemClickListener() {
								public void onItemClick(AdapterView<?> parent,
										View v, int position, long id) {

									Intent i = null;

									if (v.getId() == Constants.MENU_BEATCREATION) {
										i = new Intent(BeatManagement.this,
												BeatCreation.class);
										startActivity(i);
									} else if (v.getId() == Constants.MENU_BEATAPPROVAL) {
										i = new Intent(BeatManagement.this,
												BeatApproval.class);
										startActivity(i);
									} 

								}
							});
						} else {
							Helper.showErrorAlertDialog(
									BeatManagement.this,
									getStringFromResource(R.string.error3),
									getStringFromResource(R.string.datanotavailable));
								}

					}

				});
		fr.execute();
	
	}*/
		

 
	
	
	
}
