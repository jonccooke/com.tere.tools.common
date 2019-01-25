package com.tere.utils.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.dbcp2.BasicDataSource;

import com.tere.logging.LogManager;
import com.tere.logging.Logger;
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

	private static BasicDataSource dataSource;
	private static Properties properties = null;
	private boolean intialised =false;;
	public interface ResultSetFunction
	{
		public void result(ResultSet resultSet) throws SQLException;

	}

	public interface ResultSetListFunction<T>
	{
		public T result(ResultSet resultSet) throws SQLException;

	}

	public static boolean isIntialised()
	{
		return !getDataSource().isClosed();
	}

	public static void intialise(Properties properties) throws SQLException
	{
		DatabaseUtility.properties = properties;
		try (Connection connection = getDataSource().getConnection())
		{
			
		}
	}

	public static BasicDataSource getDataSource()
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

	public static void executeSQL(String sqlString) throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{

				statement.execute();

				connection.commit();
			}
		}
	}


	public static void executeSQL(String sqlString, Object... params) throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				connection.commit();
			}
		}
	}

	public static void iterate(String sqlString, ResultSetFunction resultSetFunction) throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				connection.commit();

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

	public static void iterate(String sqlString, ResultSetFunction resultSetFunction, Object... params)
			throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				connection.commit();

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


	public static <T> T one(String sqlString, ResultSetListFunction<T> resultSetFunction) throws SQLException
	{
		T result = null;
 
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				connection.commit();

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

	public static <T> T one(String sqlString, ResultSetListFunction<T> resultSetFunction, Object... params)
			throws SQLException
	{
		T result = null;
		
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				connection.commit();

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

		try (Connection connection = getDataSource().getConnection())
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

	public static boolean exists(String sqlString) throws SQLException
	{

		try (Connection connection = getDataSource().getConnection())
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


	public static <T> List<T> list(List<T> list, String sqlString, ResultSetListFunction<T> resultSetListFunction)
			throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				connection.commit();

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

	public static <T> List<T> list(List<T> list, String sqlString, ResultSetListFunction<T> resultSetListFunction, Object... params)
			throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				connection.commit();

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

	public static <T> Set<T> set(Set<T> set, String sqlString, ResultSetListFunction<T> resultSetListFunction, Object... params)
			throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				int paramPos = 0;
				
				for (Object param : params)
				{
					statement.setObject(++paramPos, param);
				}
				statement.execute();

				connection.commit();

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

	public static <T> Set<T> set(Set<T> set, String sqlString, ResultSetListFunction<T> resultSetListFunction)
			throws SQLException
	{
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(sqlString))
			{
				statement.execute();

				connection.commit();

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
	
		StringBuilder builder = new StringBuilder("insert into");
		
		builder.append(" ");
		builder.append(path);
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
		try (Connection connection = getDataSource().getConnection())
		{
			try (PreparedStatement statement = createInsertStatement(path, connection, columns, params))
			{
				statement.execute();

				connection.commit();
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
		try (Connection connection = getDataSource().getConnection())
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
						connection.commit();
					}
				}
			}
		}
	}

	public static void close() throws SQLException
	{
		if (null != dataSource)
		{
			dataSource.close();
		}
	}
}