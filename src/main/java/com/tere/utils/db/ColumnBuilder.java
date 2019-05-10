package com.tere.utils.db;

import java.sql.JDBCType;

import com.tere.TereException;
import com.tere.builder.Builder;
import com.tere.builder.BuilderException;
import com.tere.builder.BuilderImpl;

public class ColumnBuilder 
{
	ColumnsBuilder columnsBuilder;
	Column column;
	ColumnBuilder(ColumnsBuilder columnsBuilder) throws TereException
	{
		super();
		this.columnsBuilder = columnsBuilder;
		this.column = new Column();
	}

	public ColumnBuilder name(String name)
	{
		column.name = name;
		return this;
	}
	
	public ColumnBuilder alias(String alias)
	{
		column.alias = alias;
		return this;
	}
	public ColumnBuilder nullable(boolean nullable)
	{
		column.nullable = nullable;
		return this;
	}
	public ColumnBuilder readOnly(boolean readOnly) 
	{
		column.readOnly = readOnly;
		return this;
	}
	public ColumnBuilder columnSize(int columnSize)
	{
		column.columnSize = columnSize;
		return this;
	}
	public ColumnBuilder length(int length)
	{
		column.length = length;
		return this;
	}
	public ColumnBuilder scale(int scale)
	{
		column.scale = scale;
		return this;
	}
	public ColumnBuilder precision(int precision)
	{
		column.precision = precision;
		return this;
	}
	public ColumnBuilder type(JDBCType type)
	{
		column.jdbcType = type;
		return this;
	}
	
	public ColumnsBuilder build()
	{
		return columnsBuilder;
	}

}