package com.tere.utils;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class StringUtils
{

	public final static int EOL = -2;
	public final static int SOL = -1;

	public static <T> String expand(T[] array, String expandString)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : array)
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(expandString);
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

	public interface ExpandFunc<T>
	{
		String expand(T t);
	}

	public static <T> String expand(Collection<T> col, ExpandFunc<T> func)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		for (T value : col)
		{
			if (0 != pos++)
			{
				stringBuilder.append(", ");
			}
			stringBuilder.append(func.expand(value));
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
