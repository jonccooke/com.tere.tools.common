package com.tere.utils.list;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

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

	public interface ListIteratorFunc<T, V, E extends Exception>
	{
		public T iterate(int pos, V value) throws E;
	}

	public static <T, V, E extends Exception> List<T> toList(Collection<V> col, ListIteratorFunc<T, V, E> iteratorFunc) throws E
	{
		int pos = 0;
		List<T> retList = new Vector<T>();
		for (V listValue : col)
		{
			retList.add(iteratorFunc.iterate(pos++, listValue));
		}
		return retList;
	}
	public static <T, V, E extends Exception> void toList(List<T> list, Collection<V> col, ListIteratorFunc<T, V, E> iteratorFunc) throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			list.add(iteratorFunc.iterate(pos++, listValue));
		}
	}

	
}
