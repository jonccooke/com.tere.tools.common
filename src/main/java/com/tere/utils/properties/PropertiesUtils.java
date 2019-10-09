package com.tere.utils.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tere.TereException;
import com.tere.builder.Builder;
import com.tere.utils.directory.FileUtils;

public class PropertiesUtils
{

	public static Properties load(String filePath) throws IOException
	{
		Properties properties = new Properties();
		try (InputStream propStream = FileUtils.getInputStream(filePath))
		{
			properties.load(propStream);
		}
		return properties;
	}

	public static Properties create(String... props)
	{
		Properties properties = new Properties();

		if (null != props)
		{
			for (String prop : props)
			{
				if (0 != prop.length())
				{
					String[] propValue = prop.split(":");
					String key = propValue[0].trim();
					String value = null;
					if (prop.length() > 1)
					{
						value = propValue[1].trim();
					}
					properties.setProperty(key, value);
				}
			}
		}
		return properties;
	}

	public static PropertiesBuilder getBuilder()
	{
		return new PropertiesBuilder();
	}
	
	public static class PropertiesBuilder implements Builder<Properties, TereException>
	{
		private Properties properties = new Properties();
		
		public PropertiesBuilder put(String key, String value)
		{
			properties.put(key, value);
			return this;
		}

		public PropertiesBuilder putIfExists(String key, String value)
		{
			if (value != null)
			{
				properties.put(key, value);
			}
			return this;
		}

		@Override
		public Properties build() throws TereException
		{
			return properties;
		}

		@Override
		public PropertiesBuilder fromJson(JsonElement jsonElement)
		{
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
			{
				this.properties.put(entry.getKey(), entry.getValue().toString());
			}
			return this;
		}
	}
	
	public static JsonElement toJson(Properties properties)
	{
		JsonObject jsonProps = new JsonObject();
		
		properties.forEach((key, val)->jsonProps.addProperty((String)key, (String)val));
		return jsonProps;
	}
}
