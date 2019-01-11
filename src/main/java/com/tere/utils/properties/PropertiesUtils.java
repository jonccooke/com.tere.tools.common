package com.tere.utils.properties;

import java.util.Properties;

import com.tere.TereException;
import com.tere.builder.Builder;

public class PropertiesUtils
{

	public static Properties createProperties(String... props)
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

	public static PropertiesBuilder toBuilder()
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
		@Override
		public Properties build() throws TereException
		{
			return properties;
		}
		
	}
}
