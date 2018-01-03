package com.tere;

/**
 * Main class for exceptions
 * 
 * @author Jonc
 *
 */
public class TereException extends Exception
{

	public TereException()
	{
		super();
	}

	public TereException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TereException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TereException(String message)
	{
		super(message);
	}

	public TereException(Throwable cause)
	{
		super(cause);
	}

}
