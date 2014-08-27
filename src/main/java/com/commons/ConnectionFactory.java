package com.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 连接工厂
 * 
 * @author pc
 * 
 */
public class ConnectionFactory
{
	private static final String DB_DRIVER = "db.driver";
	private static final String DB_PASSWORD = "db.password";
	private static final String DB_URL = "db.url";
	private static final String DB_USERNAME = "db.username";
	private static ConfigUtils configUtils = new ConfigUtils("/dbconfig.properties");
	private static String dbDriver = "org.gjt.mm.mysql.Driver";
	private static String dbUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8";
	private static String dbUserName = "root";
	private static String dbPassword = "123456";

	static
	{
		initDbConfig();
	}

	private static void initDbConfig()
	{
		dbDriver = configUtils.getProperty(DB_DRIVER).isEmpty() ? dbDriver : configUtils.getProperty(DB_DRIVER);
		dbUrl = configUtils.getProperty(DB_URL).isEmpty() ? dbUrl : configUtils.getProperty(DB_URL);
		dbUserName = configUtils.getProperty(DB_USERNAME).isEmpty() ? dbUserName : configUtils.getProperty(DB_USERNAME);
		dbPassword = configUtils.getProperty(DB_PASSWORD).isEmpty() ? dbPassword : configUtils.getProperty(DB_PASSWORD);
	}

	public static Connection getConnection()
	{
		try
		{
			Class.forName(dbDriver);
			Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			return conn;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
	}

	public static void close(Connection conn, Statement stm, ResultSet rs)
	{
		try
		{
			if (rs != null)
				rs.close();
			if (stm != null)
				stm.close();
			if (conn != null)
				conn.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
