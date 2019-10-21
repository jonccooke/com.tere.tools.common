package com.tere.utils.io.json;

import java.util.Date;
import java.util.Map.Entry;
import java.util.Properties;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class JsonUtils
{
	public interface GetterFunction<T>
	{
		public T got(JsonElement el);
	}

	public interface SetterFunction
	{
		public void set(JsonElement el);
	}

	public interface HasFunction<T>
	{
		public void has(T el);
	}

	public static boolean isNull(JsonObject object, String elementName)
	{
		return (object.get(elementName)==null  || object.get(elementName).isJsonNull());
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

	public static void set(JsonObject requestData, String elementName, SetterFunction setterFunction) throws JSONException
	{
		JsonElement el = requestData.get(elementName);
		if (null !=  el && el.isJsonNull())
		{
			setterFunction.set(requestData.get(elementName));
		}
	}

	public static <T> T getObject(JsonObject requestData, String elementName, GetterFunction<T> getterFunction) throws JSONException
	{
		JsonElement el = requestData.get(elementName);
		if (null ==  el || el.isJsonNull())
		{
			return null;
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
	

	public static Long getLong(JsonObject requestData, String elementName, GetterFunction<Long> getterFunction) throws JSONException
	{
		return getObject(requestData, elementName, getterFunction); 
	}
	
	public static long getLong(JsonObject requestData, String elementName) throws JSONException
	{
		return getObject(requestData, elementName, (el)-> el.getAsLong()); 
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
	

	public static String getString(JsonElement jsonElement)
	{
		if ((null != jsonElement) && (JsonNull.INSTANCE != jsonElement))
		{
			return jsonElement.getAsString();
		}
		return null;
	}

	public static int getInteger(JsonElement jsonElement, int defaultValue)
	{
		if ((null != jsonElement) && (JsonNull.INSTANCE != jsonElement))
		{
			return Integer.parseInt(jsonElement.getAsString());
		}
		return defaultValue;
	}

	public static long getLong(JsonElement jsonElement, long defaultValue)
	{
		if ((null != jsonElement) && (JsonNull.INSTANCE != jsonElement))
		{
			return Long.parseLong(jsonElement.getAsString());
		}
		return defaultValue;
	}
	
	public static Date getDate(JsonObject jsonObject, String memberName)
	{
		JsonElement element = jsonObject.get(memberName);
		if ((null != element) && (JsonNull.INSTANCE != element))
		{
			return new Date(element.getAsLong());
		}
		return null;
	}



//	public static byte[] getBytes(JsonElement jsonElement)
//	{
//		if ((null != jsonElement) && (JsonNull.INSTANCE != jsonElement))
//		{
//			return jsonElement.getAsJsonArray();
//		}
//		return null;
//	}

	public static boolean getBoolean(JsonElement jsonElement,
			boolean defaultValue)
	{
		if ((null != jsonElement) && (JsonNull.INSTANCE != jsonElement))
		{
			return jsonElement.getAsBoolean();
		}
		return defaultValue;
	}

	public static JsonArray getArray(JsonElement jsonElement)
	{
		if ((null != jsonElement) && (JsonNull.INSTANCE != jsonElement))
		{
			return jsonElement.getAsJsonArray();
		}
		return null;
	}

	public static Properties getProperties(JsonElement jsonElement)
	{
		Properties properties = null;
		if ((null != jsonElement) && (JsonNull.INSTANCE != jsonElement))
		{
			properties = new Properties();

			for (Entry<String, JsonElement> elementEntry : jsonElement
					.getAsJsonObject().entrySet())
			{
				properties.setProperty(elementEntry.getKey(), elementEntry
						.getValue().getAsString());
			}
		}
		return properties;
	}

}
