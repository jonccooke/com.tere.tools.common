package com.tere.utils.db;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.tere.TereException;
import com.tere.builder.BuilderException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.StringUtils;
import com.tere.utils.collections.CollectionsUtils;
import com.tere.utils.directory.FileUtils;
import com.tere.utils.properties.PropertiesUtils;

public class DatabaseUtility implements AutoCloseable
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
	public static final String SQL_DIALECT = "dialect";

	private static final int CAT_NAME_POS = 1;
	private static final int SCHEMA_NAME_POS = 2;
	private static final int TABLE_NAME_POS = 3;
	private static final int DATA_TYPE_POS = 5;
	private static final int COLUMN_NAME_POS = 4;
	private static final int NULLABLE_POS = 11;
	private static final int COLUMN_SIZE_POS = 7;
	private static final int DECIMAL_DIGITS = 9;
	private static final int NUM_PREC_RADIX = 10;

	private final Pattern EXPAND_PATTERN = Pattern.compile("\\{.*?\\}");

	private BasicDataSource dataSource;
	private Properties properties = null;
	private boolean intialised = false;;
	private boolean autoCommit = false;;
	private String catalog;
	private String schema;
	private Properties dialects;
	private String dialect;
	private String createTableStatementStr;
	private String insertStatementStr;
	private String updateStatementStr;
	private String selectStatementStr;
	private String deleteTableStatementStr;
	private String deleteRowsStatementStr;
	private String unionStatementStr;
	private String joinStatementStr;
	private VelocityEngine velocityEngine;

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

	public DatabaseUtility(Properties properties) throws TereException
	{
		this.properties = properties;
		catalog = properties.getProperty(DATABSE_CATALOG, "PUBLIC");
		schema = properties.getProperty(DATABASE_SCHEMA);
//		if (null == schema)
//		{
//			throw new SchemaNotSpecifiedException();
//		}
		dialect = properties.getProperty(SQL_DIALECT, "hsqldb").toLowerCase();

		if (dialect.equals("mysql"))
		{
			catalog = null;
		}
		try
		{
			dialects = PropertiesUtils.load("classpath:db/sql-dialects/sql-dialects.properties");

			try (Connection connection = getDataSource().getConnection())
			{
				autoCommit = Boolean.parseBoolean(
						properties.getProperty(DATABSE_ATUO_COMMIT, Boolean.toString(connection.getAutoCommit())));

				if (null == schema)
				{
					schema = connection.getSchema();
				}
				String catalog = connection.getCatalog();

			}
//			new org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader().
			velocityEngine = new VelocityEngine();
			velocityEngine.init();
//			velocityEngine .
//			String path = "src/main/resources/db/sql-dialects/" + dialect+ "/createtable.vm";
			createTableStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/createtable.vm")
					.toString();
			selectStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/select.vm")
					.toString();
			deleteTableStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/deleteTable.vm")
					.toString();
			deleteRowsStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/deleteRows.vm")
					.toString();
			unionStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/union.vm").toString();
			joinStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/join.vm").toString();

			insertStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/insert.vm")
					.toString();
			
			updateStatementStr = FileUtils.readTextFile("classpath:db/sql-dialects/" + dialect + "/update.vm")
					.toString();
			
//					velocityEngine
//					.getTemplate(path);
		} catch (ResourceNotFoundException | ParseErrorException | SQLException | IOException e)
		{
			throw new TereException(e);
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
		while (matcher.find(startPos))
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
			} while (matchGrpNo < grpCount);
		}
		repString.append(sqlString, startPos, sqlString.length());
		return repString.toString();
	}

	protected static int expandParam(PreparedStatement statement, int pos, Collection param) throws SQLException
	{
		int pos2 = pos;
		for (Object p : (Collection) param)
		{
			statement.setObject(pos2++, p);
		}
		return pos2;
	}

	protected void addParams(PreparedStatement statement, Object[] params) throws SQLException
	{
		if (null == params)
		{
			return;
		}
		int pos = 1;

		for (Object param : params)
		{
			statement.setObject(pos++, param);
		}


	}

	protected void addParams(PreparedStatement statement, List params) throws SQLException
	{
		if (null == params)
		{
			return;
		}
		int pos = 1;

		for (Object param : params)
		{
//			if (param != null)
//			{
//				if (param instanceof Collection)
//				{
//					for (Object p : (Collection) param)
//					{
//						statement.setObject(pos++, p);
//					}
//				} else if (param.getClass().isArray())
//				{
//					for (Object p : (Object[]) param)
//					{
//						statement.setObject(pos++, p);
//					}
//				} else
//				{
//					statement.setObject(pos++, param);
//
//				}
//			} else
//			{
			statement.setObject(pos++, param);
		}

//		}

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

	public void executeSQL(String sqlString, List params) throws SQLException
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				addParams(statement, params);
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public <E extends Exception> void executeSQL(String sqlString, ResultSetFunction<E> resultSetFunction)
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
		}
	}

	public <E extends Exception> void executeSQL(String sqlString, List params, ResultSetFunction<E> resultSetFunction)
			throws SQLException, E
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				addParams(statement, params);
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

	public <E extends Exception> void iterate(String sqlString, ResultSetFunction<E> resultSetFunction, List params)
			throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				addParams(statement, params);
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

	public <E extends Exception> void iterate(String tableName, String[] columns, List params,
			ResultSetFunction<E> resultSetFunction) throws SQLException, E, TereException
	{
		iterate(catalog, schema, tableName, columns, null, null, null, -1, -1, params, resultSetFunction);

	}

	public <E extends Exception> void iterate(String tableName, String[] columns,
			ResultSetFunction<E> resultSetFunction) throws SQLException, E, TereException
	{
		iterate(catalog, schema, tableName, columns, null, null, null, -1, -1, null, resultSetFunction);

	}

	public <E extends Exception> void iterate(String tableName, String[] columns, String[] whereItems,
			ResultSetFunction<E> resultSetFunction) throws TereException, SQLException, E
	{
		iterate(catalog, schema, tableName, columns, whereItems, null, null, -1, -1, null, resultSetFunction);
	}

	public <E extends Exception> void iterate(String tableName, String[] columns, String[] whereItems,
			String[] orderByItems, ResultSetFunction<E> resultSetFunction) throws TereException, SQLException, E
	{
		iterate(catalog, schema, tableName, columns, whereItems, orderByItems, null, -1, -1, null, resultSetFunction);
	}

	public <E extends Exception> void iterate(String tableName, String[] columns, String[] whereItems, List params,
			ResultSetFunction<E> resultSetFunction) throws TereException, SQLException, E
	{
		iterate(catalog, schema, tableName, columns, whereItems, null, null, -1, -1, params, resultSetFunction);

	}

	public <E extends Exception> void iterate(String catalog, String schema, String tableName, String[] columns,
			String[] whereItems, String[] orderByItems, String[] groupByItems, long limit, long offset, List params,
			ResultSetFunction<E> resultSetFunction) throws TereException, SQLException, E
	{

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createSelectStatement(connection, catalog, schema, tableName, columns,
					whereItems, orderByItems, groupByItems, limit, offset, params))
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
		}
	}

	public <T, E extends Exception> T one(String catalog, String schema, String tableName, String[] columns,
			String[] whereItems, String[] orderByItems, String[] groupByItems, long limit, long offset, List params,
			ResultSetToObjectFunction<T, E> resultSetFunction) throws TereException, SQLException, E
	{
		T result = null;

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createSelectStatement(connection, catalog, schema, tableName, columns,
					whereItems, orderByItems, groupByItems, limit, offset, params))
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
		}
		return result;
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
			List params) throws SQLException, E
	{
		T result = null;
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				addParams(statement, params);

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

	public <T, E extends Exception> T one(String tableName, String[] columns, List params,
			ResultSetToObjectFunction<T, E> resultSetFunction) throws SQLException, E, TereException
	{
		return one(catalog, schema, tableName, columns, null, null, null, -1, -1, params, resultSetFunction);
	}

	public <T, E extends Exception> T one(String tableName, String[] columns,
			ResultSetToObjectFunction<T, E> resultSetFunction) throws SQLException, E, TereException
	{
		return one(catalog, schema, tableName, columns, null, null, null, -1, -1, null, resultSetFunction);
	}

	public <T, E extends Exception> T one(String tableName, String[] columns, String[] whereItems, List params,
			ResultSetToObjectFunction<T, E> resultSetFunction) throws SQLException, E, TereException
	{

		return one(catalog, schema, tableName, columns, whereItems, null, null, -1, -1, params, resultSetFunction);
	}

	public <T, E extends Exception> T one(String tableName, String[] columns, String[] whereItems,
			ResultSetToObjectFunction<T, E> resultSetFunction) throws SQLException, E, TereException
	{

		return one(catalog, schema, tableName, columns, whereItems, null, null, -1, -1, null, resultSetFunction);
	}

	public boolean exists(String sqlString, List params) throws SQLException
	{
		String expString = expandSQLString(sqlString);

		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				addParams(statement, params);

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

//	public boolean exists(String sqlString, List params) throws SQLException
//	{
//		String expString = expandSQLString(sqlString);
//
//		try (Connection connection = getConnection())
//		{
//			try (PreparedStatement statement = connection.prepareStatement(expString))
//			{
//				addParams(statement, params);
//				
//				try (ResultSet resultSet = statement.executeQuery())
//				{
//					return resultSet.next();
//				}
//			}
//		} catch (Exception e)
//		{
//			log.error(e);
//		}
//		return false;
//	}

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
			ResultSetToObjectFunction<T, E> resultSetListFunction, List params) throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				addParams(statement, params);

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
			ResultSetToObjectFunction<T, E> resultSetListFunction, List params) throws SQLException, E
	{
		String expString = expandSQLString(sqlString);
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = connection.prepareStatement(expString))
			{
				addParams(statement, params);

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

	public PreparedStatement createInsertStatement(String catalog, String schema, String tableName,
			Connection connection, String[] columns, List params) throws SQLException, TereException
	{

		final PreparedStatement preparedStatement = createInsertStatement(catalog, schema, tableName, connection,
				columns);

		addParams(preparedStatement, params);
		return preparedStatement;
	}

	public PreparedStatement createInsertStatement(String catalog, String schema, String tableName,
			Connection connection, String[] columns, Object[][] params) throws SQLException, TereException
	{

		final PreparedStatement preparedStatement = createInsertStatement(catalog, schema, tableName, connection,
				columns);

		for (Object[] paramRow : params)
		{
			addParams(preparedStatement, paramRow);
			preparedStatement.addBatch();
		}
		return preparedStatement;
	}

	public PreparedStatement createInsertStatement(String catalog, String schema, String tableName,
			Connection connection, String[] columns) throws SQLException, TereException
	{
		VelocityContext context = new VelocityContext();
//		StringWriter writer = new StringWriter();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(insertStatementStr))
		{

			context.put("catalog", catalog);
			context.put("schema", schema);
			context.put("table", tableName);
			context.put("columns", columns);
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "selectStatement", reader);
//			createTableStatement.merge(context, writer);

			String stmtStr = writer.getBuffer().toString();

			log.debug(stmtStr);
			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}

	public PreparedStatement createInsertStatement(String tableName, Connection connection,
			LinkedHashMap<String, Object> cols) throws TereException, SQLException, RuntimeException
	{

		return createInsertStatement(catalog, schema, tableName, connection,
				CollectionsUtils.toArray(cols.keySet(), new String[cols.size()], (p, v) -> v),  CollectionsUtils.toList(cols.values(), (p, v)-> v));
//
//		StringBuilder builder = new StringBuilder("insert into ");
//		StringBuilder colNames = new StringBuilder();
//		StringBuilder colValues = new StringBuilder();
//		StringUtils.<String, Object>expand(cols, (f, l, k, v) ->
//			{
//				colNames.append(f ? k : "," + k);
//				colValues.append(f ? "?" : ", ?");
//				return "";
//			});
//
//		builder.append(getQName(path));
//		builder.append(" (");
//		builder.append(colNames);
//		builder.append(") VALUES (");
//		builder.append(colValues);
//		builder.append(")");
//
////		CollectionsUtils.iterate(Arrays.asList(params), (pos, val) -> 
////		{ 
////			builder.append(pos ==0 ? '?' : ", " + '?');
////		});
//
//		final PreparedStatement statement = connection.prepareStatement(builder.toString());
//
//		int paramNo = 1;
//		for (Map.Entry<String, Object> entry : cols.entrySet())
//		{
//			statement.setObject(paramNo++, entry.getValue());
//		}
//		;
//
//		return statement;
	}

	public PreparedStatement createSelectStatement(Connection connection, String catalog, String schema,
			String tableName, String[] columns, String[] whereItems, String[] orderByItems, String[] groupByItems,
			long limit, long offset, List params) throws TereException
	{
		VelocityContext context = new VelocityContext();
//	StringWriter writer = new StringWriter();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(selectStatementStr))
		{

			context.put("catalog", catalog);
			context.put("schema", schema);
			context.put("table", tableName);
			context.put("columns", columns);
			context.put("whereItems", whereItems);
			context.put("orderByItems", orderByItems);
			context.put("groupByItems", groupByItems);
			if (-1 != limit)
			{
				context.put("limit", limit);
			}
			if (-1 != offset)
			{
				context.put("offset", offset);
			}
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "selectStatement", reader);
//		createTableStatement.merge(context, writer);

			String stmtStr = writer.getBuffer().toString();

			log.debug(stmtStr);
			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			addParams(preparedStatement, params);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}

	public PreparedStatement createDropTableStatement(Connection connection, String catalog, String schema,
			String tableName) throws TereException
	{
		VelocityContext context = new VelocityContext();
//	StringWriter writer = new StringWriter();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(deleteTableStatementStr))
		{

			context.put("catalog", catalog);
			context.put("schema", schema);
			context.put("table", tableName);
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "selectStatement", reader);
//		createTableStatement.merge(context, writer);

			String stmtStr = writer.getBuffer().toString();

			log.debug(stmtStr);
			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}
	public PreparedStatement createDeleteRowsStatement(Connection connection, String catalog, String schema,
			String tableName, String[] whereItems, List params) throws TereException
	{
		VelocityContext context = new VelocityContext();
//	StringWriter writer = new StringWriter();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(deleteRowsStatementStr))
		{

			context.put("catalog", catalog);
			context.put("schema", schema);
			context.put("table", tableName);
			context.put("whereItems", whereItems);
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "selectStatement", reader);
//		createTableStatement.merge(context, writer);

			String stmtStr = writer.getBuffer().toString();

			log.debug(stmtStr);
			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			addParams(preparedStatement, params);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}

	
	public PreparedStatement createUpdateStatement(String tableName, Connection connection, String[] columns,
			String[] whereItems, List params) throws SQLException, TereException
	{
		VelocityContext context = new VelocityContext();
//		StringWriter writer = new StringWriter();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(updateStatementStr))
		{

			context.put("catalog", catalog);
			context.put("schema", schema);
			context.put("table", tableName);
			context.put("columns", columns);
			context.put("whereItems", whereItems);
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "selectStatement", reader);
//			createTableStatement.merge(context, writer);

			String stmtStr = writer.getBuffer().toString();

			log.debug(stmtStr);
			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			addParams(preparedStatement, params);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}

	public PreparedStatement createUnionStatement(Connection connection, String catalog1, String schema1,
			String tableName1, String catalog2, String schema2, String tableName2, String[] columns)
			throws TereException
	{
		VelocityContext context = new VelocityContext();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(unionStatementStr))
		{

			context.put("catalog1", catalog1);
			context.put("schema1", schema1);
			context.put("table1", tableName1);
			context.put("columns", columns);
			context.put("catalog1", catalog2);
			context.put("schema1", schema2);
			context.put("table1", tableName2);
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "unionStatement", reader);

			String stmtStr = writer.getBuffer().toString();

			log.debug(stmtStr);
			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}

	protected PreparedStatement createJoinStatement(Connection connection, String joinType, String catalog1,
			String schema1, String tableName1, String catalog2, String schema2, String tableName2, String[] columns,
			String[] whereItems, String[] onItems) throws TereException
	{
		VelocityContext context = new VelocityContext();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(joinStatementStr))
		{

			String table1QName = (catalog1 != null ? catalog1 + "." : "") + (schema1 != null ? schema1 + "." : "")
					+ tableName1;
			String table2QName = (catalog2 != null ? catalog2 + "." : "") + (schema2 != null ? schema2 + "." : "")
					+ tableName2;
			context.put("joinType", joinType);
			context.put("table1", table1QName);
			context.put("columns", columns);
			context.put("table2", table2QName);
			context.put("onItems", onItems);
			context.put("whereItems", whereItems);
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "joinStatement", reader);

			String stmtStr = writer.getBuffer().toString();

			log.debug(stmtStr);
			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}

	public PreparedStatement createCrossJoinStatement(Connection connection, String catalog1, String schema1,
			String tableName1, String catalog2, String schema2, String tableName2, String[] columns,
			String[] whereItems, String[] onItems) throws TereException
	{
		return createJoinStatement(connection, "CROSS", catalog1, schema1, tableName1, catalog2, schema2, tableName2,
				columns, whereItems, onItems);
	}

	public PreparedStatement createLeftJoinStatement(Connection connection, String catalog1, String schema1,
			String tableName1, String catalog2, String schema2, String tableName2, String[] columns,
			String[] whereItems, String[] onItems, boolean outer) throws TereException
	{
		return createJoinStatement(connection, outer ? "LEFT OUTER" : "LEFT", catalog1, schema1, tableName1, catalog2,
				schema2, tableName2, columns, whereItems, onItems);
	}

	public PreparedStatement createRightJoinStatement(Connection connection, String catalog1, String schema1,
			String tableName1, String catalog2, String schema2, String tableName2, String[] columns,
			String[] whereItems, String[] onItems, boolean outer) throws TereException
	{
		return createJoinStatement(connection, outer ? "RIGHT OUTER" : "RIGHT", catalog1, schema1, tableName1, catalog2,
				schema2, tableName2, columns, whereItems, onItems);
	}

	public PreparedStatement createInnerJoinStatement(Connection connection, String catalog1, String schema1,
			String tableName1, String catalog2, String schema2, String tableName2, String[] columns,
			String[] whereItems, String[] onItems) throws TereException
	{
		return createJoinStatement(connection, "INNER", catalog1, schema1, tableName1, catalog2, schema2, tableName2,
				columns, whereItems, onItems);
	}

	public void insert(String tableName, String[] columns, List params) throws SQLException, TereException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createInsertStatement(catalog, schema, tableName, connection, columns,
					params))
			{

				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}

	public void insert(String tableName, String[] columns, Object[][] params) throws SQLException, TereException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createInsertStatement(catalog, schema, tableName, connection, columns,
					params))
			{

				connection.setAutoCommit(false);
				
				statement.executeBatch();

				connection.commit();

				if (autoCommit)
				{
					connection.setAutoCommit(autoCommit);
				}
			}
		}
	}

	public interface InsertFunction<T, E extends Exception>
	{
		public void insert(T value, Map<String, Object> cols) throws E;
	}

	public <T, E extends Exception> void insert(String tableName, T value, InsertFunction<T, E> insertFunc)
			throws SQLException, TereException, E
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

	public int update(String tableName, String[] columns, String[] whereItems, List params) throws SQLException, TereException
	{
		try (Connection connection = getConnection();
				PreparedStatement statement = createUpdateStatement(tableName, connection, columns, whereItems, params))
		{
			statement.execute();

			if (!autoCommit)
			{
				connection.commit();
			}
			return statement.getUpdateCount();
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
				sqlString = expandSQLString(sqlLine);
				log.debug("executing  %s", sqlString);
				if (0 != sqlLine.trim().length())
				{
					try (PreparedStatement statement = connection.prepareStatement(sqlString))
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

	public PreparedStatement createTableStatement(Connection connection, Table table) throws TereException
	{
		VelocityContext context = new VelocityContext();
//		StringWriter writer = new StringWriter();
		try (StringWriter writer = new StringWriter(); StringReader reader = new StringReader(createTableStatementStr))
		{

			context.put("table", table);
			context.put("su", new StringUtils());
			velocityEngine.evaluate(context, writer, "createTableStatement", reader);
//			createTableStatement.merge(context, writer);

			String stmtStr = writer.getBuffer().toString();

			PreparedStatement preparedStatement = connection.prepareStatement(stmtStr);
			return preparedStatement;
		} catch (Exception e)
		{
			throw new TereException(e);
		}
	}

	public TableBuilder createTable(String tableName) throws TereException
	{
		return new TableBuilder(this, tableName)
		{

			@Override
			public Table build() throws TereException, BuilderException
			{
				Table table = super.build();

				boolean result = true;
				try (Connection connection = getConnection())
				{
					try (PreparedStatement preparedStatement = createTableStatement(connection, table))
					{
						result = preparedStatement.execute();
					}
					return table;
				} catch (SQLException e)
				{
					throw new TereException(e);
				}

			}
		};
	}

	public Table describeTable(String tableName) throws TereException, SQLException
	{

//		String[] paths = tablePath.split("[.]");

		try (Connection connection = getConnection())
		{
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ColumnsBuilder columnsBuilder = ColumnsBuilder.toBuilder();
			try (ResultSet resultSet = databaseMetaData.getColumns(catalog, schema, tableName, null))
			{
				while (resultSet.next())
				{
//					catalogName = resultSet.getString(CAT_NAME_POS);
//					schemaName = resultSet.getString(SCHEMA_NAME_POS);
//					tableName = resultSet.getString(TABLE_NAME_POS);
					ColumnBuilder columnBuilder = columnsBuilder.column().name(resultSet.getString(COLUMN_NAME_POS))
							.nullable(resultSet.getBoolean(NULLABLE_POS))
							.type(JDBCType.valueOf(resultSet.getInt(DATA_TYPE_POS)));
					switch (resultSet.getShort(DATA_TYPE_POS))
					{
					case Types.DECIMAL:
					case Types.NUMERIC:
						columnBuilder.scale(resultSet.getInt(DECIMAL_DIGITS))
								.precision(resultSet.getInt(COLUMN_SIZE_POS));
						break;
					case Types.BLOB:
					case Types.CLOB:
					case Types.JAVA_OBJECT:
					case Types.LONGNVARCHAR:
					case Types.LONGVARBINARY:
					case Types.LONGVARCHAR:
					case Types.NCLOB:
					case Types.NVARCHAR:
					case Types.STRUCT:
					case Types.ARRAY:
					case Types.VARBINARY:
					case Types.VARCHAR:
						columnBuilder.length(resultSet.getInt(COLUMN_SIZE_POS));
						break;
					}
					columnBuilder.build();
				}
//				String[] qName = path.split("[.]");

				Table table = new TableBuilder(this, tableName).schema(schema).catalog(catalog).columns(columnsBuilder)
						.build();

				return table;
			}
		}
	}

	public ResultSet getTableSchema(Connection connection, String tableName) throws SQLException
	{
		DatabaseMetaData databaseMetaData = connection.getMetaData();

		return databaseMetaData.getColumns(catalog, schema, tableName, null);
	}

	public void drop(String tableName) throws SQLException, TereException
	{
		drop(catalog, schema, tableName);
	}	
	
	public void drop(String catalog, String schema, String tableName) throws SQLException, TereException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createDropTableStatement(connection, catalog, schema, tableName))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
	}
	
	public void delete(String catalog, String schema, String tableName) throws SQLException, TereException
	{
		delete(catalog, schema, tableName, null, null); 
	}
//		try (Connection connection = getConnection())
//		{
//			try (PreparedStatement statement = createDeleteTableStatement(connection, catalog, schema, tableName))
//			{
//				statement.execute();
//
//				if (!autoCommit)
//				{
//					connection.commit();
//				}
//			}
//		}
//		
//	}
	public void delete(String tableName, String[] whereItems) throws SQLException, TereException
	{
		delete(catalog, schema, tableName, whereItems, null);
	}
	
	public void delete(String catalog, String schema, String tableName, String[] whereItems, List params) throws SQLException, TereException
	{
		try (Connection connection = getConnection())
		{
			try (PreparedStatement statement = createDeleteRowsStatement(connection, catalog, schema, tableName, whereItems, params))
			{
				statement.execute();

				if (!autoCommit)
				{
					connection.commit();
				}
			}
		}
		
	}

	// public CreateTableBuilder createTable(String name, Column)
}