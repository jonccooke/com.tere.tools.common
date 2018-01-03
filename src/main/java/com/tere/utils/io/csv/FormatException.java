package com.tere.utils.io.csv;

import com.tere.TereException;

public class FormatException extends TereException
{

	public FormatException(int colunm, String message)
	{
		super(String.format("%s for column %d", message, colunm));
	}
}
