package com.tere.utils.collections;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionsUtils
{
	@SafeVarargs
	public static <T> Set<T> set(T... elements)
	{
		return Stream.of(elements).collect(Collectors.toSet());
	}

	public static <K, V> void put(Map<K, List<V>> map, K key, V value)
	{
		if (!map.containsKey(key))
		{
			map.put(key, new CopyOnWriteArrayList<V>());
		}
		map.get(key).add(value);

	}

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

	public interface IteratorFunc<T, E extends Exception>
	{
		public void iterate(int pos, T value) throws E;
	}

	public static <T, E extends Exception> void iterate(Iterable<T> list, IteratorFunc<T, E> iteratorFunc) throws E
	{
		int pos = 0;
		for (T listValue : list)
		{
			iteratorFunc.iterate(pos++, listValue);
		}
	}

	public static <T, E extends Exception> void iterate(T[] array, IteratorFunc<T, E> iteratorFunc) throws E
	{
		int pos = 0;
		for (T listValue : array)
		{
			iteratorFunc.iterate(pos++, listValue);
		}
	}


	public static <T, V, E extends Exception> T[] toArray(Iterable<V> col, T[] array, ListIteratorFunc<T, V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, listValue);
		}
		
		return array;
	}

	public static <V, E extends Exception> int[] toArray(Iterable<V> col, int[] array, ListIntIteratorFunc<V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, listValue);
		}
		
		return array;
	}

	public static <V, E extends Exception> float[] toArray(Iterable<V> col, float[] array, ListFloatIteratorFunc<V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, listValue);
		}
		
		return array;
	}


	public static <V, E extends Exception> double[] toArray(Iterable<V> col, double[] array, ListDoubleIteratorFunc<V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, listValue);
		}
		
		return array;
	}

	public static <V, E extends Exception> long[] toArray(Iterable<V> col, long[] array, ListLongIteratorFunc<V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, listValue);
		}
		
		return array;
	}

	public static <V, E extends Exception> short[] toArray(Iterable<V> col, short[] array, ListShortIteratorFunc<V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, listValue);
		}
		
		return array;
	}

	public static <V, E extends Exception> byte[] toArray(Iterable<V> col, byte[] array, ListByteIteratorFunc<V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, listValue);
		}
		
		return array;
	}

	public static <T, V, E extends Exception> T[] toArray(V[] col, T[] array, ListIteratorFunc<T, V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		for (V value : col)
		{
			array[pos] = iteratorFunc.iterate(pos++, value);
		}
		
		return array;
	}
	public interface ListIteratorFunc<T, V, E extends Exception>
	{
		public T iterate(int pos, V value) throws E;
	}

	public interface ListFloatIteratorFunc<V, E extends Exception>
	{
		public float iterate(float pos, V value) throws E;
	}

	public interface ListDoubleIteratorFunc<V, E extends Exception>
	{
		public double iterate(int pos, V value) throws E;
	}
	public interface ListShortIteratorFunc<V, E extends Exception>
	{
		public short iterate(int pos, V value) throws E;
	}
	public interface ListLongIteratorFunc<V, E extends Exception>
	{
		public long iterate(int pos, V value) throws E;
	}
	public interface ListByteIteratorFunc<V, E extends Exception>
	{
		public byte iterate(int pos, V value) throws E;
	}
	public interface ListBooleanIteratorFunc<V, E extends Exception>
	{
		public boolean iterate(int pos, V value) throws E;
	}
	public interface ListIntIteratorFunc<V, E extends Exception>
	{
		public int iterate(int pos, V value) throws E;
	}
	public interface ListBteIteratorFunc<V, E extends Exception>
	{
		public byte iterate(int pos, V value) throws E;
	}

	public static <T, V, E extends Exception> List<T> toList(Iterable<V> col, ListIteratorFunc<T, V, E> iteratorFunc)
			throws E
	{
		int pos = 0;
		List<T> retList = new Vector<T>();
		for (V listValue : col)
		{
			retList.add(iteratorFunc.iterate(pos++, listValue));
		}
		return retList;
	}

	public static <T, V, E extends Exception> void toList(List<T> list, Iterable<V> col,
			ListIteratorFunc<T, V, E> iteratorFunc) throws E
	{
		int pos = 0;
		for (V listValue : col)
		{
			list.add(iteratorFunc.iterate(pos++, listValue));
		}
	}

	public static <T, V, E extends Exception> void toList(List<T> list, V[] array,
			ListIteratorFunc<T, V, E> iteratorFunc) throws E
	{
		int pos = 0;
		for (V listValue : array)
		{
			list.add(iteratorFunc.iterate(pos++, listValue));
		}
	}

	public static <V> List<V> toList(List<V> list, V[] array) 
	{
		for (V arrayValue : array)
		{
			list.add(arrayValue);
		}
		
		return list;
	}

	@SafeVarargs
	public static <V> List<V> toList(V...values) 
	{
		Vector<V> list = new Vector<V>();
		for (V listValue : values)
		{
			list.add(listValue);
		}
		
		return list;
	}

}
