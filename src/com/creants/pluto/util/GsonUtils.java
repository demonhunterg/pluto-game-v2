package com.creants.pluto.util;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * @author LamHa
 *
 */
public class GsonUtils {
	private static Logger logger = LoggerFactory.getLogger(GsonUtils.class);

	private static final Gson gson = new Gson();

	public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {
	}.getType();

	/**
	 * Convert đối tượng thành json string
	 * 
	 * @param data
	 * @return
	 */
	public static final String toGsonString(Object data) {
		if (data == null)
			return null;

		return gson.toJson(data);
	}

	/**
	 * Convert đối tượng thành json string
	 * 
	 * @param data
	 * @param typeOfData
	 * @return
	 */
	public static final String toGsonString(Object data, Type typeOfData) {
		if (data == null)
			return null;

		return gson.toJson(data, typeOfData);
	}

	/**
	 * Convert json string thành đối tượng
	 * 
	 * @param gsonStr
	 * @param typeOfT
	 * @return
	 */
	public static final <T> T fromGsonString(String gsonStr, Type typeOfT) {
		if (gsonStr == null)
			return null;

		return gson.fromJson(gsonStr, typeOfT);
	}

	/**
	 * Convert json string thành đối tượng từ file
	 * 
	 * @param file
	 * @param typeOfT
	 * @return
	 */
	public static final <T> T fromGsonFile(String file, Type typeOfT) {
		try {
			return gson.fromJson(new InputStreamReader(GsonUtils.class.getResourceAsStream(file), "UTF-8"), typeOfT);
		} catch (Exception e) {
			logger.error("ERROR:", e);
			return null;
		}
	}

	/**
	 * Convert json string thành đối tượng
	 * 
	 * @param gsonStr
	 * @param classOfT
	 * @return
	 */
	public static final <T> T fromGsonString(String gsonStr, Class<T> classOfT) {
		if (gsonStr == null)
			return null;
		return gson.fromJson(gsonStr, classOfT);
	}

	/**
	 * Chuyển đối tượng thành json tree
	 * 
	 * @param src
	 * @return
	 */
	public static final JsonElement toJsonTree(Object src) {
		return gson.toJsonTree(src);
	}
}
