package com.tere.builder;

public class FieldNotSetException extends BuilderException
{

	public FieldNotSetException(String classname, String field, Throwable cause)
	{
		super(String.format("Class %s, field %s", classname, field), cause);
	}

	public FieldNotSetException(String classname, String field)
	{
		super(String.format("Class %s, field %s", classname, field));
	}

}
