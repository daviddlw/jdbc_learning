package com.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.commons.ConnectionFactory;
import com.dto.Student;

public class StudentDaoImpl implements IStudentDao
{
	private Connection conn = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;

	public Serializable save(Student student)
	{
		int returnId = 0;
		try
		{
			conn = ConnectionFactory.getConnection();
			String sql = "INSERT INTO STUDENT (NAME, CREATETIME) VALUES (?, ?)";
			pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, student.getName());
			pstm.setTimestamp(2, new Timestamp(new Date().getTime()));
			pstm.executeUpdate();
			rs = pstm.getGeneratedKeys();
			if (rs.next())
			{
				returnId = rs.getInt(1);
				System.err.println("Id: " + returnId);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		} finally
		{
			ConnectionFactory.close(conn, pstm, rs);
		}

		return returnId;
	}

	public void update(Student student)
	{
		try
		{
			conn = ConnectionFactory.getConnection();
			String sql = "UPDATE STUDENT SET NAME=? WHERE ID = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, student.getName());			
			pstm.setInt(2, student.getId());
			
			pstm.executeUpdate();

		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		} finally
		{
			ConnectionFactory.close(conn, pstm, rs);
		}
	}

	public void delete(Serializable id)
	{
		try
		{
			conn = ConnectionFactory.getConnection();
			String sql = "DELETE FROM STUDENT WHERE ID = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setObject(1, id);

			pstm.executeUpdate();

		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		} finally
		{
			ConnectionFactory.close(conn, pstm, rs);
		}
	}

	public Student get(Serializable id)
	{
		Student stu = new Student();
		try
		{
			conn = ConnectionFactory.getConnection();
			String sql = "SELECT ID, NAME FROM STUDENT WHERE ID = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setObject(1, id);

			rs = pstm.executeQuery();

			if (rs.next())
			{
				stu = new Student();
				stu.setId(rs.getInt("ID"));
				stu.setName(rs.getString("NAME"));
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		} finally
		{
			ConnectionFactory.close(conn, pstm, rs);
		}

		return stu;
	}

	public List<Student> findAll()
	{
		List<Student> ls = new ArrayList<Student>();
		try
		{
			conn = ConnectionFactory.getConnection();
			String sql = "SELECT ID, NAME FROM STUDENT";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next())
			{
				Student stu = new Student();
				stu.setId(rs.getInt("ID"));
				stu.setName(rs.getString("NAME"));
				ls.add(stu);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		} finally
		{
			ConnectionFactory.close(conn, pstm, rs);
		}

		return ls;
	}

}
