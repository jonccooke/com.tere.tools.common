package com.tere.utils.list;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class ListUtils
{

	public static void addUnique(Object value, List list)
	{
		if (!list.contains(value))
		{
			list.add(value);
		}
	}

	public static void addUnique(Object value, List list, Comparator comparator)
	{
		for (Object listVal : list)
		{
			if (0 == comparator.compare(listVal, value))
			{
				list.add(value);
			}
		}
	}

	public static Properties createProperties(String... props)
	{
		Properties properties = new Properties();

		if (null != props)
		{
			for (String prop : props)
			{
				if (0 != prop.length())
				{
					String[] propValue = prop.split(":");
					String key = propValue[0].trim();
					String value = null;
					if (prop.length() > 1)
					{
						value = propValue[1].trim();
					}
					properties.setProperty(key, value);
				}
			}
		}
		return properties;
	}

	public interface IteratorFunc<T>
	{
		public void iterate(int pos, T value);
	}

	public static <T> void iterate(List<T> list, IteratorFunc<T> iteratorFunc)
	{
		int pos = 0;
		for (T listValue : list)
		{
			iteratorFunc.iterate(pos++, listValue);
		}
	}

	public interface StringBuilderIteratorFunc<T>
	{
		public void iterate(StringBuilder builder,int pos, T value);
	}

	public static <T> StringBuilder iterateString(List<T> list, StringBuilderIteratorFunc<T> iteratorFunc)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T listValue : list)
		{
			iteratorFunc.iterate(stringBuilder, pos, listValue);
		}
		
		return stringBuilder;
	}


	
}
