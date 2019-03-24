package com.tere.utils.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.StringUtils;
import com.tere.utils.directory.FileUtils;
import com.tere.utils.list.ListUtils;

public class DatabaseUtility
{
	private static Logger log = LogManager.getLogger(DatabaseUtility.class);

	public static final String DATABSE_DRIVER_STR = "databaseDriver";
	public static final String DATABSE_URL_STR = "databaseURL";
	public static final String DATABSE_USERNAME_STR = "databaseUsername";
	public static final String DATABSE_PASSWORD_STR = "databasePassword";
	public static final String DATABSE_POOL_MIN_IDLE_STR = "databaseMinIdle";
	public static final String DATABSE_POOL_MAX_IDLE_STR = "databaseMaxIdle";
	public static final String DATABSE_POOL_MAX_STATEMENTS_STR = "databaseMaxStatements";
	public static final String DATABSE_CATALOG = "databaseCatalog";
	public static final String DATABSE_ATUO_COMMIT = "autoCommit";
	public static final String DATABASE_SCHEMA = "databaseSchema";

	private static BasicDataSource dataSource;
	private static Properties properties = null;
	private boolean intialised =false;;
	private static boolean autoCommit =false;;
	private static String catalog;
	private static String schema;
	
	public interface ResultSetFunction<E extends Exception>
	{
		public void result(ResultSet resultSet) throws SQLException, E;

	}

	public interface ResultSetToObjectFunction<T, E extends Exception>
	{
		public T result(ResultSet resultSet) throws SQLException, E;

	}

	public static boolean isIntialised()
	{
		return !getDataSource().isClosed();
	}

	public static void intialise(Properties properties) throws SQLException, SchemaNotSpecifiedException
	{
		DatabaseUtility.properties = properties;
		catalog = properties.getProperty(DATABSE_CATALOG, "PUBLIC");
		schema = properties.getProperty(DATABASE_SCHEMA);
		if (null == schema)
		{
			throw new SchemaNotSpecifiedException();
		}
		
		try (Connection connection = getDataSource().getConnection())
		{
			autoCommit = Boolean.parseBoolean(properties.getProperty(DATABSE_ATUO_COMMIT, Boolean.toString(connection.getAutoCommit())));
	
		}
	}

	protected static BasicDataSource getDataSource()
	{

		String prop;

		if (null == properties)
		{
			return null;
		}
		if (dataSource == null)
		{

			BasicDataSource ds = new BasicDataSource();

			prop = properties.getProperty(DATABSE_DRIVER_STR);
			ds.setDriverClassName(prop);

			prop = properties.getProperty(DATABSE_URL_STR);
			ds.setUrl(prop);

			ds.setUsername(properties.getProperty(DATABSE_USERNAME_STR));

			ds.setPassword(properties.getProperty(DATABSE_PASSWORD_STR));

			ds.setMinIdle(Integer.parseInt(properties.getProperty(DATABSE_POOL_MIN_IDLE_STR, "5")));

			ds.setMaxIdle(Integer.parseInt(properties.getProperty(DATABSE_POOL_MAX_IDLE_STR, "10")));

			ds.setMaxOpenPreparedStatements(
					Integer.parseInt(properties.getProperty(DATABSE_POOL_MAX_STATEMENTS_STR, "100")));

			dataSource = ds;

		}
		return dataSource;
	}

	public static String getQName(String objectName) 
	{
		return (catalog == null ? "" : catalog + ".") + schema + "." + objectName;
	}
	
