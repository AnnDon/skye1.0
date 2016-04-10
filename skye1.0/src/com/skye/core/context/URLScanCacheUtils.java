package com.skye.core.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Iterator;


public class URLScanCacheUtils {
	private static final Map<String,String> HANDLEER_URL = new HashMap<String,String>();
	public static List<String> filterURL(String pattern){
		List result = new  ArrayList();
		Pattern p = Pattern.compile(pattern);
		for(Iterator<String> i$ = HANDLEER_URL.keySet().iterator();i$.hasNext();){
			String url = i$.next();
			if(p.matcher(url).matches()){
				result.add(url);
			}
		}
		return result;
	}
	public static boolean contains(String url){
		return HANDLEER_URL.keySet().contains(url);
	}
	public static void addURL(String url,String funDesc){
		HANDLEER_URL.put(url, funDesc);
	}
	public static Map<String,String> getAllURL(){
		return HANDLEER_URL;
	}
	public static void clear(){
		HANDLEER_URL.clear();
	}
	public static void remove(String url){
		HANDLEER_URL.remove(url);
	}
}
