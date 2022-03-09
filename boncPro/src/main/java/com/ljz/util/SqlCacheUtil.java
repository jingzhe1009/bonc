package com.ljz.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class SqlCacheUtil {
	
	private static volatile SqlCacheUtil instance;
	
	public static SqlCacheUtil getInstance() {
		if (null == instance) {
            synchronized (SqlCacheUtil.class) {
                if (null == instance) {
                	instance = new SqlCacheUtil();
                }
            }
        }
		return instance;
	}
	
	private Map<String,Map<String,Object>> constantMap = new HashMap<String,Map<String,Object>> ();

	public Map<String, Map<String,Object>> getConstantMap() {
		return constantMap;
	}

	public void setConstantMap(Map<String, Map<String,Object>> constantMap) {
		this.constantMap = constantMap;
	}
	
	public void put(String out,String in,List list) {
		Map<String,Object> innerMap = new HashMap<String,Object>();
		innerMap.put(out, list);
		Map<String,Map<String,Object>> outMap = new HashMap<String,Map<String,Object>>();
		outMap.put(in, innerMap);
		setConstantMap(outMap);
	}
	
	

}
