package com.tere.utils.db;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.tere.TereException;
import com.tere.builder.Builder;
import com.tere.builder.BuilderException;
import com.tere.builder.BuilderImpl;

public class ColumnsBuilder extends BuilderImpl<Map<String, Column>, TereException>
		implements Builder<Map<String, Column>, TereException>
{
	List<ColumnBuilder> columns;

	ColumnsBuilder() throws TereException
	{
		super();
		columns = new Vector<ColumnBuilder>();
	}

	public ColumnBuilder column() throws TereException
	{
		ColumnBuilder columnBuilder = new ColumnBuilder(this);
		columns.add(columnBuilder);
		return columnBuilder;
	}

	@Override
	protected Map<String, Column> createInstance() throws TereException
	{
		return new LinkedHashMap<String, Column>();
	}

	public static ColumnsBuilder toBuilder() throws TereException
	{
		return new ColumnsBuilder();
	}

	@Override
	public Map<String, Column> build() throws TereException, BuilderException
	{
		for (ColumnBuilder columnBuilder : columns)
		{
			check(columnBuilder.column);
			value.put(columnBuilder.column.name, columnBuilder.column);
		}
		return super.build();
	}

}