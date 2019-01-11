package com.tere.utils.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.directory.FileUtils;

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

	public static void intialise(Properties properties)
	{
		DatabaseUtility.properties = properties;
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

			ds.setMinIdle(Integer.parseInt(properties.getProperty(
					DATABSE_POOL_MIN_IDLE_STR, "5")));

			ds.setMaxIdle(Integer.parseInt(properties.getProperty(
					DATABSE_POOL_MAX_IDLE_STR, "10")));

			ds.setMaxOpenPreparedStatements(Integer.parseInt(properties
					.getProperty(DATABSE_POOL_MAX_STATEMENTS_STR, "100")));

			dataSource = ds;

		}
		return dataSource;
	}

	public static void executeSQL(String sqlString, Object ...params) throws SQLException
	{
		Connection connection = null;
		PreparedStatement statement = null;

		try
		{
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sqlString);
			int paramPos =0;
			for (Object param : params)
			{
				statement.setObject(++paramPos, param);
			}
			statement.execute();
			
			connection.commit();
		}
		finally
		{
			if (null != statement)
			{
				statement.close();
			}
			if (null != connection)
			{
				connection.close();
			}
		}
	}

	public static void executeSQL(String sqlString, ResultSetFunction resultSetFunction, Object ...params) throws SQLException
	{
		Connection connection = null;
		PreparedStatement statement = null;

		try
		{
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sqlString);
			int paramPos =0;
			for (Object param : params)
			{
				statement.setObject(++paramPos, param);
			}
			statement.execute();
			
			connection.commit();

			resultSetFunction.result(statement.getResultSet());

		}
		finally
		{
			if (null != statement)
			{
				statement.close();
			}
			if (null != connection)
			{
				connection.close();
			}
		}
	}

	public static void executeSQL(String sqlString) throws SQLException
	{
		Connection connection = null;
		PreparedStatement statement = null;

		try
		{
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sqlString);

			statement.execute();
			
			connection.commit();
		}
		finally
		{
			if (null != statement)
			{
				statement.close();
			}
			if (null != connection)
			{
				connection.close();
			}
		}
	}


	public interface ResultSetFunction 
	{
		public void result(ResultSet resultSet);
		
	}
	public static void executeSQL(String sqlString, ResultSetFunction resultSetFunction) throws SQLException
	{
		Connection connection = null;
		PreparedStatement statement = null;

		try
		{
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sqlString);

			statement.execute();
				
			connection.commit();
			
			resultSetFunction.result(statement.getResultSet());
		}
		finally
		{
			if (null != statement)
			{
				statement.close();
			}
			if (null != connection)
			{
				connection.close();
			}
		}
	}

	public static void executeSQLFromFile(String filePath) throws SQLException, IOException
	{
		Connection connection = null;
		Statement statement = null;
		StringBuffer sqlStringBuffer = FileUtils.readTextFile(filePath);
		String sqlString = null;
		if (null == sqlStringBuffer || 0 == sqlStringBuffer.length())
		{
			return;
		}
		sqlString = sqlStringBuffer.toString();
		try
		{
			connection = getDataSource().getConnection();
			statement = connection.createStatement();
			String[] lines = sqlString.split(";");
			for (String sqlLine : lines)
			{
				log.debug("executing  %s", sqlLine);
				if (0 != sqlLine.trim().length())
				{
					boolean ret = statement.execute(sqlLine);
					connection.commit();
				}
			}
		}
		finally
		{
			if (null != statement)
			{
				statement.close();
			}
			if (null != connection)
			{
				connection.close();
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