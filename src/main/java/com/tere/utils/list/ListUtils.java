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

}
