package com.samsung.ssc.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.CompetitionDto;
import com.samsung.ssc.dto.CompetitionQuestions;
import com.samsung.ssc.util.Helper;
import com.samsung.ssc.util.SingletonHashMap;

/**
 * Class will used to fill the play list items, like sound clip name , Time,
 * pencil icon etc.
 * 
 * @author vasingh
 */
public class CompetitionAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ViewHolder holder;
	private ArrayList<CompetitionQuestions> questions;
	private SingletonHashMap mMap = SingletonHashMap.getInstance();
	private String optionKey;
	
	public CompetitionAdapter(Context context, ArrayList<CompetitionQuestions> questions, int optionType) {
		inflater = LayoutInflater.from(context);
		this.questions = questions;
		if(optionType == 1){
			optionKey = "_dispShare";
		}else if(optionType == 2){
			optionKey = "_CountrShare";
		}

	}

	@Override
	public int getCount() {
		return questions.size();
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
		holder = new ViewHolder();

		/*	if (convertView == null) {*/

			
			convertView = inflater.inflate(R.layout.competition_items, null);
			holder.question = (TextView) convertView.findViewById(R.id.questions);
			holder.brand1 = (EditText) convertView.findViewById(R.id.brand_1);
			holder.brand2 = (EditText) convertView.findViewById(R.id.brand_2);
			holder.brand3 = (EditText) convertView.findViewById(R.id.brand_3);
			holder.brand4 = (EditText) convertView.findViewById(R.id.brand_4);
			convertView.setTag(holder);
			
		/*} else {
			holder = (ViewHolder) convertView.getTag();
		}*/
		
		EditTextWatcher1 watcher1 = new EditTextWatcher1();
		holder.brand1.addTextChangedListener(watcher1);
		watcher1.setTarget(position);
		
		EditTextWatcher2 watcher2 = new EditTextWatcher2();
		holder.brand2.addTextChangedListener(watcher2);
		watcher2.setTarget(position);
		
		EditTextWatcher3 watcher3 = new EditTextWatcher3();
		holder.brand3.addTextChangedListener(watcher3);
		watcher3.setTarget(position);
		
		EditTextWatcher4 watcher4 = new EditTextWatcher4();
		holder.brand4.addTextChangedListener(watcher4);
		watcher4.setTarget(position);
		
		
		holder.question.setText(questions.get(position).getQuestion().toString());
		
		
		try {
		
		String key = questions.get(position).getQuestionID() + optionKey;
		
		if (mMap.size() != 0){
			
			if(mMap.containsKey(key)){
				if(mMap.getValue(key).getBrand1()==0){
					holder.brand1.setText("");
				}else {
					holder.brand1.setText("" + mMap.getValue(key).getBrand1());
				}
				
				
				if(mMap.getValue(key).getBrand2()==0){
					holder.brand2.setText("");
				}else {
					holder.brand2.setText("" + mMap.getValue(key).getBrand2());
				}
				
				
				if(mMap.getValue(key).getBrand3()==0){
					holder.brand3.setText("");
				}else {
					holder.brand3.setText("" + mMap.getValue(key).getBrand3());
				}
				
				if(mMap.getValue(key).getBrand4()==0){
					holder.brand4.setText("");
				}else {
					holder.brand4.setText("" + mMap.getValue(key).getBrand4());
				}
				

				Helper.printLog("Brand1", "" + mMap.getValue(key).getBrand1());
				Helper.printLog("Brand2", "" + mMap.getValue(key).getBrand2());
				Helper.printLog("Brand3", "" + mMap.getValue(key).getBrand3());
				Helper.printLog("Brand4", "" + mMap.getValue(key).getBrand4());
			}
			
		}
		}
		catch (Exception e) {
			Helper.printLog("getView Competition adapter", "" + e.getMessage());
		}
				
		return convertView;

	}

	
	/**
	 * Static class just holds the items of list view.
	 * 
	 * @author vasingh
	 * 
	 */
	static class ViewHolder {
		TextView question;
		EditText brand1, brand2, brand3, brand4;

	}
	
	
	
	private class EditTextWatcher1 implements TextWatcher{
		private int position;

	    private void setTarget(int target) {
	        this.position = target;
	    }
		@Override
		public void afterTextChanged(Editable s) {
			
			
			CompetitionDto obj = new CompetitionDto();
				
				if(!Helper.isNullOrEmpty(s.toString()) && !(s.toString().equals("0"))){
					try{
						obj.setBrand1(Integer.parseInt(s.toString()));
					}catch(Exception e){
						
						Helper.printLog("Error", e.getMessage());
						
					}
				}
				if(mMap.size() != 0){
					
					String key = questions.get(position).getQuestionID() + optionKey;
					
					if(mMap.containsKey(key)){
						
						try{
							
							if(mMap.containsKey(key) && mMap.getValue(key).getBrand2() != 0){
								obj.setBrand2(mMap.getValue(key).getBrand2());	
							}
							if(mMap.containsKey(key) && mMap.getValue(key).getBrand3() != 0){
								obj.setBrand3(mMap.getValue(key).getBrand3());	
							}
							if(mMap.containsKey(key) && mMap.getValue(key).getBrand4() != 0){
								obj.setBrand4(mMap.getValue(key).getBrand4());	
							}
						}catch(Exception e){
							Helper.printLog("Error", e.getMessage());
						}
						
					}
					
					
			}

			mMap.putValue(questions.get(position).getQuestionID() + optionKey, obj);
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
	}
	
	private class EditTextWatcher2 implements TextWatcher{
		private int position;
		

	    private void setTarget(int target) {
	        this.position = target;
	    }
	 
		@Override
		public void afterTextChanged(Editable s) {
			
			
			CompetitionDto obj = new CompetitionDto();
				
				if(!Helper.isNullOrEmpty(s.toString()) && !(s.toString().equals("0"))){
					try{
						obj.setBrand2(Integer.parseInt(s.toString()));
					}catch(Exception e){
						Helper.printLog("Error", e.getMessage());
					}
				}
				if(mMap.size() != 0){
					
					String key = questions.get(position).getQuestionID() + optionKey;
					
					try{
					
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand1() != 0){
						obj.setBrand1(mMap.getValue(key).getBrand1());	
					}
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand3() != 0){
						obj.setBrand3(mMap.getValue(key).getBrand3());	
					}
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand4() != 0){
						obj.setBrand4(mMap.getValue(key).getBrand4());	
					}
				}catch(Exception e){
					Helper.printLog("Error", e.getMessage());
				}
			}

			mMap.putValue(questions.get(position).getQuestionID() + optionKey, obj);
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
	}
	private class EditTextWatcher3 implements TextWatcher{
		private int position;
		

	    private void setTarget(int target) {
	        this.position = target;
	    }
	 
		@Override
		public void afterTextChanged(Editable s) {
			
			
			CompetitionDto obj = new CompetitionDto();
				
				if(!Helper.isNullOrEmpty(s.toString()) && !(s.toString().equals("0"))){
					try{
						obj.setBrand3(Integer.parseInt(s.toString()));
					}catch(Exception e){
						Helper.printLog("Error", e.getMessage());
					}
				}
				if(mMap.size() != 0){
					
					String key = questions.get(position).getQuestionID() + optionKey;
					
					try{
					
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand2() != 0){
						obj.setBrand2(mMap.getValue(key).getBrand2());	
					}
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand1() != 0){
						obj.setBrand1(mMap.getValue(key).getBrand1());	
					}
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand4() != 0){
						obj.setBrand4(mMap.getValue(key).getBrand4());	
					}
				}catch(Exception e){
					Helper.printLog("Error", e.getMessage());
				}
			}

			mMap.putValue(questions.get(position).getQuestionID() + optionKey, obj);
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
	}
	
	private class EditTextWatcher4 implements TextWatcher{
		private int position;
		

	    private void setTarget(int target) {
	        this.position = target;
	    }
	 
		@Override
		public void afterTextChanged(Editable s) {
			
			
			CompetitionDto obj = new CompetitionDto();
				
				if(!Helper.isNullOrEmpty(s.toString()) && !(s.toString().equals("0"))){
					try{
						obj.setBrand4(Integer.parseInt(s.toString()));
					}catch(Exception e){
						Helper.printLog("Error", e.getMessage());
					}
				}
				if(mMap.size() != 0){
					
					String key = questions.get(position).getQuestionID() + optionKey;
					
					try{
					
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand2() != 0){
						obj.setBrand2(mMap.getValue(key).getBrand2());	
					}
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand3() != 0){
						obj.setBrand3(mMap.getValue(key).getBrand3());	
					}
					if(mMap.containsKey(key) && mMap.getValue(key).getBrand1() != 0){
						obj.setBrand1(mMap.getValue(key).getBrand1());	
					}
				}catch(Exception e){
					Helper.printLog("Error", e.getMessage());
				}
			}

			mMap.putValue(questions.get(position).getQuestionID() + optionKey, obj);
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
	}

  }
