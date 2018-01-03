package com.tere.utils.io.csv;

public class CommandMessage
{
	private String[] values;
	private ReaderCommand command;
	private long rowNum;

	public CommandMessage(ReaderCommand command)
	{
		this.command = command;
	}
	
	public CommandMessage(String[] values, ReaderCommand command, long rowNum)
	{
		super();
		this.values = values;
		this.command = command;
		this.rowNum = rowNum;
	}
	
	public ReaderCommand getCommand()
	{
		return command;
	}
	
	public String[] getValues()
	{
		return values;
	}
	
	public long getRowNum()
	{
		return rowNum;
	}

}
