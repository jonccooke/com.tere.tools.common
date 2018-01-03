package com.tere.mapping;

import java.util.Properties;

public interface TMapper
{
	public void init(Properties properties) throws MappingException;

	public Object createContext(Object src) throws MappingException;

	public boolean map(Object srcContext, Object destContext, Object srcPath,
			Object destPath, Class mappingClazz) throws MappingException;

	public boolean map(Object context, CompiledPath compiledPath,
			Object source, Object dest) throws MappingException;

	public Object get(Object srcContext, Object srcPath, Class mappingClazz)
			throws MappingException;

	public boolean map(Object srcContext, Object destContext, Object srcPath,
			Object destPath) throws MappingException;

	public Object get(Object srcContext, Object srcPath) throws MappingException;

}
