package com.mainrun;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.math.NumberUtils;

import com.commons.ConfigUtils;
import com.commons.ConnectionFactory;
import com.dao.BaseDaoImplT;
import com.dao.BookDaoImpl;
import com.dao.IBaseDaoT;
import com.dao.IBookDao;
import com.dao.IStudentDao;
import com.dao.StudentDaoImpl;
import com.dto.Book;
import com.dto.Student;
import com.enums.DaoEnum;
import com.mysql.jdbc.Driver;

public class MainRun
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
		dbDriver = configUtils.getProperty(DB_DRIVER).isEmpty() ? dbDriver : configUtils.getProperty(DB_DRIVER);
		dbUrl = configUtils.getProperty(DB_URL).isEmpty() ? dbUrl : configUtils.getProperty(DB_URL);
		dbUserName = configUtils.getProperty(DB_USERNAME).isEmpty() ? dbUserName : configUtils.getProperty(DB_USERNAME);
		dbPassword = configUtils.getProperty(DB_PASSWORD).isEmpty() ? dbPassword : configUtils.getProperty(DB_PASSWORD);
	}

	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		// testGetDataFromDB();
		// testStudentOperations();
		// testReflect();
		 testDBCP();
	}

	public static void testDBCP() throws Exception
	{
		// 获得数据源对象
		BasicDataSource ds = new BasicDataSource();

		// 连接数据库
		ds.setUrl(dbUrl);
		ds.setDriverClassName(dbDriver);
		ds.setUsername(dbUserName);
		ds.setPassword(dbPassword);
		ds.setMaxActive(4); //最大活动数
		ds.setMaxIdle(2); //最大保存数
		ds.setMaxWait(200);

		Connection conn1 = ds.getConnection();
		System.err.println("conn1 => " + conn1.toString());
		Connection conn2 = ds.getConnection();
		System.err.println("conn2 => " + conn2.toString());
		Connection conn3 = ds.getConnection();
		System.err.println("conn3 => " + conn3.toString());

		conn1.close();
		conn2.close();
		conn3.close();

		System.err.println("=============================");

		Connection conn4 = ds.getConnection();
		System.err.println("conn4 => " + conn4.toString());
		Connection conn5 = ds.getConnection();
		System.err.println("conn5 => " + conn5.toString());
		Connection conn6 = ds.getConnection();
		System.err.println("conn6 => " + conn6.toString());

		conn4.close();
		conn5.close();
		conn6.close();

	}

	public static void testReflect()
	{
		Book book = new Book(1, "三国演义", "罗贯中", 100, 20);
		Class<Book> type = Book.class;
		for (Field field : type.getDeclaredFields())
		{
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}

			try
			{
				System.err.println("field name: " + field.getName() + "-" + field.get(book));
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void testStudentOperations() throws Exception
	{
		// IStudentDao stuDao = new StudentDaoImpl();
		// stuDao.delete(5);
		// System.err.println("删除成功...");

		// List<Student> ls = stuDao.findAll();
		// for (Student student : ls)
		// {
		// System.err.println(student);
		// }

		// System.err.println(stuDao.save(new Student(0, "MONGODB3")));
		// System.err.println(stuDao.get(6));
		// stuDao.update(new Student(6, "最新NOSQL技术"));
		// System.err.println(stuDao.get(6));

		// IBookDao bookDao = new BookDaoImpl();
		// List<Book> books = bookDao.findAll();
		// System.err.println(books);

		// testStudentCRUD(DaoEnum.List, 14);

		testBookCRUD(DaoEnum.Update, 11);
	}

	public static void testBookCRUD(DaoEnum type, int queryId)
	{
		IBookDao dao = new BookDaoImpl();
		Random r = new Random();
		switch (type)
		{
		case Save:
			int id = (Integer) dao.save(new Book(0, "书籍" + System.currentTimeMillis(), "作者" + System.currentTimeMillis(), r.nextInt(100), r
					.nextDouble() * 1000));
			System.err.println("新增记录：" + dao.get(id));
			break;
		case Delete:
			System.err.println("删除了" + dao.delete(queryId) + "条数据");
			break;
		case Update:
			dao.update(new Book(queryId, "更新书籍" + System.currentTimeMillis(), "作者" + r.nextInt(100), r.nextInt(1000), r.nextDouble() * 1000));
			System.err.println("更新记录：" + dao.get(queryId));
			break;
		case Get:
			System.err.println("获取记录" + dao.get(queryId));
			break;
		case List:
			System.err.println("获取列表");
			List<Book> ls = dao.findAll();
			for (Book info : ls)
			{
				System.err.println(info);
			}
			break;
		default:
			break;
		}
	}

	public static void testStudentCRUD(DaoEnum type, int queryId) throws Exception
	{
		IStudentDao dao = new StudentDaoImpl();
		switch (type)
		{
		case Save:
			int id = (Integer) dao.save(new Student(0, "新增学生" + System.currentTimeMillis()));
			System.err.println("新增记录：" + dao.get(id));
			break;
		case Delete:
			dao.delete(queryId);
			break;
		case Update:
			dao.update(new Student(queryId, "更新学生" + System.currentTimeMillis()));
			System.err.println("更新记录：" + dao.get(queryId));
			break;
		case Get:
			System.err.println("获取记录" + dao.get(queryId));
			break;
		case List:
			System.err.println("获取列表");
			List<Student> ls = dao.findAll();
			for (Student info : ls)
			{
				System.err.println(info);
			}
			break;
		default:
			break;
		}
	}

	public static void test(Statement stm) throws SQLException
	{
		// 发送sql语句
		String sql = "SELECT 1";
		boolean result = stm.execute(sql);
		System.err.println("执行结果: " + result);
	}

	public static void insert(Statement stm) throws SQLException
	{
		String insertSql = "INSERT INTO STUDENT (NAME, CREATETIME) VALUES ('新增数据','2014-08-26')";
		int icount = stm.executeUpdate(insertSql);
		System.err.println("新增：" + icount + "条数据");
	}

	public static void update(Statement stm) throws SQLException
	{
		String updateSql = "UPDATE STUDENT SET NAME = '修改数据' WHERE ID = 11";
		int ucount = stm.executeUpdate(updateSql);
		System.err.println("修改：" + ucount + "条数据");
	}

	public static void selectAll(ResultSet rs, Statement stm) throws SQLException
	{
		String selectSql = "SELECT * FROM STUDENT";
		rs = stm.executeQuery(selectSql);
		while (rs.next())
		{
			String info = String.format("Id: %d, Name: %s", NumberUtils.toInt(String.valueOf(rs.getInt("ID")), 0),
					String.valueOf(rs.getString("Name")));
			System.err.println(info);
		}
	}

	public static void selectByIdWithStatement(ResultSet rs, Statement stm, int id) throws SQLException
	{
		String selectSql = "SELECT * FROM STUDENT WHERE ID=" + id;
		rs = stm.executeQuery(selectSql);
		while (rs.next())
		{
			String info = String.format("Id: %d, Name: %s", NumberUtils.toInt(String.valueOf(rs.getInt("ID")), 0),
					String.valueOf(rs.getString("Name")));
			System.err.println(info);
		}
	}

	public static void selectByIdWithPreStatement(Connection conn, ResultSet rs, int id) throws SQLException
	{
		String selectSql = "SELECT * FROM STUDENT WHERE ID = ?";
		PreparedStatement pstm = conn.prepareStatement(selectSql);
		pstm.setLong(1, id);
		// pstm.setString(1, "daviddai");
		rs = pstm.executeQuery();
		// System.out.println(pstm.getQueryTimeout());
		while (rs.next())
		{
			String info = String.format("Id: %d, Name: %s", NumberUtils.toInt(String.valueOf(rs.getInt("ID")), 0),
					String.valueOf(rs.getString("Name")));
			System.err.println(info);
		}
	}

	/**
	 * 测试获取数据库连接
	 */
	public static void testGetDataFromDB()
	{
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try
		{
			conn = ConnectionFactory.getConnection();
			System.err.println("conn:" + conn);
			stm = conn.createStatement();

			// 加载驱动
			Driver driver = new Driver();
			// Connection conn = driver.connect(dbStr, prop);

			System.err.println(conn);

			// test(stm);
			// insert(stm);
			// update(stm);
			// selectAll(rs, stm);

			// selectByIdWithPreStatement(conn, rs, 3);
			selectByIdWithPreStatement(conn, rs, 1);

			Connection conn2 = ConnectionFactory.getConnection();
			System.err.println("conn2: " + conn2);

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			ConnectionFactory.close(conn, stm, rs);
		}
	}
}
