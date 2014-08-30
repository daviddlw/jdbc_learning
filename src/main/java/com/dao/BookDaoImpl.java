package com.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.commons.ConnectionFactory;
import com.dto.Book;

public class BookDaoImpl implements IBookDao
{
	private Connection conn = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;
	
	public Serializable save(Book student)
	{
		return null;
	}

	public void update(Book student)
	{
	}

	public void delete(Serializable id)
	{
	}

	public Book get(Serializable id)
	{
		return null;
	}

	public List<Book> findAll()
	{
		List<Book> ls = new ArrayList<Book>();
		try
		{
			conn = ConnectionFactory.getConnection();
			String sql = "SELECT ID, NAME, AUTHOR, AMOUNT, PRICE FROM BOOK";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next())
			{
				Book book = new Book();
				book.setId(rs.getInt("ID"));
				book.setName(rs.getString("NAME"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setAmount(rs.getInt("AMOUNT"));
				book.setPrice(rs.getDouble("PRICE"));
				
				ls.add(book);
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
