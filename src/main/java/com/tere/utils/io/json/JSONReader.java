package com.tere.utils.io.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.tere.TereException;
import com.tere.utils.io.TReader;

public abstract class JSONReader<T> implements TReader<T>
{

	private Gson gson = new Gson();

	protected abstract Class<T> getBindingClass();

	@Override
	public T create(Object readObject) throws TereException
	{
		T result = gson.fromJson((JsonElement) readObject, getBindingClass());
		return result;
	}

	@Override
	public Collection<T> read(InputStream inputStream, Properties properties)
			throws TereException
	{
		List<T> results;
		try
		{
			JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
			results = new ArrayList<T>();
			reader.beginArray();
			while (reader.hasNext())
			{
				T result = create(reader);
				results.add(result);
			}
			reader.endArray();
			reader.close();
			return results;
		}
		catch (UnsupportedEncodingException e)
		{
			throw new TereException(e);
		}
		catch (IOException e)
		{
			throw new TereException(e);
		}
	}

}
