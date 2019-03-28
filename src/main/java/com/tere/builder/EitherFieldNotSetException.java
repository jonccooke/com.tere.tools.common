package com.tere.builder;

public class EitherFieldNotSetException extends BuilderException
{

	public EitherFieldNotSetException(String classname, String field1, String field2, Throwable cause)
	{
		super(String.format("Class %s, either %s or %s", classname, field1, field2), cause);
	}

	public EitherFieldNotSetException(String classname, String field1, String field2)
	{
		super(String.format("Class %s, either %s or %s", classname, field1, field2));
	}

}
