package com.samsung.ssc.util;

import java.util.ArrayList;

import com.samsung.ssc.dto.SKUProductList;

public class StockEscalationArrayList {

	private ArrayList<SKUProductList> data;

	private static StockEscalationArrayList instance = new StockEscalationArrayList();

	private StockEscalationArrayList() {
		data = new ArrayList<SKUProductList>();
	}

	public static StockEscalationArrayList getInstance() {

		return instance;

	}

	public void add(SKUProductList obj) {

		data.add(obj);

	}
	
	
	public void addAll(ArrayList<SKUProductList> obj) {

		data.addAll(obj);

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
	
	public void removeAll(){
		data.clear();
	}
	

}
