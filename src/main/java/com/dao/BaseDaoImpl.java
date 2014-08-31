package com.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class BaseDaoImpl extends JdbcTemplate implements IBaseDao
{

	public Serializable save(final Object obj)
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

		return this.template(new ICallBack<Integer>() {

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

	@SuppressWarnings("rawtypes")
	public Serializable delete(final Class type, final int id)
	{
		String tableName = type.getSimpleName();
		StringBuilder sb = new StringBuilder("DELETE FROM " + tableName);
		sb.append(" WHERE ID=?");
		final String sql = sb.toString();

		return this.template(new ICallBack<Integer>() {

			public Integer doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.err.println("SQL: " + sql);
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, id);

				return pstm.executeUpdate();
			}
		});

	}

	public Serializable update(final Object obj)
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

		return this.template(new ICallBack<Integer>() {

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

	@SuppressWarnings("rawtypes")
	public Object get(final Class type, final int id)
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

		return this.template(new ICallBack<Object>() {

			public Object doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.err.println("SQL: " + sql);
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, id);
				rs = pstm.executeQuery();

				Object obj = type.newInstance();
				if (rs.next())
				{
					// for (Field field : fields)
					// {
					// if (!field.isAccessible())
					// {
					// field.setAccessible(true);
					// }
					//
					// field.set(obj, rs.getObject(field.getName()));
					// }

					ResultSetMetaData rsmd = rs.getMetaData();
					String columnName = StringUtils.EMPTY;
					for (int i = 1; i <= rsmd.getColumnCount(); i++)
					{
						columnName = rsmd.getColumnName(i).toLowerCase();
						Field field = type.getDeclaredField(columnName);
						if (!field.isAccessible())
						{
							field.setAccessible(true);
						}
						field.set(obj, rs.getObject(columnName));
					}
				}

				return obj;

			}
		});

	}

	@SuppressWarnings("rawtypes")
	public List<? extends Object> findAll(final Class type)
	{
		final List<Object> dataLs = new ArrayList<Object>();
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

		return this.template(new ICallBack<List<? extends Object>>() {

			public List<? extends Object> doInCallBack(Connection conn, PreparedStatement pstm, ResultSet rs) throws Throwable
			{
				System.out.println("SQL: " + sql);
				pstm = conn.prepareStatement(sql);
				rs = pstm.executeQuery();

				while (rs.next())
				{
					Object obj = type.newInstance();
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

}
