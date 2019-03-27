package com.tere.builder;

import com.tere.TereException;

public class FieldNotSetException extends BuilderException
{

	public FieldNotSetException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FieldNotSetException(String message)
	{
		super(message);
	}

}
