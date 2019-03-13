package com.tere.utils.io.json;

import com.google.gson.JsonObject;

public class JSONUtils
{
	public interface GetterFunction<T>
	{
		public void got(T value);
	}

	public static String getString(JsonObject requestData, String elementName) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		return requestData.get(elementName).getAsString();
	}

	public static String getString(JsonObject requestData, String elementName, String defaultValue) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			return defaultValue;
		}

		return requestData.get(elementName).getAsString();
	}

	public static void getString(JsonObject requestData, String elementName, GetterFunction<String> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		getterFunction.got(requestData.get(elementName).getAsString());
	}

	public static void hasString(JsonObject requestData, String elementName, GetterFunction<String> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			getterFunction.got(requestData.get(elementName).getAsString());
		}

	}

	public static boolean getBoolean(JsonObject requestData, String elementName) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		return requestData.get(elementName).getAsBoolean();
	}

	public static boolean getBoolean(JsonObject requestData, String elementName, boolean defaultValue) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			return defaultValue;
		}

		return requestData.get(elementName).getAsBoolean();
	}

	public static void getBoolean(JsonObject requestData, String elementName, GetterFunction<Boolean> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		getterFunction.got(requestData.get(elementName).getAsBoolean());
	}

	public static void hasBoolean(JsonObject requestData, String elementName, GetterFunction<Boolean> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			getterFunction.got(requestData.get(elementName).getAsBoolean());
		}

	}

	public static double getDouble(JsonObject requestData, String elementName) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		return requestData.get(elementName).getAsDouble();
	}

	public static double getDouble(JsonObject requestData, String elementName, double defaultValue) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			return defaultValue;
		}

		return requestData.get(elementName).getAsDouble();
	}

	public static void getDouble(JsonObject requestData, String elementName, GetterFunction<Double> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		getterFunction.got(requestData.get(elementName).getAsDouble());
	}

	public static void hasDouble(JsonObject requestData, String elementName, GetterFunction<Double> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			getterFunction.got(requestData.get(elementName).getAsDouble());
		}

	}

	public static int getInteger(JsonObject requestData, String elementName) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		return requestData.get(elementName).getAsInt();
	}

	public static int getInteger(JsonObject requestData, String elementName, int defaultValue) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			return defaultValue;
		}

		return requestData.get(elementName).getAsInt();
	}

	public static void getInteger(JsonObject requestData, String elementName, GetterFunction<Integer> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			throw new JSONException(elementName + " not specified");
		}

		getterFunction.got(requestData.get(elementName).getAsInt());
	}

	public static void hasInteger(JsonObject requestData, String elementName, GetterFunction<Integer> getterFunction) throws JSONException
	{
		if (null == requestData.get(elementName))
		{
			getterFunction.got(requestData.get(elementName).getAsInt());
		}

	}

}
