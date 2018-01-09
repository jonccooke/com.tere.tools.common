package com.tere.utils.io.json;

import org.jglue.fluentjson.JsonBuilder;

import com.google.gson.JsonElement;

public interface JSONAdapter<T>
{

	public JsonBuilder toJsonBuilder(T value);
	
	public T fromJson(JsonElement jsonElement);

	public T fromJson(String jsonString);


}
