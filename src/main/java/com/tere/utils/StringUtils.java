package com.tere.utils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StringUtils
{

	public final static int EOL = -2;
	public final static int SOL = -1;

	public static boolean isNull(String value)
	{
		return value ==null;
	}

	public static boolean isNullOrEmpty(String value)
	{
		return !(value !=null && value.length() != 0);
	}

	public static <T> String expand(T[] array, String separatorString)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : array)
		{
			if (0 != pos++)
			{
				stringBuilder.append(separatorString);
			}
			stringBuilder.append(value);
		}
		return stringBuilder.toString();
	}

	public static <T> String expandReplace(T[] array, String replaceString)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : array)
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(replaceString);
		}
		return stringBuilder.toString();
	}

	public static <T> String expand(Collection<T> col, String separatorString, String postString)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : col)
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(value);
			stringBuilder.append(postString);
		}
		return stringBuilder.toString();
	}

	public static <T> String expand(T[] array, String separatorString, String postString)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : array)
		{
			if (0 != pos++)
			{
				stringBuilder.append(separatorString);
			}
			stringBuilder.append(value);
			stringBuilder.append(postString);
		}
		return stringBuilder.toString();
	}

	public static <T> String expand(Collection<T> col, String expandString)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : col)
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(expandString);
		}
		return stringBuilder.toString();
	}


	public interface ExpandMapFunc<K, V>
	{
		public String expand(boolean first, boolean last, K key, V value);
	}

	public static <K, V> String expand(Map<K, V> map, ExpandMapFunc<K, V> func)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		int size = map.size();
		for (Map.Entry<K,V> entry : map.entrySet())
		{
			stringBuilder.append(func.expand(pos==0, pos == size-1, entry.getKey(), entry.getValue()));
			pos++;
		}
		return stringBuilder.toString();
	}

	public static <K, V> String expandKeys(Map<K, V> map)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (Map.Entry<K,V> entry : map.entrySet())
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(entry.getKey());
		}
		return stringBuilder.toString();
	}

	public static <K, V> String expandValues(Map<K, V> map)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (Map.Entry<K,V> entry : map.entrySet())
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(entry.getValue());
		}
		return stringBuilder.toString();
	}

	public static <T> String expand(T[] array)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : array)
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(value);
		}
		return stringBuilder.toString();
	}

	public static <T> String expand(Collection<T> col)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : col)
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(value);
		}
		return stringBuilder.toString();
	}

	public interface ExpandFunc<T>
	{
		String expand(boolean first, boolean last, T t);
	}

	public interface ExpandByteFunc
	{
		String expand(boolean first, boolean last, byte b);
	}

	public static <T> String expand(Collection<T> col, ExpandFunc<T> func)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : col)
		{
			stringBuilder.append(func.expand(pos==0, pos == col.size()-1, value));
			pos++;
		}
		return stringBuilder.toString();
	}

	public static <T> String expand(T[] col, ExpandFunc<T> func)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : col)
		{
			stringBuilder.append(func.expand(pos==0, pos == col.length-1, value));
			pos++;
		}
		return stringBuilder.toString();
	}

	public static <T> String expand(T[] col, String separatorString, ExpandFunc<T> func)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : col)
		{
			if (0 != pos++)
			{
				stringBuilder.append(separatorString);
			}
			stringBuilder.append(func.expand(pos==0, pos == col.length-1, value));
			pos++;
		}
		return stringBuilder.toString();
	}

	public static String expand(byte[] col, ExpandByteFunc func)
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		int pos=0;
		for (byte value : col)
		{
			stringBuilder.append(func.expand(pos == 0, pos == col.length -1, value));
			pos++;
		}
		return stringBuilder.toString();
	}

	public static String[] getQuotedValues(String str)
	{
		String r[] = str.split("['\"]\\s?,\\s?['\"]");

		for (int strPos = 0; strPos < r.length; strPos++)
		{
			String rs = r[strPos];
			if (0 != rs.length())
			{
				if (rs.charAt(0) == '\'')
				{
					rs = rs.substring(1);
				}
			}
			r[strPos] = rs;
		}
		return r;
	}

	public interface MatchingFunction<T>
	{
		public boolean match(String patternRegex, T matchingObject);
	}

	public static <T> int match(String pattern, Collection<T> collection, MatchingFunction<T> matchingFunction)
	{

		AtomicInteger matched = new AtomicInteger(0);
		String patternRegex = createRegexFromGlob(pattern);

		collection.forEach(t ->
			{
				if (matchingFunction.match(patternRegex, t))
				{
					matched.incrementAndGet();
				}
			});
		return matched.get();
	}

	public static <T> boolean match(String pattern, T value, MatchingFunction<T> matchingFunction)
	{

		String patternRegex = createRegexFromGlob(pattern);

		return matchingFunction.match(patternRegex, value);
	}

	public static String createRegexFromGlob(String glob)
	{
		StringBuilder out = new StringBuilder("^");
		for (int i = 0; i < glob.length(); ++i)
		{
			final char c = glob.charAt(i);
			switch (c)
			{
			case '*':
				out.append(".*");
				break;
			case '?':
				out.append('.');
				break;
			case '.':
				out.append("\\.");
				break;
			case '\\':
				out.append("\\\\");
				break;
			default:
				out.append(c);
			}
		}
		out.append('$');
		return out.toString();
	}

	public static String removeIfPressent(String value, String valToRemove, int pos, boolean ignoreCase)
	{
		int valTRlen = valToRemove.length();
		int vallen = value.length();
		int startPos = pos;
		int endPos = pos;
		String retVal = value;
		String strToComp;
		if (valTRlen <= vallen)
		{
			switch (pos)
			{
			case EOL:
				startPos = vallen - valTRlen;
				endPos = vallen;
				strToComp = value.substring(startPos, endPos);
				endPos = startPos;
				startPos = 0;
				break;
			case SOL:
				startPos = 0;
				endPos = valTRlen;
				strToComp = value.substring(startPos, endPos);
				startPos = valTRlen;
				endPos = vallen;
				break;
			default:
				startPos = pos;
				endPos = pos + valTRlen;
				strToComp = value.substring(startPos, endPos);
				if (ignoreCase)
				{
					if (strToComp.equalsIgnoreCase(valToRemove))
					{
						retVal = value.substring(0, startPos).concat(value.substring(endPos, vallen));
						return retVal;
					}
				} else
				{
					if (strToComp.contentEquals(valToRemove))
					{
						retVal = value.substring(0, startPos).concat(value.substring(endPos, vallen));
						return retVal;
					}
				}
				break;
			}
			if (ignoreCase)
			{
				if (strToComp.equalsIgnoreCase(valToRemove))
				{
					retVal = value.substring(startPos, endPos);
				}
			} else
			{
				if (strToComp.contentEquals(valToRemove))
				{
					retVal = value.substring(startPos, endPos);
				}
			}
		}
		return retVal;
	}
}
