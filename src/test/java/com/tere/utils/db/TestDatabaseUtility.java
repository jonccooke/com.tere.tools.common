package com.tere.utils.db;

import java.io.IOException;
import java.sql.SQLException;

import org.hsqldb.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tere.TereException;
import com.tere.utils.collections.CollectionsUtils;
import com.tere.utils.properties.PropertiesUtils;

public class TestDatabaseUtility
{
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

}
