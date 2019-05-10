package com.tere.utils.db;

import java.sql.JDBCType;

import com.tere.builder.NotNull;

public class Column 
{
	@NotNull
	String name;
	String alias;
	boolean nullable=true;
	boolean readOnly=false;
	int columnSize = -1;
	int length = -1;	
	int scale = -1;
	int precision = -1;
	@NotNull
	JDBCType jdbcType;
	
	Column()
	{
		
	}
	public String getName()
	{
		return name;
	}
	public String getAlias()
	{
		return alias;
	}
	public boolean isNullable()
	{
		return nullable;
	}
	public boolean isReadOnly()
	{
		return readOnly;
	}
	public int getColumnSize()
	{
		return columnSize;
	}
	public int getLength()
	{
		return length;
	}
	public int getScale()
	{
		return scale;
	}
	public int getPrecision()
	{
		return precision;
	}
	public JDBCType getJdbcType()
	{
		return jdbcType;
	}

}