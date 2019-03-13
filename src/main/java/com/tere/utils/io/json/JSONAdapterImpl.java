package com.tere.utils.io.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.tere.TereException;

public abstract class JSONAdapterImpl<T> implements JSONAdapter<T>
{

	@Override
	public T fromJson(String jsonString) throws JsonSyntaxException, TereException
	{
		JsonParser parser = new JsonParser();
		
		return fromJson(parser .parse(jsonString).getAsJsonObject());
	}

}
