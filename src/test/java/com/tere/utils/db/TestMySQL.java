package com.tere.utils.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.tere.TereException;
import com.tere.utils.properties.PropertiesUtils;

public class TestMySQL
{

	@Test
	public void test() throws SQLException, TereException
	{
		try (DatabaseUtility databaseUtility = new DatabaseUtility(PropertiesUtils.toBuilder()
				.put(DatabaseUtility.DATABSE_URL_STR,
						"jdbc:mysql://database-1.cdeokilyv7zn.eu-west-2.rds.amazonaws.com:3306")
				.put(DatabaseUtility.DATABASE_SCHEMA, "testdb").put(DatabaseUtility.DATABSE_USERNAME_STR, "admin")
				.put(DatabaseUtility.SQL_DIALECT, "mysql")
				.put(DatabaseUtility.DATABSE_PASSWORD_STR, "L1onF1sh")
				.put(DatabaseUtility.DATABSE_DRIVER_STR, "com.mysql.cj.jdbc.Driver").build()))
		{
			Table table = databaseUtility.describeTable("test");
			table=null;
//			try (Connection connection = databaseUtility.getConnection())
//			{
//				try (ResultSet rs = databaseUtility.getTableSchema(connection, "test"))
//				{
//					while (rs.next())
//					{
//						System.out.println("cat:"+rs.getString(1) + ", schema:"+rs.getString(2));
//					}
//					
//				}
////				DatabaseMetaData databaseMetaData = connection.getMetaData();
////
////				try (ResultSet rs = databaseMetaData.getCatalogs())
////				{
////					while (rs.next())
////					{
////						System.out.println("cat:"+rs.getString(1));
////					}
////				}
////				try (ResultSet rs = databaseMetaData.getSchemas())
////				{
////					while (rs.next())
////					{
////						System.out.println("cat:"+rs.getString(1) + ", schema:"+rs.getString(2));
////					}
////				}
////
//			}
		}
	}

}
