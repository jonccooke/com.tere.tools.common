package com.tere.utils;


public class StringUtils
{
  	
	public static String[] getQuotedValues(String str)
	{
		String r[] = str.split("['\"]\\s?,\\s?['\"]");

		for (int strPos =0; strPos< r.length; strPos++)
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
}
