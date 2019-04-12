package com.tere.utils.io.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSONUtils
{
	public interface GetterFunction<T>
	{
		public T got(JsonElement el);
	}

	public interface HasFunction<T>
	{
		public void has(T el);
	}

	public static JsonElement getElement(JsonObject requestData, String elementName) throws JSONException
	{
		JsonElement el = requestData.get(elementName);
		if (null ==  el || el.isJsonNull())
		{
			return null;//throw new JSONException(elementName + " not specified");
		}

		return el;
	}
	public static <T> T getObject(JsonObject requestData, String elementName, GetterFunction<T> getterFunction) throws JSONException
	{
		JsonElement el = requestData.get(elementName);
		if (null ==  el || el.isJsonNull())
		{
			throw new JSONException(elementName + " not specified");
		}
		return getterFunction.got(el);
	}

	public static <T> T getObject(JsonObject requestData, String elementName, T defaultValue, GetterFunction<T> getterFunction) throws JSONException
	{
		JsonElement el = requestData.get(elementName);
		if (null ==  el || el.isJsonNull())
		{
			return defaultValue;
		}
		return getterFunction.got(el);
	}

	public static boolean hasElement(JsonObject requestData, String elementName) throws JSONException
	{
		return null != getElement(requestData, elementName);
	}

	public static  boolean hasString(JsonObject requestData, String elementName, HasFunction<String> getterFunction) throws JSONException
	{
		JsonElement el = requestData.get(elementName);
		if (null ==  el || el.isJsonNull())
		{
			return false;	
		}
		getterFunction.has(el.getAsString());
		return true;
	}

	public static String getString(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsString()); 
	}
	
	public static String getString(JsonObject requestData, String elementName, String defaultValue) throws JSONException
	{
		return getObject(requestData, elementName, defaultValue, (el)-> el.getAsString()); 
	}

	public static String getString(JsonObject requestData, String elementName, GetterFunction<String> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}


	public static int getInteger(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsInt()); 
	}
	
	public static int getInteger(JsonObject requestData, String elementName, Integer defaultValue) throws JSONException
	{
		return getObject(requestData, elementName, defaultValue, (el)-> el.getAsInt()); 
	}

	public static Integer getInteger(JsonObject requestData, String elementName, GetterFunction<Integer> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}
	

	public static double getDouble(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsDouble()); 
	}
	
	public static double getDouble(JsonObject requestData, String elementName, double defaultValue) throws JSONException
	{
		return getObject(requestData, elementName, defaultValue, (el)-> el.getAsDouble()); 
	}

	public static double getDouble(JsonObject requestData, String elementName, GetterFunction<Double> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}

	

	public static boolean getBoolean(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsBoolean()); 
	}
	
	public static boolean getBoolean(JsonObject requestData, String elementName, Boolean defaultValue) throws JSONException
	{
		return getObject(requestData, elementName, defaultValue, (el)-> el.getAsBoolean()); 
	}

	public static boolean getBoolean(JsonObject requestData, String elementName, GetterFunction<Boolean> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}
	
	

	public static float getFloat(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsFloat()); 
	}
	
	public static float getFloat(JsonObject requestData, String elementName, Float defaultValue) throws JSONException
	{
		return getObject(requestData, elementName, defaultValue, (el)-> el.getAsFloat()); 
	}

	public static float getFloat(JsonObject requestData, String elementName, GetterFunction<Float> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}
	

	public static byte getByte(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsByte()); 
	}
	
	public static byte getByte(JsonObject requestData, String elementName, Byte defaultValue) throws JSONException
	{
		return getObject(requestData, elementName, defaultValue, (el)-> el.getAsByte()); 
	}

	public static byte getByte(JsonObject requestData, String elementName, GetterFunction<Byte> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}
	

	public static short getShort(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsShort()); 
	}
	
	public static short getShort(JsonObject requestData, String elementName, Short defaultValue) throws JSONException
	{
		return getObject(requestData, elementName, defaultValue, (el)-> el.getAsShort()); 
	}

	public static short getShort(JsonObject requestData, String elementName, GetterFunction<Short> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}
	
}
