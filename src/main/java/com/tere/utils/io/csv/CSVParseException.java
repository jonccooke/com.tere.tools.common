package com.tere.utils.io.csv;

import com.tere.TereException;

public class CSVParseException extends TereException
{

	private long rowNum;
	private int colNum;
	private Throwable throwable;

	public CSVParseException(long rowNum, int colNum, Throwable throwable)
	{
		super();
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.throwable = throwable;
	}

	public long getRowNum()
	{
		return rowNum;
	}

	public int getColNum()
	{
		return colNum;
	}

	public Throwable getThrowable()
	{
		return throwable;
	}

	public String getMessage()
	{
		return throwable.getMessage();
	}

	@Override
	public String toString()
	{
		return String.format("%s at row %d, column %d", getMessage(), getRowNum(), getColNum());
	}

}
