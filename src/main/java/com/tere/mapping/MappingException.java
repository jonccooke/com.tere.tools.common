package com.tere.mapping;

import com.tere.TereException;

public class MappingException extends TereException
{

	public MappingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MappingException(String message)
	{
		super(message);
	}

	public MappingException(Throwable cause)
	{
		super(cause);
	}

}
