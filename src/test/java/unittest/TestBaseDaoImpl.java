package unittest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.commons.ConnectionFactory;
import com.dao.BaseDaoImpl;
import com.dao.BaseDaoImplT;
import com.dao.IBaseDao;
import com.dao.IBaseDaoT;
import com.dto.Book;
import com.dto.Student;
import com.dto.Account;
import com.dto.TestBlob;
import com.sun.rowset.CachedRowSetImpl;

public class TestBaseDaoImpl
{
	private Random r = new Random();
	private long start = 0;
	private long end = 0;

	@Before
	public void setUp() throws Exception
	{
		start = System.currentTimeMillis();
	}

	@After
	public void setDown()
	{
		end = System.currentTimeMillis();
		System.err.println("耗时：" + (end - start));
	}

	@Test
	public void testDelete()
	{
		IBaseDao daoImpl = new BaseDaoImpl();
		System.err.println(daoImpl.delete(Book.class, 12));
	}

	@Test
	public void testUpdate()
	{
		IBaseDao daoImpl = new BaseDaoImpl();
		System.err.println(daoImpl.update(new Book(16, "更新16", "作者16", 16, 16)));

	}

	@Test
	public void testGet()
	{
		IBaseDao daoImpl = new BaseDaoImpl();
		Book book = (Book) daoImpl.get(Book.class, 3);
		Student student = (Student) daoImpl.get(Student.class, 3);
		System.err.println(book);
		System.err.println(student);
	}

	@Test
	public void testSave()
	{
		IBaseDao daoImpl = new BaseDaoImpl();
		Book book = new Book(0, "书籍" + r.nextInt(100), "作者" + r.nextInt(100), r.nextInt(100), r.nextInt(50));
		daoImpl.save(book);
	}

	@Test
	public void testFindAll()
	{
		IBaseDaoT<Student> daoImpl = new BaseDaoImplT<Student>(Student.class);
		List<Student> datals = daoImpl.findAll();
		for (Student data : datals)
		{
			System.err.println(data);
		}
	}

	@Test
	public void testTransaction() throws SQLException
	{
		// 判断用户2是否有500余额
		IBaseDaoT<Account> daoImpl = new BaseDaoImplT<Account>(Account.class);
		Account account = daoImpl.get(2);
		int balanceLimit = 1000;

		if (account.getBalance() >= balanceLimit)
		{
			System.err.println("余额充足，共" + account.getBalance() + "元，开始执行扣款...");
			Connection conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			System.err.println("autoCommit=>" + conn.getAutoCommit());
			Savepoint point1 = null;
			Savepoint point2 = null;

			try
			{
				// 对账户2执行扣款500
				String sql = "UPDATE ACCOUNT SET BALANCE=BALANCE-" + balanceLimit + " WHERE ID=?";
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setInt(1, 2);

				System.out.println("执行扣款成功=> " + pstm.executeUpdate());

				point1 = conn.setSavepoint();
				int a = 5 / 0; // 人为抛异常

				// 对账户1执行增加款项500
				String sql2 = "UPDATE ACCOUNT SET BALANCE=BALANCE+" + balanceLimit + " WHERE ID=?";
				PreparedStatement pstm2 = conn.prepareStatement(sql2);
				pstm2.setInt(1, 1);
				System.out.println("执行添加款项成功=> " + pstm2.executeUpdate());

				point2 = conn.setSavepoint();

				conn.commit();
			} catch (Exception e)
			{
				// 发生异常扣款失败
				e.printStackTrace();
				conn.rollback(point2);
				conn.commit();

				System.err.println("异常报错执行回滚...");
				// TODO: handle exception
			} finally
			{
				System.err.println("查询当前余额：");
				System.out.println("账户1：" + daoImpl.get(1));
				System.out.println("账户2：" + daoImpl.get(2));
			}
		}
	}

