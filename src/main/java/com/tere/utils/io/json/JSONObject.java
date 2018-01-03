package com.tere.utils.io.json;

import com.tere.TereException;

public interface JSONObject<T>
{
	static String QUOTE = "\"";
	static String SEPARATOR = ":";
	static String BLANK = "";
	static String COMMA = ",";
	static String OPEN_BRACE = "{";	
	static String CLOSE_BRACE = "}";	

	public void create(T value, Object ... args) throws TereException;

	public String toJSONString() throws TereException;


}
