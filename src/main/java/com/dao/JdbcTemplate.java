package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.commons.ConnectionFactory;
import com.dto.DataAccessException;

/**
 * JDBC通用模板
 * 
 * @author pc
 * 
 */
public abstract class JdbcTemplate
{
	/**
	 * 执行JDBC操作的通用接口
	 * 
	 * @author pc
	 * 
	 * @param <T>
	 *            返回泛型T参数
	 */
	protected interface ICallBack<T>
	{
		T doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable;
	}

	/**
	 * JDBC通用模板
	 * 
	 * @param callBack
	 *            回调接口实体
	 * @return 实体方法
	 */
	public <T> T template(ICallBack<T> callBack) throws DataAccessException
	{
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;

		T result = null;
		try
		{
			result = callBack.doInCallBack(conn, pstm, rs);
		} catch (Throwable e)
		{
			throw new DataAccessException(e);
			// TODO: handle exception
		} finally
		{
			ConnectionFactory.close(conn, pstm, rs);
		}
		return result;
	}
}
