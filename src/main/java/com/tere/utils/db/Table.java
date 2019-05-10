package com.tere.utils.db;

import java.util.Collection;
import java.util.Map;

import com.tere.builder.NotNull;

public class Table
{
	String catalog = "public";
	String schema ="public";
	@NotNull
	String name;
	String alias;
	Map<String, Column> columns;
	Table()
	{
		
	}
	public String getCatalog()
	{
		return catalog;
	}
	public String getSchema()
	{
		return schema;
	}
	public String getName()
	{
		return name;
	}
	public String getAlias()
	{
		return alias;
	}
	
	public Collection<Column> getColumns()
	{
		return columns.values();
	}
}