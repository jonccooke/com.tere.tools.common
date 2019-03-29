package com.tere.utils.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbcp2.BasicDataSource;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.StringUtils;
import com.tere.utils.collections.CollectionsUtils;
import com.tere.utils.directory.FileUtils;

public class DatabaseUtility
{
	private Logger log = LogManager.getLogger(DatabaseUtility.class);

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
	private final Pattern EXPAND_PATTERN = Pattern.compile("\\{.*?\\}");

	private BasicDataSource dataSource;
	private Properties properties = null;
	private boolean intialised = false;;
	private boolean autoCommit = false;;
	private String catalog;
	private String schema;

	public interface ResultSetFunction<E extends Exception>
	{
		public void result(ResultSet resultSet) throws SQLException, E;

	}

	public interface ResultSetToObjectFunction<T, E extends Exception>
	{
		public T result(ResultSet resultSet) throws SQLException, E;

	}

	public boolean isIntialised()
	{
		return !getDataSource().isClosed();
	}

	public DatabaseUtility(Properties properties) throws SQLException, DatabaseConfigException
	{
		this.properties = properties;
		catalog = properties.getProperty(DATABSE_CATALOG, "PUBLIC");
		schema = properties.getProperty(DATABASE_SCHEMA);
		if (null == schema)
		{
			throw new SchemaNotSpecifiedException();
		}

		try (Connection connection = getDataSource().getConnection())
		{
			autoCommit = Boolean.parseBoolean(
					properties.getProperty(DATABSE_ATUO_COMMIT, Boolean.toString(connection.getAutoCommit())));

		}
	}

	protected BasicDataSource getDataSource()
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

	public String getQName(String objectName)
	{
		return (catalog == null ? "" : catalog + ".") + schema + "." + objectName;
	}

	public Connection getConnection() throws SQLException
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

	protected String expandSQLString(String sqlString)
	{
		StringBuilder repString = new StringBuilder();

		Matcher matcher = EXPAND_PATTERN.matcher(sqlString);
		int startPos = 0;
		int endPos = 0;
		if (matcher.find())
		{
			int grpCount = matcher.groupCount();
			int matchGrpNo = 0;
//			for (int matchGrpNo = 0; matchGrpNo < grpCount; matchGrpNo++)
			do
			{
				String matchGrpStr = matcher.group(matchGrpNo);
				String tableName = matchGrpStr.substring(1, matchGrpStr.length() - 1);
				endPos = matcher.start(matchGrpNo);
				repString.append(sqlString, startPos, endPos);
				repString.append(getQName(tableName));
				startPos = matcher.end(matchGrpNo);
				matchGrpNo++;
			} while (matchGrpNo<grpCount);
		}
		repString.append(sqlString, startPos, sqlString.length());
		return repString.toString();
	}