	public static Connection getConnection() throws SQLException
	{
		Connection connection = getDataSource().getConnection();
		
		if (null != catalog)
		{
//			connection.setCatalog(catalog);
//			connection.setSchema(schema);
			connection.setAutoCommit(autoCommit);
		}
		
		return connection;
	}
	public static void executeSQL(String sqlString) throws SQLException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{

				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public static void executeSQL(String sqlString, Object... params) throws SQLException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public static <E extends Exception> void iterate(String sqlString, ResultSetFunction<E> resultSetFunction) throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					while (resultSet.next())
					{
						resultSetFunction.result(resultSet);
					}
				}
			}
			connection.close();
		}
	}

	public static <E extends Exception>  void iterate(String sqlString, ResultSetFunction<E> resultSetFunction, Object... params)
			throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					while (resultSet.next())
					{
						resultSetFunction.result(resultSet);
					}
				}

			}
		}
	}


	public static <T, E extends Exception> T one(String sqlString, ResultSetToObjectFunction<T, E> resultSetFunction) throws SQLException, E
	{
		T result = null;
 
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					if (resultSet.next())
					{
						result = resultSetFunction.result(resultSet);
					}
				}
			}
			connection.close();
		}
		return result;
	}

	public static <T, E extends Exception> T one(String sqlString, ResultSetToObjectFunction<T, E> resultSetFunction, Object... params)
			throws SQLException, E
	{
		T result = null;
		
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					if (resultSet.next())
					{
						result = resultSetFunction.result(resultSet);
					}
				}

			}
		}
		return result;
	}


	public static boolean exists(String sqlString, Object... params) throws SQLException
	{

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				log.debug("Param count %d", statement.getParameterMetaData().getParameterCount());
				for (int paramPos = 0;paramPos<params.length;paramPos++)
				{
					Object param = params[paramPos];
					
					statement.setObject(paramPos+1, param);
				}
				try (ResultSet resultSet = statement.executeQuery())
				{
					return resultSet.next();
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return false;
	}

	public static boolean exists(String sqlString, List params) throws SQLException
	{

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				log.debug("Param count %d", statement.getParameterMetaData().getParameterCount());
				for (int paramPos = 0;paramPos<params.size();paramPos++)
				{
					Object param = params.get(paramPos);
					
					statement.setObject(paramPos+1, param);
				}
				try (ResultSet resultSet = statement.executeQuery())
				{
					return resultSet.next();
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return false;
	}

	public static boolean exists(String sqlString) throws SQLException
	{

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();
				try (ResultSet resultSet = statement.executeQuery())
				{
					return statement.getMoreResults();
				}
			}
		}
	}


	public static <T,E extends Exception> List<T> list(List<T> list, String sqlString, ResultSetToObjectFunction<T, E> resultSetListFunction)
			throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					while (resultSet.next())
					{
						list.add(resultSetListFunction.result(resultSet));
					}
				}
				return list;
			}
		}
	}

	public static <T, E extends Exception> List<T> list(List<T> list, String sqlString, ResultSetToObjectFunction<T, E> resultSetListFunction, Object... params)
			throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					while (resultSet.next())
					{
						list.add(resultSetListFunction.result(resultSet));
					}
				}
				return list;
			}
		}
	}

	public static <T, E extends Exception> Set<T> set(Set<T> set, String sqlString, ResultSetToObjectFunction<T, E> resultSetListFunction, Object... params)
			throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					while (resultSet.next())
					{
						set.add(resultSetListFunction.result(resultSet));
					}
				}
				return set;
			}
		}
	}

	public static <T, E extends Exception> Set<T> set(Set<T> set, String sqlString, ResultSetToObjectFunction<T, E> resultSetListFunction)
			throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}

				try (ResultSet resultSet = statement.getResultSet())
				{
					while (resultSet.next())
					{
						set.add(resultSetListFunction.result(resultSet));
					}
				}
				return set;
			}
		}
	}

	protected static PreparedStatement createInsertStatement(String path, Connection connection, String[] columns, Object ... params) throws SQLException
	{
	
		StringBuilder builder = new StringBuilder("insert into ");
		
		builder.append(getQName(path));
		builder.append(" (");
		ListUtils.iterate(Arrays.asList(columns), (pos, val) -> 
			{ 
				builder.append(pos ==0 ? val : ", " + val);
			});
		builder.append(") VALUES (");
		ListUtils.iterate(Arrays.asList(params), (pos, val) -> 
		{ 
			builder.append(pos ==0 ? '?' : ", " + '?');
		});
		builder.append(")");

		final PreparedStatement statement = connection.prepareStatement(builder.toString());

		ListUtils.iterate(Arrays.asList(params), (pos, val) -> 
		{ 
			try
			{
				statement.setObject(pos+1, val);
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		return statement;
	}

	protected static PreparedStatement createInsertStatement(String path, Connection connection, LinkedHashMap<String, Object> cols) throws SQLException
	{
	
		StringBuilder builder = new StringBuilder("insert into ");
		StringBuilder colNames = new StringBuilder();
		StringBuilder colValues = new StringBuilder();
		StringUtils.<String, Object>expand(cols, (f, l, k,v) -> { colNames.append(f ? k : "," + k); colValues.append(f ? "?" : ", ?");return ""; });
		
		builder.append(getQName(path));
		builder.append(" (");
		builder.append(colNames);
		builder.append(") VALUES (");
		builder.append(colValues);
		builder.append(")");

//		ListUtils.iterate(Arrays.asList(params), (pos, val) -> 
//		{ 
//			builder.append(pos ==0 ? '?' : ", " + '?');
//		});

		final PreparedStatement statement = connection.prepareStatement(builder.toString());

		int paramNo=1;
		for (Map.Entry<String, Object> entry : cols.entrySet())
		{
			statement.setObject(paramNo++, entry.getValue());
		};

		return statement;
	}

	//
//	protected static String createSelectColsString(String path,String[] columns, Object ... params)
//	{
//		StringBuilder builder = new StringBuilder("select");
//		
//		
//		builder.append(" ");
//		builder.append(path);
//		builder.append(" ");
//		ListUtils.iterate(Arrays.asList(columns), (pos, val) -> { builder.append(pos ==0 ? val : ", " + val);});
//		builder.append(" ");
//		return builder.toString();
//	}

	public static void insert(String path, String[] columns, Object ... params) throws SQLException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createInsertStatement(path, connection, columns, params))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public interface InsertFunction<T, E extends Exception>
	{
		public void insert(T value, Map<String, Object> cols) throws E;
	}

	public static <T, E extends Exception> void insert(String path, T value, InsertFunction<T, E> insertFunc) throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			LinkedHashMap<String, Object> cols = new LinkedHashMap<String, Object>();
			insertFunc.insert(value, cols);
			
			try (PreparedStatement statement = createInsertStatement(path, connection, cols))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public static void executeSQLFromFile(String filePath) throws SQLException, IOException
	{
		StringBuffer sqlStringBuffer = FileUtils.readTextFile(filePath);
		String sqlString = null;
		if (null == sqlStringBuffer || 0 == sqlStringBuffer.length())
		{
			return;
		}
		sqlString = sqlStringBuffer.toString();
		try (Connection connection = getConnection())
		{
			String[] lines = sqlString.split(";");
			for (String sqlLine : lines)
			{
				log.debug("executing  %s", sqlLine);
				if (0 != sqlLine.trim().length())
				{
					try (PreparedStatement statement = connection.prepareStatement(sqlLine))
					{
						boolean ret = statement.execute();
						if (!autoCommit)
						{
							connection.commit();
						}
					}
				}
			}
		}
	}

	public interface GetterFunc<T,  E extends TereException>
	{
		public void get(T v) throws E;
	}
	
	public static <T, E extends TereException>void getNotNull(String colName, ResultSet rs, GetterFunc<T, E> getterFunc) throws SQLException, E
	{
		T val;
		val = (T)rs.getObject(colName);
		if (!rs.wasNull())
		{
			getterFunc.get(val);
		}
	}

	public static void close() throws SQLException
	{
		if (null != dataSource)
		{
			dataSource.close();
		}
	}

	public static String getCatalog()
	{
		return catalog;
	}

	public static String getSchema()
	{
		return schema;
	}
	
	public static Set<String> getTableNames() throws SQLException
	{
		Set<String> tableNames = new HashSet<String>();
		
		try (Connection connection = getConnection())
		{
			log.debug("Catalog %s, schema %s", connection.getCatalog(), connection.getSchema());
			
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			
			try (ResultSet tableRs = databaseMetaData.getTables(connection.getCatalog(), connection.getSchema(), null, null))
			{
				while (tableRs.next())
				{
					String tableName = tableRs.getString("TABLE_NAME");
					tableNames .add(tableName);
					log.debug(tableName);
				}
			} 
		}
		return tableNames ;

	}
}