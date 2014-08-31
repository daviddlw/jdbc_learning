package com.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.commons.ConnectionFactory;
import com.dao.JdbcTemplate.ICallBack;
import com.dto.DataAccessException;

/**
 * JDBC通用模板
 * 
 * @author pc
 * 
 */
public class JdbcTemplateT<T>
{

	public Serializable save(final T obj)
	{
		// TODO Auto-generated method stub
		String tableName = obj.getClass().getSimpleName();
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(tableName);
		sb.append(" (");
		List<String> fieldLs = new ArrayList<String>();
		List<String> valueLs = new ArrayList<String>();
		final Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			if (!field.getName().equalsIgnoreCase("id"))
			{
				fieldLs.add(field.getName());
			}
		}

		sb.append(StringUtils.join(fieldLs, ","));
		sb.append(") VALUES (");

		for (Field field : fields)
		{
			if (!field.getName().equalsIgnoreCase("id"))
			{
				valueLs.add("?");
			}
		}

		sb.append(StringUtils.join(valueLs, ","));
		sb.append(")");

		final String sql = sb.toString();

		return template(new ICallBack<Integer>() {

			int index = 1;

			public Integer doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.err.println("SQL: " + sql);
				pstm = conn.prepareStatement(sql);
				for (Field field : fields)
				{
					if (!field.getName().equalsIgnoreCase("id"))
					{
						if (!field.isAccessible())
						{
							field.setAccessible(true);
						}

						pstm.setObject(index, field.get(obj));
						index++;
					}
				}

				int row = pstm.executeUpdate();
				// TODO Auto-generated method stub
				return row;
			}
		});

	}

	public Serializable delete(Class<T> type, final int id)
	{
		String tableName = type.getSimpleName();
		StringBuilder sb = new StringBuilder("DELETE FROM " + tableName);
		sb.append(" WHERE ID=?");
		final String sql = sb.toString();

		return template(new ICallBack<Integer>() {

			public Integer doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.err.println("SQL: " + sql);
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, id);

				return pstm.executeUpdate();
			}
		});

	}

	public Serializable update(final T obj)
	{
		Class<? extends Object> type = obj.getClass();
		String tableName = type.getSimpleName();
		StringBuilder sb = new StringBuilder("UPDATE " + tableName + " SET ");
		List<String> fieldLs = new ArrayList<String>();
		final Field[] fields = obj.getClass().getDeclaredFields();
		final Map<String, Integer> fieldMap = new TreeMap<String, Integer>();
		int index = 1;
		for (Field field : fields)
		{
			if (!field.getName().equalsIgnoreCase("id"))
			{
				fieldLs.add(String.format(" %s=?", field.getName()));
				if (!fieldMap.containsKey(field.getName().toLowerCase()))
				{
					fieldMap.put(field.getName().toLowerCase(), index);
					index++;
				}
			}
		}
		sb.append(StringUtils.join(fieldLs, ","));
		sb.append(" WHERE ID=?");
		fieldMap.put("id", index);

		final String sql = sb.toString();

		return template(new ICallBack<Integer>() {

			public Integer doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.err.println("SQL: " + sql);
				// TODO Auto-generated method stub
				pstm = conn.prepareStatement(sql);

				for (Field field : fields)
				{
					if (!field.isAccessible())
					{
						field.setAccessible(true);
					}
					if (fieldMap.containsKey(field.getName().toLowerCase()))
					{
						pstm.setObject(fieldMap.get(field.getName().toLowerCase()), field.get(obj));
					}
				}

				return pstm.executeUpdate();
			}
		});
	}

	public T get(final Class<T> type, final int id)
	{
		String tableName = type.getSimpleName();
		StringBuilder sb = new StringBuilder("SELECT ");
		List<String> fieldLs = new ArrayList<String>();
		final Field[] fields = type.getDeclaredFields();
		for (Field field : fields)
		{
			fieldLs.add(field.getName());
		}

		sb.append(StringUtils.join(fieldLs, ","));
		sb.append(" FROM " + tableName);
		sb.append(" WHERE ID=?");

		final String sql = sb.toString();

		return template(new ICallBack<T>() {

			public T doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.err.println("SQL: " + sql);
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, id);
				rs = pstm.executeQuery();

				T obj = type.newInstance();
				if (rs.next())
				{
					for (Field field : fields)
					{
						if (!field.isAccessible())
						{
							field.setAccessible(true);
						}

						field.set(obj, rs.getObject(field.getName()));
					}
				}

				return obj;

			}
		});

	}

	public List<T> findAll(final Class<T> type)
	{
		final List<T> dataLs = new ArrayList<T>();
		String tableName = type.getSimpleName();
		StringBuilder sb = new StringBuilder("SELECT ");
		List<String> fieldLs = new ArrayList<String>();
		final Field[] fields = type.getDeclaredFields();
		for (Field field : fields)
		{
			fieldLs.add(field.getName());
		}

		sb.append(StringUtils.join(fieldLs, ","));
		sb.append(" FROM " + tableName);

		final String sql = sb.toString();

		return template(new ICallBack<List<T>>() {
			
			public List<T> doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.out.println("SQL: " + sql);
				pstm = conn.prepareStatement(sql);
				rs = pstm.executeQuery();

				while (rs.next())
				{
					T obj = type.newInstance();
					for (Field field : fields)
					{
						if (!field.isAccessible())
						{
							field.setAccessible(true);
						}

						field.set(obj, rs.getObject(field.getName()));
					}

					dataLs.add(obj);
				}

				return dataLs;
			}
		});
	}
	
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
	@SuppressWarnings("hiding")
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
