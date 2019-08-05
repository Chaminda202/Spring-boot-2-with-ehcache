package com.spring.ehcache.common;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class GsonUtil {
	private static Gson gson = new Gson();
	
	private GsonUtil(){
		
	}
	
	public static String getToString(Object src, Type typeOfSrc) {
		try {
			if (src != null)
				return gson.toJson(src, typeOfSrc);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return "";
	}

	public static <T> T getFromObject(String json, Class<T> classOfT) {
		try {
			if (json != null)
				return gson.fromJson(json, classOfT);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
}
