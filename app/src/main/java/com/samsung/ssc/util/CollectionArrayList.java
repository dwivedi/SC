package com.samsung.ssc.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.samsung.ssc.dto.CollectionDataDto;

public class CollectionArrayList {

	private ArrayList<Object> data;

	private static CollectionArrayList instance = new CollectionArrayList();

	private CollectionArrayList() {
		data = new ArrayList<Object>();
	}

	public static CollectionArrayList getInstance() {

		return instance; 

	}

	public void add(CollectionDataDto obj) {
		
		data.add(obj);

	}

	public Object get(int i) {

		return data.get(i);

	}

	public int size() {

		return data.size();

	}
	
	public void remove(int pos){
		data.remove(pos);
	}
	
	public void clear(){
		data.clear();
	}
	
	public void set(int index, CollectionDataDto object){
		data.set(index, object);
	}

	/**
	 * 
	 * @param mData
	 */
	public void addAll(JSONArray mData) {
		
		for (int i = 0; i < mData.length(); i++) {
			try {
				CollectionDataDto modal = new CollectionDataDto();
				
				JSONObject item = mData.getJSONObject(i);
				modal.setAmount(String.valueOf(item.getInt("Amount")));
				modal.setMode(item.getString("PaymentModeName"));
 				modal.setModeId(String.valueOf(item.getInt("PaymentModeID")));
 				modal.setTransId(item.getString("TransactionID"));
 				modal.setDisplayDate(item.getString("PaymentDate"));
				modal.setDescription(item.getString("Comments"));
				//modal.setTransDate(item.getString("PaymentDate"));
 				add(modal);;
				
			 } catch (JSONException e) {
				e.printStackTrace();
			 }
		 }
	 }
	

}
