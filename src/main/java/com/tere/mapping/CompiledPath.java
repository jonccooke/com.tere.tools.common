package com.tere.mapping;

/**
 * Main interface for compiled xpaths
 * 
 * @author Jonc
 *
 */
public interface CompiledPath
{
	public void compile(Object context, Object path, Class mappingClass) throws MappingException;
	
	public boolean isCompiled();
	
	public Class getMappingClass();
}
