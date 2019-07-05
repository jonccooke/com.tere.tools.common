package com.tere.utils.db;

import java.io.IOException;
import java.sql.JDBCType;
import java.sql.SQLException;

import org.hsqldb.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tere.TereException;
import com.tere.logging.LogManager;
import com.tere.logging.Logger;
import com.tere.utils.collections.CollectionsUtils;
import com.tere.utils.properties.PropertiesUtils;

public class TestDatabaseUtility
{
	static Logger log = LogManager.getLogger(TestDatabaseUtility.class);
	static Server server;
	static DatabaseUtility databaseUtility;

	@BeforeClass
	public static void setup() throws SQLException, IOException, TereException
	{
		String databaseName = "securitytest";
		int port = 1001;
		String url = "jdbc:hsqldb:hsql://localhost:" + port + "/" + databaseName;

		server = new Server();
		server.setDatabaseName(0, databaseName);

		server.setDatabasePath(0, "./src/test/resources/database/hsqldb/dbtest");
		server.setPort(port);
		server.setSilent(true);
		server.start();

		databaseUtility = new DatabaseUtility(PropertiesUtils.toBuilder().put(databaseUtility.DATABSE_URL_STR, url)
				.put(databaseUtility.DATABASE_SCHEMA, "TESTSCHEMA")
				.put(databaseUtility.DATABSE_DRIVER_STR, "org.hsqldb.jdbc.JDBCDriver").build());
		databaseUtility.executeSQL("DROP SCHEMA TESTSCHEMA IF EXISTS CASCADE");
		databaseUtility.executeSQL("CREATE SCHEMA TESTSCHEMA AUTHORIZATION SA");
		databaseUtility.executeSQL("SET SCHEMA TESTSCHEMA");

		databaseUtility.executeSQL("CREATE TABLE TEST_OBJECT (\r\n" + "    obj_id INTEGER NOT NULL,\r\n"
				+ "    obj_name VARCHAR(100) NOT NULL,\r\n" + "    obj_description VARCHAR(255),\r\n" + "    \r\n"
				+ "    PRIMARY KEY (obj_id)\r\n" + ");");
		
		databaseUtility.insert("TEST_OBJECT", new String[]{"obj_id", "obj_name", "obj_description"}, CollectionsUtils.toList(0, "Test", "Test"));
	}

	@AfterClass
	public static void shutdown() throws SQLException
	{


		databaseUtility.close();

		server.stop();

	}

	@Test
	public void testExpand() throws DatabaseConfigException, SQLException, TereException
	{
		databaseUtility.iterate("select * from {TEST_OBJECT} where obj_id in (?, ?, ?)", (rs)-> {}, CollectionsUtils.toList("Test1", "Test", "Test2"));

	}

	@Test
	public void testSelectWhere() throws DatabaseConfigException, SQLException, TereException
	{
		databaseUtility.iterate("TEST_OBJECT", new String[] {"obj_id", "obj_name", "obj_description"}, new String[] {"obj_id=0"}, (rs)-> {log.info(rs.getString(1));});

	}

	@Test
	public void testSelectWhereOrderBy() throws DatabaseConfigException, SQLException, TereException
	{
		databaseUtility.iterate("TEST_OBJECT", new String[] {"obj_id", "obj_name", "obj_description"}, new String[] {"obj_id=0"}, new String[] {"obj_id"}, (rs)-> {log.info(rs.getString(1));});

	}

	@Test
	public void testInsert() throws DatabaseConfigException, SQLException, TereException
	{
		databaseUtility.insert("TEST_OBJECT", new String[] {"obj_id", "obj_name", "obj_description"}, CollectionsUtils.toList(2, "test", "Test desc"));

		databaseUtility.iterate("select * from TEST_OBJECT", (rs) -> { log.debug("%d", rs.getInt(1));});
	}

	@Test
	public void testUpdate() throws DatabaseConfigException, SQLException, TereException
	{
		databaseUtility.insert("TEST_OBJECT", new String[] {"obj_id", "obj_name", "obj_description"}, CollectionsUtils.toList(2, "test", "Test desc"));
		databaseUtility.iterate("select * from TEST_OBJECT", (rs) -> { log.debug("%d", rs.getInt(1));});
		databaseUtility.update("TEST_OBJECT", new String[] {"obj_id", "obj_name", "obj_description"}, new String[] {"obj_id = '2'"}, CollectionsUtils.toList(3, "test2", "Test desc2"));

		databaseUtility.iterate("select * from TEST_OBJECT", (rs) -> { log.debug("%d", rs.getInt(1));});
	}

	@Test
	public void testCreateTable() throws DatabaseConfigException, SQLException, TereException
	{
		databaseUtility.createTable("test2").schema("TESTSCHEMA").columns(ColumnsBuilder.toBuilder()
					.column().name("col1").type(JDBCType.DOUBLE).build()
					.column().name("col2").type(JDBCType.VARCHAR).length(10).build()
					.column().name("col3").type(JDBCType.DECIMAL).precision(10).scale(10).build())
					.build(); //"select * from {TEST_OBJECT} where obj_id in (?, ?, ?)", (rs)-> {}, CollectionsUtils.toList("Test1", "Test", "Test2"));

	}
//	
//	@Test
//	public void testUnion() throws DatabaseConfigException, SQLException, TereException
//	{
//		databaseUtility.createTable("test3").schema("TESTSCHEMA").columns(ColumnsBuilder.toBuilder()
//					.column().name("col1").type(JDBCType.DOUBLE).build()
//					.column().name("col2").type(JDBCType.VARCHAR).length(10).build()
//					.column().name("col3").type(JDBCType.DECIMAL).precision(10).scale(10).build())
//					.build(); //"select * from {TEST_OBJECT} where obj_id in (?, ?, ?)", (rs)-> {}, CollectionsUtils.toList("Test1", "Test", "Test2"));
//		databaseUtility.createTable("test4").schema("TESTSCHEMA").columns(ColumnsBuilder.toBuilder()
//				.column().name("col1").type(JDBCType.DOUBLE).build()
//				.column().name("col2").type(JDBCType.VARCHAR).length(10).build()
//				.column().name("col3").type(JDBCType.DECIMAL).precision(10).scale(10).build())
//				.build(); //"select * from {TEST_OBJECT} where obj_id in (?, ?, ?)", (rs)-> {}, CollectionsUtils.toList("Test1", "Test", "Test2"));
//
//		databaseUtility.createUnionStatement(connection, catalog1, schema1, tableName1, catalog2, schema2, tableName2, columns)
//	}


	
}
