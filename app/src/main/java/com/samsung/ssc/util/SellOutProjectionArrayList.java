package com.samsung.ssc.util;

import java.util.ArrayList;

import com.samsung.ssc.dto.SKUProductList;

public class SellOutProjectionArrayList {

	private ArrayList<SKUProductList> data;

	private static SellOutProjectionArrayList instance = new SellOutProjectionArrayList();

	private SellOutProjectionArrayList() {
		data = new ArrayList<SKUProductList>();
	}

	public static SellOutProjectionArrayList getInstance() {

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
