package com.mainrun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

import com.commons.ConfigUtils;
import com.mysql.jdbc.Driver;

public class MainRun
{
	private static final String DB_DRIVER = "db.driver";
	private static final String DB_PASSWORD = "db.password";
	private static final String DB_URL = "db.url";
	private static final String DB_USERNAME = "db.username";
	private static ConfigUtils configUtils = new ConfigUtils("/dbconfig.properties");
	private static String dbDriver = "org.gjt.mm.mysql.Driver";
	private static String dbStr = "jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8";
	private static String dbUserName = "root";
	private static String dbPassword = "123456";

	static
	{
		dbDriver = configUtils.getProperty(DB_DRIVER).isEmpty() ? dbDriver : configUtils.getProperty(DB_DRIVER);
		dbStr = configUtils.getProperty(DB_URL).isEmpty() ? dbStr : configUtils.getProperty(DB_URL);
		dbUserName = configUtils.getProperty(DB_USERNAME).isEmpty() ? dbUserName : configUtils.getProperty(DB_USERNAME);
		dbPassword = configUtils.getProperty(DB_PASSWORD).isEmpty() ? dbPassword : configUtils.getProperty(DB_PASSWORD);
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		testGetDriver();
	}

	/**
	 * 测试获取数据库连接
	 */
	private static void testGetDriver()
	{
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try
		{
			conn = DriverManager.getConnection(dbStr, dbUserName, dbPassword);
			stm = conn.createStatement();
			
			// 加载驱动
			Driver driver = new Driver();
			// Connection conn = driver.connect(dbStr, prop);

			System.err.println(conn);

			// 发送sql语句

			String sql = "SELECT 1";
			boolean result = stm.execute(sql);
			System.err.println("执行结果: " + result);

			// String insertSql =
			// "INSERT INTO STUDENT (NAME, CREATETIME) VALUES ('新增数据','2014-08-26')";
			// int icount = stm.executeUpdate(insertSql);
			// System.err.println("新增：" + icount + "条数据");

			// String updateSql =
			// "UPDATE STUDENT SET NAME = '修改数据' WHERE ID = 11";
			// int ucount = stm.executeUpdate(updateSql);
			// System.err.println("修改：" + ucount + "条数据");

			String selectSql = "SELECT * FROM STUDENT";
			rs = stm.executeQuery(selectSql);
			while (rs.next())
			{
				String info = String.format("Id: %d, Name: %s", NumberUtils.toInt(String.valueOf(rs.getInt("ID")), 0),
						String.valueOf(rs.getString("Name")));
				System.err.println(info);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try
			{
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2)
			{
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
	}
}