	@Test
	public void testBigObject() throws Exception
	{
		Connection conn = ConnectionFactory.getConnection();
		String sql = "INSERT INTO tb_Lob (PIC, TXT) VALUES (?,?)";
		PreparedStatement pstm = conn.prepareStatement(sql);
		File f_pic = new File("file/Koala.jpg");
		InputStream picStream = new FileInputStream(f_pic);
		pstm.setBinaryStream(1, picStream, (int) f_pic.length());

		File f_txt = new File("file/linux mysql.txt");
		Reader fr = new InputStreamReader(new FileInputStream(f_txt));
		pstm.setCharacterStream(2, fr, (int) f_txt.length());

		int result = pstm.executeUpdate();
		System.err.println("存储了" + result + "条记录");
	}

	@Test
	public void testGetBlog() throws Exception
	{
		Connection conn = ConnectionFactory.getConnection();
		String sql = "SELECT ID, PIC, TXT FROM TB_LOB WHERE ID=1";
		PreparedStatement pstm = conn.prepareStatement(sql);
		ResultSet rs = pstm.executeQuery();
		TestBlob tb = new TestBlob();
		System.err.println(rs.getType());
		System.err.println(rs.getConcurrency());
		if (rs.next())
		{
			Blob pic = rs.getBlob("pic");
			byte[] picBytes = pic.getBytes(1, (int) pic.length());

			Blob txt = rs.getBlob("txt");
			byte[] txtBytes = txt.getBytes(1, (int) txt.length());

			tb.setId(rs.getInt("id"));
			tb.setPicBytes(picBytes);
			tb.setTxtBytes(txtBytes);
		}

		System.err.println(tb);
	}

	/*
	 * (不使用批量)耗时：7941 (使用批量)耗时：7602
	 */
	@Test
	public void testExecuteBatch() throws Exception
	{
		Connection conn = ConnectionFactory.getConnection();
		try
		{
			String sql = "INSERT INTO tb_batch (NAME) VALUES (?)";
			PreparedStatement pstm = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			int count = 50000;
			for (int i = 0; i < count; i++)
			{
				pstm.setString(1, "name" + i);
				// pstm.executeUpdate();
				pstm.addBatch();
			}
			pstm.executeBatch();
			conn.commit();
		} catch (Exception e)
		{
			conn.rollback();
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	@Test
	public void testBatchOperation()
	{
		Connection conn = ConnectionFactory.getConnection();
		try
		{
			String sql = "SELECT ID, NAME FROM BOOK";
			PreparedStatement pstm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstm.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				if (id == 2)
				{
					System.err.println("找到要指定记录");
					rs.updateString("name", "三国演义");
					rs.updateRow();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	@Test
	public void testDataBaseMetaData() throws SQLException
	{
		Connection conn = ConnectionFactory.getConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		System.err.println("getDatabaseMajorVersion=>" + dbmd.getDatabaseMajorVersion());
		System.err.println("getDatabaseProductName=>" + dbmd.getDatabaseProductName());
		System.err.println("getDatabaseProductVersion=>" + dbmd.getDatabaseProductVersion());
		System.err.println("getURL=>" + dbmd.getURL());
		System.err.println("getUserName=>" + dbmd.getUserName());
	}

	@Test
	public void testOffLineOperation() throws Exception
	{
		Connection conn = ConnectionFactory.getConnection();
		String sql = "SELECT ID, NAME, AUTHOR FROM BOOK";
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt.executeQuery(sql);

		RowSetFactory factory = RowSetProvider.newFactory();
		//相当于c#里面的DataSet
		CachedRowSet crs = new CachedRowSetImpl();
		crs.populate(rs);

		rs.close();
		stmt.close();
		conn.close();

		crs.afterLast();
		while (crs.previous())
		{
			String information = String.format("%d_%s_%s", crs.getInt("id"), crs.getString("name"), crs.getString("author"));
			System.err.println(information);
			int updateId = crs.getInt("id");
			if (updateId == 3)
			{
				crs.updateString("author", "施耐庵");
				crs.updateRow();
				//因为已经是离线操作了，如果需要操作ResultSet必须重新应用到一个新的链接
				Connection newConn = ConnectionFactory.getConnection();
				newConn.setAutoCommit(false);
				crs.acceptChanges(newConn);
			}
		}
	}
}
