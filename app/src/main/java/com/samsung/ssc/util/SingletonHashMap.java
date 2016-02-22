package com.samsung.ssc.util;

import java.util.HashMap;

import com.samsung.ssc.dto.CompetitionDto;

public class SingletonHashMap {
	
	private final HashMap<String, CompetitionDto> settings;

    // As this is static,the VM makes sure that this is called only once
    private static final SingletonHashMap INSTANCE = new SingletonHashMap(); 

    // We know the constructor is called only once. Making it private
    // Guarantees no other classes can call it.
    // ==> Thus this is a nice safe place to initialize your Hash
    
    private SingletonHashMap() {
        settings = new HashMap<String, CompetitionDto>();
    }

    public static SingletonHashMap getInstance() {
        return INSTANCE;
    }
    
    public void putValue(String key, CompetitionDto value){
    	
    	settings.put(key, value);
    	
    }

   public final CompetitionDto getValue(String key) {
       return settings.get(key); 
   }
   
	public int size() {
		return settings.size();
	}
	
	public boolean containsKey(String key){
		return settings.containsKey(key);
	}
	
	public void remove(){
		settings.clear();
	}

}
