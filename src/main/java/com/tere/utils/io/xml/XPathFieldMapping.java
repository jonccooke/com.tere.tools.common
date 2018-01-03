package com.tere.utils.io.xml;

import com.tere.TereException;

public interface XPathFieldMapping<T>
{

	public void map(T object, String field, Object value) throws TereException;
}
