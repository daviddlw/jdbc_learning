package com.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.commons.ConnectionFactory;
import com.dto.Book;

public class BookDaoImpl extends JdbcTemplate implements IBookDao
{
	private Connection conn = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;

	public Serializable save(final Book book)
	{
		final String sql = "INSERT INTO BOOK (NAME, AUTHOR, AMOUNT, PRICE) VALUES (?, ?, ?, ?)";
		return this.template(new ICallBack<Integer>() {

			public Integer doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws SQLException
			{
				pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				pstm.setString(1, book.getName());
				pstm.setString(2, book.getAuthor());
				pstm.setInt(3, book.getAmount());
				pstm.setDouble(4, book.getPrice());

				pstm.executeUpdate();

				rs = pstm.getGeneratedKeys();

				int autoGeneratedId = 0;
				if (rs.next())
				{
					autoGeneratedId = rs.getInt(1);
				}
				
				return autoGeneratedId;
			}

		});
	}

	public Serializable update(final Book book)
	{
		final String sql = "UPDATE BOOK SET NAME=?, AUTHOR=?, AMOUNT=?, PRICE=? WHERE ID=?";
		return this.template(new ICallBack<Integer>() {

			public Integer doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws SQLException
			{
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, book.getName());
				pstm.setString(2, book.getAuthor());
				pstm.setInt(3, book.getAmount());
				pstm.setDouble(4, book.getPrice());
				pstm.setInt(5, book.getId());

				return pstm.executeUpdate();
			}

		});
	}

	public Serializable delete(final Serializable id)
	{
		final String sql = "DELETE FROM BOOK WHERE ID=?";
		return this.template(new ICallBack<Integer>() {

			public Integer doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws SQLException
			{
				pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				pstm.setObject(1, id);
				
				return pstm.executeUpdate();
			}
		});
	}

	public Book get(final Serializable id)
	{
		final String sql = "SELECT ID, NAME, AUTHOR, AMOUNT, PRICE FROM BOOK WHERE ID=?";
		return this.template(new ICallBack<Book>() {

			public Book doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws SQLException
			{
				pstm = conn.prepareStatement(sql);
				pstm.setObject(1, id);

				rs = pstm.executeQuery();

				Book book = null;
				if (rs.next())
				{
					book = new Book();
					book.setId(rs.getInt("ID"));
					book.setName(rs.getString("NAME"));
					book.setAuthor(rs.getString("AUTHOR"));
					book.setAmount(rs.getInt("AMOUNT"));
					book.setPrice(rs.getDouble("PRICE"));
				}

				return book;
				// TODO Auto-generated method stub
			}
		});
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
