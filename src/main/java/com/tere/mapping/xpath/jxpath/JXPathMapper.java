package com.tere.mapping.xpath.jxpath;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.util.BasicTypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;

import com.tere.mapping.CompiledPath;
import com.tere.mapping.MappingException;
import com.tere.mapping.TMapper;

public class JXPathMapper implements TMapper
{
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'hh:mm:ss'Z'");

	@Override
	public void init(Properties properties) throws MappingException
	{
		if (null != properties)
		{
			if (properties.containsKey("dateFormat"))
			{
				String dateFormatString = properties.getProperty("dateFormat");
				dateFormat = new SimpleDateFormat(dateFormatString);
			}
		}
	}

	@Override
	public Object createContext(Object src) throws MappingException
	{
		JXPathContext srcContex = JXPathContext.newContext(src);
		TypeUtils.setTypeConverter(new BasicTypeConverter()
		{

			@Override
			public Object convert(Object object, Class toType)
			{
				if ((toType == Date.class) && (object instanceof String))
				{
					try
					{
						return dateFormat.parseObject(object.toString());
					}
					catch (ParseException e)
					{
						return null;
					}
				}
				return super.convert(object, toType);
			}

			@Override
			public boolean canConvert(Object object, Class toType)
			{
				if (toType == Date.class)
				{
					return true;
				}
				return super.canConvert(object, toType);
			}
		});
		return srcContex;
	}

	@Override
	public boolean map(Object srcContext, Object destContext, Object srcPath,
			Object destPath, Class mappingClazz) throws MappingException
	{

		Object result = get(srcContext, srcPath, mappingClazz);
		
		((JXPathContext) destContext).setValue((String) destPath, result);
		return false;
	}

	@Override
	public boolean map(Object srcContext, Object destContext, Object srcPath,
			Object destPath) throws MappingException
	{

		Object result = get(srcContext, srcPath);
		
		((JXPathContext) destContext).setValue((String) destPath, result);
		return false;
	}

	@Override
	public boolean map(Object context, CompiledPath compiledPath,
			Object source, Object dest) throws MappingException
	{
		XPathCompiledPath xPathCompiledPath = (XPathCompiledPath) compiledPath;
		JXPathContext destContext = JXPathContext.newContext(dest);

		Object result = xPathCompiledPath.getCompiledExpression().getValue(
				(JXPathContext) context, xPathCompiledPath.getMappingClass());
		// destContext.setValue(xpath, value)
		return false;
	}

	@Override
	public Object get(Object srcContext, Object srcPath, Class mappingClazz)
			throws MappingException
	{
		Object result = ((JXPathContext) srcContext).getValue((String) srcPath,
				mappingClazz);
		return result;
	}

	@Override
	public Object get(Object srcContext, Object srcPath)
			throws MappingException
	{
		Object result = ((JXPathContext) srcContext).getValue((String) srcPath);
		return result;
	}

}