	public void executeSQL(String sqlString) throws SQLException
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{

				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public void executeSQL(String sqlString, Object... params) throws SQLException
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	public <E extends Exception> void iterate(String sqlString, ResultSetFunction<E> resultSetFunction)
			throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	public <E extends Exception> void iterate(String sqlString, ResultSetFunction<E> resultSetFunction,
			Object... params) throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	public <E extends Exception> void iterate(String tableName,String[] columns, ResultSetFunction<E> resultSetFunction,
			Object... params) throws SQLException, E
	{
		iterate(tableName, columns, null, resultSetFunction, params);
	}

	public <E extends Exception> void iterate(String tableName,String[] columns, String whereClause, ResultSetFunction<E> resultSetFunction,
			Object... params) throws SQLException, E
	{

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createSelectStatement(connection, expandSQLString(tableName),columns, whereClause, params))
			{
			
				int paramPos = 0;
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

	public <T, E extends Exception> T one(String sqlString, ResultSetToObjectFunction<T, E> resultSetFunction)
			throws SQLException, E
	{
		T result = null;
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	public <T, E extends Exception> T one(String sqlString, ResultSetToObjectFunction<T, E> resultSetFunction,
			Object... params) throws SQLException, E
	{
		T result = null;
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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


	public <T, E extends Exception>T one(String tableName, String[] columns, ResultSetToObjectFunction<T, E> resultSetFunction, Object ...params) throws SQLException, E
	{
		return one(tableName, columns, null, resultSetFunction, params);
	}
	
	public <T, E extends Exception>T one(String tableName, String[] columns, String whereClause,ResultSetToObjectFunction<T, E> resultSetFunction, Object ...params) throws SQLException, E
	{
		T result = null;

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createSelectStatement(connection, expandSQLString(tableName),columns, whereClause, params))
			{
			
				int paramPos = 0;
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

	
	public boolean exists(String sqlString, Object... params) throws SQLException
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				log.debug("Param count %d", statement.getParameterMetaData().getParameterCount());
				for (int paramPos = 0; paramPos < params.length; paramPos++)
				{
					Object param = params[paramPos];

					statement.setObject(paramPos + 1, param);
				}
				try (ResultSet resultSet = statement.executeQuery())
				{
					return resultSet.next();
				}
			}
		} catch (Exception e)
		{
			log.error(e);
		}
		return false;
	}

	public boolean exists(String sqlString, List params) throws SQLException
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				log.debug("Param count %d", statement.getParameterMetaData().getParameterCount());
				for (int paramPos = 0; paramPos < params.size(); paramPos++)
				{
					Object param = params.get(paramPos);

					statement.setObject(paramPos + 1, param);
				}
				try (ResultSet resultSet = statement.executeQuery())
				{
					return resultSet.next();
				}
			}
		} catch (Exception e)
		{
			log.error(e);
		}
		return false;
	}

	public boolean exists(String sqlString) throws SQLException
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				statement.execute();
				try (ResultSet resultSet = statement.executeQuery())
				{
					return statement.getMoreResults();
				}
			}
		}
	}

	public <T, E extends Exception> List<T> list(List<T> list, String sqlString,
			ResultSetToObjectFunction<T, E> resultSetListFunction) throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	public <T, E extends Exception> List<T> list(List<T> list, String sqlString,
			ResultSetToObjectFunction<T, E> resultSetListFunction, Object... params) throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	public <T, E extends Exception> Set<T> set(Set<T> set, String sqlString,
			ResultSetToObjectFunction<T, E> resultSetListFunction, Object... params) throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	public <T, E extends Exception> Set<T> set(Set<T> set, String sqlString,
			ResultSetToObjectFunction<T, E> resultSetListFunction) throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
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

	protected PreparedStatement createInsertStatement(String path, Connection connection, String[] columns,
			Object... params) throws SQLException
	{

		StringBuilder builder = new StringBuilder("insert into ");

		builder.append(getQName(path));
		builder.append(" (");
		CollectionsUtils.iterate(Arrays.asList(columns), (pos, val) ->
			{
				builder.append(pos == 0 ? val : ", " + val);
			});
		builder.append(") VALUES (");
		CollectionsUtils.iterate(Arrays.asList(params), (pos, val) ->
			{
				builder.append(pos == 0 ? '?' : ", " + '?');
			});
		builder.append(")");

		final PreparedStatement statement = connection.prepareStatement(builder.toString());

		CollectionsUtils.iterate(Arrays.asList(params), (pos, val) ->
			{
				try
				{
					statement.setObject(pos + 1, val);
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		return statement;
	}

	protected PreparedStatement createInsertStatement(String path, Connection connection,
			LinkedHashMap<String, Object> cols) throws SQLException
	{

		StringBuilder builder = new StringBuilder("insert into ");
		StringBuilder colNames = new StringBuilder();
		StringBuilder colValues = new StringBuilder();
		StringUtils.<String, Object>expand(cols, (f, l, k, v) ->
			{
				colNames.append(f ? k : "," + k);
				colValues.append(f ? "?" : ", ?");
				return "";
			});

		builder.append(getQName(path));
		builder.append(" (");
		builder.append(colNames);
		builder.append(") VALUES (");
		builder.append(colValues);
		builder.append(")");

//		CollectionsUtils.iterate(Arrays.asList(params), (pos, val) -> 
//		{ 
//			builder.append(pos ==0 ? '?' : ", " + '?');
//		});

		final PreparedStatement statement = connection.prepareStatement(builder.toString());

		int paramNo = 1;
		for (Map.Entry<String, Object> entry : cols.entrySet())
		{
			statement.setObject(paramNo++, entry.getValue());
		}
		;

		return statement;
	}

	protected PreparedStatement createSelectStatement(Connection connection, String tableName, String[] columns,
			String whereClause, Object... params) throws SQLException
	{

		StringBuilder builder = new StringBuilder("select ");

		CollectionsUtils.iterate(Arrays.asList(columns), (pos, val) ->
			{
				builder.append(pos == 0 ? val : ", " + val);
			});
		builder.append(" FROM ");
		builder.append(getQName(tableName));
		if (null != whereClause)
		{
			builder.append(" WHERE ");	
			builder.append(whereClause);
			}
		final PreparedStatement statement = connection.prepareStatement(builder.toString());

		CollectionsUtils.iterate(Arrays.asList(params), (pos, val) ->
			{
				try
				{
					statement.setObject(pos + 1, val);
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		return statement;
	}

	//
//	protected  String createSelectColsString(String path,String[] columns, Object ... params)
//	{
//		StringBuilder builder = new StringBuilder("select");
//		
//		
//		builder.append(" ");
//		builder.append(path);
//		builder.append(" ");
//		CollectionsUtils.iterate(Arrays.asList(columns), (pos, val) -> { builder.append(pos ==0 ? val : ", " + val);});
//		builder.append(" ");
//		return builder.toString();
//	}

	public void insert(String tableName, String[] columns, Object... params) throws SQLException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createInsertStatement(tableName, connection, columns, params))
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

	public <T, E extends Exception> void insert(String tableName, T value, InsertFunction<T, E> insertFunc)
			throws SQLException, E
	{
		try (Connection connection = getConnection())
		{
			LinkedHashMap<String, Object> cols = new LinkedHashMap<String, Object>();
			insertFunc.insert(value, cols);

			try (PreparedStatement statement = createInsertStatement(tableName, connection, cols))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public void executeSQLFromFile(String filePath) throws SQLException, IOException
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

	public interface GetterFunc<T, E extends TereException>
	{
		public void get(T v) throws E;
	}

	public <T, E extends TereException> void getNotNull(String colName, ResultSet rs, GetterFunc<T, E> getterFunc)
			throws SQLException, E
	{
		T val;
		val = (T) rs.getObject(colName);
		if (!rs.wasNull())
		{
			getterFunc.get(val);
		}
	}

	public void close() throws SQLException
	{
		if (null != dataSource)
		{
			dataSource.close();
		}
	}

	public String getCatalog()
	{
		return catalog;
	}

	public String getSchema()
	{
		return schema;
	}

	public Set<String> getTableNames() throws SQLException
	{
		Set<String> tableNames = new HashSet<String>();

		try (Connection connection = getConnection())
		{
			log.debug("Catalog %s, schema %s", connection.getCatalog(), connection.getSchema());

			DatabaseMetaData databaseMetaData = connection.getMetaData();

			try (ResultSet tableRs = databaseMetaData.getTables(connection.getCatalog(), connection.getSchema(), null,
					null))
			{
				while (tableRs.next())
				{
					String tableName = tableRs.getString("TABLE_NAME");
					tableNames.add(tableName);
					log.debug(tableName);
				}
			}
		}
		return tableNames;

	}
}