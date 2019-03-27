package com.tere.builder;

import com.tere.TereException;

public class BuilderException extends TereException
{

	public BuilderException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public BuilderException(String message)
	{
		super(message);
	}

	public BuilderException(Exception e)
	{
		super(e);
	}

}
