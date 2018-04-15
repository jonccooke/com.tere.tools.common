package com.tere.compiler;

import java.util.HashMap;
import java.util.Map;

public class ByteCodeClassLoader extends ClassLoader
{

	private Map<String, byte[]> classMap = new HashMap<String, byte[]>();

	public void addClass(String className, byte[] classByteCode)
	{
		this.classMap.put(className, classByteCode);
	}

	public void addClassMap(Map<String, byte[]> newClassMap)
	{
		this.classMap.putAll(newClassMap);
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException
	{
		if (classMap.containsKey(name))
		{
			byte[] bytes = classMap.get(name);
			return defineClass(name, bytes, 0, bytes.length);
		}
		return super.findClass(name);
	}
};

