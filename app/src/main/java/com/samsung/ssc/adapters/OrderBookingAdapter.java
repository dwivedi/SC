package com.samsung.ssc.adapters;


import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.samsung.ssc.R;
import com.samsung.ssc.dto.SKUProductList;

public class OrderBookingAdapter extends ArrayAdapter<SKUProductList>{
	private final Context context;
	private final ArrayList<SKUProductList> values;
	

	
	public OrderBookingAdapter(Context pContext, int textViewResourceId,ArrayList<SKUProductList> pObjects) {
		super(pContext, R.layout.bookorderitem, pObjects);
		context=pContext;
		values=pObjects;
		for(int i=0;i<values.size();i++){
			values.get(i).setQuantity("0");
		}
		
	}

	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.bookorderitem, parent, false);
		    TextView product = (TextView) rowView.findViewById(R.id.tvproduct);
		    final EditText qty = (EditText) rowView.findViewById(R.id.etqty);
		    TextView dpvalue = (TextView) rowView.findViewById(R.id.tvdpvalue);
		    product.setText(values.get(position).getSKUCode().toString());
		    qty.setText(values.get(position).getQuantity().toString());
		    dpvalue.setText(values.get(position).getDealerPrice());
		   EditTextWatcher watcher=new EditTextWatcher();
		   qty.addTextChangedListener(watcher);
		   watcher.setTarget(position);
		  
		   qty.setOnFocusChangeListener(new OnFocusChangeListener() {// if data is zero clear edit text filed    
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(qty.getText().toString().equals("0")){
						qty.setText("");
					}	
				}
			}
		});
		   
		  
		return rowView;
	 }
	
	public ArrayList<SKUProductList> getItems() {	
        return values;
    }

private class EditTextWatcher implements TextWatcher{
	private int target;
	

    private void setTarget(int target) {
        this.target = target;
    }
 
	@Override
	public void afterTextChanged(Editable s) {
		values.get(target).setQuantity(s.toString());
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

