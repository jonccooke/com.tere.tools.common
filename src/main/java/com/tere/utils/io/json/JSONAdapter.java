package com.tere.utils.io.json;

import org.jglue.fluentjson.JsonBuilder;

import com.google.gson.JsonElement;
import com.tere.TereException;

public interface JSONAdapter<T>
{

	public JsonBuilder toJsonBuilder(T value) throws TereException;
	
	public T fromJson(JsonElement jsonElement) throws TereException;

	public T fromJson(String jsonString) throws TereException;


}
