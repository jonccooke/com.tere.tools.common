package com.tere.utils.io.json;

import com.google.gson.JsonElement;
import com.tere.TereException;

public interface FromJSON<T>
{
	public T fromJson(JsonElement jsonElement) throws TereException;

}
