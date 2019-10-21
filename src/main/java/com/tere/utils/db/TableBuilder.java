package com.tere.utils.db;

import java.util.LinkedHashMap;

import com.google.gson.JsonElement;
import com.tere.TereException;
import com.tere.builder.Builder;
import com.tere.builder.BuilderImpl;

public class TableBuilder extends BuilderImpl<Table, TereException> implements Builder<Table, TereException>
{
	private DatabaseUtility databaseUtility;
	
	TableBuilder(DatabaseUtility databaseUtility, String name) throws TereException
	{
		super();
		this.databaseUtility= databaseUtility;
		this.value.columns =  new LinkedHashMap<String, Column>();
		this.value.name = name;
	}
	
	public TableBuilder catalog(String catalog)
	{
		value.catalog = catalog ;
		return this;
	}
	public TableBuilder schema(String schema)
	{
		value.schema = schema;
		return this;
	}
	public TableBuilder alias(String alias)
	{
		value.alias = alias;
		return this;
	}
	public TableBuilder columns(ColumnsBuilder columnsBuilder) throws TereException
	{
		this.value.columns.putAll(columnsBuilder.build());
		return this;
	}

	@Override
	protected Table createInstance() throws TereException
	{
		return new Table();
	}

	@Override
	public TableBuilder fromJson(JsonElement jsonElement) throws TereException
	{
		return this;
	}

//	@Override
//	public Table build() throws TereException, BuilderException
//	{
//		Table table  = super.build();
//		boolean result = true;
//		try (Connection connection = databaseUtility.getConnection())
//		{
//			try (PreparedStatement preparedStatement = databaseUtility.createTableStatement(connection, table))
//			{
//				result = preparedStatement.execute();
//			}
//			return table;
//		} catch (SQLException e)
//		{
//			throw new TereException(e);
//		}
//	}
}