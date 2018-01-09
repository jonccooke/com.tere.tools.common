package com.tere.utils.io.json;

import com.google.gson.JsonParser;

public abstract class JSONAdapterImpl<T> implements JSONAdapter<T>
{

	@Override
	public T fromJson(String jsonString)
	{
		JsonParser parser = new JsonParser();
		
		return fromJson(parser .parse(jsonString).getAsJsonObject());
	}

}
