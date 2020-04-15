package com.cqi.hr.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class JSONUtils {
	public static final List<String> parseJSON2List(String jsonString){
		JSONArray ja =  JSONArray.fromObject(jsonString);
		
		List<String> jsonList = new ArrayList<String>();
		for(int i = 0; i < ja.size(); i++){
			jsonList.add(ja.get(i).toString());
		}
		return jsonList;
	}
	
	public static final Map<String, String> parseJSON2Map(String jsonString){
		JSONObject jo = JSONObject.fromObject(jsonString);
		Iterator<?> keys = jo.keys();

		Map<String, String> jsonMap = new HashMap<String, String>();
		while( keys.hasNext() ) {
		    String key = (String)keys.next();
		    jsonMap.put(key, (String)jo.get(key));
		}

		return jsonMap;
	}
}
