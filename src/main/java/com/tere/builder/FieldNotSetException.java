package com.tere.builder;

public class FieldNotSetException extends BuilderException
{

	private String classname;
	private String field;
	
	public FieldNotSetException(String classname, String field, Throwable cause)
	{
		super(String.format("Class %s, field %s", classname, field), cause);
		this.classname = classname;
		this.field = field;
	}

	public FieldNotSetException(String classname, String field)
	{
		super(String.format("Class %s, field %s", classname, field));
		this.field = field;
	}

	public String getClassname()
	{
		return classname;
	}

	public String getField()
	{
		return field;
	}

}
