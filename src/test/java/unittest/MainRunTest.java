package unittest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mainrun.MainRun;

public class MainRunTest
{
	private long start = 0;
	private long time = 0;

	@Before
	public void setUp() throws Exception
	{
		start = System.currentTimeMillis();
//		System.err.println("start: " + start);
	}

	@After
	public void setDown() throws Exception
	{
		long end = System.currentTimeMillis();
//		System.err.println("end: " + end);
		time = end - start;
		System.err.println("time: " + time);
	}

	@Test
	public void testSelectById()
	{
		MainRun.testGetDataFromDB();
	}

}
