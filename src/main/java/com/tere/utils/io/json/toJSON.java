package com.tere.utils.io.json;

import com.google.gson.JsonElement;
import com.tere.TereException;

public interface toJSON<T>
{
	public JsonElement toJson() throws TereException;

}
