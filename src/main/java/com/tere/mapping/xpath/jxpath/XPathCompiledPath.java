package com.tere.mapping.xpath.jxpath;

import org.apache.commons.jxpath.CompiledExpression;
import org.apache.commons.jxpath.JXPathContext;

import com.tere.mapping.CompiledPath;
import com.tere.mapping.MappingException;

public class XPathCompiledPath implements CompiledPath
{
	private CompiledExpression compiledExpression;
	private boolean isCompiled = false;
	private Class mappingClass;
	
	@Override
	public void compile(Object context, Object path, Class mappingClass) throws MappingException
	{
		this.compiledExpression = JXPathContext.compile((String) path);
		this.mappingClass = mappingClass;
		isCompiled = true;
	}

	public CompiledExpression getCompiledExpression()
	{
		return compiledExpression;
	}

	@Override
	public boolean isCompiled()
	{
		return isCompiled;
	}

	public Class getMappingClass()
	{
		return mappingClass;
	}

}
