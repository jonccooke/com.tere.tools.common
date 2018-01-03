package com.tere.utils.io.xml;

import java.lang.reflect.InvocationTargetException;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.reflection.ClassReflect;
import com.tere.utils.reflection.InvalidArgumentException;
import com.tere.utils.reflection.MethodNotFoundException;

public class PrimativeFieldMapping<T> implements XPathFieldMapping<T>
{
	private static Logger log = LogManager.getLogger(PrimativeFieldMapping.class);

	@Override
	public void map(T object, String field, Object value) throws TereException
	{
		try
		{
			log.debug("setting %s, %s",field, value);
			ClassReflect.executeSetter(object, field, value);
		}
		catch (IllegalArgumentException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		catch (IllegalAccessException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		catch (InvocationTargetException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		catch (MethodNotFoundException e)
		{
			throw new TereException(e.getMessage(), e);
		}
		catch (InvalidArgumentException e)
		{
			throw new TereException(e.getMessage(), e);
		}
	}

}
