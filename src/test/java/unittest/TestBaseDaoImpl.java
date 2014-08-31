package unittest;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.dao.BaseDaoImpl;
import com.dao.BaseDaoImplT;
import com.dao.IBaseDao;
import com.dao.IBaseDaoT;
import com.dto.Book;
import com.dto.Student;

public class TestBaseDaoImpl
{
	private Random r = new Random();

	@Before
	public void setUp() throws Exception
	{
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

}
